package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.ProfissionalDeSaudeDto;
import com.sas.sas_backend.dtos.response.ProfissionalDeSaudeResponse;
import com.sas.sas_backend.exceptions.paciente.CredentialsNotMatchException;
import com.sas.sas_backend.exceptions.profissionalDeSaude.ProfissionalDeSaudeAlreadyExistsException;
import com.sas.sas_backend.exceptions.profissionalDeSaude.ProfissionalDeSaudeNotFoundException;
import com.sas.sas_backend.exceptions.unidadeDeSaude.UnidadeDeSaudeNotFoundException; // Adicionado
import com.sas.sas_backend.mappers.ProfissionalDeSaudeMapper;
import com.sas.sas_backend.models.ProfissionalDeSaude;
import com.sas.sas_backend.models.UnidadeDeSaude;
import com.sas.sas_backend.models.enumerated.RolesEnum;
import com.sas.sas_backend.repository.ProfissionalDeSaudeRepository;
import com.sas.sas_backend.repository.UnidadeDeSaudeRepository;
import com.sas.sas_backend.utils.password.HashPassword;
import com.sas.sas_backend.utils.password.PasswordHashResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.RequestBody; // REMOVIDO: não pertence à camada de serviço

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importação adicionada

@Service
@RequiredArgsConstructor
public class ProfissionalDeSaudeService {

    private final ProfissionalDeSaudeRepository profissionalDeSaudeRepository;
    private final UnidadeDeSaudeRepository unidadeDeSaudeRepository;
    private final ProfissionalDeSaudeMapper profissionalDeSaudeMapper;
    private final TokenService tokenService;

    public ProfissionalDeSaudeDto buscarPorNome(String nome) {
        ProfissionalDeSaude profissional = profissionalDeSaudeRepository.findByNome(nome)
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional de saúde com nome '%s' não encontrado.", nome)));
        return profissionalDeSaudeMapper.toProfissionalDeSaudeDto(profissional);
    }

    public String loginProfissional(String email, String password) {
        ProfissionalDeSaude user = profissionalDeSaudeRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialsNotMatchException("Email ou senha inválidos."));

