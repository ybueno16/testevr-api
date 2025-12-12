package com.testevr.testejava.cliente.valueobject;

import java.util.Objects;

public class NomeFantasia {
    private final String valor;

    public NomeFantasia(String valor) {
        if (valor != null && valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome Fantasia não pode ser uma string vazia");
        }
        if (valor != null && valor.trim().length() > 150) {
            throw new IllegalArgumentException("Nome Fantasia não pode ter mais de 150 caracteres");
        }
        this.valor = valor != null ? valor.trim() : null;
    }

    public String getValor() {
        return valor;
    }

    public boolean isPresente() {
        return valor != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NomeFantasia that = (NomeFantasia) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor != null ? valor : "";
    }
}
