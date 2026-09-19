Garden Planner & Manager — V1 Technical Architecture

Document: docs/V1_TECHNICAL_ARCHITECTURE.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the technical architecture for Version 1 of the Garden Planner & Manager.

It translates the product scope, data model, UX specification, recommendation architecture, core architecture and module architecture into an implementable technical structure.

The architecture is intended to:

support the complete V1 feature set;

remain offline-first;

preserve garden history;

keep modules independently maintainable;

avoid unnecessary infrastructure;

support deterministic and explainable recommendations;

allow future expansion without requiring a fundamental rewrite;

remain understandable enough for a small development effort.


This document describes technical structure rather than individual screen designs or detailed database schemas.


---

2. Architectural principles

The V1 implementation follows these principles.

2.1 Local-first

The user's garden is primarily stored and operated locally.

Core garden functionality must not depend on an Internet connection.


---

2.2 Data is more durable than features

Garden history must survive:

application updates;

changes to UI;

changes to recommendation rules;

changes to individual modules;

introduction of new fields.


Features may evolve.

Historical garden information must remain interpretable.


---

2.3 Domain logic is independent of UI

Business rules must not be implemented directly inside widgets or screens.

The architecture should separate:

presentation;

application workflows;

domain logic;

persistence;

recommendation logic;

external integrations.


This allows the same domain behaviour to be used by different interfaces.


---

2.4 Modules communicate through stable interfaces

Modules should not directly manipulate another module's internal state.

For example:

The Planner may request suitability information from the Recommendation Engine.

It should not directly manipulate the Recommendation Engine's internal rules.


---

2.5 Deterministic before intelligent

V1 should favour:

explicit rules;

structured data;

transparent calculations;

deterministic recommendations;

explicit provenance.


Machine learning or opaque AI reasoning is not required for core V1 functionality.


---

2.6 Progressive precision

The technical architecture must support information becoming more precise over time.

For example:

unknown
    ↓
observation
    ↓
estimate
    ↓
measurement
    ↓
repeated measurements

The system must not overwrite earlier evidence merely because a later, more precise value exists.


---

2.7 Unknown is a valid state

Missing information is not automatically an error.

The architecture must distinguish between:

unknown;

not recorded;

not applicable;

not measured.



---

2.8 Explainability is a system requirement

Important derived results must retain enough information to explain:

what was considered;

what affected the result;

what information was missing;

what confidence applied;

which rules contributed.


The UI should not have to reconstruct reasoning from raw database records.


---

3. Technology baseline

V1 is a native Android application.

Core stack:

- Kotlin
- Jetpack Compose
- Android Jetpack (Navigation, lifecycle, ViewModel)
- Room over SQLite
- Kotlin Coroutines
- Kotlin Flow
- Hilt for dependency injection
- Android Studio / Gradle

The initial and only V1 target is Android. Local/offline operation is
the primary mode of use.

Cross-platform support is not a V1 requirement.

The domain layer should nevertheless remain sufficiently independent of
Android-specific types that it is testable without the Android runtime
and portable in principle. This is a design constraint on the domain
layer, not a commitment to a future cross-platform client.


---

4. High-level architecture

The application is divided into the following logical layers:

┌───────────────────────────────────────────┐
│                 Presentation              │
│                                           │
│ Screens / Widgets / Navigation / UI State │
└─────────────────────┬─────────────────────┘
                      │
┌─────────────────────▼─────────────────────┐
│             Application Layer             │
│                                           │
│ Workflows / Use Cases / Commands / Query  │
└─────────────────────┬─────────────────────┘
                      │
┌─────────────────────▼─────────────────────┐
│                Domain Layer                │
│                                           │
│ Entities / Value Objects / Rules / Logic  │
└─────────────────────┬─────────────────────┘
                      │
┌─────────────────────▼─────────────────────┐
│             Infrastructure                │
│                                           │
│ Persistence / Files / Import / Services   │
└───────────────────────────────────────────┘

Cross-cutting services such as:

logging;

configuration;

localisation;

date/time handling;

unit conversion;


must remain separate from individual feature modules.


---

5. Presentation layer

The presentation layer is responsible for:

displaying information;

accepting user input;

navigation;

temporary UI state;

validation feedback;

loading states;

error presentation.


It must not contain:

database queries;

recommendation algorithms;

persistence rules;

domain calculations;

import parsing logic.



---

6. Application layer

The application layer represents user-facing operations.

Examples include:

CreateGarden
AddGrowingSpace
AddPlant
RecordActivity
RecordObservation
RecordHarvest
CreatePlan
CompletePlan
GenerateRecommendations
ReviewHistory
CreateExperiment
ImportGarden
ExportGarden

Application services coordinate domain operations.

They may:

1. obtain required data;


2. invoke domain services;


3. save changes;


