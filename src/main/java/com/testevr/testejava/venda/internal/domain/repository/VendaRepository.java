package com.testevr.testejava.venda.internal.domain.repository;

import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;

import java.util.List;

public interface VendaRepository {
    Venda create(Venda venda);
    Venda update(Venda venda);
    Venda findById(Long id);
    List<Venda> findAll();
    List<Venda> findByClienteId(Long clienteId);
    List<VendaConsolidadaDto> buscarVendasConsolidadas();
    List<VendaConsolidadaDto> buscarVendasConsolidadasPorCliente(Long clienteId);
    void delete(Long id);
}
