package com.sas.sas_backend.controller;

import com.sas.sas_backend.dtos.ProntuarioDto;
import com.sas.sas_backend.dtos.response.ProntuarioResponse;
import com.sas.sas_backend.service.ProntuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("prontuario")
@RequiredArgsConstructor
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    @PostMapping("create")
    public ResponseEntity<ProntuarioResponse> create(@RequestBody @Valid ProntuarioDto prontuarioDto) {
        return ResponseEntity.ok().body(prontuarioService.create(prontuarioDto));
    }
    @GetMapping("findByCpf/{cpf}")
    public   ResponseEntity<ProntuarioResponse> buscarPorCpf(@PathVariable @Valid String cpf) {
        return ResponseEntity.ok().body(prontuarioService.buscarPorCpf(cpf));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<ProntuarioResponse> atualizarProntuario(
            @PathVariable String cpf,
            @RequestBody @Valid ProntuarioDto prontuarioDto) {

        ProntuarioResponse response = prontuarioService.atualizar(cpf, prontuarioDto);
        return ResponseEntity.ok(response);
    }
}
