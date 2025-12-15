package com.testevr.testejava.venda.internal.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import com.testevr.testejava.venda.external.application.dto.ProdutoDto;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.repository.VendaRepository;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import com.testevr.testejava.venda.internal.domain.valueobject.ValorVenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository repository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ValorVenda valorVenda;

    private VendaService vendaService;

    private LocalDateTime dataAtual;

    @BeforeEach
    void setUp() {
        vendaService = new VendaService(repository, produtoService);
        dataAtual = LocalDateTime.now();
    }

    @Test
    void deveCriarVendaComSucesso() throws Exception {
        Venda venda = new Venda(null, 1L, 100L, valorVenda, 5);
        Venda vendaSalva = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CONCLUIDA);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);
        BaixaEstoqueResponse baixaResponse = new BaixaEstoqueResponse("sucesso");

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(100L, 5)).thenReturn(baixaResponse);
        when(repository.create(any(Venda.class))).thenReturn(vendaSalva);

        Venda resultado = vendaService.create(venda);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdValue());
        assertEquals(StatusVenda.CONCLUIDA, resultado.getStatus());
        verify(produtoService).buscarProdutoPorId(100L);
        verify(produtoService).realizarBaixaEstoque(100L, 5);
        verify(repository).create(any(Venda.class));
    }

    @Test
    void deveCriarVendaParcialQuandoEstoqueInsuficiente() throws Exception {
        Venda venda = new Venda(null, 1L, 100L, valorVenda, 10);
        Venda vendaSalva = new Venda(1L, 1L, 100L, valorVenda, 3, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 3, 50.0, "UN", dataAtual);
        BaixaEstoqueResponse baixaResponse = new BaixaEstoqueResponse("sucesso");

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(100L, 3)).thenReturn(baixaResponse);
        when(repository.create(any(Venda.class))).thenReturn(vendaSalva);

        Venda resultado = vendaService.create(venda);

        assertNotNull(resultado);
        assertEquals(StatusVenda.PENDENTE, resultado.getStatus());
        assertEquals(3, resultado.getQuantidade());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() throws Exception {
        Venda venda = new Venda(null, 1L, 100L, valorVenda, 5);

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> vendaService.create(venda));
        assertTrue(exception.getMessage().contains("Produto não encontrado"));
        verify(repository, never()).create(any());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoSemEstoque() throws Exception {
        Venda venda = new Venda(null, 1L, 100L, valorVenda, 5);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 0, 50.0, "UN", dataAtual);

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);

        Exception exception = assertThrows(RuntimeException.class, () -> vendaService.create(venda));
        assertTrue(exception.getMessage().contains("sem estoque"));
        verify(repository, never()).create(any());
    }

    @Test
    void deveAtualizarVenda() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CONCLUIDA);
        when(repository.update(venda)).thenReturn(venda);

        Venda resultado = vendaService.update(venda);

        assertEquals(venda, resultado);
        verify(repository).update(venda);
    }

    @Test
    void deveBuscarVendaPorId() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CONCLUIDA);
        when(repository.findById(1L)).thenReturn(venda);

        Venda resultado = vendaService.findById(1L);

        assertEquals(venda, resultado);
        verify(repository).findById(1L);
    }

    @Test
    void deveRetornarTodasVendas() {
        Venda venda1 = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CONCLUIDA);
        Venda venda2 = new Venda(2L, 2L, 101L, valorVenda, 3, StatusVenda.PENDENTE);
        List<Venda> vendas = Arrays.asList(venda1, venda2);

        when(repository.findAll()).thenReturn(vendas);

        List<Venda> resultado = vendaService.findAll();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void deveBuscarVendasPorClienteId() {
        Venda venda1 = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CONCLUIDA);
        Venda venda2 = new Venda(2L, 1L, 101L, valorVenda, 3, StatusVenda.PENDENTE);
        List<Venda> vendas = Arrays.asList(venda1, venda2);

        when(repository.findByClienteId(1L)).thenReturn(vendas);

        List<Venda> resultado = vendaService.findByClienteId(1L);

        assertEquals(2, resultado.size());
        verify(repository).findByClienteId(1L);
    }

    @Test
    void deveAtualizarStatusVenda() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        Venda vendaAtualizada = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CONCLUIDA);

        when(repository.findById(1L)).thenReturn(venda);
        when(repository.update(any(Venda.class))).thenReturn(vendaAtualizada);

        Venda resultado = vendaService.atualizarStatus(1L, StatusVenda.CONCLUIDA);

        assertEquals(StatusVenda.CONCLUIDA, resultado.getStatus());
        verify(repository).findById(1L);
        verify(repository).update(any(Venda.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarStatusDeVendaInexistente() {
        when(repository.findById(1L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.atualizarStatus(1L, StatusVenda.CONCLUIDA));

        assertTrue(exception.getMessage().contains("Venda não encontrada"));
    }

    @Test
    void deveCancelarVenda() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        Venda vendaCancelada = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CANCELADA);

        when(repository.findById(1L)).thenReturn(venda);
        when(repository.update(any(Venda.class))).thenReturn(vendaCancelada);

        Venda resultado = vendaService.cancelarVenda(1L);

        assertEquals(StatusVenda.CANCELADA, resultado.getStatus());
        verify(repository).findById(1L);
        verify(repository).update(any(Venda.class));
    }

    @Test
    void deveLancarExcecaoAoCancelarVendaInexistente() {
        when(repository.findById(1L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.cancelarVenda(1L));

        assertTrue(exception.getMessage().contains("Venda não encontrada"));
    }

    @Test
    void deveLancarExcecaoAoCancelarVendaJaCancelada() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CANCELADA);

        when(repository.findById(1L)).thenReturn(venda);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.cancelarVenda(1L));

        assertTrue(exception.getMessage().contains("já foi cancelada"));
    }

    @Test
    void deveLancarExcecaoAoCancelarVendaComErro() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.ERRO);

        when(repository.findById(1L)).thenReturn(venda);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.cancelarVenda(1L));

        assertTrue(exception.getMessage().contains("já foi cancelada"));
    }

    @Test
    void deveDeletarVenda() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        when(repository.findById(1L)).thenReturn(venda);
        doNothing().when(repository).delete(1L);

        vendaService.delete(1L);

        verify(repository).findById(1L);
        verify(repository).delete(1L);
    }
    

    @Test
    void deveAdicionarProdutoVenda() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        Venda vendaAtualizada = new Venda(1L, 1L, 100L, valorVenda, 8, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(repository.update(any(Venda.class))).thenReturn(vendaAtualizada);

        Venda resultado = vendaService.adicionarProdutoVenda(1L, 100L, 3);

        assertEquals(8, resultado.getQuantidade());
        verify(repository).findById(1L);
        verify(produtoService).buscarProdutoPorId(100L);
        verify(repository).update(any(Venda.class));
    }

    @Test
    void deveAdicionarNovoProdutoVenda() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        Venda vendaAtualizada = new Venda(1L, 1L, 101L, valorVenda, 3, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(101L, "Novo Produto", 10, 50.0, "UN", dataAtual);

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(101L)).thenReturn(produto);
        when(repository.update(any(Venda.class))).thenReturn(vendaAtualizada);

        Venda resultado = vendaService.adicionarProdutoVenda(1L, 101L, 3);

        assertEquals(101L, resultado.getProdutoId());
        assertEquals(3, resultado.getQuantidade());
    }

    @Test
    void deveLancarExcecaoAoAdicionarProdutoEmVendaFinalizada() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.SUCESSO);

        when(repository.findById(1L)).thenReturn(venda);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.adicionarProdutoVenda(1L, 100L, 3));

        assertTrue(exception.getMessage().contains("finalizada ou cancelada"));
    }

    @Test
    void deveLancarExcecaoAoAdicionarProdutoEmVendaCancelada() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.CANCELADA);

        when(repository.findById(1L)).thenReturn(venda);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.adicionarProdutoVenda(1L, 100L, 3));

        assertTrue(exception.getMessage().contains("finalizada ou cancelada"));
    }

    @Test
    void deveLancarExcecaoAoAdicionarProdutoInexistente() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(200L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.adicionarProdutoVenda(1L, 200L, 3));

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
    }

    @Test
    void deveLancarExcecaoAoAdicionarProdutoComEstoqueInsuficiente() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 2, 50.0, "UN", dataAtual);

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.adicionarProdutoVenda(1L, 100L, 3));

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    void deveEditarVenda() throws Exception {
        Venda vendaAtualizada = new Venda(1L, 1L, 100L, valorVenda, 8, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(repository.update(vendaAtualizada)).thenReturn(vendaAtualizada);

        Venda resultado = vendaService.editarVenda(vendaAtualizada);

        assertEquals(vendaAtualizada, resultado);
        verify(produtoService).buscarProdutoPorId(100L);
        verify(repository).update(vendaAtualizada);
    }

    @Test
    void deveLancarExcecaoAoEditarVendaComProdutoInexistente() throws Exception {
        Venda vendaAtualizada = new Venda(1L, 1L, 100L, valorVenda, 15, StatusVenda.PENDENTE);

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.editarVenda(vendaAtualizada));

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
    }

    @Test
    void deveLancarExcecaoAoEditarVendaComEstoqueInsuficiente() throws Exception {
        Venda vendaAtualizada = new Venda(1L, 1L, 100L, valorVenda, 15, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.editarVenda(vendaAtualizada));

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    void deveFinalizarVendaParcialCompleta() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        Venda vendaFinalizada = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.SUCESSO);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);
        BaixaEstoqueResponse baixaResponse = new BaixaEstoqueResponse("sucesso");

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(100L, 5)).thenReturn(baixaResponse);
        when(repository.update(any(Venda.class))).thenReturn(vendaFinalizada);

        Venda resultado = vendaService.finalizarVendaParcial(1L);

        assertEquals(StatusVenda.SUCESSO, resultado.getStatus());
        verify(repository).findById(1L);
        verify(produtoService).buscarProdutoPorId(100L);
        verify(produtoService).realizarBaixaEstoque(100L, 5);
        verify(repository).update(any(Venda.class));
    }

    @Test
    void deveFinalizarVendaParcialParcial() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 10, StatusVenda.PENDENTE);
        Venda vendaFinalizada = new Venda(1L, 1L, 100L, valorVenda, 3, StatusVenda.SUCESSO,
                venda.getCreatedAt(), venda.getUpdatedAt());
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 3, 50.0, "UN", dataAtual);
        BaixaEstoqueResponse baixaResponse = new BaixaEstoqueResponse("sucesso");

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(100L, 3)).thenReturn(baixaResponse);
        when(repository.update(any(Venda.class))).thenReturn(vendaFinalizada);

        Venda resultado = vendaService.finalizarVendaParcial(1L);

        assertEquals(3, resultado.getQuantidade());
        assertEquals(StatusVenda.SUCESSO, resultado.getStatus());
    }

    @Test
    void deveLancarExcecaoAoFinalizarVendaNaoPendente() {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.SUCESSO);

        when(repository.findById(1L)).thenReturn(venda);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.finalizarVendaParcial(1L));

        assertTrue(exception.getMessage().contains("pendentes"));
    }

    @Test
    void deveLancarExcecaoAoFinalizarVendaComProdutoInexistente() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.finalizarVendaParcial(1L));

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
    }

    @Test
    void deveLancarExcecaoAoFinalizarVendaSemEstoqueDisponivel() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 10, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 0, 50.0, "UN", dataAtual);

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.finalizarVendaParcial(1L));

        assertTrue(exception.getMessage().contains("Sem estoque"));
    }

    @Test
    void deveLancarExcecaoAoFinalizarVendaComErroNaBaixa() throws Exception {
        Venda venda = new Venda(1L, 1L, 100L, valorVenda, 5, StatusVenda.PENDENTE);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);

        when(repository.findById(1L)).thenReturn(venda);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(100L, 5))
                .thenThrow(new RuntimeException("Erro na baixa"));

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.finalizarVendaParcial(1L));

        assertTrue(exception.getMessage().contains("Erro ao finalizar venda"));
    }

    @Test
    void deveBuscarVendasConsolidadas() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();
        List<VendaConsolidadaDto> dtos = Collections.singletonList(dto);

        when(repository.buscarVendasConsolidadas()).thenReturn(dtos);

        List<VendaConsolidadaDto> resultado = vendaService.buscarVendasConsolidadas();

        assertEquals(1, resultado.size());
        verify(repository).buscarVendasConsolidadas();
    }

    @Test
    void deveBuscarVendasConsolidadasPorCliente() {
        VendaConsolidadaDto dto = new VendaConsolidadaDto();
        List<VendaConsolidadaDto> dtos = Collections.singletonList(dto);

        when(repository.buscarVendasConsolidadasPorCliente(1L)).thenReturn(dtos);

        List<VendaConsolidadaDto> resultado = vendaService.buscarVendasConsolidadasPorCliente(1L);

        assertEquals(1, resultado.size());
        verify(repository).buscarVendasConsolidadasPorCliente(1L);
    }

    @Test
    void deveVerificarEstoqueDisponivel() throws Exception {
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 25, 50.0, "UN", dataAtual);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);

        Integer estoque = vendaService.verificarEstoqueDisponivel(100L);

        assertEquals(25, estoque);
        verify(produtoService).buscarProdutoPorId(100L);
    }

    @Test
    void deveLancarExcecaoAoVerificarEstoqueDeProdutoInexistente() throws Exception {
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.verificarEstoqueDisponivel(100L));

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
    }

    @Test
    void deveSimularVenda() throws Exception {
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);

        Integer quantidadeVendavel = vendaService.simularVenda(100L, 15);

        assertEquals(10, quantidadeVendavel);
        verify(produtoService).buscarProdutoPorId(100L);
    }

    @Test
    void deveSimularVendaComEstoqueZero() throws Exception {
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 0, 50.0, "UN", dataAtual);
        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);

        Integer quantidadeVendavel = vendaService.simularVenda(100L, 15);

        assertEquals(0, quantidadeVendavel);
    }

    @Test
    void deveLancarExcecaoQuandoIOExceptionNaCriacao() throws Exception {
        Venda venda = new Venda(null, 1L, 100L, valorVenda, 5);
        when(produtoService.buscarProdutoPorId(100L)).thenThrow(new IOException("Erro de conexão"));

        Exception exception = assertThrows(RuntimeException.class, () -> vendaService.create(venda));
        assertTrue(exception.getMessage().contains("Erro de comunicação"));
    }

    @Test
    void deveLancarExcecaoQuandoErroNaBaixaEstoque() throws Exception {
        Venda venda = new Venda(null, 1L, 100L, valorVenda, 5);
        ProdutoDto produto = new ProdutoDto(100L, "Produto Teste", 10, 50.0, "UN", dataAtual);

        when(produtoService.buscarProdutoPorId(100L)).thenReturn(produto);
        when(produtoService.realizarBaixaEstoque(100L, 5))
                .thenThrow(new RuntimeException("Erro na baixa"));

        Exception exception = assertThrows(RuntimeException.class, () -> vendaService.create(venda));
        assertTrue(exception.getMessage().contains("Erro ao realizar baixa"));
    }

    @Test
    void deveLancarExcecaoQuandoIOExceptionAoVerificarEstoque() throws Exception {
        when(produtoService.buscarProdutoPorId(100L)).thenThrow(new IOException("Erro de conexão"));

        Exception exception = assertThrows(RuntimeException.class,
                () -> vendaService.verificarEstoqueDisponivel(100L));

        assertTrue(exception.getMessage().contains("Erro ao consultar estoque"));
    }

}