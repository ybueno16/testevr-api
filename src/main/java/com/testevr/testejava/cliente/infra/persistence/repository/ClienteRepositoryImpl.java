package com.testevr.testejava.cliente.infra.persistence.repository;

import com.testevr.testejava.cliente.application.mapper.ClienteMapper;
import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.repository.ClienteRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository implementation using pure JDBC (no ORM)
 * Direct usage of Connection, PreparedStatement, and ResultSet
 */
@Repository
public class ClienteRepositoryImpl implements ClienteRepository {

    private final DataSource dataSource;
    private final ClienteMapper mapper;

    public ClienteRepositoryImpl(DataSource dataSource, ClienteMapper mapper) {
        this.mapper = mapper;
        this.dataSource = dataSource;
    }

    @Override
    public Cliente create(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, razao_social, nome_fantasia, cnpj, ativo) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome().getValue());
            stmt.setString(2, cliente.getRazaoSocial().getValue());
            stmt.setString(3, cliente.getNomeFantasia().getValue());
            stmt.setString(4, cliente.getCnpj().getValue());
            stmt.setBoolean(5, cliente.isAtivo());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    return new Cliente(
                        id,
                        cliente.getNome(),
                        cliente.getRazaoSocial(),
                        cliente.getNomeFantasia(),
                        cliente.getCnpj(),
                        cliente.isAtivo()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar cliente", e);
        }

        throw new RuntimeException("Não foi possível criar o cliente");
    }

    public Cliente findAll(){
        String sql = "SELECT id, nome, razao_social, nome_fantasia, cnpj, ativo " +
                     "FROM cliente";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapper.mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes", e);
        }

        return null;
    }

    public Cliente findById(Long id) {
        String sql = "SELECT id, nome, razao_social, nome_fantasia, cnpj, ativo " +
                     "FROM cliente WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente", e);
        }

        return null;
    }

    public Cliente update(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, razao_social = ?, nome_fantasia = ?, " +
                     "cnpj = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome().getValue());
            stmt.setString(2, cliente.getRazaoSocial().getValue());
            stmt.setString(3, cliente.getNomeFantasia().getValue());
            stmt.setString(4, cliente.getCnpj().getValue());
            stmt.setBoolean(5, cliente.isAtivo());
            stmt.setLong(6, cliente.getId().getValue());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Cliente não encontrado para atualização");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
        return cliente;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Cliente não encontrado para exclusão");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente", e);
        }
    }


}
