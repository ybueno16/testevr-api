package com.testevr.testejava.venda.internal.infra.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueRequest;
import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
import com.testevr.testejava.venda.internal.application.dto.CriarVendaDto;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;
import com.testevr.testejava.venda.internal.application.dto.VendaDto;
import com.testevr.testejava.venda.internal.application.mapper.VendaMapper;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.service.VendaExternaService;
import com.testevr.testejava.venda.internal.domain.service.VendaService;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendaController.class)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VendaService vendaService;

    @MockBean
    private VendaExternaService vendaExternaService;

    @MockBean
    private VendaMapper vendaMapper;

    @MockBean
    private ProdutoService produtoService;

    @Test
    void processarVenda_ComDadosValidos_DeveCriarVenda() throws Exception {
        // Given
        CriarVendaDto criarVendaDto = new CriarVendaDto();
        criarVendaDto.setClienteId(1L);
        criarVendaDto.setProdutoId(24L);
        criarVendaDto.setValor(150.0);
        criarVendaDto.setQuantidade(5);

        Venda vendaEntity = new Venda(null, 1L, 24L, 150.0, 5, StatusVenda.PENDENTE);
        Venda vendaCriada = new Venda(1L, 1L, 24L, 150.0, 5, StatusVenda.CONCLUIDA);
        
        VendaDto vendaDto = new VendaDto();
        vendaDto.setId(1L);
        vendaDto.setClienteId(1L);
        vendaDto.setProdutoId(24L);
        vendaDto.setValor(150.0);
        vendaDto.setQuantidade(5);
        vendaDto.setStatus(StatusVenda.CONCLUIDA);

        when(vendaMapper.toEntity(any(CriarVendaDto.class))).thenReturn(vendaEntity);
        when(vendaService.create(any(Venda.class))).thenReturn(vendaCriada);
        when(vendaMapper.toDto(any(Venda.class))).thenReturn(vendaDto);

        // When & Then
        mockMvc.perform(post("/api/v1/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarVendaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpected(jsonPath("$.clienteId").value(1L))
                .andExpected(jsonPath("$.produtoId").value(24L))
                .andExpected(jsonPath("$.quantidade").value(5))
                .andExpected(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    void processarVenda_ComErroNoService_DeveRetornarErro() throws Exception {
        // Given
        CriarVendaDto criarVendaDto = new CriarVendaDto();
        criarVendaDto.setClienteId(1L);
        criarVendaDto.setProdutoId(24L);
        criarVendaDto.setValor(150.0);
        criarVendaDto.setQuantidade(10);

        Venda vendaEntity = new Venda(null, 1L, 24L, 150.0, 10, StatusVenda.PENDENTE);

        when(vendaMapper.toEntity(any(CriarVendaDto.class))).thenReturn(vendaEntity);
        when(vendaService.create(any(Venda.class))).thenThrow(new RuntimeException("Produto sem estoque disponível"));

        // When & Then
        mockMvc.perform(post("/api/v1/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarVendaDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornarVenda() throws Exception {
        // Given
        Long vendaId = 1L;
        Venda venda = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.CONCLUIDA);
        
        VendaDto vendaDto = new VendaDto();
        vendaDto.setId(vendaId);
        vendaDto.setClienteId(1L);
        vendaDto.setProdutoId(24L);
        vendaDto.setQuantidade(5);
        vendaDto.setStatus(StatusVenda.CONCLUIDA);

        when(vendaService.findById(vendaId)).thenReturn(venda);
        when(vendaMapper.toDto(venda)).thenReturn(vendaDto);

        // When & Then
        mockMvc.perform(get("/api/v1/vendas/{id}", vendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vendaId))
                .andExpected(jsonPath("$.clienteId").value(1L));
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornarNotFound() throws Exception {
        // Given
        Long vendaId = 999L;

        when(vendaService.findById(vendaId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/v1/vendas/{id}", vendaId))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarTodas_DeveRetornarListaDeVendas() throws Exception {
        // Given
        List<Venda> vendas = Arrays.asList(
            new Venda(1L, 1L, 24L, 150.0, 5, StatusVenda.CONCLUIDA),
            new Venda(2L, 2L, 25L, 200.0, 3, StatusVenda.PENDENTE)
        );

        List<VendaDto> vendasDto = Arrays.asList(
            createVendaDto(1L, 1L, 24L, 5, StatusVenda.CONCLUIDA),
            createVendaDto(2L, 2L, 25L, 3, StatusVenda.PENDENTE)
        );

        when(vendaService.findAll()).thenReturn(vendas);
        when(vendaMapper.toDto(any(Venda.class)))
                .thenReturn(vendasDto.get(0))
                .thenReturn(vendasDto.get(1));

        // When & Then
        mockMvc.perform(get("/api/v1/vendas"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$").isArray())
                .andExpected(jsonPath("$[0].id").value(1L))
                .andExpected(jsonPath("$[1].id").value(2L));
    }

    @Test
    void buscarPorCliente_DeveRetornarVendasDoCliente() throws Exception {
        // Given
        Long clienteId = 1L;
        List<Venda> vendas = Arrays.asList(
            new Venda(1L, clienteId, 24L, 150.0, 5, StatusVenda.CONCLUIDA)
        );

        List<VendaDto> vendasDto = Arrays.asList(
            createVendaDto(1L, clienteId, 24L, 5, StatusVenda.CONCLUIDA)
        );

        when(vendaService.findByClienteId(clienteId)).thenReturn(vendas);
        when(vendaMapper.toDto(any(Venda.class))).thenReturn(vendasDto.get(0));

        // When & Then
        mockMvc.perform(get("/api/v1/vendas/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$").isArray())
                .andExpected(jsonPath("$[0].clienteId").value(clienteId));
    }

    @Test
    void atualizarStatus_ComDadosValidos_DeveAtualizarStatus() throws Exception {
        // Given
        Long vendaId = 1L;
        StatusVenda novoStatus = StatusVenda.CANCELADA;
        Venda vendaAtualizada = new Venda(vendaId, 1L, 24L, 150.0, 5, novoStatus);
        
        VendaDto vendaDto = createVendaDto(vendaId, 1L, 24L, 5, novoStatus);

        when(vendaService.atualizarStatus(vendaId, novoStatus)).thenReturn(vendaAtualizada);
        when(vendaMapper.toDto(vendaAtualizada)).thenReturn(vendaDto);

        // When & Then
        mockMvc.perform(patch("/api/v1/vendas/{id}/status", vendaId)
                .param("status", novoStatus.name()))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.id").value(vendaId))
                .andExpected(jsonPath("$.status").value("CANCELADA"));
    }

    @Test
    void cancelarVenda_ComVendaValida_DeveCancelar() throws Exception {
        // Given
        Long vendaId = 1L;
        Venda vendaCancelada = new Venda(vendaId, 1L, 24L, 150.0, 5, StatusVenda.CANCELADA);
        
        VendaDto vendaDto = createVendaDto(vendaId, 1L, 24L, 5, StatusVenda.CANCELADA);

        when(vendaService.cancelarVenda(vendaId)).thenReturn(vendaCancelada);
        when(vendaMapper.toDto(vendaCancelada)).thenReturn(vendaDto);

        // When & Then
        mockMvc.perform(patch("/api/v1/vendas/{id}/cancelar", vendaId))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.status").value("CANCELADA"));
    }

    @Test
    void cancelarVenda_ComErro_DeveRetornarBadRequest() throws Exception {
        // Given
        Long vendaId = 1L;

        when(vendaService.cancelarVenda(vendaId)).thenThrow(new RuntimeException("Venda já foi cancelada"));

        // When & Then
        mockMvc.perform(patch("/api/v1/vendas/{id}/cancelar", vendaId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletar_ComIdValido_DeveRetornarNoContent() throws Exception {
        // Given
        Long vendaId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/v1/vendas/{id}", vendaId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testeBaixaEstoque_ComDadosValidos_DeveRealizarBaixa() throws Exception {
        // Given
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        request.setId(24L);
        request.setQuantidade(5);

        BaixaEstoqueResponse response = new BaixaEstoqueResponse();
        response.setMessage("Baixa realizada com sucesso");

        when(produtoService.realizarBaixaEstoque(24L, 5)).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/vendas/teste-baixa-estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.message").value("Baixa realizada com sucesso"));
    }

    @Test
    void simularVenda_ComDadosValidos_DeveRetornarSimulacao() throws Exception {
        // Given
        CriarVendaDto vendaDto = new CriarVendaDto();
        vendaDto.setProdutoId(24L);
        vendaDto.setQuantidade(10);

        when(vendaService.verificarEstoqueDisponivel(24L)).thenReturn(7);
        when(vendaService.simularVenda(24L, 10)).thenReturn(7);

        // When & Then
        mockMvc.perform(post("/api/v1/vendas/simular-venda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendaDto)))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.produtoId").value(24L))
                .andExpected(jsonPath("$.quantidadeSolicitada").value(10))
                .andExpected(jsonPath("$.estoqueDisponivel").value(7))
                .andExpected(jsonPath("$.quantidadeQueSeriaVendida").value(7))
                .andExpected(jsonPath("$.vendaParcial").value(true))
                .andExpected(jsonPath("$.statusEsperado").value("PENDENTE"));
    }

    private VendaDto createVendaDto(Long id, Long clienteId, Long produtoId, Integer quantidade, StatusVenda status) {
        VendaDto dto = new VendaDto();
        dto.setId(id);
        dto.setClienteId(clienteId);
        dto.setProdutoId(produtoId);
        dto.setQuantidade(quantidade);
        dto.setStatus(status);
        dto.setValor(150.0);
        return dto;
    }
}
