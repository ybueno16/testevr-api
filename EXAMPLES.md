# Exemplos Práticos de Uso - Sistema de Vendas

## Testando a API Externa (Produtos)

Primeiro, certifique-se de que a API externa esteja rodando em `http://localhost:3000`

### 1. Verificar produtos disponíveis
```bash
curl -X GET http://localhost:3000/api/produtos
```

### 2. Verificar produto específico
```bash
curl -X GET http://localhost:3000/api/produtos/1
```

## Testando a API de Clientes

### 1. Criar um cliente
```bash
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "razaoSocial": "João Silva ME",
    "nomeFantasia": "Silva Comércio",
    "cnpj": "12.345.678/0001-90"
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "razaoSocial": "João Silva ME",
  "nomeFantasia": "Silva Comércio",
  "cnpj": "12.345.678/0001-90",
  "ativo": true,
  "createdAt": "2024-12-13T15:30:00",
  "updatedAt": "2024-12-13T15:30:00"
}
```

### 2. Listar todos os clientes
```bash
curl -X GET http://localhost:8080/api/v1/clientes
```

### 3. Buscar cliente por ID
```bash
curl -X GET http://localhost:8080/api/v1/clientes/1
```

### 4. Atualizar cliente
```bash
curl -X PUT http://localhost:8080/api/v1/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Santos",
    "razaoSocial": "João Silva Santos ME",
    "nomeFantasia": "Silva Santos Comércio",
    "cnpj": "12.345.678/0001-90"
  }'
```

## Testando a API de Vendas

### 1. Criar uma venda simples
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "produtoId": 1,
    "valor": 36.45,
    "quantidade": 2
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "clienteId": 1,
  "produtoId": 1,
  "valor": 36.45,
  "quantidade": 2,
  "status": "PENDENTE"
}
```

### 2. Verificar a venda criada
```bash
curl -X GET http://localhost:8080/api/v1/vendas/1
```

### 3. Finalizar venda parcialmente (tenta baixar estoque)
```bash
curl -X PATCH http://localhost:8080/api/v1/vendas/1/finalizar-parcial
```

### 4. Verificar status após finalização
```bash
curl -X GET http://localhost:8080/api/v1/vendas/1
```

### 5. Adicionar mais produtos à venda (se ainda não finalizada)
```bash
curl -X POST http://localhost:8080/api/v1/vendas/1/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "produtoId": 1,
    "quantidade": 1
  }'
```

### 6. Criar outra venda para teste de consolidação
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "produtoId": 2,
    "valor": 85.90,
    "quantidade": 1
  }'
```

### 7. Visualizar vendas consolidadas
```bash
curl -X GET http://localhost:8080/api/v1/vendas/consolidada
```

**Resposta esperada:**
```json
[
  {
    "clienteId": 1,
    "status": "SUCESSO",
    "valorTotal": 122.35,
    "quantidadeVendas": 2
  }
]
```

### 8. Cancelar uma venda
```bash
curl -X PATCH http://localhost:8080/api/v1/vendas/2/cancelar
```

### 9. Visualizar vendas por cliente
```bash
curl -X GET http://localhost:8080/api/v1/vendas/cliente/1
```

### 10. Atualizar status manualmente
```bash
curl -X PATCH "http://localhost:8080/api/v1/vendas/1/status?status=SUCESSO"
```

## Fluxo Completo de Teste

### Cenário 1: Venda com Sucesso

1. **Criar cliente:**
```bash
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Maria Santos", "razaoSocial": "Maria Santos LTDA", "nomeFantasia": "Santos Store", "cnpj": "98.765.432/0001-10"}'
```

2. **Verificar produto disponível:**
```bash
curl -X GET http://localhost:3000/api/produtos/1
```

3. **Criar venda:**
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 1, "valor": 36.45, "quantidade": 1}'
```

4. **Finalizar venda:**
```bash
curl -X PATCH http://localhost:8080/api/v1/vendas/1/finalizar-parcial
```

5. **Verificar resultado:**
```bash
curl -X GET http://localhost:8080/api/v1/vendas/1
```

### Cenário 2: Venda com Estoque Insuficiente

1. **Criar venda com quantidade alta:**
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 1, "valor": 36.45, "quantidade": 999}'
```

