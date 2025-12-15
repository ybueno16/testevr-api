package com.testevr.testejava.cliente.application.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ClienteDtoTest {
    @Test
    void testConstrutorVazioESettersNull() {
        ClienteDto dto = new ClienteDto();
        dto.setId(null);
        dto.setNome(null);
        dto.setRazaoSocial(null);
        dto.setNomeFantasia(null);
        dto.setCnpj(null);
        dto.setAtivo(null);
        dto.setCreatedAt(null);
        dto.setUpdatedAt(null);
        assertNull(dto.getId());
        assertNull(dto.getNome());
        assertNull(dto.getRazaoSocial());
        assertNull(dto.getNomeFantasia());
        assertNull(dto.getCnpj());
        assertNull(dto.getAtivo());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }


    @Test
    void testConstrutoresEGettersSetters() {
        LocalDateTime now = LocalDateTime.now();
    ClienteDto dto = new ClienteDto(1L, "Nome", "Razao", "Fantasia", "69423022000160", true, now, now);
        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals("Razao", dto.getRazaoSocial());
        assertEquals("Fantasia", dto.getNomeFantasia());
    assertEquals("69423022000160", dto.getCnpj());
        assertTrue(dto.getAtivo());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());

        dto.setId(2L);
        dto.setNome("NovoNome");
        dto.setRazaoSocial("NovaRazao");
        dto.setNomeFantasia("NovaFantasia");
    dto.setCnpj("69423022000160");
        dto.setAtivo(false);
        LocalDateTime novoNow = LocalDateTime.now();
        dto.setCreatedAt(novoNow);
        dto.setUpdatedAt(novoNow);
        assertEquals(2L, dto.getId());
        assertEquals("NovoNome", dto.getNome());
        assertEquals("NovaRazao", dto.getRazaoSocial());
        assertEquals("NovaFantasia", dto.getNomeFantasia());
    assertEquals("69423022000160", dto.getCnpj());
        assertFalse(dto.getAtivo());
        assertEquals(novoNow, dto.getCreatedAt());
        assertEquals(novoNow, dto.getUpdatedAt());
    }
}
