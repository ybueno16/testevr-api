package com.testevr.testejava.cliente.infra.web.controller;

import com.testevr.testejava.cliente.application.dto.ClienteDto;
import com.testevr.testejava.cliente.application.mapper.ClienteMapper;
import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.base-path}/clientes")
public class ClienteController {

    private final ClienteService service;
    private final ClienteMapper mapper;

    public ClienteController (ClienteService service, ClienteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    //  TODO Validar a Necessidade de Ter GlobalExceptionHandler
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDto create(@RequestBody ClienteDto clienteDto) {
        Cliente entity = mapper.toEntity(clienteDto);
        Cliente clienteCriado = service.create(entity);
        return mapper.toDto(clienteCriado);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteDto update(@PathVariable Long id, @RequestBody ClienteDto clienteDto) {
        Cliente entity = mapper.toEntity(clienteDto);
        Cliente clienteAtualizado = service.update(entity);
        return mapper.toDto(clienteAtualizado);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteDto findById(@PathVariable Long id) {
        Cliente cliente = service.findById(id);
        return mapper.toDto(cliente);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ClienteDto findAll() {
        Cliente cliente = service.findAll();
        return mapper.toDto(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
        }
}
