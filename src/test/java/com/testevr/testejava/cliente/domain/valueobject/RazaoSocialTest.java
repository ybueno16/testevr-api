package com.testevr.testejava.cliente.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RazaoSocialTest {
    @Test
    void testToStringENullEquals() {
        RazaoSocial r = new RazaoSocial("Razao");
        assertTrue(r.toString().contains("Razao"));
        assertNotEquals(r, null);
        assertNotEquals(r, "string");
    }
    @Test
    void testRazaoSocialValida() {
        RazaoSocial r = new RazaoSocial("Empresa Teste");
        assertEquals("Empresa Teste", r.getValue());
    }

    @Test
    void testRazaoSocialNulaOuVazia() {
        assertThrows(IllegalArgumentException.class, () -> new RazaoSocial(null));
        assertThrows(IllegalArgumentException.class, () -> new RazaoSocial(" "));
    }

    @Test
    void testRazaoSocialMuitoCurta() {
        assertThrows(IllegalArgumentException.class, () -> new RazaoSocial("A"));
    }

    @Test
    void testRazaoSocialMuitoLonga() {
        String longa = "A".repeat(151);
        assertThrows(IllegalArgumentException.class, () -> new RazaoSocial(longa));
    }

    @Test
    void testEqualsAndHashCode() {
        RazaoSocial r1 = new RazaoSocial("Empresa");
        RazaoSocial r2 = new RazaoSocial("Empresa");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
