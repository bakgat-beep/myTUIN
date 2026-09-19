# Garden Planner & Manager — Architecture Plan

**Document:** `ARCHITECTURE_PLAN.md`  
**Version:** 1.0 consolidated baseline  
**Status:** Pre-development baseline  
**Date:** August 2026

## 1. Architectural objective

Build a durable local garden system first; add intelligence and integrations around it.

The architecture must preserve the distinction between:

> gardener-recorded fact → observation/measurement → inference → recommendation → user decision → actual activity → outcome

No stage silently becomes another stage.

## 2. Authoritative architectural decisions

### Platform decision
The consolidated baseline uses the **Phase 0 native Android stack**:

- Kotlin
- Jetpack Compose
- Android Jetpack
- Room over SQLite
- Kotlin Coroutines and Flow
- Android Studio / Gradle
- Hilt unless Phase 0 implementation identifies a concrete reason to change it

V1_TECHNICAL_ARCHITECTURE.md has been corrected to reflect this stack (see DEC-038). The Flutter/Dart content is removed. No remaining specification document conflicts with the platform decision.

### Architecture decision
Use a layered, modular monolith rather than microservices or a distributed architecture.

```text
Presentation
    ↓
Application
    ↓
Domain
    ↓
Infrastructure / Persistence
```

Infrastructure implementations may depend inward on domain/application contracts; the domain must not depend on Compose, Room or Android UI classes.

## 3. Logical layers

### Presentation
Owns Compose screens, navigation, UI state, forms, dialogs, maps, accessibility and rendering.

**Must not own:** persistence, recommendation rules or durable business logic.

### Application
Owns user-facing workflows, commands, queries, orchestration, validation coordination and transaction boundaries.

Typical use cases:
`CreateGarden`, `AddGrowingSpace`, `AddPlant`, `RecordActivity`, `RecordObservation`, `RecordHarvest`, `PlanPlanting`, `GetRecommendations`, `ExportGarden`, `ImportGarden`.

### Domain
Owns entities, value objects, business rules, planning logic, recommendation logic, history semantics, vocabulary semantics, provenance and information-state rules.

The domain is as Android-independent as practical.

### Infrastructure
Owns Room/SQLite persistence, repositories, file access, import/export, search indexes, notifications and Android platform services.

## 4. Feature/module architecture

Principal modules:

- Core/Foundation
- Garden
- Plants/Plant Knowledge
- Garden Records / Plant Instances
- Activities
- Observations
- Soil & Conditions
- Problems/Diagnostics
- Planning
- Recommendations
- History
- Inbox
- Experiments
- Import/Export

These are **logical ownership boundaries**, not necessarily one package per concept.

### Ownership rule
Every authoritative concept has one owner. Other modules consume it through stable interfaces or shared domain types.

### Dependency rule
No circular feature dependencies. Optional modules must not become prerequisites for core garden use.

## 5. Core data architecture

### Source-of-truth split

**User garden data** is authoritative for what the gardener records.  
**Plant knowledge** is authoritative only for general horticultural knowledge.  
**Recommendations** are derived results.  
**Search indexes/caches** are rebuildable derived data.

General knowledge must never overwrite a user's history.

### Stable identity
Persistent records use stable identifiers independent of display names, translated strings or list position.

### Historical preservation
Prefer append-only/event-like records for things whose history matters. Current-state changes and historical records remain distinguishable.

Soft deletion/archival is preferred where deletion could destroy useful context.

### Unknown/uncertain information
The data model must support explicit states such as unknown, not applicable, measured, observed, estimated and inferred. Missing data must not automatically be interpreted as negative evidence.

## 6. Spatial architecture

Garden owns:

- Garden
- Area
- GrowingSpace
- SpatialObject
- Geometry
- spatial relationships
- garden-level spatial context

Geometry editing must validate and save the current geometry without destroying historical plantings, activities or spatial context.

The map is a presentation of authoritative spatial data, not the authoritative record itself.

## 7. Recommendation architecture

The recommendation engine is a deterministic domain service.

**Inputs:** garden location/context, growing space, plant knowledge, current conditions, plans, rotation/history, personal history and user preferences where applicable.

**Outputs:** candidate result, suitability, confidence, factor results, evidence, missing information, conditions, explanation data and rule/version metadata.

Recommendations are reproducible and inspectable. Changing future rules must not rewrite historical facts or previously stored decisions.

## 8. Planning architecture

Plans represent intended future actions. Actual activities represent what happened.

Completing a plan only creates actual evidence when the user explicitly records that the action occurred.

Recurring plans store the recurrence definition separately from generated occurrences.

Partial completion is represented explicitly.

## 9. Cross-cutting services

Keep these small and shared:

- identity/IDs;
- date/time/season handling;
- units/conversion;
- vocabulary resolution;
- localisation;
- validation/error types;
- configuration;
- logging;
- migration support;
- file handling.

Core must not become a dumping ground for feature-specific logic.

## 10. State and persistence

Compose screens follow the pattern:

`Composable → ViewModel → Use Case → Repository → Room`

Use Flow for reactive database/UI state where it improves clarity.

Repositories hide persistence details. Room entities are not automatically domain objects or UI models.

Database migrations are explicit and deterministic. Import/export compatibility is tested across supported schema versions.

## 11. Import/export boundary

Import/export is a controlled application workflow, not a shortcut into database tables.

```text
file
 ↓
parse
 ↓
validate
 ↓
identify errors/conflicts
 ↓
preview
 ↓
confirm
 ↓
transactional import
 ↓
report
```

Invalid input is never silently imported.

## 12. Search, caching and derived state

Search indexes and caches are derived infrastructure and can be rebuilt from authoritative records. They must never become a second source of truth.

## 13. Events and integration

V1 does not require a general-purpose event bus.

Use application services first. Domain events may be introduced only for meaningful domain occurrences such as `ActivityCompleted`, `ObservationRecorded` or `HarvestRecorded`, and must not become a second persistence mechanism.

## 14. Security and privacy boundary

Core operation should require no external account. Minimise collection of personal/location data and store only information needed for garden functionality. User exports must contain the data necessary to reconstruct the supported garden record.

## 15. Testing architecture

Testing is part of the architecture. Priorities are:

- domain/rule unit tests;
- application/use-case tests;
- repository/database integration tests;
- focused Compose tests;
- end-to-end tests for critical workflows;
- migration tests;
- import/export round-trip tests;
- offline tests;
- recommendation explanation tests;
- historical-preservation tests.

## 16. Architectural invariants

1. Garden data remains authoritative.
2. Knowledge remains separate from personal records.
3. Recommendations remain derived.
4. Plans remain distinct from actual activities.
5. Observations remain distinct from diagnoses.
6. Measurements remain distinct from estimates.
7. History is preserved.
8. User decisions remain user-controlled.
9. Optional services remain optional.
10. UI remains separate from domain logic.
11. Core workflows work offline.
12. Translated UI labels never become data identifiers.
13. Simplicity is preferred over speculative abstraction.
