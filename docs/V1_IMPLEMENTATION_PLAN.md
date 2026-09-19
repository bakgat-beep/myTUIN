Worked for 10s

Garden Planner & Manager — V1 Implementation Plan

Document: docs/V1_IMPLEMENTATION_PLAN.md
Version: 0.3
Status: Working implementation specification
Last updated: August 2026


---

1. Purpose

This document defines the implementation sequence for Version 1 of Garden Planner & Manager.

It translates the approved product, UX, architecture, data model, database schema, vocabulary, recommendation and import/export specifications into an ordered development plan.

The objective is to build V1 as a coherent, usable product rather than as a collection of disconnected features.


---

2. V1 implementation principles

Development should follow these principles:

1. Build the foundations before dependent features.


2. Keep the application local-first and offline-capable.


3. Preserve garden history from the beginning.


4. Use deterministic, explainable logic.


5. Keep the user experience simpler than the underlying data model.


6. Prefer reusable domain primitives over feature-specific duplicates.


7. Implement useful functionality incrementally.


8. Do not build future functionality merely because the architecture permits it.


9. Keep optional modules isolated from core garden functionality.


10. Test data integrity continuously, not only at the end.




---

3. V1 implementation target

V1 should provide a functioning local garden-management system supporting:

garden setup;

spatial garden representation;

growing spaces;

plant library;

user's plants;

cultivars;

basic growing information;

planting and planning;

deterministic suitability recommendations;

recommendation explanations;

quick activity recording;

observations;

soil information;

harvests;

losses;

garden history;

inbox items;

user decisions and overrides;

local persistence;

import/export;

basic backup/data safety;

multilingual-ready data architecture.


The application does not need to contain a comprehensive horticultural knowledge base at V1 launch.

The architecture should support expansion without requiring a redesign.


---

4. Explicit V1 non-goals

Do not allow the following to expand V1 unnecessarily:

social networking;

community sharing;

mandatory cloud accounts;

AI chat;

automatic garden design;

professional GIS;

sophisticated weather services;

complex statistical analysis;

automated disease diagnosis;

comprehensive chemical-treatment workflows;

elaborate gamification;

achievement systems;

large-scale community knowledge aggregation;

unnecessary notification infrastructure.


These may be considered after the core product has demonstrated value.


---

5. Implementation phases

The recommended sequence is:

Phase 0  Project foundation
    ↓
Phase 1  Data and persistence foundation
    ↓
Phase 2  Garden and spatial foundation
    ↓
Phase 3  Plant knowledge and My Plants
    ↓
Phase 4  Activities, observations and history
    ↓
Phase 5  Planner and recommendation engine
    ↓
Phase 6  Home and Inbox
    ↓
Phase 7  Import/export and data safety
    ↓
Phase 8  Integration, testing and V1 hardening

Some work within a phase may proceed in parallel once its dependencies are stable.


---

6. Phase 0 — Project foundation

Objective

Establish the application structure and development conventions before building significant functionality.

Tasks

6.1 Project structure

Establish the agreed module structure for:

domain models;

repositories;

persistence;

services;

recommendation logic;

vocabulary/reference data;

UI;

shared components;

import/export;

tests.


Avoid allowing UI screens to become the location of business logic.


---

6.2 Development conventions

Establish:

naming conventions;

ID conventions;

date/time handling;

unit handling;

error handling;

logging;

validation;

migration strategy;

test conventions.



---

6.3 Vocabulary integration

Implement the canonical vocabulary mechanism.

Vocabulary identifiers must remain independent of UI labels.

Do not hard-code translated or display strings into business logic.


---

6.4 Architecture boundaries

Establish clear interfaces between:

UI
 ↓
Application/domain services
 ↓
Repositories
 ↓
Local persistence

Recommendation logic should similarly remain independent of presentation.


---

6.5 Completion criteria

Phase 0 is complete when:

the project builds;

the agreed architecture is established;

core vocabulary handling exists;

domain/UI boundaries are clear;

automated tests can run;

local persistence can be introduced without restructuring the application.



---

