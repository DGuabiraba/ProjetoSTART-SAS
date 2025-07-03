package com.sas.sas_backend.exceptions.profissionalDeSaude;

import com.sas.sas_backend.exceptions.BaseException;

public class ProfissionalDeSaudeNotFoundException extends BaseException {
    public ProfissionalDeSaudeNotFoundException(String detail) {
        super("204", "Profissional não encontrado!", detail);
    }
}
