package com.sas.sas_backend.dtos.response;

import com.sas.sas_backend.dtos.PacienteDto;
import com.sas.sas_backend.dtos.ProfissionalDeSaudeDto;
import com.sas.sas_backend.models.enumerated.StatusAgendamento;
import com.sas.sas_backend.models.enumerated.StatusExame;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ExameAgendamentoResponse(

        String idExame,

        String tipoExame,

        @NotNull(message = "Status é obrigatório!")
        StatusExame status,

        String idAgendamento,

        LocalDateTime horario,

        StatusAgendamento statusAgendamento,

        PacienteDto paciente,

        ProfissionalDeSaudeDto profissionalDeSaude
) {
}
