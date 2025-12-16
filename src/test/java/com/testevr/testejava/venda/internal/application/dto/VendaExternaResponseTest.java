package com.testevr.testejava.venda.internal.application.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VendaExternaResponseTest {

    @Test
    void testNoArgsConstructor() {
        VendaExternaResponse response = new VendaExternaResponse();

        assertFalse(response.isSucesso());
        assertNull(response.getMensagem());
        assertNull(response.getCodigoTransacao());
    }

    @Test
    void testAllArgsConstructor() {
        boolean sucesso = true;
        String mensagem = "Venda realizada com sucesso";
        String codigoTransacao = "TRANS001";

        VendaExternaResponse response = new VendaExternaResponse(sucesso, mensagem, codigoTransacao);

        assertTrue(response.isSucesso());
        assertEquals(mensagem, response.getMensagem());
        assertEquals(codigoTransacao, response.getCodigoTransacao());
    }

    @Test
    void testSettersAndGetters() {
        VendaExternaResponse response = new VendaExternaResponse();

        boolean sucesso = true;
        String mensagem = "Transação processada";
        String codigoTransacao = "TRX123456";

        response.setSucesso(sucesso);
        response.setMensagem(mensagem);
        response.setCodigoTransacao(codigoTransacao);

        assertTrue(response.isSucesso());
        assertEquals(mensagem, response.getMensagem());
        assertEquals(codigoTransacao, response.getCodigoTransacao());
    }

    @Test
    void testSetSucesso() {
        VendaExternaResponse response = new VendaExternaResponse();

        response.setSucesso(true);
        assertTrue(response.isSucesso());

        response.setSucesso(false);
        assertFalse(response.isSucesso());
    }

    @Test
    void testSetMensagem() {
        VendaExternaResponse response = new VendaExternaResponse();

        response.setMensagem("Nova mensagem");
        assertEquals("Nova mensagem", response.getMensagem());
    }

    @Test
    void testSetCodigoTransacao() {
        VendaExternaResponse response = new VendaExternaResponse();

        response.setCodigoTransacao("COD001");
        assertEquals("COD001", response.getCodigoTransacao());
    }

    @Test
    void testIsSucesso() {
        VendaExternaResponse responseTrue = new VendaExternaResponse(true, "Sucesso", "T001");
        VendaExternaResponse responseFalse = new VendaExternaResponse(false, "Falha", "T002");

        assertTrue(responseTrue.isSucesso());
        assertFalse(responseFalse.isSucesso());
    }

    @Test
    void testGetMensagem() {
        VendaExternaResponse response = new VendaExternaResponse(false, "Erro na transação", "ERR001");
        assertEquals("Erro na transação", response.getMensagem());
    }

    @Test
    void testGetCodigoTransacao() {
        VendaExternaResponse response = new VendaExternaResponse(true, "Sucesso", "TRANX999");
        assertEquals("TRANX999", response.getCodigoTransacao());
    }

    @Test
    void testNullValues() {
        VendaExternaResponse response = new VendaExternaResponse(false, null, null);

        assertFalse(response.isSucesso());
        assertNull(response.getMensagem());
        assertNull(response.getCodigoTransacao());
    }

    @Test
    void testUpdateValues() {
        VendaExternaResponse response = new VendaExternaResponse(true, "Inicial", "CODE001");

        response.setSucesso(false);
        response.setMensagem("Atualizada");
        response.setCodigoTransacao("CODE002");

        assertFalse(response.isSucesso());
        assertEquals("Atualizada", response.getMensagem());
        assertEquals("CODE002", response.getCodigoTransacao());
    }

    @Test
    void testMultipleInstances() {
        VendaExternaResponse response1 = new VendaExternaResponse(true, "Sucesso 1", "T001");
        VendaExternaResponse response2 = new VendaExternaResponse(false, "Falha 1", "F001");

        assertTrue(response1.isSucesso());
        assertEquals("Sucesso 1", response1.getMensagem());
        assertEquals("T001", response1.getCodigoTransacao());

        assertFalse(response2.isSucesso());
        assertEquals("Falha 1", response2.getMensagem());
        assertEquals("F001", response2.getCodigoTransacao());
    }

    @Test
    void testSucessoFalse() {
        VendaExternaResponse response = new VendaExternaResponse(false, "Falha na transação", null);

        assertFalse(response.isSucesso());
        assertEquals("Falha na transação", response.getMensagem());
        assertNull(response.getCodigoTransacao());
    }

    @Test
    void testEmptyMensagem() {
        VendaExternaResponse response = new VendaExternaResponse(true, "", "CODE123");

        assertEquals("", response.getMensagem());
        assertTrue(response.isSucesso());
    }

    @Test
    void testEmptyCodigoTransacao() {
        VendaExternaResponse response = new VendaExternaResponse(true, "Sucesso", "");

        assertEquals("", response.getCodigoTransacao());
        assertTrue(response.isSucesso());
    }

    @Test
    void testWhitespaceValues() {
        VendaExternaResponse response = new VendaExternaResponse();

        response.setMensagem("   ");
        response.setCodigoTransacao("  CODE  ");

        assertEquals("   ", response.getMensagem());
        assertEquals("  CODE  ", response.getCodigoTransacao());
    }

    @Test
    void testLongMensagem() {
        String longMessage = "Esta é uma mensagem muito longa que contém muitos detalhes sobre a transação " +
                "que foi realizada no sistema externo de pagamento incluindo informações sobre " +
                "validações, processamento e confirmação da operação comercial.";

        VendaExternaResponse response = new VendaExternaResponse(true, longMessage, "LONG001");

        assertEquals(longMessage, response.getMensagem());
    }

    @Test
    void testLongCodigoTransacao() {
        String longCode = "TRANSACAO-2024-01-15-10-30-45-1234567890-ABCDEFGHIJ";

        VendaExternaResponse response = new VendaExternaResponse(true, "Sucesso", longCode);

        assertEquals(longCode, response.getCodigoTransacao());
    }

    @Test
    void testSpecialCharacters() {
        VendaExternaResponse response = new VendaExternaResponse();

        response.setMensagem("Transação com caracteres especiais: áéíóú, çãõ, !@#$%&*()");
        response.setCodigoTransacao("TRX-ÁÉÍ-123");

        assertEquals("Transação com caracteres especiais: áéíóú, çãõ, !@#$%&*()", response.getMensagem());
        assertEquals("TRX-ÁÉÍ-123", response.getCodigoTransacao());
    }

    @Test
    void testCodigoTransacaoNumerico() {
        VendaExternaResponse response = new VendaExternaResponse(true, "Sucesso", "1234567890");

        assertEquals("1234567890", response.getCodigoTransacao());
    }

    @Test
    void testCodigoTransacaoComLetrasENumeros() {
        VendaExternaResponse response = new VendaExternaResponse(true, "Sucesso", "ABC123XYZ456");

        assertEquals("ABC123XYZ456", response.getCodigoTransacao());
    }

    @Test
    void testEstadoInicial() {
        VendaExternaResponse response = new VendaExternaResponse();

        assertFalse(response.isSucesso());
        assertNull(response.getMensagem());
        assertNull(response.getCodigoTransacao());
    }

    @Test
    void testCombinacoesSucesso() {
        VendaExternaResponse response1 = new VendaExternaResponse(true, "OK", "CODE001");
        assertTrue(response1.isSucesso());

        VendaExternaResponse response2 = new VendaExternaResponse(true, "OK", null);
        assertTrue(response2.isSucesso());

        VendaExternaResponse response3 = new VendaExternaResponse(false, "Erro", "ERR001");
        assertFalse(response3.isSucesso());

        VendaExternaResponse response4 = new VendaExternaResponse(false, "Erro", null);
        assertFalse(response4.isSucesso());
    }
}