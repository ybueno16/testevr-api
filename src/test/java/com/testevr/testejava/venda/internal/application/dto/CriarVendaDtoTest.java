package com.testevr.testejava.venda.internal.application.dto;


import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CriarVendaDtoTest {

    @Test
    void testNoArgsConstructor() {
        CriarVendaDto dto = new CriarVendaDto();

        assertNull(dto.getClienteId());
        assertNull(dto.getProdutoId());
        assertNull(dto.getValor());
        assertNull(dto.getQuantidade());
    }

    @Test
    void testAllArgsConstructor() {
        Long clienteId = 1L;
        Long produtoId = 100L;
        BigDecimal valor = new BigDecimal("99.99");
        Integer quantidade = 5;

        CriarVendaDto dto = new CriarVendaDto(clienteId, produtoId, valor, quantidade);

        assertEquals(clienteId, dto.getClienteId());
        assertEquals(produtoId, dto.getProdutoId());
        assertEquals(valor, dto.getValor());
        assertEquals(quantidade, dto.getQuantidade());
    }

    @Test
    void testSettersAndGetters() {
        CriarVendaDto dto = new CriarVendaDto();

        Long clienteId = 2L;
        Long produtoId = 200L;
        BigDecimal valor = new BigDecimal("149.50");
        Integer quantidade = 3;

        dto.setClienteId(clienteId);
        dto.setProdutoId(produtoId);
        dto.setValor(valor);
        dto.setQuantidade(quantidade);

        assertEquals(clienteId, dto.getClienteId());
        assertEquals(produtoId, dto.getProdutoId());
        assertEquals(valor, dto.getValor());
        assertEquals(quantidade, dto.getQuantidade());
    }

    @Test
    void testSetClienteId() {
        CriarVendaDto dto = new CriarVendaDto();
        dto.setClienteId(10L);
        assertEquals(10L, dto.getClienteId());
    }

    @Test
    void testSetProdutoId() {
        CriarVendaDto dto = new CriarVendaDto();
        dto.setProdutoId(50L);
        assertEquals(50L, dto.getProdutoId());
    }

    @Test
    void testSetValor() {
        CriarVendaDto dto = new CriarVendaDto();
        BigDecimal valor = new BigDecimal("29.99");
        dto.setValor(valor);
        assertEquals(valor, dto.getValor());
    }

    @Test
    void testSetQuantidade() {
        CriarVendaDto dto = new CriarVendaDto();
        dto.setQuantidade(10);
        assertEquals(10, dto.getQuantidade());
    }

    @Test
    void testGetClienteId() {
        CriarVendaDto dto = new CriarVendaDto(15L, 75L, new BigDecimal("19.99"), 2);
        assertEquals(15L, dto.getClienteId());
    }

    @Test
    void testGetProdutoId() {
        CriarVendaDto dto = new CriarVendaDto(1L, 99L, new BigDecimal("49.99"), 1);
        assertEquals(99L, dto.getProdutoId());
    }

    @Test
    void testGetValor() {
        BigDecimal valor = new BigDecimal("79.99");
        CriarVendaDto dto = new CriarVendaDto(1L, 2L, valor, 1);
        assertEquals(valor, dto.getValor());
    }

    @Test
    void testGetQuantidade() {
        CriarVendaDto dto = new CriarVendaDto(1L, 2L, new BigDecimal("9.99"), 7);
        assertEquals(7, dto.getQuantidade());
    }

    @Test
    void testNullValues() {
        CriarVendaDto dto = new CriarVendaDto(null, null, null, null);

        assertNull(dto.getClienteId());
        assertNull(dto.getProdutoId());
        assertNull(dto.getValor());
        assertNull(dto.getQuantidade());
    }

    @Test
    void testUpdateValues() {
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, new BigDecimal("50.00"), 2);

        dto.setClienteId(3L);
        dto.setProdutoId(300L);
        dto.setValor(new BigDecimal("75.00"));
        dto.setQuantidade(4);

        assertEquals(3L, dto.getClienteId());
        assertEquals(300L, dto.getProdutoId());
        assertEquals(new BigDecimal("75.00"), dto.getValor());
        assertEquals(4, dto.getQuantidade());
    }

    @Test
    void testMultipleInstances() {
        CriarVendaDto dto1 = new CriarVendaDto(1L, 100L, new BigDecimal("10.00"), 1);
        CriarVendaDto dto2 = new CriarVendaDto(2L, 200L, new BigDecimal("20.00"), 2);

        assertEquals(1L, dto1.getClienteId());
        assertEquals(100L, dto1.getProdutoId());
        assertEquals(new BigDecimal("10.00"), dto1.getValor());
        assertEquals(1, dto1.getQuantidade());

        assertEquals(2L, dto2.getClienteId());
        assertEquals(200L, dto2.getProdutoId());
        assertEquals(new BigDecimal("20.00"), dto2.getValor());
        assertEquals(2, dto2.getQuantidade());
    }

    @Test
    void testZeroQuantidade() {
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, new BigDecimal("10.00"), 0);
        assertEquals(0, dto.getQuantidade());
    }

    @Test
    void testNegativeQuantidade() {
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, new BigDecimal("10.00"), -5);
        assertEquals(-5, dto.getQuantidade());
    }

    @Test
    void testZeroValor() {
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, BigDecimal.ZERO, 1);
        assertEquals(BigDecimal.ZERO, dto.getValor());
    }

    @Test
    void testNegativeValor() {
        BigDecimal valorNegativo = new BigDecimal("-10.00");
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, valorNegativo, 1);
        assertEquals(valorNegativo, dto.getValor());
    }

    @Test
    void testLargeQuantidade() {
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, new BigDecimal("100.00"), 10000);
        assertEquals(10000, dto.getQuantidade());
    }

    @Test
    void testLargeValor() {
        BigDecimal valorGrande = new BigDecimal("999999.99");
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, valorGrande, 1);
        assertEquals(valorGrande, dto.getValor());
    }

    @Test
    void testValorPrecisao() {
        BigDecimal valorPreciso = new BigDecimal("19.999999");
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, valorPreciso, 1);
        assertEquals(valorPreciso, dto.getValor());
    }

    @Test
    void testValorComEscala() {
        BigDecimal valorComEscala = new BigDecimal("15.50");
        CriarVendaDto dto = new CriarVendaDto(1L, 100L, valorComEscala, 2);
        assertEquals(valorComEscala, dto.getValor());
    }

    @Test
    void testIdsNegativos() {
        CriarVendaDto dto = new CriarVendaDto(-1L, -100L, new BigDecimal("10.00"), 1);
        assertEquals(-1L, dto.getClienteId());
        assertEquals(-100L, dto.getProdutoId());
    }

    @Test
    void testIdsGrandes() {
        CriarVendaDto dto = new CriarVendaDto(999999L, 888888L, new BigDecimal("10.00"), 1);
        assertEquals(999999L, dto.getClienteId());
        assertEquals(888888L, dto.getProdutoId());
    }
}
