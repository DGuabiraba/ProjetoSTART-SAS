package com.sas.sas_backend.exceptions.agendamento;

import com.sas.sas_backend.exceptions.BaseException;

import java.time.LocalTime;

public class OutsideBusinessHoursException extends BaseException {

    private static final LocalTime HORA_ABERTURA = LocalTime.of(8, 0);
    private static final LocalTime HORA_FECHAMENTO = LocalTime.of(18, 0);

    public OutsideBusinessHoursException(String detail) {
        super("409", "Horário fora do expediente " + HORA_ABERTURA + " às " + HORA_FECHAMENTO, detail);
    }
}
