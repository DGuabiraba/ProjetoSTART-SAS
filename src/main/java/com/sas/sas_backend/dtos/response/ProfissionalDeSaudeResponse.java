package com.sas.sas_backend.dtos.response;

import com.sas.sas_backend.dtos.UnidadeDeSaudeDto;
import com.sas.sas_backend.models.enumerated.TipoProfissional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProfissionalDeSaudeResponse(

        String id,

        @NotBlank(message = "Nome é obrigatório!")
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+${1,50}", message = "Não sei o que colocar!!!!!!")
        String nome,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}")
        String telefone,

        @NotBlank(message = "Email é obrigatório!")
        @Email(message = "Deve ser um endereço de e-mail válido.")
        String email,

        @NotBlank(message = "Senha é obrigatória!")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "A senha deve conter pelo menos 8 caracteres, uma letra, um número e um caractere especial.")
        String senha,

        @NotNull(message = "Tipo do profissional é obrigatório!")
        TipoProfissional tipoProfissional,

        @NotBlank(message = "Numero de Registro é obrigatório!")
        String numeroRegistro,

        UnidadeDeSaudeDto unidadeDeSaude) {
    public static record TokenValidationResponse(String id, String role, boolean valido) {
    }
}