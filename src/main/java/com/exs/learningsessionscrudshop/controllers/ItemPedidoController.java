package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.ItemPedido;
import com.exs.learningsessionscrudshop.services.ItemPedidoService;
import com.exs.learningsessionscrudshop.services.RateLimitingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/item-pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ItemPedidoController {

    private static final Logger log = LoggerFactory.getLogger(ItemPedidoController.class);
    private final ItemPedidoService itemPedidoService;
    private final RateLimitingService rateLimitingService;

    private static final long CAPACITY = 5;
    private static final long WINDOW_IN_SECONDS = 60; // 1 minuto

    @PostMapping
    public ResponseEntity<?> createItemPedido(@RequestBody ItemPedido itemPedido) {
        if (!rateLimitingService.allowRequisition("createItemPedido", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Criando item de pedido: {}", itemPedido);
        ItemPedido createdItemPedido = itemPedidoService.saveItemPedido(itemPedido);
        log.info("Item de pedido criado com sucesso: {}", createdItemPedido.getItemPedidoId());
        return ResponseEntity.ok(createdItemPedido);
    }

    @GetMapping
    public ResponseEntity<?> getAllItemPedidos() {
        if (!rateLimitingService.allowRequisition("getAllItemPedidos", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Listando todos os itens de pedido");
        List<ItemPedido> itemPedidos = itemPedidoService.findAllItemPedidos();
        log.info("Total de itens de pedido listados: {}", itemPedidos.size());
        return ResponseEntity.ok(itemPedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemPedidoById(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("getItemPedidoById", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
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
    public ResponseEntity<?> updateItemPedido(@PathVariable Long id, @RequestBody ItemPedido itemPedidoDetails) {
        if (!rateLimitingService.allowRequisition("updateItemPedido", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Atualizando item de pedido com ID: {}", id);
        ItemPedido updatedItemPedido = itemPedidoService.updateItemPedido(itemPedidoDetails);
        log.info("Item de pedido com ID: {} atualizado com sucesso", updatedItemPedido.getItemPedidoId());
        return ResponseEntity.ok(updatedItemPedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItemPedido(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("deleteItemPedido", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Excluindo item de pedido com ID: {}", id);
        boolean deleted = itemPedidoService.deleteItemPedido(id);
        if (!deleted) {
            log.warn("Não foi possível encontrar o item de pedido com ID: {} para exclusão", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Item de pedido com ID: {} excluído com sucesso", id);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Map<String, String>> tooManyRequestsResponse() {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", "Número de chamadas por minuto excedido, tente novamente em alguns instantes");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(responseMessage);
    }
}
