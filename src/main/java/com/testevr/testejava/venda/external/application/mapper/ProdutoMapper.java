package com.testevr.testejava.venda.external.application.mapper;

import com.testevr.testejava.venda.external.application.dto.ProdutoDto;
import com.testevr.testejava.venda.external.domain.valueobject.Produto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Produto(
            dto.getId(),
            dto.getDescricao(),
            dto.getEstoque(),
            dto.getPreco() != null ? BigDecimal.valueOf(dto.getPreco()) : null,
            dto.getUnidade(),
            dto.getUltimaAtualizacao()
        );
    }

    public ProdutoDto toDto(Produto produto) {
        if (produto == null) {
            return null;
        }

        return new ProdutoDto(
            produto.getId(),
            produto.getDescricao(),
            produto.getEstoque(),
            produto.getPreco() != null ? produto.getPreco().doubleValue() : null,
            produto.getUnidade(),
            produto.getUltimaAtualizacao()
        );
    }
}
