# AURA-X — Kafka Event Architecture

## Event Topology

### Topic Naming Convention
```
aurax.<domain>.<entity>.<action>
```

### Topics

| Topic | Partitions | Retention | Description |
|-------|-----------|-----------|-------------|
| `aurax.identity.user.lifecycle` | 6 | 30d | User created, updated, deleted |
| `aurax.identity.auth.events` | 6 | 7d | Login, logout, token refresh |
| `aurax.tenant.org.lifecycle` | 3 | 30d | Org created, updated, subscription changes |
| `aurax.asset.asset.lifecycle` | 12 | 30d | Asset CRUD events |
| `aurax.asset.asset.state` | 12 | 30d | Asset status/health changes |
| `aurax.asset.sensor.telemetry` | 24 | 7d | Raw sensor readings (high volume) |
| `aurax.asset.sensor.anomaly` | 12 | 30d | Threshold breaches, anomalies |
| `aurax.incident.incident.lifecycle` | 12 | 90d | Incident CRUD events |
| `aurax.incident.incident.assignment` | 6 | 30d | Assignments, escalations |
| `aurax.incident.workorder.lifecycle` | 12 | 90d | Work order CRUD events |
| `aurax.incident.sla.events` | 6 | 90d | SLA breaches, warnings |
| `aurax.workflow.execution.lifecycle` | 12 | 30d | Workflow execution events |
| `aurax.workflow.approval.events` | 6 | 90d | Approval requests, decisions |
| `aurax.ai.agent.execution` | 12 | 30d | Agent execution lifecycle |
| `aurax.ai.agent.tool` | 12 | 30d | Tool invocations |
| `aurax.ai.agent.decision` | 6 | 90d | AI decisions with evidence |
| `aurax.knowledge.document.lifecycle` | 6 | 30d | Document ingestion events |
| `aurax.knowledge.graph.updates` | 6 | 30d | Knowledge graph changes |
| `aurax.simulation.run.lifecycle` | 6 | 30d | Simulation executions |
| `aurax.notification.outbound` | 12 | 7d | Notification requests |
| `aurax.audit.events` | 24 | 365d | All audit events |
| `aurax.analytics.metrics` | 12 | 90d | Metric snapshots |

### Dead Letter Topics
Every topic has a corresponding DLT:
```
aurax.<domain>.<entity>.<action>.dlt
```

---

## Event Envelope

Every event follows this standard envelope:

```json
{
  "eventId": "uuid-v7",
  "eventType": "AssetCreated",
  "version": "1.0",
  "timestamp": "2026-10-08T10:00:00Z",
  "correlationId": "uuid",
  "causationId": "uuid",
  "tenantId": "uuid",
  "actorId": "uuid",
  "actorType": "USER | AGENT | SYSTEM",
  "source": "asset-service",
  "payload": { }
}
```

---

## Key Event Schemas

### AssetCreated
```json
{
  "assetId": "uuid",
  "name": "Pump #17",
  "type": "PUMP",
  "locationId": "uuid",
  "siteId": "uuid",
  "buildingId": "uuid",
  "floorId": "uuid",
  "manufacturer": "ABC Corp",
  "serialNumber": "XYZ-500-17",
  "status": "ACTIVE"
}
```

### TelemetryThresholdBreached
```json
{
  "sensorId": "uuid",
  "assetId": "uuid",
  "metric": "TEMPERATURE",
  "value": 87.3,
  "threshold": 75.0,
  "direction": "ABOVE",
  "severity": "WARNING",
  "readingTimestamp": "2026-10-08T10:41:00Z"
}
```

### IncidentCreated
```json
{
  "incidentId": "uuid",
  "assetId": "uuid",
  "title": "HVAC Anomaly - Floor 4",
  "description": "Temperature threshold breached on Pump #17",
  "severity": "P2",
  "priority": "HIGH",
  "status": "OPEN",
  "source": "AUTOMATED",
  "triggerEventId": "uuid"
}
```

### AgentExecutionStarted
```json
{
  "executionId": "uuid",
  "agentType": "SUPERVISOR",
  "incidentId": "uuid",
  "triggeredBy": "INCIDENT_CREATED",
  "model": "gpt-4o",
  "maxTokenBudget": 50000
}
```

