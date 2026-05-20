# Project Context

Este projeto e uma API REST para cadastro e gestao de clientes usando Java 17,
Spring Boot, JPA, H2 e Maven.

O objetivo e implementar o desafio tecnico da Bolt com foco em:

- Spec-Driven Development
- Clean Architecture
- DDD basico
- CQRS simples
- Testes automatizados
- Codigo claro e manutenivel

Regras principais:

- BR-001: Nao cadastrar documento duplicado.
- BR-002: Nao compartilhar unidade consumidora entre clientes diferentes.
- BR-003: Consultar enderecos via ViaCEP.
- BR-004: Nao remover clientes fisicamente.
- BR-005: Bloquear unidades consumidoras em SP, RS e PR.
- BR-006: Publicar evento `analise_cliente_mg` para clientes com unidade em MG.

Fluxo recomendado:

```txt
spec -> teste -> implementacao -> validacao -> revisao -> refatoracao
```

