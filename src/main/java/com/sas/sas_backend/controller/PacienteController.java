package com.sas.sas_backend.controller;

import com.sas.sas_backend.dtos.PacienteDto;
import com.sas.sas_backend.dtos.request.AlterarSenhaRequest;
import com.sas.sas_backend.dtos.request.PacienteLoginRequest;
import com.sas.sas_backend.dtos.request.TokenRequest;
import com.sas.sas_backend.dtos.response.TokenValidationResponse;
import com.sas.sas_backend.service.PacienteService;
import com.sas.sas_backend.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paciente")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final TokenService tokenService;


    @PostMapping("/create")
    public ResponseEntity<PacienteDto> cadastrarPaciente(@RequestBody @Valid PacienteDto pacienteDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastrarPaciente(pacienteDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginPaciente(@RequestBody() PacienteLoginRequest pacienteLoginRequest) {
        return ResponseEntity.ok(pacienteService.loginPaciente(pacienteLoginRequest.getEmail(), pacienteLoginRequest.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutPaciente(@RequestBody()TokenRequest tokenRequest) {
        tokenService.logout(tokenRequest.token());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/validate")
    public ResponseEntity<TokenValidationResponse> validarToken(@RequestBody TokenRequest tokenRequest) {
        TokenValidationResponse response = tokenService.validarTokenRetornandoDados(tokenRequest.token());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable String id,
            @Valid @RequestBody AlterarSenhaRequest request
    ) {
        pacienteService.alterarSenha(id, request.senhaAtual(), request.novaSenha());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PacienteDto>> buscarTodos() {
        return ResponseEntity.ok().body(pacienteService.buscarTodos());
    }

    @GetMapping("/findByCpf/{cpf}")
    public ResponseEntity<PacienteDto> buscarPorCpf(@PathVariable String cpf) {

        return ResponseEntity.ok().body(pacienteService.buscarPorCpf(cpf));

    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<PacienteDto> atualizarPaciente(@PathVariable String id, @Valid @RequestBody PacienteDto pacienteDto) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.atualizarPaciente(id, pacienteDto));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable String id) {
        pacienteService.deletarPaciente(id);
        return ResponseEntity.noContent().build();
    }

}
