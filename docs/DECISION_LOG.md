Garden Planner & Manager — Decision Log

Document: docs/DECISION_LOG.md
Version: 0.1
Status: Working specification
Last updated: August 2026

1. Purpose

This document records important product, architectural, data, UX, and development decisions for the Garden Planner & Manager project.

The purpose is to preserve the reasoning behind decisions so that:

future development does not accidentally contradict established decisions;

future chats can quickly understand the project's history;

architectural decisions can be revisited deliberately rather than accidentally;

new modules can be developed consistently;

the project can evolve without repeatedly re-litigating settled questions.


This document should record decisions and their rationale, not duplicate the full technical specifications contained in other project documents.


---

2. How to use this document

A decision should be added when it is significant enough that a future developer could reasonably ask:

> "Why did we build it this way?"



Examples include:

choosing a particular architectural approach;

deciding that a piece of data belongs to one entity rather than another;

deciding that something belongs in V1 or later;

deciding that data must be preserved in a particular way;

deciding that a module must remain independent;

deciding how localisation works;

deciding how recommendations are represented;

deliberately rejecting an otherwise plausible approach.


Minor implementation choices do not need to be recorded here.


---

3. Decision status

Each decision should have one of the following statuses:

Accepted

The decision is currently part of the project specification.

Provisional

The decision is currently preferred but may reasonably change during implementation.

Superseded

A newer decision has replaced it.

Rejected

The approach was considered and deliberately rejected.

Deferred

The question has been recognised but deliberately postponed.


---

4. Decision format

New decisions should use this structure:

Decision ID: DEC-XXX
Date: YYYY-MM-DD
Status: Accepted / Provisional / Superseded / Rejected / Deferred
Area: Product / Architecture / Data / UX / Development / Other

Decision

What was decided.

Rationale

Why it was decided.

Consequences

What this decision means for the project.

Related documents

Which project documents contain the detailed implementation/specification.

Supersedes

Any previous decision that this replaces.


---

5. Product decisions

DEC-001 — The application is a Garden Planner & Manager

Date: August 2026
Status: Accepted
Area: Product

Decision

The product will be developed as a broader Garden Planner & Manager, rather than merely a planting calendar or task/reminder application.

The product should ultimately support a gardener in:

understanding their garden;

planning;

recording what actually happens;

observing outcomes;

learning from those outcomes;

making better future decisions.


Rationale

The project's value comes from connecting planning, action, observation, history and knowledge rather than treating them as unrelated features.

Consequences

V1 must establish a foundation capable of supporting the broader product.

Future modules should be able to build on the core data and workflows without requiring the core product to be redesigned.

Related documents

docs/PROJECT_OVERVIEW.md

docs/V1_SCOPE.md

docs/DATA_MODEL.md

docs/vocabularies/CORE_VOCABULARIES.md



---

6. Core product decisions

DEC-002 — V1 must prioritise a reliable core product

Date: August 2026
Status: Accepted
Area: Product

Decision

V1 will prioritise delivering a small, reliable core product rather than attempting to deliver the complete long-term Garden Planner & Manager vision.

The core product must remain useful independently of future modules.

Rationale

A broad product can easily become fragile if too many features are introduced simultaneously.

The core gardening workflow should therefore be stabilised before optional complexity is added.

Consequences

Future features may be deferred even if they are desirable.

The architecture must nevertheless leave appropriate extension points for future functionality.

Related documents

docs/V1_SCOPE.md

docs/CORE_ARCHITECTURE.md

docs/MODULE_ARCHITECTURE.md

docs/V1_IMPLEMENTATION_PLAN.md



---

DEC-003 — Future functionality must have minimal adverse impact on the core product

Date: August 2026
Status: Accepted
Area: Architecture / Product

Decision

Optional and future modules should be designed so that they have minimal adverse impact on:

core functionality;

core reliability;

core data;

core development;

core testing;

core UX.


Rationale

The project is expected to grow substantially.

Adding every future feature directly into the core would make the application increasingly fragile and difficult to maintain.

Consequences

New functionality should normally be implemented behind defined module/service boundaries.

Optional functionality should not become a prerequisite for basic gardening functionality unless explicitly intended.


---

7. Modularity decisions

