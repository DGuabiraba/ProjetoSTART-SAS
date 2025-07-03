package com.sas.sas_backend.exceptions.profissionalDeSaude;

import com.sas.sas_backend.exceptions.BaseException;

public class ProfissionalDeSaudeAlreadyExistsException extends BaseException {
    public ProfissionalDeSaudeAlreadyExistsException(String detail) {
        super("409", "Profissional já existente!", detail);
    }
}
