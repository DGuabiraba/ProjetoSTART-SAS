package com.sas.sas_backend.exceptions.prontuario;

import com.sas.sas_backend.exceptions.BaseException;

public class DuplicateRegistrationException extends BaseException {
    public DuplicateRegistrationException(String detail) {
        super("409", "Paciente já possui prontuário cadastrado.", detail);
    }
}
