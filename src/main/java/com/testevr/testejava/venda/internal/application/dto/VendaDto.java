package com.testevr.testejava.venda.internal.application.dto;

import java.math.BigDecimal;

public class VendaDto {
    private Long id;
    private Long clienteId;
    private Long produtoId;
    private BigDecimal valor;
    private Integer quantidade;
    private String status;

    public VendaDto() {}

    public VendaDto(Long id, Long clienteId, Long produtoId, BigDecimal valor, Integer quantidade, String status) {
        this.id = id;
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.valor = valor;
        this.quantidade = quantidade;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
