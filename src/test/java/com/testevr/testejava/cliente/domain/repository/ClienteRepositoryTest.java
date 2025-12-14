package com.testevr.testejava.cliente.domain.repository;

import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.valueobject.Cnpj;
import com.testevr.testejava.cliente.domain.valueobject.Nome;
import com.testevr.testejava.cliente.domain.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.domain.valueobject.RazaoSocial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteRepositoryTest {
    @Test
    void testCreateNull() {
        when(repository.create(null)).thenReturn(null);
        assertNull(repository.create(null));
        verify(repository).create(null);
    }

    @Test
    void testUpdateNull() {
        when(repository.update(null)).thenReturn(null);
        assertNull(repository.update(null));
        verify(repository).update(null);
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(999L)).thenReturn(null);
        assertNull(repository.findById(999L));
        verify(repository).findById(999L);
    }

    @Test
    void testFindAllEmpty() {
        when(repository.findAll()).thenReturn(java.util.Collections.emptyList());
        assertTrue(repository.findAll().isEmpty());
        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    void testDeleteMultiple() {
        doNothing().when(repository).delete(1L);
        doNothing().when(repository).delete(2L);
        repository.delete(1L);
        repository.delete(2L);
        verify(repository).delete(1L);
        verify(repository).delete(2L);
    }
    private ClienteRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ClienteRepository.class);
    }

    @Test
    void testCreate() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        when(repository.create(cliente)).thenReturn(cliente);
        Cliente result = repository.create(cliente);
        assertEquals(cliente, result);
        verify(repository).create(cliente);
    }

    @Test
    void testUpdate() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        when(repository.update(cliente)).thenReturn(cliente);
        Cliente result = repository.update(cliente);
        assertEquals(cliente, result);
        verify(repository).update(cliente);
    }

    @Test
    void testFindById() {
        Cliente cliente = new Cliente(1L, new Nome("Teste"), new RazaoSocial("Razao"), new NomeFantasia("Fantasia"), new Cnpj("69423022000160"));
        when(repository.findById(1L)).thenReturn(cliente);
        Cliente result = repository.findById(1L);
        assertEquals(cliente, result);
        verify(repository).findById(1L);
    }

    @Test
    void testFindAll() {
        Cliente cliente1 = new Cliente(1L, new Nome("Teste1"), new RazaoSocial("Razao1"), new NomeFantasia("Fantasia1"), new Cnpj("69423022000160"));
        Cliente cliente2 = new Cliente(2L, new Nome("Teste2"), new RazaoSocial("Razao2"), new NomeFantasia("Fantasia2"), new Cnpj("69423022000160"));
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(repository.findAll()).thenReturn(clientes);
        List<Cliente> result = repository.findAll();
        assertEquals(clientes, result);
        verify(repository).findAll();
    }

    @Test
    void testDelete() {
        doNothing().when(repository).delete(1L);
        repository.delete(1L);
        verify(repository).delete(1L);
    }
}
