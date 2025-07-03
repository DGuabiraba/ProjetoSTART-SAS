package com.sas.sas_backend.exceptions.agendamento;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Retorna 409 Conflict
public class AgendamentoConflictException extends RuntimeException {

    public AgendamentoConflictException(String message) {
        super(message);
    }

    public AgendamentoConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}