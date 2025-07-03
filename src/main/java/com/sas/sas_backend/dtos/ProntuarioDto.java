package com.sas.sas_backend.dtos;


import com.sas.sas_backend.models.enumerated.TipoSanguineo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record ProntuarioDto(


        String id,

        @NotBlank(message = "Descrição é obrigatória!")
        String descricao,

        @NotBlank(message = "Alergias são obrigatórias")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9,\\-() ]+$")
        String alergias,

        String observacoes,

        @NotNull(message = "Tipo sanguíneo é obrigatório!")
        TipoSanguineo tipoSanguineo,

        @NotBlank(message = "Doenças crônicas são obrigatórias")
        @Pattern(regexp = "^[a-zA-ZÀ-ú,0-9 ]+$")
        String doencasCronicas,

        String historicoFamiliar,

        String pacienteCpf,

        String profissionalNumero


) {
}