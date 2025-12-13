package com.testevr.testejava.venda.external.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Produto {
    private final Long id;
    private final String descricao;
    private final Integer estoque;
    private final BigDecimal preco;
    private final String unidade;
    private final LocalDateTime ultimaAtualizacao;

    public Produto(Long id, String descricao, Integer estoque, BigDecimal preco, String unidade, LocalDateTime ultimaAtualizacao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do produto não pode ser vazia");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo");
        }
        if (estoque == null || estoque < 0) {
            throw new IllegalArgumentException("Estoque do produto não pode ser negativo");
        }

        this.id = id;
        this.descricao = descricao.trim();
        this.estoque = estoque;
        this.preco = preco;
        this.unidade = unidade != null ? unidade.trim() : null;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getUnidade() {
        return unidade;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", estoque=" + estoque +
                ", preco=" + preco +
                ", unidade='" + unidade + '\'' +
                ", ultimaAtualizacao=" + ultimaAtualizacao +
                '}';
    }
}


