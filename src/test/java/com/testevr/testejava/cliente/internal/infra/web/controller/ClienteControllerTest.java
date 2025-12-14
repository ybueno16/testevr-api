package com.testevr.testejava.cliente.internal.infra.web.controller;

import com.testevr.testejava.cliente.application.dto.ClienteDto;
import com.testevr.testejava.cliente.application.mapper.ClienteMapper;
import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.service.ClienteService;
import com.testevr.testejava.cliente.web.ClienteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {
    @Mock
    private ClienteService clienteService;
    @Mock
    private ClienteMapper mapper;
    @InjectMocks
    private ClienteController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarCliente() {
    ClienteDto dto = new ClienteDto(1L, "Nome", "Razao", "Fantasia", "69423022000160", true);
        Cliente entity = mock(Cliente.class);
        Cliente created = mock(Cliente.class);
    ClienteDto createdDto = new ClienteDto(2L, "Nome2", "Razao2", "Fantasia2", "69423022000160", true);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(clienteService.create(entity)).thenReturn(created);
        when(mapper.toDto(created)).thenReturn(createdDto);
        ResponseEntity<ClienteDto> response = controller.criarCliente(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDto, response.getBody());
    }

    @Test
    void testBuscarPorIdFound() {
        Cliente entity = mock(Cliente.class);
    ClienteDto dto = new ClienteDto(1L, "Nome", "Razao", "Fantasia", "69423022000160", true);
        when(clienteService.findById(1L)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);
        ResponseEntity<ClienteDto> response = controller.buscarPorId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testBuscarPorIdNotFound() {
        when(clienteService.findById(1L)).thenReturn(null);
        ResponseEntity<ClienteDto> response = controller.buscarPorId(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarTodos() {
        Cliente entity1 = mock(Cliente.class);
        Cliente entity2 = mock(Cliente.class);
    ClienteDto dto1 = new ClienteDto(1L, "Nome1", "Razao1", "Fantasia1", "69423022000160", true);
    ClienteDto dto2 = new ClienteDto(2L, "Nome2", "Razao2", "Fantasia2", "69423022000160", true);
        List<Cliente> entities = Arrays.asList(entity1, entity2);
        List<ClienteDto> dtos = Arrays.asList(dto1, dto2);
        when(clienteService.findAll()).thenReturn(entities);
        when(mapper.toDto(entity1)).thenReturn(dto1);
        when(mapper.toDto(entity2)).thenReturn(dto2);
        ResponseEntity<List<ClienteDto>> response = controller.buscarTodos();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos.size(), response.getBody().size());
    }

    @Test
    void testAtualizarClienteFound() {
    ClienteDto dto = new ClienteDto(1L, "Nome", "Razao", "Fantasia", "69423022000160", true);
        Cliente entity = mock(Cliente.class);
        Cliente updated = mock(Cliente.class);
    ClienteDto updatedDto = new ClienteDto(3L, "Nome3", "Razao3", "Fantasia3", "69423022000160", true);
        when(clienteService.findById(1L)).thenReturn(entity);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(entity.atualizarId(1L)).thenReturn(entity);
        when(clienteService.update(entity)).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(updatedDto);
        ResponseEntity<ClienteDto> response = controller.atualizarCliente(1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
    }

    @Test
    void testAtualizarClienteNotFound() {
    ClienteDto dto = new ClienteDto(1L, "Nome", "Razao", "Fantasia", "69423022000160", true);
        when(clienteService.findById(1L)).thenReturn(null);
        ResponseEntity<ClienteDto> response = controller.atualizarCliente(1L, dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeletar() {
        doNothing().when(clienteService).delete(1L);
        controller.deletar(1L);
        verify(clienteService).delete(1L);
    }
}
