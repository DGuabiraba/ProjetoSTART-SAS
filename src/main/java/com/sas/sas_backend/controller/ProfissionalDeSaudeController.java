package com.sas.sas_backend.controller;

import com.sas.sas_backend.dtos.ProfissionalDeSaudeDto;
import com.sas.sas_backend.dtos.request.AlterarSenhaRequest;
import com.sas.sas_backend.dtos.request.ProfissionalLoginRequest;
import com.sas.sas_backend.dtos.request.TokenRequest;
import com.sas.sas_backend.dtos.response.ProfissionalDeSaudeResponse;
import com.sas.sas_backend.dtos.response.TokenValidationResponse;
import com.sas.sas_backend.service.ProfissionalDeSaudeService;
import com.sas.sas_backend.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissional-de-saude")
@RequiredArgsConstructor
public class ProfissionalDeSaudeController {

    private final ProfissionalDeSaudeService profissionalDeSaudeService;
    private final TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<ProfissionalDeSaudeResponse> cadastrarProfissionalDeSaude(@RequestBody @Valid ProfissionalDeSaudeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalDeSaudeService.cadastrarProfissionalDeSaude(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginProfissional(@RequestBody() ProfissionalLoginRequest profissionalLoginRequest) {
        return ResponseEntity.ok(profissionalDeSaudeService.loginProfissional(profissionalLoginRequest.getEmail(), profissionalLoginRequest.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> loginProfissional(@RequestBody() TokenRequest tokenRequest) {
        tokenService.logout(tokenRequest.token());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/validate")
    public ResponseEntity<TokenValidationResponse> validarToken(@RequestBody TokenRequest tokenRequest) {
        TokenValidationResponse response= tokenService.validarTokenRetornandoDados(tokenRequest.token());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable String id,
            @Valid @RequestBody AlterarSenhaRequest request
    ) {
        profissionalDeSaudeService.alterarSenha(id, request.senhaAtual(), request.novaSenha());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<ProfissionalDeSaudeDto>> buscarTodos() {
        return ResponseEntity.ok().body(profissionalDeSaudeService.buscarTodos());
    }

    @GetMapping("/findBy/nome/{nome}")
    public ResponseEntity<ProfissionalDeSaudeDto> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok().body(profissionalDeSaudeService.buscarPorNome(nome));
    }

    @PutMapping("/updateBy/id/{id}")
    public ResponseEntity<ProfissionalDeSaudeDto> atualizarProfissionalDeSaude(@PathVariable String id, @Valid @RequestBody ProfissionalDeSaudeDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(profissionalDeSaudeService.atualizarProfissionalDeSaude(id, dto));
    }

    @DeleteMapping("/deleteBy/id/{id}")
    public ResponseEntity<Void> deletarProfissionalDeSaude(@PathVariable String id) {
        profissionalDeSaudeService.deletarProfissionalDeSaude(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("findBy/numRegistro/{numeroRegistro}")
    public ResponseEntity<ProfissionalDeSaudeResponse> buscarPorNumeroRegistro(String numero){
        return ResponseEntity.ok().body(profissionalDeSaudeService.buscarPorNumeroRegistro(numero));
    }

}
