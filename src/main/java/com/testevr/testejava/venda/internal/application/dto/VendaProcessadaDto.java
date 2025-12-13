package com.testevr.testejava.venda.internal.application.dto;

import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VendaProcessadaDto {
    private Long id;
    private Long clienteId;
    private Long produtoId;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private StatusVenda status;
    private LocalDateTime dataVenda;
    private LocalDateTime dataProcessamento;
    private String erro;

    // Construtores
    public VendaProcessadaDto() {}

    public VendaProcessadaDto(Long clienteId, Long produtoId, Integer quantidade,
                              BigDecimal valorUnitario, StatusVenda status) {
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.status = status;
        this.dataVenda = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public StatusVenda getStatus() { return status; }
    public void setStatus(StatusVenda status) { this.status = status; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public LocalDateTime getDataProcessamento() { return dataProcessamento; }
    public void setDataProcessamento(LocalDateTime dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public String getErro() { return erro; }
    public void setErro(String erro) { this.erro = erro; }
}