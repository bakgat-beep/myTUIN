# Garden Planner & Manager — Phase 0: Project Foundation

**Document:** `docs/PHASE_0_PROJECT_FOUNDATION.md`
**Version:** 0.2
**Status:** Working specification
**Last updated:** August 2026

---

# 1. Purpose

Phase 0 establishes the technical foundation for Garden Planner & Manager before feature development begins.

The goal is to create a clean, testable, offline-first Android application structure that can support the V1 architecture without introducing unnecessary complexity.

Phase 0 does **not** implement the garden planner itself.

It establishes the project on which V1 will be built.

---

# 2. Technology Decision

V1 will be developed as a native Android application using:

* **Kotlin**
* **Jetpack Compose**
* **Android Jetpack**
* **Room**
* **SQLite**
* **Kotlin Coroutines**
* **Kotlin Flow**
* **Android Studio**
* **Gradle**

Android is the primary and intended V1 platform.

Cross-platform support is not a V1 requirement.

---

# 3. Architectural Principles

The implementation must follow these principles.

## 3.1 Local-first

The local database is the primary source of garden data.

The application must remain useful without Internet access.

Connectivity must not be required for:

* viewing the garden;
* viewing plants;
* recording activities;
* recording observations;
* recording harvests;
* viewing history;
* running stored-data recommendations.

---

## 3.2 Domain-first

The garden's concepts and rules must not be defined by the UI.

The application should separate:

```text
UI
Application
Domain
Data
```

The domain should remain as independent of Android UI frameworks as practical.

---

## 3.3 Explainable

Important recommendations and derived information must be traceable to:

* source information;
* garden information;
* rules;
* personal history;
* uncertainty;
* missing information.

The application must not depend on opaque decision-making.

---

## 3.4 Historical preservation

Garden history is valuable data.

The implementation must avoid destructive updates that accidentally erase:

* planting history;
* observations;
* measurements;
* harvests;
* activities;
* experiments;
* recommendations;
* decisions.

---

## 3.5 Progressive complexity

Basic gardening tasks must remain simple.

Advanced functionality should be accessible without being required for ordinary use.

---

## 3.6 Modular design

Modules should communicate through clear interfaces.

A failure or future removal of an optional module should not unnecessarily prevent core garden functionality from working.

---

# 4. V1 Application Architecture

The initial structure should be:

```text
UI
 ↓
Application
 ↓
Domain
 ↓
Data
```

with platform services supporting the appropriate layers.

### UI

Responsible for:

* Jetpack Compose screens;
* navigation;
* user interaction;
* UI state;
* accessibility;
* presentation.

### Application

Responsible for:

* user-facing workflows;
* use cases;
* commands;
* queries;
* coordinating domain operations.

### Domain

Responsible for:

* core entities;
* value objects;
* business rules;
* recommendation logic;
* planning logic;
* vocabulary semantics;
* historical rules.

### Data

Responsible for:

* repositories;
* Room;
* SQLite;
* persistence;
* import/export;
* data mapping.

---

# 5. Android Technology Stack

## 5.1 Kotlin

Kotlin is the primary programming language.

Use modern Kotlin features where they improve correctness and clarity, particularly:

* null safety;
* data classes;
* sealed types;
* immutable data where practical;
* extension functions;
* coroutines;
* Flow.

Avoid unnecessary abstraction or advanced language features merely for sophistication.

---

## 5.2 Jetpack Compose

Jetpack Compose is the V1 UI framework.

Use Compose for:

* screens;
* reusable UI components;
* forms;
* lists;
* dialogs;
* bottom sheets;
* filtering;
* status displays;
* recommendation presentation.

UI components should remain presentation-focused.

Business rules must not be embedded in composables.

---

## 5.3 Navigation

Use Jetpack Navigation for Compose.

Navigation should reflect the application's user experience rather than database entities.

Primary V1 destinations remain:

```text
Home
Garden
Plants
Planner
Inbox
```

with prominent:

```text
+ Add
```

actions.

---

## 5.4 Room and SQLite

Room will provide the primary persistence layer over SQLite.

Room should handle:

* entities;
* relationships;
* queries;
* transactions;
* migrations.

The database schema must follow `V1_DATABASE_SCHEMA.md`.

Database entities must not automatically become UI concepts.

---

## 5.5 Coroutines and Flow

Use Kotlin Coroutines for asynchronous work.

Use Flow where reactive updates are useful, particularly for:

* database observations;
* UI state;
* long-running local operations;
* background processing.

Do not introduce reactive streams where ordinary synchronous code is clearer.

---

# 6. Project Structure

The initial project should be organised around architectural responsibility rather than individual screens.

A suitable structure is:

```text
app/
└── src/main/
    ├── java/.../
    │   ├── GardenPlannerApplication.kt
    │   │
    │   ├── data/
    │   │   ├── database/
    │   │   ├── dao/
    │   │   ├── entities/
    │   │   ├── repositories/
    │   │   └── importexport/
    │   │
    │   ├── domain/
    │   │   ├── model/
    │   │   ├── rules/
    │   │   ├── recommendations/
    │   │   └── usecases/
    │   │
    │   ├── ui/
    │   │   ├── navigation/
    │   │   ├── components/
    │   │   ├── home/
    │   │   ├── garden/
    │   │   ├── plants/
    │   │   ├── planner/
    │   │   └── inbox/
    │   │
    │   └── platform/
    │       ├── location/
    │       ├── notifications/
    │       └── files/
    │
    └── res/
```

The exact package structure may evolve during implementation, provided architectural boundaries remain clear.

---

# 7. Dependency Direction

Dependencies should generally flow inward:

```text
UI
 ↓
Application
 ↓
Domain
 ↓
Data interfaces
```

Infrastructure implementations may depend on domain/application interfaces.

The domain should not depend directly on:

* Compose;
* Android Activities;
* Android Views;
* Room;
* database-specific classes.

Where platform functionality is required, expose a suitable interface and provide an Android implementation.

---

# 8. Core Domain Independence

The following should remain substantially independent of Compose:

* garden model;
* plant model;
* activity model;
* observation model;
* recommendation engine;
* planning rules;
* vocabulary interpretation;
* provenance;
* historical logic.

This protects the application's core logic from UI changes.

---

# 9. Dependency Injection

V1 should use a lightweight, standard dependency-injection approach.

**Hilt** should be used unless Phase 0 identifies a compelling reason otherwise.

Dependency injection should provide:

* database;
* DAOs;
* repositories;
* application services;
* recommendation services;
* platform services.

Avoid creating a large dependency-injection framework around simple objects.

---

# 10. Database Foundation

Phase 0 should establish:

* Room database;
* database versioning;
* migration mechanism;
* DAO conventions;
* repository conventions;
* transaction handling;
* test database support.

The database must not be treated as disposable development storage.

Schema changes must use explicit migrations once persistent user data exists.

---

# 11. Seed Data

Phase 0 should provide only the minimum seed data required to verify the architecture.

Seed data may include:

* a small number of example plants;
* required vocabulary values;
* minimal demonstration records.

It must not attempt to populate the complete horticultural knowledge base.

Real V1 knowledge will be added separately.

---

# 12. Vocabulary Implementation

Canonical vocabulary identifiers must follow the vocabulary specifications.

Stored values use stable English identifiers such as:

```text
perennial
full_sun
moderate
user_observation
```

UI labels are supplied through Android localisation resources.

The application must never use translated display text as a business-logic identifier.

Planning and recommendation vocabularies must follow `PLANNING_VOCABULARIES.md`.

Shared vocabularies must follow `CORE_VOCABULARIES.md`.

---

# 13. Localisation Foundation

The UI must be designed for localisation from the beginning.

Do not hard-code user-facing strings in Kotlin source.

Use Android string resources.

Initial language:

```text
English
```

The architecture must permit later addition of:

```text
Afrikaans
```

and other languages without changing stored data.

---

# 14. State Management

Compose screens should follow a predictable state model.

A typical screen should have:

```text
UI
 ↓
ViewModel
 ↓
Use case
 ↓
Repository
```

The ViewModel should expose UI-ready state.

Composables should render state and emit user actions.

