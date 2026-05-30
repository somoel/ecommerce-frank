# E-commerce Monorepo

5 independent Spring Boot microservices. **No multi-module build** -- each service is a standalone project with its own build tool, wrapper, and dependencies.

## Services & Ports

| Service | Port | Build Tool | Spring Boot | Java |
|---------|------|-----------|-------------|------|
| `auth` | 6767 | Gradle 9.4 | 4.0.4 | 17 |
| `catalogo` | 8080 | Maven | 4.0.5 | 17 |
| `inventario` | 6771 | Gradle 9.5 | 4.0.6 | 17 |
| `notificaciones` | 6770 | Gradle 9.5 | 4.0.6 | 17 |
| `ordenes` | 6769 | Gradle 9.5 | 4.0.6 | 17 |

## Running a Service

```powershell
# Start DB first (one-time)
docker compose -f auth\compose.yaml up -d

# Gradle services (auth, inventario, notificaciones, ordenes)
cd <service>
.\gradlew.bat bootRun

# Maven service (catalogo)
cd catalogo
.\mvnw.cmd spring-boot:run
```

## Running Tests

```powershell
# Gradle services
cd <service>; .\gradlew.bat test

# Maven service
cd catalogo; .\mvnw.cmd test
```

**Only `auth` has isolated tests** (H2 in-memory, PostgreSQL mode). The other 4 services require a running PostgreSQL instance at `localhost:5432` for `@SpringBootTest` to pass.

## Database

Shared PostgreSQL instance across all services:

```
URL:      jdbc:postgresql://localhost:5432/ecommerce_frank
User:     frank / Password: 123456
DDL mode: update (auto-creates tables from JPA entities)
```

## Architecture Pattern

**Hexagonal Architecture (Ports & Adapters)** -- all services follow the same layout:

```
domain/model/
  ├── Entity.java, *UseCase.java
  ├── gateway/        → Port interfaces (UsuarioGateway, SecurityGateway)
  └── exception/      → Domain exceptions
application/
  └── useCaseConfig.java  → Bean wiring (@Configuration)
infraestructure/
  ├── entry_points/    → Controllers, DTOs, ExceptionHandler
  ├── driver_adapters/ → JPA Entity, Repository, GatewayImpl
  ├── mapper/          → Domain ↔ Data mapping
  └── security/        → (auth only) BCrypt impl
```

Rules: Domain layer has **zero framework annotations**. Gateways define ports. Adapters implement them. Mappers keep layers decoupled.

## Gotchas

- **`catalogo` has no DB config** in `application.properties` -- only `spring.application.name` is set. It will fail at startup without datasource configuration.
- **`notificaciones` has hardcoded SMTP credentials** in `application.properties`. Do not commit further credentials to this file.
- **Root `.gitignore` only covers `auth/build/` and `catalogo/target/`** -- other service build dirs rely on per-service `.gitignore`.
- **No CI/CD** -- no GitHub Actions, Jenkinsfile, or Dockerfiles exist.
- **Inconsistent versions** -- Spring Boot (4.0.4/4.0.5/4.0.6) and Gradle (9.4/9.5) vary across services.
