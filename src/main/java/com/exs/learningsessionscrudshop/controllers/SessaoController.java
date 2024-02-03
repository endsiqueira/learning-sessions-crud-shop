package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.services.SessaoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;

    @PostMapping("/sessoes")
    public ResponseEntity<String> criarSessao(@RequestBody String usuario) {
        String sessaoId = sessaoService.criarSessao(usuario);
        return ResponseEntity.ok(sessaoId);
    }
}
