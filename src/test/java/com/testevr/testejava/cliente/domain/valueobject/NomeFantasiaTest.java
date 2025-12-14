package com.testevr.testejava.cliente.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NomeFantasiaTest {
    @Test
    void testToStringENullEquals() {
        NomeFantasia n = new NomeFantasia("Fantasia");
        assertTrue(n.toString().contains("Fantasia"));
        assertNotEquals(n, null);
        assertNotEquals(n, "string");
    }
    @Test
    void testNomeFantasiaValido() {
        NomeFantasia n = new NomeFantasia("Fantasia");
        assertEquals("Fantasia", n.getValue());
        assertTrue(n.isPresente());
    }

    @Test
    void testNomeFantasiaNulo() {
        NomeFantasia n = new NomeFantasia(null);
        assertNull(n.getValue());
        assertFalse(n.isPresente());
    }

    @Test
    void testNomeFantasiaVazio() {
        assertThrows(IllegalArgumentException.class, () -> new NomeFantasia(" "));
    }

    @Test
    void testNomeFantasiaMuitoLongo() {
        String longa = "A".repeat(151);
        assertThrows(IllegalArgumentException.class, () -> new NomeFantasia(longa));
    }

    @Test
    void testEqualsAndHashCode() {
        NomeFantasia n1 = new NomeFantasia("F");
        NomeFantasia n2 = new NomeFantasia("F");
        assertEquals(n1, n2);
        assertEquals(n1.hashCode(), n2.hashCode());
    }
}