DEC-004 — Features should be implemented as independently as reasonably practical

Date: August 2026
Status: Accepted
Area: Architecture

Decision

The application's functions should be separated into modules/components/services with clear responsibilities and interfaces.

Modules should be able to be:

developed;

tested;

modified;

replaced;

expanded


with minimal unnecessary changes to unrelated modules.

Rationale

The user specifically identified excessive cross-file coupling in another application as a major development problem.

The Garden Planner & Manager should avoid reproducing that pattern.

Consequences

The architecture should favour:

separation of concerns;

stable interfaces;

domain services;

repository abstractions;

feature modules;

dependency injection where useful;

focused responsibilities.


The architecture should not pursue modularity for its own sake.

Unnecessary abstraction and fragmentation should be avoided.

Related documents

docs/CORE_ARCHITECTURE.md

docs/MODULE_ARCHITECTURE.md

docs/V1_TECHNICAL_ARCHITECTURE.md

docs/CONTRIBUTION_AND_DEVELOPMENT_GUIDELINES.md



---

DEC-005 — Minimise blast radius

Date: August 2026
Status: Accepted
Area: Architecture / Development

Decision

A small change should not routinely require changes across large portions of the application.

The project should actively monitor the blast radius of changes.

Rationale

Large blast radii indicate excessive coupling and make development increasingly fragile.

Consequences

If implementing a small feature repeatedly requires unrelated screens, services and entities to change, the architecture should be reviewed rather than simply accepting the coupling.


---

8. Data decisions

DEC-006 — User data must survive future application versions

Date: August 2026
Status: Accepted
Area: Data

Decision

The data model must be designed so that user data created in V1 can be preserved through future application versions wherever reasonably possible.

This includes:

plans;

records;

observations;

history;

garden information;

plant information;

other meaningful user-generated data.


Rationale

The application's long-term value depends partly on accumulating gardening history.

A user should not lose years of information merely because the application has evolved.

Consequences

Database migrations are a first-class architectural concern.

Schema changes must be planned with backward compatibility and migration in mind.

Related documents

docs/DATA_MODEL.md

docs/DATA_MIGRATION_STRATEGY.md

docs/TESTING_STRATEGY.md



---

DEC-007 — Historical records are durable

Date: August 2026
Status: Accepted
Area: Data

Decision

Historical records should generally be treated as durable facts once recorded.

Changing current information must not silently rewrite historical records unless such behaviour is explicitly intended and documented.

Rationale

The application is intended to learn from what actually happened.

Historical integrity is therefore essential.

Consequences

The data model must distinguish appropriately between:

current state;

planned state;

actual historical events;

observations;

inferred information.



---

DEC-008 — Planned and actual events must remain distinct

Date: August 2026
Status: Accepted
Area: Data / Product

Decision

A plan is not evidence that an event actually occurred.

The application must distinguish between:

what was planned;

what was completed/recorded;

what was observed.


Rationale

A gardening plan can change.

If planned actions were automatically treated as historical facts, the application's history would become unreliable.

Consequences

Planning, task management and historical event recording must have clearly defined semantics.

This distinction must be protected by automated tests.


---

9. Canonical language decisions

DEC-009 — English is the canonical data language

Date: August 2026
Status: Accepted
Area: Data / Localisation

Decision

The underlying data model uses English canonical identifiers.

This includes:

entity identifiers;

controlled vocabulary identifiers;

relationship identifiers;

enum values;

machine-readable classifications;

import/export identifiers;

rule identifiers.


Rationale

A stable canonical language provides a consistent machine-readable data layer independent of user-facing language.

Consequences

Canonical identifiers must not change merely because the user changes language.

Related documents

docs/vocabularies/CORE_VOCABULARIES.md

docs/VOCABULARY_INDEX.md

docs/DATA_MODEL.md



---

DEC-010 — Localisation applies to the user experience, not the underlying data

Date: August 2026
Status: Accepted
Area: UX / Data

Decision

The application should eventually support multiple user-facing languages, including Afrikaans.

Localisation should affect:

labels;

descriptions;

instructions;

messages;

user-facing terminology;

other presentation content.


It should not change the underlying canonical data.

Rationale

The same garden data should remain structurally identical regardless of the user's language.

