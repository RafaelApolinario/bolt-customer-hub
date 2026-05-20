# Plano de testes

## Estratégia

Os testes devem seguir a mesma separação da arquitetura:

- Domínio: regras puras, sem Spring e sem banco.
- Aplicação: use cases com repositórios e gateways mockados.
- Infraestrutura: repositórios JPA, client ViaCEP e adapters.
- Interfaces REST: contrato HTTP, validação de payload e tradução de erros.

## Testes unitários de domínio

### CustomerTest

- `shouldCreateActiveCustomer()`
- `shouldNotCreateCustomerWithoutName()`
- `shouldNotCreateCustomerWithoutDocument()`
- `shouldAddConsumerUnit()`
- `shouldLogicallyDeleteCustomer()`

Regras cobertas:

- BR-004
- BR-007
- BR-008

### DocumentTest

- `shouldNormalizeDocument()`
- `shouldRejectBlankDocument()`
- `shouldCompareDocumentsByNormalizedValue()`

Regras cobertas:

- BR-001
- BR-008

## Testes unitários de aplicação

### CreateCustomerUseCaseTest

- `shouldCreateCustomerWhenDataIsValid()`
- `shouldNotCreateCustomerWhenDocumentAlreadyExists()`
- `shouldNotCreateCustomerWhenConsumerUnitBelongsToAnotherCustomer()`
- `shouldNotCreateCustomerWhenZipCodeIsInvalid()`
- `shouldNotCreateCustomerWhenConsumerUnitIsInSp()`
- `shouldNotCreateCustomerWhenConsumerUnitIsInRs()`
- `shouldNotCreateCustomerWhenConsumerUnitIsInPr()`
- `shouldPublishMgAnalysisEventWhenCustomerHasConsumerUnitInMg()`
- `shouldNotPublishMgAnalysisEventWhenCustomerHasNoConsumerUnitInMg()`

Regras cobertas:

- BR-001
- BR-002
- BR-003
- BR-005
- BR-006
- BR-007
- BR-009

### UpdateCustomerUseCaseTest

- `shouldUpdateCustomerWhenDataIsValid()`
- `shouldNotUpdateUnknownCustomer()`
- `shouldNotUpdateCustomerWithDuplicatedDocument()`
- `shouldNotUpdateCustomerWithConsumerUnitFromAnotherCustomer()`
- `shouldUpdateUpdatedAt()`

Regras cobertas:

- BR-001
- BR-002
- BR-003
- BR-005
- BR-009

### DeleteCustomerUseCaseTest

- `shouldLogicallyDeleteCustomer()`
- `shouldNotDeleteUnknownCustomer()`

Regra coberta:

- BR-004

### ListLatestCustomersUseCaseTest

- `shouldReturnAtMostTwentyActiveCustomers()`
- `shouldReturnLatestCustomersOrderedByCreatedAtDesc()`

Regra coberta:

- BR-010

## Testes de integração REST

### CustomerControllerIntegrationTest

- `postCustomersShouldReturnCreated()`
- `postCustomersShouldReturnConflictForDuplicatedDocument()`
- `postCustomersShouldReturnUnprocessableEntityForBlockedState()`
- `putCustomersShouldReturnOk()`
- `putCustomersShouldReturnNotFoundForUnknownCustomer()`
- `deleteCustomersShouldReturnNoContent()`
- `getCustomersShouldReturnOnlyActiveCustomers()`
- `getCustomerByIdShouldReturnNotFoundForUnknownCustomer()`
- `getLatestCustomersShouldReturnAtMostTwentyCustomers()`

## Testes de infraestrutura

### JpaCustomerRepositoryTest

- `shouldSaveAndFindCustomer()`
- `shouldFindByDocument()`
- `shouldFindByConsumerUnitNumber()`
- `shouldListOnlyActiveCustomers()`
- `shouldListLatestActiveCustomers()`

### ViaCepAddressGatewayTest

- `shouldReturnAddressWhenZipCodeExists()`
- `shouldThrowBusinessExceptionWhenZipCodeDoesNotExist()`

## Comandos de validação planejados

```bash
mvn clean test
```

Após adicionar scripts:

```bash
scripts/check.sh
```
