package com.testevr.testejava.venda.external.application.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoDtoTest {

    @Test
    void testNoArgsConstructor() {
        ProdutoDto produto = new ProdutoDto();

        assertNull(produto.getId());
        assertNull(produto.getDescricao());
        assertNull(produto.getEstoque());
        assertNull(produto.getPreco());
        assertNull(produto.getUnidade());
        assertNull(produto.getUltimaAtualizacao());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String descricao = "Produto Teste";
        Integer estoque = 100;
        Double preco = 29.99;
        String unidade = "UN";
        LocalDateTime ultimaAtualizacao = LocalDateTime.of(2024, 1, 15, 10, 30);

        ProdutoDto produto = new ProdutoDto(id, descricao, estoque, preco, unidade, ultimaAtualizacao);

        assertEquals(id, produto.getId());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(estoque, produto.getEstoque());
        assertEquals(preco, produto.getPreco());
        assertEquals(unidade, produto.getUnidade());
        assertEquals(ultimaAtualizacao, produto.getUltimaAtualizacao());
    }

    @Test
    void testSettersAndGetters() {
        ProdutoDto produto = new ProdutoDto();

        Long id = 2L;
        String descricao = "Produto Novo";
        Integer estoque = 50;
        Double preco = 15.75;
        String unidade = "KG";
        LocalDateTime ultimaAtualizacao = LocalDateTime.now();

        produto.setId(id);
        produto.setDescricao(descricao);
        produto.setEstoque(estoque);
        produto.setPreco(preco);
        produto.setUnidade(unidade);
        produto.setUltimaAtualizacao(ultimaAtualizacao);

        assertEquals(id, produto.getId());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(estoque, produto.getEstoque());
        assertEquals(preco, produto.getPreco());
        assertEquals(unidade, produto.getUnidade());
        assertEquals(ultimaAtualizacao, produto.getUltimaAtualizacao());
    }

    @Test
    void testSetId() {
        ProdutoDto produto = new ProdutoDto();
        produto.setId(3L);
        assertEquals(3L, produto.getId());
    }

    @Test
    void testSetDescricao() {
        ProdutoDto produto = new ProdutoDto();
        produto.setDescricao("Descrição Teste");
        assertEquals("Descrição Teste", produto.getDescricao());
    }

    @Test
    void testSetEstoque() {
        ProdutoDto produto = new ProdutoDto();
        produto.setEstoque(200);
        assertEquals(200, produto.getEstoque());
    }

    @Test
    void testSetPreco() {
        ProdutoDto produto = new ProdutoDto();
        produto.setPreco(49.99);
        assertEquals(49.99, produto.getPreco());
    }

    @Test
    void testSetUnidade() {
        ProdutoDto produto = new ProdutoDto();
        produto.setUnidade("LT");
        assertEquals("LT", produto.getUnidade());
    }

    @Test
    void testSetUltimaAtualizacao() {
        ProdutoDto produto = new ProdutoDto();
        LocalDateTime data = LocalDateTime.of(2024, 2, 1, 14, 45);
        produto.setUltimaAtualizacao(data);
        assertEquals(data, produto.getUltimaAtualizacao());
    }

    @Test
    void testGetId() {
        ProdutoDto produto = new ProdutoDto(4L, "Teste", 10, 9.99, "UN", null);
        assertEquals(4L, produto.getId());
    }

    @Test
    void testGetDescricao() {
        ProdutoDto produto = new ProdutoDto(1L, "Produto A", 20, 19.99, "UN", null);
        assertEquals("Produto A", produto.getDescricao());
    }

    @Test
    void testGetEstoque() {
        ProdutoDto produto = new ProdutoDto(1L, "Produto", 75, 5.99, "UN", null);
        assertEquals(75, produto.getEstoque());
    }

    @Test
    void testGetPreco() {
        ProdutoDto produto = new ProdutoDto(1L, "Produto", 10, 12.50, "UN", null);
        assertEquals(12.50, produto.getPreco());
    }

    @Test
    void testGetUnidade() {
        ProdutoDto produto = new ProdutoDto(1L, "Produto", 10, 1.99, "CX", null);
        assertEquals("CX", produto.getUnidade());
    }

    @Test
    void testGetUltimaAtualizacao() {
        LocalDateTime data = LocalDateTime.of(2024, 3, 1, 9, 0);
        ProdutoDto produto = new ProdutoDto(1L, "Produto", 10, 2.99, "UN", data);
        assertEquals(data, produto.getUltimaAtualizacao());
    }

    @Test
    void testNullValues() {
        ProdutoDto produto = new ProdutoDto(null, null, null, null, null, null);

        assertNull(produto.getId());
        assertNull(produto.getDescricao());
        assertNull(produto.getEstoque());
        assertNull(produto.getPreco());
        assertNull(produto.getUnidade());
        assertNull(produto.getUltimaAtualizacao());
    }

    @Test
    void testUpdateValues() {
        ProdutoDto produto = new ProdutoDto(1L, "Original", 100, 10.0, "UN", LocalDateTime.now());

        produto.setId(2L);
        produto.setDescricao("Atualizado");
        produto.setEstoque(200);
        produto.setPreco(20.0);
        produto.setUnidade("KG");
        LocalDateTime novaData = LocalDateTime.now().plusDays(1);
        produto.setUltimaAtualizacao(novaData);

        assertEquals(2L, produto.getId());
        assertEquals("Atualizado", produto.getDescricao());
        assertEquals(200, produto.getEstoque());
        assertEquals(20.0, produto.getPreco());
        assertEquals("KG", produto.getUnidade());
        assertEquals(novaData, produto.getUltimaAtualizacao());
    }

    @Test
    void testMultipleInstances() {
        ProdutoDto produto1 = new ProdutoDto(1L, "Produto 1", 10, 5.99, "UN", LocalDateTime.now());
        ProdutoDto produto2 = new ProdutoDto(2L, "Produto 2", 20, 15.99, "KG", LocalDateTime.now().plusHours(1));

        assertEquals(1L, produto1.getId());
        assertEquals("Produto 1", produto1.getDescricao());
        assertEquals(5.99, produto1.getPreco());

        assertEquals(2L, produto2.getId());
        assertEquals("Produto 2", produto2.getDescricao());
        assertEquals(15.99, produto2.getPreco());
    }

    @Test
    void testZeroValues() {
        ProdutoDto produto = new ProdutoDto();
        produto.setEstoque(0);
        produto.setPreco(0.0);

        assertEquals(0, produto.getEstoque());
        assertEquals(0.0, produto.getPreco());
    }

    @Test
    void testNegativeValues() {
        ProdutoDto produto = new ProdutoDto();
        produto.setEstoque(-5);
        produto.setPreco(-10.99);

        assertEquals(-5, produto.getEstoque());
        assertEquals(-10.99, produto.getPreco());
    }

    @Test
    void testLargeValues() {
        ProdutoDto produto = new ProdutoDto();
        produto.setEstoque(1000000);
        produto.setPreco(999999.99);

        assertEquals(1000000, produto.getEstoque());
        assertEquals(999999.99, produto.getPreco());
    }

    @Test
    void testEmptyStringValues() {
        ProdutoDto produto = new ProdutoDto();
        produto.setDescricao("");
        produto.setUnidade("");

        assertEquals("", produto.getDescricao());
        assertEquals("", produto.getUnidade());
    }

    @Test
    void testPrecoPrecisao() {
        ProdutoDto produto = new ProdutoDto();
        produto.setPreco(19.999999);
        assertEquals(19.999999, produto.getPreco());
    }

    @Test
    void testUnidadesDiferentes() {
        ProdutoDto produto = new ProdutoDto();

        produto.setUnidade("UN");
        assertEquals("UN", produto.getUnidade());

        produto.setUnidade("KG");
        assertEquals("KG", produto.getUnidade());

        produto.setUnidade("LT");
        assertEquals("LT", produto.getUnidade());

        produto.setUnidade("CX");
        assertEquals("CX", produto.getUnidade());
    }
}