This also supports future import/export and interoperability.

Consequences

Translation resources must map user-facing text to stable canonical concepts.

The data model must not use translated strings as identifiers.


---

10. Knowledge and recommendation decisions

DEC-011 — Gardening knowledge is separate from user garden history

Date: August 2026
Status: Accepted
Area: Data / Knowledge

Decision

General gardening knowledge should be separated from an individual user's garden records.

For example:

General knowledge:

a plant's typical requirements;

environmental preferences;

known associations;

common problems.


User information:

what the user planted;

what happened;

what they observed;

what they tried;

what outcome occurred.


Rationale

The application must be able to distinguish general knowledge from individual experience.

Consequences

Knowledge should not overwrite user records.

User observations should not automatically become universal gardening facts.


---

DEC-012 — Recommendations must be explainable

Date: August 2026
Status: Accepted
Area: Product / Architecture

Decision

Recommendations should be accompanied by an understandable basis where practical.

The application should be able to distinguish:

known information;

inferred information;

assumptions;

uncertainty;

recommendation rationale.


Rationale

Gardening conditions are variable and often uncertain.

The application should help the user make decisions rather than present uncertain conclusions as unquestionable facts.

Consequences

Recommendation rules should retain enough information to explain why a recommendation was generated.


---

DEC-013 — Unknown information must remain distinguishable from negative information

Date: August 2026
Status: Accepted
Area: Data / Recommendations

Decision

The application must distinguish between:

known positive;

known negative;

unknown/not recorded;

uncertain/inferred where appropriate.


Rationale

For gardening, absence of information is not necessarily evidence of absence.

For example:

> "soil pH has not been tested"



is different from:

> "soil pH is known to be unsuitable."



Consequences

The data model and recommendation system must not treat missing information as a definitive negative unless explicitly specified by a rule.


---

11. V1 scope decisions

DEC-014 — V1 should not attempt to implement the complete product vision

Date: August 2026
Status: Accepted
Area: Product

Decision

V1 should focus on the core functionality defined in the V1 scope.

Advanced functionality should be deferred where implementing it would:

significantly increase complexity;

delay the core product;

introduce unnecessary dependencies;

create architectural fragility.


Rationale

A functional, reliable foundation is more valuable than a broad but fragile first release.


---

DEC-015 — Future functionality should be anticipated, not prematurely implemented

Date: August 2026
Status: Accepted
Area: Architecture / Product

Decision

V1 should be future-proofed where reasonably practical, but should not implement speculative functionality merely because it may be useful later.

Rationale

There is a balance between:

under-designing

and:

over-engineering.

The project should establish stable foundations without building large abstractions for hypothetical requirements.

Consequences

Future requirements should be considered when defining stable data structures and interfaces, but implementation should remain focused on V1 needs.


---

12. Offline-first decisions

DEC-016 — Core functionality should work offline

Date: August 2026
Status: Accepted
Area: Product / Architecture

Decision

The core Garden Planner & Manager functionality should be usable without an internet connection.

Rationale

Gardening frequently occurs outdoors and in locations where reliable connectivity cannot be assumed.

Offline operation also improves resilience and privacy.

Consequences

Core data should be stored locally.

Future online features must not unnecessarily make core functionality dependent on network availability.


---

13. Import/export decisions

DEC-017 — User data should be exportable

Date: August 2026
Status: Accepted
Area: Data / Product

Decision

The application should provide a reliable way to export user data.

Rationale

Export supports:

backup;

portability;

migration;

user confidence;

future interoperability.


Consequences

The export format should use stable canonical identifiers and preserve meaningful relationships.


---

DEC-018 — Import/export should support long-term data portability

Date: August 2026
Status: Accepted
Area: Data

Decision

Import/export should be designed as a durable data interchange mechanism rather than merely a convenience feature.

Rationale

The application is expected to evolve substantially.

A stable interchange mechanism provides an additional safeguard against vendor/application lock-in and future migration problems.

Consequences

Changes to the data model should consider their effect on import/export.

Round-trip testing should be used.


---

14. Testing decisions

DEC-019 — Testing is part of architecture

Date: August 2026
Status: Accepted
Area: Development / Architecture

Decision

