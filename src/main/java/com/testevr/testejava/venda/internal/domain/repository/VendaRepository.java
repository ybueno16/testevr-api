package com.testevr.testejava.venda.internal.domain.repository;

import com.testevr.testejava.venda.internal.domain.entity.Venda;

import java.util.List;

public interface VendaRepository {
    Venda create(Venda venda);
    Venda update(Venda venda);
    Venda findById(Long id);
    List<Venda> findAll();
    List<Venda> findByClienteId(Long clienteId);
    void delete(Long id);
}
