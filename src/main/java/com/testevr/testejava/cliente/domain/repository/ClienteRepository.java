package com.testevr.testejava.cliente.domain.repository;

import com.testevr.testejava.cliente.domain.entity.Cliente;

import java.util.List;

public interface ClienteRepository {
     Cliente create(Cliente cliente);
     Cliente update(Cliente cliente);
     Cliente findById(Long id);
     List<Cliente> findAll();
     void delete(Long id);
}
