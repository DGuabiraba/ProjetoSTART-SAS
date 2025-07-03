package com.sas.sas_backend.models.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoProfissional {
    MEDICO("Médico"),
    ENFERMEIRO("Enfermeiro"),
    DENTISTA("Dentista"),
    PSICOLOGO("Psicologo"),
    FISIOTERAPEUTA("Fisioterapeuta"),
    NUTRICIONISTA("Nutricionista"),
    TECNICODEENFERMAGEM("Técnico De Enfermagem");

    private final String description;



}


