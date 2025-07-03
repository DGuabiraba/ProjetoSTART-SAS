package com.sas.sas_backend.exceptions.unidadeDeSaude;

import com.sas.sas_backend.exceptions.BaseException;

public class UnidadeDeSaudeAlreadyExistsException extends BaseException {
    public UnidadeDeSaudeAlreadyExistsException(String detail) {
        super("409","Unidade de saude já existente!", detail);
    }


}
