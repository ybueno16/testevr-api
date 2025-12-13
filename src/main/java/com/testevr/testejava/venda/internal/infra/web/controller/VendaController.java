package com.testevr.testejava.venda.internal.infra.web.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${app.api.base-path}/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final VendaExternaService vendaExternaService;
    private final VendaMapper mapper;

    public VendaController(VendaService vendaService, VendaExternaService vendaExternaService,
                          VendaMapper mapper, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.vendaExternaService = vendaExternaService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VendaDto> processarVenda(@RequestBody VendaDto vendaDto) {
        try {
            // Criar a venda usando o novo fluxo com validação de estoque
            Venda vendaEntity = mapper.toEntity(vendaDto);
            Venda vendaCriada = vendaService.create(vendaEntity);

            // A venda já foi criada com o status correto baseado no estoque
            // Não precisamos chamar a API externa aqui, pois a validação de estoque
            // e baixa já são feitas no serviço de venda
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(vendaCriada));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar venda: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDto> buscarPorId(@PathVariable Long id) {
        Venda venda = vendaService.findById(id);
        if (venda == null) {
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
        return ResponseEntity.ok(vendasDto);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaDto>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Venda> vendas = vendaService.findByClienteId(clienteId);
        List<VendaDto> vendasDto = vendas.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
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

        Venda vendaAtualizada = vendaService.atualizarStatus(id, status);
        return ResponseEntity.ok(mapper.toDto(vendaAtualizada));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<VendaDto> cancelarVenda(@PathVariable Long id) {
        try {
            Venda vendaCancelada = vendaService.cancelarVenda(id);
            return ResponseEntity.ok(mapper.toDto(vendaCancelada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/finalizar-parcial")
    public ResponseEntity<VendaDto> finalizarVendaParcial(@PathVariable Long id) {
        try {
            Venda vendaFinalizada = vendaService.finalizarVendaParcial(id);
            return ResponseEntity.ok(mapper.toDto(vendaFinalizada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        vendaService.delete(id);
    }

    @PostMapping("/{id}/produtos")
    public ResponseEntity<VendaDto> adicionarProdutoVenda(
            @PathVariable Long id,
            @RequestBody VendaDto produtoParaAdicionar) {
        try {
            Venda vendaExistente = vendaService.findById(id);
            if (vendaExistente == null) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se a venda pode ser editada (não finalizada)
            if (vendaExistente.getStatus() == StatusVenda.SUCESSO || 
                vendaExistente.getStatus() == StatusVenda.CANCELADA) {
                return ResponseEntity.badRequest().build();
            }

            // Adicionar produto à venda (soma quantidades se produto já existe)
            Venda vendaAtualizada = vendaService.adicionarProdutoVenda(id, 
                produtoParaAdicionar.getProdutoId(), 
                produtoParaAdicionar.getQuantidade());
            
            return ResponseEntity.ok(mapper.toDto(vendaAtualizada));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar produto à venda: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaDto> editarVenda(@PathVariable Long id, @RequestBody VendaDto vendaDto) {
        try {
            Venda vendaExistente = vendaService.findById(id);
            if (vendaExistente == null) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se a venda pode ser editada
            if (vendaExistente.getStatus() == StatusVenda.SUCESSO || 
                vendaExistente.getStatus() == StatusVenda.CANCELADA) {
                return ResponseEntity.badRequest().build();
            }

            Venda vendaEntity = mapper.toEntity(vendaDto);
            vendaEntity = vendaEntity.atualizarId(vendaExistente.getId());
            Venda vendaAtualizada = vendaService.editarVenda(vendaEntity);
            
            return ResponseEntity.ok(mapper.toDto(vendaAtualizada));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar venda: " + e.getMessage(), e);
        }
    }
}