Testing should be designed alongside functionality rather than added only at the end.

Rationale

The project's modularity and data-preservation goals require confidence that changes do not cause unrelated regressions.

Consequences

Modules should be designed in ways that make their important behaviour independently testable.


---

DEC-020 — Data loss is a release-blocking concern

Date: August 2026
Status: Accepted
Area: Testing / Data

Decision

Known defects that can cause unintended user-data loss, corruption or destructive migration should normally block release.

Rationale

The accumulated garden record is one of the application's most valuable assets.

A feature that works perfectly but destroys historical data is unacceptable.


---

15. Development decisions

DEC-021 — Prefer simple architecture over unnecessary sophistication

Date: August 2026
Status: Accepted
Area: Architecture / Development

Decision

The project should actively challenge proposed complexity.

Before introducing an abstraction, framework, service, dependency or architectural layer, ask:

Is it necessary?

What problem does it solve?

Could a simpler solution work?

Does it reduce or increase coupling?

Does it make testing easier or harder?

Will it help future modules?

Does it create maintenance burden?


Rationale

Future-proofing does not mean building the most elaborate architecture possible.

The goal is a robust architecture with enough structure to grow safely.


---

DEC-022 — Avoid premature abstraction

Date: August 2026
Status: Accepted
Area: Development

Decision

The project should not create elaborate generic abstractions solely because something might eventually need them.

Abstraction should normally be introduced when:

a real recurring need exists;

a stable boundary is identifiable;

the abstraction reduces coupling or duplication;

its benefits outweigh its complexity.


Rationale

Premature abstractions can become constraints rather than safeguards.


---

DEC-023 — Explainable development instructions are required

Date: August 2026
Status: Accepted
Area: Development

Decision

Development guidance should explain not only what to do but, where useful, what the action accomplishes and why it is being done.

Rationale

The project is being developed in a workflow where the user may execute development instructions without extensive programming experience.

Clear explanations reduce the risk of blindly making changes that have unintended consequences.

Consequences

Implementation instructions should be explicit and ordered.


---

16. Documentation decisions

DEC-024 — Documentation is part of the project

Date: August 2026
Status: Accepted
Area: Development

Decision

Important product and architectural knowledge should be maintained in project documentation rather than relying on conversation history.

Rationale

The project is large enough that conversation history alone is not a reliable long-term specification.

Documentation also makes it possible to continue development in a new chat without reconstructing decisions from memory.

Consequences

Significant decisions should be recorded in the appropriate .md document.


---

DEC-025 — Each document should have a defined responsibility

Date: August 2026
Status: Accepted
Area: Documentation

Decision

Project documents should each have a clear purpose.

The project should avoid creating large documents that simply duplicate other specifications.

Rationale

Duplicated documentation eventually becomes contradictory.

A smaller set of authoritative documents is easier to maintain.


---

DEC-026 — Project documentation must be sufficient to restart development

Date: August 2026
Status: Accepted
Area: Documentation

Decision

The documentation set should eventually contain enough information for a new development chat to understand:

what the product is;

what V1 includes;

what it deliberately excludes;

the data model;

architecture;

module boundaries;

UX;

technical implementation;

testing;

migration;

important decisions;

future direction.


Rationale

Development may need to move between conversations.

The project should not depend on a single conversation retaining all context.


---

17. Documentation maintenance rules

DEC-027 — Decisions should be updated when superseded

Date: August 2026
Status: Accepted
Area: Documentation

Decision

When an accepted decision changes, the old decision should not simply be deleted.

Instead:

1. mark the old decision Superseded;


2. identify the new decision;


3. record the reason for the change;


4. update affected specifications.



Rationale

Preserving decision history helps explain how the architecture evolved.


---

DEC-028 — Specifications are authoritative over conversation history

Date: August 2026
Status: Accepted
Area: Documentation

Decision

Once a decision or specification has been formally recorded in project documentation, that documentation should be treated as the authoritative project reference unless explicitly revised.

Rationale

Conversation discussion is exploratory.

The project documentation represents the current agreed state.


---

18. Decisions about future development

DEC-029 — New modules should be independently developable where practical

Date: August 2026
Status: Accepted
Area: Architecture / Development

Decision

