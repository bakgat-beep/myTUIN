Garden Planner & Manager — V1 Testing Strategy

Document: docs/TTESTINGSTRATEGY.md
Version: 0.2
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the testing strategy for Version 1 of the Garden Planner & Manager.

The purpose of testing is to establish that V1 is:

functionally correct;

reliable offline;

safe for garden data;

historically durable;

explainable;

deterministic where required;

usable through its core workflows;

modular enough that failures remain contained;

maintainable as the application evolves.


Testing should protect the core product rather than attempt to test every possible combination of data exhaustively.


---

2. Testing principles

2.1 Protect the core workflows

Testing priority follows the importance of the product's core workflows.

The most important workflows are:

1. open the application and understand what matters;


2. view and interact with the garden;


3. view plants;


4. plan what to grow;


5. determine suitability;


6. record what actually happened;


7. review garden history;


8. understand important recommendations.



A feature that works in isolation but breaks one of these workflows is not considered acceptable.


---

2.2 Test behaviour, not implementation

Tests should primarily verify observable behaviour and stable contracts.

Avoid tests that unnecessarily depend on:

widget hierarchy;

private implementation details;

database internals;

exact class structure;

temporary UI wording;

rendering implementation.


Implementation-specific tests are appropriate where the implementation itself represents an important contract, such as database migrations or architectural boundaries.


---

2.3 Prefer deterministic tests

Core V1 behaviour should be deterministic wherever practical.

Given the same:

stored data;

vocabulary values;

rules;

dates;

geographic context;

user preferences;


the application should produce the same result.

Tests must therefore avoid depending on:

random ordering;

network availability;

current weather;

uncontrolled external services;

machine-specific state.


Where randomness is part of the user experience, tests should use controlled seeds or verify the required invariants instead.


---

2.4 Test unknown information explicitly

Unknown information is a valid state.

Tests must verify that:

unknown is not silently converted to a negative;

missing information does not automatically make something unsuitable;

missing information can reduce confidence where appropriate;

irrelevant missing information does not unnecessarily affect recommendations;

unknown remains distinguishable from measured, observed, estimated and inferred information.



---

2.5 Test historical preservation

Garden history is a core product asset.

Tests must verify that ordinary updates do not accidentally erase or rewrite:

past plantings;

activities;

observations;

harvests;

losses;

soil measurements;

experiments;

decisions;

recommendation-related evidence.



---

2.6 Test offline-first behaviour

Core V1 functionality must work without Internet access.

Tests should not merely verify that the application can start offline.

They must verify that ordinary workflows remain functional offline, including:

viewing the garden;

viewing plants;

recording activities;

recording observations;

recording harvests;

planning;

recommendations based on stored information;

viewing history.



---

2.7 Test explainability

Important recommendations and derived results must be explainable.

Tests should verify not only the final result but also that the application can identify:

factors considered;

factors supporting the result;

factors opposing it;

missing information;

confidence;

relevant history;

relevant preferences;

important constraints.



---

3. Testing layers

V1 should use several complementary testing layers.

Unit tests
    ↓
Data/repository tests
    ↓
Widget/UI tests
    ↓
Integration tests
    ↓
Golden/core workflow tests
    ↓
Migration and data integrity tests
    ↓
Manual usability and release testing

No single layer is sufficient.


---

4. Unit testing

Unit tests verify isolated domain behaviour.

Priority areas include:

vocabulary handling;

value/state interpretation;

date and season calculations;

hemisphere handling;

unit conversion;

recommendation factors;

suitability calculations;

confidence calculations;

rule evaluation;

recommendation explanations;

planning status transitions;

activity validation;

observation processing;

harvest calculations;

history-derived analysis;

import validation;

data transformation.


Unit tests should be fast and numerous enough to support frequent development.


---

5. Vocabulary testing

Controlled vocabularies are part of the application's data contract.

Tests should verify:

every active vocabulary ID is valid;

deprecated IDs remain interpretable;

