package com.sas.sas_backend.mappers;

import com.sas.sas_backend.dtos.AgendamentoDto;
import com.sas.sas_backend.dtos.response.AgendamentoResponse;
import com.sas.sas_backend.models.Agendamento;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        PacienteMapper.class, ProfissionalDeSaudeMapper.class, UnidadeDeSaudeMapper.class
})
public interface AgendamentoMapper {

    Agendamento toAgendamento (AgendamentoDto agendamentoDto);

    AgendamentoDto toDto (Agendamento agendamento);

    AgendamentoResponse toResponse (Agendamento agendamento);

    List<AgendamentoDto> toListDto(List<Agendamento> agendamentoList);
}
