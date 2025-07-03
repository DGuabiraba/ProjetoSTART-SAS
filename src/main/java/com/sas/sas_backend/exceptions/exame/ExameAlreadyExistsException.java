package com.sas.sas_backend.exceptions.exame;

import com.sas.sas_backend.exceptions.BaseException;

public class ExameAlreadyExistsException extends BaseException {
    public ExameAlreadyExistsException(String detail) {
        super("409", "Exame Agendado!", detail);
    }
}