invalid IDs are rejected where required;

localisation is separate from canonical IDs;

rules operate on canonical IDs rather than display labels;

vocabulary values are not accidentally duplicated across incompatible contexts;

unknown, not_recorded, not_applicable and not_measured remain distinguishable where required.


Vocabulary tests should also verify that adding a valid new reference value does not require unnecessary application-code changes.


---

6. Date, season and hemisphere testing

Seasonal logic is particularly important because V1 must support Southern Hemisphere gardens.

Tests must cover:

Northern Hemisphere;

Southern Hemisphere;

equatorial/global contexts where applicable;

unknown hemisphere;

season boundaries;

planting windows crossing calendar years;

year-round conditions;

leap years where dates are relevant;

location-specific seasonal interpretation;

adapted timing.


The application must never assume:

March = spring

or equivalent fixed calendar assumptions.


---

7. Data and repository testing

Repository and persistence tests verify that domain data can be safely stored and retrieved.

Tests should cover:

create;

read;

update;

archive/deactivate where applicable;

retrieval by relationship;

filtering;

sorting;

date ranges;

spatial relationships;

history retrieval;

transaction behaviour;

validation;

persistence after application restart.


Tests should verify that relationships remain intact when related records are edited.


---

8. Database testing

The SQLite database and selected persistence layer are critical persistence boundaries.

Tests should verify:

schema creation;

constraints;

indexes where behaviour depends on them;

foreign-key relationships;

nullable/unknown states;

unique identifiers;

date storage;

numeric precision;

canonical vocabulary storage;

transaction integrity;

historical records;

archival behaviour.


Database tests should use isolated test databases rather than development or production garden data.


---

9. Migration testing

Every database schema migration must be tested.

At minimum, migration tests should verify:

1. creation of a V1 database from scratch;


2. migration from each supported previous schema version;


3. preservation of existing records;


4. preservation of relationships;


5. preservation of vocabulary identifiers;


6. preservation of historical dates;


7. preservation of measurements and observations;


8. correct handling of newly introduced fields;


9. appropriate handling of deprecated values;


10. application startup after migration.



A migration must never silently fabricate historical information.

Where a new field did not exist previously, the migrated value should normally remain unknown or otherwise appropriately unspecified.


---

10. Migration test fixtures

Representative migration fixtures should include:

an empty garden;

a minimal garden;

multiple growing spaces;

current plants;

historical plantings;

activities;

observations;

harvests;

soil measurements;

recommendations;

user preferences;

imported data where applicable.


Fixtures should include both ordinary and edge-case records.


---

11. Spatial and map testing

The map is a functional planning tool and requires behavioural testing.

Tests should verify:

creating a spatial object;

editing geometry;

moving an object;

resizing a growing space;

selecting overlapping objects;

displaying relevant information;

layer visibility;

filters;

historical views;

association between spatial objects and garden records.


Changing the geometry of a growing space must not erase its historical records.


---

12. Spatial calculation testing

Where spatial calculations affect recommendations or garden information, test:

dimensions;

area;

position;

containment;

overlap;

relationships between spaces and plants;

changes to geometry;

invalid or degenerate geometry;

unit conversion.


Calculated values should be tested against known expected results.


---

13. Plant data testing

Plant-related tests should distinguish clearly between:

Plant Library knowledge;

cultivar information;

My Plants;

individual plant instances;

historical plantings.


Tests should verify that editing a user's plant record does not modify general Plant Library knowledge.

Similarly, changes to general plant knowledge must not accidentally alter historical user records.


---

14. Activity testing

Activity records form a major part of garden history.

Tests should cover:

planting;

watering;

feeding;

pruning;

interventions;

observations where represented through the activity model;

harvesting;

other supported activity types.


Tests should verify:

correct date/time;

relevant location;

relevant plant;

planned versus actual state;

independent event records;

editing;

historical retrieval.



---

15. Planned versus actual testing

This distinction is critical.

Tests must verify that:

