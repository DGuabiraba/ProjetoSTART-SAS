package com.sas.sas_backend.exceptions.paciente;

import com.sas.sas_backend.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class CredentialsNotMatchException extends BaseException {

    public CredentialsNotMatchException(String detail) {
        super(HttpStatus.UNAUTHORIZED.toString(),"Token invalido", detail);
    }
}