Avoid placing persistence, recommendation rules or complex business logic directly inside composables.

---

# 15. Error Handling

Errors should be represented in terms useful to the user.

For example:

```text
We couldn't save that change.
```

rather than exposing:

```text
SQLiteConstraintException
```

Technical information may be logged or exposed through a troubleshooting mechanism where appropriate.

Errors in optional functionality must not unnecessarily prevent core application use.

---

# 16. Logging

Use structured application logging during development.

Logging should help diagnose:

* database failures;
* import/export errors;
* recommendation failures;
* unexpected state;
* migration problems.

Do not log sensitive garden information unnecessarily.

Production logging should be conservative.

---

# 17. Testing Foundation

Phase 0 must establish the testing structure before substantial feature development.

At minimum:

### Unit tests

For:

* domain logic;
* vocabulary handling;
* recommendation rules;
* calculations;
* validation;
* use cases.

### Database tests

For:

* Room queries;
* relationships;
* transactions;
* migrations.

### UI tests

For critical user workflows once screens exist.

The test structure should make it straightforward to test domain logic without launching Android UI.

---

# 18. Build and Quality Checks

The project must establish a repeatable development workflow.

At minimum:

```text
Build
↓
Unit tests
↓
Static analysis
↓
UI/instrumentation tests where applicable
```

The exact Gradle tasks should be documented in the implementation guide once established.

A Phase 0 build must complete successfully before feature implementation proceeds.

---

# 19. Offline Verification

Phase 0 must verify that the application can:

1. launch without network access;
2. open the local database;
3. read local data;
4. create local data;
5. update local data;
6. display local data.

No network service should be required for these operations.

---

# 20. File and Import/Export Foundation

The application should establish an abstraction for file access without implementing the complete import/export system during Phase 0.

The platform layer should provide access to Android's document/file mechanisms.

Import/export logic remains in the data layer.

The UI should not directly manipulate database files.

---

# 21. Location Foundation

Garden location is important to:

* seasons;
* hemisphere;
* geographic applicability;
* future weather integration;
* local recommendations.

Phase 0 should establish the ability to store garden location.

Automatic location acquisition is not required for the foundation.

The user must be able to enter or change location manually.

---

# 22. Map Foundation

The map is a core V1 capability.

Phase 0 should therefore prove that the selected Android mapping/spatial technology can support the required interactions.

The proof of concept should demonstrate:

* displaying a map;
* basic pan/zoom;
* displaying a garden boundary;
* displaying a simple growing-space geometry;
* selecting a spatial object;
* basic geometry editing.

The full Garden map should not be implemented during Phase 0.

The mapping implementation must be isolated behind appropriate application/domain interfaces where practical.

---

# 23. Spatial Data

Spatial information must be stored independently of the map renderer.

The database should represent:

* geometry;
* dimensions;
* location;
* spatial relationships;

rather than storing renderer-specific objects.

Changing map libraries must not require rewriting garden history.

---

# 24. Application Startup

The application should have a minimal working startup flow.

For a new installation:

```text
Launch
 ↓
No garden exists
 ↓
Useful empty state
 ↓
Create Garden
```

For an existing installation:

```text
Launch
 ↓
Home
 ↓
Local garden data
```

The application must not begin with an extensive setup wizard.

---

# 25. Initial Navigation

Phase 0 should establish the permanent V1 navigation structure:

```text
Home
Garden
Plants
Planner
Inbox
```

The destinations may initially contain placeholder/empty states.

A global or prominent:

```text
+ Add
```

action should also be established.

---

# 26. Design System Foundation

Establish a small shared Compose design system containing:

* typography;
* spacing;
* common buttons;
* cards;
* status indicators;
* icons;
* form controls;
* empty states;
* error states;
* confirmation/undo feedback.

The design system should remain deliberately small.

Do not build a large component library before real screens demonstrate the need for one.

---

# 27. Accessibility Foundation

From the beginning:

* use semantic Compose components;
* provide content descriptions where required;
* support text scaling;
* avoid colour-only meaning;
* maintain sufficient touch target sizes;
* use readable contrast;
* ensure status information has textual meaning.

