package com.sas.sas_backend.exceptions.paciente;

import com.sas.sas_backend.exceptions.BaseException;

public class PacienteAlreadyExistsException extends BaseException {

    public PacienteAlreadyExistsException(String detail) {
        super("409", "Paciente ja existente !", detail);
    }
}
