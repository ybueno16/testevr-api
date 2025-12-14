package com.testevr.testejava.venda.internal.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class VendaExternaRequestTest {

    @Test
    void testNoArgsConstructor() {
        VendaExternaRequest request = new VendaExternaRequest();

        assertNull(request.getClienteId());
        assertNull(request.getProdutoId());
        assertNull(request.getValor());
        assertNull(request.getQuantidade());
    }

    @Test
    void testAllArgsConstructor() {
        Long clienteId = 1L;
        Long produtoId = 100L;
        BigDecimal valor = new BigDecimal("99.99");
        Integer quantidade = 5;

        VendaExternaRequest request = new VendaExternaRequest(clienteId, produtoId, valor, quantidade);

        assertEquals(clienteId, request.getClienteId());
        assertEquals(produtoId, request.getProdutoId());
        assertEquals(valor, request.getValor());
        assertEquals(quantidade, request.getQuantidade());
    }

    @Test
    void testSettersAndGetters() {
        VendaExternaRequest request = new VendaExternaRequest();

        Long clienteId = 2L;
        Long produtoId = 200L;
        BigDecimal valor = new BigDecimal("149.50");
        Integer quantidade = 3;

        request.setClienteId(clienteId);
        request.setProdutoId(produtoId);
        request.setValor(valor);
        request.setQuantidade(quantidade);

        assertEquals(clienteId, request.getClienteId());
        assertEquals(produtoId, request.getProdutoId());
        assertEquals(valor, request.getValor());
        assertEquals(quantidade, request.getQuantidade());
    }

    @Test
    void testSetClienteId() {
        VendaExternaRequest request = new VendaExternaRequest();
        request.setClienteId(10L);
        assertEquals(10L, request.getClienteId());
    }

    @Test
    void testSetProdutoId() {
        VendaExternaRequest request = new VendaExternaRequest();
        request.setProdutoId(50L);
        assertEquals(50L, request.getProdutoId());
    }

    @Test
    void testSetValor() {
        VendaExternaRequest request = new VendaExternaRequest();
        BigDecimal valor = new BigDecimal("29.99");
        request.setValor(valor);
        assertEquals(valor, request.getValor());
    }

    @Test
    void testSetQuantidade() {
        VendaExternaRequest request = new VendaExternaRequest();
        request.setQuantidade(10);
        assertEquals(10, request.getQuantidade());
    }

    @Test
    void testGetClienteId() {
        VendaExternaRequest request = new VendaExternaRequest(15L, 75L, new BigDecimal("19.99"), 2);
        assertEquals(15L, request.getClienteId());
    }

    @Test
    void testGetProdutoId() {
        VendaExternaRequest request = new VendaExternaRequest(1L, 99L, new BigDecimal("49.99"), 1);
        assertEquals(99L, request.getProdutoId());
    }

    @Test
    void testGetValor() {
        BigDecimal valor = new BigDecimal("79.99");
        VendaExternaRequest request = new VendaExternaRequest(1L, 2L, valor, 1);
        assertEquals(valor, request.getValor());
    }

    @Test
    void testGetQuantidade() {
        VendaExternaRequest request = new VendaExternaRequest(1L, 2L, new BigDecimal("9.99"), 7);
        assertEquals(7, request.getQuantidade());
    }

    @Test
    void testNullValues() {
        VendaExternaRequest request = new VendaExternaRequest(null, null, null, null);

        assertNull(request.getClienteId());
        assertNull(request.getProdutoId());
        assertNull(request.getValor());
        assertNull(request.getQuantidade());
    }

    @Test
    void testUpdateValues() {
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, new BigDecimal("50.00"), 2);

        request.setClienteId(3L);
        request.setProdutoId(300L);
        request.setValor(new BigDecimal("75.00"));
        request.setQuantidade(4);

        assertEquals(3L, request.getClienteId());
        assertEquals(300L, request.getProdutoId());
        assertEquals(new BigDecimal("75.00"), request.getValor());
        assertEquals(4, request.getQuantidade());
    }

    @Test
    void testMultipleInstances() {
        VendaExternaRequest request1 = new VendaExternaRequest(1L, 100L, new BigDecimal("10.00"), 1);
        VendaExternaRequest request2 = new VendaExternaRequest(2L, 200L, new BigDecimal("20.00"), 2);

        assertEquals(1L, request1.getClienteId());
        assertEquals(100L, request1.getProdutoId());
        assertEquals(new BigDecimal("10.00"), request1.getValor());
        assertEquals(1, request1.getQuantidade());

        assertEquals(2L, request2.getClienteId());
        assertEquals(200L, request2.getProdutoId());
        assertEquals(new BigDecimal("20.00"), request2.getValor());
        assertEquals(2, request2.getQuantidade());
    }

    @Test
    void testZeroQuantidade() {
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, new BigDecimal("10.00"), 0);
        assertEquals(0, request.getQuantidade());
    }

    @Test
    void testNegativeQuantidade() {
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, new BigDecimal("10.00"), -5);
        assertEquals(-5, request.getQuantidade());
    }

    @Test
    void testZeroValor() {
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, BigDecimal.ZERO, 1);
        assertEquals(BigDecimal.ZERO, request.getValor());
    }

    @Test
    void testNegativeValor() {
        BigDecimal valorNegativo = new BigDecimal("-10.00");
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, valorNegativo, 1);
        assertEquals(valorNegativo, request.getValor());
    }

    @Test
    void testLargeQuantidade() {
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, new BigDecimal("100.00"), 10000);
        assertEquals(10000, request.getQuantidade());
    }

    @Test
    void testLargeValor() {
        BigDecimal valorGrande = new BigDecimal("999999.99");
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, valorGrande, 1);
        assertEquals(valorGrande, request.getValor());
    }

    @Test
    void testValorPrecisao() {
        BigDecimal valorPreciso = new BigDecimal("19.999999");
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, valorPreciso, 1);
        assertEquals(valorPreciso, request.getValor());
    }

    @Test
    void testValorComEscala() {
        BigDecimal valorComEscala = new BigDecimal("15.50");
        VendaExternaRequest request = new VendaExternaRequest(1L, 100L, valorComEscala, 2);
        assertEquals(valorComEscala, request.getValor());
    }

    @Test
    void testIdsNegativos() {
        VendaExternaRequest request = new VendaExternaRequest(-1L, -100L, new BigDecimal("10.00"), 1);
        assertEquals(-1L, request.getClienteId());
        assertEquals(-100L, request.getProdutoId());
    }

    @Test
    void testIdsGrandes() {
        VendaExternaRequest request = new VendaExternaRequest(999999L, 888888L, new BigDecimal("10.00"), 1);
        assertEquals(999999L, request.getClienteId());
        assertEquals(888888L, request.getProdutoId());
    }

    @Test
    void testIdsZero() {
        VendaExternaRequest request = new VendaExternaRequest(0L, 0L, new BigDecimal("10.00"), 1);
        assertEquals(0L, request.getClienteId());
        assertEquals(0L, request.getProdutoId());
    }

    @Test
    void testCompareWithOtherDto() {
        VendaExternaRequest request1 = new VendaExternaRequest(1L, 100L, new BigDecimal("10.00"), 1);
        VendaExternaRequest request2 = new VendaExternaRequest(1L, 100L, new BigDecimal("10.00"), 1);

        assertEquals(request1.getClienteId(), request2.getClienteId());
        assertEquals(request1.getProdutoId(), request2.getProdutoId());
        assertEquals(request1.getValor(), request2.getValor());
        assertEquals(request1.getQuantidade(), request2.getQuantidade());
    }
}