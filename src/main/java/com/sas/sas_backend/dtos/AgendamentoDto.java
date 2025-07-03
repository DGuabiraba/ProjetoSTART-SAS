package com.sas.sas_backend.dtos;

import com.sas.sas_backend.models.enumerated.StatusAgendamento;

import java.time.LocalDateTime;

public record AgendamentoDto(
        String id,

        LocalDateTime dataHoraInicio,

        LocalDateTime dataHoraFim,

        StatusAgendamento status,

        String observacoes,

        String pacienteCpf,

        String profissionalNumero,

        String unidadeCnpj

) {
}
