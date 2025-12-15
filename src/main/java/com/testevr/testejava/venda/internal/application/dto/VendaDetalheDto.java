package com.testevr.testejava.venda.internal.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VendaDetalheDto {
    private Long id;
    private String clienteNome;
    private String status;
    private LocalDateTime data;
    private BigDecimal valorTotal;

    public VendaDetalheDto() {}

    public VendaDetalheDto(Long id, String clienteNome, String status, LocalDateTime data, BigDecimal valorTotal) {
        this.id = id;
        this.clienteNome = clienteNome;
        this.status = status;
        this.data = data;
        this.valorTotal = valorTotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}
