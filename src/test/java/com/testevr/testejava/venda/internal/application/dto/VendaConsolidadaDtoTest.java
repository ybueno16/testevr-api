package com.testevr.testejava.venda.internal.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class VendaConsolidadaDtoTest {

    @Test
    void testNoArgsConstructor() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();

        assertNull(dto.getClienteId());
        assertNull(dto.getStatus());
        assertNull(dto.getValorTotal());
        assertNull(dto.getQuantidadeVendas());
    }

    @Test
    void testAllArgsConstructor() {
        Long clienteId = 1L;
        String status = "CONCLUIDO";
        BigDecimal valorTotal = new BigDecimal("199.99");
        Integer quantidadeVendas = 5;

        VendaConsolidadaDto dto = new VendaConsolidadaDto(clienteId, status, valorTotal, quantidadeVendas);

        assertEquals(clienteId, dto.getClienteId());
        assertEquals(status, dto.getStatus());
        assertEquals(valorTotal, dto.getValorTotal());
        assertEquals(quantidadeVendas, dto.getQuantidadeVendas());
    }

    @Test
    void testSettersAndGetters() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();

        Long clienteId = 2L;
        String status = "PENDENTE";
        BigDecimal valorTotal = new BigDecimal("349.50");
        Integer quantidadeVendas = 3;

        dto.setClienteId(clienteId);
        dto.setStatus(status);
        dto.setValorTotal(valorTotal);
        dto.setQuantidadeVendas(quantidadeVendas);

        assertEquals(clienteId, dto.getClienteId());
        assertEquals(status, dto.getStatus());
        assertEquals(valorTotal, dto.getValorTotal());
        assertEquals(quantidadeVendas, dto.getQuantidadeVendas());
    }

    @Test
    void testSetClienteId() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();
        dto.setClienteId(10L);
        assertEquals(10L, dto.getClienteId());
    }

    @Test
    void testSetStatus() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();
        dto.setStatus("CANCELADO");
        assertEquals("CANCELADO", dto.getStatus());
    }

    @Test
    void testSetValorTotal() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();
        BigDecimal valorTotal = new BigDecimal("99.99");
        dto.setValorTotal(valorTotal);
        assertEquals(valorTotal, dto.getValorTotal());
    }

    @Test
    void testSetQuantidadeVendas() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();
        dto.setQuantidadeVendas(10);
        assertEquals(10, dto.getQuantidadeVendas());
    }

    @Test
    void testGetClienteId() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(15L, "CONCLUIDO", new BigDecimal("19.99"), 2);
        assertEquals(15L, dto.getClienteId());
    }

    @Test
    void testGetStatus() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "EM_PROCESSAMENTO", new BigDecimal("49.99"), 1);
        assertEquals("EM_PROCESSAMENTO", dto.getStatus());
    }

    @Test
    void testGetValorTotal() {
        BigDecimal valorTotal = new BigDecimal("79.99");
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", valorTotal, 1);
        assertEquals(valorTotal, dto.getValorTotal());
    }

    @Test
    void testGetQuantidadeVendas() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", new BigDecimal("9.99"), 7);
        assertEquals(7, dto.getQuantidadeVendas());
    }

    @Test
    void testNullValues() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(null, null, null, null);

        assertNull(dto.getClienteId());
        assertNull(dto.getStatus());
        assertNull(dto.getValorTotal());
        assertNull(dto.getQuantidadeVendas());
    }

    @Test
    void testUpdateValues() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", new BigDecimal("50.00"), 2);

        dto.setClienteId(3L);
        dto.setStatus("CANCELADO");
        dto.setValorTotal(new BigDecimal("75.00"));
        dto.setQuantidadeVendas(4);

        assertEquals(3L, dto.getClienteId());
        assertEquals("CANCELADO", dto.getStatus());
        assertEquals(new BigDecimal("75.00"), dto.getValorTotal());
        assertEquals(4, dto.getQuantidadeVendas());
    }

    @Test
    void testMultipleInstances() {
        VendaConsolidadaDto dto1 = new VendaConsolidadaDto(1L, "CONCLUIDO", new BigDecimal("10.00"), 1);
        VendaConsolidadaDto dto2 = new VendaConsolidadaDto(2L, "PENDENTE", new BigDecimal("20.00"), 2);

        assertEquals(1L, dto1.getClienteId());
        assertEquals("CONCLUIDO", dto1.getStatus());
        assertEquals(new BigDecimal("10.00"), dto1.getValorTotal());
        assertEquals(1, dto1.getQuantidadeVendas());

        assertEquals(2L, dto2.getClienteId());
        assertEquals("PENDENTE", dto2.getStatus());
        assertEquals(new BigDecimal("20.00"), dto2.getValorTotal());
        assertEquals(2, dto2.getQuantidadeVendas());
    }

    @Test
    void testZeroQuantidadeVendas() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", new BigDecimal("10.00"), 0);
        assertEquals(0, dto.getQuantidadeVendas());
    }

    @Test
    void testNegativeQuantidadeVendas() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", new BigDecimal("10.00"), -5);
        assertEquals(-5, dto.getQuantidadeVendas());
    }

    @Test
    void testZeroValorTotal() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", BigDecimal.ZERO, 1);
        assertEquals(BigDecimal.ZERO, dto.getValorTotal());
    }

    @Test
    void testNegativeValorTotal() {
        BigDecimal valorNegativo = new BigDecimal("-10.00");
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", valorNegativo, 1);
        assertEquals(valorNegativo, dto.getValorTotal());
    }

    @Test
    void testLargeQuantidadeVendas() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", new BigDecimal("100.00"), 10000);
        assertEquals(10000, dto.getQuantidadeVendas());
    }

    @Test
    void testLargeValorTotal() {
        BigDecimal valorGrande = new BigDecimal("999999.99");
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", valorGrande, 1);
        assertEquals(valorGrande, dto.getValorTotal());
    }

    @Test
    void testValorTotalPrecisao() {
        BigDecimal valorPreciso = new BigDecimal("19.999999");
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", valorPreciso, 1);
        assertEquals(valorPreciso, dto.getValorTotal());
    }

    @Test
    void testValorTotalComEscala() {
        BigDecimal valorComEscala = new BigDecimal("15.50");
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "CONCLUIDO", valorComEscala, 2);
        assertEquals(valorComEscala, dto.getValorTotal());
    }

    @Test
    void testClienteIdNegativo() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(-1L, "CONCLUIDO", new BigDecimal("10.00"), 1);
        assertEquals(-1L, dto.getClienteId());
    }

    @Test
    void testClienteIdGrande() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(999999L, "CONCLUIDO", new BigDecimal("10.00"), 1);
        assertEquals(999999L, dto.getClienteId());
    }

    @Test
    void testStatusDiferentes() {
        String[] statusPossiveis = {"CONCLUIDO", "PENDENTE", "CANCELADO", "EM_PROCESSAMENTO", "FALHA"};

        for (String status : statusPossiveis) {
            VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, status, new BigDecimal("10.00"), 1);
            assertEquals(status, dto.getStatus());
        }
    }

    @Test
    void testStatusVazio() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "", new BigDecimal("10.00"), 1);
        assertEquals("", dto.getStatus());
    }

    @Test
    void testStatusComEspacos() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto(1L, "  CONCLUIDO  ", new BigDecimal("10.00"), 1);
        assertEquals("  CONCLUIDO  ", dto.getStatus());
    }

    @Test
    void testStatusCaseSensitive() {
        VendaConsolidadaDto dto1 = new VendaConsolidadaDto(1L, "concluido", new BigDecimal("10.00"), 1);
        VendaConsolidadaDto dto2 = new VendaConsolidadaDto(2L, "CONCLUIDO", new BigDecimal("10.00"), 1);

        assertEquals("concluido", dto1.getStatus());
        assertEquals("CONCLUIDO", dto2.getStatus());
        assertNotEquals(dto1.getStatus(), dto2.getStatus());
    }
}