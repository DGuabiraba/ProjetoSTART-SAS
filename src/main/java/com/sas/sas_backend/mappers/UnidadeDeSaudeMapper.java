package com.sas.sas_backend.mappers;

import com.sas.sas_backend.dtos.UnidadeDeSaudeDto;
import com.sas.sas_backend.models.UnidadeDeSaude;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ProfissionalDeSaudeMapper.class, EnderecoMapper.class
})
public interface UnidadeDeSaudeMapper {


    @Mapping(source = "endereco", target = "endereco")
    UnidadeDeSaudeDto toDto (UnidadeDeSaude unidadeDeSaude);

    @Mapping(source = "endereco", target = "endereco")
    UnidadeDeSaude toUnidade (UnidadeDeSaudeDto unidadeDeSaudeDto);


}
