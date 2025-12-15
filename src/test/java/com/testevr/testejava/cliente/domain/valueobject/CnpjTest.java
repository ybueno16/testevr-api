package com.testevr.testejava.cliente.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CnpjTest {
    @Test
    void testCnpjValido() {
        Cnpj cnpj = new Cnpj("69423022000160");
        assertEquals("69423022000160", cnpj.getValue());
        assertEquals("69.423.022/0001-60", cnpj.getValorFormatado());
    }

    @Test
    void testCnpjNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Cnpj(null));
    }

    @Test
    void testCnpjInvalidoTamanho() {
        assertThrows(IllegalArgumentException.class, () -> new Cnpj("123"));
        assertThrows(IllegalArgumentException.class, () -> new Cnpj("123456789012345"));
    }

    @Test
    void testEqualsAndHashCode() {
        Cnpj c1 = new Cnpj("69423022000160");
        Cnpj c2 = new Cnpj("69423022000160");
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