planned

does not become:

completed

without an explicit user action or valid event.

A planned activity must never by itself constitute evidence that the activity occurred.

Tests should cover:

scheduled activities;

completed activities;

skipped activities;

cancelled activities;

partial completion;

recurring plans;

overdue/expired plans where applicable.



---

16. Partial completion testing

For activities covering multiple targets, tests must verify partial completion.

Example:

Plan:

Water Beds 1–4

User records:

Beds 1–3 completed

Expected result:

Beds 1–3 completed
Bed 4 remaining

The system must not mark the entire plan completed automatically.


---

17. Quick Add testing

Quick Add is a high-priority workflow.

Tests should verify that common actions can be recorded with minimal interaction.

At minimum:

Plant;

Water;

Harvest;

Observe;

Feed;

Prune;

Intervention.


Tests should verify contextual defaults.

For example:

Garden → Bed 2 → Quick Add → Water

should preselect Bed 2 where appropriate.

The user must still be able to change the context.


---

18. Observation testing

Observation workflows should test:

structured observation types;

relevant fields;

optional notes;

confidence;

location;

plant association;

growing-space association;

dates;

unknown values.


Tests must verify that an observation remains an observation.

For example:

Leaves yellowing

must not automatically become:

Nutrient deficiency

without an explicit diagnostic/inference process.


---

19. Soil testing

Soil functionality should be tested across multiple information states.

Examples:

Unknown
Observed
Estimated
Measured

Tests should verify that:

observations can exist without measurements;

estimates retain their basis;

measurements retain method/source/date where required;

measured values are not overwritten by estimates;

proxy observations can inform reasoning;

proxy-derived estimates are labelled as estimates;

testing suggestions are optional;

missing soil information affects recommendations only when relevant.



---

20. Harvest testing

Harvest workflows should verify:

plant association;

location;

date;

quantity;

unit;

optional size category;

multiple supported quantity units;

historical retrieval.


Weight must not be required.

Harvest loss must remain distinguishable from harvested quantity.

The application must not invent lost yield where the user has not supplied or deliberately estimated it.


---

21. Recommendation engine testing

The recommendation engine is a high-priority testing area.

Tests must verify deterministic results from controlled inputs.

Each recommendation test should define:

input garden conditions;

plant requirements;

timing;

spatial context;

rotation history;

relationships;

preferences;

rule set;

expected factor results;

expected overall status;

expected confidence;

expected explanation.



---

22. Recommendation factor testing

Individual suitability dimensions should be tested independently before being tested in combination.

Examples:

light;

water;

moisture;

soil;

pH;

fertility;

temperature;

frost;

timing;

space;

rotation;

relationships;

infrastructure;

history.


Tests should verify:

strong match;

acceptable match;

marginal match;

poor match;

unknown;

unavailable data.



---

23. Hard and soft constraints

Where the recommendation architecture distinguishes hard constraints from soft factors, tests must verify that distinction.

For example:

an impossible physical requirement may make a candidate unsuitable;

a moderate rotation concern may reduce suitability without automatically making the plant impossible;

missing information should not automatically behave like a negative result.


Each rule should explicitly define its expected effect.


---

24. Unknown versus negative testing

This distinction requires explicit regression tests.

For example:

Known poor drainage

and:

Drainage unknown

must not necessarily produce the same recommendation.

Likewise:

Known unsuitable temperature

must remain distinguishable from:

Temperature unknown

The expected result should be defined by the relevant recommendation rule.


---

25. Confidence testing

Suitability and confidence must be tested independently.

Examples:

Good suitability
Moderate confidence

and:

Marginal suitability
High confidence

must both be representable.

Tests should verify that adding reliable information can change confidence without necessarily changing suitability.

Conversely, changing an important suitability factor can change suitability without implying a proportional change in confidence.


---

26. Recommendation explanation testing

For every important recommendation scenario, tests should verify that the explanation identifies the relevant causes.

A useful test should be able to answer:

