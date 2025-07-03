package com.sas.sas_backend.controller;

import com.sas.sas_backend.dtos.EnderecoDto;
import com.sas.sas_backend.service.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Endereco")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping("/create")
    public ResponseEntity<EnderecoDto> cadastrarEndereco(@RequestBody @Valid EnderecoDto enderecoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.cadastrarEndereco(enderecoDto));
    }

    @PutMapping("/update/Endereco/{id}")
    public ResponseEntity<EnderecoDto> atualizarEndereco(@PathVariable String id, @Valid @RequestBody EnderecoDto enderecoDto) {
        return ResponseEntity.status(HttpStatus.OK).body(enderecoService.atualizarEndereco(id, enderecoDto));

    }

    @DeleteMapping("/delete/Endereco/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable String id) {
        enderecoService.deletarEndereco(id);
        return ResponseEntity.noContent().build();
    }
}