7. Phase 1 — Data and persistence foundation

Objective

Implement the minimum reliable data foundation on which all later features depend.


---

7.1 Database schema

Implement the approved V1 database schema.

Core concepts should include the required entities for:

Garden;

Area;

GrowingSpace;

SpatialObject;

Plant;

Cultivar;

PlantInstance;

Activity;

Observation;

Harvest;

Loss;

Soil information;

Conditions;

Plans;

Recommendations;

User decisions;

Inbox items;

experiments where included in V1;

provenance/source information where required.


The exact schema is governed by:

docs/V1_DATABASE_SCHEMA.md

Do not introduce duplicate entities merely to support individual screens.


---

7.2 IDs

Every persistent entity requiring stable identity should use a stable identifier.

IDs must not depend on:

display names;

translated labels;

list position;

database row ordering.



---

7.3 Historical records

Implement the persistence model so that updating current garden information does not erase historical records.

Historical events should remain independently queryable.


---

7.4 Repository layer

Create repositories for the main domain areas.

Repositories should hide persistence implementation details from the rest of the application.


---

7.5 Migrations

Implement schema versioning and migration infrastructure before significant user data exists.

Future versions must be able to migrate V1 data safely.


---

7.6 Completion criteria

Phase 1 is complete when:

the database can be created from scratch;

core entities can be stored and retrieved;

relationships work;

IDs are stable;

historical records survive updates;

migrations can be tested;

repository interfaces are usable by application services.



---

8. Phase 2 — Garden and spatial foundation

Objective

Allow the user to create and understand the physical garden.


---

8.1 Garden setup

Implement the minimum setup flow:

1. Garden name.


2. Location.


3. Hemisphere/seasonal context.


4. First growing space.



Do not create a large mandatory setup wizard.


---

8.2 Growing spaces

Implement:

create;

edit;

view;

archive where appropriate;

dimensions;

type;

location;

relevant characteristics.


The user must be able to create a useful growing space without completing every optional field.


---

8.3 Spatial representation

Implement the V1 spatial model sufficiently to support:

garden outline;

areas where required;

growing spaces;

spatial objects;

plants associated with locations;

geometry editing.



---

8.4 Geometry editing

Changing the geometry of a current object must not destroy historical records associated with it.

Spatial calculations should update automatically where applicable.


---

8.5 Map interaction

Implement:

pan;

zoom;

select;

create;

move;

resize/edit;

inspect;

layer visibility;

basic filters.


Do not attempt professional GIS functionality.


---

8.6 Historical map access

Provide a simple way to inspect historical planting/spatial information.

The user should be able to answer:

> What was planted here?



without manually reconstructing history from individual activity records.


---

8.7 Completion criteria

Phase 2 is complete when a user can:

create a garden;

create a growing space;

represent its dimensions/location;

view it spatially;

edit it;

retain associated history;

inspect relevant objects.



---

9. Phase 3 — Plant knowledge and My Plants

Objective

Separate general horticultural knowledge from the user's actual plants.


---

9.1 Plant Library

Implement:

plant search;

browsing;

plant details;

basic growing requirements;

planting timing;

lifecycle;

relationships;

problems;

relevant source/provenance information.


The initial knowledge dataset can remain deliberately small.


---

9.2 My Plants

Implement plant instances representing what the user actually grows or plans to grow.

A plant instance should be able to connect to:

plant;

cultivar;

growing space/location;

planting event;

activities;

observations;

harvests;

outcomes;

history.



---

9.3 Cultivars

Implement cultivar selection without making it mandatory.

The UI must distinguish:

Plant:
Tomato

Cultivar:
Black Krim

Plant-level information and cultivar-specific information must remain distinguishable.


---

9.4 Add plant workflow

Implement the short workflow:

Add → Plant
    ↓
Select plant
    ↓
Optional cultivar
    ↓
Select location
    ↓
Planting/planned date
    ↓
Save

Additional information can be added later.


---

9.5 Completion criteria

Phase 3 is complete when a user can:

find a plant;

inspect its general information;

add it to the garden;

select a cultivar;

