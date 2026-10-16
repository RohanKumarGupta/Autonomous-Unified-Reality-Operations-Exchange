# AURA-X — API Architecture

## API Design Principles

1. **RESTful** — Resource-oriented URLs, standard HTTP methods
2. **Versioned** — `/api/v1/` prefix on all endpoints
3. **Consistent** — Standardized response structure across all services
4. **Tenant-scoped** — Tenant ID resolved from JWT, enforced on every query
5. **Paginated** — Cursor-based or offset pagination on all list endpoints
6. **Validated** — Request body validation with clear error messages
7. **Documented** — OpenAPI 3.0 specification per service
8. **Traced** — Correlation ID on every request/response

---

## Standard Response Structure

### Success Response
```json
{
  "success": true,
  "data": { },
  "meta": {
    "timestamp": "2026-10-08T10:00:00Z",
    "correlationId": "uuid",
    "requestId": "uuid"
  }
}
```

### Paginated Response
```json
{
  "success": true,
  "data": [ ],
  "pagination": {
    "page": 1,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "hasNext": true,
    "hasPrevious": false
  },
  "meta": {
    "timestamp": "2026-10-08T10:00:00Z",
    "correlationId": "uuid"
  }
}
```

### Error Response
```json
{
  "success": false,
  "error": {
    "code": "ASSET_NOT_FOUND",
    "message": "Asset with ID 'uuid' not found",
    "status": 404,
    "details": [ ],
    "path": "/api/v1/assets/uuid",
    "timestamp": "2026-10-08T10:00:00Z",
    "correlationId": "uuid"
  }
}
```

### Validation Error Response
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "Request validation failed",
    "status": 400,
    "details": [
      {
        "field": "name",
        "message": "Name is required",
        "rejectedValue": null
      }
    ],
    "correlationId": "uuid"
  }
}
```

---

## Service API Specifications

### Identity Service — Port 8081

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Authenticate user |
| POST | `/api/v1/auth/refresh` | Refresh JWT token |
| POST | `/api/v1/auth/logout` | Invalidate session |
| GET | `/api/v1/users/me` | Get current user profile |
| PUT | `/api/v1/users/me` | Update current user profile |
| GET | `/api/v1/users` | List users (admin) |
| GET | `/api/v1/users/{id}` | Get user by ID |
| PUT | `/api/v1/users/{id}/roles` | Assign roles to user |
| GET | `/api/v1/roles` | List roles |
| POST | `/api/v1/roles` | Create role |
| GET | `/api/v1/permissions` | List permissions |

### Tenant Service — Port 8082

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/organizations` | Create organization |
| GET | `/api/v1/organizations/{id}` | Get organization |
| PUT | `/api/v1/organizations/{id}` | Update organization |
| GET | `/api/v1/organizations/{id}/subscription` | Get subscription |
| PUT | `/api/v1/organizations/{id}/subscription` | Update subscription |
| GET | `/api/v1/plans` | List available plans |
| GET | `/api/v1/organizations/{id}/usage` | Get usage metrics |
| GET | `/api/v1/organizations/{id}/features` | Get feature flags |
| PUT | `/api/v1/organizations/{id}/features/{key}` | Toggle feature flag |

### Asset Service — Port 8083

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Sites** | | |
| GET | `/api/v1/sites` | List sites |
| POST | `/api/v1/sites` | Create site |
| GET | `/api/v1/sites/{id}` | Get site details |
| PUT | `/api/v1/sites/{id}` | Update site |
| DELETE | `/api/v1/sites/{id}` | Deactivate site |
| **Buildings** | | |
| GET | `/api/v1/sites/{siteId}/buildings` | List buildings in site |
| POST | `/api/v1/buildings` | Create building |
| GET | `/api/v1/buildings/{id}` | Get building details |
| PUT | `/api/v1/buildings/{id}` | Update building |
| **Floors** | | |
| GET | `/api/v1/buildings/{buildingId}/floors` | List floors |
| POST | `/api/v1/floors` | Create floor |
| GET | `/api/v1/floors/{id}` | Get floor details |
| **Rooms** | | |
| GET | `/api/v1/floors/{floorId}/rooms` | List rooms |
| POST | `/api/v1/rooms` | Create room |
| GET | `/api/v1/rooms/{id}` | Get room details |
| **Assets** | | |
| GET | `/api/v1/assets` | List assets (filterable) |
| POST | `/api/v1/assets` | Create asset |
| GET | `/api/v1/assets/{id}` | Get asset details |
| PUT | `/api/v1/assets/{id}` | Update asset |
| DELETE | `/api/v1/assets/{id}` | Decommission asset |
| GET | `/api/v1/assets/{id}/sensors` | List sensors for asset |
| GET | `/api/v1/assets/{id}/dependencies` | Get asset dependencies |
| GET | `/api/v1/assets/{id}/maintenance-history` | Get maintenance history |
| GET | `/api/v1/assets/{id}/health` | Get asset health score |
| GET | `/api/v1/assets/{id}/timeline` | Get asset event timeline |
| **Sensors** | | |
| POST | `/api/v1/sensors` | Register sensor |
| GET | `/api/v1/sensors/{id}` | Get sensor details |
| GET | `/api/v1/sensors/{id}/readings` | Get sensor readings (time range) |
| PUT | `/api/v1/sensors/{id}/thresholds` | Update sensor thresholds |
| **Digital Twin** | | |
| GET | `/api/v1/digital-twin/{siteId}` | Get full site hierarchy |
| GET | `/api/v1/digital-twin/{siteId}/health` | Get site health overview |

