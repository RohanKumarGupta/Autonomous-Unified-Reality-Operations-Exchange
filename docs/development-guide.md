# AURA-X — Development Guide

## Prerequisites

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 21+ | Backend services |
| Maven | 3.9+ | Build tool |
| Node.js | 20+ | Frontend |
| pnpm | 9+ | Frontend package manager |
| Docker | 24+ | Containerization |
| Docker Compose | 2.20+ | Local orchestration |
| Git | 2.40+ | Version control |

---

## Local Development Setup

### 1. Clone Repository
```bash
git clone <repo-url>
cd aura-x
```

### 2. Start Infrastructure
```bash
# Start all databases, Kafka, and observability stack
docker compose -f docker-compose.infra.yml up -d
```

This starts:
- PostgreSQL (port 5432)
- MongoDB (port 27017)
- Redis (port 6379)
- Neo4j (port 7474/7687)
- Apache Kafka + Zookeeper (port 9092)
- Kafka UI (port 8090)
- Prometheus (port 9090)
- Grafana (port 3001)

### 3. Build All Services
```bash
# From root directory
mvn clean install -DskipTests
```

### 4. Run Services
```bash
# Option A: Run all services
docker compose up

# Option B: Run individual service for development
cd services/identity-service
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 5. Start Frontend
```bash
cd frontend
pnpm install
pnpm dev
```

### 6. Seed Demo Data
```bash
# After all services are running
./scripts/seed-demo-data.sh
```

---

## Environment Variables

### Common (All Services)
```env
SPRING_PROFILES_ACTIVE=local
EUREKA_CLIENT_ENABLED=false

# PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/aurax_{service}
SPRING_DATASOURCE_USERNAME=aurax
SPRING_DATASOURCE_PASSWORD=aurax_local

# MongoDB
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/aurax_{service}

# Redis
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATA_REDIS_PORT=6379

# Kafka
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Observability
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,prometheus,metrics
MANAGEMENT_TRACING_SAMPLING_PROBABILITY=1.0
```

### AI Orchestrator Specific
```env
# LLM Configuration
AURAX_AI_PROVIDER=openai
AURAX_AI_OPENAI_API_KEY=sk-...
AURAX_AI_OPENAI_MODEL=gpt-4o
AURAX_AI_OPENAI_BASE_URL=https://api.openai.com/v1

# Alternative: Ollama (local)
# AURAX_AI_PROVIDER=ollama
# AURAX_AI_OLLAMA_BASE_URL=http://localhost:11434
# AURAX_AI_OLLAMA_MODEL=llama3.1

# Alternative: Anthropic
# AURAX_AI_PROVIDER=anthropic
# AURAX_AI_ANTHROPIC_API_KEY=sk-...
# AURAX_AI_ANTHROPIC_MODEL=claude-sonnet-4-20250514
```

### Identity Service Specific
```env
AURAX_JWT_SECRET=<32-byte-base64-encoded-secret>
AURAX_JWT_EXPIRATION=3600000
AURAX_JWT_REFRESH_EXPIRATION=86400000
```

---

## Service Ports

| Service | HTTP Port | Debug Port |
|---------|-----------|------------|
| API Gateway | 8080 | 5000 |
| Identity Service | 8081 | 5001 |
| Tenant Service | 8082 | 5002 |
| Asset Service | 8083 | 5003 |
| Incident Service | 8084 | 5004 |
| Workflow Service | 8085 | 5005 |
| Event Service | 8086 | 5006 |
| AI Orchestrator | 8087 | 5007 |
| Knowledge Service | 8088 | 5008 |
| Simulation Service | 8089 | 5009 |
| Notification Service | 8090 | 5010 |
| Audit Service | 8091 | 5011 |
| Analytics Service | 8092 | 5012 |
| Frontend | 3000 | — |

---

## Database Setup

### PostgreSQL
Each service gets its own database:
```sql
CREATE DATABASE aurax_identity;
CREATE DATABASE aurax_tenant;
CREATE DATABASE aurax_asset;
CREATE DATABASE aurax_incident;
CREATE DATABASE aurax_workflow;
CREATE DATABASE aurax_audit;
CREATE DATABASE aurax_analytics;
```

Migrations are managed by **Flyway** and run automatically on service startup.

### MongoDB
Collections are created automatically by Spring Data MongoDB.

### Neo4j
Graph schema is initialized via knowledge-service startup scripts.

### pgvector
Extension is enabled in the knowledge database:
```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