What information was used?

What supported the recommendation?

What opposed it?

What information was missing?

Why does confidence have its current level?

What could change the result?


The explanation must correspond to the actual rule results.

The UI must not display an explanation that contradicts the recommendation engine's underlying reasoning.


---

27. Personal history testing

Personal history can influence recommendations.

Tests should verify that:

historical evidence is correctly retrieved;

historical evidence is distinguished from general knowledge;

relevant history can affect a recommendation;

irrelevant history does not;

the user can identify when personal history affected the result;

historical evidence does not silently become universal knowledge.


Example:

Poor historical tomato performance in a particular bed should affect recommendations for that context, not automatically make tomatoes unsuitable everywhere.


---

28. User preference testing

Preferences must influence recommendations transparently.

Tests should verify that:

preferences are applied where intended;

preferences do not silently erase relevant knowledge;

an excluded or avoided plant can remain visible when appropriate;

the reason is shown as a user preference;

changing a preference produces the expected recommendation change.



---

29. Recommendation regression scenarios

A curated set of deterministic scenarios should be maintained as permanent regression tests.

Scenarios should include:

clearly suitable crop;

clearly unsuitable crop;

conditional recommendation;

marginal candidate;

insufficient information;

unknown soil information;

rotation concern;

space constraint;

timing constraint;

temperature constraint;

positive personal history;

negative personal history;

user preference conflict;

adapted Southern Hemisphere timing;

no suitable candidate;

do-nothing recommendation.


These scenarios form a stable behavioural contract for the recommendation engine.


---

30. Planning testing

Planner tests should cover:

selecting a growing space;

finding suitable candidates;

viewing explanations;

creating a plan;

changing a plan;

overriding a recommendation;

postponing a decision;

cancelling a plan;

planned versus completed state;

timing changes;

conflicts with existing plants or plans.


A user override must be accepted as a legitimate decision.


---

31. Inbox testing

Inbox tests should verify:

prioritisation;

meaningful item creation;

explanation;

completion;

dismissal;

muting;

restoration/review of muted items;

category handling;

duplicate suppression where required.


The system should not generate excessive or meaningless items from ordinary data.


---

32. Mute versus dismiss testing

Tests must preserve the distinction:

mute

means:

> Do not currently surface this item.



Whereas:

dismiss

means:

> The user has dealt with or otherwise resolved this item.



Muting must not destroy the underlying information.

Dismissal must not automatically create a permanent category-wide mute.


---

33. Search and filtering testing

Search tests should verify:

plant names;

cultivars;

growing spaces;

problems;

activities;

observations;

configured aliases.


Filtering tests should verify:

correct results;

combinations of filters;

clearing filters;

active-filter visibility;

empty filtered results;

appropriate persistence where intended.


Canonical identifiers should remain separate from display labels.


---

34. Import/export testing

Import/export is an important data-integrity boundary.

Tests should verify:

valid export;

valid import;

round-trip preservation;

vocabulary validation;

missing required information;

unknown values;

invalid values;

duplicate records;

conflicting records;

deprecated identifiers;

warnings;

errors;

cancellation;

partial import results.


A valid dataset exported from the application and imported again should preserve its meaningful information.


---

35. Import round-trip testing

At minimum:

Garden
    ↓
Export
    ↓
Import
    ↓
New test database

should preserve:

garden identity;

growing spaces;

geometry;

plants;

cultivars;

activities;

observations;

harvests;

losses;

soil information;

planning records;

preferences;

historical information;

relevant vocabulary IDs.


Where exact binary equality is inappropriate, semantic equivalence must be tested instead.


---

36. Invalid import testing

Tests should include:

malformed files;

missing fields;

invalid vocabulary IDs;

invalid dates;

invalid numeric values;

incompatible versions;

duplicate identifiers;

broken relationships;

unsupported data.


The application must report understandable validation results.

It must not partially modify the garden before validation has established what can safely be imported.


---

37. UI/widget testing

