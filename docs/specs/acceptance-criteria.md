# Critérios de aceite

## CA-001 - Cadastro válido

Dado um cliente com documento novo e unidades consumidoras válidas,
quando `POST /api/customers` for executado,
então a API deve retornar `201 Created`,
e o cliente deve ser salvo como ativo,
e `createdAt` e `updatedAt` devem ser preenchidos.

Regras cobertas:

- BR-001
- BR-002
- BR-003
- BR-007
- BR-008
- BR-009

## CA-002 - Documento duplicado no cadastro

Dado um cliente já cadastrado com determinado documento,
quando outro cadastro usar o mesmo documento,
então a API deve retornar `409 Conflict`,
e o novo cliente não deve ser persistido.

Regra coberta:

- BR-001

## CA-003 - Unidade consumidora duplicada

Dado uma unidade consumidora já vinculada a um cliente,
quando outro cliente tentar usar a mesma unidade,
então a API deve retornar `409 Conflict`.

Regra coberta:

- BR-002

## CA-004 - CEP inválido

Dado um CEP inexistente ou inválido,
quando cadastro ou atualização forem executados,
então a API deve retornar `422 Unprocessable Entity`.

Regra coberta:

- BR-003

## CA-005 - UF bloqueada

Dado uma unidade consumidora localizada em `SP`, `RS` ou `PR`,
quando cadastro ou atualização forem executados,
então a API deve retornar `422 Unprocessable Entity`,
e o cliente não deve ser salvo com essa unidade.

Regra coberta:

- BR-005

## CA-006 - Atualização válida

Dado um cliente existente,
quando `PUT /api/customers/{id}` for executado com dados válidos,
então a API deve retornar `200 OK`,
e `updatedAt` deve ser alterado.

Regras cobertas:

- BR-001
- BR-002
- BR-003
- BR-005
- BR-009

## CA-007 - Cliente inexistente na atualização

Dado um ID inexistente,
quando `PUT /api/customers/{id}` for executado,
então a API deve retornar `404 Not Found`.

## CA-008 - Remoção lógica

Dado um cliente existente,
quando `DELETE /api/customers/{id}` for executado,
então a API deve retornar `204 No Content`,
e o cliente deve continuar no banco com `active = false`.

Regra coberta:

- BR-004

## CA-009 - Listagem pública

Dado clientes ativos e inativos,
quando `GET /api/customers` for executado,
então apenas clientes ativos devem ser retornados.

Regra coberta:

- BR-004

## CA-010 - Busca por ID

Dado um cliente existente,
quando `GET /api/customers/{id}` for executado,
então a API deve retornar `200 OK` com os dados do cliente.

## CA-011 - Últimos 20 clientes

Dado mais de 20 clientes ativos cadastrados,
quando `GET /api/customers/latest` for executado,
então a API deve retornar no máximo 20 registros em ordem decrescente de criação.

Regra coberta:

- BR-010

## CA-012 - Evento de análise para MG

Dado um cliente com ao menos uma unidade consumidora em `MG`,
quando o cadastro for concluído com sucesso,
então o evento lógico `analise_cliente_mg` deve ser publicado.

Regra coberta:

- BR-006

## CA-013 - Sem evento para outras UFs

Dado um cliente sem unidade consumidora em `MG`,
quando o cadastro for concluído com sucesso,
então o evento lógico `analise_cliente_mg` não deve ser publicado.

Regra coberta:

- BR-006