### Incident Service — Port 8084

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/incidents` | List incidents (filterable) |
| POST | `/api/v1/incidents` | Create incident |
| GET | `/api/v1/incidents/{id}` | Get incident details |
| PUT | `/api/v1/incidents/{id}` | Update incident |
| PUT | `/api/v1/incidents/{id}/assign` | Assign incident |
| PUT | `/api/v1/incidents/{id}/severity` | Update severity |
| PUT | `/api/v1/incidents/{id}/resolve` | Resolve incident |
| PUT | `/api/v1/incidents/{id}/close` | Close incident |
| GET | `/api/v1/incidents/{id}/timeline` | Get incident timeline |
| GET | `/api/v1/incidents/{id}/related` | Get related incidents |
| **Work Orders** | | |
| GET | `/api/v1/incidents/{id}/work-orders` | List work orders |
| POST | `/api/v1/work-orders` | Create work order |
| PUT | `/api/v1/work-orders/{id}` | Update work order |
| PUT | `/api/v1/work-orders/{id}/complete` | Complete work order |
| **Technicians** | | |
| GET | `/api/v1/technicians` | List technicians |
| GET | `/api/v1/technicians/available` | Get available technicians |
| **Vendors** | | |
| GET | `/api/v1/vendors` | List vendors |
| GET | `/api/v1/vendors/{id}/contracts` | Get vendor contracts |
| **SLA** | | |
| GET | `/api/v1/sla-policies` | List SLA policies |
| POST | `/api/v1/sla-policies` | Create SLA policy |

### Workflow Service — Port 8085

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/workflows` | List workflows |
| POST | `/api/v1/workflows` | Create workflow |
| GET | `/api/v1/workflows/{id}` | Get workflow definition |
| PUT | `/api/v1/workflows/{id}` | Update workflow |
| POST | `/api/v1/workflows/{id}/publish` | Publish workflow version |
| POST | `/api/v1/workflows/{id}/execute` | Execute workflow |
| GET | `/api/v1/workflow-executions` | List executions |
| GET | `/api/v1/workflow-executions/{id}` | Get execution details |
| GET | `/api/v1/workflow-executions/{id}/steps` | Get execution steps |
| **Approvals** | | |
| GET | `/api/v1/approvals` | List pending approvals |
| GET | `/api/v1/approvals/{id}` | Get approval details |
| POST | `/api/v1/approvals/{id}/approve` | Approve request |
| POST | `/api/v1/approvals/{id}/reject` | Reject request |

### Event Service — Port 8086

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/events/ingest` | Ingest event (single) |
| POST | `/api/v1/events/ingest/batch` | Ingest events (batch) |
| GET | `/api/v1/events` | Query event history |
| GET | `/api/v1/events/{id}` | Get event details |
| **SSE** | | |
| GET | `/api/v1/events/stream` | SSE stream for live events |

### AI Orchestrator Service — Port 8087

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/ai/execute` | Start AI agent workflow |
| GET | `/api/v1/ai/executions` | List agent executions |
| GET | `/api/v1/ai/executions/{id}` | Get execution details |
| GET | `/api/v1/ai/executions/{id}/replay` | Get execution replay data |
| GET | `/api/v1/ai/executions/{id}/evidence` | Get evidence chain |
| GET | `/api/v1/ai/executions/{id}/explanation` | Get decision explanation |
| GET | `/api/v1/ai/agents` | List available agents |
| GET | `/api/v1/ai/agents/{type}/tools` | List agent tools |
| GET | `/api/v1/ai/tools` | List all tools |
| **SSE** | | |
| GET | `/api/v1/ai/executions/{id}/stream` | SSE stream for execution |