Widget tests should verify important user-visible behaviour.

Priority areas include:

Home;

Garden;

Plant Library;

My Plants;

Planner;

Inbox;

Quick Add;

recommendation cards;

explanation panels;

growing-space forms;

observation forms;

harvest forms;

empty states;

error states.


Tests should focus on behaviour and accessibility rather than implementation structure.


---

38. Core workflow integration testing

Integration tests should exercise complete user workflows across multiple modules.

At minimum:

New garden

Create garden
→ Add growing space
→ Record conditions
→ Save
→ Reopen

Add plant

Find plant
→ Select plant
→ Select cultivar if known
→ Select location
→ Add to My Plants
→ View plant

Plan planting

Select growing space
→ Ask what could grow here
→ Review recommendations
→ Open explanation
→ Create plan
→ Override if desired

Record activity

Open context
→ Quick Add
→ Select activity
→ Confirm
→ Save
→ View history

Soil observation

Select space
→ Observe soil
→ Record drainage observations
→ Save
→ View resulting estimate/reasoning

Harvest

Quick Add
→ Harvest
→ Select plant
→ Enter quantity/unit
→ Save
→ View history


---

39. Historical workflow testing

Tests should verify the complete lifecycle of important information.

Example:

Create growing space
→ plant crop
→ record activity
→ harvest
→ change current geometry
→ view historical record

The historical record must remain understandable after later changes.


---

40. Context preservation testing

Navigation tests should verify that useful context is preserved.

Example:

Garden
→ Bed 2
→ Tomato
→ History
→ Back

should return to an appropriate Bed 2 context rather than unexpectedly returning to the application root.

This should be tested for major cross-module navigation paths.


---

41. Accessibility testing

Accessibility testing should cover:

text scaling;

readable labels;

touch target sizes;

contrast;

semantic labels;

screen-reader compatibility where supported;

non-colour-only status;

focus/navigation behaviour where applicable;

understandable error messages.


Important information must remain understandable without relying exclusively on colour.


---

42. Localisation testing

Although V1 may initially launch in one language, the architecture should be tested for localisation readiness.

Tests should verify that:

canonical IDs are not displayed accidentally;

UI strings are externalised appropriately;

text expansion does not break important layouts;

translated labels do not affect rules;

dates and units follow locale settings;

database data remains unchanged when the UI language changes.



---

43. Offline testing

Offline testing should be performed with network connectivity unavailable.

Verify:

application launch;

database access;

garden viewing;

plant viewing;

Quick Add;

observations;

harvests;

planning;

recommendations;

history;

search;

filtering.


If a future optional capability requires connectivity, its failure must remain isolated from core functionality.


---

44. Failure isolation testing

Feature boundaries should be tested deliberately.

Examples:

a future weather service unavailable;

optional external data unavailable;

an optional knowledge source unavailable;

an optional import source unavailable.


Core local functionality must continue to work.

The application should not allow an optional module failure to corrupt or block unrelated garden operations.


---

45. Performance testing

V1 should establish reasonable performance expectations for common local operations.

Measure at least:

application startup;

Home loading;

Garden loading;

map interaction;

plant lookup;

Quick Add opening;

activity save;

observation save;

recommendation generation;

history retrieval.


Performance tests should use representative garden sizes rather than only tiny test datasets.


---

46. Representative data sets

Testing should include at least three broad data profiles.

Minimal garden

one garden;

one growing space;

few plants;

little history.


Purpose:

onboarding;

empty-state transitions;

basic workflows.


Typical garden

Multiple:

growing spaces;

plants;

activities;

observations;

harvests;

recommendations.


Purpose:

ordinary V1 use.


Rich garden

Large history including:

multiple seasons;

many plantings;

soil observations and measurements;

cultivars;

harvests;

experiments;

recommendations;

personal history.


Purpose:

performance;

filtering;

history;

migration;

recommendation complexity.



---

47. Property and invariant testing

Where practical, tests should verify general invariants rather than only individual examples.

