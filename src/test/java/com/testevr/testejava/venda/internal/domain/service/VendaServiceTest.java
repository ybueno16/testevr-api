package com.testevr.testejava.venda.internal.domain.service;

import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import com.testevr.testejava.venda.external.application.dto.ProdutoDto;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.repository.VendaRepository;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private VendaService vendaService;

    @Test
    void create_ComEstoqueSuficiente_DeveCriarVendaCompleta() throws Exception {
        // Given
        Venda vendaInput = new Venda(null, 1L, 24L, 150.0, 5, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(24L, "Produto Teste", 10, 100.0, "UN", LocalDateTime.now());
        BaixaEstoqueResponse baixaResponse = new BaixaEstoqueResponse();
        baixaResponse.setMessage("Baixa realizada com sucesso");
        
        Venda vendaCriada = new Venda(1L, 1L, 24L, 150.0, 5, StatusVenda.CONCLUIDA);
        
        when(produtoService.buscarProdutoPorId(24L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(24L, 5)).thenReturn(baixaResponse);
        when(vendaRepository.create(any(Venda.class))).thenReturn(vendaCriada);

        // When
        Venda resultado = vendaService.create(vendaInput);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusVenda.CONCLUIDA, resultado.getStatus());
        assertEquals(5, resultado.getQuantidade());
        
        verify(produtoService).buscarProdutoPorId(24L);
        verify(produtoService).realizarBaixaEstoque(24L, 5);
        verify(vendaRepository).create(any(Venda.class));
    }

    @Test
    void create_ComEstoqueInsuficiente_DeveCriarVendaParcial() throws Exception {
        // Given
        Venda vendaInput = new Venda(null, 1L, 24L, 150.0, 10, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(24L, "Produto Teste", 7, 100.0, "UN", LocalDateTime.now());
        BaixaEstoqueResponse baixaResponse = new BaixaEstoqueResponse();
        baixaResponse.setMessage("Baixa realizada com sucesso");
        
        Venda vendaCriada = new Venda(1L, 1L, 24L, 150.0, 7, StatusVenda.PENDENTE);
        
        when(produtoService.buscarProdutoPorId(24L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(24L, 7)).thenReturn(baixaResponse);
        when(vendaRepository.create(any(Venda.class))).thenReturn(vendaCriada);

        // When
        Venda resultado = vendaService.create(vendaInput);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusVenda.PENDENTE, resultado.getStatus());
        assertEquals(7, resultado.getQuantidade()); // Quantidade parcial vendida
        
        verify(produtoService).buscarProdutoPorId(24L);
        verify(produtoService).realizarBaixaEstoque(24L, 7); // Baixa apenas a quantidade disponível
        verify(vendaRepository).create(any(Venda.class));
    }

    @Test
    void create_ComEstoqueZero_DeveLancarExcecao() throws IOException {
        // Given
        Venda vendaInput = new Venda(null, 1L, 24L, 150.0, 5, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(24L, "Produto Teste", 0, 100.0, "UN", LocalDateTime.now());
        
        when(produtoService.buscarProdutoPorId(24L)).thenReturn(produto);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vendaService.create(vendaInput);
        });
        
        assertTrue(exception.getMessage().contains("Produto sem estoque disponível"));
        
        verify(produtoService).buscarProdutoPorId(24L);
        verify(produtoService, never()).realizarBaixaEstoque(anyLong(), anyInt());
        verify(vendaRepository, never()).create(any(Venda.class));
    }

    @Test
    void create_ComProdutoNaoEncontrado_DeveLancarExcecao() throws IOException {
        // Given
        Venda vendaInput = new Venda(null, 1L, 999L, 150.0, 5, StatusVenda.PENDENTE);
        
        when(produtoService.buscarProdutoPorId(999L)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vendaService.create(vendaInput);
        });
        
        assertTrue(exception.getMessage().contains("Produto não encontrado"));
        
        verify(produtoService).buscarProdutoPorId(999L);
        verify(produtoService, never()).realizarBaixaEstoque(anyLong(), anyInt());
        verify(vendaRepository, never()).create(any(Venda.class));
    }

    @Test
    void create_ComErroBaixaEstoque_DeveLancarExcecao() throws Exception {
        // Given
        Venda vendaInput = new Venda(null, 1L, 24L, 150.0, 5, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(24L, "Produto Teste", 10, 100.0, "UN", LocalDateTime.now());
        
        when(produtoService.buscarProdutoPorId(24L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(24L, 5)).thenThrow(new RuntimeException("Erro na API externa"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vendaService.create(vendaInput);
        });
        
        assertTrue(exception.getMessage().contains("Erro ao realizar baixa de estoque"));
        
        verify(produtoService).buscarProdutoPorId(24L);
        verify(produtoService).realizarBaixaEstoque(24L, 5);
        verify(vendaRepository, never()).create(any(Venda.class));
    }

    @Test
    void findById_ComIdValido_DeveRetornarVenda() {
        // Given
        Long vendaId = 1L;
        Venda vendaEsperada = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.CONCLUIDA);
        
        when(vendaRepository.findById(vendaId)).thenReturn(vendaEsperada);

        // When
        Venda resultado = vendaService.findById(vendaId);

        // Then
        assertNotNull(resultado);
        assertEquals(vendaId, resultado.getIdValue());
        verify(vendaRepository).findById(vendaId);
    }

    @Test
    void findAll_DeveRetornarListaDeVendas() {
        // Given
        List<Venda> vendasEsperadas = Arrays.asList(
            new Venda(1L, 1L, 24L, 150.0, 5, StatusVenda.CONCLUIDA),
            new Venda(2L, 2L, 25L, 200.0, 3, StatusVenda.PENDENTE)
        );
        
        when(vendaRepository.findAll()).thenReturn(vendasEsperadas);

        // When
        List<Venda> resultado = vendaService.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(vendaRepository).findAll();
    }

    @Test
    void atualizarStatus_ComVendaExistente_DeveAtualizarStatus() {
        // Given
        Long vendaId = 1L;
        StatusVenda novoStatus = StatusVenda.CANCELADA;
        Venda vendaExistente = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.PENDENTE);
        Venda vendaAtualizada = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.CANCELADA);
        
        when(vendaRepository.findById(vendaId)).thenReturn(vendaExistente);
        when(vendaRepository.update(any(Venda.class))).thenReturn(vendaAtualizada);

        // When
        Venda resultado = vendaService.atualizarStatus(vendaId, novoStatus);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusVenda.CANCELADA, resultado.getStatus());
        verify(vendaRepository).findById(vendaId);
        verify(vendaRepository).update(any(Venda.class));
    }

    @Test
    void atualizarStatus_ComVendaNaoEncontrada_DeveLancarExcecao() {
        // Given
        Long vendaId = 999L;
        StatusVenda novoStatus = StatusVenda.CANCELADA;
        
        when(vendaRepository.findById(vendaId)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vendaService.atualizarStatus(vendaId, novoStatus);
        });
        
        assertTrue(exception.getMessage().contains("Venda não encontrada"));
        verify(vendaRepository).findById(vendaId);
        verify(vendaRepository, never()).update(any(Venda.class));
    }

    @Test
    void cancelarVenda_ComVendaValida_DeveCancelar() {
        // Given
        Long vendaId = 1L;
        Venda vendaExistente = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.PENDENTE);
        Venda vendaCancelada = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.CANCELADA);
        
        when(vendaRepository.findById(vendaId)).thenReturn(vendaExistente);
        when(vendaRepository.update(any(Venda.class))).thenReturn(vendaCancelada);

        // When
        Venda resultado = vendaService.cancelarVenda(vendaId);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusVenda.CANCELADA, resultado.getStatus());
        verify(vendaRepository).findById(vendaId);
        verify(vendaRepository).update(any(Venda.class));
    }

    @Test
    void cancelarVenda_ComVendaJaCancelada_DeveLancarExcecao() {
        // Given
        Long vendaId = 1L;
        Venda vendaJaCancelada = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.CANCELADA);
        
        when(vendaRepository.findById(vendaId)).thenReturn(vendaJaCancelada);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vendaService.cancelarVenda(vendaId);
        });
        
        assertTrue(exception.getMessage().contains("Venda já foi cancelada"));
        verify(vendaRepository).findById(vendaId);
        verify(vendaRepository, never()).update(any(Venda.class));
    }

    @Test
    void verificarEstoqueDisponivel_ComProdutoExistente_DeveRetornarEstoque() throws IOException {
        // Given
        Long produtoId = 24L;
        ProdutoDto produto = new ProdutoDto(produtoId, "Produto Teste", 15, 100.0, "UN", LocalDateTime.now());
        
        when(produtoService.buscarProdutoPorId(produtoId)).thenReturn(produto);

        // When
        Integer resultado = vendaService.verificarEstoqueDisponivel(produtoId);

        // Then
        assertEquals(15, resultado);
        verify(produtoService).buscarProdutoPorId(produtoId);
    }

    @Test
    void simularVenda_ComEstoqueSuficiente_DeveRetornarQuantidadeSolicitada() throws IOException {
        // Given
        Long produtoId = 24L;
        Integer quantidadeSolicitada = 5;
        ProdutoDto produto = new ProdutoDto(produtoId, "Produto Teste", 10, 100.0, "UN", LocalDateTime.now());
        
        when(produtoService.buscarProdutoPorId(produtoId)).thenReturn(produto);

        // When
        Integer resultado = vendaService.simularVenda(produtoId, quantidadeSolicitada);

        // Then
        assertEquals(5, resultado);
        verify(produtoService).buscarProdutoPorId(produtoId);
    }

    @Test
    void simularVenda_ComEstoqueInsuficiente_DeveRetornarEstoqueDisponivel() throws IOException {
        // Given
        Long produtoId = 24L;
        Integer quantidadeSolicitada = 15;
        ProdutoDto produto = new ProdutoDto(produtoId, "Produto Teste", 7, 100.0, "UN", LocalDateTime.now());
        
        when(produtoService.buscarProdutoPorId(produtoId)).thenReturn(produto);

        // When
        Integer resultado = vendaService.simularVenda(produtoId, quantidadeSolicitada);

        // Then
        assertEquals(7, resultado); // Deve retornar apenas o que está disponível
        verify(produtoService).buscarProdutoPorId(produtoId);
    }
}