2. **Tentar finalizar:**
```bash
curl -X PATCH http://localhost:8080/api/v1/vendas/2/finalizar-parcial
```

3. **Verificar que ficou pendente:**
```bash
curl -X GET http://localhost:8080/api/v1/vendas/2
```

### Cenário 3: Edição de Venda

1. **Criar venda:**
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 1, "valor": 36.45, "quantidade": 1}'
```

2. **Editar a venda (alterar quantidade):**
```bash
curl -X PUT http://localhost:8080/api/v1/vendas/3 \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 1, "valor": 36.45, "quantidade": 2}'
```

3. **Verificar alteração:**
```bash
curl -X GET http://localhost:8080/api/v1/vendas/3
```

## Verificação de Erros Comuns

### 1. Produto não encontrado:
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 999, "valor": 100.00, "quantidade": 1}'
```

### 2. Cliente não encontrado:
```bash
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 999, "produtoId": 1, "valor": 36.45, "quantidade": 1}'
```

### 3. Tentar editar venda finalizada:
```bash
# Primeiro finalizar uma venda
curl -X PATCH http://localhost:8080/api/v1/vendas/1/finalizar-parcial

# Depois tentar editar (deve dar erro)
curl -X PUT http://localhost:8080/api/v1/vendas/1 \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 2, "valor": 50.00, "quantidade": 1}'
```

## Scripts para Batch Testing

### Script Bash para criar dados de teste:
```bash
#!/bin/bash

echo "Criando clientes de teste..."

# Cliente 1
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "João Silva", "razaoSocial": "João Silva ME", "nomeFantasia": "Silva Comércio", "cnpj": "12.345.678/0001-90"}'

# Cliente 2
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Maria Santos", "razaoSocial": "Maria Santos LTDA", "nomeFantasia": "Santos Store", "cnpj": "98.765.432/0001-10"}'

echo -e "\n\nCriando vendas de teste..."

# Venda 1
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 1, "valor": 36.45, "quantidade": 2}'

# Venda 2
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "produtoId": 2, "valor": 50.00, "quantidade": 1}'

# Venda 3
curl -X POST http://localhost:8080/api/v1/vendas \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 2, "produtoId": 1, "valor": 36.45, "quantidade": 3}'

echo -e "\n\nFinalizando vendas..."

# Finalizar vendas
curl -X PATCH http://localhost:8080/api/v1/vendas/1/finalizar-parcial
curl -X PATCH http://localhost:8080/api/v1/vendas/2/finalizar-parcial
curl -X PATCH http://localhost:8080/api/v1/vendas/3/finalizar-parcial

echo -e "\n\nVisualizando relatório consolidado..."
curl -X GET http://localhost:8080/api/v1/vendas/consolidada | jq .
```

Salve este script como `test-api.sh`, dê permissão de execução (`chmod +x test-api.sh`) e execute.

## Testando com Postman

Importe a seguinte collection no Postman:

```json
{
  "info": {
    "name": "Sistema de Vendas API",
    "description": "Collection para testar todas as APIs do sistema"
  },
  "item": [
    {
      "name": "Clientes",
      "item": [
        {
          "name": "Criar Cliente",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/v1/clientes",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "body": {
              "raw": "{\"nome\": \"João Silva\", \"razaoSocial\": \"João Silva ME\", \"nomeFantasia\": \"Silva Comércio\", \"cnpj\": \"12.345.678/0001-90\"}"
            }
          }
        },
        {
          "name": "Listar Clientes",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/v1/clientes"
          }
        }
      ]
    },
    {
      "name": "Vendas",
      "item": [
        {
          "name": "Criar Venda",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/v1/vendas",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "body": {
              "raw": "{\"clienteId\": 1, \"produtoId\": 1, \"valor\": 36.45, \"quantidade\": 2}"
            }
          }
        },
        {
          "name": "Vendas Consolidadas",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/v1/vendas/consolidada"
          }
        }
      ]
    }
  ]
}
```
