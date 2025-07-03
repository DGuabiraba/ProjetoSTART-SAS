package com.sas.sas_backend.exceptions.paciente;

import com.sas.sas_backend.exceptions.BaseException;

public class PacienteNotFoundException extends BaseException {

    public PacienteNotFoundException(String detail) {
        super("204", "Paciente não encontrado !", detail);
    }
}
