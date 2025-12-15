package com.testevr.testejava.venda.internal.domain.entity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.testevr.testejava.shared.domain.valueobject.Id;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import com.testevr.testejava.venda.internal.domain.valueobject.ValorVenda;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class VendaTest {

    @Mock
    private ValorVenda valorVenda;

    @Test
    void deveCriarVendaComIdNulo() {
        Venda venda = new Venda(null, 1L, 2L, valorVenda, 3);

        assertNull(venda.getId());
        assertNull(venda.getIdValue());
        assertEquals(1L, venda.getClienteId());
        assertEquals(2L, venda.getProdutoId());
        assertEquals(valorVenda, venda.getValor());
        assertEquals(3, venda.getQuantidade());
        assertEquals(StatusVenda.PENDENTE, venda.getStatus());
        assertNotNull(venda.getCreatedAt());
        assertNotNull(venda.getUpdatedAt());
    }

    @Test
    void deveCriarVendaComId() {
        Venda venda = new Venda(100L, 1L, 2L, valorVenda, 3);

        Id id = venda.getId();
        assertEquals(100L, id.getValue());
        assertEquals(100L, venda.getIdValue());
        assertEquals(1L, venda.getClienteId());
        assertEquals(2L, venda.getProdutoId());
        assertEquals(valorVenda, venda.getValor());
        assertEquals(3, venda.getQuantidade());
        assertEquals(StatusVenda.PENDENTE, venda.getStatus());
        assertNotNull(venda.getCreatedAt());
        assertNotNull(venda.getUpdatedAt());
    }

    @Test
    void deveCriarVendaComStatusPersonalizado() {
        Venda venda = new Venda(100L, 1L, 2L, valorVenda, 3, StatusVenda.CONCLUIDA);

        assertEquals(100L, venda.getIdValue());
        assertEquals(1L, venda.getClienteId());
        assertEquals(2L, venda.getProdutoId());
        assertEquals(valorVenda, venda.getValor());
        assertEquals(3, venda.getQuantidade());
        assertEquals(StatusVenda.CONCLUIDA, venda.getStatus());
        assertNotNull(venda.getCreatedAt());
        assertNotNull(venda.getUpdatedAt());
    }

    @Test
    void deveCriarVendaComDatasPersonalizadas() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        Venda venda = new Venda(100L, 1L, 2L, valorVenda, 3, StatusVenda.PENDENTE, createdAt, updatedAt);

        assertEquals(100L, venda.getIdValue());
        assertEquals(1L, venda.getClienteId());
        assertEquals(2L, venda.getProdutoId());
        assertEquals(valorVenda, venda.getValor());
        assertEquals(3, venda.getQuantidade());
        assertEquals(StatusVenda.PENDENTE, venda.getStatus());
        assertEquals(createdAt, venda.getCreatedAt());
        assertEquals(updatedAt, venda.getUpdatedAt());
    }

    @Test
    void deveAtualizarStatus() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        Venda venda = new Venda(100L, 1L, 2L, valorVenda, 3, StatusVenda.PENDENTE, createdAt, updatedAt);

        Venda vendaAtualizada = venda.atualizarStatus(StatusVenda.CONCLUIDA);

        assertEquals(100L, vendaAtualizada.getIdValue());
        assertEquals(1L, vendaAtualizada.getClienteId());
        assertEquals(2L, vendaAtualizada.getProdutoId());
        assertEquals(valorVenda, vendaAtualizada.getValor());
        assertEquals(3, vendaAtualizada.getQuantidade());
        assertEquals(StatusVenda.CONCLUIDA, vendaAtualizada.getStatus());
        assertEquals(createdAt, vendaAtualizada.getCreatedAt());
        assertNotEquals(updatedAt, vendaAtualizada.getUpdatedAt());
        assertTrue(vendaAtualizada.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    void deveAtualizarId() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        Venda venda = new Venda(null, 1L, 2L, valorVenda, 3, StatusVenda.PENDENTE, createdAt, updatedAt);

        Venda vendaAtualizada = venda.atualizarId(200L);

        assertEquals(200L, vendaAtualizada.getIdValue());
        assertEquals(1L, vendaAtualizada.getClienteId());
        assertEquals(2L, vendaAtualizada.getProdutoId());
        assertEquals(valorVenda, vendaAtualizada.getValor());
        assertEquals(3, vendaAtualizada.getQuantidade());
        assertEquals(StatusVenda.PENDENTE, vendaAtualizada.getStatus());
        assertEquals(createdAt, vendaAtualizada.getCreatedAt());
        assertEquals(updatedAt, vendaAtualizada.getUpdatedAt());
    }

    @Test
    void deveCompararVendasPeloId() {
        Venda venda1 = new Venda(100L, 1L, 2L, valorVenda, 3);
        Venda venda2 = new Venda(100L, 3L, 4L, valorVenda, 5);
        Venda venda3 = new Venda(200L, 1L, 2L, valorVenda, 3);

        assertEquals(venda1, venda2);
        assertNotEquals(venda1, venda3);
        assertEquals(venda1.hashCode(), venda2.hashCode());
        assertNotEquals(venda1.hashCode(), venda3.hashCode());
    }

    @Test
    void deveRetornarStringFormatada() {
        when(valorVenda.toString()).thenReturn("R$ 100,00");

        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        Venda venda = new Venda(100L, 1L, 2L, valorVenda, 3, StatusVenda.PENDENTE, createdAt, updatedAt);

        String resultado = venda.toString();

        assertTrue(resultado.contains("id=100"));
        assertTrue(resultado.contains("clienteId=1"));
        assertTrue(resultado.contains("produtoId=2"));
        assertTrue(resultado.contains("valor=R$ 100,00"));
        assertTrue(resultado.contains("quantidade=3"));
        assertTrue(resultado.contains("status=PENDENTE"));
        assertTrue(resultado.contains("createdAt=2024-01-01T10:00"));
        assertTrue(resultado.contains("updatedAt=2024-01-01T10:00"));
    }

    @Test
    void deveRetornarIdObjetoQuandoIdExiste() {
        Venda venda = new Venda(100L, 1L, 2L, valorVenda, 3);

        Id id = venda.getId();

        assertNotNull(id);
        assertEquals(100L, id.getValue());
    }

    @Test
    void deveRetornarNullParaIdQuandoIdNulo() {
        Venda venda = new Venda(null, 1L, 2L, valorVenda, 3);

        Id id = venda.getId();

        assertNull(id);
    }
}