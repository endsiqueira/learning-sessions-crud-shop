package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Cliente;
import com.exs.learningsessionscrudshop.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cria um novo cliente", description = "Adiciona um novo cliente ao sistema")
    @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        log.info("Iniciando criação do cliente: {}", cliente.getNome());
        Cliente savedCliente = clienteService.saveCliente(cliente);
        log.info("Cliente criado com sucesso: {}", savedCliente.getClienteId());
        return ResponseEntity.ok(savedCliente);
    }

    @GetMapping
    @Operation(summary = "Lista todos os clientes", description = "Retorna uma lista de todos os clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtida com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    public ResponseEntity<List<Cliente>> getAllClientes() {
        log.info("Listando todos os clientes");
        List<Cliente> clientes = clienteService.findAllClientes();
        log.info("Total de clientes listados: {}", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém um cliente pelo ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<Cliente> getClienteById(@Parameter(description = "ID do cliente") @PathVariable Long id) {
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
    @Operation(summary = "Atualiza um cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<Cliente> updateCliente(@Parameter(description = "ID do cliente") @PathVariable Long id,
                                                 @RequestBody Cliente clienteDetails) {
        log.info("Atualizando cliente com ID: {}", id);
        Cliente cliente = clienteService.updateCliente(clienteDetails);
        if (cliente == null) {
            log.warn("Falha ao atualizar o cliente com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Cliente com ID: {} atualizado com sucesso.", cliente.getClienteId());
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um cliente", description = "Remove um cliente do sistema pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Cliente excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<Void> deleteCliente(@Parameter(description = "ID do cliente") @PathVariable Long id) {
        log.info("Excluindo cliente com ID: {}", id);
        boolean isDeleted = clienteService.deleteCliente(id);
        if (!isDeleted) {
            log.warn("Falha ao excluir o cliente com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Cliente com ID: {} excluído com sucesso.", id);
        return ResponseEntity.ok().build();
    }
}
