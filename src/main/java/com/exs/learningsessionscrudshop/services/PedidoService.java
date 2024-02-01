package com.exs.learningsessionscrudshop.services;

import com.exs.learningsessionscrudshop.models.Pedido;
import com.exs.learningsessionscrudshop.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PedidoService {

    private PedidoRepository pedidoRepository;

    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> findAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido findPedidoById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public Pedido updatePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void deletePedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}
