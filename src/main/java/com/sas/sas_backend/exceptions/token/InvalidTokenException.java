package com.sas.sas_backend.exceptions.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // ou outro status HTTP apropriado para tokens inválidos
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    // Adicione este construtor
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    // Você também pode adicionar um construtor que só aceita a causa, se for útil em algum cenário
    public InvalidTokenException(Throwable cause) {
        super(cause);
    }
}