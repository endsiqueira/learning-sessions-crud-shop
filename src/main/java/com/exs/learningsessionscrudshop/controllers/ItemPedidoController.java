package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.ItemPedido;
import com.exs.learningsessionscrudshop.services.ItemPedidoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/item-pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ItemPedidoController {

    private static final Logger log = LoggerFactory.getLogger(ItemPedidoController.class);
    private final ItemPedidoService itemPedidoService;

    @PostMapping
    public ResponseEntity<ItemPedido> createItemPedido(@RequestBody ItemPedido itemPedido) {
        log.info("Criando item de pedido: {}", itemPedido);
        ItemPedido createdItemPedido = itemPedidoService.saveItemPedido(itemPedido);
        log.info("Item de pedido criado com sucesso: {}", createdItemPedido.getItemPedidoId());
        return ResponseEntity.ok(createdItemPedido);
    }

    @GetMapping
    public ResponseEntity<List<ItemPedido>> getAllItemPedidos() {
        log.info("Listando todos os itens de pedido");
        List<ItemPedido> itemPedidos = itemPedidoService.findAllItemPedidos();
        log.info("Total de itens de pedido listados: {}", itemPedidos.size());
        return ResponseEntity.ok(itemPedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPedido> getItemPedidoById(@PathVariable Long id) {
        log.info("Buscando item de pedido com ID: {}", id);
        ItemPedido itemPedido = itemPedidoService.findItemPedidoById(id);
        if (itemPedido == null) {
            log.warn("Item de pedido com ID: {} não encontrado", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Item de pedido encontrado: {}", itemPedido);
        return ResponseEntity.ok(itemPedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemPedido> updateItemPedido(@PathVariable Long id, @RequestBody ItemPedido itemPedidoDetails) {
        log.info("Atualizando item de pedido com ID: {}", id);
        ItemPedido existingItemPedido = itemPedidoService.findItemPedidoById(id);
        if (existingItemPedido == null) {
            log.warn("Não foi possível encontrar o item de pedido com ID: {} para atualização", id);
            return ResponseEntity.notFound().build();
        }
        ItemPedido updatedItemPedido = itemPedidoService.updateItemPedido(itemPedidoDetails);
        log.info("Item de pedido com ID: {} atualizado com sucesso", updatedItemPedido.getItemPedidoId());
        return ResponseEntity.ok(updatedItemPedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemPedido(@PathVariable Long id) {
        log.info("Excluindo item de pedido com ID: {}", id);
        if (itemPedidoService.findItemPedidoById(id) == null) {
            log.warn("Não foi possível encontrar o item de pedido com ID: {} para exclusão", id);
            return ResponseEntity.notFound().build();
        }
        itemPedidoService.deleteItemPedido(id);
        log.info("Item de pedido com ID: {} excluído com sucesso", id);
        return ResponseEntity.ok().build();
    }
}
