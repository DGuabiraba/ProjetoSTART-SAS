package com.sas.sas_backend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sas.sas_backend.models.enumerated.Genero;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record PacienteDto(

        String id,

        @NotBlank(message = "CPF é obrigatório!")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
        String cpf,

        @NotBlank(message = "Nome é obrigatório!")
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+${1,50}", message = "This field must contain only letters and spaces.")
        String nome,

        @NotBlank(message = "Email é obrigatório!")
        @Email(message = "Must be a valid email address.")
        String email,

        @NotBlank(message = "Senha é obrigatória!")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "A senha deve conter pelo menos 8 caracteres, uma letra, um número e um caractere especial.")
        String senha,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,

        @NotNull(message = "Gênero é obrigatório!")
        Genero genero,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}")
        String telefone,

        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "This field must contain only letters, spaces, and accented characters.")
        String grauInstrucao,

        @NotNull
        Boolean notificacoesAtivadas,

        EnderecoDto endereco


        ) {
}

