package com.testevr.testejava.venda.external.application.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaixaEstoqueRequestTest {

    @Test
    void testNoArgsConstructor() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();

        assertNull(request.getId());
        assertNull(request.getQuantidade());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 123L;
        Integer quantidade = 5;

        BaixaEstoqueRequest request = new BaixaEstoqueRequest(id, quantidade);

        assertEquals(id, request.getId());
        assertEquals(quantidade, request.getQuantidade());
    }

    @Test
    void testSettersAndGetters() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();

        Long id = 456L;
        Integer quantidade = 10;

        request.setId(id);
        request.setQuantidade(quantidade);

        assertEquals(id, request.getId());
        assertEquals(quantidade, request.getQuantidade());
    }

    @Test
    void testSetId() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();

        Long id = 789L;
        request.setId(id);

        assertEquals(id, request.getId());
    }

    @Test
    void testSetQuantidade() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();

        Integer quantidade = 15;
        request.setQuantidade(quantidade);

        assertEquals(quantidade, request.getQuantidade());
    }

    @Test
    void testGetId() {
        Long id = 999L;
        BaixaEstoqueRequest request = new BaixaEstoqueRequest(id, 3);

        assertEquals(id, request.getId());
    }

    @Test
    void testGetQuantidade() {
        Integer quantidade = 20;
        BaixaEstoqueRequest request = new BaixaEstoqueRequest(111L, quantidade);

        assertEquals(quantidade, request.getQuantidade());
    }

    @Test
    void testNullValues() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest(null, null);

        assertNull(request.getId());
        assertNull(request.getQuantidade());
    }

    @Test
    void testUpdateValues() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest(1L, 5);

        assertEquals(1L, request.getId());
        assertEquals(5, request.getQuantidade());

        request.setId(2L);
        request.setQuantidade(10);

        assertEquals(2L, request.getId());
        assertEquals(10, request.getQuantidade());
    }

    @Test
    void testMultipleInstances() {
        BaixaEstoqueRequest request1 = new BaixaEstoqueRequest(1L, 3);
        BaixaEstoqueRequest request2 = new BaixaEstoqueRequest(2L, 7);

        assertEquals(1L, request1.getId());
        assertEquals(3, request1.getQuantidade());
        assertEquals(2L, request2.getId());
        assertEquals(7, request2.getQuantidade());
    }

    @Test
    void testZeroQuantidade() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest(100L, 0);

        assertEquals(100L, request.getId());
        assertEquals(0, request.getQuantidade());
    }

    @Test
    void testNegativeQuantidade() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest(200L, -5);

        assertEquals(200L, request.getId());
        assertEquals(-5, request.getQuantidade());
    }

    @Test
    void testLargeQuantidade() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest(300L, 10000);

        assertEquals(300L, request.getId());
        assertEquals(10000, request.getQuantidade());
    }
}