        boolean passwordMatch = HashPassword.checkPassword(password, user.getSenha(), user.getSalt());
        if (!passwordMatch) {
            throw new CredentialsNotMatchException("Email ou senha inválidos.");
        }
        return tokenService.gerarToken(user.getId(), RolesEnum.PROFISSIONAL.getNome());
    }

    @Transactional
    public ProfissionalDeSaudeResponse cadastrarProfissionalDeSaude(ProfissionalDeSaudeDto dto) {
        // Verifica se o profissional já existe pelo número de registro
        profissionalDeSaudeRepository.findByNumeroRegistro(dto.numeroRegistro())
                .ifPresent(p -> {
                    throw new ProfissionalDeSaudeAlreadyExistsException(String.format("Profissional já cadastrado com o número de registro: '%s'.", dto.numeroRegistro()));
                });

        // Busca a unidade de saúde pelo CNPJ
        UnidadeDeSaude unidade = unidadeDeSaudeRepository.findByCnpj(dto.unidadeCnpj())
                .orElseThrow(() -> new UnidadeDeSaudeNotFoundException(String.format("Unidade de saúde com CNPJ '%s' não encontrada.", dto.unidadeCnpj())));

        // Mapeia o DTO para a entidade ProfissionalDeSaude
        ProfissionalDeSaude profissional = profissionalDeSaudeMapper.toProfissionalDeSaude(dto);

        // Gera hash e salt para a senha
        PasswordHashResponse passwordHashResponse = HashPassword.hashPassword(dto.senha());
        profissional.setSenha(passwordHashResponse.hash());
        profissional.setSalt(passwordHashResponse.salt());

        // Associa a unidade de saúde encontrada
        profissional.setUnidadeDeSaude(unidade);

        // Salva o profissional e retorna a resposta mapeada
        ProfissionalDeSaude savedProfissional = profissionalDeSaudeRepository.save(profissional);
        return profissionalDeSaudeMapper.toResponse(savedProfissional);
    }

    @Transactional
    public void alterarSenha(String id, String senhaAtual, String novaSenha) {
        ProfissionalDeSaude profissionalDeSaude = profissionalDeSaudeRepository.findById(id)
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional de saúde com ID '%s' não encontrado.", id)));

        boolean senhaCorreta = HashPassword.checkPassword(senhaAtual, profissionalDeSaude.getSenha(), profissionalDeSaude.getSalt());
        if (!senhaCorreta) {
            throw new CredentialsNotMatchException("Senha atual incorreta.");
        }

        PasswordHashResponse novaHash = HashPassword.hashPassword(novaSenha);
        profissionalDeSaude.setSenha(novaHash.hash());
        profissionalDeSaude.setSalt(novaHash.salt());

        profissionalDeSaudeRepository.save(profissionalDeSaude);
    }

    public List<ProfissionalDeSaudeDto> buscarTodos() {
        List<ProfissionalDeSaude> profissionais = profissionalDeSaudeRepository.findAll();
        // Retorna lista vazia se não encontrar, em vez de lançar exceção
        return profissionais.stream().map(profissionalDeSaudeMapper::toProfissionalDeSaudeDto).collect(Collectors.toList());
    }

    @Transactional
    public ProfissionalDeSaudeDto atualizarProfissionalDeSaude(String id, ProfissionalDeSaudeDto dto) {
        ProfissionalDeSaude profissionalExistente = profissionalDeSaudeRepository.findById(id)
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional de saúde com ID '%s' não encontrado para atualização.", id)));

        // Copia as propriedades do DTO para a entidade existente
        // Não atualize ID, senha ou salt aqui.
        profissionalExistente.setNome(dto.nome());
        profissionalExistente.setTelefone(dto.telefone());
        profissionalExistente.setEmail(dto.email());
        profissionalExistente.setTipoProfissional(dto.tipoProfissional());
        profissionalExistente.setNumeroRegistro(dto.numeroRegistro());

        // Se a unidade for alterada, busque e associe a nova unidade
        if (dto.unidadeCnpj() != null &&
                (profissionalExistente.getUnidadeDeSaude() == null || !dto.unidadeCnpj().equals(profissionalExistente.getUnidadeDeSaude().getCnpj()))) {
            UnidadeDeSaude novaUnidade = unidadeDeSaudeRepository.findByCnpj(dto.unidadeCnpj())
                    .orElseThrow(() -> new UnidadeDeSaudeNotFoundException(String.format("Unidade de saúde com CNPJ '%s' não encontrada para atualização do profissional.", dto.unidadeCnpj())));
            profissionalExistente.setUnidadeDeSaude(novaUnidade);
        } else if (dto.unidadeCnpj() == null && profissionalExistente.getUnidadeDeSaude() != null) {
            // Se o DTO tem CNPJ nulo, mas o profissional tem unidade, desassocia.
            profissionalExistente.setUnidadeDeSaude(null);
        }

        return profissionalDeSaudeMapper.toProfissionalDeSaudeDto(profissionalDeSaudeRepository.save(profissionalExistente));
    }

    @Transactional
    public void deletarProfissionalDeSaude(String id) {
        if (!profissionalDeSaudeRepository.existsById(id)) {
            throw new ProfissionalDeSaudeNotFoundException(String.format("Profissional de saúde com ID '%s' não encontrado para exclusão.", id));
        }
        profissionalDeSaudeRepository.deleteById(id);
    }

    public ProfissionalDeSaudeResponse buscarPorNumeroRegistro(String numero) {
        ProfissionalDeSaude profissional = profissionalDeSaudeRepository.findByNumeroRegistro(numero)
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional de saúde com número de registro '%s' não encontrado.", numero)));
        return profissionalDeSaudeMapper.toResponse(profissional);
    }
}