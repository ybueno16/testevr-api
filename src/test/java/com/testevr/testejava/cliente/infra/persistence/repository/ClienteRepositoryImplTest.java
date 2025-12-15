package com.testevr.testejava.cliente.infra.persistence.repository;

import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.valueobject.Nome;
import com.testevr.testejava.cliente.domain.valueobject.RazaoSocial;
import com.testevr.testejava.cliente.domain.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.domain.valueobject.Cnpj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryImplTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private ClienteRepositoryImpl repository;

    private Cliente cliente;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        repository = new ClienteRepositoryImpl(dataSource);
        now = LocalDateTime.now();

        cliente = new Cliente(
                1L,
                new Nome("Cliente Teste"),
                new RazaoSocial("Razão Social Teste"),
                new NomeFantasia("Nome Fantasia Teste"),
                new Cnpj("12345678000195"),
                true,
                now,
                now
        );
    }

    @Test
    void create_shouldReturnClienteWithId_whenInsertIsSuccessful() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);

        Cliente clienteWithoutId = new Cliente(
                null,
                new Nome("Cliente Teste"),
                new RazaoSocial("Razão Social Teste"),
                new NomeFantasia("Nome Fantasia Teste"),
                new Cnpj("12345678000195"),
                true,
                now,
                now
        );

        Cliente result = repository.create(clienteWithoutId);

        assertNotNull(result);
        assertEquals(1L, result.getId().getValue());

        verify(preparedStatement).setString(1, "Cliente Teste");
        verify(preparedStatement).setString(2, "Razão Social Teste");
        verify(preparedStatement).setString(3, "Nome Fantasia Teste");
        verify(preparedStatement).setString(4, "12345678000195");
        verify(preparedStatement).setBoolean(5, true);
        verify(preparedStatement).setTimestamp(eq(6), any(Timestamp.class));
        verify(preparedStatement).setTimestamp(eq(7), any(Timestamp.class));
        verify(preparedStatement).executeQuery();
    }

    @Test
    void create_shouldThrowRuntimeException_whenNoResultSet() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Cliente clienteWithoutId = new Cliente(
                null,
                new Nome("Cliente Teste"),
                new RazaoSocial("Razão Social Teste"),
                new NomeFantasia("Nome Fantasia Teste"),
                new Cnpj("12345678000195"),
                true,
                now,
                now
        );

        assertThrows(RuntimeException.class, () -> repository.create(clienteWithoutId));
    }

    @Test
    void create_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        Cliente clienteWithoutId = new Cliente(
                null,
                new Nome("Cliente Teste"),
                new RazaoSocial("Razão Social Teste"),
                new NomeFantasia("Nome Fantasia Teste"),
                new Cnpj("12345678000195"),
                true,
                now,
                now
        );

        assertThrows(RuntimeException.class, () -> repository.create(clienteWithoutId));
    }

    @Test
    void update_shouldReturnUpdatedCliente_whenUpdateIsSuccessful() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Cliente result = repository.update(cliente);

        assertNotNull(result);
        assertEquals(cliente, result);

        verify(preparedStatement).setString(1, "Cliente Teste");
        verify(preparedStatement).setString(2, "Razão Social Teste");
        verify(preparedStatement).setString(3, "Nome Fantasia Teste");
        verify(preparedStatement).setString(4, "12345678000195");
        verify(preparedStatement).setBoolean(5, true);
        verify(preparedStatement).setTimestamp(eq(6), any(Timestamp.class));
        verify(preparedStatement).setLong(7, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void update_shouldThrowRuntimeException_whenNoRowsAffected() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        assertThrows(RuntimeException.class, () -> repository.update(cliente));
    }

    @Test
    void update_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        assertThrows(RuntimeException.class, () -> repository.update(cliente));
    }

    @Test
    void findById_shouldReturnCliente_whenFound() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Cliente Teste");
        when(resultSet.getString("razao_social")).thenReturn("Razão Social Teste");
        when(resultSet.getString("nome_fantasia")).thenReturn("Nome Fantasia Teste");
        when(resultSet.getString("cnpj")).thenReturn("12345678000195");
        when(resultSet.getBoolean("ativo")).thenReturn(true);
        when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(now));
        when(resultSet.getTimestamp("updated_at")).thenReturn(Timestamp.valueOf(now));

        Cliente result = repository.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId().getValue());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void findById_shouldReturnNull_whenNotFound() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Cliente result = repository.findById(1L);

        assertNull(result);
    }

    @Test
    void findById_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        assertThrows(RuntimeException.class, () -> repository.findById(1L));
    }

    @Test
    void findAll_shouldReturnListOfClientes() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Cliente Teste");
        when(resultSet.getString("razao_social")).thenReturn("Razão Social Teste");
        when(resultSet.getString("nome_fantasia")).thenReturn("Nome Fantasia Teste");
        when(resultSet.getString("cnpj")).thenReturn("12345678000195");
        when(resultSet.getBoolean("ativo")).thenReturn(true);
        when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(now));
        when(resultSet.getTimestamp("updated_at")).thenReturn(Timestamp.valueOf(now));

        List<Cliente> result = repository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(preparedStatement).executeQuery();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoClientes() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Cliente> result = repository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        assertThrows(RuntimeException.class, () -> repository.findAll());
    }

    @Test
    void delete_shouldExecuteDeleteSuccessfully() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        repository.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void delete_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        assertThrows(RuntimeException.class, () -> repository.delete(1L));
    }
}