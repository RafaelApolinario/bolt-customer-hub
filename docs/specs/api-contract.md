# Contrato da API

Base URL local planejada:

```txt
http://localhost:8082
```

Prefixo:

```txt
/api/customers
```

## Modelo de erro

```json
{
  "timestamp": "2026-05-20T17:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Document already exists",
  "path": "/api/customers"
}
```

## POST /api/customers

Cadastra um cliente.

### Request

```json
{
  "name": "Maria Silva",
  "document": "12345678901",
  "consumerUnits": [
    {
      "number": "UC-1001",
      "zipCode": "30140071"
    }
  ]
}
```

### Response 201

```json
{
  "id": "6e6efc4f-bc77-4d86-b92d-36dcefd799d8",
  "name": "Maria Silva",
  "document": "12345678901",
  "active": true,
  "consumerUnits": [
    {
      "number": "UC-1001",
      "zipCode": "30140071",
      "street": "Rua dos Timbiras",
      "neighborhood": "Funcionários",
      "city": "Belo Horizonte",
      "state": "MG"
    }
  ],
  "createdAt": "2026-05-20T17:30:00",
  "updatedAt": "2026-05-20T17:30:00"
}
```

### Status esperados

- `201 Created`: cliente cadastrado.
- `400 Bad Request`: payload inválido.
- `409 Conflict`: documento ou unidade consumidora duplicada.
- `422 Unprocessable Entity`: CEP inválido ou UF bloqueada.

## PUT /api/customers/{id}

Atualiza um cliente existente.

### Request

```json
{
  "name": "Maria Silva Souza",
  "document": "12345678901",
  "consumerUnits": [
    {
      "number": "UC-1001",
      "zipCode": "30140071"
    },
    {
      "number": "UC-1002",
      "zipCode": "20040002"
    }
  ]
}
```

### Response 200

Retorna o cliente atualizado no mesmo formato do cadastro.

### Status esperados

- `200 OK`: cliente atualizado.
- `400 Bad Request`: payload inválido.
- `404 Not Found`: cliente não encontrado.
- `409 Conflict`: documento ou unidade consumidora vinculada a outro cliente.
- `422 Unprocessable Entity`: CEP inválido ou UF bloqueada.

## DELETE /api/customers/{id}

Remove logicamente um cliente.

### Response 204

Sem corpo.

### Status esperados

- `204 No Content`: cliente marcado como inativo.
- `404 Not Found`: cliente não encontrado.

## GET /api/customers

Lista clientes ativos.

### Response 200

```json
[
  {
    "id": "6e6efc4f-bc77-4d86-b92d-36dcefd799d8",
    "name": "Maria Silva",
    "document": "12345678901",
    "active": true,
    "consumerUnits": [
      {
        "number": "UC-1001",
        "zipCode": "30140071",
        "street": "Rua dos Timbiras",
        "neighborhood": "Funcionários",
        "city": "Belo Horizonte",
        "state": "MG"
      }
    ],
    "createdAt": "2026-05-20T17:30:00",
    "updatedAt": "2026-05-20T17:30:00"
  }
]
```

### Status esperados

- `200 OK`: lista retornada, mesmo quando vazia.

## GET /api/customers/{id}

Busca cliente por ID.

### Response 200

Retorna o cliente no mesmo formato do cadastro.

### Status esperados

- `200 OK`: cliente encontrado.
- `404 Not Found`: cliente não encontrado.

## GET /api/customers/latest

Lista os últimos 20 clientes ativos, ordenados por `createdAt desc`.

### Response 200

Retorna lista no mesmo formato de `GET /api/customers`.

### Status esperados

- `200 OK`: lista retornada, mesmo quando vazia.
