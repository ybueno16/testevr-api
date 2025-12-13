package com.testevr.testejava.venda.internal.domain.service;

import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import com.testevr.testejava.venda.external.application.dto.ProdutoDto;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.repository.VendaRepository;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class VendaService {
    
    private static final Logger logger = LoggerFactory.getLogger(VendaService.class);
    
    private final VendaRepository repository;
    private final ProdutoService produtoService;

    public VendaService(VendaRepository repository, ProdutoService produtoService) {
        this.repository = repository;
        this.produtoService = produtoService;
    }

    @Transactional
    public Venda create(Venda venda) throws Exception {
        try {
            ProdutoDto produto = validarProdutoExiste(venda.getProdutoId());
            validarEstoqueDisponivel(produto);
            
            Integer quantidadeVendida = calcularQuantidadeVenda(venda.getQuantidade(), produto.getEstoque());
            boolean vendaParcial = !quantidadeVendida.equals(venda.getQuantidade());
            
            logger.info("Processando venda - Produto: {}, Solicitado: {}, Disponível: {}, Vendida: {}", 
                       venda.getProdutoId(), venda.getQuantidade(), produto.getEstoque(), quantidadeVendida);
            
            realizarBaixaEstoque(venda.getProdutoId(), quantidadeVendida);
            
            StatusVenda statusFinal = vendaParcial ? StatusVenda.PENDENTE : StatusVenda.CONCLUIDA;
            
            Venda vendaComDadosCorretos = new Venda(
                null,
                venda.getClienteId(),
                venda.getProdutoId(),
                venda.getValor(),
                quantidadeVendida,
                statusFinal
            );
            
            Venda vendaCriada = this.repository.create(vendaComDadosCorretos);
            
            logResultadoVenda(venda, quantidadeVendida, vendaParcial, vendaCriada);
            
            return vendaCriada;

        } catch (IOException e) {
            logger.error("Erro de comunicação ao verificar produto: {}", e.getMessage());
            throw new RuntimeException("Erro de comunicação ao verificar produto: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Erro na criação da venda: {}", e.getMessage(), e);
            throw e;
        }
    }

    private ProdutoDto validarProdutoExiste(Long produtoId) throws IOException {
        ProdutoDto produto = produtoService.buscarProdutoPorId(produtoId);
        
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado: " + produtoId);
        }
        
        return produto;
    }

    private void validarEstoqueDisponivel(ProdutoDto produto) {
        if (produto.getEstoque() <= 0) {
            throw new RuntimeException("Produto sem estoque disponível. Estoque atual: " + produto.getEstoque());
        }
    }

    private Integer calcularQuantidadeVenda(Integer quantidadeSolicitada, Integer estoqueDisponivel) {
        return Math.min(quantidadeSolicitada, estoqueDisponivel);
    }

    private void realizarBaixaEstoque(Long produtoId, Integer quantidade) {
        try {
            BaixaEstoqueResponse baixaResponse = produtoService.realizarBaixaEstoque(produtoId, quantidade);
            logger.info("Baixa de estoque realizada com sucesso: {}", baixaResponse.getMessage());
        } catch (Exception e) {
            logger.error("Falha na baixa de estoque: {}", e.getMessage());
            throw new RuntimeException("Erro ao realizar baixa de estoque: " + e.getMessage());
        }
    }

    private void logResultadoVenda(Venda vendaOriginal, Integer quantidadeVendida, boolean vendaParcial, Venda vendaCriada) {
        logger.info("Venda criada com sucesso - ID: {}, Quantidade: {}, Status: {}", 
                   vendaCriada.getIdValue(), vendaCriada.getQuantidade(), vendaCriada.getStatus());
        
        if (vendaParcial) {
            logger.info("Venda parcial realizada - Solicitado: {}, Vendido: {}, Pendente: {}", 
                       vendaOriginal.getQuantidade(), quantidadeVendida, 
                       vendaOriginal.getQuantidade() - quantidadeVendida);
        }
    }

    public Venda update(Venda venda) {
        return this.repository.update(venda);
    }

    public Venda findById(Long id) {
        return this.repository.findById(id);
    }

    public List<Venda> findAll() {
        return this.repository.findAll();
    }

    public List<Venda> findByClienteId(Long clienteId) {
        return this.repository.findByClienteId(clienteId);
    }

    public Venda atualizarStatus(Long id, StatusVenda status) {
        Venda venda = this.repository.findById(id);
        
        if (venda == null) {
            throw new RuntimeException("Venda não encontrada");
        }

        Venda vendaAtualizada = venda.atualizarStatus(status);
        return this.repository.update(vendaAtualizada);
    }

    @Transactional
    public Venda cancelarVenda(Long id) {
        Venda venda = this.repository.findById(id);
        
        if (venda == null) {
            throw new RuntimeException("Venda não encontrada");
        }

        if (venda.getStatus() == StatusVenda.CANCELADA || venda.getStatus() == StatusVenda.ERRO) {
            throw new RuntimeException("Venda já foi cancelada ou está com erro");
        }

        try {
            Venda vendaCancelada = venda.atualizarStatus(StatusVenda.CANCELADA);
            return this.repository.update(vendaCancelada);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cancelar venda: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) {
        Venda venda = this.repository.findById(id);
        
        if (venda != null && venda.getStatus() == StatusVenda.SUCESSO) {
            cancelarVenda(id);
        }
        
        this.repository.delete(id);
    }

    @Transactional
    public Venda adicionarProdutoVenda(Long vendaId, Long produtoId, Integer quantidade) {
        Venda venda = this.repository.findById(vendaId);
        
        if (venda == null) {
            throw new RuntimeException("Venda não encontrada");
        }

        if (venda.getStatus() == StatusVenda.SUCESSO || venda.getStatus() == StatusVenda.CANCELADA) {
            throw new RuntimeException("Não é possível editar uma venda finalizada ou cancelada");
        }

        try {
            ProdutoDto produto = produtoService.buscarProdutoPorId(produtoId);
            
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado: " + produtoId);
            }
            
            if (produto.getEstoque() < quantidade) {
                throw new RuntimeException("Estoque insuficiente para o produto ID: " + produtoId + 
                    ". Disponível: " + produto.getEstoque() + ", Solicitado: " + quantidade);
            }

            Venda vendaAtualizada;
            if (venda.getProdutoId().equals(produtoId)) {
                int novaQuantidade = venda.getQuantidade() + quantidade;
                vendaAtualizada = new Venda(venda.getIdValue(), venda.getClienteId(), 
                                          produtoId, venda.getValor(), novaQuantidade, venda.getStatus());
            } else {
                vendaAtualizada = new Venda(venda.getIdValue(), venda.getClienteId(), 
                                          produtoId, venda.getValor(), quantidade, venda.getStatus());
            }

            return this.repository.update(vendaAtualizada);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao verificar estoque: " + e.getMessage(), e);
        }
    }

    @Transactional  
    public Venda editarVenda(Venda vendaAtualizada) {
        try {
            ProdutoDto produto = produtoService.buscarProdutoPorId(vendaAtualizada.getProdutoId());
            
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado: " + vendaAtualizada.getProdutoId());
            }
            
            if (produto.getEstoque() < vendaAtualizada.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto ID: " + vendaAtualizada.getProdutoId() + 
                    ". Disponível: " + produto.getEstoque() + ", Solicitado: " + vendaAtualizada.getQuantidade());
            }

            return this.repository.update(vendaAtualizada);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao verificar estoque: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Venda finalizarVendaParcial(Long vendaId) {
        Venda venda = this.repository.findById(vendaId);
        
        if (venda == null) {
            throw new RuntimeException("Venda não encontrada");
        }

        if (venda.getStatus() != StatusVenda.PENDENTE) {
            throw new RuntimeException("Apenas vendas pendentes podem ser finalizadas");
        }

        try {
            ProdutoDto produto = produtoService.buscarProdutoPorId(venda.getProdutoId());
            
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado: " + venda.getProdutoId());
            }

            if (produto.getEstoque() >= venda.getQuantidade()) {
                return finalizarVendaCompleta(venda);
            }
            
            if (produto.getEstoque() <= 0) {
                throw new RuntimeException("Sem estoque disponível para finalização parcial");
            }
            
            return finalizarVendaParcialComEstoqueDisponivel(venda, produto);

        } catch (IOException e) {
            throw new RuntimeException("Erro de comunicação com API externa: " + e.getMessage(), e);
        }
    }

    private Venda finalizarVendaCompleta(Venda venda) {
        try {
            BaixaEstoqueResponse response = produtoService.realizarBaixaEstoque(
                    venda.getProdutoId(), venda.getQuantidade());

            if (response.getMessage() != null && response.getMessage().contains("sucesso")) {
                Venda vendaFinalizada = venda.atualizarStatus(StatusVenda.SUCESSO);
                return this.repository.update(vendaFinalizada);
            }
            
            throw new RuntimeException("Falha na baixa do estoque");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao finalizar venda: " + e.getMessage());
        }
    }

    private Venda finalizarVendaParcialComEstoqueDisponivel(Venda venda, ProdutoDto produto) {
        try {
            BaixaEstoqueResponse response = produtoService.realizarBaixaEstoque(
                    venda.getProdutoId(), produto.getEstoque());

            if (response.getMessage() == null || !response.getMessage().contains("sucesso")) {
                throw new RuntimeException("Falha na baixa parcial do estoque");
            }

            Venda vendaParcial = new Venda(
                    venda.getIdValue(),
                    venda.getClienteId(),
                    venda.getProdutoId(),
                    venda.getValor(),
                    produto.getEstoque(),
                    StatusVenda.SUCESSO,
                    venda.getCreatedAt(),
                    venda.getUpdatedAt()
            );
            
            return this.repository.update(vendaParcial);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar baixa parcial: " + e.getMessage());
        }
    }

    public List<VendaConsolidadaDto> buscarVendasConsolidadas() {
        return this.repository.buscarVendasConsolidadas();
    }

    public List<VendaConsolidadaDto> buscarVendasConsolidadasPorCliente(Long clienteId) {
        return this.repository.buscarVendasConsolidadasPorCliente(clienteId);
    }

    public Integer verificarEstoqueDisponivel(Long produtoId) throws IOException {
        try {
            ProdutoDto produto = produtoService.buscarProdutoPorId(produtoId);
            
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado: " + produtoId);
            }
            
            return produto.getEstoque();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao consultar estoque do produto: " + produtoId, e);
        }
    }

    public Integer simularVenda(Long produtoId, Integer quantidadeSolicitada) throws IOException {
        Integer estoqueDisponivel = verificarEstoqueDisponivel(produtoId);
        return Math.min(quantidadeSolicitada, Math.max(0, estoqueDisponivel));
    }
}
