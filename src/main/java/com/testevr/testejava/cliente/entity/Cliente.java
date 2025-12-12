package com.testevr.testejava.cliente.entity;

import com.testevr.testejava.cliente.valueobject.Nome;
import com.testevr.testejava.cliente.valueobject.RazaoSocial;
import com.testevr.testejava.cliente.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.valueobject.Cnpj;
import com.testevr.testejava.shared.valueobject.Id;

import java.util.Objects;

public class Cliente {
    private final Id id;
    private final Nome nome;
    private final RazaoSocial razaoSocial;
    private final NomeFantasia nomeFantasia;
    private final Cnpj cnpj;
    private final boolean ativo;

    public Cliente(Long id, Nome nome, RazaoSocial razaoSocial, NomeFantasia nomeFantasia, Cnpj cnpj) {
        this.id = new Id(id);
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.ativo = true;
    }

    public Cliente(Long id, Nome nome, RazaoSocial razaoSocial, NomeFantasia nomeFantasia, Cnpj cnpj, boolean ativo) {
        this.id = new Id(id);
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.ativo = ativo;
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
                '}';
    }
}
