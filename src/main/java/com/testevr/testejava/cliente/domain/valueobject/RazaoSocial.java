package com.testevr.testejava.cliente.domain.valueobject;

import java.util.Objects;

public class RazaoSocial {
    private final String valor;

    public RazaoSocial(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Razão Social não pode ser nula ou vazia");
        }
        if (valor.trim().length() < 2) {
            throw new IllegalArgumentException("Razão Social deve ter pelo menos 2 caracteres");
        }
        if (valor.trim().length() > 150) {
            throw new IllegalArgumentException("Razão Social não pode ter mais de 150 caracteres");
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
        RazaoSocial that = (RazaoSocial) o;
        return Objects.equals(valor, that.valor);
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
