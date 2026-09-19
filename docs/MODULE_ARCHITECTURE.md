Garden Planner & Manager — Module Architecture

Document: docs/MODULE_ARCHITECTURE.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the functional module boundaries of the Garden Planner & Manager application.

It describes:

what each module is responsible for;

what each module owns;

what each module may depend on;

how modules communicate;

which capabilities are core;

which capabilities are optional;

how the architecture prevents unrelated features from becoming tightly coupled.


This document complements:

PROJECT_OVERVIEW.md

CORE_ARCHITECTURE.md

V1_DATABASE_SCHEMA.md

V1_SCOPE.md

V1_USER_EXPERIENCE.md

V1_SCREEN_SPECIFICATION.md

RECOMMENDATION_ENGINE.md

V1_IMPLEMENTATION_PLAN.md

TESTING_STRATEGY.md


It does not define individual database tables or screen layouts.


---

2. Architectural principle

The application should be understood as a set of cooperating modules around a stable garden record.

The modules should communicate through explicit domain concepts rather than through direct knowledge of each other's internal implementation.

The central distinction is:

Garden data
    ↓
Knowledge
    ↓
Reasoning
    ↓
Planning / recommendations
    ↓
User decisions
    ↓
Recorded outcomes
    ↓
History

No module should silently collapse these layers into one another.

In particular:

knowledge is not garden history;

observations are not diagnoses;

recommendations are not facts;

plans are not completed activities;

user decisions are not evidence that an action occurred;

inferred information is not measured information.



---

3. Module design principles

3.1 Clear ownership

Every significant concept should have a primary owning module.

Other modules may reference that concept but should not maintain competing versions of it.


---

3.2 One source of truth

A concept should have one authoritative representation.

For example:

a growing space belongs to the garden domain;

a plant definition belongs to plant knowledge;

a garden plant belongs to the user's garden records;

an activity belongs to activity/history;

a recommendation belongs to recommendation reasoning;

a plan belongs to planning.


Modules should not duplicate these records merely for convenience.


---

3.3 Dependency direction

Dependencies should generally flow toward stable foundational services.

A simplified dependency direction is:

Foundation
    ↓
Garden / Knowledge / Records
    ↓
Planning / Recommendation / History
    ↓
Presentation

Higher-level modules may consume lower-level services.

Lower-level modules should not depend on UI screens.


---

3.4 No circular module dependencies

Circular dependencies should be avoided.

For example:

Recommendation → Plant Knowledge
Plant Knowledge → Recommendation

should not exist.

Instead:

Plant Knowledge
        ↓
Recommendation

The recommendation engine may consume plant knowledge, but plant knowledge does not depend on the recommendation engine.


---

3.5 Modules communicate through contracts

A module should expose a small, stable interface rather than its internal classes, database implementation or UI state.

Examples:

PlantKnowledge.getPlant()
Garden.getGrowingSpace()
ActivityLog.recordActivity()
RecommendationEngine.evaluate()
Planner.createPlan()
History.getHistory()

The exact implementation may change without requiring unrelated modules to change.


---

4. Module overview

The V1 application is organised into the following principal modules:

Foundation
│
├── Garden
├── Plant Knowledge
├── Garden Records
├── Activity & History
├── Soil & Conditions
├── Planning
├── Recommendation Engine
├── Problems & Observations
├── Inbox
└── Import / Export

Some modules may be implemented as separate packages, services or feature areas.

The architectural boundary is more important than the physical source-code directory structure.


---

5. Foundation module

Responsibility

Provides application-wide primitives and services used by other modules.

Typical responsibilities include:

canonical IDs;

dates and time;

units;

localisation interfaces;

geographic primitives;

vocabulary resolution;

validation primitives;

persistence abstractions;

common error handling;

application configuration;

migration support.



---

Owns

Foundation owns only genuinely shared infrastructure concepts.

It must not become a dumping ground for feature-specific business logic.


---

Must not own

Foundation must not own:

plant knowledge;

garden recommendations;

planting plans;

garden activities;

observations;

diagnoses;

