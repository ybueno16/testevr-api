package com.testevr.testejava.cliente.domain.service;

import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService (ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente create(Cliente cliente) {
        return this.repository.create(cliente);
    }

    public Cliente update(Cliente cliente) {
        return this.repository.update(cliente);
    }

    public Cliente findById(Long id) {
        return this.repository.findById(id);
    }

    public Cliente findAll() {
        return this.repository.findAll();
    }

    public void delete(Long id) {
        this.repository.delete(id);
    }
}
