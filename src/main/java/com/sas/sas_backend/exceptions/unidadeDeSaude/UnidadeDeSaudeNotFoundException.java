package com.sas.sas_backend.exceptions.unidadeDeSaude;

import com.sas.sas_backend.exceptions.BaseException;

public class UnidadeDeSaudeNotFoundException  extends BaseException {

    public UnidadeDeSaudeNotFoundException(String detail) {
        super("204", "Unidade de saúde não encontrada!", detail);
    }
}
