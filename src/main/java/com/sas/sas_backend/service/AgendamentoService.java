package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.AgendamentoDto;
import com.sas.sas_backend.dtos.AgendamentoExameDto;
import com.sas.sas_backend.dtos.response.AgendamentoResponse;
import com.sas.sas_backend.exceptions.agendamento.AgendamentoConflictException;
import com.sas.sas_backend.exceptions.agendamento.InvalidEndTimeException;
import com.sas.sas_backend.exceptions.agendamento.MinimumDurationException;
import com.sas.sas_backend.exceptions.agendamento.OutsideBusinessHoursException;
import com.sas.sas_backend.exceptions.paciente.PacienteNotFoundException;
import com.sas.sas_backend.exceptions.profissionalDeSaude.ProfissionalDeSaudeNotFoundException;
import com.sas.sas_backend.exceptions.unidadeDeSaude.UnidadeDeSaudeNotFoundException;
import com.sas.sas_backend.mappers.AgendamentoMapper;
import com.sas.sas_backend.mappers.ExameAgendamentoMapper;
import com.sas.sas_backend.models.Agendamento;
import com.sas.sas_backend.models.Paciente;
import com.sas.sas_backend.models.ProfissionalDeSaude;
import com.sas.sas_backend.models.UnidadeDeSaude;
import com.sas.sas_backend.repository.AgendamentoRepository;
import com.sas.sas_backend.repository.PacienteRepository;
import com.sas.sas_backend.repository.ProfissionalDeSaudeRepository;
import com.sas.sas_backend.repository.UnidadeDeSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional; // Importação adicionada