user preferences;

UI workflows.



---

6. Garden module

Responsibility

Represents the physical structure and spatial organisation of the user's garden.

It provides the authoritative representation of:

gardens;

areas;

growing spaces;

spatial objects;

geometry;

dimensions;

garden infrastructure;

spatial relationships;

garden-level conditions;

map state and spatial context.



---

Owns

The Garden module owns:

Garden
Area
GrowingSpace
SpatialObject
Geometry
Garden-level spatial context

The exact entities are defined by V1_DATABASE_SCHEMA.md.


---

Provides

Other modules may ask the Garden module for:

available growing spaces;

dimensions;

location;

spatial relationships;

current spatial context;

historical spatial context;

conditions associated with a location.



---

Does not own

Garden does not own:

plant knowledge;

plant species definitions;

cultivars as global knowledge;

recommendation rules;

completed activities;

harvest records;

diagnoses.


It may display these things spatially without owning their underlying records.


---

7. Plant Knowledge module

Responsibility

Provides general horticultural knowledge about plants.

It answers questions such as:

> What is this plant?



> What conditions does it generally require?



> When can it be grown?



> What relationships or problems are associated with it?




---

Owns

Plant knowledge includes concepts such as:

plants;

plant classifications;

cultivars;

growth characteristics;

lifecycle;

growing requirements;

timing knowledge;

plant relationships;

known problems;

relevant knowledge sources.



---

Important distinction

Plant Knowledge describes what is generally known.

It does not represent what is actually happening in the user's garden.

For example:

Plant Knowledge
Tomato
    lifecycle = annual
    frost sensitivity = high

is different from:

User Garden
Tomato
    Bed 2
    planted 2026-10-15


---

Does not own

Plant Knowledge must not own:

the user's actual plantings;

personal observations;

personal harvests;

garden-specific outcomes;

user decisions.



---

8. Garden Records module

Responsibility

Represents the user's actual garden state.

This is the bridge between general knowledge and the individual garden.

It answers:

> What do I actually have, where is it, and what is intended?




---

Owns

The module represents user-specific instances such as:

plants in the garden;

planned plantings;

cultivar selections;

associations with growing spaces;

user preferences;

user-specific plant state where applicable.


A garden plant references plant knowledge rather than duplicating it.


---

Example

Plant Knowledge
    Tomato
        ↓
Garden Record
    Tomato in Bed 2
        ↓
Activities
    planted
    watered
    harvested
        ↓
History


---

Does not own

Garden Records does not own:

general plant requirements;

recommendation rules;

global diagnoses;

source records;

generic horticultural claims.



---

9. Activity & History module

Responsibility

Records what actually happened in the garden and preserves the garden's historical record.

This is one of the most important modules in the application.


---

Owns

Activities may include:

planting;

watering;

feeding;

pruning;

harvesting;

interventions;

other recorded garden actions.


It also supports historical retrieval of relevant events.


---

Core principle

An activity is evidence of an occurrence.

A plan is not an activity.

For example:

Plan:
Water Bed 2 tomorrow.

Activity:
Bed 2 was watered.

The first does not imply the second.


---

History

History should be reconstructed from durable records rather than from a separate manually maintained "history" database.

For example:

Planting activity
    ↓
Plant history

Harvest activity
    ↓
Harvest history

Observation
    ↓
Observation history

Where a dedicated historical state is required, it must remain traceable to its underlying records.


---

10. Soil & Conditions module

Responsibility

Represents environmental conditions relevant to gardening.

This includes both measured information and less precise observations.


---

Owns

Potential condition categories include:

soil;

moisture;

drainage;

texture;

compaction;

pH;

fertility;

salinity;

light;

temperature;

frost-related conditions;

other relevant environmental conditions.



---

Progressive precision

The module must support:

Unknown
    ↓
Observation
    ↓
Estimate
    ↓
Measurement
    ↓
Repeated measurements

It must not require measurement when a useful observation is sufficient.


---

Evidence distinction

For example:

Observation:
Water pools after heavy rain.

Estimate:
Drainage probably slow.

