package com.sas.sas_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDto(


        @NotBlank(message = "Rua é obrigatória!")
        @Size(max = 50, message = "A rua deve ter no máximo 50 caracteres.")
        String rua,

        @Size(max = 100, message = "O número deve ter no máximo 100 caracteres.")
        String complemento,

        @NotBlank(message = "Número é obrigatório!")
        @Size(max = 10, message = "O número deve ter no máximo 10 caracteres.")
        String numero,


        @NotBlank(message = "Bairro é obrigatório!")
        @Size(max = 50, message = "O bairro deve ter no máximo 50 caracteres.")
        String bairro,

        @NotBlank(message = "Cidade é obrigatória!")
        @Size(max = 50, message = "A cidade deve ter no máximo 50 caracteres.")
        String cidade,

        @NotBlank(message = "UF é obrigatória!")
        @Size(min = 2, max = 2, message = "A UF deve ter exatamente 2 caracteres.")
        @Pattern(regexp = "[A-Z]{2}", message = "A UF deve conter apenas letras maiúsculas.")
        String uf,

        @NotBlank(message = "CEP é obrigatório!")
        @Size(min = 8, max = 9, message = "O CEP deve ter entre 8 e 9 caracteres.")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve estar no formato 12345-678 ou 12345678.")
        String cep

        ) {
}
