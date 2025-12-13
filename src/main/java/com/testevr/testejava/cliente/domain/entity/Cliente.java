package com.testevr.testejava.cliente.domain.entity;

import com.testevr.testejava.cliente.domain.valueobject.Nome;
import com.testevr.testejava.cliente.domain.valueobject.RazaoSocial;
import com.testevr.testejava.cliente.domain.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.domain.valueobject.Cnpj;
import com.testevr.testejava.shared.domain.valueobject.Id;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cliente {
    private final Id id;
    private final Nome nome;
    private final RazaoSocial razaoSocial;
    private final NomeFantasia nomeFantasia;
    private final Cnpj cnpj;
    private final boolean ativo;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Cliente(Long id, Nome nome, RazaoSocial razaoSocial, NomeFantasia nomeFantasia, Cnpj cnpj) {
        this.id = id != null ? new Id(id) : null;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.ativo = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cliente(Long id, Nome nome, RazaoSocial razaoSocial, NomeFantasia nomeFantasia, Cnpj cnpj, boolean ativo) {
        this.id = id != null ? new Id(id) : null;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.ativo = ativo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cliente(Long id, Nome nome, RazaoSocial razaoSocial, NomeFantasia nomeFantasia, Cnpj cnpj, boolean ativo,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id != null ? new Id(id) : null;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.ativo = ativo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Id getId() {
        return id;
    }

    public Nome getNome() {
        return nome;
    }

    public RazaoSocial getRazaoSocial() {
        return razaoSocial;
    }

    public NomeFantasia getNomeFantasia() {
        return nomeFantasia;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Cliente atualizarId(Long novoId) {
        return new Cliente(novoId, this.nome, this.razaoSocial, this.nomeFantasia, 
                          this.cnpj, this.ativo, this.createdAt, this.updatedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome=" + nome +
                ", razaoSocial=" + razaoSocial +
                ", nomeFantasia=" + nomeFantasia +
                ", cnpj=" + cnpj +
                ", ativo=" + ativo +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}