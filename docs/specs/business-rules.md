# Regras de negócio

## BR-001 - Documento único

Não deve ser permitido cadastrar ou atualizar um cliente usando documento já vinculado
a outro cliente.

Resultado esperado:

- Conflito de negócio.
- HTTP `409 Conflict` na API REST.
- Nenhum dado novo deve ser persistido no cenário inválido.

## BR-002 - Unidade consumidora única

Não deve ser permitido vincular a mesma unidade consumidora a clientes diferentes.

Resultado esperado:

- Conflito de negócio.
- HTTP `409 Conflict` na API REST.
- A unidade consumidora deve continuar vinculada apenas ao cliente original.

## BR-003 - Endereço validado via ViaCEP

Endereços de unidades consumidoras devem ser obtidos via ViaCEP a partir do CEP informado.
O cliente da API não deve enviar endereço completo como fonte de verdade.

Resultado esperado:

- CEP existente preenche endereço da unidade consumidora.
- CEP inexistente ou inválido retorna erro de negócio.
- HTTP `422 Unprocessable Entity` para CEP inválido.

## BR-004 - Remoção lógica

Cliente não deve ser removido fisicamente do banco de dados.

Resultado esperado:

- Campo `active` deve ser alterado para `false`.
- Registro deve continuar persistido.
- Listagens públicas devem retornar apenas clientes ativos.

## BR-005 - Bloqueio regional

Unidades consumidoras localizadas em `SP`, `RS` ou `PR` bloqueiam cadastro e atualização
do cliente.

Resultado esperado:

- Cadastro ou atualização deve ser recusado.
- HTTP `422 Unprocessable Entity`.
- Nenhum dado inválido deve ser persistido.

## BR-006 - Evento para Minas Gerais

Cliente com ao menos uma unidade consumidora localizada em `MG` deve gerar o evento lógico
`analise_cliente_mg`.

Resultado esperado:

- Evento deve ser publicado após cadastro bem-sucedido.
- Cliente sem unidade em `MG` não deve publicar evento.
- A implementação inicial pode usar `ApplicationEventPublisher` do Spring.

## BR-007 - Cliente ativo ao ser criado

Todo cliente criado com sucesso deve iniciar com `active = true`.

## BR-008 - Dados obrigatórios do cliente

Cliente deve possuir nome e documento obrigatórios.

## BR-009 - Datas de auditoria

Cliente deve possuir `createdAt` e `updatedAt`.

Resultado esperado:

- `createdAt` deve ser preenchido na criação.
- `updatedAt` deve ser preenchido na criação e alterado a cada atualização.

## BR-010 - Últimos clientes

A consulta de últimos clientes deve retornar no máximo 20 clientes ativos, ordenados por
`createdAt` em ordem decrescente.
