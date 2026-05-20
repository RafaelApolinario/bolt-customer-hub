# Requisitos

## Objetivo

Construir uma API REST para gerenciamento de clientes, usando Java 17, Spring Boot,
Clean Architecture, DDD básico e CQRS simples.

O sistema deve permitir cadastrar, atualizar, remover logicamente e consultar clientes,
mantendo regras de negócio explícitas, testáveis e separadas da camada REST.

## Escopo funcional

### RF-001 - Cadastrar cliente

O sistema deve permitir o cadastro de um cliente com seus dados básicos e uma ou mais
unidades consumidoras.

### RF-002 - Atualizar cliente

O sistema deve permitir a atualização dos dados de um cliente existente, incluindo suas
unidades consumidoras.

### RF-003 - Remover cliente logicamente

O sistema deve permitir remover um cliente sem apagar o registro do banco de dados.

### RF-004 - Listar clientes

O sistema deve permitir listar clientes ativos.

### RF-005 - Buscar cliente por ID

O sistema deve permitir buscar os detalhes de um cliente pelo identificador.

### RF-006 - Listar últimos clientes

O sistema deve permitir listar os últimos 20 clientes cadastrados em ordem decrescente
de criação.

### RF-007 - Consultar endereço por CEP

O sistema deve consultar endereços via ViaCEP durante o cadastro ou atualização de
unidades consumidoras.

### RF-008 - Publicar evento para cliente com unidade em MG

O sistema deve publicar um evento interno no tópico lógico `analise_cliente_mg` quando
um cliente possuir ao menos uma unidade consumidora em Minas Gerais.

## Escopo não funcional

### RNF-001 - Arquitetura

O domínio não deve depender de Spring, JPA, Hibernate, DTOs REST ou qualquer detalhe de
infraestrutura.

### RNF-002 - Testes

As principais regras de negócio devem possuir testes automatizados.

### RNF-003 - Documentação

A API deve possuir documentação Swagger/OpenAPI e coleção Postman ao final do projeto.

### RNF-004 - Execução local

A aplicação deve subir localmente na porta `8082`.

### RNF-005 - Banco local

O projeto deve usar H2 para facilitar execução e avaliação local.

## Fora do escopo inicial

- Autenticação e autorização com Spring Security.
- Mensageria externa real, como Kafka, RabbitMQ ou AWS SNS/SQS.
- Banco de dados externo obrigatório.
- Frontend.