assign a location;

view the user's plant separately from the Plant Library;

access relevant growing information.



---

10. Phase 4 — Activities, observations and history

Objective

Make the application useful as an everyday garden record.


---

10.1 Activity primitive

Implement the Activity model as the reusable event mechanism.

Activities should support appropriate types such as:

planting;

watering;

feeding;

pruning;

intervention;

other defined activity types.


Avoid creating separate unrelated systems for every activity.


---

10.2 Quick Add

Implement Quick Add as a high-priority workflow.

Initial actions:

Plant;

Water;

Harvest;

Observe;

Feed;

Prune;

Intervention.


Additional actions may be placed under More.


---

10.3 Context-aware Quick Add

When Quick Add is opened from a known context, prefill the likely:

garden;

area;

growing space;

plant.


The user must be able to change the context.


---

10.4 Planned versus completed

Implement a clear distinction between:

Plan

and:

Actual activity

Completing a plan must create or update actual evidence only when the user explicitly records that something occurred.


---

10.5 Partial completion

Support activities/plans covering multiple targets.

Example:

Planned:
Water Beds 1–4

Completed:
Beds 1–3

Remaining:
Bed 4


---

10.6 Observations

Implement structured observation recording.

Observation types should expose only relevant fields.

Examples:

Observe → Soil
Observe → Plant
Observe → Growing Space


---

10.7 Observation versus diagnosis

Do not automatically convert observations into diagnoses.

Store:

Observed:
Leaves yellowing

separately from:

Possible explanation:
Water stress


---

10.8 Soil observations

Support useful observations when measurements are unavailable.

Examples:

pooling;

drainage behaviour;

moisture;

texture;

compaction;

visible organic matter.


Estimates must remain distinguishable from measurements.


---

10.9 Measurements

Where measurements are recorded, preserve:

value;

unit;

date;

method/source;

location/context;

confidence where appropriate.



---

10.10 Harvests

Implement quick harvest recording supporting:

plant;

location;

date;

quantity;

unit;

optional size category.


Weight must not be mandatory.


---

10.11 Losses

Implement losses separately from harvested yield.

The application must not invent an exact lost quantity where the user has not provided one.


---

10.12 History

Provide accessible history for:

plantings;

activities;

observations;

harvests;

losses;

soil measurements;

outcomes;

relevant decisions.



---

10.13 Completion criteria

Phase 4 is complete when a user can record ordinary garden events quickly and later reconstruct what happened.


---

11. Phase 5 — Planner and recommendation engine

Objective

Turn stored garden information into useful, explainable planning assistance.

This is a major V1 capability and should be implemented only after the underlying garden, plant and history data are reliable.


---

11.1 Recommendation engine architecture

Implement the deterministic recommendation engine as a separate domain service.

It should not depend on UI screens.

The engine should consume structured inputs and produce structured results.


---

11.2 Recommendation workflow

Support at least:

Select growing space
        ↓
What could I grow here?
        ↓
Evaluate candidates
        ↓
Present suitability
        ↓
Explain factors
        ↓
Create plan or choose alternative

Also support planning around:

upcoming planting windows;

known user intentions;

available space;

spontaneous planning.



---

11.3 Recommendation factors

Initially implement the factors that materially affect V1 decisions, such as:

light;

water/moisture;

soil;

pH where relevant;

fertility/nutrients where relevant;

temperature;

frost;

timing;

space;

rotation;

relationships;

infrastructure;

relevant personal history.


Do not require every factor for every recommendation.


---

11.4 Unknown information

Missing information must remain explicit.

The engine must distinguish:

known
unknown
not_applicable
estimated
measured
observed
inferred

Unknown information may reduce confidence without automatically making a plant unsuitable.


---

11.5 Suitability versus confidence

Return both where relevant.

Example:

Suitability:
Good

Confidence:
Moderate

The engine must not infer high confidence merely because the suitability result is favourable.


---

11.6 Conditional recommendations

Support results such as:

conditional
possible
insufficient_information

Examples:

> Good candidate if drainage is improved.



> Potentially suitable, but frost risk should decrease first.