Future modules should be documented sufficiently that they can be developed semi-independently and subsequently integrated into the overall application.

Rationale

This supports:

parallel development;

staged development;

reduced core-product disruption;

easier testing;

easier future continuation in separate development chats.


Consequences

Each substantial future module should eventually have:

a defined purpose;

scope;

entities/interfaces;

dependencies;

UX requirements;

data requirements;

testing requirements;

integration requirements.



---

DEC-030 — Core functionality should not depend unnecessarily on optional modules

Date: August 2026
Status: Accepted
Area: Architecture

Decision

Optional modules should not become mandatory dependencies of the core product unless explicitly approved.

Rationale

The core product must remain reliable even while future modules are unfinished, disabled or unavailable.

Consequences

The architecture should favour optional integration points where appropriate.


---

19. Deliberately deferred decisions

The following areas are recognised but should not be prematurely fixed unless required for V1.

DEC-031 — Advanced recommendation/AI approach

Date: August 2026
Status: Deferred
Area: Product / Architecture

Decision

The exact long-term approach to advanced recommendation intelligence is deferred.

Potential future approaches may include:

increasingly sophisticated rule systems;

statistical approaches;

machine-learning approaches;

AI-assisted reasoning;

combinations of these.


Rationale

V1 should establish trustworthy structured data and explainable rules before committing to sophisticated intelligence.


---

DEC-032 — Cloud synchronisation

Date: August 2026
Status: Deferred
Area: Architecture / Product

Decision

Cloud synchronisation is not required for the V1 core product.

Rationale

Offline-first operation is a priority.

Cloud functionality can be evaluated later without making it a prerequisite for core gardening functionality.


---

DEC-033 — Social/community functionality

Date: August 2026
Status: Deferred
Area: Product

Decision

Social/community features are not part of the V1 core.

Rationale

They would introduce substantial additional complexity, moderation requirements, privacy considerations and infrastructure.


---

DEC-034 — Commercial ecosystem/integrations

Date: August 2026
Status: Deferred
Area: Product

Decision

Retailer, supplier, product marketplace and other commercial integrations are deferred.

Rationale

They are not required to establish the core Garden Planner & Manager product.


---

20. Rejected approaches

DEC-035 — Build the complete product before validating the core

Date: August 2026
Status: Rejected
Area: Product

Decision

The project will not attempt to build the entire long-term feature set before establishing a reliable V1 core.

Rationale

This would increase development time, coupling and risk while delaying validation of the fundamental product.


---

DEC-036 — Treat all functionality as one tightly coupled application layer

Date: August 2026
Status: Rejected
Area: Architecture

Decision

The application will not be designed as a tightly coupled collection of screens where each new feature requires widespread modification.

Rationale

This directly conflicts with the project's modularity and maintainability objectives.


---

DEC-037 — Use translated strings as underlying data identifiers

Date: August 2026
Status: Rejected
Area: Data / Localisation

Decision

Translated user-facing text will not be used as canonical database identifiers.

Rationale

It would make localisation, migration, imports, exports and interoperability unnecessarily fragile.


---

21. Decision review process

A significant proposed change should be evaluated against existing decisions.

Before changing an established architectural or product decision, ask:

1. What problem are we solving?


2. Why is the existing decision insufficient?


3. What new complexity does the change introduce?


4. What existing functionality could be affected?


5. What happens to existing user data?


6. What modules are affected?


7. What tests must change?


8. What documentation must change?


9. Is this genuinely required now?


10. Could the change be deferred?



If the answer indicates that an existing decision should change, update this document rather than silently diverging from it.


---

22. Current architectural principles derived from the decision log

The accumulated decisions can be summarised into the following principles.

1. Protect the core

The core gardening workflow must remain reliable.

2. Protect the data

User history should survive application evolution.

3. Separate planning from reality

A plan is not an event.

4. Separate knowledge from experience

General gardening knowledge is not the same thing as what happened in one user's garden.

5. Keep uncertainty explicit

Unknown is not the same as false.

6. Keep the canonical data language stable

English identifiers underpin the data model.

7. Localise the experience, not the data

Users can interact with the application in different languages without changing the underlying data.

8. Keep modules reasonably independent

A feature should not unnecessarily require changes throughout the application.

