package com.sas.sas_backend.exceptions.exame;

import com.sas.sas_backend.exceptions.BaseException;

public class ExameNotFoundException extends BaseException {
    public ExameNotFoundException(String detail) {
        super("209", "Exame n encontrado",detail);
    }
}