---

11.7 Factor results

Return structured factor outcomes so the UI can present:

Excellent;

Good;

Marginal;

Poor;

Unknown.



---

11.8 Explanation generation

Each important recommendation should expose structured explanation data including:

factors used;

positive factors;

negative factors;

missing information;

confidence;

relevant evidence;

conditions;

potential changes that could alter the result.


The UI converts this into human-readable explanations.


---

11.9 Personal history

Personal history may influence recommendations.

When it does, the result must identify that personal history affected the recommendation.

It must remain possible to inspect the underlying historical evidence.


---

11.10 User preferences

Preferences may influence recommendations.

They should not silently erase information.

Example:

Lettuce
Suitability: Excellent
Preference: Plant to avoid

The result may therefore be:

Excluded by preference

rather than simply omitting lettuce.


---

11.11 User decisions

Implement explicit decision recording:

accepted;

rejected;

modified;

overridden;

deferred;

cancelled.


A user override is valid and must not be treated as an application error.


---

11.12 "Do nothing"

The engine must be capable of returning a useful result where no planting is recommended.

Examples:

no suitable candidate;

timing unsuitable;

conditions need improvement;

insufficient information.



---

11.13 Adapted knowledge

Where timing or guidance has been adapted from another geographic or hemispheric context, preserve that relationship and expose it when relevant.


---

11.14 Completion criteria

Phase 5 is complete when the user can:

ask what to grow;

receive deterministic recommendations;

see relevant suitability factors;

distinguish suitability from confidence;

understand why the recommendation was made;

see missing information;

create or modify a plan;

override the recommendation;

have the decision retained.



---

12. Phase 6 — Home and Inbox

Objective

Turn the underlying system into a useful daily gardening assistant.


---

12.1 Home

Home should answer:

> What matters now?



It should draw from existing data rather than becoming an independent information system.

Potential sections include:

current priorities;

upcoming activities;

important Inbox items;

garden status;

recent activity;

planning opportunities;

experiments.


Content should be dynamic.


---

12.2 New-garden Home

An empty garden should provide useful next actions rather than an empty dashboard.

Example:

Let's set up your garden

Add garden location
Add a growing space
Record conditions
Add your first plant

Optional steps must be skippable.


---

12.3 Inbox

Implement a prioritised work queue for useful items.

Initial categories:

action;

warning;

observation request;

information request;

experiment;

knowledge.



---

12.4 Priority

At minimum distinguish:

important
useful
informational

Do not generate excessive notifications.


---

12.5 Inbox explanations

Important items must explain why they exist.

Example:

Rotation warning

Solanaceae were grown here last season.

Why this matters:
Repeated related cropping can increase some disease and soil-related risks.


---

12.6 Mute and dismiss

Keep these separate.

Mute means:

> Do not surface this for now.



Dismiss means:

> I have dealt with this item or otherwise resolved it.



Muted items remain reviewable.


---

12.7 Completion criteria

Phase 6 is complete when Home provides useful prioritisation and Inbox surfaces meaningful actions without overwhelming the user.


---

13. Phase 7 — Import/export and data safety

Objective

Protect user data and provide controlled movement of garden data.


---

13.1 Export

Implement a complete export of the user's garden data in the defined canonical format.

Export should preserve:

stable IDs;

relationships;

dates;

history;

vocabulary identifiers;

provenance where appropriate;

measurements;

spatial information.



---

13.2 Import

Implement the defined workflow:

Select file
    ↓
Validate
    ↓
Show summary
    ↓
Show errors/conflicts
    ↓
Allow cancellation
    ↓
Confirm
    ↓
Import
    ↓
Report result

Invalid data must not be silently imported.


---

13.3 Validation

Use canonical vocabulary identifiers.

Validate:

required fields;

identifiers;

relationships;

vocabulary values;

data types;

units;

dates;

spatial data;

conflicts.



---

13.4 Conflict handling

Conflicts must be visible and understandable.

Do not silently overwrite meaningful garden history.


---

13.5 Backup/data safety

Provide practical access to:

