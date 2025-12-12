package com.testevr.testejava.cliente.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public class Cnpj {
    private static final Pattern CNPJ_PATTERN = Pattern.compile("\\d{14}");
    private final String valor;

    public Cnpj(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("CNPJ não pode ser nulo");
        }

        String cnpjLimpo = valor.replaceAll("[^0-9]", "");

        if (!CNPJ_PATTERN.matcher(cnpjLimpo).matches()) {
            throw new IllegalArgumentException("CNPJ deve conter exatamente 14 dígitos");
        }

        if (!isValidCnpj(cnpjLimpo)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        this.valor = cnpjLimpo;
    }

    public String getValue() {
        return valor;
    }

    public String getValorFormatado() {
        return valor.substring(0, 2) + "." +
               valor.substring(2, 5) + "." +
               valor.substring(5, 8) + "/" +
               valor.substring(8, 12) + "-" +
               valor.substring(12, 14);
    }

    private boolean isValidCnpj(String cnpj) {
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] digits = cnpj.chars().map(Character::getNumericValue).toArray();

        int sum = 0;
        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 12; i++) {
            sum += digits[i] * weights1[i];
        }
        int remainder = sum % 11;
        int firstVerifier = remainder < 2 ? 0 : 11 - remainder;

        if (digits[12] != firstVerifier) {
            return false;
        }

        sum = 0;
        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 13; i++) {
            sum += digits[i] * weights2[i];
        }
        remainder = sum % 11;
        int secondVerifier = remainder < 2 ? 0 : 11 - remainder;

        return digits[13] == secondVerifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cnpj cnpj = (Cnpj) o;
        return Objects.equals(valor, cnpj.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return getValorFormatado();
    }
}