Examples:

a completed activity has an appropriate completion record;

changing current geometry does not delete historical activities;

canonical IDs remain language-independent;

export followed by import preserves semantic data;

unknown values remain unknown;

planned activities do not become actual activities automatically;

a user override does not corrupt the underlying recommendation;

deleting or archiving a current record does not silently destroy protected history.



---

48. Randomised testing

Randomised testing may be used for algorithms and data transformations where it provides value.

However, production behaviour must remain deterministic where required.

Randomised tests should:

use reproducible seeds;

record failing inputs;

avoid dependence on current time;

avoid network data.


Random generation must never replace curated domain scenarios.


---

49. Regression testing

Every significant bug should result in a regression test where practical.

The regression test should reproduce:

1. the original failure;


2. the expected behaviour;


3. the corrected behaviour.



This is especially important for:

data loss;

incorrect recommendations;

migration failures;

history corruption;

planning state errors;

import/export failures.



---

50. Architectural boundary testing

The application architecture is modular.

Tests should help enforce important boundaries between:

presentation;

application/domain logic;

persistence;

recommendation logic;

knowledge/reference data;

optional integrations.


The UI should not contain recommendation rules.

The recommendation engine should not depend on UI wording.

Persistence code should not determine user-facing recommendation explanations.

Modules should communicate through defined contracts rather than reaching into one another's internal implementation.


---

51. Data integrity testing

Tests should specifically target destructive or corrupting behaviour.

Examples:

deleting a plant instance;

changing a growing space;

editing a historical measurement;

importing conflicting data;

changing vocabulary definitions;

migrating old records;

archiving records.


Expected behaviour must favour preservation of meaningful historical information.


---

52. Undo and destructive action testing

Where Undo is supported, tests should verify:

action is correctly reversed;

related data remains consistent;

historical records are not unintentionally removed;

repeated undo/redo behaviour is safe where supported;

the undo period behaves correctly.


Where confirmation is required, tests should verify that cancellation leaves data unchanged.


---

53. Error-state testing

Important error paths must be tested.

Examples:

database save failure;

invalid input;

unavailable optional service;

malformed import;

failed migration;

invalid geometry;

recommendation calculation failure.


The user-facing result should be understandable.

Technical details may be available for troubleshooting but should not replace the primary explanation.


---

54. Recovery testing

Where an operation fails, verify that the application can recover without corrupting data.

Examples:

failed save;

interrupted import;

failed migration;

application restart during ordinary persistence;

invalid user input;

cancelled operation.


A failed operation must not leave the garden in a partially invalid state.


---

55. Security and privacy testing

Although V1 is local-first, tests should still verify protection of garden data.

At minimum:

private garden information is not automatically shared;

future contribution functionality is opt-in;

exact location is not exposed through unintended export/sharing paths;

import does not execute arbitrary content;

external data cannot silently overwrite private records;

backups/exports contain only intended data.



---

56. Test environment separation

Development, test and user data must remain separate.

Automated tests must never modify the user's real garden.

Test databases and fixtures should be isolated and disposable.


---

57. Automated test execution

During development, the normal validation sequence should be approximately:

format
→ static analysis
→ unit tests
→ data/repository tests
→ widget tests
→ integration tests

The exact command sequence is an implementation detail and may evolve with the project.

The important requirement is that automated validation is run frequently rather than only before release.


---

58. Continuous regression

After changes to a core module, run the tests most directly affected by that module plus the core workflow regression suite.

High-impact changes should trigger broader testing.

Examples:

Recommendation engine change

Run:

recommendation unit tests;

factor tests;

confidence tests;

explanation tests;

history tests;

planning workflows;

core recommendation regression scenarios.


Database schema change

Run:

database tests;

migration tests;

repository tests;

import/export tests;

core workflows.


Vocabulary change

Run:

vocabulary tests;

validation tests;

recommendation rules;

import/export;

localisation-related tests.



---

59. Golden scenarios

