package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.AgendamentoExameDto;
import com.sas.sas_backend.dtos.ExameDto;
import com.sas.sas_backend.dtos.response.ExameAgendamentoResponse;
import com.sas.sas_backend.exceptions.exame.ExameNotFoundException;
import com.sas.sas_backend.exceptions.paciente.PacienteNotFoundException;
import com.sas.sas_backend.exceptions.profissionalDeSaude.ProfissionalDeSaudeNotFoundException;
import com.sas.sas_backend.mappers.ExameAgendamentoMapper; // Permanece
import com.sas.sas_backend.mappers.ExameMapper; // Novo: para mapeamento Exame <-> ExameDto
import com.sas.sas_backend.models.Agendamento;
import com.sas.sas_backend.models.Exame;
import com.sas.sas_backend.models.Paciente;
import com.sas.sas_backend.models.ProfissionalDeSaude;
import com.sas.sas_backend.models.enumerated.StatusExame;
import com.sas.sas_backend.repository.ExameRepository;
import com.sas.sas_backend.repository.PacienteRepository; // Adicionado para buscar Paciente
import com.sas.sas_backend.repository.ProfissionalDeSaudeRepository; // Adicionado para buscar Profissional
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors; // Importação adicionada

@Service
@RequiredArgsConstructor
public class ExameService {

    private final ExameRepository exameRepository;
    private final AgendamentoService agendamentoService;
    private final ExameAgendamentoMapper exameAgendamentoMapper; // Para mapear agendamento+exame para resposta
    private final ExameMapper exameMapper; // Para mapear Exame <-> ExameDto
    private final PacienteRepository pacienteRepository; // Injetado
    private final ProfissionalDeSaudeRepository profissionalRepository; // Injetado

    @Transactional
    public ExameAgendamentoResponse agendarExame(AgendamentoExameDto dto) {
        // 1. Criar e salvar o agendamento através do AgendamentoService
        Agendamento agendamento = agendamentoService.criarAgendamentoParaExame(dto);

        // 2. Buscar Paciente e Profissional para associar ao Exame
        Paciente paciente = pacienteRepository.findByCpf(dto.pacienteCpf())
                .orElseThrow(() -> new PacienteNotFoundException(String.format("Paciente com CPF '%s' não encontrado.", dto.pacienteCpf())));
        ProfissionalDeSaude profissional = profissionalRepository.findByNumeroRegistro(dto.profissionalNumero())
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional com número de registro '%s' não encontrado.", dto.profissionalNumero())));

        // 3. Criar a entidade Exame e associá-la ao agendamento
        Exame exame = new Exame();
        exame.setTipoExame(dto.tipoExame());
        exame.setDescricao(dto.descricao());
        exame.setStatus(StatusExame.AGENDADO); // Define o status como AGENDADO
        exame.setAgendamento(agendamento);
        exame.setPaciente(paciente); // Associar paciente
        exame.setProfissional(profissional); // Associar profissional
        exame.setDataSolicitacao(LocalDate.now()); // Define a data de solicitação como a data atual

        // 4. Salvar o exame
        exameRepository.save(exame);

        // 5. Atualizar o agendamento com a referência ao exame recém-criado
        // (Isso já deve ser feito no AgendamentoService ao setar agendamento.setExame(exame);
        // ou você pode fazer aqui explicitamente se a relação é bidirecional e você quer garantir)
        agendamento.setExame(exame); // Garante a consistência bidirecional
        // agendamentoRepository.save(agendamento); // Se você precisar salvar o agendamento de novo aqui. Geralmente, não é necessário se já está em uma transação.

        // 6. Retornar a resposta combinada
        return exameAgendamentoMapper.toResponse(exame, agendamento);
    }

    public List<ExameDto> listarExames() {
        List<Exame> exames = exameRepository.findAll();
        // Não lançar exceção se a lista estiver vazia, apenas retornar uma lista vazia.
        // O cliente deve lidar com a ausência de dados.
        return exames.stream().map(exameMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void removerExame(String id) {
        // Usar existsById para eficiência
        if (!exameRepository.existsById(id)) {
            throw new ExameNotFoundException(String.format("Exame com ID '%s' não encontrado para exclusão.", id));
        }
        exameRepository.deleteById(id);
    }

    @Transactional
    public ExameDto atualizarExame(String id, ExameDto dto) {
        Exame exameExistente = exameRepository.findById(id)
                .orElseThrow(() -> new ExameNotFoundException(String.format("Exame com ID '%s' não encontrado para atualização.", id)));

        // Mapear propriedades do DTO para a entidade existente para atualização
        // O mapper deve ser capaz de fazer o merge dos dados do DTO na entidade existente
        // Para isso, o ExameMapper precisaria de um método @MappingTarget
        // Por enquanto, vamos fazer manualmente ou esperar o MapStruct.
        // Se você não tem um método @MappingTarget no mapper, faça a cópia manual das propriedades
        exameExistente.setDescricao(dto.descricao());
        exameExistente.setTipoExame(dto.tipoExame());
        exameExistente.setStatus(dto.status());
        // horaExame e dataHoraFim do ExameDto precisam ser mapeados para dataRealizacao no Exame
        // ExameDto tem horaExame (LocalDateTime), Exame tem dataRealizacao (LocalDate).
        // Isso pode ser uma inconsistência.
        // Se horaExame é o horário do exame, deve ser LocalDateTime.
        // Se dataRealizacao é apenas a data, deve ser LocalDate.
        // Assumindo que horaExame do DTO se refere à dataRealizacao do Exame para o dia do exame.
        if (dto.horaExame() != null) {
            exameExistente.setDataRealizacao(dto.horaExame().toLocalDate());
        }

        // Você também precisa buscar Paciente e Profissional se eles forem alterados no DTO
        if (dto.pacienteCpf() != null && !dto.pacienteCpf().equals(exameExistente.getPaciente().getCpf())) {
            Paciente novoPaciente = pacienteRepository.findByCpf(dto.pacienteCpf())
                    .orElseThrow(() -> new PacienteNotFoundException(String.format("Novo paciente com CPF '%s' não encontrado.", dto.pacienteCpf())));
            exameExistente.setPaciente(novoPaciente);
        }
        if (dto.profissionalNumero() != null && !dto.profissionalNumero().equals(exameExistente.getProfissional().getNumeroRegistro())) {
            ProfissionalDeSaude novoProfissional = profissionalRepository.findByNumeroRegistro(dto.profissionalNumero())
                    .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Novo profissional com número de registro '%s' não encontrado.", dto.profissionalNumero())));
            exameExistente.setProfissional(novoProfissional);
        }


        // exame.setId(exameExistente.getId()); // Isso não é necessário se você está atualizando a entidade existente

        return exameMapper.toDto(exameRepository.save(exameExistente));
    }
}