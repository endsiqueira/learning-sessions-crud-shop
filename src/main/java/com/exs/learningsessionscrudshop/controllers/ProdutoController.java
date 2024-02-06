package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Produto;
import com.exs.learningsessionscrudshop.services.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        log.info("Criando novo produto: {}", produto.getNome());
        Produto savedProduto = produtoService.saveProduto(produto);
        log.info("Produto criado com sucesso: Produto ID {}", savedProduto.getProdutoId());
        return ResponseEntity.ok(savedProduto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {
        log.info("Listando todos os produtos");
        List<Produto> produtos = produtoService.findAllProdutos();
        log.info("Total de produtos listados: {}", produtos.size());
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
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
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        log.info("Atualizando produto com ID: {}", id);
        Produto existingProduto = produtoService.findProdutoById(id);
        if (existingProduto == null) {
            log.warn("Produto com ID: {} para atualização não encontrado", id);
            return ResponseEntity.notFound().build();
        }
        // Suponha que o método updateProduto faz a atualização das propriedades do produto
        Produto updatedProduto = produtoService.updateProduto(produtoDetails);
        log.info("Produto com ID: {} atualizado com sucesso", id);
        return ResponseEntity.ok(updatedProduto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        log.info("Excluindo produto com ID: {}", id);
        Produto produto = produtoService.findProdutoById(id);
        if (produto != null) {
            produtoService.deleteProduto(id);
            log.info("Produto com ID: {} excluído com sucesso", id);
            return ResponseEntity.ok().build();
        }
        log.warn("Produto com ID: {} para exclusão não encontrado", id);
        return ResponseEntity.notFound().build();
    }
}
