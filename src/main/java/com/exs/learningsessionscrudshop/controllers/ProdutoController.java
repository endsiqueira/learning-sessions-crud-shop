package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Produto;
import com.exs.learningsessionscrudshop.services.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        Produto savedProduto = produtoService.saveProduto(produto);
        return ResponseEntity.ok(savedProduto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {
        return ResponseEntity.ok(produtoService.findAllProdutos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Produto produto = produtoService.findProdutoById(id);
        return produto != null ? ResponseEntity.ok(produto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        Produto existingProduto = produtoService.findProdutoById(id);
        if (existingProduto == null) {
            return ResponseEntity.notFound().build();
        }
        // Atualiza as propriedades do produto existente com os detalhes fornecidos
        // (por exemplo, nome, preço, etc.)
        // existingProduto.setNome(produtoDetails.getNome());
        // ...
        Produto updatedProduto = produtoService.updateProduto(existingProduto);
        return ResponseEntity.ok(updatedProduto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        Produto produto = produtoService.findProdutoById(id);
        if (produto == null) {
            return ResponseEntity.notFound().build();
        }
        produtoService.deleteProduto(id);
        return ResponseEntity.ok().build();
    }
}
