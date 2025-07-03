package com.sas.sas_backend.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnidadeDeSaudeDto(


        String id,

        @NotBlank
        String nome,

        @NotBlank
        String cnpj,

        @NotBlank(message = "Email é obrigatório!")
        @Email(message = "Deve ser um endereço de e-mail válido.")
        @NotBlank
        String email,

        @NotBlank
        String senha,

        @NotNull
        EnderecoDto endereco
){
}
