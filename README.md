# Lookgen Monorepo

Repositório com todos os serviços que compõem o projeto **Lookgen**.

## Estrutura

- `frontend` – aplicação web (Vite).
- `ui-backend` – API principal em Spring Boot.
- `styler-service` – serviço de geração de estilos.
- `notification-service` – serviço de notificações.
- `docs.ai` – arquivos usados para geração de documentação via IA.
- `tools` – scripts auxiliares.

## Requisitos

- Node.js 20+ e pnpm
- JDK 17 ou superior
- MySQL 5.7+

Defina as variáveis `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` antes de iniciar os serviços.

## Build e Execução

### Frontend

```bash
cd frontend
pnpm install
pnpm dev            # modo desenvolvimento
pnpm build          # build de produção
```

### Serviços Java

Cada serviço pode ser executado de forma independente.

```bash
# ui-backend
cd ui-backend
./mvnw spring-boot:run
```

```bash
# styler-service
cd styler-service
./mvnw spring-boot:run
```

```bash
# notification-service
cd notification-service
./gradlew bootRun
```

Os logs são gravados em `/var/log/lookgen`.
