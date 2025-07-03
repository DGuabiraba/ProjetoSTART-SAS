package com.sas.sas_backend.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoExameDto(

        @NotNull
        @Future
        LocalDateTime horaExame,

        @NotBlank
        String pacienteCpf,

        @NotBlank
        String profissionalNumero,

        @NotBlank
        String tipoExame,

        String descricao
) {
}
