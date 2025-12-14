package com.testevr.testejava.cliente.application.mapper;

import com.testevr.testejava.cliente.application.dto.ClienteDto;
import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.valueobject.Cnpj;
import com.testevr.testejava.cliente.domain.valueobject.Nome;
import com.testevr.testejava.cliente.domain.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.domain.valueobject.RazaoSocial;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClienteMapperTest {
    @Test
    void testToEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void testToDtoNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void testMapRow() throws Exception {
        java.sql.ResultSet rs = mock(java.sql.ResultSet.class);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("nome")).thenReturn("Nome");
        when(rs.getString("razao_social")).thenReturn("Razao");
        when(rs.getString("nome_fantasia")).thenReturn("Fantasia");
        when(rs.getString("cnpj")).thenReturn("69423022000160");
        when(rs.getBoolean("ativo")).thenReturn(true);
        Cliente entity = mapper.mapRow(rs);
        assertNotNull(entity);
        assertEquals("Nome", entity.getNome().getValue());
        assertEquals("Razao", entity.getRazaoSocial().getValue());
        assertEquals("Fantasia", entity.getNomeFantasia().getValue());
        assertEquals("69423022000160", entity.getCnpj().getValue());
        assertTrue(entity.isAtivo());
    }
    private final ClienteMapper mapper = new ClienteMapper();

    @Test
    void testToEntityComId() {
        LocalDateTime now = LocalDateTime.now();
        ClienteDto dto = new ClienteDto(1L, "Nome", "Razao", "Fantasia", "69423022000160", true, now, now);
        Cliente entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals("Nome", entity.getNome().getValue());
        assertEquals("Razao", entity.getRazaoSocial().getValue());
        assertEquals("Fantasia", entity.getNomeFantasia().getValue());
        assertEquals("69423022000160", entity.getCnpj().getValue());
        assertTrue(entity.isAtivo());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testToEntitySemId() {
        ClienteDto dto = new ClienteDto(null, "Nome", "Razao", "Fantasia", "69423022000160", true);
        Cliente entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Nome", entity.getNome().getValue());
    }

    @Test
    void testToDto() {
        LocalDateTime now = LocalDateTime.now();
        Cliente entity = new Cliente(1L, new Nome("Nome"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"), true, now, now);
        ClienteDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals("Razao", dto.getRazaoSocial());
        assertEquals("Fantasia", dto.getNomeFantasia());
        assertEquals("69423022000160", dto.getCnpj());
        assertTrue(dto.getAtivo());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }
}
