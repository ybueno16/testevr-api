package com.testevr.testejava.cliente.domain.service;

import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {
    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Cliente cliente = mock(Cliente.class);
        when(repository.create(cliente)).thenReturn(cliente);
        Cliente result = service.create(cliente);
        assertEquals(cliente, result);
        verify(repository).create(cliente);
    }

    @Test
    void testUpdate() {
        Cliente cliente = mock(Cliente.class);
        when(repository.update(cliente)).thenReturn(cliente);
        Cliente result = service.update(cliente);
        assertEquals(cliente, result);
        verify(repository).update(cliente);
    }

    @Test
    void testFindById() {
        Cliente cliente = mock(Cliente.class);
        when(repository.findById(1L)).thenReturn(cliente);
        Cliente result = service.findById(1L);
        assertEquals(cliente, result);
        verify(repository).findById(1L);
    }

    @Test
    void testFindAll() {
        Cliente cliente1 = mock(Cliente.class);
        Cliente cliente2 = mock(Cliente.class);
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(repository.findAll()).thenReturn(clientes);
        List<Cliente> result = service.findAll();
        assertEquals(clientes, result);
        verify(repository).findAll();
    }

    @Test
    void testDelete() {
        doNothing().when(repository).delete(1L);
        service.delete(1L);
        verify(repository).delete(1L);
    }
}
