package com.testevr.testejava.cliente.infra.persistence.repository;

import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.repository.ClienteRepository;
import com.testevr.testejava.cliente.domain.valueobject.Nome;
import com.testevr.testejava.cliente.domain.valueobject.RazaoSocial;
import com.testevr.testejava.cliente.domain.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.domain.valueobject.Cnpj;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository {

    private final DataSource dataSource;

    public ClienteRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Cria um novo registro de cliente no banco de dados.
     * Utiliza a funcionalidade RETURNING do PostgreSQL para obter o ID gerado.
     *
     * @param cliente Objeto Cliente contendo os dados a serem persistidos
     * @return Cliente O cliente criado com o ID gerado pelo banco de dados
     * @throws RuntimeException Se ocorrer um erro SQL ou se o ID não for retornado
     */
    @Override
    public Cliente create(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, razao_social, nome_fantasia, cnpj, ativo, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome().getValue());
            stmt.setString(2, cliente.getRazaoSocial().getValue());
            stmt.setString(3, cliente.getNomeFantasia().getValue());
            stmt.setString(4, cliente.getCnpj().getValue());
            stmt.setBoolean(5, cliente.isAtivo());
            stmt.setTimestamp(6, Timestamp.valueOf(cliente.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.valueOf(cliente.getUpdatedAt()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    return new Cliente(
                            id,
                            cliente.getNome(),
                            cliente.getRazaoSocial(),
                            cliente.getNomeFantasia(),
                            cliente.getCnpj(),
                            cliente.isAtivo(),
                            cliente.getCreatedAt(),
                            cliente.getUpdatedAt()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar cliente", e);
        }

        throw new RuntimeException("Erro ao criar cliente");
    }

    /**
     * Atualiza um registro de cliente existente no banco de dados.
     *
     * @param cliente Objeto Cliente com os dados atualizados
     * @return Cliente O cliente atualizado
     * @throws RuntimeException Se ocorrer um erro SQL ou se o cliente não for encontrado
     */
    @Override
    public Cliente update(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, razao_social = ?, nome_fantasia = ?, " +
                "cnpj = ?, ativo = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome().getValue());
            stmt.setString(2, cliente.getRazaoSocial().getValue());
            stmt.setString(3, cliente.getNomeFantasia().getValue());
            stmt.setString(4, cliente.getCnpj().getValue());
            stmt.setBoolean(5, cliente.isAtivo());
            stmt.setTimestamp(6, Timestamp.valueOf(cliente.getUpdatedAt()));
            stmt.setLong(7, cliente.getId().getValue());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return cliente;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }

        throw new RuntimeException("Cliente não encontrado para atualização");
    }

    /**
     * Busca um cliente pelo seu identificador único.
     *
     * @param id Identificador único do cliente
     * @return Cliente O cliente encontrado ou null caso não exista
     * @throws RuntimeException Se ocorrer um erro SQL durante a consulta
     */
    @Override
    public Cliente findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCliente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }

        return null;
    }

    /**
     * Retorna todos os clientes cadastrados no banco de dados, ordenados por ID.
     *
     * @return List<Cliente> Lista contendo todos os clientes
     * @throws RuntimeException Se ocorrer um erro SQL durante a consulta
     */
    @Override
    public List<Cliente> findAll() {
        String sql = "SELECT * FROM cliente ORDER BY id";

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = mapRowToCliente(rs);
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os clientes", e);
        }

        return clientes;
    }

    /**
     * Remove um cliente do banco de dados com base no seu identificador único.
     *
     * @param id Identificador único do cliente a ser removido
     * @throws RuntimeException Se ocorrer um erro SQL durante a exclusão
     */
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente", e);
        }
    }

    /**
     * Método auxiliar para mapear uma linha do ResultSet para um objeto Cliente.
     * Converte os dados do banco de dados em objetos de valor (Value Objects) e cria
     * uma instância de Cliente.
     *
     * @param rs ResultSet posicionado na linha atual a ser mapeada
     * @return Cliente Objeto Cliente populado com os dados do ResultSet
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet
     */
    private Cliente mapRowToCliente(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String razaoSocial = rs.getString("razao_social");
        String nomeFantasia = rs.getString("nome_fantasia");
        String cnpj = rs.getString("cnpj");
        Boolean ativo = rs.getBoolean("ativo");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

        Nome nomeVO = new Nome(nome);
        RazaoSocial razaoSocialVO = new RazaoSocial(razaoSocial);
        NomeFantasia nomeFantasiaVO = new NomeFantasia(nomeFantasia);
        Cnpj cnpjVO = new Cnpj(cnpj);

        return new Cliente(id, nomeVO, razaoSocialVO, nomeFantasiaVO, cnpjVO, ativo, createdAt, updatedAt);
    }
}