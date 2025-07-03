package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.UnidadeDeSaudeDto;
import com.sas.sas_backend.exceptions.paciente.CredentialsNotMatchException;
import com.sas.sas_backend.exceptions.unidadeDeSaude.UnidadeDeSaudeAlreadyExistsException;
import com.sas.sas_backend.exceptions.unidadeDeSaude.UnidadeDeSaudeNotFoundException;
import com.sas.sas_backend.mappers.UnidadeDeSaudeMapper;
import com.sas.sas_backend.models.UnidadeDeSaude;
import com.sas.sas_backend.models.enumerated.RolesEnum;
import com.sas.sas_backend.repository.UnidadeDeSaudeRepository;
import com.sas.sas_backend.utils.password.HashPassword;
import com.sas.sas_backend.utils.password.PasswordHashResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnidadeDeSaudeService {

    private final UnidadeDeSaudeRepository unidadeDeSaudeRepository;
    private final UnidadeDeSaudeMapper unidadeDeSaudeMapper;
    private final TokenService tokenService;

    @Transactional
    public UnidadeDeSaudeDto create(UnidadeDeSaudeDto unidadeDeSaudeDto) {
        // Verifica se a unidade já existe pelo CNPJ ou Email
        unidadeDeSaudeRepository.findByCnpj(unidadeDeSaudeDto.cnpj())
                .ifPresent(u -> {
                    throw new UnidadeDeSaudeAlreadyExistsException(String.format("Unidade de saúde com CNPJ '%s' já cadastrada.", unidadeDeSaudeDto.cnpj()));
                });
        // Opcional: verificar também por email para evitar duplicidade de email
        unidadeDeSaudeRepository.findByEmail(unidadeDeSaudeDto.email())
                .ifPresent(u -> {
                    throw new UnidadeDeSaudeAlreadyExistsException(String.format("Unidade de saúde com email '%s' já cadastrada.", unidadeDeSaudeDto.email()));
                });


        UnidadeDeSaude unidade = unidadeDeSaudeMapper.toUnidade(unidadeDeSaudeDto);

        PasswordHashResponse passwordHashResponse = HashPassword.hashPassword(unidadeDeSaudeDto.senha());
        unidade.setSenha(passwordHashResponse.hash());
        unidade.setSalt(passwordHashResponse.salt());

        UnidadeDeSaude savedUnidade = unidadeDeSaudeRepository.save(unidade);
        return unidadeDeSaudeMapper.toDto(savedUnidade);
    }

    public String loginUnidade(String email, String password) {
        UnidadeDeSaude user = unidadeDeSaudeRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialsNotMatchException("Email ou senha inválidos.")); // Mensagem genérica por segurança

        boolean passwordMatch = HashPassword.checkPassword(password, user.getSenha(), user.getSalt());
        if (!passwordMatch) {
            throw new CredentialsNotMatchException("Email ou senha inválidos."); // Mensagem genérica
        }
        return tokenService.gerarToken(user.getId(), RolesEnum.UNIDADE.getNome());
    }

    @Transactional
    public void alterarSenha(String id, String senhaAtual, String novaSenha) {
        UnidadeDeSaude unidadeDeSaude = unidadeDeSaudeRepository.findById(id)
                .orElseThrow(() -> new UnidadeDeSaudeNotFoundException(String.format("Unidade de Saúde com ID '%s' não encontrada.", id)));

        boolean senhaCorreta = HashPassword.checkPassword(senhaAtual, unidadeDeSaude.getSenha(), unidadeDeSaude.getSalt());
        if (!senhaCorreta) {
            throw new CredentialsNotMatchException("Senha atual incorreta."); // Mensagem mais específica
        }

        PasswordHashResponse novaHash = HashPassword.hashPassword(novaSenha);
        unidadeDeSaude.setSenha(novaHash.hash());
        unidadeDeSaude.setSalt(novaHash.salt());

        unidadeDeSaudeRepository.save(unidadeDeSaude);
    }

    // Método para buscar unidade (pode ser útil para outros serviços)
    public UnidadeDeSaudeDto buscarUnidadePorCnpj(String cnpj) {
        UnidadeDeSaude unidade = unidadeDeSaudeRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new UnidadeDeSaudeNotFoundException(String.format("Unidade de saúde com CNPJ '%s' não encontrada.", cnpj)));
        return unidadeDeSaudeMapper.toDto(unidade);
    }
}