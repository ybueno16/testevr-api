package com.testevr.testejava.venda.external.application.dto;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testNoArgsConstructor() {
        ErrorResponse response = new ErrorResponse();

        assertNull(response.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Erro ao processar a requisição";

        ErrorResponse response = new ErrorResponse(message);

        assertEquals(message, response.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponse response = new ErrorResponse();

        String message = "Mensagem de erro";
        response.setMessage(message);

        assertEquals(message, response.getMessage());
    }

    @Test
    void testSetMessage() {
        ErrorResponse response = new ErrorResponse();

        String message = "Novo erro";
        response.setMessage(message);

        assertEquals(message, response.getMessage());
    }

    @Test
    void testGetMessage() {
        String message = "Erro de validação";
        ErrorResponse response = new ErrorResponse(message);

        assertEquals(message, response.getMessage());
    }

    @Test
    void testNullMessage() {
        ErrorResponse response = new ErrorResponse();

        assertNull(response.getMessage());

        response.setMessage(null);
        assertNull(response.getMessage());
    }

    @Test
    void testUpdateMessage() {
        ErrorResponse response = new ErrorResponse("Erro inicial");

        assertEquals("Erro inicial", response.getMessage());

        response.setMessage("Erro atualizado");
        assertEquals("Erro atualizado", response.getMessage());
    }

    @Test
    void testMultipleInstances() {
        ErrorResponse response1 = new ErrorResponse("Erro 1");
        ErrorResponse response2 = new ErrorResponse("Erro 2");

        assertEquals("Erro 1", response1.getMessage());
        assertEquals("Erro 2", response2.getMessage());
    }

    @Test
    void testEmptyMessage() {
        ErrorResponse response = new ErrorResponse("");

        assertEquals("", response.getMessage());
    }

    @Test
    void testWhitespaceMessage() {
        ErrorResponse response = new ErrorResponse("   ");

        assertEquals("   ", response.getMessage());
    }

    @Test
    void testLongMessage() {
        String longMessage = "Este é um erro muito longo que contém muitos detalhes sobre o que deu errado no sistema " +
                "quando tentamos processar a requisição do usuário final durante o fluxo de baixa de estoque.";

        ErrorResponse response = new ErrorResponse(longMessage);

        assertEquals(longMessage, response.getMessage());
    }

    @Test
    void testSpecialCharactersMessage() {
        String message = "Erro com caracteres especiais: áéíóú, çãõ, !@#$%&*()";

        ErrorResponse response = new ErrorResponse(message);

        assertEquals(message, response.getMessage());
    }
}