9. Minimise blast radius

Small changes should remain small changes.

10. Prefer simple solutions

Future-proofing should not become over-engineering.

11. Test what matters

Data integrity and core workflows receive the highest testing priority.

12. Document important reasoning

The project should not depend on conversation memory.

13. Build for evolution

V1 should provide a foundation for V2+ without prematurely implementing every future idea.


---

23. Decision log maintenance

This document should be updated when:

a significant architectural decision is made;

a major product scope decision is made;

an important data-model decision is made;

an existing decision is changed;

an important approach is deliberately rejected;

a previously deferred question is resolved.


Do not record every implementation detail.

The purpose of this document is to preserve important decisions and their reasoning.

The project specifications remain responsible for the detailed implementation requirements.

---

DEC-038 — Native Android (Kotlin + Jetpack Compose) is the V1 technology stack

Date: September 2026
Status: Accepted
Area: Architecture / Development

Decision

V1 is developed as a native Android application using:

- Kotlin
- Jetpack Compose
- Android Jetpack (Navigation, lifecycle, viewmodel)
- Room over SQLite
- Kotlin Coroutines
- Kotlin Flow
- Hilt for dependency injection
- Android Studio / Gradle

Flutter / Dart is not the V1 stack. Cross-platform support is not a V1
requirement.

Rationale

PHASE_0_PROJECT_FOUNDATION.md, V1_IMPLEMENTATION_PLAN.md and
IMPLEMENTATION_PLAN.md all specify the native Android stack.
V1_TECHNICAL_ARCHITECTURE.md had retained older Flutter/Dart content
that conflicted with those documents. ARCHITECTURE_PLAN.md already
adjudicated the conflict in favour of native Android. This decision
records that adjudication formally and removes the conflict from the
specification set.

Consequences

- V1_TECHNICAL_ARCHITECTURE.md is rewritten to reflect the native
  Android stack. All framework-agnostic content (layering, module
  ownership, domain semantics, recommendation architecture, data
  authority, migration, import/export pipeline) is unchanged. Only the
  technology-specific passages change.
- The domain layer remains Android-independent in principle so that
  domain logic is testable without the Android runtime and could in
  principle be ported later. V1 does not attempt cross-platform.
- A future iOS application, if ever pursued, would be a separate
  client against the same domain semantics. It is not part of V1.

Related documents

docs/V1_TECHNICAL_ARCHITECTURE.md
docs/PHASE_0_PROJECT_FOUNDATION.md
docs/ARCHITECTURE_PLAN.md
docs/V1_IMPLEMENTATION_PLAN.md
docs/IMPLEMENTATION_PLAN.md

Supersedes

Any earlier implicit assumption that V1 was Flutter-based.

---

DEC-039 — Map and spatial rendering technology

Date: September 2026
Status: Accepted
Area: Architecture / Presentation

Decision

V1 uses MapLibre GL Native (Android) as the map rendering engine,
accessed through a thin Compose wrapper. The map library is treated
as an implementation detail of the spatial presentation layer, not as
part of the domain.

The domain stores geometry and dimensions in a technology-independent
form (see V1_DATABASE_SCHEMA.md §13–15). The map component consumes
and produces that form. Replacing the map library must not require
changes to garden records or spatial history.

If the Phase 0 proof of concept reveals that MapLibre cannot support
the required editing interactions within the V1 timeline, the fallback
is a Compose Canvas renderer for V1, with the same domain contract.
This is the only pre-approved fallback and does not require a new
decision.

Rationale

- The required map is a garden-planning view, not a street or routing
  map. It shows polygons, lines, points, dimensions and overlays.
- Offline-first operation is a core product constraint. MapLibre
  supports offline tile caching without a paid tier or account.
- No mandatory external account or Google Play Services dependency
  aligns with the offline-first and privacy-first principles.
- Vector tiles allow the earthy, low-contrast palette required by
  V1_VISUAL_DESIGN_SPECIFICATION §28–29 to be applied to the base
  map, not just overlays.
- No per-user licensing cost or vendor terms that constrain offline
  behaviour.

Rejected alternatives

- Google Maps SDK — Play Services dependency, offline tile
  restrictions in the standard terms, styling limits, and a
  mandatory Google account for some configurations. Poor fit for
  offline-first.
