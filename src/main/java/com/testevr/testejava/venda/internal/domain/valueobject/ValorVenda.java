package com.testevr.testejava.venda.internal.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public class ValorVenda {
    private final BigDecimal valor;

    public ValorVenda(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da venda deve ser maior que zero");
        }
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValorVenda that = (ValorVenda) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return "ValorVenda{" +
                "valor=" + valor +
                '}';
    }
}