Measurement:
Water remains for approximately 6 hours.

These are different information states and must remain distinguishable.


---

Does not own

The Soil & Conditions module does not own:

plant suitability decisions;

recommendations;

planting plans.


Those modules consume condition information.


---

11. Problems & Observations module

Responsibility

Records things the gardener observes and represents problems or possible causes without confusing observation with diagnosis.


---

Owns

Potential records include:

observations;

symptoms;

damage;

pests;

diseases;

problems;

severity;

diagnostic evidence;

possible causes.



---

Observation principle

The application must preserve what the gardener actually observed.

Example:

Observation:
Leaves are yellowing.

This must not automatically become:

Diagnosis:
Nutrient deficiency.

The system may reason about possible causes separately.


---

Diagnostic reasoning

Where the application produces possible explanations, those explanations should remain:

separate from the observation;

confidence-qualified;

explainable;

revisable.



---

12. Recommendation Engine

Responsibility

Evaluates garden-specific suitability and produces explainable recommendations.

The Recommendation Engine is a reasoning module, not a knowledge repository.


---

Consumes

It may consume:

plant knowledge;

cultivar information;

garden conditions;

growing-space information;

current plants;

historical activities;

rotation history;

relationships;

user preferences;

timing;

recommendation rules;

uncertainty information.



---

Produces

A recommendation may contain:

recommendation state;

factor results;

reasons;

confidence;

missing information;

constraints;

conditions;

relevant evidence references.



---

Does not own

The Recommendation Engine must not become the authoritative owner of:

plant facts;

soil measurements;

activities;

observations;

garden geometry.


It evaluates those records.


---

Explainability

A recommendation should be inspectable.

The user should be able to determine:

What was considered?
        ↓
What affected the result?
        ↓
What is uncertain?
        ↓
Why did the recommendation reach this conclusion?


---

13. Planning module

Responsibility

Supports forward-looking decisions.

It answers:

> What do I intend to do?



and:

> What could I grow here?




---

Owns

Planning may include:

planting plans;

planned activities;

candidate planting decisions;

schedules;

planning status;

user decisions;

planning priorities.



---

Relationship with recommendations

Planning may request recommendations.

The Recommendation Engine evaluates suitability.

Planning turns the result into an actionable user decision.

Conceptually:

Growing Space
      ↓
Recommendation Engine
      ↓
Candidates
      ↓
User decision
      ↓
Plan


---

Important distinction

A recommendation does not create a plan automatically.

A plan does not prove that an activity occurred.


---

14. Inbox module

Responsibility

Provides a prioritised presentation of things that may require the gardener's attention.

Inbox is not the owner of the underlying garden facts.


---

Consumes

Inbox may consume signals from:

planning;

recommendations;

observations;

problems;

experiments;

reminders;

garden history.



---

Produces

Inbox items such as:

actions;

warnings;

observation requests;

information requests;

experiments;

knowledge items.



---

Ownership principle

Inbox items are derived work items.

For example:

Rotation issue
    ↓
Inbox item

The Inbox does not become the source of truth for the rotation issue.


---

Mute and dismiss

Inbox state must remain separate from the underlying event.

Muting:

> Do not show this prominently for now.



Dismissing:

> This item has been dealt with or is no longer relevant.



Neither should silently delete the underlying garden record.


---

15. Import & Export module

Responsibility

Moves structured garden or knowledge data into and out of the application.


---

Owns

The Import/Export module owns:

file format handling;

schema validation;

vocabulary validation;

import staging;

conflict detection;

import results;

export formatting.



---

Import principle

Import should occur through a staged process:

Select file
    ↓
Parse
    ↓
Validate
    ↓
Identify warnings/errors/conflicts
    ↓
User review
    ↓
Confirm
    ↓
Apply
    ↓
Report result

Invalid data must not silently modify the garden.


---

Does not own

Import/Export does not own the underlying garden or knowledge records.

It calls the appropriate domain services to create or update them.


---

16. Search module

Responsibility

Provides consistent retrieval of garden and knowledge records.

Search should support concepts such as:

plants;

cultivars;

growing spaces;

problems;

activities;

observations.



---

Ownership

Search does not own the records being searched.

It provides an access mechanism over other modules.


---

Canonical identity

Search results should resolve to the canonical underlying entity.

For example:

"Roma tomato"
"tomato"

may lead to the appropriate plant or cultivar records according to configured aliases.

Search must not create duplicate plant identities merely because users use different names.


---

17. Map / Spatial Presentation module

Responsibility

Provides interactive spatial presentation and editing of garden information.

This module is primarily a presentation and interaction layer over the Garden domain.


---

Provides

map display;

pan;

zoom;

selection;

geometry editing;

layer control;

filters;

historical map views.



---

Important boundary

The map does not own the authoritative garden geometry.

Instead:

Map UI
    ↓
Garden domain
    ↓
Stored geometry

This prevents map implementation details from becoming part of the core domain model.


---

18. User Preferences module

Responsibility

Stores optional preferences that influence application behaviour.

Examples include:

favourite plants;

plants to avoid;

water conservation;

pollinator priorities;

low-maintenance preference;

food-production priorities;

experimentation preference.



---

Recommendation relationship

Preferences may influence recommendations.

They should not silently alter the underlying suitability facts.

For example:

Lettuce
Suitability: Excellent

User preference:
Avoid lettuce

Recommendation:
Excluded by preference

The user can therefore understand the difference between horticultural suitability and personal preference.


---

19. Experiment module

Responsibility

Supports optional structured garden experiments.

Experiments are an advanced feature and should not be required for ordinary garden management.


---

Owns

An experiment may define:

question;

hypothesis;

treatment;

control;

timeframe;

measurements;

observations;

result;

conclusion.



---

Relationship to garden records

Experiments should reuse ordinary:

activities;

observations;

measurements;

harvests;

outcomes.


The application should not create an entirely separate parallel event system for experiments.


---

Result principle

The module must distinguish:

Observed result

from:

Conclusion

A garden experiment may suggest a local finding without becoming a universal horticultural rule.


---

20. Knowledge / Provenance support

Provenance is a cross-cutting capability rather than a separate user-facing knowledge module.

Relevant information may include:

evidence type;

source;

source status;

data origin;

applicability;

geographic context;

adaptation;

confidence;

knowledge maturity.



---

Ownership

Knowledge records own their relevant provenance relationships.

Shared provenance primitives are provided by the architecture's foundational data model.


---

Principle

The system must be able to distinguish:

General knowledge
User observation
User measurement
Imported information
System inference
Recommendation
Experimental result

These must not be silently merged.


---

21. Module interaction model

The principal relationships are:

┌─────────────────┐
                    │   Foundation    │
                    └────────┬────────┘
                             │
             ┌───────────────┼────────────────┐
             │               │                │
             ▼               ▼                ▼
       ┌──────────┐   ┌──────────────┐   ┌──────────────┐
       │  Garden  │   │ Plant        │   │ Soil &       │
       │          │   │ Knowledge    │   │ Conditions   │
       └────┬─────┘   └──────┬───────┘   └──────┬───────┘
            │                │                  │
            └────────┬───────┴──────────┬───────┘
                     │                  │
                     ▼                  ▼
              ┌──────────────┐   ┌───────────────┐
              │ Garden       │   │ Problems &    │
              │ Records      │   │ Observations  │
              └──────┬───────┘   └───────┬───────┘
                     │                   │
                     └─────────┬─────────┘
                               ▼
                    ┌────────────────────┐
                    │ Recommendation     │
                    │ Engine             │
                    └─────────┬──────────┘
                              │
                    ┌─────────▼──────────┐
                    │ Planning           │
                    └─────────┬──────────┘
                              │
                    ┌─────────▼──────────┐
                    │ Activity & History │
                    └────────────────────┘

Presentation-oriented modules consume these domain capabilities.


---

22. Presentation architecture

The user interface should not contain the core business rules.

A screen may:

collect input;

display information;

initiate an action;

request a recommendation;

show validation;