4. return a result for presentation.



They should not contain UI-specific behaviour.


---

7. Domain layer

The domain layer contains concepts that represent the garden and its behaviour.

Examples include:

Garden;

Area;

GrowingSpace;

Plant;

PlantInstance;

Cultivar;

Activity;

Observation;

Harvest;

Problem;

SoilInformation;

Experiment;

Plan;

Recommendation;

UserDecision;

Source;

Evidence.


The exact entity structure is defined by the Data Model document.

The domain layer must not depend on Compose, Android UI classes, or database implementation details.


---

8. Value objects

Small concepts with defined semantics should be represented as value objects where useful.

Examples include:

coordinates;

dimensions;

quantities;

date ranges;

geographic bounds;

measurement values;

confidence;

suitability results;

recommendation factors.


Value objects should validate their own basic invariants where practical.


---

9. Repository abstraction

Domain/application code should access persistent data through repository interfaces.

Conceptually:

GardenRepository
PlantRepository
ActivityRepository
ObservationRepository
HarvestRepository
PlanningRepository
RecommendationRepository
ExperimentRepository
SourceRepository

The application layer should depend on these interfaces rather than directly on the database.


---

10. Persistence implementation

V1 should use a local structured database suitable for:

relational data;

indexed queries;

transactions;

offline operation;

migrations;

moderate data volumes.


SQLite is an appropriate underlying storage technology.

Room is used on top of SQLite to provide:

typed access;

migrations;

query support;

transaction handling.


The specific ORM/query library is an implementation decision and should not become part of the domain model.


---

11. Database responsibilities

The database is responsible for:

durable storage;

relationships;

indexes;

constraints;

transactions;

migrations.


It should not be responsible for:

UI presentation;

recommendation explanations;

navigation;

localisation;

user-facing terminology.


Complex recommendation logic should remain in application/domain services rather than database queries.


---

12. Transactions

Operations that modify multiple related records should use database transactions where appropriate.

Examples:

Adding a plant may involve:

PlantInstance
PlantingEvent
spatial association

Recording a harvest may involve:

Harvest
quantity
plant association

Importing data may involve many records.

A partial transaction must not leave the database in an invalid state.


---

13. IDs

All persistent entities require stable identifiers.

IDs should:

be unique;

remain stable throughout the record's lifetime;

not depend on display names;

not depend on database row ordering.


UUIDs or another equivalent stable identifier strategy are appropriate.

Human-readable names must never be used as primary identifiers.


---

14. Timestamps

Records that require temporal history should retain appropriate timestamps.

Where applicable, distinguish:

occurrence date;

planned date;

created date;

updated date;

source date.


The application must not substitute the database creation timestamp for the date an event actually occurred.


---

15. Historical records

Historical information must be retained wherever it has garden significance.

Examples include:

previous planting;

historical soil measurements;

observations;

harvests;

interventions;

experiments;

recommendation decisions.


Current state may be derived from historical records where appropriate.


---

16. Soft deletion and archival

Important historical garden records should generally not be physically deleted through ordinary UI actions.

Possible states include:

active
archived

Where deletion is required, the application should determine whether:

the record can safely be removed;

dependent history must remain;

the operation requires explicit confirmation.


Historical integrity takes precedence over database cleanliness.


---

17. Data integrity

The application must enforce appropriate invariants.

Examples:

an activity must have a valid date;

a quantity must have a valid unit;

a plant instance must reference a valid plant;

a spatial reference must point to a valid spatial object;

vocabulary values must be valid for their field;

measurements must retain their units;

historical events must not be silently moved into the future.


Validation should occur at appropriate boundaries rather than relying solely on the UI.


---

18. Vocabulary implementation

Controlled vocabularies are represented internally by stable canonical identifiers.

Example:

perennial
partial_shade
moderate
user_observation

The UI obtains translated labels through localisation resources.

Business logic must use identifiers rather than display strings.

Vocabulary definitions are governed by:

docs/VOCABULARY_INDEX.md

and the associated vocabulary documents.


---

19. Localisation

Localisation must be implemented independently of domain data.

The application should support:

canonical identifier
        ↓
localisation key
        ↓
translated UI label

For example:

partial_shade

may be displayed as:

Partial shade

or:

Gedeeltelike skadu

The stored value does not change.


---

20. Dates, time and seasons

Dates must be handled using explicit date/time types rather than arbitrary strings.

The application must distinguish:

calendar dates;

date-times;

recurring schedules;

seasonal concepts.


Seasonal interpretation depends on:

geographic location;

hemisphere;

applicable knowledge;

date.


The application must not hard-code Northern Hemisphere seasonal assumptions.


---

21. Units

Internally, measurements should use consistent canonical units.

The presentation layer may convert values to user-preferred units.

Example:

stored: metres
display: feet

