# AURA-X

## Autonomous Unified Reality & Operations Exchange

> **From operational intent to verified action.**

AURA-X is an AI-native operational intelligence and execution platform that connects people, assets, workflows, and knowledge with governed AI agents capable of investigating, simulating, executing, and verifying real-world operations.

---

## 🎯 What is AURA-X?

AURA-X creates a **Living Operational Digital Twin** — a unified intelligence layer that transforms fragmented operational data into safe, evidence-backed, risk-aware, and verifiable actions using bounded AI agents.

### The AURA-X Loop

```
OBSERVE → UNDERSTAND → REASON → PLAN → SIMULATE → ASSESS RISK → APPROVE → EXECUTE → VERIFY → RECORD → LEARN
```

### Core Innovation

Unlike simple CRUD apps, chatbots, or generic RAG systems, AURA-X performs **actual workflow orchestration** — converting real-world operational intent and events into governed, auditable actions through a multi-agent AI architecture.

---

## 🏗️ MVP Domain: AI-Powered Facilities & Field Operations

The first production-ready version manages:

- **Physical Infrastructure**: Sites, Buildings, Floors, Rooms, Zones
- **Assets & Equipment**: HVAC, Generators, Pumps, Elevators, Electrical Systems, Fire Safety, CCTV
- **Sensors & Telemetry**: Temperature, Vibration, Pressure, Humidity, Power
- **People**: Technicians, Vendors, Maintenance Contracts
- **Operations**: Incidents, Work Orders, Maintenance Schedules, Spare Parts
- **Intelligence**: Knowledge Graph, Organizational Memory, AI Predictions

---

## 🤖 Multi-Agent Architecture

| Agent | Responsibility |
|-------|---------------|
| **Supervisor** | Intent understanding, task decomposition, agent coordination |
| **Context** | Operational context retrieval across all data sources |
| **Diagnostic** | Root cause analysis with confidence scoring |
| **Risk** | Operational, financial, and safety risk assessment |
| **Planning** | Multi-plan generation with cost/time/risk tradeoffs |
| **Simulation** | Plan simulation and what-if analysis |
| **Execution** | Controlled action execution through tool gateway |
| **Verification** | Outcome verification against expectations |
| **Reporter** | Evidence-backed report generation |
| **Memory** | Organizational knowledge storage and retrieval |

---

## 🏛️ Architecture

### Backend
- **Java 21+** with Spring Boot, Spring Cloud, Spring AI
- **12 Microservices** — independently deployable, domain-aligned
- **Apache Kafka** — event-driven asynchronous communication
- **Polyglot Persistence** — PostgreSQL, MongoDB, Redis, Neo4j, pgvector

### Frontend
- **Next.js** with TypeScript, React, Tailwind CSS, shadcn/ui
- **Real-time** — WebSocket/SSE for live operational updates

### AI
- **Spring AI** — LLM-provider agnostic (OpenAI, Anthropic, Ollama)
- **Tool Gateway** — Policy-checked, permission-enforced, audited tool execution
- **Risk-Based Autonomy** — 5 levels from recommendation-only to bounded autonomy

### Infrastructure
- **Docker & Docker Compose** — local development
- **Kubernetes & Helm** — production deployment
- **OpenTelemetry, Prometheus, Grafana** — observability

---

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Node.js 20+
- Docker & Docker Compose
- Git

### Local Development

```bash
# Clone the repository
git clone <repo-url>
cd aura-x

# Start all infrastructure and services
docker compose up

# The application will be available at:
# Frontend:  http://localhost:3000
# API Gateway: http://localhost:8080
# Grafana:   http://localhost:3001
```

### Demo Data
The system comes pre-loaded with realistic synthetic data:
- 3 organizations, 10 sites, 30 buildings, 100+ assets
- 300+ sensors, 500+ historical incidents
- 100 technicians, 30 vendors
- Knowledge documents and historical AI decisions

---

## 📁 Project Structure

```
aura-x/
├── frontend/                    # Next.js frontend application
├── services/
│   ├── identity-service/        # Authentication, authorization, users
│   ├── tenant-service/          # Multi-tenancy, subscriptions, plans
│   ├── asset-service/           # Assets, locations, sensors, devices
│   ├── incident-service/        # Incidents, work orders, SLA
│   ├── workflow-service/        # Workflow engine, approvals
│   ├── event-service/           # Event ingestion, routing, processing
│   ├── ai-orchestrator-service/ # Multi-agent AI coordination
│   ├── knowledge-service/       # RAG, embeddings, knowledge graph
│   ├── simulation-service/      # Plan simulation, what-if analysis
│   ├── notification-service/    # Multi-channel notifications
│   ├── audit-service/           # Immutable audit trail
│   └── analytics-service/       # KPIs, operational analytics
├── infrastructure/
│   ├── docker/                  # Dockerfiles
│   ├── kubernetes/              # K8s manifests
│   ├── helm/                    # Helm charts
│   └── terraform/               # Infrastructure as Code
├── docs/                        # Architecture & API documentation
├── scripts/                     # Development & deployment scripts
├── test/                        # Integration & E2E tests
├── docker-compose.yml           # Local development environment
└── README.md
```

---

## 🔐 Security

- OAuth2/OIDC with JWT
- Role-Based Access Control (RBAC) + Attribute-Based Access Control (ABAC)
- Multi-tenant data isolation
- Agent permission matrix — bounded authority per agent
- API rate limiting
- Immutable audit logging
- Encrypted secrets management

---

## 📊 Observability

- Distributed tracing with OpenTelemetry
- Metrics with Micrometer + Prometheus
- Dashboards with Grafana
- Structured JSON logging with correlation IDs
- Health checks on all services
- AI-specific metrics: token usage, latency, cost, success rate

---

## 🧪 Testing

- **Unit**: JUnit 5, Mockito
- **Integration**: Spring Boot Test, Testcontainers
- **API**: REST Assured
- **Frontend**: React Testing Library
- **E2E**: Playwright
- **Load**: k6
- **AI**: Scenario-based evaluation datasets

---

## 📋 Development Phases

| Phase | Description | Status |
|-------|-------------|--------|
| 0 | Architecture & Design | 🔄 In Progress |
| 1 | Repository & Dev Environment | ⬜ |
| 2 | Identity & Auth | ⬜ |
| 3 | Multi-tenancy | ⬜ |
| 4 | Asset Management | ⬜ |
| 5 | Incident Management | ⬜ |
| 6 | Kafka Event Architecture | ⬜ |
| 7 | Workflow Engine | ⬜ |
| 8-10 | Knowledge & RAG | ⬜ |
| 11-21 | AI Agents & Orchestration | ⬜ |
| 22-24 | Digital Twin, Replay, Eval Lab | ⬜ |
| 25 | Premium Frontend | ⬜ |
| 26-28 | Observability, Security, Testing | ⬜ |
| 29-34 | DevOps, CI/CD, Production | ⬜ |

---

## 📄 License

Proprietary — All rights reserved.

---

## 🏢 Built With

<p>
  <img src="https://img.shields.io/badge/Java-21+-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3-green?style=flat-square" />
  <img src="https://img.shields.io/badge/Next.js-14-black?style=flat-square" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Kafka-3.7-red?style=flat-square" />
  <img src="https://img.shields.io/badge/Redis-7-red?style=flat-square" />
  <img src="https://img.shields.io/badge/Neo4j-5-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/MongoDB-7-green?style=flat-square" />
</p>
