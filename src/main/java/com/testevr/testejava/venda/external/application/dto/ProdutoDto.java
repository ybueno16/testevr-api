package com.testevr.testejava.venda.external.application.dto;

import java.time.LocalDateTime;

public class ProdutoDto {
    private Long id;
    private String descricao;
    private Integer estoque;
    private Double preco;
    private String unidade;
    private LocalDateTime ultimaAtualizacao;

    public ProdutoDto() {}

    public ProdutoDto(Long id, String descricao, Integer estoque, Double preco,
                      String unidade, LocalDateTime ultimaAtualizacao) {
        this.id = id;
        this.descricao = descricao;
        this.estoque = estoque;
        this.preco = preco;
        this.unidade = unidade;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}