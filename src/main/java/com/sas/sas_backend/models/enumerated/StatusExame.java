package com.sas.sas_backend.models.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusExame {
    PENDENTE("Pendente"),
    REALIZADO("Realizado"),
    CANCELADO("Cancelado"),
    AGENDADO("Agendado"),
    ANALISADO("Analisado");

    private final String description;

    StatusExame(String description) {
        this.description = description;
    }

    @Override
    @JsonValue
    public String toString() {
        return description;
    }

    public static StatusExame fromDescription(String description) {
        for (StatusExame statusExame : StatusExame.values()) {
            if (statusExame.description.equals(description)) {
                return statusExame;
            }
        }
        throw new IllegalArgumentException("Error: " + description);
    }
}