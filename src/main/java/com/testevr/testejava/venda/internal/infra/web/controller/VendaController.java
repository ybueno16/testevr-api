package com.testevr.testejava.venda.internal.infra.web.controller;


import com.testevr.testejava.venda.internal.application.dto.CriarVendaDto;
import com.testevr.testejava.venda.internal.application.dto.VendaDto;
import com.testevr.testejava.venda.internal.application.mapper.VendaMapper;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.service.VendaService;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueRequest;
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
    private final VendaMapper mapper;
    private final ProdutoService produtoService;

    public VendaController(VendaService vendaService,
                          VendaMapper mapper, ProdutoService produtoService) {
        this.vendaService = vendaService;
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
    @PostMapping("/baixa")
    public ResponseEntity<?> baixarEstoqueEmLote(@RequestBody List<BaixaEstoqueRequest> requests) {
        try {
            Object response = produtoService.realizarBaixaEstoqueEmLote(requests);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

