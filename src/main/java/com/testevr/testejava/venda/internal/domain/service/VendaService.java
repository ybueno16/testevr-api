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

    /**
     * Cria uma nova venda, realizando validações de produto e estoque.
     * Este método é transacional e integra com a API externa para verificar
     * disponibilidade e realizar baixa de estoque.
     *
     * @param venda Objeto Venda contendo os dados da venda a ser criada
     * @return Venda A venda criada com status apropriado
     * @throws Exception Se ocorrer erro na validação, comunicação com API externa ou persistência
     */
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

    /**
     * Valida se o produto existe na API externa.
     *
     * @param produtoId Identificador único do produto
     * @return ProdutoDto DTO contendo os dados do produto
     * @throws IOException Se ocorrer erro na comunicação com a API externa
     * @throws RuntimeException Se o produto não for encontrado
     */
    private ProdutoDto validarProdutoExiste(Long produtoId) throws IOException {
        ProdutoDto produto = produtoService.buscarProdutoPorId(produtoId);

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado: " + produtoId);
        }

        return produto;
    }

    /**
     * Valida se há estoque disponível para o produto.
     *
     * @param produto DTO contendo os dados do produto
     * @throws RuntimeException Se o estoque estiver esgotado (<= 0)
     */
    private void validarEstoqueDisponivel(ProdutoDto produto) {
        if (produto.getEstoque() <= 0) {
            throw new RuntimeException("Produto sem estoque disponível. Estoque atual: " + produto.getEstoque());
        }
    }

    /**
     * Calcula a quantidade que pode ser vendida com base no estoque disponível.
     *
     * @param quantidadeSolicitada Quantidade desejada pelo cliente
     * @param estoqueDisponivel Quantidade disponível em estoque
     * @return Integer Quantidade que pode ser efetivamente vendida
     */
    private Integer calcularQuantidadeVenda(Integer quantidadeSolicitada, Integer estoqueDisponivel) {
        return Math.min(quantidadeSolicitada, estoqueDisponivel);
    }

    /**
     * Realiza a baixa de estoque na API externa.
     *
     * @param produtoId Identificador único do produto
     * @param quantidade Quantidade a ser baixada do estoque
     * @throws RuntimeException Se ocorrer erro na baixa de estoque
     */
    private void realizarBaixaEstoque(Long produtoId, Integer quantidade) {
        try {
            BaixaEstoqueResponse baixaResponse = produtoService.realizarBaixaEstoque(produtoId, quantidade);
            logger.info("Baixa de estoque realizada com sucesso: {}", baixaResponse.getMessage());
        } catch (Exception e) {
            logger.error("Falha na baixa de estoque: {}", e.getMessage());
            throw new RuntimeException("Erro ao realizar baixa de estoque: " + e.getMessage());
        }
    }

    /**
     * Registra o resultado da venda no log.
     *
     * @param vendaOriginal Venda original solicitada
     * @param quantidadeVendida Quantidade efetivamente vendida
     * @param vendaParcial Indica se foi uma venda parcial
     * @param vendaCriada Venda criada e persistida
     */
    private void logResultadoVenda(Venda vendaOriginal, Integer quantidadeVendida, boolean vendaParcial, Venda vendaCriada) {
        logger.info("Venda criada com sucesso - ID: {}, Quantidade: {}, Status: {}",
                vendaCriada.getIdValue(), vendaCriada.getQuantidade(), vendaCriada.getStatus());

        if (vendaParcial) {
            logger.info("Venda parcial realizada - Solicitado: {}, Vendido: {}, Pendente: {}",
                    vendaOriginal.getQuantidade(), quantidadeVendida,
                    vendaOriginal.getQuantidade() - quantidadeVendida);
        }
    }

    /**
     * Atualiza os dados de uma venda existente.
     *
     * @param venda Objeto Venda com os dados atualizados
     * @return Venda A venda atualizada
     */
    public Venda update(Venda venda) {
        return this.repository.update(venda);
    }

    /**
     * Busca uma venda pelo seu identificador único.
     *
     * @param id Identificador único da venda
     * @return Venda A venda encontrada ou null se não existir
     */
    public Venda findById(Long id) {
        return this.repository.findById(id);
    }

    /**
     * Retorna todas as vendas cadastradas no sistema.
     *
     * @return List<Venda> Lista contendo todas as vendas
     */
    public List<Venda> findAll() {
        return this.repository.findAll();
    }

    /**
     * Busca vendas pelo identificador do cliente.
     *
     * @param clienteId Identificador único do cliente
     * @return List<Venda> Lista de vendas do cliente especificado
     */
    public List<Venda> findByClienteId(Long clienteId) {
        return this.repository.findByClienteId(clienteId);
    }

    /**
     * Atualiza o status de uma venda específica.
     *
     * @param id Identificador único da venda
     * @param status Novo status a ser atribuído à venda
     * @return Venda A venda com o status atualizado
     * @throws RuntimeException Se a venda não for encontrada
     */
    public Venda atualizarStatus(Long id, StatusVenda status) {
        Venda venda = this.repository.findById(id);

        if (venda == null) {
            throw new RuntimeException("Venda não encontrada");
        }

        Venda vendaAtualizada = venda.atualizarStatus(status);
        return this.repository.update(vendaAtualizada);
    }

    /**
     * Cancela uma venda específica.
     * Este método é transacional e valida se a venda pode ser cancelada.
     *
     * @param id Identificador único da venda a ser cancelada
     * @return Venda A venda cancelada
     * @throws RuntimeException Se a venda não for encontrada, já estiver cancelada ou com erro
     */
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

    /**
     * Remove uma venda do sistema.
     * Se a venda estiver com status de sucesso, primeiro a cancela.
     * Este método é transacional.
     *
     * @param id Identificador único da venda a ser removida
     */
    @Transactional
    public void delete(Long id) {
        Venda venda = this.repository.findById(id);

        if (venda != null && venda.getStatus() == StatusVenda.SUCESSO) {
            cancelarVenda(id);
        }

        this.repository.delete(id);
    }

    /**
     * Adiciona um produto a uma venda existente.
     * Este método é transacional e valida estoque disponível.
     *
     * @param vendaId Identificador único da venda
     * @param produtoId Identificador único do produto a ser adicionado
     * @param quantidade Quantidade do produto a ser adicionada
     * @return Venda A venda atualizada
     * @throws RuntimeException Se a venda não for encontrada, estiver finalizada/cancelada,
     *                          produto não existir ou estoque for insuficiente
     */
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

    /**
     * Edita os dados de uma venda existente.
     * Este método é transacional e valida estoque disponível.
     *
     * @param vendaAtualizada Objeto Venda com os dados atualizados
     * @return Venda A venda atualizada
     * @throws RuntimeException Se o produto não existir ou estoque for insuficiente
     */
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

    /**
     * Finaliza uma venda parcial (pendente).
     * Este método é transacional e tenta completar a venda com o estoque disponível.
     *
     * @param vendaId Identificador único da venda pendente
     * @return Venda A venda finalizada (completa ou parcial)
     * @throws RuntimeException Se a venda não for encontrada, não estiver pendente,
     *                          produto não existir ou não houver estoque disponível
     */
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

    /**
     * Finaliza uma venda completa (quantidade total disponível).
     *
     * @param venda Venda a ser finalizada
     * @return Venda A venda finalizada com status de sucesso
     * @throws RuntimeException Se a baixa de estoque falhar
     */
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

    /**
     * Finaliza uma venda parcial com o estoque disponível.
     *
     * @param venda Venda original pendente
     * @param produto Dados do produto com estoque disponível
     * @return Venda A venda parcial finalizada com status de sucesso
     * @throws RuntimeException Se a baixa de estoque falhar
     */
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

    /**
     * Busca vendas consolidadas com informações agregadas.
     *
     * @return List<VendaConsolidadaDto> Lista de vendas consolidadas
     */
    public List<VendaConsolidadaDto> buscarVendasConsolidadas() {
        return this.repository.buscarVendasConsolidadas();
    }

    /**
     * Busca vendas consolidadas para um cliente específico.
     *
     * @param clienteId Identificador único do cliente
     * @return List<VendaConsolidadaDto> Lista de vendas consolidadas do cliente
     */
    public List<VendaConsolidadaDto> buscarVendasConsolidadasPorCliente(Long clienteId) {
        return this.repository.buscarVendasConsolidadasPorCliente(clienteId);
    }

    /**
     * Verifica o estoque disponível para um produto específico.
     *
     * @param produtoId Identificador único do produto
     * @return Integer Quantidade disponível em estoque
     * @throws IOException Se ocorrer erro na comunicação com a API externa
     * @throws RuntimeException Se o produto não for encontrado
     */
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

    /**
     * Simula uma venda para verificar quantas unidades podem ser vendidas.
     *
     * @param produtoId Identificador único do produto
     * @param quantidadeSolicitada Quantidade desejada para venda
     * @return Integer Quantidade que pode ser efetivamente vendida
     * @throws IOException Se ocorrer erro na comunicação com a API externa
     */
    public Integer simularVenda(Long produtoId, Integer quantidadeSolicitada) throws IOException {
        Integer estoqueDisponivel = verificarEstoqueDisponivel(produtoId);
        return Math.min(quantidadeSolicitada, Math.max(0, estoqueDisponivel));
    }
}