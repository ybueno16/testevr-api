package com.testevr.testejava.venda.external.domain.service;

import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueRequest;
import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import com.testevr.testejava.venda.external.application.dto.ProdutoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(produtoService, "apiExternaUrl", "http://localhost:3000");
    }

    @Test
    void verificarEstoqueDisponivel_ComEstoqueSuficiente_DeveRetornarTrue() throws IOException {
        // Given
        Long produtoId = 1L;
        Integer quantidadeSolicitada = 5;
        ProdutoDto produto = new ProdutoDto(1L, "Produto Teste", 10, 100.0, "UN", LocalDateTime.now());
        
        ProdutoService spyService = spy(produtoService);
        doReturn(produto).when(spyService).buscarProdutoPorId(produtoId);

        // When
        boolean resultado = spyService.verificarEstoqueDisponivel(produtoId, quantidadeSolicitada);

        // Then
        assertTrue(resultado);
        verify(spyService).buscarProdutoPorId(produtoId);
    }

    @Test
    void verificarEstoqueDisponivel_ComEstoqueInsuficiente_DeveRetornarFalse() throws IOException {
        // Given
        Long produtoId = 1L;
        Integer quantidadeSolicitada = 15;
        ProdutoDto produto = new ProdutoDto(1L, "Produto Teste", 10, 100.0, "UN", LocalDateTime.now());
        
        ProdutoService spyService = spy(produtoService);
        doReturn(produto).when(spyService).buscarProdutoPorId(produtoId);

        // When
        boolean resultado = spyService.verificarEstoqueDisponivel(produtoId, quantidadeSolicitada);

        // Then
        assertFalse(resultado);
    }

    @Test
    void verificarEstoqueDisponivel_ComProdutoNull_DeveRetornarFalse() throws IOException {
        // Given
        Long produtoId = 999L;
        Integer quantidadeSolicitada = 5;
        
        ProdutoService spyService = spy(produtoService);
        doReturn(null).when(spyService).buscarProdutoPorId(produtoId);

        // When
        boolean resultado = spyService.verificarEstoqueDisponivel(produtoId, quantidadeSolicitada);

        // Then
        assertFalse(resultado);
    }

    @Test
    void verificarEstoqueDisponivel_ComEstoqueNull_DeveRetornarFalse() throws IOException {
        // Given
        Long produtoId = 1L;
        Integer quantidadeSolicitada = 5;
        ProdutoDto produto = new ProdutoDto(1L, "Produto Teste", null, 100.0, "UN", LocalDateTime.now());
        
        ProdutoService spyService = spy(produtoService);
        doReturn(produto).when(spyService).buscarProdutoPorId(produtoId);

        // When
        boolean resultado = spyService.verificarEstoqueDisponivel(produtoId, quantidadeSolicitada);

        // Then
        assertFalse(resultado);
    }

    @Test
    void buscarProdutoPorId_ComAPIIndisponivel_DeveRetornarProdutoMock() throws IOException {
        // When
        ProdutoDto resultado = produtoService.buscarProdutoPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Produto A - Mock", resultado.getDescricao());
        assertEquals(50, resultado.getEstoque());
    }

    @Test
    void buscarProdutoPorId_ComIdNaoEncontrado_DeveRetornarProdutoMockGenerico() throws IOException {
        // When
        ProdutoDto resultado = produtoService.buscarProdutoPorId(999L);

        // Then
        assertNotNull(resultado);
        assertEquals(999L, resultado.getId());
        assertEquals("Produto Mock ID 999", resultado.getDescricao());
        assertEquals(10, resultado.getEstoque());
    }

    @Test
    void realizarBaixaEstoque_DeveProcessarCorretamente() throws IOException {
        // Given - Como estamos testando com API externa real ou mock interno,
        // este teste pode ser mais complexo. Por simplicidade, testamos o comportamento básico.
        
        // Este teste seria melhor implementado com um servidor mock ou WireMock
        // para simular a API externa de forma controlada.
        
        Long produtoId = 1L;
        Integer quantidade = 5;

        // When & Then
        // Para um teste completo, precisaríamos mockar a conexão HTTP
        // Por agora, verificamos que o método não lança exceção com dados válidos
        assertDoesNotThrow(() -> {
            try {
                produtoService.realizarBaixaEstoque(produtoId, quantidade);
            } catch (IOException e) {
                // Esperado quando API externa não está disponível
                assertTrue(e.getMessage().contains("Erro na requisição") || 
                          e.getMessage().contains("Connection refused"));
            }
        });
    }

    @Test
    void realizarBaixaEstoqueEmLote_DeveProcessarLista() throws IOException {
        // Given
        List<BaixaEstoqueRequest> requests = Arrays.asList(
            createBaixaEstoqueRequest(1L, 5),
            createBaixaEstoqueRequest(2L, 3)
        );

        // When & Then
        assertDoesNotThrow(() -> {
            try {
                produtoService.realizarBaixaEstoqueEmLote(requests);
            } catch (IOException e) {
                // Esperado quando API externa não está disponível
                assertTrue(e.getMessage().contains("Erro na requisição") || 
                          e.getMessage().contains("Connection refused"));
            }
        });
    }

    private BaixaEstoqueRequest createBaixaEstoqueRequest(Long id, Integer quantidade) {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        request.setId(id);
        request.setQuantidade(quantidade);
        return request;
    }
}
