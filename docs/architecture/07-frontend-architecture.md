# AURA-X — Frontend Architecture

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Next.js | 14+ | Framework (App Router) |
| TypeScript | 5.3+ | Type safety |
| React | 18+ | UI library |
| Tailwind CSS | 3.4+ | Utility-first styling |
| shadcn/ui | Latest | Component library |
| TanStack Query | 5+ | Server state management |
| Zustand | 4+ | Client state management |
| Recharts | 2+ | Charts and data visualization |
| React Flow | 11+ | Workflow builder |
| Lucide React | Latest | Icons |
| Framer Motion | 10+ | Animations |

---

## Design System: AURA-X Design Language

### Color Tokens

```css
/* Brand Colors */
--aurax-primary: #2563EB;       /* Blue 600 */
--aurax-primary-hover: #1D4ED8; /* Blue 700 */
--aurax-accent: #7C3AED;        /* Violet 600 */

/* Semantic Colors */
--aurax-success: #059669;       /* Emerald 600 */
--aurax-warning: #D97706;       /* Amber 600 */
--aurax-danger: #DC2626;        /* Red 600 */
--aurax-info: #0284C7;          /* Sky 600 */

/* Risk Levels */
--aurax-risk-low: #059669;
--aurax-risk-medium: #D97706;
--aurax-risk-high: #DC2626;
--aurax-risk-critical: #7F1D1D;

/* Agent Status */
--aurax-agent-working: #2563EB;
--aurax-agent-completed: #059669;
--aurax-agent-waiting: #D97706;
--aurax-agent-failed: #DC2626;

/* Severity */
--aurax-p1: #DC2626;
--aurax-p2: #EA580C;
--aurax-p3: #D97706;
--aurax-p4: #2563EB;

/* Backgrounds */
--aurax-bg-primary: #FFFFFF;
--aurax-bg-secondary: #F8FAFC;
--aurax-bg-tertiary: #F1F5F9;

/* Dark Mode */
--aurax-dark-bg-primary: #0F172A;
--aurax-dark-bg-secondary: #1E293B;
--aurax-dark-bg-tertiary: #334155;
```

### Typography

```
Heading 1:   text-3xl  font-bold    tracking-tight
Heading 2:   text-2xl  font-semibold tracking-tight
Heading 3:   text-xl   font-semibold
Heading 4:   text-lg   font-medium
Body:        text-sm   font-normal
Caption:     text-xs   font-normal   text-muted-foreground
Mono:        font-mono text-sm
```

Font: **Inter** for UI, **JetBrains Mono** for code/data.

### Spacing Scale
```
4px  (1)    — Tight gaps
8px  (2)    — Element spacing
12px (3)    — Component padding
16px (4)    — Section padding
24px (6)    — Card padding
32px (8)    — Section margins
48px (12)   — Page sections
64px (16)   — Major sections
```

---

## Application Routes

```
/                           → Landing Page
/login                      → Login
/register                   → Registration
/onboarding                 → Organization Setup + Onboarding

/dashboard                  → Command Center
/dashboard/health           → Operational Health

/incidents                  → Incident List
/incidents/[id]             → Incident Details
/incidents/[id]/war-room    → Incident War Room (live)

/assets                     → Asset Explorer
/assets/[id]                → Asset Details
/assets/digital-twin        → Digital Twin Explorer
/assets/digital-twin/[siteId] → Site Digital Twin

/workflows                  → Workflow List
/workflows/builder          → Workflow Builder
/workflows/[id]             → Workflow Details
/workflows/executions       → Execution History
/workflows/executions/[id]  → Execution Details

/ai                         → AI Agent Center
/ai/agents/[type]           → Agent Details
/ai/executions/[id]         → Agent Execution Details
/ai/executions/[id]/replay  → Agent Replay
/ai/evaluation              → AI Evaluation Lab

/simulations                → Simulation Center
/simulations/[id]           → Simulation Results
/simulations/compare        → Plan Comparison

/approvals                  → Approval Center

/knowledge                  → Knowledge Explorer
/knowledge/documents        → Document Library
/knowledge/graph            → Knowledge Graph Visualization
/knowledge/evidence/[id]    → Evidence Explorer
/knowledge/memory           → Organizational Memory

/analytics                  → Analytics Dashboard
/analytics/incidents        → Incident Analytics
/analytics/assets           → Asset Analytics
/analytics/ai               → AI Performance
/analytics/costs            → Cost Analytics
/analytics/sla              → SLA Compliance

/audit                      → Audit Explorer
/audit/[id]                 → Audit Record Details

/notifications              → Notification Center

/settings                   → Organization Settings
/settings/team              → Team Management
/settings/roles             → Role Management
/settings/subscription      → Subscription & Billing
/settings/api               → API / Developer Settings
/settings/notifications     → Notification Preferences
```

---

## Key Page Components

