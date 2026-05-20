# Coding Rules

1. O dominio nao deve depender de Spring, JPA ou qualquer framework.
2. Use cases devem conter a orquestracao das regras de negocio.
3. Controllers devem ser finos e delegar trabalho para a camada de aplicacao.
4. DTOs de REST nao devem vazar para o dominio.
5. Entidades JPA ficam apenas na camada `infrastructure`.
6. Repositorios do dominio sao interfaces.
7. Toda regra de negocio relevante deve ter teste automatizado.
8. Antes de implementar comportamento novo, atualize ou consulte a spec.
9. Nao criar abstracoes sem necessidade real.
10. Preferir nomes claros a comentarios explicativos em excesso.
11. Commands alteram estado; queries apenas consultam.
12. Exceptions de negocio devem retornar status HTTP coerente na camada REST.