@Service
@RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalDeSaudeRepository profissionalRepository;
    private final UnidadeDeSaudeRepository unidadeRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final ExameAgendamentoMapper exameAgendamentoMapper; // Mantido, mas veremos a melhoria

    private static final LocalTime HORA_ABERTURA = LocalTime.of(8, 0);
    private static final LocalTime HORA_FECHAMENTO = LocalTime.of(18, 0);
    private static final Duration DURACAO_MINIMA = Duration.ofMinutes(30);

    @Transactional
    public AgendamentoResponse criarAgendamento(AgendamentoDto dto) {
        // Validações de horário do agendamento
        validarHorarioAgendamento(dto.dataHoraInicio(), dto.dataHoraFim());

        // Busca as entidades relacionadas, garantindo que existam
        Paciente paciente = buscarPaciente(dto.pacienteCpf());
        ProfissionalDeSaude profissional = buscarProfissional(dto.profissionalNumero());
        UnidadeDeSaude unidade = buscarUnidade(dto.unidadeCnpj());

        // Validação de disponibilidade APÓS buscar as entidades para evitar exceções desnecessárias
        validarDisponibilidade(dto.dataHoraInicio(), dto.dataHoraFim(), paciente.getId(), profissional.getId());

        // Mapeia o DTO para a entidade Agendamento
        Agendamento agendamento = agendamentoMapper.toAgendamento(dto);
        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setUnidadeDeSaude(unidade);

        // Salva o agendamento e retorna a resposta
        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional // A anotação @Transactional está correta aqui
    public Agendamento criarAgendamentoParaExame(AgendamentoExameDto dto) {
        // Validações de horário do agendamento para exame (reutilizando a lógica existente)
        validarHorarioAgendamento(dto.horaExame(), dto.horaExame().plus(DURACAO_MINIMA)); // Assumindo duração mínima para exames

        // Busca as entidades relacionadas
        Paciente paciente = buscarPaciente(dto.pacienteCpf());
        ProfissionalDeSaude profissional = buscarProfissional(dto.profissionalNumero());
        // A Unidade de Saúde não está no AgendamentoExameDto, pode ser um ponto a revisar se é necessária.
        // Por enquanto, usaremos uma unidade padrão ou a unidade do profissional, se essa for a regra.
        // Para este exemplo, vou buscar a unidade do profissional para simplificar, se ela existir.
        UnidadeDeSaude unidade = profissional.getUnidadeDeSaude();
        if (unidade == null) {
            // Lançar exceção ou buscar uma unidade padrão, dependendo da regra de negócio
            throw new UnidadeDeSaudeNotFoundException("Profissional não está associado a uma unidade de saúde.");
        }

        // Validação de disponibilidade
        validarDisponibilidade(dto.horaExame(), dto.horaExame().plus(DURACAO_MINIMA), paciente.getId(), profissional.getId());

        // Mapeia AgendamentoExameDto para Agendamento.
        // O mapeador ExameAgendamentoMapper.toResponse(dto) está mapeando AgendamentoExameDto para Agendamento.
        // O nome toResponse é confuso aqui, como notado na análise do mapper.
        // Vamos renomear no mapper para `toAgendamento` para clareza e chamar aqui.
        Agendamento agendamento = exameAgendamentoMapper.toAgendamento(dto); // Assumindo renomeação no mapper
        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setUnidadeDeSaude(unidade);
        agendamento.setStatus(com.sas.sas_backend.models.enumerated.StatusAgendamento.AGENDADO); // Define status inicial para agendamento de exame
        agendamento.setObservacoes("Agendamento para exame: " + dto.tipoExame() + (dto.descricao() != null ? " - " + dto.descricao() : ""));
        agendamento.setDataHoraInicio(dto.horaExame());
        agendamento.setDataHoraFim(dto.horaExame().plus(DURACAO_MINIMA)); // Define um fim com base na duração mínima

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> buscarAgendamentosDoDia(String profissionalNumero, String pacienteCpf, LocalDate data) {
        // Considerar a possibilidade de que um ou ambos os parâmetros podem ser nulos,
        // dependendo da flexibilidade desejada para a busca.
        // A sua query method atual tenta combinar ambos, o que pode não ser o que você quer.
        // Se você quer todos os agendamentos para um profissional OU um paciente no dia,
        // a query method deve refletir isso.
        // Para a query method atual funcionar como "OU", você precisa de ambos os IDs válidos.
        // Caso contrário, considere duas chamadas separadas ou uma query JPQL mais flexível.

        LocalDateTime inicioDia = data.atTime(HORA_ABERTURA); // Usar a constante
        LocalDateTime fimDia = data.atTime(HORA_FECHAMENTO); // Usar a constante

        return agendamentoRepository.findByProfissionalNumeroRegistroOrPacienteCpfAndDataHoraInicioBetween(
                profissionalNumero,
                pacienteCpf,
                inicioDia,
                fimDia
        );
    }

    private void validarHorarioAgendamento(LocalDateTime inicio, LocalDateTime fim) {
        if (fim.isBefore(inicio)) {
            throw new InvalidEndTimeException("Horário de término (" + fim + ") deve ser após o início (" + inicio + ").");
        }
        if (Duration.between(inicio, fim).compareTo(DURACAO_MINIMA) < 0) {
            throw new MinimumDurationException(String.format("Duração mínima do agendamento deve ser de %d minutos. Duração atual: %d minutos.",
                    DURACAO_MINIMA.toMinutes(), Duration.between(inicio, fim).toMinutes()));
        }
        if (inicio.toLocalTime().isBefore(HORA_ABERTURA) || fim.toLocalTime().isAfter(HORA_FECHAMENTO)) {
            throw new OutsideBusinessHoursException(String.format("Horário fora do expediente. O atendimento é das %s às %s.",
                    HORA_ABERTURA.toString(), HORA_FECHAMENTO.toString()));
        }
    }

    private void validarDisponibilidade(LocalDateTime inicio, LocalDateTime fim,
                                        String pacienteId, String profissionalId) {
        boolean conflitoPaciente = agendamentoRepository
                .existsByPacienteIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                        pacienteId, fim, inicio);

        if (conflitoPaciente) {
            throw new AgendamentoConflictException("Paciente já possui agendamento neste horário.");
        }

        boolean conflitoProfissional = agendamentoRepository
                .existsByProfissionalIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                        profissionalId, fim, inicio);

        if (conflitoProfissional) {
            throw new AgendamentoConflictException("Profissional de saúde já possui agendamento neste horário.");
        }
    }

    // Métodos de busca de entidades, preferencialmente encapsulados ou delegados a outros serviços.
    // Mantidos aqui para ilustrar a lógica, mas idealmente seriam privados ou em serviços específicos.
    private Paciente buscarPaciente(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new PacienteNotFoundException(String.format("Paciente com CPF '%s' não encontrado.", cpf)));
    }

    private ProfissionalDeSaude buscarProfissional(String registro) {
        return profissionalRepository.findByNumeroRegistro(registro)
                .orElseThrow(() -> new ProfissionalDeSaudeNotFoundException(String.format("Profissional com número de registro '%s' não encontrado.", registro)));
    }

    private UnidadeDeSaude buscarUnidade(String cnpj) {
        return unidadeRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new UnidadeDeSaudeNotFoundException(String.format("Unidade de saúde com CNPJ '%s' não encontrada.", cnpj)));
    }
}