Accessibility should not be postponed until the end of V1.

---

# 28. Data Safety Foundation

The foundation must support:

* explicit database versioning;
* migrations;
* transactional writes;
* safe deletion practices;
* preservation of historical records.

Backup/export functionality will be implemented later, but the architecture must not prevent it.

---

# 29. What Phase 0 Does Not Build

Phase 0 should **not** attempt to implement:

* complete garden management;
* complete Plant Library;
* complete recommendation engine;
* complete planner;
* complete Inbox;
* complete soil system;
* complete experiment system;
* complete import/export;
* comprehensive map editing;
* weather integration;
* community functionality;
* AI functionality;
* sophisticated analytics;
* complete horticultural knowledge.

Phase 0 establishes the foundation needed to build these safely.

---

# 30. Phase 0 Deliverables

Phase 0 is complete when the project contains:

Project foundation

1. A working Kotlin Android project with Compose.

2. Navigation structure established (Home, Garden, Plants, Planner,
   Inbox, + Add).

3. Android API levels fixed: minSdk, targetSdk, compileSdk chosen and
   documented.

4. Gradle configured with Kotlin DSL and a Version Catalog for
   dependency management.

5. App version and database schema version initialised:
   app 0.1.0 (development), schema_version = 1.

Data and persistence

6. Room/SQLite configured.

7. Database versioning and migration mechanism established,
   including Room `exportSchema = true` and a committed
   `app/schemas/` directory.

8. Migration test harness established with at least three fixtures:
   empty database, minimal garden, representative garden. No real
   migrations exist yet; the harness and fixtures do.

9. Vocabulary infrastructure established (DEC-040), including
   at least one fixed enum and one Room-backed reference entity.

10. Garden-affecting preferences (Room) and display preferences
    (DataStore) established (DEC-042).

11. History mechanism pattern established for GrowingSpace
    (DEC-041).

12. Core seed data support established.

Architecture

13. Dependency injection established (Hilt).

14. Coroutines and Flow conventions established.

15. Core/domain/data/ui package boundaries established.

16. Repository and use-case patterns established.

17. File-access abstraction established.

18. Garden location storage established.

Presentation

19. Localisation infrastructure established; no hard-coded
    user-facing strings.

20. Basic shared Compose design system established, including
    design tokens for colour, typography, spacing, corner radii,
    elevation and touch targets, per
    V1_VISUAL_DESIGN_SPECIFICATION §63. Components consume tokens;
    they do not define literal values.

Testing

21. Testing infrastructure established with named libraries:
    JUnit 5, MockK, Turbine, Robolectric, Compose UI test,
    Room in-memory database.

22. Basic error handling established.

23. Offline verification method documented and reproducible
    (not an informal manual check).

Map and spatial

24. MapLibre GL Native (or the pre-approved Compose Canvas
    fallback) evaluated through a proof of concept per DEC-039.

25. Spatial data not coupled directly to the map library.

Workflow

26. A clean build and test workflow established: format → static
    analysis → unit tests → data/repository tests → Compose tests.

---

# 31. Phase 0 Acceptance Criteria

Phase 0 is complete only when all of the following are true.

Project

- The application builds successfully.
- The application launches on a supported Android device/emulator.
- The project uses Kotlin and Jetpack Compose.
- Dependencies are pinned and reproducible via the Version Catalog.
- App version and schema version are set and visible in a
  diagnostic surface.

Architecture

- UI, application, domain and data responsibilities are separated.
- Domain logic does not depend on Compose, Android UI classes, Room,
  or Android framework types.
- Database implementation is isolated behind repository interfaces.
- Platform services (location, files, notifications) are isolated
  from core domain logic.
- Dependency injection provides database, DAOs, repositories,
  use cases and platform services.
- Coroutines and Flow conventions are applied consistently for
  database observation and long-running work.

Data

- Room database opens successfully.
- Data can be created, read, updated and archived locally.
- Database migrations are versioned and Room `exportSchema` output
  is committed.
- Migration test harness runs against empty, minimal and
  representative fixtures.
- Fixed vocabulary identifiers round-trip through Room using
  canonical ids (not enum ordinals or display strings).
