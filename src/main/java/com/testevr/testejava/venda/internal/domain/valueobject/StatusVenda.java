package com.testevr.testejava.venda.internal.domain.valueobject;

public enum StatusVenda {
    PENDENTE("Pendente"),
    SUCESSO("Sucesso"),
    ERRO("Erro"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
