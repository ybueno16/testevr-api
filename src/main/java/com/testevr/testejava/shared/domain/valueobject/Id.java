package com.testevr.testejava.shared.domain.valueobject;

import java.util.Objects;

public class Id {
    private final Long valor;

    public Id(Long valor) {
        if (valor == null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            System.err.println("=== DEBUG: Tentando criar ID com valor nulo ===");
            for (int i = 0; i < Math.min(stackTrace.length, 10); i++) {
                System.err.println("  " + stackTrace[i]);
            }
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        this.valor = valor;
    }

    public Long getValue() {
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
