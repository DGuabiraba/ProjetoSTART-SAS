package com.sas.sas_backend.controller;

import com.sas.sas_backend.dtos.ProfissionalDeSaudeDto;
import com.sas.sas_backend.dtos.UnidadeDeSaudeDto;
import com.sas.sas_backend.dtos.request.AlterarSenhaRequest;
import com.sas.sas_backend.dtos.request.TokenRequest;
import com.sas.sas_backend.dtos.request.UnidadeLoginRequest;
import com.sas.sas_backend.dtos.response.ProfissionalDeSaudeResponse;
import com.sas.sas_backend.dtos.response.TokenValidationResponse;
import com.sas.sas_backend.service.ProfissionalDeSaudeService;
import com.sas.sas_backend.service.TokenService;
import com.sas.sas_backend.service.UnidadeDeSaudeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("unidadeDeSaude")
@RequiredArgsConstructor
public class UnidadeDeSaudeController {

    private final UnidadeDeSaudeService unidadeDeSaudeService;
    private final ProfissionalDeSaudeService profissionalDeSaudeService;
    private final TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<UnidadeDeSaudeDto> create(@RequestBody @Valid UnidadeDeSaudeDto unidadeDeSaudeDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(unidadeDeSaudeService.create(unidadeDeSaudeDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUnidade(@RequestBody() UnidadeLoginRequest unidadeLoginRequest) {
        return ResponseEntity.ok(unidadeDeSaudeService.loginUnidade(unidadeLoginRequest.getEmail(), unidadeLoginRequest.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> loginUnidade(@RequestBody() TokenRequest tokenRequest) {
        tokenService.logout(tokenRequest.token());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/validate")
    public ResponseEntity<TokenValidationResponse> validarToken(@RequestBody TokenRequest tokenRequest) {
        TokenValidationResponse response= tokenService.validarTokenRetornandoDados(tokenRequest.token());
        return ResponseEntity.ok(response);
    }

    @PostMapping("profissional/create")
    public ResponseEntity<ProfissionalDeSaudeResponse> cadastrarProfissionalDeSaude(@RequestBody @Valid ProfissionalDeSaudeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalDeSaudeService.cadastrarProfissionalDeSaude(dto));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ProfissionalDeSaudeDto>> buscarTodos() {
        return ResponseEntity.ok().body(profissionalDeSaudeService.buscarTodos());
    }
    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable String id,
            @Valid @RequestBody AlterarSenhaRequest request
    ) {
        unidadeDeSaudeService.alterarSenha(id, request.senhaAtual(), request.novaSenha());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findBy/nome/{nome}")
    public ResponseEntity<ProfissionalDeSaudeDto> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok().body(profissionalDeSaudeService.buscarPorNome(nome));
    }

    @DeleteMapping("/deleteBy/id/{id}")
    public ResponseEntity<Void> deletarProfissionalDeSaude(@PathVariable String id) {
        profissionalDeSaudeService.deletarProfissionalDeSaude(id);
        return ResponseEntity.noContent().build();
    }


}
