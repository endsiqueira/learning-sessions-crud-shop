package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Cliente;
import com.exs.learningsessionscrudshop.services.ClienteService;
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
@RequestMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService clienteService;
    private final RateLimitingService rateLimitingService;

    private static final long CAPACITY = 5;
    private static final long WINDOW_IN_SECONDS = 60; // 1 minute expressed in seconds

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        if (!rateLimitingService.allowRequisition("createCliente", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Iniciando criação do cliente: {}", cliente.getNome());
        Cliente savedCliente = clienteService.saveCliente(cliente);
        log.info("Cliente criado com sucesso: {}", savedCliente.getClienteId());
        return ResponseEntity.ok(savedCliente);
    }

    @GetMapping
    public ResponseEntity<?> getAllClientes() {
        if (!rateLimitingService.allowRequisition("getAllClientes", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Listando todos os clientes");
        List<Cliente> clientes = clienteService.findAllClientes();
        log.info("Total de clientes listados: {}", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("getClienteById", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Buscando cliente com ID: {}", id);
        Cliente cliente = clienteService.findClienteById(id);
        if (cliente == null) {
            log.warn("Cliente com ID: {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Cliente encontrado: {}", cliente.getClienteId());
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        if (!rateLimitingService.allowRequisition("updateCliente", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Atualizando cliente com ID: {}", id);
        Cliente updatedCliente = clienteService.updateCliente(clienteDetails);
        if (updatedCliente == null) {
            log.warn("Falha ao atualizar o cliente com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Cliente com ID: {} atualizado com sucesso.", updatedCliente.getClienteId());
        return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        if (!rateLimitingService.allowRequisition("deleteCliente", CAPACITY, WINDOW_IN_SECONDS)) {
            return tooManyRequestsResponse();
        }
        log.info("Excluindo cliente com ID: {}", id);
        boolean isDeleted = clienteService.deleteCliente(id);
        if (!isDeleted) {
            log.warn("Falha ao excluir o cliente com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Cliente com ID: {} excluído com sucesso.", id);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Object> tooManyRequestsResponse() {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("mensagem", "Número de chamadas por minuto excedido, tente novamente em alguns instantes");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(responseMessage);
    }
}
