package com.sas.sas_backend.models.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Genero {
    M("Masculino"),
    F("Feminino"),
    OUTRO("Outro");

    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return description;
    }

    public static Genero fromDescription(String description) {
        for (Genero genero : Genero.values()) {
            if (genero.description.equals(description)) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Error: " + description);
    }
}

