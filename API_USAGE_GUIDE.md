# Guia de Uso das APIs - Sistema de Vendas

## Base URL
Todas as rotas da API começam com: `http://localhost:8080/api/v1`

## 1. API de Vendas

### 1.1 Criar uma Venda
**Endpoint:** `POST /api/v1/vendas`

**Request Body:**
```json
{
  "clienteId": 1,
  "produtoId": 10,
  "valor": 150.50,
  "quantidade": 2
}
```

**Response (201 Created):**
```json
{
  "id": 123,
  "clienteId": 1,
  "produtoId": 10,
  "valor": 150.50,
  "quantidade": 2,
  "status": "PENDENTE"
}
```

**Exemplo cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "produtoId": 10,
    "valor": 150.50,
    "quantidade": 2
  }'
```

### 1.2 Buscar Venda por ID
**Endpoint:** `GET /api/v1/vendas/{id}`

**Response (200 OK):**
```json
{
  "id": 123,
  "clienteId": 1,
  "produtoId": 10,
  "valor": 150.50,
  "quantidade": 2,
  "status": "SUCESSO"
}
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/v1/vendas/123
```

### 1.3 Listar Todas as Vendas
**Endpoint:** `GET /api/v1/vendas`

**Response (200 OK):**
```json
[
  {
    "id": 123,
    "clienteId": 1,
    "produtoId": 10,
    "valor": 150.50,
    "quantidade": 2,
    "status": "SUCESSO"
  },
  {
    "id": 124,
    "clienteId": 2,
    "produtoId": 15,
    "valor": 75.30,
    "quantidade": 1,
    "status": "PENDENTE"
  }
]
```

### 1.4 Buscar Vendas por Cliente
**Endpoint:** `GET /api/v1/vendas/cliente/{clienteId}`

**Response (200 OK):** Array de vendas do cliente específico

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/v1/vendas/cliente/1
```

### 1.5 Visualização Consolidada
**Endpoint:** `GET /api/v1/vendas/consolidada`

**Response (200 OK):**
```json
[
  {
    "clienteId": 1,
    "status": "SUCESSO",
    "valorTotal": 450.50,
    "quantidadeVendas": 3
  },
  {
    "clienteId": 1,
    "status": "PENDENTE",
    "valorTotal": 150.00,
    "quantidadeVendas": 1
  },
  {
    "clienteId": 2,
    "status": "SUCESSO",
    "valorTotal": 300.75,
    "quantidadeVendas": 2
  }
]
```

### 1.6 Consolidada por Cliente
**Endpoint:** `GET /api/v1/vendas/consolidada/cliente/{clienteId}`

**Response (200 OK):** Array consolidado apenas do cliente específico

### 1.7 Adicionar Produto à Venda Existente
**Endpoint:** `POST /api/v1/vendas/{id}/produtos`

**Request Body:**
```json
{
  "produtoId": 15,
  "quantidade": 3
}
```

**Comportamento:**
- Se o produto já existe na venda, soma as quantidades
- Se é um produto diferente, substitui (limitação atual do modelo)

**Response (200 OK):** Venda atualizada

**Exemplo cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/vendas/123/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "produtoId": 15,
    "quantidade": 3
  }'
