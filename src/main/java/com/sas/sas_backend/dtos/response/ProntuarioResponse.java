package com.sas.sas_backend.dtos.response;

import com.sas.sas_backend.dtos.PacienteDto;
import com.sas.sas_backend.dtos.ProfissionalDeSaudeDto;
import com.sas.sas_backend.models.enumerated.TipoSanguineo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record ProntuarioResponse(

        String id,

        @NotBlank(message = "Descrição é obrigatória!")
        String descricao,

        @NotBlank(message = "Alergias são obrigatórias")
        @Pattern(regexp = "^[a-zA-ZÀ-ú, ]+$")
        String alergias,

        @NotNull(message = "Tipo sanguíneo é obrigatório!")
        TipoSanguineo tipoSanguineo,

        @NotBlank(message = "Doenças crônicas são obrigatórias")
        @Pattern(regexp = "^[a-zA-ZÀ-ú, ]+$")
        String doencasCronicas,

        String historicoFamiliar,

        PacienteDto pacienteDto,

        ProfissionalDeSaudeDto profissionalDeSaude


) {
}