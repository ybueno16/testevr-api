package com.testevr.testejava.venda.internal.application.dto;

import java.math.BigDecimal;

public class VendaConsolidadaDto {
    private Long clienteId;
    private String status;
    private BigDecimal valorTotal;
    private Integer quantidadeVendas;

    // Construtores
    public VendaConsolidadaDto() {}

    public VendaConsolidadaDto(Long clienteId, String status, BigDecimal valorTotal, Integer quantidadeVendas) {
        this.clienteId = clienteId;
        this.status = status;
        this.valorTotal = valorTotal;
        this.quantidadeVendas = quantidadeVendas;
    }

    // Getters e Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getQuantidadeVendas() {
        return quantidadeVendas;
    }

    public void setQuantidadeVendas(Integer quantidadeVendas) {
        this.quantidadeVendas = quantidadeVendas;
    }
}
