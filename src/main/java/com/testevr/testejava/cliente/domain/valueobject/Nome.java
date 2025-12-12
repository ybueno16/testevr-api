package com.testevr.testejava.cliente.domain.valueobject;

import java.util.Objects;

public class Nome {
    private final String valor;

    public Nome(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        if (valor.trim().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        if (valor.trim().length() > 50) {
            throw new IllegalArgumentException("Nome não pode ter mais de 50 caracteres");
        }
        this.valor = valor.trim();
    }

    public String getValue() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nome nome = (Nome) o;
        return Objects.equals(valor, nome.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
