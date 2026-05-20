# Bolt Customer Hub

Customer Management API para o desafio técnico da Bolt.

Este repositório será desenvolvido como um mini produto profissional, usando o ciclo:

```txt
spec -> teste -> implementação -> validação -> revisão -> refatoração
```

## Estratégia inicial

A primeira entrega do projeto é a especificação funcional e técnica em `docs/specs`.
Antes de implementar endpoints, banco ou integração externa, cada regra de negócio recebe
um identificador rastreável e cada endpoint esperado fica documentado com contrato,
status HTTP e critérios de aceite.

## Branches

Fluxo sugerido:

```txt
feature/* -> develop -> main
```

Branches planejadas:

```txt
develop
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
release/v1
```

## Documentação

- [Requisitos](docs/specs/requirements.md)
- [Regras de negócio](docs/specs/business-rules.md)
- [Contrato da API](docs/specs/api-contract.md)
- [Critérios de aceite](docs/specs/acceptance-criteria.md)
- [Plano de testes](docs/specs/test-plan.md)

## Stack planejada

```txt
Java 17
Spring Boot 3
Spring Web
Spring Data JPA
Hibernate
H2 Database
Maven
Bean Validation
RestClient ou OpenFeign para ViaCEP
Springdoc OpenAPI / Swagger
JUnit 5
Mockito
Docker
GitHub Actions
```

## Status

Entrega atual: especificação inicial do desafio.

Próximo passo recomendado: `feature/00-project-bootstrap`, criando a base Spring Boot
com Maven, Java 17, H2, profiles `dev` e `test`, porta `8082` e validação com `mvn test`.
