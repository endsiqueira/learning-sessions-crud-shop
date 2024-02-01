package com.exs.learningsessionscrudshop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produtoId;

    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEmEstoque;

    @JsonIgnore
    @OneToMany(mappedBy = "produto")
    private Set<ItemPedido> itensPedidos;
}
