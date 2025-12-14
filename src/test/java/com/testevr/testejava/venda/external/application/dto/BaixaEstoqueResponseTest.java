package com.testevr.testejava.venda.external.application.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaixaEstoqueResponseTest {

    @Test
    void testNoArgsConstructor() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();

        assertNull(response.getMessage());
        assertNull(response.getEstoqueAtual());
        assertNull(response.getQuantidadeSolicitada());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Estoque atualizado com sucesso";

        BaixaEstoqueResponse response = new BaixaEstoqueResponse(message);

        assertEquals(message, response.getMessage());
        assertNull(response.getEstoqueAtual());
        assertNull(response.getQuantidadeSolicitada());
    }

    @Test
    void testSettersAndGetters() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();

        String message = "Teste de mensagem";
        Integer estoqueAtual = 50;
        Integer quantidadeSolicitada = 10;

        response.setMessage(message);
        response.setEstoqueAtual(estoqueAtual);
        response.setQuantidadeSolicitada(quantidadeSolicitada);

        assertEquals(message, response.getMessage());
        assertEquals(estoqueAtual, response.getEstoqueAtual());
        assertEquals(quantidadeSolicitada, response.getQuantidadeSolicitada());
    }

    @Test
    void testSetMessage() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();

        String message = "Nova mensagem";
        response.setMessage(message);

        assertEquals(message, response.getMessage());
    }

    @Test
    void testSetEstoqueAtual() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();

        Integer estoqueAtual = 100;
        response.setEstoqueAtual(estoqueAtual);

        assertEquals(estoqueAtual, response.getEstoqueAtual());
    }

    @Test
    void testSetQuantidadeSolicitada() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();

        Integer quantidadeSolicitada = 25;
        response.setQuantidadeSolicitada(quantidadeSolicitada);

        assertEquals(quantidadeSolicitada, response.getQuantidadeSolicitada());
    }

    @Test
    void testGetMessage() {
        String message = "Estoque insuficiente";
        BaixaEstoqueResponse response = new BaixaEstoqueResponse(message);

        assertEquals(message, response.getMessage());
    }

    @Test
    void testGetEstoqueAtual() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();
        response.setEstoqueAtual(75);

        assertEquals(75, response.getEstoqueAtual());
    }

    @Test
    void testGetQuantidadeSolicitada() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();
        response.setQuantidadeSolicitada(30);

        assertEquals(30, response.getQuantidadeSolicitada());
    }

    @Test
    void testNullValues() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();

        assertNull(response.getMessage());
        assertNull(response.getEstoqueAtual());
        assertNull(response.getQuantidadeSolicitada());
    }

    @Test
    void testUpdateValues() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse("Mensagem inicial");
        response.setEstoqueAtual(100);
        response.setQuantidadeSolicitada(20);

        assertEquals("Mensagem inicial", response.getMessage());
        assertEquals(100, response.getEstoqueAtual());
        assertEquals(20, response.getQuantidadeSolicitada());

        response.setMessage("Mensagem atualizada");
        response.setEstoqueAtual(80);
        response.setQuantidadeSolicitada(15);

        assertEquals("Mensagem atualizada", response.getMessage());
        assertEquals(80, response.getEstoqueAtual());
        assertEquals(15, response.getQuantidadeSolicitada());
    }

    @Test
    void testMultipleInstances() {
        BaixaEstoqueResponse response1 = new BaixaEstoqueResponse("Sucesso 1");
        response1.setEstoqueAtual(50);
        response1.setQuantidadeSolicitada(10);

        BaixaEstoqueResponse response2 = new BaixaEstoqueResponse("Sucesso 2");
        response2.setEstoqueAtual(30);
        response2.setQuantidadeSolicitada(5);

        assertEquals("Sucesso 1", response1.getMessage());
        assertEquals(50, response1.getEstoqueAtual());
        assertEquals(10, response1.getQuantidadeSolicitada());

        assertEquals("Sucesso 2", response2.getMessage());
        assertEquals(30, response2.getEstoqueAtual());
        assertEquals(5, response2.getQuantidadeSolicitada());
    }

    @Test
    void testZeroValues() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();
        response.setEstoqueAtual(0);
        response.setQuantidadeSolicitada(0);

        assertEquals(0, response.getEstoqueAtual());
        assertEquals(0, response.getQuantidadeSolicitada());
    }

    @Test
    void testNegativeValues() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();
        response.setEstoqueAtual(-5);
        response.setQuantidadeSolicitada(-10);

        assertEquals(-5, response.getEstoqueAtual());
        assertEquals(-10, response.getQuantidadeSolicitada());
    }

    @Test
    void testLargeValues() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse();
        response.setEstoqueAtual(1000000);
        response.setQuantidadeSolicitada(500000);

        assertEquals(1000000, response.getEstoqueAtual());
        assertEquals(500000, response.getQuantidadeSolicitada());
    }

    @Test
    void testEmptyMessage() {
        BaixaEstoqueResponse response = new BaixaEstoqueResponse("");

        assertEquals("", response.getMessage());
    }
}
