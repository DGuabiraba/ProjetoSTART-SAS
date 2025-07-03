package com.sas.sas_backend.dtos;

import com.sas.sas_backend.models.enumerated.StatusExame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ExameDto(

        @NotBlank(message = "Descrição é obrigatória!")
        String descricao,

        String tipoExame,

        @NotNull(message = "Status é obrigatório!")
        StatusExame status,

        LocalDateTime horaExame,

        String pacienteCpf,

        String profissionalNumero

) {
}