Conversions must preserve sufficient precision.

Original measured values should not be overwritten merely because the display unit changes.


---

22. Geographic data

Geographic information should be represented as structured data.

Examples:

latitude;

longitude;

bounding geometry;

dimensions;

area;

spatial relationships.


The application should not store geographic information solely as display text.


---

23. Spatial architecture

The spatial system must support:

points;

lines;

polygons;

dimensions;

relationships;

overlapping objects;

historical associations.


The map UI is a consumer of spatial data rather than the owner of it.

Spatial information should remain usable even if the map rendering library changes.


---

24. Map implementation

The map component should provide:

rendering;

selection;

editing;

panning;

zooming;

layer visibility;

filtering.


It should communicate with the application through spatial commands and queries.

Map-specific rendering objects must not become the application's canonical spatial records.


---

25. Spatial editing

When geometry changes:

1. validate the new geometry;


2. save the change;


3. recalculate derived spatial information;


4. preserve historical records associated with the space.



Changing the geometry of a growing space must not recreate the growing space as a new identity unless the user explicitly creates a new space.


---

26. Plant architecture

The plant system separates:

Plant
    ↓
Cultivar
    ↓
PlantInstance
    ↓
Activities / Observations / Harvests / Problems

The Plant represents general knowledge.

The PlantInstance represents the user's actual plant or planting.

This distinction must be preserved throughout the application.


---

27. Plant knowledge

Plant knowledge may include:

growing requirements;

lifecycle;

timing;

growth stages;

relationships;

common problems;

cultivation guidance;

source information.


This is reference/knowledge data rather than garden history.


---

28. Garden plant records

A PlantInstance connects a known plant to the user's garden.

It may contain:

plant;

cultivar;

location;

planting information;

current status;

relevant history.


Changes to the plant instance must not modify the underlying general Plant knowledge.


---

29. Activity architecture

Activities represent things that happened or were intended to happen.

Examples:

planting;

watering;

feeding;

pruning;

intervention;

harvesting.


An activity may be:

planned
completed
partially_completed
skipped
cancelled

Planned activities must never be interpreted as evidence that the action occurred.


---

30. Observation architecture

Observations represent recorded observations.

They must remain distinct from:

measurements;

diagnoses;

inferred conditions;

recommendations.


For example:

Observation:
Leaves are yellowing.

Possible inference:
Nutrient deficiency.

Possible diagnosis:
Not established.

The system must preserve this distinction.


---

31. Measurement architecture

Measurements must retain:

value;

unit;

date;

location/context;

method where known;

source;

confidence where appropriate.


A measurement must not be silently replaced by a later estimate or vice versa.


---

32. Soil information

Soil information may consist of:

observations;

estimates;

measurements;

test results;

inferred conditions.


The architecture must support all of these without pretending they have equivalent evidential strength.

For example:

Observed:
water pools after rain.

Estimated:
probably slow drainage.

Measured:
water remained for 6 hours.

These remain distinct records or evidence states as appropriate.


---

33. Recommendation architecture

The Recommendation Engine is a domain/application service.

It receives structured context and produces a structured recommendation.

Conceptually:

Garden context
       +
Plant knowledge
       +
Timing
       +
User preferences
       +
Garden history
       +
Rules
       ↓
Recommendation Engine
       ↓
Recommendation result
       +
Factors
       +
Reasons
       +
Confidence
       +
Missing information
       +
Explanation

The engine must not directly manipulate UI elements.


---

34. Recommendation engine inputs

Inputs may include:

plant;

cultivar;

growing space;

environmental conditions;

soil;

light;

water;

timing;

rotation history;

relationships;

infrastructure;

personal history;

user preferences.


Only relevant inputs should influence a particular recommendation.


---

35. Recommendation engine outputs

A recommendation should contain enough structured information to represent:

status;

factor results;

overall confidence;

supporting reasons;

limiting factors;

missing information;

conditional requirements;

applicable sources/rules;

relevant personal history.


The UI converts this into user-friendly explanations.


---

36. Recommendation persistence

Recommendations should generally be treated as derived results rather than permanent facts.

Where the application records a recommendation history, it should retain:

recommendation context;

result;

date/time;

rules/version used where necessary;

relevant evidence;

user decision.


The application must distinguish:

what the system recommended

from:

what the user decided

and:

what actually happened


---

37. Rule versioning

Recommendation rules may evolve.

If historical recommendations need to remain explainable, the system should retain an appropriate rule/version identifier.

A future rule change must not make an old recommendation appear as though it was generated by the new rules.


---

38. Personal history

Personal garden history should supplement general knowledge.

The system may identify patterns such as:

poor establishment
repeated disease
successful cultivar
repeated drainage issue

Personal history must be explicitly identified as personal evidence.

It must not silently become a universal horticultural rule.


