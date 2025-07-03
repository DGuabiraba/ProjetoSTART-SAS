package com.sas.sas_backend.exceptions.agendamento;

import com.sas.sas_backend.exceptions.BaseException;

public class InvalidEndTimeException extends BaseException {
    public InvalidEndTimeException(String detail) {
        super("422","Horário de término deve ser após o início", detail);
    }
}