provide navigation.


It should not independently calculate authoritative suitability or maintain its own version of garden state.


---

23. Screen-to-module relationship

The primary V1 navigation can be mapped as follows:

UI area	Primary module(s)

Home	Inbox, Planning, Garden Records, Activity & History, Recommendation Engine
Garden	Garden, Garden Records, Map/Spatial Presentation
Plants	Plant Knowledge, Garden Records
Planner	Planning, Recommendation Engine, Garden, Plant Knowledge
Inbox	Inbox plus source modules
Quick Add	Activity & History, Garden Records, Soil & Conditions, Problems & Observations


The UI may combine information from multiple modules.

This does not mean those modules should be merged internally.


---

24. Quick Add architecture

Quick Add is a UI workflow over existing domain modules.

It must not become a separate data model.

For example:

Quick Add → Water
       ↓
Activity & History
       ↓
Watering Activity

and:

Quick Add → Observe → Soil
       ↓
Soil & Conditions
       ↓
Soil Observation

This allows Quick Add to remain simple while preserving clean domain ownership.


---

25. Context propagation

Modules should support contextual actions without creating hidden dependencies.

Example:

Garden
  → Bed 2
      → Quick Add
          → Water

The Quick Add workflow can receive:

context = Bed 2

and use that to prepopulate the likely location.

The Activity module remains responsible for validating and storing the activity.


---

26. Historical context

Modules should preserve relationships to historical records.

For example:

Growing Space
    ↓
Planting activities
    ↓
Plant history
    ↓
Historical recommendation context

Changing the current state must not erase historical evidence.


---

27. Recommendation dependency boundaries

The Recommendation Engine may depend on:

Garden
Plant Knowledge
Garden Records
Soil & Conditions
Activity & History
Problems & Observations
User Preferences
Planning context
Rule definitions

Those modules must not depend on the Recommendation Engine merely to represent their own data.

This is particularly important for:

Plant Knowledge;

Garden;

Activities;

Observations.



---

28. Planning dependency boundaries

Planning may depend on:

Garden
Plant Knowledge
Garden Records
Recommendation Engine
Activity & History
User Preferences

Planning owns planning state.

It does not own the underlying facts used to make the plan.


---

29. History dependency boundaries

History is principally reconstructed from durable garden records.

It may consume:

activities;

observations;

harvests;

measurements;

planting records;

experiments;

decisions.


Other modules may query history.

History must not require recommendations to exist.

A user should still be able to review their garden history if the recommendation engine is unavailable.


---

30. Optional module isolation

Optional functionality must fail independently where practical.

Examples include:

future weather integration;

community knowledge;

advanced experiments;

external data services.


Failure of an optional module must not prevent core functions such as:

opening the garden;

recording activities;

viewing plants;

viewing history;

using stored recommendations or rules offline.



---

31. Offline architecture

Core domain modules should operate locally.

At minimum, offline operation should support:

garden viewing;

plant viewing;

adding and editing garden records;

Quick Add;

observations;

harvests;

history;

stored plant knowledge;

deterministic recommendations based on locally available data.


External services must be treated as optional dependencies.


---

32. Future weather module

Weather should not be embedded directly into Garden or Recommendation Engine logic.

A future weather integration should provide a defined weather data interface.

Conceptually:

Weather Provider
       ↓
Weather Data Service
       ↓
Recommendation / Planning

If unavailable:

Weather unavailable
       ↓
Use stored information
       ↓
Reduce confidence where appropriate

The rest of the application must continue functioning.


---

33. Future community knowledge module

Community knowledge must remain separate from private garden records.

Conceptually:

Private Garden
     ↓
Optional contribution
     ↓
Contribution workflow
     ↓
Review
     ↓
Community knowledge

A user's private records must never automatically become community knowledge.


---

34. Future AI integration

AI must not become a foundational dependency of the application.

Future AI features may assist with:

natural-language search;

explanation;

summarisation;

optional identification assistance;

optional data-entry assistance.


AI output must not silently overwrite authoritative garden data.

Deterministic core functions should remain usable without AI.


