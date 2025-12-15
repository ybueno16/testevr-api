package com.testevr.testejava.cliente.domain.service;

import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService (ClienteRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria um novo cliente no sistema.
     *
     * @param cliente Objeto Cliente contendo os dados a serem persistidos
     * @return Cliente O cliente criado com possíveis dados atualizados (ex: ID gerado)
     */
    public Cliente create(Cliente cliente) {
        return this.repository.create(cliente);
    }

    /**
     * Atualiza os dados de um cliente existente.
     *
     * @param cliente Objeto Cliente com os dados atualizados
     * @return Cliente O cliente atualizado
     */
    public Cliente update(Cliente cliente) {
        return this.repository.update(cliente);
    }

    /**
     * Busca um cliente pelo seu identificador único.
     *
     * @param id Identificador único do cliente
     * @return Cliente O cliente encontrado ou null caso não exista
     */
    public Cliente findById(Long id) {
        return this.repository.findById(id);
    }

    /**
     * Retorna uma lista com todos os clientes cadastrados no sistema.
     *
     * @return List<Cliente> Lista contendo todos os clientes
     */
    public List<Cliente> findAll() {
        return this.repository.findAll();
    }

    /**
     * Remove um cliente do sistema com base no seu identificador único.
     *
     * @param id Identificador único do cliente a ser removido
     */
    public void delete(Long id) {
        this.repository.delete(id);
    }
}