- Mapbox — strong product but per-user pricing and vendor terms that
  make offline caching awkward at scale.
- osmdroid — raster-only, dated visual language, awkward offline.
- Pure Compose Canvas — viable but reimplements tile loading,
  gesture handling, hit-testing and layer composition. Accepted as
  fallback only.

Consequences

- Phase 0 §22 proof of concept demonstrates: displaying tiles,
  displaying a polygon garden boundary, displaying a growing-space
  polygon, selecting a spatial object, and basic geometry editing.
- The map wrapper lives in ui/garden/ and depends on domain spatial
  types, never the reverse.
- MapLibre styles are defined as project assets, not inline in code.
- The spatial data model is defined independently and does not
  reference MapLibre types.

Related documents

docs/V1_TECHNICAL_ARCHITECTURE.md §23–24
docs/GARDEN_VOCABULARIES.md
docs/V1_DATABASE_SCHEMA.md §13–15
docs/PHASE_0_PROJECT_FOUNDATION.md §22

---

DEC-040 — Vocabulary implementation pattern

Date: September 2026
Status: Accepted
Area: Data / Architecture

Decision

Controlled vocabularies are implemented according to the four
categories defined in VOCABULARY_INDEX.md §3, using the following
patterns uniformly across the codebase.

1. Fixed enum (hemisphere, activity status, recommendation status,
   confidence, information state, evidence type, and the other
   genuinely bounded vocabularies)

   Kotlin `enum class` with an explicit canonical identifier:

       enum class Hemisphere(val id: String) {
           NORTHERN("northern"),
           SOUTHERN("southern"),
           EQUATORIAL("equatorial"),
           GLOBAL("global"),
           UNKNOWN("unknown")
       }

   The `id` is the value stored in Room, used in rules, and written
   to import/export. The enum constant name is a code-level
   convenience and MUST NOT be used as a stored value.

   Room stores these as TEXT columns. Room TypeConverters map
   enum <-> id. Reading an unknown id from the database is a
   validation error, not a silent fallback.

2. Reference data (plants, cultivars, sources, specific problems,
   specific pests/diseases when they carry their own attributes)

   Room entities. Reference data is authored, versioned, and shipped
   as seed content, or imported as knowledge packages.

3. User-extensible reference data (custom rotation groups, custom
   organisational tags)

   Room entities with an `owner` or `is_system` column. User-created
   values are never promoted to system knowledge automatically.

4. Structured value (pH, temperature, quantity, dimension, date
   range, coordinate)

   Kotlin value classes wrapping the underlying primitive with unit
   and validation. Not stored as vocabulary strings.

Display labels are never stored. Display strings are resolved through
the localisation layer from the canonical id.

Rationale

- Storing enum ordinals is unsafe: inserting a new value renumbers
  existing records. Canonical ids are stable.
- A uniform pattern across all fixed vocabularies prevents the
  common drift where some vocabularies become enums, others become
  sealed classes, and others become string constants.
- Making the id explicit makes localisation a pure mapping concern
  and keeps rules and queries independent of display language.
- Reference data as Room entities (not enums) is required because
  reference data grows without a code release.

Consequences

- A single `VocabularyId` pattern is established in core/. All
  fixed vocabularies follow it.
- Phase 0 implements at least one fixed enum (Hemisphere) and one
  Room-backed reference entity (Plant) to demonstrate both patterns
  in the vertical slice.
- Validation functions exist to verify that a string is a valid id
  in a given vocabulary, used by import and by domain validation.
- No translated string may be written to a database column or used
  as a rule input.

Related documents

