# Bolt Customer Hub

Customer Management API para o desafio tecnico da Bolt, desenvolvida como um mini produto profissional usando:

```txt
spec -> teste -> implementacao -> validacao -> revisao -> refatoracao
```

O projeto implementa cadastro, atualizacao, remocao logica e consulta de clientes com Clean Architecture, DDD basico e CQRS simples.

## Tecnologias

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Hibernate
- H2 Database
- Maven Wrapper
- Bean Validation
- Spring RestClient para ViaCEP
- Springdoc OpenAPI / Swagger
- JUnit 5
- Mockito
- Docker
- GitHub Actions

## Comecar do zero

Pre-requisitos obrigatorios:

- Java 17
- Git

Pre-requisitos opcionais:

- Docker, apenas se quiser subir com `docker compose`
- Postman, apenas se quiser importar a colecao em `docs/postman`

Nao e necessario instalar Maven. O projeto usa Maven Wrapper (`mvnw` e `mvnw.cmd`).

Passo a passo no Windows com PowerShell:

```powershell
git clone <url-do-repositorio>
cd bolt-customer-hub
.\scripts\check.ps1
.\scripts\run.ps1
```

Se a porta `8082` ja estiver em uso, rode em outra porta:

```powershell
.\scripts\run.ps1 -ServerPort 18082
```

Passo a passo no Git Bash, Linux ou macOS:

```bash
git clone <url-do-repositorio>
cd bolt-customer-hub
./scripts/check.sh
./scripts/run.sh
```

Se a porta `8082` ja estiver em uso, rode em outra porta:

```bash
SERVER_PORT=18082 ./scripts/run.sh
```

Depois que a aplicacao subir, acesse:

```txt
http://localhost:8082/swagger-ui.html
```

## Validacao rapida da API

Com a aplicacao rodando, execute em outro terminal PowerShell:

```powershell
.\scripts\api-smoke.ps1
```

Esse smoke test faz o fluxo principal da API:

- verifica `GET /api/health`
- cria um cliente com `POST /api/customers`
- lista clientes com `GET /api/customers`
- busca por ID com `GET /api/customers/{id}`
- atualiza com `PUT /api/customers/{id}`
- lista ultimos clientes com `GET /api/customers/latest`
- remove logicamente com `DELETE /api/customers/{id}`

## Arquitetura

```txt
src/main/java/com/bolt/customer

domain         -> entidades, value objects, regras puras e contratos
application    -> commands, queries, gateways, eventos e use cases
infrastructure -> JPA, ViaCEP, eventos Spring e configuracoes
interfaces     -> controllers REST, DTOs, handlers e mappers
```

Decisoes principais:

- O dominio nao depende de Spring, JPA, DTOs REST ou infrastructure.
- Repositorios do dominio sao interfaces.
- Entidades JPA ficam apenas em `infrastructure.persistence`.
- Controllers sao finos e delegam regras para use cases.
- Commands alteram estado; queries apenas consultam.
- Remocao de cliente e logica, mantendo o registro no banco.
- Listagens publicas retornam apenas clientes ativos.
- O topico logico `analise_cliente_mg` foi implementado com `ApplicationEventPublisher`.

## Regras de negocio

- BR-001: nao permitir cliente com documento duplicado.
- BR-002: nao permitir unidade consumidora vinculada a clientes diferentes.
- BR-003: consultar endereco via ViaCEP a partir do CEP informado.
- BR-004: nao remover clientes fisicamente.
- BR-005: bloquear cadastro ou atualizacao com unidades em SP, RS ou PR.
- BR-006: publicar evento `analise_cliente_mg` quando houver unidade em MG.
- BR-007: manter apenas clientes ativos nas listagens publicas.
- BR-008: exigir nome, documento e ao menos uma unidade consumidora.
- BR-009: retornar no maximo 20 clientes em `/api/customers/latest`, ordenados por `createdAt desc`.

## Endpoints

Base local:

```txt
http://localhost:8082
```

Endpoints principais:

```txt
POST   /api/customers
PUT    /api/customers/{id}
DELETE /api/customers/{id}
GET    /api/customers
GET    /api/customers/{id}
GET    /api/customers/latest
GET    /api/health
```

Exemplo de cadastro:

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

Status esperados:

