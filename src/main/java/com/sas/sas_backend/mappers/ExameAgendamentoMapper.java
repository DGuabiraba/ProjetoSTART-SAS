package com.sas.sas_backend.mappers;

import com.sas.sas_backend.dtos.AgendamentoExameDto;
import com.sas.sas_backend.dtos.ExameDto;
import com.sas.sas_backend.dtos.response.ExameAgendamentoResponse;
import com.sas.sas_backend.models.Agendamento;
import com.sas.sas_backend.models.Exame;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AgendamentoMapper.class, PacienteMapper.class, ProfissionalDeSaudeMapper.class}) // Adicionei PacienteMapper e ProfissionalDeSaudeMapper para resolver o mapeamento de AgendamentoExameDto para Agendamento
public interface ExameAgendamentoMapper {

    ExameDto toExameDto(Exame exame);

    Exame toExame(ExameDto dto);

    @Mapping(target = "idExame", source = "exame.id")
    @Mapping(target = "tipoExame", source = "exame.tipoExame")
    @Mapping(target = "status", source = "exame.status")
    @Mapping(target = "idAgendamento", source = "agendamento.id")
    @Mapping(target = "horario", source = "agendamento.dataHoraInicio")
    @Mapping(target = "statusAgendamento", source = "agendamento.status")
    @Mapping(target = "paciente", source = "agendamento.paciente")
    @Mapping(target = "profissionalDeSaude", source = "agendamento.profissional")
    ExameAgendamentoResponse toResponse(Exame exame, Agendamento agendamento);


    // O MÉTODO CORRIGIDO:
    // Mapeamento de AgendamentoExameDto para Agendamento.
    // O DTO AgendamentoExameDto contém pacienteCpf e profissionalNumero.
    // O MapStruct, ao ver esses campos, precisará de ajuda para mapeá-los para entidades Paciente e ProfissionalDeSaude.
    // A melhor forma é fornecer um @Mapping e/ou adicionar os mappers de Paciente e Profissional na cláusula 'uses'.
    @Mapping(target = "paciente", ignore = true) // Paciente será setado manualmente no serviço
    @Mapping(target = "profissional", ignore = true) // Profissional será setado manualmente no serviço
    @Mapping(target = "unidadeDeSaude", ignore = true) // Unidade de Saúde será setada manualmente no serviço
    @Mapping(target = "dataHoraInicio", source = "horaExame")
    @Mapping(target = "dataHoraFim", expression = "java(dto.horaExame().plusMinutes(30))") // Assumindo 30 minutos de duração
    @Mapping(target = "status", constant = "AGENDADO") // Define o status padrão
    @Mapping(target = "observacoes", expression = "java(\"Agendamento para exame: \" + dto.tipoExame() + (dto.descricao() != null ? \" - \" + dto.descricao() : \"\"))")
    Agendamento toAgendamento(AgendamentoExameDto dto);
}