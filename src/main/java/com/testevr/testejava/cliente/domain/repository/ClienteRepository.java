package com.testevr.testejava.cliente.domain.repository;

import com.testevr.testejava.cliente.domain.entity.Cliente;

public interface ClienteRepository {
     Cliente create(Cliente cliente);
     Cliente update(Cliente cliente);
     Cliente findById(Long id);
     Cliente findAll();
     void delete(Long id);
}
