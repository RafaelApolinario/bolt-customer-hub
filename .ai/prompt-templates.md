# Prompt Templates

## Implementar funcionalidade

```txt
Leia docs/specs e implemente [funcionalidade].

Requisitos:
- Respeitar Clean Architecture.
- Nao colocar regra de negocio no controller.
- Criar ou atualizar testes.
- Rodar validacao local.
- Explicar arquivos alterados.
```

## Criar testes

```txt
Crie testes para [regra ou endpoint].

Priorize:
- Cenario feliz.
- Erros de negocio.
- Contrato HTTP quando for endpoint.
- Nomes descritivos.
```

## Revisar arquitetura

```txt
Revise a arquitetura do projeto.

Procure:
- Dependencias indevidas no dominio.
- Use cases acoplados a infraestrutura.
- Controllers com regra de negocio.
- Regras sem cobertura de testes.
- Duplicacoes que prejudiquem manutencao.
```

## Atualizar documentacao

```txt
Atualize README e docs/specs para refletir [mudanca].

Inclua:
- Como validar.
- Regras impactadas.
- Endpoints ou payloads alterados.
- Decisoes tecnicas relevantes.
```