---

35. Data ownership matrix

Concept	Owning module

Garden	Garden
Growing space	Garden
Spatial geometry	Garden
Plant definition	Plant Knowledge
Cultivar knowledge	Plant Knowledge
Garden plant	Garden Records
Soil condition	Soil & Conditions
Environmental condition	Soil & Conditions
Observation	Problems & Observations
Problem	Problems & Observations
Activity	Activity & History
Harvest	Activity & History
Plan	Planning
User decision	Planning
Recommendation	Recommendation Engine
Recommendation rule	Recommendation Engine
Inbox item	Inbox
User preference	User Preferences
Experiment	Experiment
Import/export operation	Import & Export
Search index	Search
Map presentation state	Map / Spatial Presentation
Source/provenance metadata	Shared provenance/data layer



---

36. What modules must not do

Modules should not:

Duplicate another module's authoritative data

For example, Planning must not maintain its own copy of plant requirements.

Modify another module's records directly

Use the owning module's domain interface.

Put business logic into UI screens

Screens orchestrate user interaction; domain services implement behaviour.

Treat derived data as authoritative

Recommendations, estimates and inferred conditions must remain distinguishable from source records.

Depend on optional services for core operation

Offline core functionality must remain available.

Hide uncertainty

If a result depends on incomplete information, that uncertainty should remain available to the relevant user-facing workflow.


---

37. Module API principle

Each module should expose operations in terms of user/domain concepts rather than persistence mechanics.

Prefer:

addPlantToGrowingSpace()
recordHarvest()
recordObservation()
evaluateSuitability()
createPlan()
completePlan()
getGardenHistory()

Avoid exposing internal persistence operations such as:

insertPlantInstanceRow()
updateRecommendationTable()
createObservationEntityRelationship()

The latter are implementation details.


---

38. Events and cross-module communication

Where asynchronous or event-based communication is useful, events should describe meaningful domain occurrences.

Examples:

PlantAdded
ActivityRecorded
ObservationRecorded
HarvestRecorded
PlanCreated
PlanCompleted
RecommendationGenerated
GrowingSpaceChanged

Events should not expose database implementation details.

Consumers should be able to react without becoming tightly coupled to the producer's internal schema.


---

39. Derived data and caching

Modules may cache derived information for performance.

Cached information must:

be identifiable as derived;

be safely rebuildable;

not replace authoritative records;

respect historical context;

be invalidated when source data changes.


For example, a cached suitability result may be discarded and recalculated.

The underlying garden records must remain intact.


---

40. Validation boundaries

Validation should occur at the appropriate level.

UI validation

Checks obvious interaction errors.

Example:

> A name is required.



Domain validation

Protects business rules.

Example:

> A planting activity must refer to a valid plant or garden context.



Import validation

Checks external data before it reaches the domain.

Database constraints

Protect fundamental data integrity.

No single layer should be expected to perform all validation.


---

41. Error isolation

An error in one module should not unnecessarily make unrelated functionality unusable.

For example:

Recommendation failure
    ≠
Garden unavailable

and:

Weather failure
    ≠
Activity recording unavailable

This is particularly important for offline use.


---

42. Testing implications

Each module should be testable independently where practical.

Tests should cover:

module behaviour;

module contracts;

validation;

interactions with dependent modules;

historical preservation;

failure isolation.


Cross-module integration tests should verify important workflows such as:

Garden → Plant → Plan → Activity → History

and:

Garden conditions → Recommendation → User decision → Plan


---

43. V1 implementation priority

The implementation should prioritise the modules required for the core product experience.

Core V1

Foundation
Garden
Plant Knowledge
Garden Records
Soil & Conditions
Activity & History
Problems & Observations
Recommendation Engine
Planning
Inbox
Import / Export

Supporting presentation capabilities include:

Map / Spatial Presentation
Search
User Preferences

These should be implemented sufficiently to support the core workflows.


---

44. Advanced V1 capability

The following should remain modular and should not distort the core architecture:

Experiments
Advanced personal-history analysis
Detailed provenance presentation
Advanced rule inspection

