# Spec-Driven Workflow

Use este fluxo para qualquer mudanca relevante:

```txt
spec -> teste -> implementacao -> validacao -> revisao -> refatoracao
```

## 1. Spec

- Localize a regra em `docs/specs`.
- Se a regra nao existir, adicione ou ajuste a spec antes de implementar.
- Cada regra de negocio deve ter um ID.

## 2. Teste

- Crie teste unitario para regra de dominio ou use case.
- Crie teste de integracao para contrato REST quando houver endpoint.
- Cubra sucesso e pelo menos um erro relevante.

## 3. Implementacao

- Implemente o menor caminho que preserve a arquitetura.
- Commands alteram estado; queries consultam.
- Controllers recebem request, validam DTO e chamam use case.

## 4. Validacao

Execute:

```bash
./mvnw clean test
```

No Windows:

```powershell
$env:MAVEN_USER_HOME = (Join-Path (Get-Location) '.m2')
.\mvnw.cmd clean test
```

## 5. Revisao

- Verifique boundaries arquiteturais.
- Confira se README/spec precisam ser atualizados.
- Procure casos de erro sem teste.

## 6. Refatoracao

- Refatore apenas depois de testes verdes.
- Evite mudancas cosmeticas amplas junto de alteracoes funcionais.