- `201 Created`: cliente cadastrado.
- `200 OK`: cliente consultado ou atualizado.
- `204 No Content`: cliente removido logicamente.
- `400 Bad Request`: payload invalido.
- `404 Not Found`: cliente nao encontrado.
- `409 Conflict`: documento ou unidade consumidora duplicada.
- `422 Unprocessable Entity`: CEP invalido ou UF bloqueada.

## Documentacao

- [Requisitos](docs/specs/requirements.md)
- [Regras de negocio](docs/specs/business-rules.md)
- [Contrato da API](docs/specs/api-contract.md)
- [Criterios de aceite](docs/specs/acceptance-criteria.md)
- [Plano de testes](docs/specs/test-plan.md)
- [Colecao Postman](docs/postman/customer-api.postman_collection.json)
- [Regras de IA do projeto](.ai/project-context.md)

Swagger:

```txt
http://localhost:8082/swagger-ui.html
```

OpenAPI JSON:

```txt
http://localhost:8082/v3/api-docs
```

## Como rodar localmente

Pre-requisito:

```txt
Java 17
```

O repositorio usa Maven Wrapper, entao nao e necessario instalar Maven globalmente.

No Windows:

```powershell
.\scripts\run.ps1
```

Ou manualmente:

```powershell
$env:MAVEN_USER_HOME = Join-Path (Get-Location) ".m2"
.\mvnw.cmd spring-boot:run
```

Em Git Bash, Linux ou macOS:

```bash
./scripts/run.sh
```

Ou manualmente:

```bash
MAVEN_USER_HOME="$PWD/.m2" ./mvnw spring-boot:run
```

A aplicacao sobe em:

```txt
http://localhost:8082
```

## Como testar

No Windows:

```powershell
.\scripts\check.ps1
```

Em Git Bash, Linux ou macOS:

```bash
./scripts/check.sh
```

## H2

Console:

```txt
http://localhost:8082/h2-console
```

Dados padrao:

```txt
JDBC URL: jdbc:h2:mem:customerhub
User: sa
Password:
```

Com profile `dev`, o banco em memoria usa:

```txt
jdbc:h2:mem:customerhub_dev
```

## Docker

Subir a aplicacao:

```bash
docker compose up --build
```

Parar:

```bash
docker compose down
```

O container expoe a API em:

```txt
http://localhost:8082
```

## CI

O workflow em `.github/workflows/ci.yml` executa:

```bash
./mvnw -B clean test
```

Ele roda em push para `main`, `develop` e `feature/**`, alem de pull requests para `main` e `develop`.

## Scripts

```txt
scripts/check.ps1     -> executa validacao completa no PowerShell
scripts/test.ps1      -> executa testes no PowerShell
scripts/run.ps1       -> sobe a aplicacao no PowerShell
scripts/api-smoke.ps1 -> testa o fluxo principal da API
scripts/check.sh      -> executa validacao completa em bash
scripts/test.sh       -> executa testes em bash
scripts/run.sh        -> sobe a aplicacao em bash
```

## ViaCEP

Durante cadastro e atualizacao, a API recebe apenas o CEP da unidade consumidora. O endereco completo e consultado em:

```txt
https://viacep.com.br/ws
```

Se o CEP nao existir ou o ViaCEP retornar erro, a API responde `422 Unprocessable Entity`.

## Evento MG

Quando um cliente e cadastrado com pelo menos uma unidade consumidora em MG, a aplicacao publica o evento interno `CustomerRegisteredInMgEvent`.

Esse evento representa o topico logico:

```txt
analise_cliente_mg
```

Foi usado `ApplicationEventPublisher` para manter a solucao simples e adequada ao escopo do desafio.

## Branches

Fluxo usado:

```txt
feature/* -> develop -> main
```

Branches principais trabalhadas:

```txt
feature/00-project-bootstrap
feature/01-specs-and-rules
feature/02-domain-model
feature/03-address-viacep
feature/04-customer-create
feature/05-customer-update
feature/06-customer-delete
feature/07-customer-query
feature/08-mg-event
feature/09-tests
feature/10-openapi-postman
feature/11-docker-ci
feature/12-ai-project-rules
refactor/clean-architecture-review
```

## Validacao atual

Resultado local esperado:

```txt
Tests run: 50, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