They may use the same domain records as the core system.


---

45. Post-V1 capability boundaries

Potential future modules include:

Weather
Community Knowledge
Advanced Analytics
External Knowledge Import
AI Assistance
Notification Services

These should integrate through explicit interfaces rather than becoming embedded into existing core modules.


---

46. Module evolution

A module may grow internally without changing its public contract.

For example, the Recommendation Engine may later support:

more suitability dimensions;

more sophisticated rules;

personal-history weighting;

additional geographic adaptation;

richer explanations.


Other modules should not need to know how the recommendation was calculated.

Similarly, the Garden module may later support richer spatial objects without requiring Plant Knowledge to understand the map implementation.


---

47. Avoiding premature microservices

The logical module boundaries do not imply that each module must become a separate application service.

For V1, modules may be implemented within a single application and local database.

The important requirement is logical separation.

Physical separation should only be introduced when it provides a clear technical benefit.


---

48. Recommended internal structure

A practical implementation may organise the application approximately as:

lib/
├── core/
│   ├── models/
│   ├── services/
│   ├── vocabulary/
│   ├── localisation/
│   ├── units/
│   ├── validation/
│   └── persistence/
│
├── features/
│   ├── garden/
│   ├── plants/
│   ├── garden_records/
│   ├── soil/
│   ├── activities/
│   ├── observations/
│   ├── problems/
│   ├── planning/
│   ├── recommendations/
│   ├── inbox/
│   ├── import_export/
│   ├── search/
│   ├── preferences/
│   └── experiments/
│
└── presentation/
    ├── home/
    ├── garden/
    ├── plants/
    ├── planner/
    ├── inbox/
    └── shared/

This is an implementation guideline rather than a mandatory directory structure.


---

49. Module design test

Before adding functionality to a module, ask:

1. What concept does this module own?


2. Does another module already own it?


3. Is the information authoritative or derived?


4. What does this module need from other modules?


5. Can that dependency be expressed through a stable interface?


6. Does the dependency create a circular relationship?


7. Can the module operate offline where required?


8. Can failure of this module be isolated?


9. Does the module preserve historical information?


10. Can the module evolve without unnecessarily changing unrelated modules?




---

50. Architectural invariants

The following rules should remain true throughout V1 and future development.

1. Garden data remains authoritative

The garden record is the source of truth for what the user has recorded about their garden.

2. Knowledge remains separate from personal records

General horticultural knowledge must not be confused with the user's garden history.

3. Recommendations remain derived

Recommendations can be recalculated and must not replace the underlying facts.

4. Plans remain distinct from activities

A plan is an intention.

An activity is evidence of an occurrence.

5. Observations remain distinct from diagnoses

What was observed must remain distinguishable from what the system thinks may explain it.

6. Measurements remain distinct from estimates

A measured value must not be silently converted into an inferred or estimated value.

7. History is preserved

Current changes must not silently destroy historical records.

8. User decisions remain user-controlled

The user may accept, reject, modify, override or defer recommendations.

9. Optional services remain optional

Weather, community, AI and similar future capabilities must not become prerequisites for core garden management.

10. UI remains separate from domain logic

The interface presents and orchestrates domain behaviour; it does not become the authoritative implementation of it.


---

51. Final architectural principle

The application should behave as one coherent gardening system while remaining internally modular.

The user should experience:

> One garden assistant.



The implementation should maintain:

> Multiple clear domain boundaries.



The most important architectural separation is between:

What is known
        ↓
What is true of this garden
        ↓
What has been observed
        ↓
What the system infers
        ↓
What the system recommends
        ↓
What the gardener decides
        ↓
What actually happens
        ↓
What the garden history shows

Each stage must remain distinguishable.

This separation allows the application to remain:

simple for casual gardeners;

powerful for advanced users;

explainable;

offline-first;

historically durable;

testable;

modular;

extensible;

resistant to accidental coupling.


The architecture should therefore optimise not for the largest possible number of modules, but for clear ownership, minimal dependencies, durable data, and simple user-facing behaviour.