### RiskCalculated
```json
{
  "executionId": "uuid",
  "incidentId": "uuid",
  "riskScore": 0.21,
  "severity": "LOW",
  "financialImpact": 12000,
  "operationalImpact": "LIMITED",
  "safetyRisk": "LOW",
  "blastRadius": ["Floor 4 HVAC", "Building A comfort"],
  "confidence": 0.91,
  "reversible": true,
  "slaImpact": "NONE"
}
```

### PlanGenerated
```json
{
  "executionId": "uuid",
  "incidentId": "uuid",
  "plans": [
    {
      "planId": "uuid",
      "label": "PLAN_A",
      "description": "Replace pressure valve",
      "estimatedCost": 8500,
      "estimatedDowntime": "PT22M",
      "riskLevel": "LOW",
      "successProbability": 0.96,
      "steps": [
        {"order": 1, "action": "ASSIGN_TECHNICIAN", "params": {}},
        {"order": 2, "action": "CHECK_INVENTORY", "params": {"partNumber": "PV-XYZ-500"}},
        {"order": 3, "action": "DISPATCH", "params": {}},
        {"order": 4, "action": "REPLACE_PART", "params": {}}
      ]
    }
  ],
  "recommendation": "PLAN_A",
  "reasoning": "Lowest cost with highest success probability"
}
```

### ApprovalRequested
```json
{
  "approvalId": "uuid",
  "executionId": "uuid",
  "incidentId": "uuid",
  "requestedBy": "SUPERVISOR_AGENT",
  "approverRoles": ["FACILITY_MANAGER"],
  "action": "EXECUTE_PLAN",
  "plan": "PLAN_A",
  "riskLevel": "LOW",
  "estimatedCost": 8500,
  "expiresAt": "2026-10-08T12:00:00Z"
}
```

### ActionExecuted
```json
{
  "executionId": "uuid",
  "incidentId": "uuid",
  "action": "REPLACE_PRESSURE_VALVE",
  "result": "SUCCESS",
  "technicianId": "uuid",
  "workOrderId": "uuid",
  "actualCost": 8500,
  "actualDowntime": "PT22M",
  "completedAt": "2026-10-08T11:07:00Z"
}
```

### VerificationCompleted
```json
{
  "executionId": "uuid",
  "incidentId": "uuid",
  "verificationResult": "PASSED",
  "checks": [
    {"metric": "TEMPERATURE", "expected": "<24", "actual": 22.7, "passed": true},
    {"metric": "VIBRATION", "expected": "<5", "actual": 2.1, "passed": true},
    {"metric": "PRESSURE", "expected": ">30", "actual": 34.2, "passed": true}
  ],
  "confidence": 0.98
}
```

---

## Consumer Groups

| Consumer Group | Service | Topics Consumed |
|---------------|---------|-----------------|
| `aurax-incident-consumer` | incident-service | `aurax.asset.sensor.anomaly` |
| `aurax-ai-consumer` | ai-orchestrator | `aurax.incident.incident.lifecycle` |
| `aurax-workflow-consumer` | workflow-service | `aurax.incident.incident.lifecycle`, `aurax.workflow.approval.events` |
| `aurax-notification-consumer` | notification-service | `aurax.notification.outbound` |
| `aurax-audit-consumer` | audit-service | `aurax.audit.events` (all services publish here) |
| `aurax-analytics-consumer` | analytics-service | `aurax.incident.*`, `aurax.ai.*`, `aurax.asset.*` |
| `aurax-knowledge-consumer` | knowledge-service | `aurax.ai.agent.decision`, `aurax.incident.incident.lifecycle` |
| `aurax-digital-twin-consumer` | asset-service | `aurax.ai.agent.execution`, `aurax.incident.incident.lifecycle` |

---

## Idempotency Strategy

Every consumer implements idempotency using:
1. **Event ID deduplication** — Store processed `eventId` in `processed_events` table (PostgreSQL) or collection (MongoDB)
2. **TTL-based dedup cache** — Redis SET with `eventId` as key, 24h TTL for fast lookup
3. **Upsert semantics** — Where possible, use upsert instead of insert

---

## Retry Strategy

```
Attempt 1: Immediate
Attempt 2: 1 second delay
Attempt 3: 5 seconds delay
Attempt 4: 30 seconds delay
Attempt 5: Dead Letter Topic
```

---

## Event Versioning

Events use semantic versioning in the `version` field. Consumers must handle:
- Same major version: backward compatible
- Different major version: use schema registry or version-specific handler
