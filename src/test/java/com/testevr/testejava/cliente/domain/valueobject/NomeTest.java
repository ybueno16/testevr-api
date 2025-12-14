package com.testevr.testejava.cliente.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NomeTest {
    @Test
    void testToStringENullEquals() {
        Nome nome = new Nome("Teste");
        assertTrue(nome.toString().contains("Teste"));
        assertNotEquals(nome, null);
        assertNotEquals(nome, "string");
    }
    @Test
    void testNomeValido() {
        Nome nome = new Nome("João");
        assertEquals("João", nome.getValue());
    }

    @Test
    void testNomeNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Nome(null));
        assertThrows(IllegalArgumentException.class, () -> new Nome(" "));
    }

    @Test
    void testNomeMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () -> new Nome("A"));
    }

    @Test
    void testNomeMuitoLongo() {
        String longo = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> new Nome(longo));
    }

    @Test
    void testEqualsAndHashCode() {
        Nome n1 = new Nome("Maria");
        Nome n2 = new Nome("Maria");
        assertEquals(n1, n2);
        assertEquals(n1.hashCode(), n2.hashCode());
    }
}
