package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.ItemPedido;
import com.exs.learningsessionscrudshop.services.ItemPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/item-pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    @PostMapping
    public ResponseEntity<ItemPedido> createItemPedido(@RequestBody ItemPedido itemPedido) {
        return ResponseEntity.ok(itemPedidoService.saveItemPedido(itemPedido));
    }

    @GetMapping
    public ResponseEntity<List<ItemPedido>> getAllItemPedidos() {
        return ResponseEntity.ok(itemPedidoService.findAllItemPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPedido> getItemPedidoById(@PathVariable Long id) {
        ItemPedido itemPedido = itemPedidoService.findItemPedidoById(id);
        return itemPedido != null ? ResponseEntity.ok(itemPedido) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemPedido> updateItemPedido(@PathVariable Long id, @RequestBody ItemPedido itemPedidoDetails) {
        ItemPedido existingItemPedido = itemPedidoService.findItemPedidoById(id);
        if (existingItemPedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemPedidoService.updateItemPedido(existingItemPedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemPedido(@PathVariable Long id) {
        if (itemPedidoService.findItemPedidoById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        itemPedidoService.deleteItemPedido(id);
        return ResponseEntity.ok().build();
    }
}
