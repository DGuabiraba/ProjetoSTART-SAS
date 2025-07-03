package com.sas.sas_backend.dtos;

import java.time.LocalDateTime;

public record TimeInterval(
        LocalDateTime inicio,
        LocalDateTime fim
) {
    @Override
    public String toString(){
        return inicio.toLocalTime() + "-" + fim.toLocalTime();
    }
}
