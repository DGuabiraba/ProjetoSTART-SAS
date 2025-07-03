package com.sas.sas_backend.exceptions.prontuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404 Not Found
public class ProntuarioNotFoundException extends RuntimeException {

    public ProntuarioNotFoundException(String message) {
        super(message);
    }

    public ProntuarioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}