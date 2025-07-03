package com.sas.sas_backend.mappers;

import com.sas.sas_backend.dtos.ProfissionalDeSaudeDto;
import com.sas.sas_backend.dtos.response.ProfissionalDeSaudeResponse;
import com.sas.sas_backend.models.ProfissionalDeSaude;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ProntuarioMapper.class, UnidadeDeSaudeMapper.class
})
public interface ProfissionalDeSaudeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(target = "unidadeDeSaude", ignore = true)
    ProfissionalDeSaude toProfissionalDeSaude(ProfissionalDeSaudeDto dto);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "unidadeCnpj", ignore = true)
    ProfissionalDeSaudeDto toProfissionalDeSaudeDto(ProfissionalDeSaude profissional);

    @Mapping(source = "unidadeDeSaude", target = "unidadeDeSaude")
    ProfissionalDeSaudeResponse toResponse(ProfissionalDeSaude profissionalDeSaude);
}