A small set of complete, human-readable garden scenarios should serve as behavioural reference cases.

Each scenario should specify:

garden conditions;

plants;

spaces;

history;

relevant observations;

preferences;

expected recommendations;

expected explanations;

expected confidence;

expected planning outcomes.


These scenarios should remain stable unless the product specification deliberately changes.

When a golden scenario changes, the reason should be documented.


---

60. Manual testing

Automated testing cannot replace manual testing of the complete experience.

Manual testing should concentrate on:

usability;

discoverability;

map interaction;

touch behaviour;

clarity of explanations;

confusing states;

empty states;

accessibility;

visual hierarchy;

navigation;

real-world gardening workflows.


Manual testing should use representative garden scenarios rather than isolated screens only.


---

61. New-garden usability test

A new user should be able to:

1. create a garden;


2. add a growing space;


3. record basic conditions;


4. add a plant;


5. find relevant information;


6. make a basic plan;


7. record an activity.



The user should not need to understand:

database entities;

vocabulary IDs;

recommendation algorithms;

internal spatial models.



---

62. Casual-user test

A casual user should be able to use the application primarily as a garden record without being forced into advanced workflows.

Verify that the user can:

add plants;

record harvests;

record simple activities;

view history;

obtain useful recommendations.


Advanced configuration should remain optional.


---

63. Advanced-user test

An advanced user should be able to access:

detailed conditions;

measurements;

cultivars;

history;

experiments;

recommendation explanations;

detailed evidence/provenance where available.


The additional capability should not make basic workflows unusably complex.


---

64. Recommendation transparency test

For a representative set of recommendations, ask a tester:

> Why did the application recommend this?



The tester should be able to answer using the application's explanation without needing access to the implementation.

Similarly, when a recommendation is poor or conditional:

> What could change this result?



should have a meaningful answer where the underlying rules support one.


---

65. History integrity test

A tester should be able to:

1. create a growing space;


2. plant something;


3. record activities;


4. record a harvest;


5. change the growing space;


6. return to history;


7. confirm that the historical information remains understandable.



This is a release-critical test.


---

66. Offline acceptance test

With the device disconnected from the Internet, a tester should be able to complete the major V1 local workflows.

Failure of an optional future network-dependent capability must not prevent those workflows.


---

67. Import/export acceptance test

A tester should be able to:

1. export a representative garden;


2. inspect the export result;


3. import it into an isolated test garden;


4. review validation;


5. confirm the imported data;


6. verify semantic equivalence with the original.



Invalid data should produce understandable validation results without corrupting the destination garden.


---

68. Release testing

Before a V1 release, the following should pass:

static analysis;

unit tests;

repository/data tests;

database tests;

migration tests;

widget tests;

core integration tests;

recommendation regression suite;

import/export tests;

offline tests;

representative performance tests;

critical manual workflows.


Any known failure affecting a core workflow should block release unless explicitly accepted as a documented release decision.


---

69. Release-blocking defects

The following should normally block a V1 release:

garden data loss;

historical data corruption;

failed supported database migration;

incorrect import that silently corrupts data;

core workflow inability;

recommendations contradicting deterministic rules;

unexplained critical recommendations;

planned activity being recorded as completed without user action;

offline failure of core local functionality;

serious accessibility failure affecting core workflows.



---

70. Non-blocking defects

Lower-priority defects may be accepted when they do not materially affect:

data integrity;

core workflows;

recommendation correctness;

offline operation;

accessibility;

historical preservation.


Such defects should still be recorded for later resolution.


---

71. Test coverage philosophy

High code coverage is useful but is not the primary success criterion.

The goal is meaningful behavioural coverage.

Priority should be given to:

1. core workflows;


2. data integrity;


3. recommendation correctness;


4. historical preservation;


5. migrations;


6. import/export;


7. offline operation;


8. architectural boundaries;


9. accessibility;


10. secondary features.



A high coverage percentage does not compensate for missing tests of critical behaviour.