export;

backup;

restore where included in the V1 platform design;

destructive-data warnings.



---

13.6 Completion criteria

Phase 7 is complete when a user can export their garden, validate/import compatible data and understand any problems before data is changed.


---

14. Phase 8 — Integration and V1 hardening

Objective

Turn the separate modules into a reliable product.


---

14.1 End-to-end workflows

Test complete workflows rather than only individual screens.

At minimum:

New garden

Create garden
→ Add growing space
→ Record conditions
→ Add plant

Planting

Select plant
→ Select location
→ Record planting
→ View My Plants
→ View history

Planning

Select space
→ Ask what could grow
→ Review recommendation
→ Inspect Why?
→ Accept/override
→ Save plan

Daily activity

Open Quick Add
→ Water
→ Save
→ View history

Observation

Observe
→ Select type
→ Record structured information
→ Save
→ Review later

Harvest

Harvest
→ Select plant
→ Enter quantity
→ Save
→ View history

Soil

Record observation
→ Generate estimate
→ Distinguish estimate from measurement
→ Use in recommendation

Import/export

Export
→ Validate exported data
→ Import
→ Verify relationships/history


---

15. Testing strategy

Testing should occur continuously throughout development.


---

15.1 Unit tests

Test:

vocabulary handling;

validation;

calculations;

date/season logic;

unit conversions;

recommendation factors;

recommendation aggregation;

confidence handling;

history queries;

import validation;

migration logic.



---

15.2 Repository tests

Test:

create;

read;

update;

archive;

relationships;

historical preservation;

migrations.



---

15.3 Recommendation tests

Create deterministic test cases covering:

strong positive suitability;

strong negative suitability;

unknown information;

conflicting factors;

conditional suitability;

rotation concerns;

personal history;

user preferences;

insufficient information;

no suitable candidate;

recommendation overrides.


Recommendation tests should verify both the result and the explanation data.


---

15.4 UI tests

Prioritise core workflows rather than testing every visual detail.

Test:

navigation;

Quick Add;

plant selection;

growing-space selection;

recommendation flow;

recording activities;

observations;

harvests;

history;

import/export.



---

15.5 Migration tests

Every schema migration should be tested against representative older data.

No migration should silently discard historical information.


---

16. Seed data strategy

V1 should use a deliberately limited but high-quality initial knowledge set.

The application should first prove that the data model and workflows work correctly.

Do not delay implementation while attempting to create a comprehensive plant database.


---

16.1 Initial plant dataset

Include enough plants to exercise:

different lifecycles;

different growing requirements;

different planting windows;

different space requirements;

different rotation groups;

different cultivars;

different relationships;

different recommendation outcomes.



---

16.2 Expansion

Additional plant knowledge can be added independently after the application foundation is stable.


---

17. Development order within features

When implementing a feature, generally follow:

Vocabulary/reference data
        ↓
Domain model
        ↓
Persistence
        ↓
Repository
        ↓
Domain/application service
        ↓
Business rules
        ↓
UI
        ↓
Integration tests

Do not begin with a polished screen and attempt to retrofit the data model afterwards.


---

18. UI implementation order

The initial UI should prioritise the core navigation:

Home
Garden
Plants
Planner
Inbox

+ Add

Then implement the most important workflows within those areas.


---

18.1 First usable milestone

The first meaningful milestone should allow:

Create garden
→ Add growing space
→ Add plant
→ View plant
→ Record activity
→ View history

This establishes the application as a useful garden notebook before recommendation functionality is added.


---

18.2 Second usable milestone

Add:

Conditions
→ Planner
→ Recommendation
→ Explanation
→ Plan
→ Override


---

18.3 Third usable milestone

Add:

Home
→ Inbox
→ Soil reasoning
→ richer history
→ import/export


---

19. Data integrity rules

These rules apply throughout development.

Never silently:

delete historical events;

convert plans into actual activities;

turn observations into diagnoses;

turn estimates into measurements;

change historical measurements;

overwrite meaningful records;

reinterpret old vocabulary identifiers;

discard unknown values;