docs/VOCABULARY_INDEX.md §3, §14–17
docs/vocabularies/CORE_VOCABULARIES.md
docs/vocabularies/*.md
docs/PHASE_0_PROJECT_FOUNDATION.md §12
docs/V1_TECHNICAL_ARCHITECTURE.md §18

---

DEC-041 — Historical change mechanism for state entities

Date: September 2026
Status: Accepted
Area: Data

Decision

The application distinguishes two kinds of persistent record.

Event records
  Activities, observations, measurements, harvests, harvest losses,
  user decisions, recommendation records.

  These are inherently historical. They are created once and are not
  "updated" in the ordinary sense. Ordinary corrections are handled
  by explicit correction records or by a documented modification
  reason and audit trail. They require no separate history mechanism.

State records
  Growing space geometry and dimensions, plant instance location,
  and — as later requirements arise — other mutable garden state.

  For state records, historical change is captured using companion
  history tables written in the same transaction as the current-state
  update.

  Pattern:

      GrowingSpace           (current state)
      GrowingSpaceHistory    (append-only, references GrowingSpace)

  Each history row records the prior value, the effective period
  (valid_from, valid_to), the recorded_at timestamp, and the reason
  for the change where relevant.

  Current state is read from the primary table. Historical state is
  queried from the history table using effective-period predicates.

  History rows are append-only. They are never updated in place.

Rationale

- Most entities in the schema are event records and need no history
  mechanism. Applying a bitemporal model to the entire schema would
  add complexity to every query for no benefit.
- State entities that need history need it because the question
  "what did this space look like when the 2025 crop was planted?"
  is a real V1 product question (V1_DATABASE_SCHEMA §15).
- Companion history tables are simpler to implement and to query in
  Room than fully bitemporal rows, and do not complicate uniqueness
  constraints on the primary table.
- Append-only history rows make migration and audit straightforward.

Rejected alternatives

- Full bitemporal columns on every mutable entity. Adds complexity
  to every query and every constraint for a benefit only a few
  entities require.
- Event-sourced state. Correct but disproportionate for V1.
- Effective-dated rows in the primary table (current is the row
  with valid_to IS NULL). More elegant but harder to constrain and
  to map cleanly through Room.

Consequences

- Phase 0 delivers the pattern for GrowingSpace as the vertical
  slice's history-safe edit demonstration.
- Subsequent state entities adopt the pattern only when a real
  requirement exists. No speculative history tables.
- The pattern is documented in V1_DATABASE_SCHEMA.md §15 so that
  Phase 1 schema work follows it.

Related documents

docs/V1_DATABASE_SCHEMA.md §15
docs/DATA_MODEL.md §11, §63
docs/DATA_MIGRATION_STRATEGY.md
docs/CORE_ARCHITECTURE.md §20
docs/IMPLEMENTATION_PLAN.md §13

---

DEC-042 — User preference storage split

Date: September 2026
Status: Accepted
Area: Data / Architecture

Decision

User preferences are split by whether they participate in garden data
queries or reasoning.

Garden-affecting preferences → Room

  Favourite plants, plants to avoid, water-conservation priority,
  pollinator priority, low-maintenance preference, food-production
  priority, native-plant preference, experimentation preference.

  These participate in recommendation candidate generation,
  filtering, and rule evaluation. They are garden data and live in
  Room alongside other garden records. They are exported with the
  garden. They are versioned by the same migration discipline.

Display / device preferences → Jetpack DataStore

  Theme (light/dark/system), unit system (metric/imperial), UI
  language, notification category settings, quiet periods,
  last-viewed screen, temporary filter state.

  These are per-device, do not participate in reasoning, and do not
  belong in an export. They are not migrated by the garden schema
  migration mechanism.

Stored measurements remain in canonical units regardless of the
user's unit preference. Conversion to display units happens at the
presentation layer only. Changing the unit preference must never
alter a stored measurement.

Rationale

- Recommendation rules need to query and join against garden-
  affecting preferences. DataStore is not queryable for this purpose.
- Display preferences must survive as local state across garden
  imports and exports without polluting the interchange format.
- Splitting the two prevents the common failure where a unit
  preference change silently rewrites historical measurements.

Consequences

- Phase 0 establishes both patterns: a Room-backed Preference entity
  and a DataStore-backed PreferencesRepository.
- The export format includes garden-affecting preferences and
  excludes display preferences.
- Presentation of any measurement performs a canonical → display
  conversion using the current unit preference; the stored value is
  never rewritten.

Related documents

docs/V1_SCOPE.md §7
docs/MODULE_ARCHITECTURE.md §18
docs/V1_USER_EXPERIENCE.md §25
docs/IMPORT_EXPORT_SPECIFICATION.md §4
docs/V1_TECHNICAL_ARCHITECTURE.md §21