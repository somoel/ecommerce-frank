# E-commerce Monorepo

5 independent Spring Boot microservices. **No multi-module build** -- each service is a standalone project with its own build tool, wrapper, and dependencies.

## Services

| Service | Port | Build Tool | Spring Boot | Java |
|---------|------|-----------|-------------|------|
| `auth` | 6767 | Gradle 9.4 | 4.0.4 | 17 |
| `catalogo` | 8080 | Maven | 4.0.5 | 17 |
| `inventario` | 6771 | Gradle 9.5 | 4.0.6 | 17 |
| `notificaciones` | 6770 | Gradle 9.5 | 4.0.6 | 17 |
| `ordenes` | 6769 | Gradle 9.5 | 4.0.6 | 17 |

## Production URLs (Railway.app + Supabase)

| Service | URL |
|---------|-----|
| `auth` | https://auth-production-7c22.up.railway.app |
| `catalogo` | https://catalogo-production-7085.up.railway.app |
| `inventario` | https://inventario-production-655e.up.railway.app |
| `notificaciones` | https://notificaciones-production-3b10.up.railway.app |
| `ordenes` | https://ordenes-production-c18d.up.railway.app |

## Running a Service

### Local

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

### Local (Docker)

```
URL:      jdbc:postgresql://localhost:5432/ecommerce_frank
User:     frank / Password: 123456
DDL mode: update (auto-creates tables from JPA entities)
```

### Production (Supabase)

All services connect to a shared Supabase PostgreSQL instance. Credentials are configured via Railway environment variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:6543/postgres?sslmode=require&prepareThreshold=0
SPRING_DATASOURCE_USERNAME=postgres.<project-ref>
SPRING_DATASOURCE_PASSWORD=<password>
```

### Deploying (Railway)

1. Push changes to GitHub (`git push origin master`)
2. Railway auto-deploys OR trigger manually:
   ```powershell
   $env:RAILWAY_API_TOKEN = "<token>"
   railway redeploy -s <service> -e production -y --from-source
   ```

Railway projects: `cozy-endurance` (ordenes) and `patient-gratitude` (auth, catalogo, inventario, notificaciones).

Each service has a `Procfile` with the explicit start command and `CorsConfig.java` for cross-origin requests from the frontend.

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

- **`catalogo` now has DB config** in `application.properties` using `${DATABASE_URL}` env vars (fixed from original empty config).
- **Credentials are externalized** via environment variables (`DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD`) with local-development defaults. Do not commit real credentials to `application.properties`.
- **Java toolchain was removed** from `build.gradle` files -- Gradle uses the system JDK (required for Railway compatibility).
- **Root `.gitignore` only covers `auth/build/` and `catalogo/target/`** -- other service build dirs rely on per-service `.gitignore`.
- **No CI/CD** -- no GitHub Actions, Jenkinsfile, or Dockerfiles exist.
- **Inconsistent versions** -- Spring Boot (4.0.4/4.0.5/4.0.6) and Gradle (9.4/9.5) vary across services.
