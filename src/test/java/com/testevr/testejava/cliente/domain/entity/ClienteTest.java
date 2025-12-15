package com.testevr.testejava.cliente.domain.entity;

import com.testevr.testejava.cliente.domain.valueobject.Cnpj;
import com.testevr.testejava.cliente.domain.valueobject.Nome;
import com.testevr.testejava.cliente.domain.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.domain.valueobject.RazaoSocial;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    @Test
    void testEqualsComNullEOutroTipo() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        assertNotEquals(cliente, null);
        assertNotEquals(cliente, "string");
    }

    @Test
    void testAtualizarIdComNull() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        Cliente atualizado = cliente.atualizarId(null);
        assertNull(atualizado.getId());
        assertEquals(cliente.getNome(), atualizado.getNome());
    }

    @Test
    void testConstrutorComId() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        assertNotNull(cliente.getId());
        assertEquals("Teste", cliente.getNome().getValue());
        assertEquals("Razao", cliente.getRazaoSocial().getValue());
        assertEquals("Fantasia", cliente.getNomeFantasia().getValue());
        assertEquals("69423022000160", cliente.getCnpj().getValue());
        assertTrue(cliente.isAtivo());
        assertNotNull(cliente.getCreatedAt());
        assertNotNull(cliente.getUpdatedAt());
    }

    @Test
    void testConstrutorCompleto() {
        LocalDateTime now = LocalDateTime.now();
        Cliente cliente = new Cliente(2L, new Nome("Nome2"), new RazaoSocial("Razao2"), new NomeFantasia("Fantasia2"), new Cnpj("69423022000160"), false, now, now);
        assertEquals(2L, cliente.getId().getValue());
        assertEquals("Nome2", cliente.getNome().getValue());
        assertEquals("Razao2", cliente.getRazaoSocial().getValue());
        assertEquals("Fantasia2", cliente.getNomeFantasia().getValue());
        assertEquals("69423022000160", cliente.getCnpj().getValue());
        assertFalse(cliente.isAtivo());
        assertEquals(now, cliente.getCreatedAt());
        assertEquals(now, cliente.getUpdatedAt());
    }

    @Test
    void testAtualizarId() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        Cliente atualizado = cliente.atualizarId(99L);
        assertEquals(99L, atualizado.getId().getValue());
        assertEquals(cliente.getNome(), atualizado.getNome());
    }

    @Test
    void testToString() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        assertTrue(cliente.toString().contains("Cliente{"));
    }
}
