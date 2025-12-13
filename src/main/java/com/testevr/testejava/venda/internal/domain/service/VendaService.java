package com.testevr.testejava.venda.internal.domain.service;

import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import com.testevr.testejava.venda.external.application.dto.ProdutoDto;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;
import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.repository.VendaRepository;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class VendaService {
    private final VendaRepository repository;
    private final ProdutoService produtoService;

    public VendaService(VendaRepository repository, ProdutoService produtoService) {
        this.repository = repository;
        this.produtoService = produtoService;
    }

    @Transactional
    public Venda create(Venda venda) throws Exception {
        try {
            // 1. Verificar se o produto existe
            ProdutoDto produto = produtoService.buscarProdutoPorId(venda.getProdutoId());
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado: " + venda.getProdutoId());
            }

            // 2. Criar venda com status PENDENTE inicialmente
            Venda vendaPendente = venda.atualizarStatus(StatusVenda.PENDENTE);
            return this.repository.create(vendaPendente);

        } catch (IOException e) {
            throw new RuntimeException("Erro de comunicação ao verificar produto: " + e.getMessage(), e);
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

        // Só pode cancelar vendas que estão com status SUCESSO ou PENDENTE
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
            // Se a venda foi bem-sucedida, considere cancelá-la primeiro para lidar com o estoque
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

        // Verificar se a venda pode ser editada
        if (venda.getStatus() == StatusVenda.SUCESSO || venda.getStatus() == StatusVenda.CANCELADA) {
            throw new RuntimeException("Não é possível editar uma venda finalizada ou cancelada");
        }

        try {
            // Verificar estoque do produto
            boolean estoqueDisponivel = produtoService.verificarEstoqueDisponivel(produtoId, quantidade);
            if (!estoqueDisponivel) {
                throw new RuntimeException("Estoque insuficiente para o produto ID: " + produtoId);
            }

            // Se o produto já existe na venda, somar as quantidades
            // Para simplificar, vamos criar uma nova venda com o produto adicional
            // Em uma implementação real, teríamos uma estrutura para múltiplos produtos por venda
            
            // Por enquanto, como temos apenas um produto por venda, vamos atualizar
            Venda vendaAtualizada;
            if (venda.getProdutoId().equals(produtoId)) {
                // Mesmo produto - somar quantidade
                int novaQuantidade = venda.getQuantidade() + quantidade;
                vendaAtualizada = new Venda(venda.getId().getValue(), venda.getClienteId(), 
                                          produtoId, venda.getValor(), novaQuantidade, venda.getStatus());
            } else {
                // Produto diferente - substituir (limitação atual do modelo)
                vendaAtualizada = new Venda(venda.getId().getValue(), venda.getClienteId(), 
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
            // Verificar estoque para a nova quantidade
            boolean estoqueDisponivel = produtoService.verificarEstoqueDisponivel(
                    vendaAtualizada.getProdutoId(), vendaAtualizada.getQuantidade());
            
            if (!estoqueDisponivel) {
                throw new RuntimeException("Estoque insuficiente para o produto ID: " + vendaAtualizada.getProdutoId());
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
            // Verificar estoque disponível
            boolean estoqueDisponivel = produtoService.verificarEstoqueDisponivel(
                    venda.getProdutoId(), venda.getQuantidade());

            if (estoqueDisponivel) {
                // Estoque completo disponível - finalizar normalmente
                BaixaEstoqueResponse response = produtoService.realizarBaixaEstoque(
                        venda.getProdutoId(), venda.getQuantidade());

                if (response.getMessage() != null && response.getMessage().contains("sucesso")) {
                    Venda vendaFinalizada = venda.atualizarStatus(StatusVenda.SUCESSO);
                    return this.repository.update(vendaFinalizada);
                } else {
                    throw new RuntimeException("Falha na baixa do estoque");
                }
            } else {
                // Estoque insuficiente - finalização parcial
                // Primeiro busca o produto para ver quanto estoque tem
                ProdutoDto produto = produtoService.buscarProdutoPorId(venda.getProdutoId());
                
                if (produto.getEstoque() > 0) {
                    // Fazer baixa parcial do que tem disponível
                    BaixaEstoqueResponse response = produtoService.realizarBaixaEstoque(
                            venda.getProdutoId(), produto.getEstoque());

                    if (response.getMessage() != null && response.getMessage().contains("sucesso")) {
                        // Atualizar quantidade da venda para refletir a quantidade finalizada
                        Venda vendaParcial = new Venda(
                                venda.getId().getValue(),
                                venda.getClienteId(),
                                venda.getProdutoId(),
                                venda.getValor(),
                                produto.getEstoque(), // Quantidade finalizada (parcial)
                                StatusVenda.SUCESSO,
                                venda.getCreatedAt(),
                                venda.getUpdatedAt()
                        );
                        return this.repository.update(vendaParcial);
                    } else {
                        throw new RuntimeException("Falha na baixa parcial do estoque");
                    }
                } else {
                    // Sem estoque disponível
                    throw new RuntimeException("Sem estoque disponível para finalização parcial");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro de comunicação com API externa: " + e.getMessage(), e);
        }
    }

    public List<VendaConsolidadaDto> buscarVendasConsolidadas() {
        return this.repository.buscarVendasConsolidadas();
    }

    public List<VendaConsolidadaDto> buscarVendasConsolidadasPorCliente(Long clienteId) {
        return this.repository.buscarVendasConsolidadasPorCliente(clienteId);
    }
}
