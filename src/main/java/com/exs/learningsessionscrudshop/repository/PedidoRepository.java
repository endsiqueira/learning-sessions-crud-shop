package com.exs.learningsessionscrudshop.repository;

import com.exs.learningsessionscrudshop.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