### Knowledge Service — Port 8088

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/knowledge/documents` | Upload document |
| GET | `/api/v1/knowledge/documents` | List documents |
| GET | `/api/v1/knowledge/documents/{id}` | Get document |
| POST | `/api/v1/knowledge/search` | Semantic search (RAG) |
| GET | `/api/v1/knowledge/graph/nodes` | Query knowledge graph |
| GET | `/api/v1/knowledge/graph/nodes/{id}` | Get node details |
| GET | `/api/v1/knowledge/graph/nodes/{id}/relationships` | Get relationships |
| GET | `/api/v1/knowledge/evidence/{executionId}` | Get evidence chain |
| GET | `/api/v1/knowledge/memory` | Search organizational memory |
| POST | `/api/v1/knowledge/memory` | Store organizational knowledge |

### Simulation Service — Port 8089

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/simulations` | Run simulation |
| GET | `/api/v1/simulations/{id}` | Get simulation results |
| POST | `/api/v1/simulations/compare` | Compare multiple plans |
| POST | `/api/v1/simulations/what-if` | Run what-if scenario |
| GET | `/api/v1/simulations/history` | List past simulations |

### Notification Service — Port 8090

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/notifications/send` | Send notification |
| GET | `/api/v1/notifications` | List notifications for user |
| PUT | `/api/v1/notifications/{id}/read` | Mark as read |
| GET | `/api/v1/notifications/preferences` | Get user preferences |
| PUT | `/api/v1/notifications/preferences` | Update preferences |

### Audit Service — Port 8091

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/audit` | Query audit records (filterable) |
| GET | `/api/v1/audit/{id}` | Get audit record details |
| GET | `/api/v1/audit/timeline/{resourceId}` | Get resource audit timeline |
| GET | `/api/v1/audit/export` | Export audit records (CSV/PDF) |

### Analytics Service — Port 8092

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/analytics/dashboard` | Get dashboard KPIs |
| GET | `/api/v1/analytics/incidents` | Incident analytics |
| GET | `/api/v1/analytics/assets` | Asset analytics |
| GET | `/api/v1/analytics/sla` | SLA compliance analytics |
| GET | `/api/v1/analytics/ai` | AI performance analytics |
| GET | `/api/v1/analytics/costs` | Cost analytics |
| GET | `/api/v1/analytics/trends` | Trend analysis |

---

## API Gateway Routing (Port 8080)

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: identity-service
          uri: lb://identity-service
          predicates:
            - Path=/api/v1/auth/**, /api/v1/users/**, /api/v1/roles/**, /api/v1/permissions/**
        - id: tenant-service
          uri: lb://tenant-service
          predicates:
            - Path=/api/v1/organizations/**, /api/v1/plans/**
        - id: asset-service
          uri: lb://asset-service
          predicates:
            - Path=/api/v1/sites/**, /api/v1/buildings/**, /api/v1/floors/**, /api/v1/rooms/**, /api/v1/assets/**, /api/v1/sensors/**, /api/v1/digital-twin/**
        - id: incident-service
          uri: lb://incident-service
          predicates:
            - Path=/api/v1/incidents/**, /api/v1/work-orders/**, /api/v1/technicians/**, /api/v1/vendors/**, /api/v1/sla-policies/**
        - id: workflow-service
          uri: lb://workflow-service
          predicates:
            - Path=/api/v1/workflows/**, /api/v1/workflow-executions/**, /api/v1/approvals/**
        - id: event-service
          uri: lb://event-service
          predicates:
            - Path=/api/v1/events/**
        - id: ai-orchestrator
          uri: lb://ai-orchestrator-service
          predicates:
            - Path=/api/v1/ai/**
        - id: knowledge-service
          uri: lb://knowledge-service
          predicates:
            - Path=/api/v1/knowledge/**
        - id: simulation-service
          uri: lb://simulation-service
          predicates:
            - Path=/api/v1/simulations/**
        - id: notification-service
          uri: lb://notification-service
          predicates:
            - Path=/api/v1/notifications/**
        - id: audit-service
          uri: lb://audit-service
          predicates:
            - Path=/api/v1/audit/**
        - id: analytics-service
          uri: lb://analytics-service
          predicates:
            - Path=/api/v1/analytics/**
```

---

## Query Parameters

### Pagination
```
?page=0&size=20&sort=createdAt,desc
```

### Filtering
```
?status=OPEN&severity=P1&assetId=uuid&from=2026-01-01&to=2026-12-31
```

### Search
```
?q=pump+failure
```

---

## HTTP Headers

### Request Headers
```
Authorization: Bearer <jwt>
X-Tenant-Id: <resolved from JWT>
X-Correlation-Id: <uuid, auto-generated if absent>
X-Request-Id: <uuid>
Content-Type: application/json
Accept: application/json
```

### Response Headers
```
X-Correlation-Id: <uuid>
X-Request-Id: <uuid>
X-Rate-Limit-Remaining: 95
X-Rate-Limit-Reset: 1696752000
```
