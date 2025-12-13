package com.testevr.testejava.venda.internal.infra.persistence.repository;

import com.testevr.testejava.venda.internal.domain.entity.Venda;
import com.testevr.testejava.venda.internal.domain.repository.VendaRepository;
import com.testevr.testejava.venda.internal.domain.valueobject.StatusVenda;
import com.testevr.testejava.venda.internal.domain.valueobject.ValorVenda;
import com.testevr.testejava.venda.internal.application.dto.VendaConsolidadaDto;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation using pure JDBC (no ORM)
 * Direct usage of Connection, PreparedStatement, and ResultSet
 */
@Repository
public class VendaRepositoryImpl implements VendaRepository {

    private final DataSource dataSource;

    public VendaRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Venda create(Venda venda) {
        String sql = "INSERT INTO venda (cliente_id, produto_id, valor, quantidade, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, venda.getClienteId());
            stmt.setLong(2, venda.getProdutoId());
            stmt.setBigDecimal(3, venda.getValor().getValor());
            stmt.setInt(4, venda.getQuantidade());
            stmt.setString(5, venda.getStatus().name());
            stmt.setTimestamp(6, Timestamp.valueOf(venda.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.valueOf(venda.getUpdatedAt()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    return new Venda(
                        id,
                        venda.getClienteId(),
                        venda.getProdutoId(),
                        venda.getValor(),
                        venda.getQuantidade(),
                        venda.getStatus(),
                        venda.getCreatedAt(),
                        venda.getUpdatedAt()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar venda: " + e.getMessage() + " (Código: " + e.getSQLState() + ")", e);
        }

        throw new RuntimeException("Erro ao criar venda");
    }

    @Override
    public Venda update(Venda venda) {
        String sql = "UPDATE venda SET cliente_id = ?, produto_id = ?, valor = ?, " +
                     "quantidade = ?, status = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, venda.getClienteId());
            stmt.setLong(2, venda.getProdutoId());
            stmt.setBigDecimal(3, venda.getValor().getValor());
            stmt.setInt(4, venda.getQuantidade());
            stmt.setString(5, venda.getStatus().name());
            stmt.setTimestamp(6, Timestamp.valueOf(venda.getUpdatedAt()));
            stmt.setLong(7, venda.getIdValue());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return venda;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar venda", e);
        }

        throw new RuntimeException("Venda não encontrada para atualização");
    }

    @Override
    public Venda findById(Long id) {
        String sql = "SELECT * FROM venda WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVenda(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar venda por ID", e);
        }

        return null;
    }

    @Override
    public List<Venda> findAll() {
        String sql = "SELECT * FROM venda ORDER BY created_at DESC";
        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vendas.add(mapRowToVenda(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as vendas", e);
        }

        return vendas;
    }

    @Override
    public List<Venda> findByClienteId(Long clienteId) {
        String sql = "SELECT * FROM venda WHERE cliente_id = ? ORDER BY created_at DESC";
        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vendas.add(mapRowToVenda(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vendas por cliente", e);
        }

        return vendas;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM venda WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar venda", e);
        }
    }

    @Override
    public List<VendaConsolidadaDto> buscarVendasConsolidadas() {
        String sql = "SELECT cliente_id, status, SUM(valor * quantidade) as valor_total, COUNT(*) as quantidade_vendas " +
                     "FROM venda " +
                     "GROUP BY cliente_id, status " +
                     "ORDER BY cliente_id, status";
        
        List<VendaConsolidadaDto> vendas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Long clienteId = rs.getLong("cliente_id");
                String status = rs.getString("status");
                java.math.BigDecimal valorTotal = rs.getBigDecimal("valor_total");
                Integer quantidadeVendas = rs.getInt("quantidade_vendas");

                vendas.add(new VendaConsolidadaDto(clienteId, status, valorTotal, quantidadeVendas));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vendas consolidadas", e);
        }

        return vendas;
    }

    @Override
    public List<VendaConsolidadaDto> buscarVendasConsolidadasPorCliente(Long clienteId) {
        String sql = "SELECT cliente_id, status, SUM(valor * quantidade) as valor_total, COUNT(*) as quantidade_vendas " +
                     "FROM venda " +
                     "WHERE cliente_id = ? " +
                     "GROUP BY cliente_id, status " +
                     "ORDER BY status";
        
        List<VendaConsolidadaDto> vendas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long clienteIdResult = rs.getLong("cliente_id");
                    String status = rs.getString("status");
                    java.math.BigDecimal valorTotal = rs.getBigDecimal("valor_total");
                    Integer quantidadeVendas = rs.getInt("quantidade_vendas");

                    vendas.add(new VendaConsolidadaDto(clienteIdResult, status, valorTotal, quantidadeVendas));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vendas consolidadas por cliente", e);
        }

        return vendas;
    }

    private Venda mapRowToVenda(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long clienteId = rs.getLong("cliente_id");
        Long produtoId = rs.getLong("produto_id");
        java.math.BigDecimal valor = rs.getBigDecimal("valor");
        Integer quantidade = rs.getInt("quantidade");
        String status = rs.getString("status");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

        ValorVenda valorVenda = new ValorVenda(valor);
        StatusVenda statusVenda = StatusVenda.valueOf(status);

        return new Venda(id, clienteId, produtoId, valorVenda, quantidade,
                        statusVenda, createdAt, updatedAt);
    }
}
