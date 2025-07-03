package com.sas.sas_backend.mappers;

import com.sas.sas_backend.dtos.EnderecoDto;
import com.sas.sas_backend.models.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    Endereco toEndereco(EnderecoDto enderecoDto);

    EnderecoDto toEnderecoDto(Endereco endereco);
}
