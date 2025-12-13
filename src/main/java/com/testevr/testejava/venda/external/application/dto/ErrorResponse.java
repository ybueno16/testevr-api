// ErrorResponse.java (para tratamento de erros)
package com.testevr.testejava.venda.external.application.dto;

public class ErrorResponse {
    private String message;

    // Construtores
    public ErrorResponse() {}

    public ErrorResponse(String message) {
        this.message = message;
    }

    // Getters e Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}