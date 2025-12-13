package com.testevr.testejava.venda.internal.application.dto;

public class VendaExternaResponse {
    private boolean sucesso;
    private String mensagem;
    private String codigoTransacao;

    public VendaExternaResponse() {}

    public VendaExternaResponse(boolean sucesso, String mensagem, String codigoTransacao) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.codigoTransacao = codigoTransacao;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCodigoTransacao() {
        return codigoTransacao;
    }

    public void setCodigoTransacao(String codigoTransacao) {
        this.codigoTransacao = codigoTransacao;
    }
}
