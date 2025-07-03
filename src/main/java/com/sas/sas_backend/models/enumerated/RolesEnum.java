package com.sas.sas_backend.models.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RolesEnum {
    PACIENTE("PACIENTE"),
    UNIDADE("UNIDADE"),
    PROFISSIONAL("PROFISSIONAL");

    private final String nome;
}