---

39. User decisions

Recommendations and user decisions are separate concepts.

Example:

System:
Tomatoes are conditionally recommended.

User:
Overrides recommendation and plants tomatoes.

Later:
Planting event recorded.

These are three different facts.


---

40. Planner architecture

The Planner coordinates:

candidate selection;

timing;

growing-space availability;

recommendations;

planned activities;

user decisions.


The Planner should use the Recommendation Engine rather than implement an independent recommendation system.


---

41. Inbox architecture

The Inbox is a prioritised presentation of actionable or useful items.

Inbox items may originate from:

plans;

recommendations;

warnings;

observations;

experiments;

information requests.


The Inbox should not become the authoritative source of the underlying event.

An Inbox item should reference the relevant underlying record or action.


---

42. Inbox lifecycle

An Inbox item may be:

active
completed
muted
dismissed
expired

Mute and dismissal must remain distinct.

Muting controls future presentation.

Dismissal records that the user dealt with or intentionally dismissed the item.


---

43. Search architecture

Search should operate over canonical garden and knowledge entities.

Search may cover:

plants;

cultivars;

growing spaces;

activities;

observations;

problems.


Aliases should resolve to canonical entities where configured.

Search indexing should not alter the underlying records.


---

44. Filtering architecture

Filters should be implemented as queries over structured data.

Filters should not create duplicate copies of records.

Where filters become complex, the application should use reusable query specifications rather than screen-specific filtering logic.


---

45. Experiments

Experiments should reuse ordinary garden records.

An experiment may reference:

growing spaces;

plants;

activities;

observations;

measurements;

harvests.


The application should avoid creating a completely separate event-recording system for experiments.


---

46. Import architecture

Import is a controlled data pipeline:

File
 ↓
Parse
 ↓
Validate structure
 ↓
Validate vocabulary
 ↓
Validate relationships
 ↓
Detect conflicts
 ↓
Show summary
 ↓
User confirmation
 ↓
Transactional import

Invalid data must not silently enter the garden database.


---

47. Export architecture

Export should provide a complete enough representation of the user's garden to support:

backup;

migration;

inspection;

future application versions;

restoration.


Export should use canonical identifiers rather than translated labels.

Where practical, export should include:

garden data;

spatial data;

plants;

activities;

observations;

harvests;

measurements;

plans;

experiments;

relevant provenance.



---

48. Import/export compatibility

Import/export formats should be versioned.

Example:

format_version

A future application version should be able to determine how an older export should be interpreted.

Schema changes must use migrations or explicit compatibility handling.


---

49. Backup philosophy

Because V1 is local-first, backup/export is an important safety mechanism.

The architecture should make it possible to:

export garden data;

restore garden data;

migrate data to a newer application version.


Automatic cloud backup is not required for V1.


---

50. Offline architecture

Core functionality must work without connectivity.

Offline-capable functionality includes:

viewing the garden;

viewing plants;

recording activities;

recording observations;

recording harvests;

viewing history;

planning from locally available knowledge;

running offline recommendations using locally available data.


Connectivity-dependent functionality should be isolated.


---

51. External services

Future services may include:

weather;

external knowledge sources;

synchronisation;

community knowledge.


These should be implemented behind service interfaces.

Failure of an external service must not prevent core local garden functionality.


---

52. Connectivity state

The application should not make connectivity a prerequisite for ordinary actions.

Where an external service is unavailable, the application should clearly indicate that only the relevant feature is unavailable.

For example:

Weather unavailable offline.

Your saved garden information remains available.


---

53. Caching

External or derived information may be cached locally where useful.

Cached data must retain enough metadata to identify:

source;

retrieval time;

applicability;

version where relevant.


Stale external data must not silently appear to be current.


---

54. Application state

UI state should be separated from persistent domain state.

Examples of transient UI state:

selected map layer;

current filter;

expanded explanation;

current tab;

temporary form input.


These should not become persistent garden facts unless explicitly saved.


---

55. State management

Compose screens expose immutable UI state through ViewModels, with
Flow for reactive streams where the source is asynchronous or
database-backed.

The pattern is:

Composable
    ↓
ViewModel
    ↓
Use case
    ↓
Repository
    ↓
Room

Composables render state and emit user actions. They do not perform
persistence, invoke recommendation rules, or contain business logic.

The application should not introduce multiple competing
state-management patterns. Flow and ViewModel are sufficient for V1.


---

56. Dependency injection

Services and repositories should be provided through a controlled dependency mechanism.

Hilt is the chosen mechanism unless Phase 0 identifies a concrete reason to use something lighter.

This supports:

testing;

replacement of infrastructure;

module isolation;

future platform changes.


The UI should not instantiate database services directly.


---

57. Error handling

Errors should be represented in structured form.

The application should distinguish:

validation error
persistence error
import error
external service error
unexpected error

The presentation layer translates these into understandable messages.

Raw technical exceptions should not normally be shown to users.


---

58. Logging

V1 should include basic structured logging suitable for troubleshooting.

Logs should:

avoid sensitive garden information where practical;

distinguish expected validation failures from unexpected errors;

provide enough context to diagnose failures.


Logging must not become a substitute for proper error handling.


---

59. Performance

The architecture should optimise common local operations.

Priority operations include:

application startup;

Home;

Garden;

plant lookup;

Quick Add;

saving observations;

saving activities;

viewing history.


Database queries should use appropriate indexes.

Large datasets should be paginated or otherwise loaded incrementally where necessary.


---

60. Derived data

Derived values may be calculated from authoritative records.

Examples:

current plant count;

available space;

recommendation scores;

seasonal summaries;

historical patterns.


Derived data must be identifiable as derived.

Where cached for performance, it must be possible to regenerate it from authoritative data.


---

61. No duplicated authority

The system should avoid maintaining multiple competing sources of truth.

For example:

The authoritative record of a harvest is the Harvest record.

A dashboard count is derived.

The Inbox may reference the harvest but does not become a second harvest record.


---

62. Module boundaries

The principal technical modules are:

core
garden
plants
activities
observations
soil
planning
recommendations
problems
history
inbox
experiments
import_export

Not every module requires a completely independent package.

The purpose of the boundary is to define responsibilities and dependencies.


---

63. Core module

The Core module provides shared infrastructure and concepts such as:

IDs;

dates;

units;

vocabulary handling;

localisation;

result/error types;

common persistence abstractions;

configuration.


Core must remain small.

Feature-specific logic does not belong in Core merely because multiple screens use it.


---

64. Garden module

Responsible for:

Garden;

areas;

growing spaces;

spatial objects;

geometry;

map-facing queries;

spatial relationships.


It should not contain plant recommendation rules.


---

65. Plant module

Responsible for:

plant knowledge;

cultivars;

plant instances;

plant search;

plant relationships;

growing requirements.


General plant knowledge should remain separate from individual garden records.


---

66. Activity module

Responsible for:

activity records;

activity types;

planned/actual activity states;

recurring activity scheduling;

completion;

partial completion.



---

67. Observation module

Responsible for:

structured observations;

observation types;

observation evidence;

observation confidence;

observation history.


It may be consumed by recommendation and diagnostic systems.


---

68. Soil module

Responsible for:

soil observations;

soil measurements;

soil estimates;

soil tests;

soil characteristics;

soil-related provenance.


Soil calculations should remain separate from the UI.


---

69. Planning module

Responsible for:

planting plans;

planned activities;

scheduling;

candidate spaces;

planning workflows;

user decisions.


It consumes recommendations rather than duplicating their reasoning.


---

70. Recommendation module

Responsible for:

suitability evaluation;

rule execution;

factor evaluation;

confidence;

explanations;

missing information;

personal-history effects.


It must remain deterministic and inspectable in V1.


---

71. Problem module

Responsible for:

recorded problems;

symptoms;

pests;

diseases;

damage;

possible causes;

diagnostic reasoning.


Observation and diagnosis must remain distinct.


---

72. History module

History is primarily a query/presentation concern over authoritative records.

It should not duplicate all historical entities.

It provides:

chronological views;

spatial history;

plant history;

growing-space history;

activity history;

outcome history.



---

73. Inbox module

Responsible for:

prioritisation;

presentation state;

muting;

dismissal;

actionable items.


It references underlying domain records rather than replacing them.


---

74. Experiment module

Responsible for:

experiments;

hypotheses;

treatments;

controls;

measurements;

experiment results;

conclusions.


It should consume existing garden records wherever possible.


---

75. Import/export module

Responsible for:

file parsing;

format validation;

vocabulary validation;

conflict detection;

import previews;

export generation;

schema versions.


It must not contain garden-specific business rules that belong in domain modules.


---

76. Dependency direction

Dependencies should generally flow inward:

Presentation
    ↓
Application
    ↓
Domain
    ↓
Infrastructure abstractions

Infrastructure implementations may depend on domain/application contracts.

Feature modules may consume shared services but should avoid circular dependencies.


---

77. Avoiding circular dependencies

Examples of undesirable dependencies:

Plants → Recommendations → Plants

Instead:

Plants
   ↓
Plant knowledge interface

Recommendations
   ↓
Plant knowledge interface

Similarly:

Garden
   ↓
spatial information

Recommendations
   ↓
spatial information interface

The exact implementation may use shared domain types where appropriate, but ownership must remain clear.


---

78. Shared domain types

Shared concepts should only be centralised when their semantics are genuinely shared.

Examples:

EntityId;

Quantity;

DateRange;