invent missing historical information.



---

20. Offline-first implementation

Core application functionality must operate without network access.

Local operations must not depend on:

cloud APIs;

live weather;

remote recommendation services;

AI services;

community services.


Future network-dependent functionality must be isolated.


---

21. Performance priorities

Optimise first for:

1. application startup;


2. Home;


3. Garden;


4. plant lookup;


5. Quick Add;


6. saving records;


7. history;


8. ordinary recommendation queries.



Complex analysis can be deferred or performed asynchronously where necessary.


---

22. Error handling

Errors should be presented in user terms.

Primary messages should describe:

what failed;

whether data was saved;

what the user can do next.


Technical diagnostics may be available separately.

Example:

We couldn't save that observation.

Try again.

rather than exposing database exceptions.


---

23. Feature isolation

Modules should fail independently where practical.

For example, failure of a future weather integration must not prevent:

viewing the garden;

recording activities;

viewing plants;

accessing history;

using stored-information recommendations.


The same principle applies to future community functionality.


---

24. Documentation discipline

Implementation must remain aligned with the specification set.

When implementation decisions materially change:

data structures;

vocabulary;

workflows;

recommendation behaviour;

import/export;

architecture;


the relevant specification must be updated.

Do not allow code and documentation to diverge silently.


---

25. Definition of done

A V1 feature is not complete merely because the screen works.

A feature should generally be considered complete when:

its data model is defined;

persistence works;

validation exists;

relevant vocabulary is defined;

business logic is separated from UI;

history is preserved;

errors are handled;

offline operation works where required;

the core workflow is usable;

relevant tests pass;

the feature does not unnecessarily complicate unrelated modules;

documentation reflects the implemented behaviour.



---

26. V1 release criteria

V1 should not be considered ready until the application can reliably support the core workflows:

Garden

create garden;

create and edit growing spaces;

represent spatial relationships;

record conditions.


Plants

search Plant Library;

add plants to My Plants;

record cultivars;

view relevant information.


Planning

select a space;

request candidate plants;

evaluate suitability;

explain recommendations;

account for unknown information;

create plans;

override recommendations.


Recording

record planting;

watering;

feeding;

pruning;

observations;

interventions;

harvests;

losses.


History

inspect previous planting;

inspect activities;

inspect observations;

inspect harvests;

inspect outcomes;

understand relevant historical effects on recommendations.


Soil

record known measurements;

record qualitative observations;

distinguish observations, estimates and measurements;

use soil information in recommendations.


Inbox

surface useful actions;

explain why they matter;

complete;

mute;

dismiss.


Data

operate offline;

preserve history;

export;

validate/import;

survive supported schema migrations.



---

27. Post-V1 candidates

Only after V1 is stable should development consider:

richer community knowledge;

optional knowledge contribution;

advanced experiments;

weather integration;

more sophisticated reminders;

broader plant/cultivar knowledge;

advanced analytics;

additional map layers;

more sophisticated diagnostic assistance;

cloud synchronisation;

collaboration;

advanced reporting.


These should be evaluated against actual V1 usage rather than assumed requirements.


---

28. Implementation priorities

When resources are limited, prioritise in this order:

1. Data integrity


2. Garden and growing-space management


3. Plant management


4. Everyday recording


5. History


6. Basic planning


7. Explainable recommendations


8. Soil/condition reasoning


9. Home and Inbox


10. Import/export


11. Advanced functionality


12. Visual polish



A feature that looks impressive but compromises data integrity or core usability should not be prioritised.


---

29. Final implementation principle

Garden Planner & Manager should be built from the inside out:

Reliable data
    ↓
Reliable domain services
    ↓
Reliable garden records
    ↓
Useful planning
    ↓
Explainable recommendations
    ↓
Helpful daily experience

The application should first become a reliable record of the gardener's real garden.

It should then become a useful planner.

It should then become increasingly helpful as enough information accumulates to support better recommendations and personal insight.

The implementation should never require the gardener to understand the underlying architecture.

The final V1 experience should remain consistent with the central product principle:

> A garden notebook that understands your garden.