package com.exs.learningsessionscrudshop.services;

import com.exs.learningsessionscrudshop.models.Produto;
import com.exs.learningsessionscrudshop.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoService {

    private ProdutoRepository produtoRepository;

    public Produto saveProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> findAllProdutos() {
        return produtoRepository.findAll();
    }

    public Produto findProdutoById(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public Produto updateProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public boolean deleteProduto(Long id) {
        produtoRepository.deleteById(id);
        return false;
    }
}