Coordinate;

Confidence;

VocabularyValue.


Feature-specific concepts should remain in their owning module.


---

79. File/project structure

79. File and project structure

A practical Android project structure is:

app/
└── src/main/
    ├── java/<package>/
    │   ├── <App>Application.kt
    │   │
    │   ├── data/
    │   │   ├── database/       Room database, migrations
    │   │   ├── dao/            DAOs
    │   │   ├── entities/       Room entities
    │   │   ├── repositories/   Repository implementations
    │   │   └── importexport/   Import/export implementation
    │   │
    │   ├── domain/
    │   │   ├── model/          Domain entities and value objects
    │   │   ├── rules/          Deterministic rules
    │   │   ├── recommendations/Recommendation engine
    │   │   └── usecases/       Use cases
    │   │
    │   ├── ui/
    │   │   ├── navigation/
    │   │   ├── components/     Shared Compose components
    │   │   ├── theme/          Design tokens and theme
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
    └── res/                    String resources, images, localisation

The exact directory names may evolve, but the separation of
responsibilities must remain.

Room entities, domain models and UI models are not assumed to be the
same type. Mapping between layers is explicit where the distinction
matters.


---

80. Feature structure

Where practical, a feature should keep its Compose presentation,
application/domain logic and Room-backed data access close together
without collapsing the layer boundaries.

A feature may be organised as:

features/plants/
├── ui/                Compose screens and components
├── application/       Use cases and coordination
├── domain/            Plant-specific domain types and rules
└── data/              Room entities, DAOs and repositories for this feature

This keeps feature code together while preserving the horizontal
architectural layers. Cross-feature access still goes through defined
interfaces or shared domain types.


---

81. Do not over-abstract V1

The architecture should not introduce abstractions merely because they are theoretically possible.

Avoid:

unnecessary service layers;

excessive interfaces;

speculative plugin systems;

premature microservices;

distributed architecture;

complex event infrastructure.


An abstraction is justified when it provides a real benefit such as:

testability;

replaceable infrastructure;

clear module boundaries;

future compatibility;

reduced coupling.



---

82. Event architecture

V1 does not require a distributed event bus.

Where modules need to react to changes, ordinary application services and domain events may be sufficient.

If domain events are introduced, they should represent meaningful domain occurrences such as:

PlantAdded
ActivityCompleted
ObservationRecorded
HarvestRecorded
GrowingSpaceChanged

Events should not become a second persistence system.


---

83. Notifications

Notification generation should be based on domain/application information rather than UI widgets.

V1 notification functionality should remain intentionally simple.

A notification should reference the underlying action or record wherever possible.


---

84. Recurring activities

Recurring plans should store the schedule definition.

Generated occurrences should remain distinguishable from the recurrence rule itself.

Example:

Recurrence:
water every 3 days

Occurrence:
water Bed 2 on 18 August

Completing one occurrence must not rewrite the recurrence definition.


---

85. Search indexing

Search indexes are derived infrastructure.

They may be rebuilt from authoritative records.

Search indexing must not become a source of truth.


---

86. Migration architecture

Database migrations must be versioned.

A migration should:

1. identify the previous schema;


2. apply a deterministic transformation;


3. preserve existing information;


4. validate the resulting schema/data;


5. allow the application to continue using the upgraded data.



Migrations must not fabricate information that did not exist previously.


---

87. Vocabulary migrations

Vocabulary changes require the same historical discipline as schema changes.

If an identifier is replaced:

old_id → new_id

the migration must explicitly map the old value.

If the meaning cannot be safely mapped, the old value should remain interpretable rather than being silently converted.


---

88. Recommendation migrations

Changes to recommendation rules must not rewrite historical facts.

For example:

A later rule change may produce a different recommendation today.

It must not alter:

what the user previously recorded;

what the system previously recommended where recommendation history is retained;

what the user decided.



---

89. Testing strategy

V1 should prioritise tests around business-critical behaviour.

The testing pyramid should favour:

1. unit tests;


2. domain/application tests;


3. repository/integration tests;


4. focused Compose UI tests;


5. end-to-end tests for critical workflows.



Not every visual detail requires an end-to-end test.


---

90. Highest-priority tests

Particular attention should be given to:

historical preservation;

planned versus completed activities;

partial completion;

recommendation calculations;

recommendation explanations;

confidence handling;

unknown information;

seasonal/hemisphere handling;

spatial calculations;

import validation;

export/import round trips;

database migrations;

user overrides.



---

91. Recommendation testing

Recommendation tests should use controlled scenarios.

Example:

Input:
summer
good light
adequate water
suitable soil
recent rotation conflict

Expected:
recommendation = conditional
rotation factor = poor
explanation includes rotation concern
confidence reflects available information

The exact expected result should be deterministic.


---