---

## Maven Module Structure

```
aura-x/
├── pom.xml                          # Parent POM
├── services/
│   ├── aurax-common/                # Shared library
│   │   ├── pom.xml
│   │   └── src/
│   │       └── main/java/com/aurax/common/
│   │           ├── dto/             # Shared DTOs
│   │           ├── event/           # Event envelope, base events
│   │           ├── exception/       # Common exceptions
│   │           ├── security/        # JWT filter, security config
│   │           ├── web/             # Response wrappers, pagination
│   │           └── config/          # Common configurations
│   ├── identity-service/
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/
│   │       │   ├── java/com/aurax/identity/
│   │       │   │   ├── AuraxIdentityApplication.java
│   │       │   │   ├── config/
│   │       │   │   ├── controller/
│   │       │   │   ├── dto/
│   │       │   │   ├── entity/
│   │       │   │   ├── mapper/
│   │       │   │   ├── repository/
│   │       │   │   ├── service/
│   │       │   │   ├── security/
│   │       │   │   └── exception/
│   │       │   └── resources/
│   │       │       ├── application.yml
│   │       │       ├── application-local.yml
│   │       │       └── db/migration/
│   │       └── test/
│   ├── tenant-service/              # Same structure
│   ├── asset-service/               # Same structure
│   ├── incident-service/            # Same structure
│   ├── workflow-service/            # Same structure
│   ├── event-service/               # Same structure
│   ├── ai-orchestrator-service/     # Same structure
│   ├── knowledge-service/           # Same structure
│   ├── simulation-service/          # Same structure
│   ├── notification-service/        # Same structure
│   ├── audit-service/               # Same structure
│   ├── analytics-service/           # Same structure
│   └── api-gateway/                 # Spring Cloud Gateway
├── frontend/
│   ├── package.json
│   ├── next.config.js
│   ├── tailwind.config.ts
│   └── src/
│       ├── app/                     # Next.js App Router
│       ├── components/              # UI components
│       ├── lib/                     # Utilities
│       ├── hooks/                   # Custom hooks
│       ├── stores/                  # Zustand stores
│       ├── types/                   # TypeScript types
│       └── styles/                  # Global styles
├── infrastructure/
│   ├── docker/
│   ├── kubernetes/
│   ├── helm/
│   └── terraform/
├── docs/
├── scripts/
└── test/
```

---

## Coding Standards

### Java
- Follow Google Java Style Guide
- Use `final` for immutable variables
- Use `record` for DTOs
- Use `sealed interface` for domain events
- Use `Optional` — never return null
- Max method length: 30 lines
- Max class length: 300 lines

### Naming Conventions
```
Entity:       Asset, Incident, WorkOrder
DTO:          AssetResponse, CreateAssetRequest
Service:      AssetService, AssetServiceImpl
Repository:   AssetRepository
Controller:   AssetController
Mapper:       AssetMapper
Event:        AssetCreatedEvent
Exception:    AssetNotFoundException
Config:       SecurityConfig, KafkaConfig
```

### Package Structure (per service)
```
com.aurax.{service}/
├── config/           # Spring configurations
├── controller/       # REST controllers
├── dto/
│   ├── request/      # Request DTOs
│   └── response/     # Response DTOs
├── entity/           # JPA/Mongo entities
├── mapper/           # Entity ↔ DTO mappers
├── repository/       # Data access
├── service/          # Business logic interfaces
│   └── impl/         # Business logic implementations
├── event/
│   ├── producer/     # Kafka producers
│   └── consumer/     # Kafka consumers
├── exception/        # Domain exceptions
├── security/         # Service-specific security
└── util/             # Utilities
```

---

## Testing Strategy

### Unit Tests
```bash
mvn test
```

### Integration Tests (requires Docker)
```bash
mvn verify -P integration-test
```

### Frontend Tests
```bash
cd frontend
pnpm test
pnpm test:e2e
```

---

## Git Conventions

### Branch Naming
```
feature/{service}/{description}
bugfix/{service}/{description}
hotfix/{description}
release/{version}
```

### Commit Messages
```
feat(asset-service): add sensor threshold configuration API
fix(incident-service): correct SLA breach calculation
docs: update API architecture documentation
refactor(common): extract event envelope to shared module
test(ai-orchestrator): add supervisor agent integration tests
```
