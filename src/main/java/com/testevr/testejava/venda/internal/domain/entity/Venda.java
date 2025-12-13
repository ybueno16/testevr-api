package com.testevr.testejava.venda.internal.domain.entity;

import com.testevr.testejava.shared.domain.valueobject.Id;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import com.testevr.testejava.venda.internal.domain.valueobject.ValorVenda;

import java.time.LocalDateTime;
import java.util.Objects;

public class Venda {
    private final Id id;
    private final Long clienteId;
    private final Long produtoId;
    private final ValorVenda valor;
    private final Integer quantidade;
    private final StatusVenda status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Venda(Long id, Long clienteId, Long produtoId, ValorVenda valor, Integer quantidade) {
        this.id = new Id(id);
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.valor = valor;
        this.quantidade = quantidade;
        this.status = StatusVenda.PENDENTE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Venda(Long id, Long clienteId, Long produtoId, ValorVenda valor, Integer quantidade,
                 StatusVenda status) {
        this.id = new Id(id);
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.valor = valor;
        this.quantidade = quantidade;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Venda(Long id, Long clienteId, Long produtoId, ValorVenda valor, Integer quantidade,
                 StatusVenda status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = new Id(id);
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.valor = valor;
        this.quantidade = quantidade;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Venda atualizarStatus(StatusVenda novoStatus) {
        return new Venda(this.id.getValue(), this.clienteId, this.produtoId, this.valor,
                        this.quantidade, novoStatus, this.createdAt, LocalDateTime.now());
    }

    public Id getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public ValorVenda getValor() {
        return valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Objects.equals(id, venda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", produtoId=" + produtoId +
                ", valor=" + valor +
                ", quantidade=" + quantidade +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
