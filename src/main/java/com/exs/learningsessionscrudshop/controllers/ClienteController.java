package com.exs.learningsessionscrudshop.controllers;

import com.exs.learningsessionscrudshop.models.Cliente;
import com.exs.learningsessionscrudshop.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cria um novo cliente", description = "Adiciona um novo cliente ao sistema")
    @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Cliente.class)))
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.saveCliente(cliente));
    }

    @GetMapping
    @Operation(summary = "Lista todos os clientes", description = "Retorna uma lista de todos os clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtida com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Cliente.class)))
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(clienteService.findAllClientes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém um cliente pelo ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Cliente.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<Cliente> getClienteById(@Parameter(description = "ID do cliente") @PathVariable Long id) {
        Cliente cliente = clienteService.findClienteById(id);
        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Cliente.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<Cliente> updateCliente(@Parameter(description = "ID do cliente") @PathVariable Long id,
                                                 @RequestBody Cliente clienteDetails) {
        Cliente cliente = clienteService.findClienteById(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        // TODO (Atualizar propriedades do cliente...)
        return ResponseEntity.ok(clienteService.updateCliente(cliente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um cliente", description = "Remove um cliente do sistema pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Cliente excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<Void> deleteCliente(@Parameter(description = "ID do cliente") @PathVariable Long id) {
        Cliente cliente = clienteService.findClienteById(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        clienteService.deleteCliente(id);
        return ResponseEntity.ok().build();
    }
}
