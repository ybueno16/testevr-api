package com.testevr.testejava.venda.internal.application.dto;

import java.math.BigDecimal;

public class VendaExternaRequest {
    private Long clienteId;
    private Long produtoId;
    private BigDecimal valor;
    private Integer quantidade;

    // Construtores
    public VendaExternaRequest() {}

    public VendaExternaRequest(Long clienteId, Long produtoId, BigDecimal valor, Integer quantidade) {
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
