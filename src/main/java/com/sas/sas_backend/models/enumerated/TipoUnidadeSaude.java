package com.sas.sas_backend.models.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoUnidadeSaude {

    UBS("UBS"),
    HOSPITAL("hospital"),
    CLINICA("clinica"),
    LABORATORIO("laboratorio"),
    POSTOSAUDE("posto de saúde");

    private final String description;

}
