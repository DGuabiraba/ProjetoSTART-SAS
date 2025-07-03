package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.response.TokenValidationResponse;
import com.sas.sas_backend.exceptions.token.InvalidTokenException;
import io.jsonwebtoken.Claims; // Importação adicionada
import io.jsonwebtoken.ExpiredJwtException; // Importação adicionada
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException; // Importação adicionada
import io.jsonwebtoken.UnsupportedJwtException; // Importação adicionada
import io.jsonwebtoken.security.SignatureException; // Importação adicionada
import org.springframework.beans.factory.annotation.Value; // Adicionado para carregar a chave secreta de application.properties
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map; // Mudança para Map para maior clareza

@Service
public class TokenService {

    // CARREGAR DE VARIÁVEL DE AMBIENTE OU application.properties
    // Ex: jwt.secret=SEU_SECRET_AQUI_DEVE_SER_MUITO_LONGO_E_COMPLEXO
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Usar Map para clareza
    // ATENÇÃO: Em produção, isso deve ser um cache distribuído (ex: Redis)
    private final Map<String, String> forbiddenTokens = new HashMap<>();

    // O tempo de expiração do token (em horas)
    @Value("${jwt.expiration.hours:12}") // Valor padrão de 12 horas
    private long jwtExpirationHours;

    public String gerarToken(String id, String role) {
        // Remover o token antigo do usuário, se houver um na blacklist
        // A chave na blacklist deve ser construída de forma a ser única por usuário+sessão ou apenas usuário.
        // Se cada login gera um novo token válido e o anterior é invalidado, a chave pode ser apenas o ID do usuário.
        // Se múltiplos tokens por usuário são permitidos, a blacklist precisa ser mais complexa.
        // Por simplicidade, vou usar a chave buildCacheSufix para a blacklist.
        String cacheKey = buildCacheSufix(id, role);
        forbiddenTokens.remove(cacheKey); // Remove a entrada antiga para o mesmo usuário/role

        return Jwts.builder()
                .subject(id)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(jwtExpirationHours, ChronoUnit.HOURS)))
                .signWith(getSecretKey())
                .compact();
    }

    public TokenValidationResponse validarTokenRetornandoDados(String token) {
        try {
            var parser = Jwts.parser().verifyWith(getSecretKey()).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();

            String id = claims.getSubject();
            String role = claims.get("role", String.class);

            // Verifica se o token está na blacklist
            String cacheKey = buildCacheSufix(id, role); // Correção: Usar id e role na ordem correta
            if (forbiddenTokens.containsKey(cacheKey)) {
                throw new InvalidTokenException("Token inválido (deslogado ou revogado).");
            }

            return new TokenValidationResponse(id, role, true);

        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token expirado.", e);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // Captura diferentes tipos de exceções de JWT para mensagens mais específicas se desejar
            throw new InvalidTokenException("Token inválido.", e);
        }
    }

    public void logout(String token) {
        Claims payload;
        try {
            payload = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            // Se o token já expirou, ele não pode ser deslogado novamente.
            // Poderíamos lançar uma exceção específica ou apenas ignorar.
            throw new InvalidTokenException("Token já expirado e não pode ser deslogado.", e);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Token inválido para logout.", e);
        }

        String userId = payload.getSubject();
        String role = payload.get("role", String.class);

        String cacheKey = buildCacheSufix(userId, role); // Correção da ordem dos parâmetros
        if (forbiddenTokens.containsKey(cacheKey)) {
            throw new InvalidTokenException("Usuário já deslogado (token na blacklist)."); // Mensagem mais clara
        }

        // Adiciona o token à blacklist, associando-o ao usuário e papel.
        // O valor do token pode ser guardado, ou apenas um placeholder.
        // O importante é que a chave (userId + role) esteja lá.
        forbiddenTokens.put(cacheKey, token); // Guarda o token inteiro ou apenas um marcador
    }

    // Correção da ordem dos parâmetros para ser consistente com o uso
    private String buildCacheSufix(String id, String role) {
        return role + "_" + id;
    }

    private SecretKey getSecretKey() {
        // Garantir que a chave secreta tenha comprimento suficiente para HMACSHA256 (mínimo de 32 bytes ou 256 bits)
        if (jwtSecret.getBytes().length < 32) {
            throw new IllegalStateException("JWT Secret Key must be at least 32 bytes (256 bits) long for HmacSHA256.");
        }
        return new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    }
}