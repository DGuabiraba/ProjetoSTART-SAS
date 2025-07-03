package com.sas.sas_backend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequest(
        @NotBlank String senhaAtual,
        @NotBlank String novaSenha
) {}