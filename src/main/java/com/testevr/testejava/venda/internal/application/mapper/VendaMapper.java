package com.testevr.testejava.venda.internal.application.mapper;

import com.testevr.testejava.venda.internal.application.dto.CriarVendaDto;
import com.testevr.testejava.venda.internal.application.dto.VendaDto;
import com.testevr.testejava.venda.internal.application.dto.VendaExternaRequest;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import com.testevr.testejava.venda.internal.domain.valueobject.ValorVenda;
import org.springframework.stereotype.Component;

@Component
public class VendaMapper {

    public Venda toEntity(CriarVendaDto dto) {
        if (dto == null) {
            return null;
        }

        ValorVenda valor = new ValorVenda(dto.getValor());
        return new Venda(null, dto.getClienteId(), dto.getProdutoId(), valor, dto.getQuantidade());
    }

    public Venda toEntity(VendaDto dto) {
        if (dto == null) {
            return null;
        }

        ValorVenda valor = new ValorVenda(dto.getValor());

        if (dto.getId() != null && dto.getStatus() != null) {
            StatusVenda status = StatusVenda.valueOf(dto.getStatus().toUpperCase());
            return new Venda(dto.getId(), dto.getClienteId(), dto.getProdutoId(), valor,
                           dto.getQuantidade(), status);
        }

        return new Venda(dto.getId(), dto.getClienteId(), dto.getProdutoId(), valor, dto.getQuantidade());
    }

    public VendaDto toDto(Venda venda) {
        if (venda == null) {
            return null;
        }

        return new VendaDto(
            venda.getIdValue(),
            venda.getClienteId(),
            venda.getProdutoId(),
            venda.getValor().getValor(),
            venda.getQuantidade(),
            venda.getStatus().name()
        );
    }
}


