# Testevr API

Testevr API é uma aplicação Java desenvolvida com Spring Boot, estruturada para gerenciar clientes e vendas, seguindo boas práticas de arquitetura em camadas e DDD (Domain-Driven Design).

## Estrutura do Projeto

```
├── src/
│   ├── main/
│   │   ├── java/com/testevr/testejava/
│   │   │   ├── TestejavaApplication.java
│   │   │   ├── cliente/
│   │   │   │   ├── application/        # DTOs e mapeadores
│   │   │   │   ├── domain/             # Entidades, serviços, repositórios, value objects
│   │   │   │   ├── infra/              # Implementações de persistência
│   │   │   │   └── web/                # Controllers REST
│   │   │   ├── shared/                 # Configurações e utilitários globais
│   │   │   └── venda/                  # Módulo de vendas (external/internal)
│   │   └── resources/
│   │       ├── application.properties  # Configurações da aplicação
│   │       └── db/migration/           # Scripts Flyway para o banco de dados
│   └── test/                           # Testes unitários e de integração
├── build.gradle                        # Configuração do Gradle
├── settings.gradle                     # Configuração de módulos
└── README.md                           # Este arquivo
```

## Principais Tecnologias
- Java
- Spring Boot
- Gradle
- JUnit (testes)
- Flyway (migração de banco de dados)

## Como Executar

1. **Clone o repositório:**
	```sh
	git clone git@github.com:ybueno16/testevr-api.git
	cd testevr-api
	```
2. **Configure o banco de dados:**
	- Edite `src/main/resources/application.properties` conforme necessário.
	- Os scripts de migração estão em `src/main/resources/db/migration/`.
3. **Compile e rode a aplicação:**
	```sh
	./gradlew bootRun
	```
4. **Testes:**
	```sh
	./gradlew test
	```

## Estrutura dos Módulos

- **cliente**: Gestão de clientes (entidades, serviços, repositórios, controllers)
- **venda**: Gestão de vendas (external/internal)
- **shared**: Configurações, utilitários e tratamento global de exceções

## Padrões e Boas Práticas
- Separação clara entre camadas (application, domain, infra, web)
- Uso de DTOs para comunicação entre camadas
- Value Objects para encapsular regras de negócio
- Testes unitários e de integração organizados por domínio

## Scripts de Banco de Dados
Os scripts de migração Flyway estão em `src/main/resources/db/migration/` e são aplicados automaticamente ao iniciar a aplicação.

## Contato
Para dúvidas ou sugestões, abra uma issue ou entre em contato com o mantenedor do projeto.
# testevr-api