---

72. What should not be over-tested

Avoid creating large numbers of brittle tests for:

purely cosmetic styling;

exact spacing;

temporary UI wording;

implementation details;

framework internals;

trivial getters/setters;

generated code.


Tests should provide meaningful protection against regressions.


---

73. Test documentation

Tests should be understandable to future developers.

Important test cases should describe:

the scenario;

relevant assumptions;

input state;

expected result;

why the behaviour matters.


Complex recommendation tests should identify the specific rule or reasoning behaviour being protected.


---

74. Test data and fixtures

Fixtures should be:

deterministic;

version-controlled;

minimal where possible;

representative;

independent of real user data.


Sensitive or personal garden information must not be used as automated test data unless deliberately sanitised and authorised.


---

75. Testing future modules

Future modules should provide their own tests while respecting core application contracts.

A future module must not require unrelated modules to be operational merely for the core garden to function.

Examples include:

weather;

community knowledge;

external data;

advanced analytics.


Optional module tests should verify failure isolation as well as successful operation.


---

76. Definition of done

A V1 feature is not considered complete merely because its screen exists.

A feature should generally be considered complete when:

domain behaviour is tested;

persistence is tested where applicable;

important UI behaviour is tested;

relevant integration workflows are tested;

error states are handled;

offline behaviour is verified where applicable;

historical implications are considered;

accessibility has been checked;

relevant recommendation/explanation behaviour is tested;

regression coverage exists for important edge cases.



---

77. Testing priorities

When development time is limited, testing priority should be:

Critical

data integrity;

database migrations;

core workflows;

recommendation correctness;

history preservation;

import/export;

offline operation.


High

Quick Add;

planning;

observations;

soil reasoning;

search/filtering;

accessibility;

navigation/context preservation.


Medium

advanced preferences;

experiments;

detailed provenance presentation;

secondary UI states.


Lower

cosmetic refinement;

non-essential convenience features.



---

78. Core V1 test matrix

Area	Unit	Data	Widget	Integration	Migration	Manual

Garden setup	✓	✓	✓	✓	✓	✓
Growing spaces	✓	✓	✓	✓	✓	✓
Map/spatial behaviour	✓	✓	✓	✓	—	✓
Plant Library	✓	✓	✓	✓	✓	✓
My Plants	✓	✓	✓	✓	✓	✓
Quick Add	✓	✓	✓	✓	—	✓
Activities	✓	✓	✓	✓	✓	✓
Observations	✓	✓	✓	✓	✓	✓
Soil	✓	✓	✓	✓	✓	✓
Harvests/losses	✓	✓	✓	✓	✓	✓
Planner	✓	✓	✓	✓	✓	✓
Recommendations	✓	✓	✓	✓	✓	✓
History	✓	✓	✓	✓	✓	✓
Inbox	✓	✓	✓	✓	✓	✓
Search/filtering	✓	✓	✓	✓	—	✓
Import/export	✓	✓	✓	✓	✓	✓
Localisation readiness	✓	✓	✓	✓	✓	✓
Offline behaviour	✓	✓	✓	✓	—	✓



---

79. Core testing rule

Whenever a change affects a shared concept, test both the changed feature and the core workflows that depend on it.

Examples:

Vocabulary change
→ rules
→ recommendations
→ import/export
→ UI

Database change
→ repositories
→ history
→ import/export
→ core workflows

Recommendation change
→ factor tests
→ confidence
→ explanations
→ planner
→ personal history

UX/navigation change
→ widget tests
→ integration workflows
→ context preservation


---

80. Final principle

> Test the garden the way the gardener experiences it, while protecting the data and rules underneath.



The V1 testing strategy should ensure that the application remains:

reliable;

deterministic where required;

explainable;

offline-capable;

historically safe;

modular;

accessible;

maintainable.


The most important test is not whether an individual component works.

It is whether the gardener can safely use the application to understand, plan, record and learn from their garden without losing information or being misled by the system.