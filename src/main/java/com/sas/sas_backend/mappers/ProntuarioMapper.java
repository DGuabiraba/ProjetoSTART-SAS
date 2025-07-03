package com.sas.sas_backend.mappers;

import com.sas.sas_backend.dtos.ProntuarioDto;
import com.sas.sas_backend.dtos.response.ProntuarioResponse;
import com.sas.sas_backend.models.Prontuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PacienteMapper.class, ProntuarioMapper.class})
public interface ProntuarioMapper {

    @Mapping(source = "tipoSanguineo", target = "tipoSanguineo")
    Prontuario toEntity(ProntuarioDto prontuarioDto);

    @Mapping(source = "tipoSanguineo", target = "tipoSanguineo")
    ProntuarioDto toDto(Prontuario prontuario);

    @Mapping(source = "paciente", target = "pacienteDto")
    ProntuarioResponse toResponse(Prontuario prontuario);
}
