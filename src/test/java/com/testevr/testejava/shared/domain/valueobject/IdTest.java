package com.testevr.testejava.shared.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdTest {
    @Test
    void testIdValido() {
        Id id = new Id(10L);
        assertEquals(10L, id.getValue());
        assertEquals("10", id.toString());
    }

    @Test
    void testIdNulo() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new Id(null));
        assertTrue(ex.getMessage().contains("ID não pode ser nulo"));
    }

    @Test
    void testIdNegativoOuZero() {
        assertThrows(IllegalArgumentException.class, () -> new Id(0L));
        assertThrows(IllegalArgumentException.class, () -> new Id(-1L));
    }

    @Test
    void testEqualsAndHashCode() {
        Id id1 = new Id(5L);
        Id id2 = new Id(5L);
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, null);
        assertNotEquals(id1, "string");
    }
}