```

### 1.8 Editar Venda
**Endpoint:** `PUT /api/v1/vendas/{id}`

**Request Body:**
```json
{
  "clienteId": 1,
  "produtoId": 20,
  "valor": 200.00,
  "quantidade": 5
}
```

**Restrições:**
- Só permite editar vendas com status PENDENTE ou ERRO
- Não permite editar vendas SUCESSO ou CANCELADA

### 1.9 Atualizar Status da Venda
**Endpoint:** `PATCH /api/v1/vendas/{id}/status?status={STATUS}`

**Parâmetros:**
- `status`: PENDENTE, SUCESSO, ERRO, CANCELADA

**Exemplo cURL:**
```bash
curl -X PATCH "http://localhost:8080/api/v1/vendas/123/status?status=SUCESSO"
```

### 1.10 Cancelar Venda
**Endpoint:** `PATCH /api/v1/vendas/{id}/cancelar`

**Response (200 OK):** Venda com status atualizado para CANCELADA

### 1.11 Finalizar Venda Parcialmente
**Endpoint:** `PATCH /api/v1/vendas/{id}/finalizar-parcial`

**Comportamento:**
- Verifica estoque disponível
- Se há estoque suficiente, finaliza com SUCESSO
- Se há estoque parcial, finaliza com status PARCIAL
- Se não há estoque, mantém PENDENTE

### 1.12 Deletar Venda
**Endpoint:** `DELETE /api/v1/vendas/{id}`

**Response:** 204 No Content

## 2. API de Clientes

### 2.1 Criar Cliente
**Endpoint:** `POST /api/v1/clientes`

**Request Body:**
```json
{
  "nome": "João Silva",
  "razaoSocial": "João Silva ME",
  "nomeFantasia": "Silva Comércio",
  "cnpj": "12.345.678/0001-90"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "razaoSocial": "João Silva ME",
  "nomeFantasia": "Silva Comércio",
  "cnpj": "12.345.678/0001-90"
}
```

### 2.2 Buscar Cliente por ID
**Endpoint:** `GET /api/v1/clientes/{id}`

### 2.3 Listar Todos os Clientes
**Endpoint:** `GET /api/v1/clientes`

### 2.4 Atualizar Cliente
**Endpoint:** `PUT /api/v1/clientes/{id}`

### 2.5 Deletar Cliente
**Endpoint:** `DELETE /api/v1/clientes/{id}`

## 3. Status das Vendas

- **PENDENTE**: Venda criada, aguardando processamento
- **SUCESSO**: Venda finalizada com sucesso, estoque baixado
- **ERRO**: Erro no processamento da venda
- **CANCELADA**: Venda cancelada
- **PARCIAL**: Venda finalizada parcialmente (alguns produtos sem estoque)

## 4. API Externa de Produtos

### 4.1 Listar Produtos
**Endpoint:** `GET http://localhost:3000/api/produtos`

### 4.2 Buscar Produto por ID
**Endpoint:** `GET http://localhost:3000/api/produtos/{id}`

### 4.3 Baixa de Estoque Individual
**Endpoint:** `POST http://localhost:3000/api/produtos/{id}/baixa`

**Request Body:**
```json
{
  "quantidade": 5
}
```

### 4.4 Baixa de Estoque em Lote
**Endpoint:** `POST http://localhost:3000/api/produtos/baixa`

**Request Body:**
```json
[
  {
    "id": 1,
    "quantidade": 2
  },
  {
    "id": 2,
    "quantidade": 3
  }
]
```

## 5. Fluxo Completo de Uso

### Cenário 1: Criar uma venda simples
1. Criar cliente (se não existir)
2. Verificar produtos disponíveis na API externa
3. Criar venda
4. Sistema valida estoque automaticamente
5. Se há estoque, venda fica PENDENTE
6. Finalizar venda (baixa estoque e muda status para SUCESSO)

### Cenário 2: Venda com múltiplos produtos
1. Criar venda inicial
2. Adicionar produtos usando POST /vendas/{id}/produtos
3. Finalizar venda

### Cenário 3: Finalização parcial
1. Criar venda
2. Tentar finalizar com PATCH /vendas/{id}/finalizar-parcial
3. Sistema verifica estoque e finaliza o que é possível

## 6. Tratamento de Erros

### Códigos de Status HTTP:
- **200 OK**: Operação realizada com sucesso
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Operação realizada, sem conteúdo retornado
- **400 Bad Request**: Dados inválidos ou operação não permitida
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor

### Estrutura de Erro:
```json
{
  "error": "Erro ao processar venda",
  "message": "Estoque insuficiente para o produto ID: 10",
  "timestamp": "2024-12-13T15:30:00Z"
}
```

## 7. Exemplos de Uso com JavaScript/Fetch

```javascript
// Criar uma venda
async function criarVenda() {
  const response = await fetch('http://localhost:8080/api/v1/vendas', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      clienteId: 1,
      produtoId: 10,
      valor: 150.50,
      quantidade: 2
    })
  });
  
  if (response.ok) {
    const venda = await response.json();
    console.log('Venda criada:', venda);
  } else {
    console.error('Erro ao criar venda:', response.status);
  }
}

// Buscar vendas consolidadas
async function buscarVendasConsolidadas() {
  const response = await fetch('http://localhost:8080/api/v1/vendas/consolidada');
  const vendas = await response.json();
  console.log('Vendas consolidadas:', vendas);
}
```

## 8. Postman Collection

Para facilitar os testes, você pode importar as seguintes requisições no Postman:

1. **Criar Venda**: POST http://localhost:8080/api/v1/vendas
2. **Listar Vendas**: GET http://localhost:8080/api/v1/vendas
3. **Vendas Consolidadas**: GET http://localhost:8080/api/v1/vendas/consolidada
4. **Adicionar Produto**: POST http://localhost:8080/api/v1/vendas/{id}/produtos
5. **Finalizar Parcial**: PATCH http://localhost:8080/api/v1/vendas/{id}/finalizar-parcial
