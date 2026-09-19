# Garden Planner & Manager — Implementation Plan

**Document:** `IMPLEMENTATION_PLAN.md`  
**Version:** 1.0 consolidated baseline  
**Status:** Pre-development baseline  
**Date:** August 2026

## 1. Build strategy

Build in dependency order. Stabilise the data model and persistence before implementing intelligence. Validate historical preservation continuously rather than at the end.

## 2. Phase 0 — Project foundation

**Goal:** establish the development skeleton and conventions.

Deliver:

- native Android Kotlin project;
- Jetpack Compose and Navigation;
- Room/SQLite and migration mechanism;
- Coroutines/Flow;
- Hilt or similarly lightweight DI;
- project/module structure;
- canonical vocabulary mechanism;
- localisation-ready resources;
- common IDs, dates, units, errors and validation;
- logging and test harness;
- file/import-export foundation;
- location/map foundation sufficient to prove technology choices;
- basic design system and accessibility foundation.

**Exit:** project builds, launches, tests run, local DB opens, core architectural boundaries are enforceable.

## 3. Phase 1 — Data and persistence foundation

**Goal:** make user data durable.

Implement the approved V1 schema around:

`Garden, Area, GrowingSpace, SpatialObject, Plant, PlantAlias, Cultivar, PlantInstance, Activity, Observation, Measurement, SoilRecord, Problem, Intervention, Harvest, HarvestLoss, Plan, Recommendation, RecommendationFactor, RecommendationEvidence, UserDecision, Experiment, InboxItem, Source`.

Also implement:

- stable IDs;
- timestamps/status fields;
- relationships and referential integrity;
- repository interfaces;
- transactions;
- explicit migrations;
- historical preservation;
- test database fixtures.

**Exit:** core entities and relationships survive create/update/archive/migration tests without data loss.

## 4. Phase 2 — Garden and spatial foundation

**Goal:** let the user build a useful physical model of the garden.

Implement:

- garden setup;
- location and seasonal context;
- growing spaces;
- dimensions and characteristics;
- geometry and spatial objects;
- map pan/zoom/select/create/move/edit;
- layer visibility and basic filters;
- historical spatial queries.

**Exit:** a user can create and edit a garden and spaces, place/associate objects, and retain relevant history.

## 5. Phase 3 — Plant knowledge and My Plants

**Goal:** connect general plant knowledge to user-specific plant instances.

Implement:

- Plant Library;
- search/browse/detail;
- minimum V1 knowledge fields;
- cultivars;
- sources/provenance;
- My Plants;
- add-plant flow;
- planned/actual planting record.

Knowledge dataset can remain intentionally small at launch.

**Exit:** a user can find a plant, inspect it, add it to a space, and distinguish it from general Plant Library knowledge.

## 6. Phase 4 — Activities, observations, harvests and history

**Goal:** make the app a practical daily garden notebook.

Implement:

- reusable Activity model;
- Quick Add;
- context inheritance;
- planned vs actual activity state;
- recurrence and partial completion;
- structured observations;
- measurements;
- soil records and estimate/measurement distinction;
- harvests and losses;
- history queries/views;
- corrections/archival rules.

**Exit:** ordinary garden events can be recorded quickly and the user can later reconstruct what happened.

## 7. Phase 5 — Planner and recommendation engine

**Goal:** turn trusted garden data into useful decision support.

Implement the engine as a pure/domain service with deterministic rule evaluation.

Sequence:

1. define structured recommendation input/output contracts;
2. implement factor evaluators;
3. aggregate factor results;
4. calculate suitability and confidence separately;
5. support conditional/possible/insufficient-information outcomes;
6. generate structured explanations;
7. include personal history/preferences where specified;
8. persist recommendation results where required for reproducibility/history;
9. implement Plan/UserDecision integration;
10. add "Do nothing" outcomes.

**Exit:** recommendations are deterministic, explainable, testable and user-overridable.

## 8. Phase 6 — Home and Inbox

**Goal:** make the product useful between data-entry sessions.

Implement:

- Home priorities and current context;
- Inbox generation/presentation;
- action/warning/information categories;
- priority levels;
- mute vs dismiss;
- explanation for important items;
- useful empty-state guidance.

**Exit:** Home answers "What matters now?" and Inbox surfaces useful work without overwhelming the user.

## 9. Phase 7 — Import/export and data safety

**Goal:** make the data portable and protect against loss.

Implement:

- canonical export;
- versioned interchange format;
- import validation and preview;
- conflict detection;
- cancellation before mutation;
- transactional application of accepted imports;
- result reporting;
- backup/restore behaviour where supported by the platform design;
- destructive-action warnings;
- round-trip tests.

**Exit:** exported data can be validated/imported without silent loss or corruption.

## 10. Phase 8 — Integration and V1 hardening

**Goal:** convert working modules into a reliable product.

End-to-end workflows:

- new garden;
- planting;
- planning and recommendation;
- daily activity;
- observation;
- harvest;
- soil observation/estimate/measurement;
- history review;
- import/export.

Hardening:

- migration matrix;
- offline verification;
- accessibility pass;
- performance pass;
- error-state review;
- empty-state review;
- recommendation explanation review;
- release checklist;
- data-loss regression tests.

**Exit:** V1 acceptance criteria are met and no known release-blocking data-integrity defects remain.

## 11. Work that may proceed in parallel

Once Phase 0 boundaries are stable, the following can proceed in parallel where practical:

- vocabulary/reference-data preparation;
- plant knowledge data curation;
- design system/UI component work;
- migration fixtures;
- recommendation test scenarios;
- import/export format examples;
- accessibility test preparation.

Feature implementation itself should still respect data dependencies.

## 12. Definition of done for every feature

A feature is not complete until:

1. its scope is explicit;
2. authoritative data ownership is clear;
3. business rules are outside the UI;
4. unknown/error states are handled;
5. history implications are addressed;
6. repositories/use cases are tested as appropriate;
7. critical UI flow is tested;
8. offline behaviour is verified where applicable;
9. documentation/decision changes are recorded;
10. no unrelated module has been coupled unnecessarily.

## 13. Recommended first coding slice

Do not begin with the map or recommendation engine. Begin with a thin vertical foundation:

`Create Garden → Create Growing Space → Persist → Reload → Verify history-safe edit`

Once that passes, expand the same path into plants and activities. This validates the most important architectural assumptions before complexity increases.
