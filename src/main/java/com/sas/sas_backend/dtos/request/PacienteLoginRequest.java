package com.sas.sas_backend.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PacienteLoginRequest {
    public String email;
    public String password;
}
