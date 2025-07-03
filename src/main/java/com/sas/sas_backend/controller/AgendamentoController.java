package com.sas.sas_backend.controller;

import com.sas.sas_backend.dtos.AgendamentoDto;
import com.sas.sas_backend.dtos.response.AgendamentoResponse;
import com.sas.sas_backend.models.Agendamento;
import com.sas.sas_backend.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Agendamento")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @GetMapping("/{profissionalNumero}/{pacienteCpf}/{data}")
    public ResponseEntity<List<Agendamento>> buscarAgendamentosDoDia(
            @PathVariable String profissionalNumero,
            @PathVariable String pacienteCpf,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosDoDia(
                profissionalNumero, pacienteCpf, data
        );
        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }

    @PostMapping("/criarAgendamento")
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@RequestBody @Valid AgendamentoDto agendamentoDto) {
        return ResponseEntity.ok().body(agendamentoService.criarAgendamento(agendamentoDto));
    }

}



