package com.exs.learningsessionscrudshop.services;

import com.exs.learningsessionscrudshop.models.ItemPedido;
import com.exs.learningsessionscrudshop.repository.ItemPedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemPedidoService {

    private ItemPedidoRepository itemPedidoRepository;

    public ItemPedido saveItemPedido(ItemPedido itemPedido) {
        return itemPedidoRepository.save(itemPedido);
    }

    public List<ItemPedido> findAllItemPedidos() {
        return itemPedidoRepository.findAll();
    }

    public ItemPedido findItemPedidoById(Long id) {
        return itemPedidoRepository.findById(id).orElse(null);
    }

    public ItemPedido updateItemPedido(ItemPedido itemPedido) {
        return itemPedidoRepository.save(itemPedido);
    }

    public void deleteItemPedido(Long id) {
        itemPedidoRepository.deleteById(id);
    }
}