### Command Center (Dashboard)
```
┌─────────────────────────────────────────────────────────────────┐
│  AURA-X Command Center                          [Dark/Light] ⌘K│
├────────────┬──────────────┬────────────────┬────────────────────┤
│ Operations │ Active       │ Critical       │ AI Agent           │
│ Health     │ Incidents    │ Risks          │ Activity           │
│ Score: 94% │ P1: 2  P2: 5│ Assets: 3      │ Running: 2         │
│ ██████████ │ P3: 12      │ Risk > 80%     │ Completed: 47      │
├────────────┴──────────────┴────────────────┴────────────────────┤
│ LIVE INCIDENT FEED                                     [Filter]│
│                                                                 │
│ 10:41  P2  HVAC Anomaly - Floor 4    Investigating    [→ View] │
│ 10:38  P3  Elevator #3 Noise         Assigned         [→ View] │
│ 10:22  P4  Light Flickering - Room 201 Scheduled      [→ View] │
├─────────────────────────────┬───────────────────────────────────┤
│ ASSET HEALTH MAP            │ SLA COMPLIANCE                    │
│                             │                                   │
│ [Interactive Site Map]      │ Response: 98.2% ████████████      │
│                             │ Resolution: 94.7% ██████████      │
│                             │ Breached Today: 1                 │
├─────────────────────────────┼───────────────────────────────────┤
│ MAINTENANCE DUE             │ AI RECOMMENDATIONS                │
│                             │                                   │
│ Today: 3 tasks              │ "Schedule preventive maintenance  │
│ This week: 12 tasks         │  for Generator #5 — similar       │
│ Overdue: 1 task ⚠️          │  pattern to last year's failure"  │
└─────────────────────────────┴───────────────────────────────────┘
```

### Incident War Room
```
┌─────────────────────────────────────────────────────────────────┐
│ ● LIVE    P1 — HVAC Failure - Building A, Floor 4     Risk: 87%│
├─────────────────────────────┬───────────────────────────────────┤
│ LIVE EVENT STREAM           │ AGENTS                            │
│                             │                                   │
│ 10:41  Anomaly detected     │ Supervisor    ● Working           │
│ 10:42  Context Agent ✓      │ Context       ✓ Completed         │
│ 10:43  Root cause: 91%      │ Diagnostic    ✓ Completed         │
│ 10:44  3 plans generated    │ Risk          ✓ Completed         │
│ 10:45  Simulation ✓         │ Planning      ✓ Completed         │
│ 10:46  Approval requested   │ Simulation    ✓ Completed         │
│        ██████ Waiting...    │ Executor      ◐ Waiting           │
├─────────────────────────────┼───────────────────────────────────┤
│ RECOMMENDED PLAN            │ EVIDENCE                          │
│                             │                                   │
│ Replace pressure valve      │ ✓ Sensor telemetry (3 readings)  │
│                             │ ✓ Previous incident (6 months)   │
│ Cost: ₹8,500               │ ✓ Maintenance history            │
│ Downtime: 22 minutes        │ ✓ Manufacturer document          │
│ Risk: LOW                   │                                   │
│ Recovery: 96%               │ [View Full Evidence Chain]        │
│                             │                                   │
│ [APPROVE] [REJECT] [MODIFY] │ [Why this recommendation?]       │
└─────────────────────────────┴───────────────────────────────────┘
```

---

## Real-Time Architecture

### SSE (Server-Sent Events) Connections
```typescript
// Incident War Room — live event stream
EventSource: /api/v1/ai/executions/{id}/stream

// Dashboard — live incident feed
EventSource: /api/v1/events/stream?type=incident

// Agent execution — step-by-step progress
EventSource: /api/v1/ai/executions/{id}/stream
```

### SSE Event Types
```typescript
interface SSEEvent {
  type: 'AGENT_STARTED' | 'AGENT_COMPLETED' | 'TOOL_INVOKED' |
        'RISK_CALCULATED' | 'PLAN_GENERATED' | 'APPROVAL_REQUESTED' |
        'ACTION_EXECUTED' | 'VERIFICATION_COMPLETED' | 'INCIDENT_UPDATE';
  data: Record<string, unknown>;
  timestamp: string;
  correlationId: string;
}
```

---

## State Management

### Server State (TanStack Query)
All API data fetching, caching, and synchronization.

```typescript
// Example: useIncidents hook
const useIncidents = (filters: IncidentFilters) => {
  return useQuery({
    queryKey: ['incidents', filters],
    queryFn: () => incidentApi.list(filters),
    staleTime: 30_000,
    refetchOnWindowFocus: true,
  });
};
```

### Client State (Zustand)
UI-specific state only.

```typescript
// Example: Command palette, sidebar, theme
interface UIStore {
  sidebarOpen: boolean;
  theme: 'light' | 'dark' | 'system';
  commandPaletteOpen: boolean;
  activeFilters: Record<string, unknown>;
}
```

---

## Accessibility Requirements

- WCAG 2.1 AA compliance
- Keyboard navigation for all interactive elements
- Screen reader support
- Focus management
- Color contrast ratios
- Reduced motion support
- ARIA labels on all interactive elements
