package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.ProntuarioDto;
import com.sas.sas_backend.dtos.response.ProntuarioResponse;
import com.sas.sas_backend.exceptions.paciente.PacienteNotFoundException;
import com.sas.sas_backend.exceptions.profissionalDeSaude.ProfissionalDeSaudeNotFoundException;
import com.sas.sas_backend.exceptions.prontuario.DuplicateRegistrationException;
import com.sas.sas_backend.exceptions.prontuario.ProntuarioNotFoundException; // Nova exceção
import com.sas.sas_backend.mappers.ProntuarioMapper;
import com.sas.sas_backend.models.Paciente;
import com.sas.sas_backend.models.ProfissionalDeSaude;
import com.sas.sas_backend.models.Prontuario;
import com.sas.sas_backend.repository.PacienteRepository;
import com.sas.sas_backend.repository.ProfissionalDeSaudeRepository;
import com.sas.sas_backend.repository.ProntuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalDeSaudeRepository profissionalDeSaudeRepository;
    private final ProntuarioMapper prontuarioMapper;

    @Transactional
    public ProntuarioResponse create(ProntuarioDto prontuarioDto) {
        // A validação de null para pacienteCpf e profissionalNumero deve ser feita no DTO com @NotBlank
        // e @Valid no controller. Se chegou aqui e é nulo, é um erro de design ou o DTO não foi validado.
        // Se você ainda quiser verificar aqui, use Optional.ofNullable ou um if mais robusto.
        if (prontuarioDto.pacienteCpf() == null || prontuarioDto.pacienteCpf().trim().isEmpty() ||
                prontuarioDto.profissionalNumero() == null || prontuarioDto.profissionalNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do paciente e número do profissional são obrigatórios e não podem ser vazios.");
        }

        // Verifica se o paciente já possui prontuário
        prontuarioRepository.findByPaciente_Cpf(prontuarioDto.pacienteCpf())
                .ifPresent(p -> {
                    throw new DuplicateRegistrationException(String.format("Paciente com CPF '%s' já possui prontuário cadastrado.", prontuarioDto.pacienteCpf()));
                });

        // Busca o paciente
        Paciente paciente = pacienteRepository.findByCpf(prontuarioDto.pacienteCpf())
                .orElseThrow(() -> new PacienteNotFoundException(String.format("Paciente com CPF '%s' não encontrado.", prontuarioDto.pacienteCpf())));

        // Busca o profissional de saúde
        ProfissionalDeSaude profissionalDeSaude = profissionalDeSaudeRepository.findByNumeroRegistro(prontuarioDto.profissionalNumero())
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional de saúde com número de registro '%s' não encontrado.", prontuarioDto.profissionalNumero())));

        // Mapeia o DTO para a entidade Prontuario
        Prontuario prontuario = prontuarioMapper.toEntity(prontuarioDto);
        prontuario.setPaciente(paciente);
        prontuario.setProfissionalDeSaude(profissionalDeSaude);

        Prontuario savedProntuario = prontuarioRepository.save(prontuario);
        return prontuarioMapper.toResponse(savedProntuario);
    }

    public ProntuarioResponse buscarPorCpf(String cpf) {
        Prontuario prontuario = prontuarioRepository.findByPaciente_Cpf(cpf)
                .orElseThrow(() -> new ProntuarioNotFoundException(String.format("Prontuário não encontrado para o CPF '%s' informado.", cpf))); // Usar ProntuarioNotFoundException
        return prontuarioMapper.toResponse(prontuario);
    }

    @Transactional
    public ProntuarioResponse atualizar(String cpf, ProntuarioDto prontuarioDto) {
        Prontuario prontuario = prontuarioRepository.findByPaciente_Cpf(cpf)
                .orElseThrow(() -> new ProntuarioNotFoundException(String.format("Prontuário não encontrado para o CPF '%s' informado para atualização.", cpf))); // Usar ProntuarioNotFoundException

        // Busca o profissional, mesmo que ele não mude no DTO, para garantir que é válido.
        // Se o profissionalNumero for nulo no DTO e o prontuário já tem um profissional,
        // você pode manter o profissional existente ou desassociar. A lógica atual busca novamente.
        ProfissionalDeSaude profissional = profissionalDeSaudeRepository.findByNumeroRegistro(prontuarioDto.profissionalNumero())
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional de saúde com o número de registro '%s' não foi encontrado.", prontuarioDto.profissionalNumero())));

        // Atualiza os campos do prontuário com os valores do DTO
        prontuario.setDescricao(prontuarioDto.descricao());
        prontuario.setAlergias(prontuarioDto.alergias());
        prontuario.setObservacoes(prontuarioDto.observacoes());
        prontuario.setTipoSanguineo(prontuarioDto.tipoSanguineo());
        prontuario.setDoencasCronicas(prontuarioDto.doencasCronicas());
        prontuario.setHistoricoFamiliar(prontuarioDto.historicoFamiliar());

        // Associa o profissional (se mudou ou se precisa ser reassociado)
        prontuario.setProfissionalDeSaude(profissional);

        Prontuario atualizado = prontuarioRepository.save(prontuario);
        return prontuarioMapper.toResponse(atualizado);
    }
}