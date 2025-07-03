package com.sas.sas_backend.exceptions.endereco;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404 Not Found quando esta exceção é lançada
public class EnderecoNotFoundException extends RuntimeException {

    public EnderecoNotFoundException(String message) {
        super(message);
    }

    public EnderecoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}