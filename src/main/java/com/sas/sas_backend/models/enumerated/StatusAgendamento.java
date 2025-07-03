package com.sas.sas_backend.models.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatusAgendamento {
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído"),
    AGENDADO("Agendado");

    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return description;
    }

    public static StatusAgendamento fromDescription(String description) {
        for (StatusAgendamento statusAgendamento : StatusAgendamento.values()) {
            if (statusAgendamento.description.equals(description)) {
                return statusAgendamento;
            }
        }
        throw new IllegalArgumentException("Error: " + description);
    }
}