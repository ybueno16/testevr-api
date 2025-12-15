package com.testevr.testejava.cliente.web;

import com.testevr.testejava.cliente.application.dto.ClienteDto;
import com.testevr.testejava.cliente.application.mapper.ClienteMapper;
import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${app.api.base-path}/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper mapper;

    public ClienteController(ClienteService clienteService, ClienteMapper mapper) {
        this.clienteService = clienteService;
        this.mapper = mapper;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ClienteDto> criarCliente(@RequestBody ClienteDto clienteDto) {
        try {
            Cliente clienteEntity = mapper.toEntity(clienteDto);
            Cliente clienteCriado = clienteService.create(clienteEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(clienteCriado));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar cliente: " + e.getMessage(), e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDto(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> buscarTodos() {
        List<Cliente> clientes = clienteService.findAll();
        List<ClienteDto> clientesDto = clientes.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientesDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> atualizarCliente(@PathVariable Long id, @RequestBody ClienteDto clienteDto) {
        try {
            Cliente clienteExistente = clienteService.findById(id);
            if (clienteExistente == null) {
                return ResponseEntity.notFound().build();
            }

            Cliente clienteEntity = mapper.toEntity(clienteDto);
            clienteEntity = clienteEntity.atualizarId(id);
            Cliente clienteAtualizado = clienteService.update(clienteEntity);
            
            return ResponseEntity.ok(mapper.toDto(clienteAtualizado));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        clienteService.delete(id);
    }
}
