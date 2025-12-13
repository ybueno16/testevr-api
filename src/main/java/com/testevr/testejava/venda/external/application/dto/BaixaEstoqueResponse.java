// BaixaEstoqueResponse.java
package com.testevr.testejava.venda.external.application.dto;

public class BaixaEstoqueResponse {
    private String message;
    private Integer estoqueAtual;
    private Integer quantidadeSolicitada;

    // Construtores
    public BaixaEstoqueResponse() {}

    public BaixaEstoqueResponse(String message) {
        this.message = message;
    }

    // Getters e Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getEstoqueAtual() { return estoqueAtual; }
    public void setEstoqueAtual(Integer estoqueAtual) { this.estoqueAtual = estoqueAtual; }

    public Integer getQuantidadeSolicitada() { return quantidadeSolicitada; }
    public void setQuantidadeSolicitada(Integer quantidadeSolicitada) {
        this.quantidadeSolicitada = quantidadeSolicitada;
    }
}