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
}
