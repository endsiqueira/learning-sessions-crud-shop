package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.services.SessaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessaoController {

    private static final Logger log = LoggerFactory.getLogger(SessaoController.class);
    private final SessaoService sessaoService;

    public SessaoController(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    @PostMapping("/sessoes")
    public ResponseEntity<String> criarSessao(@RequestBody String usuario) {
        log.info("Tentativa de criar sessão para o usuário: {}", usuario);
        String sessaoId = sessaoService.criarSessao(usuario);
        if (sessaoId != null) {
            log.info("Sessão criada com sucesso. ID da Sessão: {}", sessaoId);
            return ResponseEntity.ok(sessaoId);
        } else {
            log.error("Falha ao criar sessão para o usuário: {}", usuario);
            return ResponseEntity.internalServerError().body("Erro ao criar sessão");
        }
    }
}
