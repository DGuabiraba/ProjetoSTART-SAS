package com.sas.sas_backend.dtos.response;

import com.sas.sas_backend.models.Paciente;
import com.sas.sas_backend.models.ProfissionalDeSaude;
import com.sas.sas_backend.models.UnidadeDeSaude;
import com.sas.sas_backend.models.enumerated.StatusAgendamento;

import java.time.LocalDateTime;

public record AgendamentoResponse(
        String id,

        LocalDateTime dataHoraInicio,

        LocalDateTime dataHoraFim,

        StatusAgendamento status,

        String observacoes,

        Paciente paciente,

        ProfissionalDeSaude profissionalDeSaude,

        UnidadeDeSaude unidadeDeSaude
) {
}
