package com.sas.sas_backend.mappers;

import com.sas.sas_backend.dtos.ExameDto;
import com.sas.sas_backend.models.Exame;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {PacienteMapper.class, ProfissionalDeSaudeMapper.class})
public interface ExameMapper {

    @Mapping(target = "agendamento", ignore = true)
    Exame toEntity(ExameDto dto);

    ExameDto toDto(Exame entity);
}