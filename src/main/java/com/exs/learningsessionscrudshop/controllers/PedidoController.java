package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Pedido;
import com.exs.learningsessionscrudshop.services.PedidoService;
import com.exs.learningsessionscrudshop.services.RateLimitingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);
    private final PedidoService pedidoService;
    private final RateLimitingService rateLimitingService;

    private static final long CAPACITY = 5;
    private static final long WINDOW_IN_SECONDS = 60; // 1 minuto

    @PostMapping
    public ResponseEntity<?> createPedido(@RequestBody Pedido pedido) {
        if (!rateLimitingService.allowRequisition("createPedido", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Criando novo pedido");
        Pedido novoPedido = pedidoService.savePedido(pedido);
        log.info("Pedido criado com sucesso: Pedido ID {}", novoPedido.getPedidoId());
        return ResponseEntity.ok(novoPedido);
    }

    @GetMapping
    public ResponseEntity<?> getAllPedidos() {
        if (!rateLimitingService.allowRequisition("getAllPedidos", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Listando todos os pedidos");
        List<Pedido> pedidos = pedidoService.findAllPedidos();
        log.info("Total de pedidos listados: {}", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("getPedidoById", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Buscando pedido com ID: {}", id);
        Pedido pedido = pedidoService.findPedidoById(id);
        if (pedido != null) {
            log.info("Pedido encontrado: Pedido ID {}", id);
            return ResponseEntity.ok(pedido);
        }
        log.warn("Pedido com ID: {} não encontrado", id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoDetails) {
        if (!rateLimitingService.allowRequisition("updatePedido", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Atualizando pedido com ID: {}", id);
        Pedido pedidoAtualizado = pedidoService.updatePedido(pedidoDetails);
        log.info("Pedido com ID: {} atualizado com sucesso", id);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("deletePedido", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Excluindo pedido com ID: {}", id);
        boolean deleted = pedidoService.deletePedido(id);
        if (deleted) {
            log.info("Pedido com ID: {} excluído com sucesso", id);
            return ResponseEntity.ok().build();
        }
        log.warn("Pedido com ID: {} para exclusão não encontrado", id);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Map<String, String>> tooManyRequestsResponse() {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", "Número de chamadas por minuto excedido, tente novamente em alguns instantes");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(responseMessage);
    }
}
