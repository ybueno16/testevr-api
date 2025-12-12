package com.testevr.testejava.shared.valueobject;

import java.util.Objects;

public class Id {
    private final Long valor;

    public Id(Long valor) {
        if (valor == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        this.valor = valor;
    }

    public Long getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id id = (Id) o;
        return Objects.equals(valor, id.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
