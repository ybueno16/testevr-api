package com.testevr.testejava.venda.internal.domain.service;

import com.testevr.testejava.venda.external.application.dto.BaixaEstoqueResponse;
import com.testevr.testejava.venda.external.domain.service.ProdutoService;
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
    public Venda create(Venda venda) {
        try {
            // 1. Verificar se há estoque suficiente
            boolean estoqueDisponivel = produtoService.verificarEstoqueDisponivel(
                    venda.getProdutoId(),
                    venda.getQuantidade()
            );

            if (!estoqueDisponivel) {
                // Criar venda com status ERRO se não há estoque
                Venda vendaComErro = venda.atualizarStatus(StatusVenda.ERRO);
                return this.repository.create(vendaComErro);
            }

            // 2. Realizar baixa no estoque
            BaixaEstoqueResponse response = produtoService.realizarBaixaEstoque(
                    venda.getProdutoId(),
                    venda.getQuantidade()
            );

            // 3. Verificar se a baixa foi bem-sucedida
            if (response.getMessage() != null && response.getMessage().contains("sucesso")) {
                // Sucesso: criar venda com status SUCESSO
                Venda vendaComSucesso = venda.atualizarStatus(StatusVenda.SUCESSO);
                return this.repository.create(vendaComSucesso);
            } else {
                // Erro na baixa: criar venda com status ERRO
                Venda vendaComErro = venda.atualizarStatus(StatusVenda.ERRO);
                return this.repository.create(vendaComErro);
            }

        } catch (IOException e) {
            // Erro de comunicação: criar venda com status ERRO
            Venda vendaComErro = venda.atualizarStatus(StatusVenda.ERRO);
            return this.repository.create(vendaComErro);
        } catch (RuntimeException e) {
            // Outros erros: criar venda com status ERRO
            Venda vendaComErro = venda.atualizarStatus(StatusVenda.ERRO);
            return this.repository.create(vendaComErro);
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
            // Se a venda tinha status SUCESSO, tentar restaurar o estoque
            if (venda.getStatus() == StatusVenda.SUCESSO) {
                // Aqui você implementaria a lógica de restauração de estoque
                // Por enquanto, vamos apenas marcar como cancelada
                // TODO: Implementar método de restauração no ProdutoService se necessário
            }

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
}
