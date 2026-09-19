# Garden Planner & Manager — One-Page Architecture Overview

**Version:** 1.0 consolidated baseline | **Status:** Pre-development | **Date:** August 2026

## 1. Architecture diagram

```mermaid
flowchart TB
    UI[Presentation\nCompose screens · Navigation · ViewModels · Map] --> APP[Application\nUse cases · Commands · Queries · Orchestration]
    APP --> DOM[Domain\nEntities · Value Objects · Rules · Planning · Recommendations · History semantics]
    DOM --> PORTS[Infrastructure contracts\nRepositories · FileStore · Search · Platform services]
    PORTS --> DATA[Infrastructure\nRoom · SQLite · Files · Local indexes · Android services]

    subgraph FEATURES[Feature modules]
      G[Garden & Spatial]
      P[Plants / Plant Knowledge]
      R[Garden Records]
      A[Activities]
      O[Observations]
      S[Soil & Conditions]
      PR[Problems / Diagnostics]
      PL[Planning]
      RC[Recommendations]
      H[History]
      I[Inbox]
      E[Experiments]
      X[Import / Export]
    end

    DOM --- FEATURES
    EXT[Optional future services\nWeather · Cloud sync · Community · AI] -. optional .-> APP
```

## 2. Domain model / ERD

```mermaid
erDiagram
    GARDEN ||--o{ AREA : contains
    AREA ||--o{ GROWING_SPACE : contains
    GARDEN ||--o{ SPATIAL_OBJECT : contains
    GROWING_SPACE ||--o{ PLANT_INSTANCE : hosts
    PLANT ||--o{ CULTIVAR : has
    PLANT ||--o{ PLANT_ALIAS : has
    PLANT ||--o{ PLANT_INSTANCE : referenced_by
    CULTIVAR ||--o{ PLANT_INSTANCE : optionally_selected
    PLANT_INSTANCE ||--o{ ACTIVITY : has
    PLANT_INSTANCE ||--o{ OBSERVATION : has
    PLANT_INSTANCE ||--o{ HARVEST : produces
    GROWING_SPACE ||--o{ OBSERVATION : has
    GROWING_SPACE ||--o{ SOIL_RECORD : has
    GARDEN ||--o{ ACTIVITY : contains
    GARDEN ||--o{ OBSERVATION : contains
    GARDEN ||--o{ HARVEST : contains
    GARDEN ||--o{ PLAN : contains
    PLAN ||--o{ USER_DECISION : results_in
    GROWING_SPACE ||--o{ PLAN : targets
    PLAN ||--o{ RECOMMENDATION : informs
    RECOMMENDATION ||--o{ RECOMMENDATION_FACTOR : contains
    RECOMMENDATION ||--o{ RECOMMENDATION_EVIDENCE : supported_by
    OBSERVATION ||--o{ PROBLEM : may_support
    EXPERIMENT ||--o{ OBSERVATION : uses
    GARDEN ||--o{ EXPERIMENT : contains
    SOURCE ||--o{ PLANT : documents
    SOURCE ||--o{ RECOMMENDATION_EVIDENCE : supports
    GARDEN ||--o{ INBOX_ITEM : surfaces
```

**Important semantic boundary:** `Plant` is general knowledge; `PlantInstance` is the user's actual plant. `Plan` is intent; `Activity` is actual occurrence. `Observation` is what was observed; `Problem`/diagnosis is an interpretation. `Recommendation` is derived; `UserDecision` is the user's choice.

## 3. Data-flow description

### Read / understand

`Room → Repository → Use Case → ViewModel → Compose`

Garden, plant, activity and historical data are read from the local database. Derived views may combine multiple authoritative records.

### Plan / recommend

`Garden + Space + Plant Knowledge + Conditions + History + Preferences → Recommendation Engine → Structured Recommendation → UI`

The engine does not write user facts. It produces suitability, confidence, factor results, evidence, missing information and explanations.

### Decide / act

`Recommendation/Plan → User Decision → Actual Activity → Outcome/Observation/Harvest → History`

An override is a valid user decision, not an application error.

### Import

`File → Parse → Validate → Conflict analysis → Preview → Confirm → Transaction → Repository → Room`

### Export

`Authoritative records → Validation/serialization → Versioned export file`

## 4. Architectural invariants

```text
User facts       = authoritative
Knowledge        = separate reference information
Recommendations  = derived
Plans            = intended future actions
Activities       = what actually happened
History          = durable record
Search/cache     = rebuildable
UI               = presentation, not business logic
```
