package com.testevr.testejava.cliente.application.mapper;

import com.testevr.testejava.cliente.application.dto.ClienteDto;
import com.testevr.testejava.cliente.domain.entity.Cliente;
import com.testevr.testejava.cliente.domain.valueobject.Cnpj;
import com.testevr.testejava.cliente.domain.valueobject.Nome;
import com.testevr.testejava.cliente.domain.valueobject.NomeFantasia;
import com.testevr.testejava.cliente.domain.valueobject.RazaoSocial;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteDto dto) {
        if (dto == null) return null;

        if (dto.getId() != null) {
            // Atualização - com ID
            return new Cliente(
                    dto.getId(),
                    new Nome(dto.getNome()),
                    new RazaoSocial(dto.getRazaoSocial()),
                    new NomeFantasia(dto.getNomeFantasia()),
                    new Cnpj(dto.getCnpj()),
                    dto.getAtivo() != null ? dto.getAtivo() : true,
                    dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now(),
                    dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now()
            );
        } else {
            // Criação - sem ID
            return new Cliente(
                    null,
                    new Nome(dto.getNome()),
                    new RazaoSocial(dto.getRazaoSocial()),
                    new NomeFantasia(dto.getNomeFantasia()),
                    new Cnpj(dto.getCnpj())
            );
        }
    }

    public ClienteDto toDto(Cliente entity) {
        if (entity == null) return null;

        return new ClienteDto(
                entity.getId() != null ? entity.getId().getValue() : null,
                entity.getNome().getValue(),
                entity.getRazaoSocial().getValue(),
                entity.getNomeFantasia().getValue(),
                entity.getCnpj().getValue(),
                entity.isAtivo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    public Cliente mapRow(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String razaoSocial = rs.getString("razao_social");
        String nomeFantasia = rs.getString("nome_fantasia");
        String cnpj = rs.getString("cnpj");
        boolean ativo = rs.getBoolean("ativo");

        return new Cliente(
                id,
                new Nome(nome),
                new RazaoSocial(razaoSocial),
                new NomeFantasia(nomeFantasia),
                new Cnpj(cnpj),
                ativo
        );
    }


}
