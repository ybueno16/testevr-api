package com.testevr.testejava.venda.internal.infra.web.controller;

import com.testevr.testejava.venda.internal.application.dto.CriarVendaDto;
import com.testevr.testejava.venda.internal.application.dto.VendaDto;
import com.testevr.testejava.venda.internal.application.dto.VendaExternaRequest;
import com.testevr.testejava.venda.internal.application.dto.VendaExternaResponse;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;
import com.testevr.testejava.venda.internal.application.mapper.VendaMapper;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.service.VendaExternaService;
import com.testevr.testejava.venda.internal.domain.service.VendaService;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueRequest;
import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${app.api.base-path}/vendas")
public class VendaController {

    private static final Logger logger = LoggerFactory.getLogger(VendaController.class);

    private final VendaService vendaService;
    private final VendaExternaService vendaExternaService;
    private final VendaMapper mapper;
    private final ProdutoService produtoService;

    public VendaController(VendaService vendaService, VendaExternaService vendaExternaService,
                          VendaMapper mapper, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.vendaExternaService = vendaExternaService;
        this.mapper = mapper;
        this.produtoService = produtoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VendaDto> processarVenda(@RequestBody CriarVendaDto criarVendaDto) {
        try {
            logger.info("Processando nova venda - Cliente: {}, Produto: {}, Quantidade: {}", 
                       criarVendaDto.getClienteId(), criarVendaDto.getProdutoId(), criarVendaDto.getQuantidade());
            
            Venda vendaEntity = mapper.toEntity(criarVendaDto);
            Venda vendaCriada = vendaService.create(vendaEntity);

            logger.info("Venda processada com sucesso - ID: {}, Status: {}", 
                       vendaCriada.getIdValue(), vendaCriada.getStatus());

            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(vendaCriada));

        } catch (Exception e) {
            logger.error("Erro ao processar venda: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar venda: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDto> buscarPorId(@PathVariable Long id) {
        Venda venda = vendaService.findById(id);
        
        if (venda == null) {
            logger.warn("Venda não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(mapper.toDto(venda));
    }

    @GetMapping
    public ResponseEntity<List<VendaDto>> buscarTodas() {
        List<Venda> vendas = vendaService.findAll();
        List<VendaDto> vendasDto = vendas.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        logger.debug("Retornando {} vendas", vendasDto.size());
        return ResponseEntity.ok(vendasDto);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaDto>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Venda> vendas = vendaService.findByClienteId(clienteId);
        List<VendaDto> vendasDto = vendas.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        logger.debug("Retornando {} vendas para cliente {}", vendasDto.size(), clienteId);
        return ResponseEntity.ok(vendasDto);
    }

    @GetMapping("/consolidada")
    public ResponseEntity<List<VendaConsolidadaDto>> buscarVendasConsolidadas() {
        List<VendaConsolidadaDto> vendasConsolidadas = vendaService.buscarVendasConsolidadas();
        return ResponseEntity.ok(vendasConsolidadas);
    }

    @GetMapping("/consolidada/cliente/{clienteId}")
    public ResponseEntity<List<VendaConsolidadaDto>> buscarVendasConsolidadasPorCliente(@PathVariable Long clienteId) {
        List<VendaConsolidadaDto> vendasConsolidadas = vendaService.buscarVendasConsolidadasPorCliente(clienteId);
        return ResponseEntity.ok(vendasConsolidadas);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VendaDto> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusVenda status) {

        logger.info("Atualizando status da venda {} para {}", id, status);
        Venda vendaAtualizada = vendaService.atualizarStatus(id, status);
        return ResponseEntity.ok(mapper.toDto(vendaAtualizada));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<VendaDto> cancelarVenda(@PathVariable Long id) {
        try {
            logger.info("Cancelando venda {}", id);
            Venda vendaCancelada = vendaService.cancelarVenda(id);
            return ResponseEntity.ok(mapper.toDto(vendaCancelada));
        } catch (RuntimeException e) {
            logger.error("Erro ao cancelar venda {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/finalizar-parcial")
    public ResponseEntity<VendaDto> finalizarVendaParcial(@PathVariable Long id) {
        try {
            logger.info("Finalizando venda parcial {}", id);
            Venda vendaFinalizada = vendaService.finalizarVendaParcial(id);
            return ResponseEntity.ok(mapper.toDto(vendaFinalizada));
        } catch (RuntimeException e) {
            logger.error("Erro ao finalizar venda parcial {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        logger.info("Deletando venda {}", id);
        vendaService.delete(id);
    }

    @PostMapping("/{id}/produtos")
    public ResponseEntity<VendaDto> adicionarProdutoVenda(
            @PathVariable Long id,
            @RequestBody VendaDto produtoParaAdicionar) {
        try {
            Venda vendaExistente = vendaService.findById(id);
            
            if (vendaExistente == null) {
                logger.warn("Tentativa de adicionar produto à venda inexistente: {}", id);
                return ResponseEntity.notFound().build();
            }

            if (vendaExistente.getStatus() == StatusVenda.SUCESSO || 
                vendaExistente.getStatus() == StatusVenda.CANCELADA) {
                logger.warn("Tentativa de editar venda finalizada: {}", id);
                return ResponseEntity.badRequest().build();
            }

            logger.info("Adicionando produto {} à venda {}", produtoParaAdicionar.getProdutoId(), id);
            Venda vendaAtualizada = vendaService.adicionarProdutoVenda(id, 
                produtoParaAdicionar.getProdutoId(), 
                produtoParaAdicionar.getQuantidade());
            
            return ResponseEntity.ok(mapper.toDto(vendaAtualizada));

        } catch (Exception e) {
            logger.error("Erro ao adicionar produto à venda {}: {}", id, e.getMessage());
            throw new RuntimeException("Erro ao adicionar produto à venda: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaDto> editarVenda(@PathVariable Long id, @RequestBody VendaDto vendaDto) {
        try {
            Venda vendaExistente = vendaService.findById(id);
            
            if (vendaExistente == null) {
                logger.warn("Tentativa de editar venda inexistente: {}", id);
                return ResponseEntity.notFound().build();
            }

            if (vendaExistente.getStatus() == StatusVenda.SUCESSO || 
                vendaExistente.getStatus() == StatusVenda.CANCELADA) {
                logger.warn("Tentativa de editar venda finalizada: {}", id);
                return ResponseEntity.badRequest().build();
            }

            logger.info("Editando venda {}", id);
            Venda vendaEntity = mapper.toEntity(vendaDto);
            vendaEntity = vendaEntity.atualizarId(vendaExistente.getIdValue());
            Venda vendaAtualizada = vendaService.editarVenda(vendaEntity);
            
            return ResponseEntity.ok(mapper.toDto(vendaAtualizada));

        } catch (Exception e) {
            logger.error("Erro ao editar venda {}: {}", id, e.getMessage());
            throw new RuntimeException("Erro ao editar venda: " + e.getMessage(), e);
        }
    }

    @PostMapping("/teste-baixa-estoque")
    public ResponseEntity<?> testeBaixaEstoque(@RequestBody BaixaEstoqueRequest request) {
        try {
            logger.info("Testando baixa de estoque - Produto: {}, Quantidade: {}", 
                       request.getId(), request.getQuantidade());
            
            BaixaEstoqueResponse response = produtoService.realizarBaixaEstoque(
                request.getId(), 
                request.getQuantidade()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro no teste de baixa de estoque: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro na baixa de estoque: " + e.getMessage());
        }
    }

    @PostMapping("/simular-venda")
    public ResponseEntity<?> simularVenda(@RequestBody CriarVendaDto vendaDto) {
        try {
            logger.info("Simulando venda - Produto: {}, Quantidade: {}", 
                       vendaDto.getProdutoId(), vendaDto.getQuantidade());
            
            Integer estoqueDisponivel = vendaService.verificarEstoqueDisponivel(vendaDto.getProdutoId());
            Integer quantidadePossivel = vendaService.simularVenda(vendaDto.getProdutoId(), vendaDto.getQuantidade());
            
            boolean vendaParcial = !quantidadePossivel.equals(vendaDto.getQuantidade());
            
            return ResponseEntity.ok(java.util.Map.of(
                "produtoId", vendaDto.getProdutoId(),
                "quantidadeSolicitada", vendaDto.getQuantidade(),
                "estoqueDisponivel", estoqueDisponivel,
                "quantidadeQueSeriaVendida", quantidadePossivel,
                "vendaParcial", vendaParcial,
                "statusEsperado", vendaParcial ? "PENDENTE" : "CONCLUIDA"
            ));
        } catch (Exception e) {
            logger.error("Erro na simulação de venda: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro na simulação: " + e.getMessage());
        }
    }
}

