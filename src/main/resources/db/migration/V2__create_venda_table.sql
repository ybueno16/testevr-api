-- Create table for Venda entity
CREATE TABLE venda (
    id SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    quantidade INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT chk_valor_positivo CHECK (valor > 0),
    CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0),
    CONSTRAINT chk_status CHECK (status IN ('PENDENTE', 'CONCLUIDA', 'SUCESSO', 'ERRO', 'CANCELADA'))
);

-- Create indexes for better performance
CREATE INDEX idx_venda_cliente_id ON venda(cliente_id);
CREATE INDEX idx_venda_produto_id ON venda(produto_id);
CREATE INDEX idx_venda_status ON venda(status);
CREATE INDEX idx_venda_created_at ON venda(created_at);
