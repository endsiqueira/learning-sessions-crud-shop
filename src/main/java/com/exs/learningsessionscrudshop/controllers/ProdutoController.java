package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Produto;
import com.exs.learningsessionscrudshop.services.ProdutoService;
import com.exs.learningsessionscrudshop.services.RateLimitingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);
    private final ProdutoService produtoService;
    private final RateLimitingService rateLimitingService;

    private static final long CAPACITY = 5;
    private static final long WINDOW_IN_SECONDS = 60; // 1 minuto

    @PostMapping
    public ResponseEntity<?> createProduto(@RequestBody Produto produto) {
        if (!rateLimitingService.allowRequisition("createProduto", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Criando novo produto: {}", produto.getNome());
        Produto savedProduto = produtoService.saveProduto(produto);
        log.info("Produto criado com sucesso: Produto ID {}", savedProduto.getProdutoId());
        return ResponseEntity.ok(savedProduto);
    }

    @GetMapping
    public ResponseEntity<?> getAllProdutos() {
        if (!rateLimitingService.allowRequisition("getAllProdutos", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Listando todos os produtos");
        List<Produto> produtos = produtoService.findAllProdutos();
        log.info("Total de produtos listados: {}", produtos.size());
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProdutoById(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("getProdutoById", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Buscando produto com ID: {}", id);
        Produto produto = produtoService.findProdutoById(id);
        if (produto != null) {
            log.info("Produto encontrado: Produto ID {}", id);
            return ResponseEntity.ok(produto);
        }
        log.warn("Produto com ID: {} não encontrado", id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        if (!rateLimitingService.allowRequisition("updateProduto", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Atualizando produto com ID: {}", id);
        Produto updatedProduto = produtoService.updateProduto(produtoDetails);
        log.info("Produto com ID: {} atualizado com sucesso", id);
        return ResponseEntity.ok(updatedProduto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("deleteProduto", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Excluindo produto com ID: {}", id);
        boolean deleted = produtoService.deleteProduto(id);
        if (deleted) {
            log.info("Produto com ID: {} excluído com sucesso", id);
            return ResponseEntity.ok().build();
        }
        log.warn("Produto com ID: {} para exclusão não encontrado", id);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Map<String, String>> tooManyRequestsResponse() {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", "Número de chamadas por minuto excedido, tente novamente em alguns instantes");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(responseMessage);
    }
}
