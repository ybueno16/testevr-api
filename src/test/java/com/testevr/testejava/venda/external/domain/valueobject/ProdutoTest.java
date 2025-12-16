package com.testevr.testejava.venda.external.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    @Test
    void testCreateValidProduto() {
        Long id = 1L;
        String descricao = "Produto Teste";
        Integer estoque = 100;
        BigDecimal preco = new BigDecimal("29.99");
        String unidade = "UN";
        LocalDateTime ultimaAtualizacao = LocalDateTime.of(2024, 1, 15, 10, 30);

        Produto produto = new Produto(id, descricao, estoque, preco, unidade, ultimaAtualizacao);

        assertNotNull(produto);
        assertEquals(id, produto.getId());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(estoque, produto.getEstoque());
        assertEquals(preco, produto.getPreco());
        assertEquals(unidade, produto.getUnidade());
        assertEquals(ultimaAtualizacao, produto.getUltimaAtualizacao());
    }

    @Test
    void testCreateValidProdutoWithSpaces() {
        String descricao = "  Produto com espaços  ";
        String descricaoEsperada = "Produto com espaços";
        Produto produto = new Produto(1L, descricao, 50, new BigDecimal("10.50"), "KG", null);

        assertEquals(descricaoEsperada, produto.getDescricao());
    }

    @Test
    void testCreateValidProdutoWithNullUnidade() {
        Produto produto = new Produto(2L, "Produto sem unidade", 10, new BigDecimal("5.00"), null, null);

        assertNull(produto.getUnidade());
    }

    @Test
    void testCreateValidProdutoWithUnidadeSpaces() {
        String unidade = "  UN  ";
        Produto produto = new Produto(1L, "Produto", 10, new BigDecimal("1.00"), unidade, null);

        assertEquals("UN", produto.getUnidade());
    }

    @Test
    void testCreateValidProdutoZeroEstoque() {
        Produto produto = new Produto(1L, "Produto sem estoque", 0, new BigDecimal("10.00"), "UN", null);

        assertEquals(0, produto.getEstoque());
    }

    @Test
    void testCreateValidProdutoZeroPreco() {
        Produto produto = new Produto(1L, "Produto grátis", 10, BigDecimal.ZERO, "UN", null);

        assertEquals(BigDecimal.ZERO, produto.getPreco());
    }

    @Test
    void testCreateProdutoNullDescricao() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1L, null, 10, new BigDecimal("10.00"), "UN", null));

        assertEquals("Descrição do produto não pode ser vazia", exception.getMessage());
    }

    @Test
    void testCreateProdutoEmptyDescricao() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1L, "", 10, new BigDecimal("10.00"), "UN", null));

        assertEquals("Descrição do produto não pode ser vazia", exception.getMessage());
    }

    @Test
    void testCreateProdutoWhitespaceDescricao() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1L, "   ", 10, new BigDecimal("10.00"), "UN", null));

        assertEquals("Descrição do produto não pode ser vazia", exception.getMessage());
    }

    @Test
    void testCreateProdutoNegativeEstoque() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1L, "Produto", -1, new BigDecimal("10.00"), "UN", null));

        assertEquals("Estoque do produto não pode ser negativo", exception.getMessage());
    }

    @Test
    void testCreateProdutoNullEstoque() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1L, "Produto", null, new BigDecimal("10.00"), "UN", null));

        assertEquals("Estoque do produto não pode ser negativo", exception.getMessage());
    }

    @Test
    void testCreateProdutoNegativePreco() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1L, "Produto", 10, new BigDecimal("-1.00"), "UN", null));

        assertEquals("Preço do produto não pode ser negativo", exception.getMessage());
    }

    @Test
    void testCreateProdutoNullPreco() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1L, "Produto", 10, null, "UN", null));

        assertEquals("Preço do produto não pode ser negativo", exception.getMessage());
    }

    @Test
    void testGetters() {
        Long id = 10L;
        String descricao = "Descrição do Produto";
        Integer estoque = 75;
        BigDecimal preco = new BigDecimal("15.75");
        String unidade = "CX";
        LocalDateTime ultimaAtualizacao = LocalDateTime.of(2024, 3, 1, 9, 0);

        Produto produto = new Produto(id, descricao, estoque, preco, unidade, ultimaAtualizacao);

        assertEquals(id, produto.getId());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(estoque, produto.getEstoque());
        assertEquals(preco, produto.getPreco());
        assertEquals(unidade, produto.getUnidade());
        assertEquals(ultimaAtualizacao, produto.getUltimaAtualizacao());
    }

    @Test
    void testGetDescricaoTrimmed() {
        Produto produto = new Produto(1L, "  Produto com espaços  ", 10, new BigDecimal("10.00"), "UN", null);

        assertEquals("Produto com espaços", produto.getDescricao());
    }

    @Test
    void testGetUnidadeTrimmed() {
        Produto produto = new Produto(1L, "Produto", 10, new BigDecimal("10.00"), "  KG  ", null);

        assertEquals("KG", produto.getUnidade());
    }

    @Test
    void testEquals() {
        Produto produto1 = new Produto(1L, "Produto A", 10, new BigDecimal("10.00"), "UN", null);
        Produto produto2 = new Produto(1L, "Produto A", 10, new BigDecimal("10.00"), "UN", null);
        Produto produto3 = new Produto(2L, "Produto B", 20, new BigDecimal("20.00"), "KG", null);
        Produto produto4 = new Produto(1L, "Produto Diferente", 5, new BigDecimal("5.00"), "CX", null);

        assertEquals(produto1, produto2);
        assertNotEquals(produto1, produto3);
        assertEquals(produto1, produto4);
        assertEquals(produto1.hashCode(), produto2.hashCode());
        assertNotEquals(produto1.hashCode(), produto3.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        Produto produto = new Produto(1L, "Produto", 10, new BigDecimal("10.00"), "UN", null);

        assertEquals(produto, produto);
    }

    @Test
    void testEqualsWithNull() {
        Produto produto = new Produto(1L, "Produto", 10, new BigDecimal("10.00"), "UN", null);

        assertNotEquals(null, produto);
    }

    @Test
    void testEqualsDifferentClass() {
        Produto produto = new Produto(1L, "Produto", 10, new BigDecimal("10.00"), "UN", null);

        assertNotEquals("string", produto);
    }

    @Test
    void testHashCode() {
        Produto produto1 = new Produto(1L, "Produto A", 10, new BigDecimal("10.00"), "UN", null);
        Produto produto2 = new Produto(1L, "Produto A", 10, new BigDecimal("10.00"), "UN", null);
        Produto produto3 = new Produto(2L, "Produto B", 20, new BigDecimal("20.00"), "KG", null);

        assertEquals(produto1.hashCode(), produto2.hashCode());
        assertNotEquals(produto1.hashCode(), produto3.hashCode());
    }

    @Test
    void testToString() {
        Produto produto = new Produto(1L, "Produto Teste", 100, new BigDecimal("29.99"), "UN",
                LocalDateTime.of(2024, 1, 1, 10, 30));

        String toString = produto.toString();

        assertTrue(toString.contains("Produto{"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("descricao='Produto Teste'"));
        assertTrue(toString.contains("estoque=100"));
        assertTrue(toString.contains("preco=29.99"));
        assertTrue(toString.contains("unidade='UN'"));
        assertTrue(toString.contains("ultimaAtualizacao=2024-01-01T10:30"));
    }

    @Test
    void testToStringNullValues() {
        Produto produto = new Produto(null, "Produto", 10, new BigDecimal("10.00"), null, null);

        String toString = produto.toString();

        assertTrue(toString.contains("id=null"));
        assertTrue(toString.contains("unidade='null'"));
        assertTrue(toString.contains("ultimaAtualizacao=null"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Produto com acentuação çãõé",
            "Produto & Cia Ltda.",
            "Produto 123 Teste",
            "Produto Especial!@#$%"
    })
    void testDescricaoComCaracteresEspeciais(String descricao) {
        Produto produto = new Produto(1L, descricao, 10, new BigDecimal("10.00"), "UN", null);

        assertEquals(descricao, produto.getDescricao());
    }

    @Test
    void testPrecoPrecisao() {
        BigDecimal preco = new BigDecimal("19.999999");
        Produto produto = new Produto(1L, "Produto", 10, preco, "UN", null);

        assertEquals(preco, produto.getPreco());
    }

    @Test
    void testLargeValues() {
        Produto produto = new Produto(999999L, "Produto Grande", 1000000,
                new BigDecimal("999999.99"), "UN", LocalDateTime.now());

        assertEquals(999999L, produto.getId());
        assertEquals(1000000, produto.getEstoque());
        assertEquals(new BigDecimal("999999.99"), produto.getPreco());
    }

    @Test
    void testDifferentUnidades() {
        String[] unidades = {"UN", "KG", "LT", "CX", "PCT", "M"};

        for (String unidade : unidades) {
            Produto produto = new Produto(1L, "Produto", 10, new BigDecimal("10.00"), unidade, null);
            assertEquals(unidade, produto.getUnidade());
        }
    }

    @Test
    void testUltimaAtualizacaoDifferentFormats() {
        LocalDateTime data1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime data2 = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

        Produto produto1 = new Produto(1L, "Produto 1", 10, new BigDecimal("10.00"), "UN", data1);
        Produto produto2 = new Produto(2L, "Produto 2", 20, new BigDecimal("20.00"), "KG", data2);

        assertEquals(data1, produto1.getUltimaAtualizacao());
        assertEquals(data2, produto2.getUltimaAtualizacao());
    }
}
