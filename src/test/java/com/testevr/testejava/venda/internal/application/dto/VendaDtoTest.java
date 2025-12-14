package com.testevr.testejava.venda.internal.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class VendaDtoTest {

    @Test
    void testNoArgsConstructor() {
        VendaDto dto = new VendaDto();

        assertNull(dto.getId());
        assertNull(dto.getClienteId());
        assertNull(dto.getProdutoId());
        assertNull(dto.getValor());
        assertNull(dto.getQuantidade());
        assertNull(dto.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        Long clienteId = 10L;
        Long produtoId = 100L;
        BigDecimal valor = new BigDecimal("99.99");
        Integer quantidade = 5;
        String status = "CONCLUIDO";

        VendaDto dto = new VendaDto(id, clienteId, produtoId, valor, quantidade, status);

        assertEquals(id, dto.getId());
        assertEquals(clienteId, dto.getClienteId());
        assertEquals(produtoId, dto.getProdutoId());
        assertEquals(valor, dto.getValor());
        assertEquals(quantidade, dto.getQuantidade());
        assertEquals(status, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        VendaDto dto = new VendaDto();

        Long id = 2L;
        Long clienteId = 20L;
        Long produtoId = 200L;
        BigDecimal valor = new BigDecimal("149.50");
        Integer quantidade = 3;
        String status = "PENDENTE";

        dto.setId(id);
        dto.setClienteId(clienteId);
        dto.setProdutoId(produtoId);
        dto.setValor(valor);
        dto.setQuantidade(quantidade);
        dto.setStatus(status);

        assertEquals(id, dto.getId());
        assertEquals(clienteId, dto.getClienteId());
        assertEquals(produtoId, dto.getProdutoId());
        assertEquals(valor, dto.getValor());
        assertEquals(quantidade, dto.getQuantidade());
        assertEquals(status, dto.getStatus());
    }

    @Test
    void testSetId() {
        VendaDto dto = new VendaDto();
        dto.setId(10L);
        assertEquals(10L, dto.getId());
    }

    @Test
    void testSetClienteId() {
        VendaDto dto = new VendaDto();
        dto.setClienteId(30L);
        assertEquals(30L, dto.getClienteId());
    }

    @Test
    void testSetProdutoId() {
        VendaDto dto = new VendaDto();
        dto.setProdutoId(50L);
        assertEquals(50L, dto.getProdutoId());
    }

    @Test
    void testSetValor() {
        VendaDto dto = new VendaDto();
        BigDecimal valor = new BigDecimal("29.99");
        dto.setValor(valor);
        assertEquals(valor, dto.getValor());
    }

    @Test
    void testSetQuantidade() {
        VendaDto dto = new VendaDto();
        dto.setQuantidade(10);
        assertEquals(10, dto.getQuantidade());
    }

    @Test
    void testSetStatus() {
        VendaDto dto = new VendaDto();
        dto.setStatus("CANCELADO");
        assertEquals("CANCELADO", dto.getStatus());
    }

    @Test
    void testGetId() {
        VendaDto dto = new VendaDto(15L, 1L, 75L, new BigDecimal("19.99"), 2, "CONCLUIDO");
        assertEquals(15L, dto.getId());
    }

    @Test
    void testGetClienteId() {
        VendaDto dto = new VendaDto(1L, 25L, 99L, new BigDecimal("49.99"), 1, "CONCLUIDO");
        assertEquals(25L, dto.getClienteId());
    }

    @Test
    void testGetProdutoId() {
        VendaDto dto = new VendaDto(1L, 1L, 88L, new BigDecimal("79.99"), 1, "CONCLUIDO");
        assertEquals(88L, dto.getProdutoId());
    }

    @Test
    void testGetValor() {
        BigDecimal valor = new BigDecimal("79.99");
        VendaDto dto = new VendaDto(1L, 1L, 2L, valor, 1, "CONCLUIDO");
        assertEquals(valor, dto.getValor());
    }

    @Test
    void testGetQuantidade() {
        VendaDto dto = new VendaDto(1L, 1L, 2L, new BigDecimal("9.99"), 7, "CONCLUIDO");
        assertEquals(7, dto.getQuantidade());
    }

    @Test
    void testGetStatus() {
        VendaDto dto = new VendaDto(1L, 1L, 2L, new BigDecimal("9.99"), 1, "EM_PROCESSAMENTO");
        assertEquals("EM_PROCESSAMENTO", dto.getStatus());
    }

    @Test
    void testNullValues() {
        VendaDto dto = new VendaDto(null, null, null, null, null, null);

        assertNull(dto.getId());
        assertNull(dto.getClienteId());
        assertNull(dto.getProdutoId());
        assertNull(dto.getValor());
        assertNull(dto.getQuantidade());
        assertNull(dto.getStatus());
    }

    @Test
    void testUpdateValues() {
        VendaDto dto = new VendaDto(1L, 10L, 100L, new BigDecimal("50.00"), 2, "CONCLUIDO");

        dto.setId(3L);
        dto.setClienteId(30L);
        dto.setProdutoId(300L);
        dto.setValor(new BigDecimal("75.00"));
        dto.setQuantidade(4);
        dto.setStatus("CANCELADO");

        assertEquals(3L, dto.getId());
        assertEquals(30L, dto.getClienteId());
        assertEquals(300L, dto.getProdutoId());
        assertEquals(new BigDecimal("75.00"), dto.getValor());
        assertEquals(4, dto.getQuantidade());
        assertEquals("CANCELADO", dto.getStatus());
    }

    @Test
    void testMultipleInstances() {
        VendaDto dto1 = new VendaDto(1L, 10L, 100L, new BigDecimal("10.00"), 1, "CONCLUIDO");
        VendaDto dto2 = new VendaDto(2L, 20L, 200L, new BigDecimal("20.00"), 2, "PENDENTE");

        assertEquals(1L, dto1.getId());
        assertEquals(10L, dto1.getClienteId());
        assertEquals(100L, dto1.getProdutoId());
        assertEquals(new BigDecimal("10.00"), dto1.getValor());
        assertEquals(1, dto1.getQuantidade());
        assertEquals("CONCLUIDO", dto1.getStatus());

        assertEquals(2L, dto2.getId());
        assertEquals(20L, dto2.getClienteId());
        assertEquals(200L, dto2.getProdutoId());
        assertEquals(new BigDecimal("20.00"), dto2.getValor());
        assertEquals(2, dto2.getQuantidade());
        assertEquals("PENDENTE", dto2.getStatus());
    }

    @Test
    void testZeroQuantidade() {
        VendaDto dto = new VendaDto(1L, 1L, 100L, new BigDecimal("10.00"), 0, "CONCLUIDO");
        assertEquals(0, dto.getQuantidade());
    }

    @Test
    void testNegativeQuantidade() {
        VendaDto dto = new VendaDto(1L, 1L, 100L, new BigDecimal("10.00"), -5, "CONCLUIDO");
        assertEquals(-5, dto.getQuantidade());
    }

    @Test
    void testZeroValor() {
        VendaDto dto = new VendaDto(1L, 1L, 100L, BigDecimal.ZERO, 1, "CONCLUIDO");
        assertEquals(BigDecimal.ZERO, dto.getValor());
    }

    @Test
    void testNegativeValor() {
        BigDecimal valorNegativo = new BigDecimal("-10.00");
        VendaDto dto = new VendaDto(1L, 1L, 100L, valorNegativo, 1, "CONCLUIDO");
        assertEquals(valorNegativo, dto.getValor());
    }

    @Test
    void testLargeQuantidade() {
        VendaDto dto = new VendaDto(1L, 1L, 100L, new BigDecimal("100.00"), 10000, "CONCLUIDO");
        assertEquals(10000, dto.getQuantidade());
    }

    @Test
    void testLargeValor() {
        BigDecimal valorGrande = new BigDecimal("999999.99");
        VendaDto dto = new VendaDto(1L, 1L, 100L, valorGrande, 1, "CONCLUIDO");
        assertEquals(valorGrande, dto.getValor());
    }

    @Test
    void testValorPrecisao() {
        BigDecimal valorPreciso = new BigDecimal("19.999999");
        VendaDto dto = new VendaDto(1L, 1L, 100L, valorPreciso, 1, "CONCLUIDO");
        assertEquals(valorPreciso, dto.getValor());
    }

    @Test
    void testValorComEscala() {
        BigDecimal valorComEscala = new BigDecimal("15.50");
        VendaDto dto = new VendaDto(1L, 1L, 100L, valorComEscala, 2, "CONCLUIDO");
        assertEquals(valorComEscala, dto.getValor());
    }

    @Test
    void testIdsNegativos() {
        VendaDto dto = new VendaDto(-1L, -10L, -100L, new BigDecimal("10.00"), 1, "CONCLUIDO");
        assertEquals(-1L, dto.getId());
        assertEquals(-10L, dto.getClienteId());
        assertEquals(-100L, dto.getProdutoId());
    }

    @Test
    void testIdsGrandes() {
        VendaDto dto = new VendaDto(999999L, 888888L, 777777L, new BigDecimal("10.00"), 1, "CONCLUIDO");
        assertEquals(999999L, dto.getId());
        assertEquals(888888L, dto.getClienteId());
        assertEquals(777777L, dto.getProdutoId());
    }

    @Test
    void testStatusDiferentes() {
        String[] statusPossiveis = {"CONCLUIDO", "PENDENTE", "CANCELADO", "EM_PROCESSAMENTO", "FALHA", "ESTORNADO"};

        for (String status : statusPossiveis) {
            VendaDto dto = new VendaDto(1L, 1L, 100L, new BigDecimal("10.00"), 1, status);
            assertEquals(status, dto.getStatus());
        }
    }

    @Test
    void testStatusVazio() {
        VendaDto dto = new VendaDto(1L, 1L, 100L, new BigDecimal("10.00"), 1, "");
        assertEquals("", dto.getStatus());
    }

    @Test
    void testStatusComEspacos() {
        VendaDto dto = new VendaDto(1L, 1L, 100L, new BigDecimal("10.00"), 1, "  CONCLUIDO  ");
        assertEquals("  CONCLUIDO  ", dto.getStatus());
    }

    @Test
    void testStatusCaseSensitive() {
        VendaDto dto1 = new VendaDto(1L, 1L, 100L, new BigDecimal("10.00"), 1, "concluido");
        VendaDto dto2 = new VendaDto(2L, 2L, 200L, new BigDecimal("10.00"), 1, "CONCLUIDO");

        assertEquals("concluido", dto1.getStatus());
        assertEquals("CONCLUIDO", dto2.getStatus());
        assertNotEquals(dto1.getStatus(), dto2.getStatus());
    }

    @Test
    void testIdsZero() {
        VendaDto dto = new VendaDto(0L, 0L, 0L, new BigDecimal("10.00"), 1, "CONCLUIDO");
        assertEquals(0L, dto.getId());
        assertEquals(0L, dto.getClienteId());
        assertEquals(0L, dto.getProdutoId());
    }
}
