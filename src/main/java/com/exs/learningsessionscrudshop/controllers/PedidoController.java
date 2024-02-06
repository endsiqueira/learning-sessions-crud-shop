package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Pedido;
import com.exs.learningsessionscrudshop.services.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        log.info("Criando novo pedido");
        Pedido novoPedido = pedidoService.savePedido(pedido);
        log.info("Pedido criado com sucesso: Pedido ID {}", novoPedido.getPedidoId());
        return ResponseEntity.ok(novoPedido);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        log.info("Listando todos os pedidos");
        List<Pedido> pedidos = pedidoService.findAllPedidos();
        log.info("Total de pedidos listados: {}", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
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
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoDetails) {
        log.info("Atualizando pedido com ID: {}", id);
        Pedido pedidoExistente = pedidoService.findPedidoById(id);
        if (pedidoExistente == null) {
            log.warn("Pedido com ID: {} para atualização não encontrado", id);
            return ResponseEntity.notFound().build();
        }

        Pedido pedidoAtualizado = pedidoService.updatePedido(pedidoDetails);
        log.info("Pedido com ID: {} atualizado com sucesso", id);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        log.info("Excluindo pedido com ID: {}", id);
        Pedido pedido = pedidoService.findPedidoById(id);
        if (pedido != null) {
            pedidoService.deletePedido(id);
            log.info("Pedido com ID: {} excluído com sucesso", id);
            return ResponseEntity.ok().build();
        }
        log.warn("Pedido com ID: {} para exclusão não encontrado", id);
        return ResponseEntity.notFound().build();
    }
}
