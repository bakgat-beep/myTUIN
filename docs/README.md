# Garden Planner & Manager — Pre-Coding Specification Package

This package consolidates the supplied August 2026 project documents into four implementation-ready baselines.

## Files

- `PRODUCT_SPECIFICATION.md` — what V1 must do.
- `ARCHITECTURE_PLAN.md` — how the application is structured and the rules that govern it.
- `IMPLEMENTATION_PLAN.md` — the order in which V1 should be built and validated.
- `ARCHITECTURE_OVERVIEW.md` — the one-page architecture, ERD/domain model and data flows.

## Consolidation decisions

1. The Phase 0 native Android technology choice is treated as authoritative for V1: Kotlin, Jetpack Compose, Room/SQLite, Coroutines/Flow and Android Jetpack.
2. Flutter/Dart references in the existing V1 technical architecture are considered stale and should be removed from the project documentation before implementation begins.
3. The conceptual data model, V1 database schema, module architecture, UX/screen specifications, recommendation architecture, import/export strategy, migration strategy, testing strategy and decision log remain the detailed supporting specifications beneath these consolidated documents.
4. The architecture deliberately remains a modular monolith. Future cloud, weather, community and AI capabilities are extension points, not V1 dependencies.