- At least one reference-data entity (e.g. Plant) round-trips.
- Garden-affecting preferences persist in Room and survive export.
- Display preferences persist in DataStore and are excluded from
  export.
- Growing space geometry changes preserve prior state via the
  companion history table (DEC-041).

Vocabulary

- A validation function exists to check a string against a given
  vocabulary and is exercised by at least one test.
- No display string is written to a database column or used as a
  rule input.

Offline

- Core local operations work with no network connection.
- The offline verification method is documented and can be repeated
  by a second developer without verbal instruction.

Presentation

- Primary navigation works.
- Empty states are usable.
- User-facing strings are externalised for localisation.
- Design tokens are defined centrally; components reference them.
- Basic accessibility requirements are demonstrated: readable
  contrast, adequate touch targets, text scaling, non-colour-only
  status communication.

Map

- The chosen map approach displays a base map, a polygon garden
  boundary, and a growing-space polygon.
- Pan, zoom, select and basic geometry edit work in the proof of
  concept.
- Spatial data is stored in a technology-independent form and is
  not tied to the map library's own types.

Testing

- Unit tests execute successfully.
- Repository/data tests execute successfully against an in-memory
  Room database.
- Compose UI tests execute successfully for at least the empty
  application shell.
- The defined test workflow (format → static analysis → unit →
  data → UI) runs end to end on a clean checkout.

Vertical slice

- The slice from IMPLEMENTATION_PLAN §13 succeeds:
  Create Garden → Create Growing Space → Persist → Reload →
  Verify history-safe edit.
- This is the Phase 0 exit demonstration. It exercises stable IDs,
  migrations infrastructure, layering, and history preservation in
  one workflow.

---

# 32. Phase 0 Development Sequence

The recommended implementation order is:

1. Create the Android/Kotlin project.

2. Configure Gradle and dependency management.

3. Configure Compose.

4. Establish package/module boundaries.

5. Configure Hilt.

6. Configure Room and SQLite.

7. Establish database migrations.

8. Implement core domain foundations.

9. Implement vocabulary infrastructure.

10. Establish repositories and use-case patterns.

11. Establish Compose navigation.

12. Establish the basic design system.

13. Establish localisation resources.

14. Establish testing infrastructure.

15. Establish file-access abstraction.

16. Establish garden-location storage.

17. Prototype the mapping/spatial layer.

18. Verify offline operation.

19. Run the complete build/test workflow.

20. Resolve architectural issues before beginning V1 feature implementation.

---

# 33. Architecture Decision Rule

During Phase 0, prefer:

**simple over clever;**

**explicit over implicit;**

**stable over fashionable;**

**testable over convenient;**

**domain clarity over framework convenience.**

Do not introduce infrastructure merely because it might become useful later.

Introduce abstractions when they protect a genuine architectural boundary.

---

# 34. Phase 0 Exit Condition

Phase 0 ends when the project has a **small, working Android application with a sound foundation**, not when every architectural question about the future has been solved.

The result should make it straightforward to begin implementing V1 without needing to restructure the application around:

* the database;
* the UI;
* the recommendation engine;
* the map;
* localisation;
* offline operation.

The next phase may then implement the core garden workflows on this foundation.

---

# 35. Guiding Principle

> **Build the smallest solid foundation that can support the full V1 garden experience.**

Kotlin and Jetpack Compose provide the Android-native application layer.

The domain, data model, recommendation system and garden history remain clearly separated from the UI so that the application can evolve without compromising its core architecture.

---

# 36. Relationship to subsequent phases

Phase 0 ends when the deliverable list in §30 is met and the
acceptance criteria in §31 pass.

The vertical slice — Create Garden → Create Growing Space → Persist
→ Reload → Verify history-safe edit — is the Phase 0 exit
demonstration and the first work item of Phase 1. It is not a
separate task between the phases.

The four decisions recorded for this phase are DEC-039 (map
technology), DEC-040 (vocabulary implementation), DEC-041
(historical change mechanism) and DEC-042 (preference storage).
Phase 1 must conform to these decisions; deviations require a new
DEC entry.