92. Import/export testing

A valid export should be capable of being imported without loss of supported information.

Tests should cover:

empty gardens;

normal gardens;

historical records;

unknown values;

deprecated vocabulary values;

invalid vocabulary values;

duplicate IDs;

conflicts;

partial failures;

schema version changes.



---

93. Offline testing

Core workflows must be tested without network access.

At minimum:

open application;

view garden;

add plant;

record activity;

record observation;

record harvest;

view history;

run local recommendations.



---

94. Accessibility testing

The technical implementation should support:

semantic labels;

scalable text;

keyboard/accessibility navigation where applicable;

touch targets;

screen readers;

non-colour-only status communication.


Accessibility should be considered during implementation rather than added after the UI is complete.


---

95. Security and privacy

V1 primarily stores private garden information locally.

The application should:

avoid unnecessary external transmission;

avoid silently sharing garden information;

clearly distinguish future optional contribution functionality;

protect exported data appropriately within the limits of the platform.


Exact location should not be shared externally by default.


---

96. No mandatory account architecture

V1 does not require a mandatory cloud account.

The application should be usable as a local garden application without registration.

Any future account/synchronisation architecture should be additive rather than a prerequisite for core garden functionality.


---

97. Future synchronisation

Future synchronisation may introduce:

cloud storage;

multi-device support;

conflict resolution;

authentication.


V1 should not implement these prematurely.

However, stable IDs, timestamps, provenance and modular persistence should avoid making future synchronisation unnecessarily difficult.


---

98. Future community knowledge

Community knowledge is explicitly separated from private garden data.

A future contribution system must introduce a controlled pipeline:

Private observation
        ↓
User chooses to contribute
        ↓
Contribution record
        ↓
Review/aggregation
        ↓
Potential knowledge

The V1 architecture should not automatically expose private garden records to a community system.


---

99. AI and machine learning

AI is not required for the core V1 architecture.

If AI-assisted functionality is added later, it should operate through a clearly isolated service.

AI-generated suggestions must not automatically become:

authoritative plant knowledge;

verified evidence;

diagnoses;

permanent recommendations.


AI output should retain appropriate provenance and uncertainty.


---

100. Weather and external environmental data

Weather is not a core dependency of the V1 garden database.

A future weather service may provide:

current conditions;

forecasts;

historical weather;

frost information;

rainfall.


Such data should be treated as external evidence and must not overwrite user-recorded observations or measurements.


---

101. Configuration

Application configuration should distinguish between:

fixed application behaviour;

user preferences;

vocabulary data;

horticultural knowledge;

feature availability.


Configuration should not be scattered throughout the codebase.


---

102. Feature flags

Feature flags may be used during development for incomplete or experimental functionality.

They should not become a permanent substitute for proper architecture.

Experimental recommendation rules, for example, should be clearly marked as experimental.


---

103. Data flow example — Quick Add

A typical Quick Add workflow is:

User
 ↓
Quick Add UI
 ↓
Record Activity command
 ↓
Activity domain validation
 ↓
Activity repository
 ↓
Local database
 ↓
Success result
 ↓
UI confirmation

The UI does not write directly to the database.


---

104. Data flow example — Observation

User
 ↓
Observation UI
 ↓
Record Observation use case
 ↓
Observation validation
 ↓
Observation repository
 ↓
Local database
 ↓
Optional recommendation/diagnostic refresh
 ↓
UI confirmation

Recording an observation should remain useful even if later diagnostic processing fails.


---

105. Data flow example — Recommendation

Planner
 ↓
Recommendation request
 ↓
Gather garden context
 ↓
Gather plant knowledge
 ↓
Gather relevant history
 ↓
Apply deterministic rules
 ↓
Evaluate factors
 ↓
Calculate result/confidence
 ↓
Generate explanation
 ↓
Return Recommendation
 ↓
Planner/UI

The recommendation engine should not directly control navigation or UI.


---

106. Data flow example — Import

User selects file
 ↓
Import parser
 ↓
Format validation
 ↓
Vocabulary validation
 ↓
Relationship validation
 ↓
Conflict detection
 ↓
Preview
 ↓
User confirms
 ↓
Transaction
 ↓
Database
 ↓
Import result

A failed import must not leave a partially imported dataset unless the user explicitly chose an operation that permits partial import and the result is clearly reported.


---

107. Data flow example — History

History should be assembled from authoritative records.

User requests history
 ↓
History query
 ↓
Relevant repositories
 ↓
Temporal/spatial filtering
 ↓
Chronological result
 ↓
Presentation

History should not require a separate duplicated "history database."


---

108. Data flow example — Personal pattern

A personal pattern may be generated from:

Historical observations
+
Activities
+
Harvests
+
Growing conditions
+
Outcomes

The resulting pattern should be explicitly labelled as:

