# Testes pelo Postman

Este diretorio contem a colecao Postman da API:

```txt
customer-api.postman_collection.json
```

A colecao foi preparada para rodar localmente usando a variavel:

```txt
baseUrl = http://localhost:8082
```

Antes de executar as requisicoes, suba a aplicacao:

```powershell
.\scripts\run.ps1
```

## Como importar

1. Abra o Postman.
2. Clique em `Import`.
3. Selecione o arquivo `docs/postman/customer-api.postman_collection.json`.
4. Confirme se a variavel `baseUrl` esta como `http://localhost:8082`.

## Execucao recomendada

Para testar o fluxo principal da aplicacao, execute as requisicoes nesta ordem:

```txt
1. Health
2. Create customer
3. List customers
4. Get customer by ID
5. Update customer
6. List latest customers
7. Delete customer
```

A request `Create customer` gera valores dinamicos e salva o `customerId` automaticamente nas variaveis da colecao. Por isso, `Get customer by ID`, `Update customer` e `Delete customer` devem ser executadas depois dela.

## Resultados esperados

| Request | Status esperado | Objetivo |
| --- | --- | --- |
| Health | `200 OK` | Verificar se a API esta no ar. |
| Create customer | `201 Created` | Criar um cliente valido. |
| List customers | `200 OK` | Listar clientes ativos. |
| Get customer by ID | `200 OK` | Buscar o cliente criado. |
| Update customer | `200 OK` | Atualizar o cliente criado. |
| List latest customers | `200 OK` | Listar ate 20 clientes mais recentes. |
| Delete customer | `204 No Content` | Remover o cliente logicamente. |
| Create customer with blocked state SP | `422 Unprocessable Entity` | Validar bloqueio de unidade consumidora em SP. |
| Create customer with duplicated document | `409 Conflict` | Validar bloqueio de documento duplicado. |

## Testando os cenarios de erro

As duas ultimas requisicoes testam regras de negocio. Portanto, elas devem retornar erro. Isso significa que o teste passou.

### Create customer with blocked state SP

Essa request tenta cadastrar uma unidade consumidora com CEP de Sao Paulo:

```txt
01001000
```

Como SP e um estado bloqueado pela regra de negocio, a API deve responder:

```txt
422 Unprocessable Entity
```

Essa request pode ser executada sozinha. Ela cria automaticamente a variavel `uniqueSuffix`, caso ainda nao exista.

### Create customer with duplicated document

Essa request valida que dois clientes nao podem ter o mesmo documento.

Antes de enviar a requisicao principal, o script da propria request cria um cliente base com o mesmo documento. Em seguida, a request tenta criar outro cliente com esse documento repetido.

Resultado esperado:

```txt
409 Conflict
```

Essa request tambem pode ser executada sozinha.

## Variaveis da colecao

| Variavel | Uso |
| --- | --- |
| `baseUrl` | URL base da API local. |
| `customerId` | ID salvo apos criar um cliente. |
| `uniqueSuffix` | Sufixo usado para gerar dados unicos. |
| `document` | Documento usado nos testes. |
| `consumerUnitNumber` | Numero da unidade consumidora principal. |
| `secondConsumerUnitNumber` | Numero da segunda unidade usada no update. |

## Dicas de troubleshooting

Se alguma request retornar `404`, confira se a aplicacao esta rodando na porta correta:

```txt
http://localhost:8082
```

Se `Get customer by ID`, `Update customer` ou `Delete customer` falharem, execute novamente a request `Create customer` para preencher a variavel `customerId`.

Se a API estiver rodando em outra porta, atualize a variavel `baseUrl` no Postman. Exemplo:

```txt
http://localhost:18082
```