personal history

or equivalent.

It should not be inserted into general plant knowledge automatically.


---

109. Data authority hierarchy

When information conflicts, the application should preserve the evidence rather than silently overwrite it.

A useful conceptual distinction is:

Observed/measured user evidence
        +
documented external evidence
        +
derived/inferred information
        +
recommendation
        +
user decision
        +
actual outcome

These are different information types.

There is no single universal hierarchy in which one always overrides every other type.

Context determines relevance.


---

110. Source and provenance

Knowledge records should be able to identify their source/provenance where applicable.

The architecture should support:

source identity;

source type;

source status;

source relationship;

applicability;

retrieval/publication date;

confidence where appropriate.


The user interface can progressively disclose these details.


---

111. Derived versus authoritative data

Every important stored value should have a clear conceptual status:

authoritative user/system record

or:

derived result

Derived values must be regenerable where practical.

For example:

Planting history

is authoritative.

Rotation warning

is derived from history and knowledge.


---

112. Caching derived results

Derived results may be cached for performance.

Cached recommendations must be invalidated or regenerated when materially relevant inputs change.

Potential inputs include:

plant;

growing space;

conditions;

timing;

history;

rules;

preferences.


Stale results must not be presented as current without appropriate handling.


---

113. Concurrency

V1 is primarily a single-user local application.

Complex multi-user concurrency is therefore not required.

Nevertheless, repository operations should use transactions and consistent state updates so that future synchronisation remains possible.


---

114. Recovery

The application should favour recoverable operations.

Where practical:

use transactions;

preserve historical records;

support undo;

avoid destructive mutation;

provide export/backup;

validate before committing changes.



---

115. Technical UX alignment

The technical architecture must directly support the UX principles.

In particular:

UX requirement	Technical support

Quick recording	Local repositories and lightweight commands
Progressive precision	Distinct information states
Explainable recommendations	Structured recommendation results
Unknown information	Explicit nullable/stateful values
Historical preservation	Immutable/archival event records where appropriate
Offline operation	Local database and local domain services
Progressive complexity	Modular domain and presentation layers
User overrides	Explicit UserDecision records
Planned vs actual	Separate planning and occurrence state
Partial completion	Per-target completion records
Localisation	Canonical IDs + localisation layer
Import/export	Versioned data pipeline
Future expansion	Stable module boundaries



---

116. Implementation priority

Implementation should proceed in an order that protects the core system.

Recommended technical sequence:

1. project foundation;


2. local database;


3. migrations;


4. core vocabulary system;


5. garden/spatial model;


6. plant model;


7. activity/event model;


8. observation model;


9. history queries;


10. Quick Add;


11. planner;


12. recommendation engine;


13. soil functionality;


14. Inbox;


15. experiments;


16. import/export;


17. optional integrations.



The exact development order may change where dependencies require it, but core data integrity should remain the priority.


---

117. V1 technical non-goals

V1 should not require:

microservices;

cloud-hosted databases;

mandatory user accounts;

real-time multi-user synchronisation;

machine-learning infrastructure;

distributed event processing;

professional GIS infrastructure;

complex analytics infrastructure;

mandatory external APIs;

a cloud recommendation engine.


These would introduce complexity without being necessary for the V1 product.


---

118. Architectural decision rule

When choosing between two technically valid approaches, prefer the approach that:

1. preserves garden data;


2. keeps core functionality offline;


3. reduces coupling;


4. is easier to test;


5. is easier to understand;


6. is easier to migrate;


7. supports explainability;


8. avoids unnecessary infrastructure.



Technical sophistication should not be mistaken for architectural quality.


---

119. Definition of technical readiness

The V1 technical architecture is considered sufficiently implemented when:

core garden data persists reliably;

migrations work;

historical records are preserved;

core workflows work offline;

modules communicate through defined boundaries;

recommendations are deterministic and explainable;

planned and actual activities remain distinct;

unknown information is preserved explicitly;

import/export is validated and versioned;

localisation does not affect business logic;

critical workflows have automated tests;

future modules can be added without restructuring the entire application.



---

120. Final architectural principle

> Build a durable local garden system first; add intelligence and integrations around it.



The V1 application should have a relatively simple technical foundation:

Android application (Kotlin + Jetpack Compose)
↓
Application services
↓
Domain modules
↓
Local repositories (Room)
↓
SQLite database

Around this foundation sit:

Recommendations
Planning
History
Observations
Soil
Inbox
Experiments
Import/Export

The architecture should remain understandable, modular and testable.

Most importantly, the technical architecture must preserve the distinction between:

what the gardener recorded,

what the application knows,

what the application inferred,

what the application recommended,

what the gardener decided,

and

what actually happened.

That distinction is fundamental to the reliability, explainability and long-term usefulness of the Garden Planner & Manager.