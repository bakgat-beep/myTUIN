Garden Planner & Manager — Core Architecture

Document: docs/CORE_ARCHITECTURE.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the architectural principles and boundaries of the Garden Planner & Manager application.

It describes how the major parts of the application fit together without prescribing implementation details that belong in the technical architecture.

The architecture must support the V1 product requirements while remaining suitable for later expansion.

The architecture prioritises:

local-first operation;

offline functionality;

data integrity;

historical preservation;

deterministic behaviour;

explainable recommendations;

modularity;

progressive precision;

future extensibility;

simple user interaction.



---

2. Architectural Goal

The application should behave as:

> A garden notebook that understands your garden.



The architecture therefore separates:

1. the gardener's records;


2. general horticultural knowledge;


3. deterministic reasoning;


4. recommendations;


5. user decisions;


6. presentation.



These concerns interact, but should not become unnecessarily coupled.


---

3. Core Architectural Principles

3.1 Local first

The garden's primary data lives locally on the user's device.

Core functionality must not depend on an Internet connection.

The application must remain useful for:

viewing the garden;

viewing plants;

recording activities;

recording observations;

recording harvests;

planning;

recommendations based on stored information;

reviewing history.


Connectivity may support future optional capabilities, but it must not become a hidden dependency of core garden operation.


---

3.2 No AI dependency

V1 does not require AI.

Recommendations, suitability assessments, diagnostics and other reasoning must be based on:

structured data;

deterministic rules;

explicit calculations;

recorded observations;

known relationships;

transparent user preferences;

garden history.


Future AI functionality must not be required for the core architecture to operate.


---

3.3 Explainability

Important application outputs must be explainable.

A recommendation should be traceable to:

the factors considered;

the relevant garden information;

applicable plant knowledge;

rules or calculations;

user preferences;

personal history;

uncertainty;

missing information.


The system must not produce unexplained scores that cannot be interpreted by the user.


---

3.4 Historical preservation

Garden history is a core product asset.

The architecture must preserve historical records rather than allowing changes to current information to silently rewrite the past.

Important historical information includes:

plantings;

activities;

observations;

measurements;

harvests;

losses;

problems;

interventions;

experiments;

recommendations;

user decisions;

outcomes.



---

3.5 Progressive precision

The architecture must allow information to begin as an observation or estimate and later become more precise.

For example:

Unknown

→ observed

→ estimated

→ measured

→ repeatedly measured

The application must retain the distinction between what was:

observed;

measured;

estimated;

inferred.


A more precise later record must not fabricate precision in earlier history.


---

3.6 Explicit uncertainty

Unknown information is legitimate application state.

The architecture must support distinctions such as:

unknown;

not recorded;

not applicable;

not measured;

observed;

measured;

estimated;

inferred.


Missing information must not automatically be converted into a false assumption.


---

3.7 User authority

The application assists the gardener but does not control the garden.

Recommendations may be:

accepted;

rejected;

modified;

overridden;

deferred;

cancelled.


An override is a legitimate user decision.

The architecture must not interpret disagreement with a recommendation as an application error.


---

3.8 Modular boundaries

Major functional areas should communicate through defined domain concepts and interfaces.

A module should not directly depend on the internal implementation of another module where a stable interface can be used instead.

The architecture should allow individual modules to evolve without unnecessarily destabilising unrelated functionality.


---

4. Architectural Layers

The application is conceptually organised into the following layers:

Presentation
    ↓
Application / Use Cases
    ↓
Domain
    ↓
Persistence / Infrastructure

Knowledge and deterministic reasoning operate as domain services rather than becoming presentation concerns.


---

5. Presentation Layer

The presentation layer is responsible for:

screens;

navigation;

user interaction;

displaying records;

collecting input;

displaying recommendations;

progressive disclosure;

localisation;

accessibility;

user feedback.


It must not contain core business rules.

For example, a screen may display:

> Good candidate



but must not itself determine whether a plant is a good candidate.

That determination belongs to the recommendation domain.


---

6. Application Layer

The application layer coordinates user-facing operations.

Examples include:

add plant;

record activity;

record observation;

record harvest;

create planting plan;

evaluate a growing space;

complete planned activity;

review history;

import data;

export data.


The application layer coordinates domain services and persistence.

It should not duplicate domain rules.


---

7. Domain Layer

The domain layer contains the core concepts and behaviour of the application.

Major domains include:

garden and spatial management;

plants;

plantings;

activities;

observations;

soil and conditions;

problems;

planning;

recommendations;

history;

experiments;

provenance;

user decisions.


The domain layer should remain independent of UI technology and database implementation.


---

8. Persistence Layer

Persistence is responsible for storing and retrieving application data.

V1 persistence is local.

The persistence layer should provide repositories or equivalent interfaces so that domain/application logic does not depend directly on database implementation details.

Persistence must preserve:

identifiers;

relationships;

dates;

historical records;

provenance;

information state;

confidence;

vocabulary identifiers.



---

9. Core Domain Model

The application is centred around a user's garden and the records associated with it.

Conceptually:

Garden
 ├── Areas
 ├── Growing Spaces
 ├── Infrastructure
 ├── Plants / Plantings
 ├── Conditions
 ├── Activities
 ├── Observations
 ├── Harvests
 ├── Problems
 ├── Plans
 ├── Recommendations
 ├── Decisions
 └── Experiments

General horticultural knowledge is maintained separately from the user's garden records.


---

10. Knowledge versus Garden Data

The architecture must clearly separate:

General knowledge

Examples:

Tomato is frost-sensitive.

A particular cultivar has a particular maturity period.

A plant has particular light requirements.

A pest affects particular plants.


User garden data

Examples:

Tomato was planted in Bed 2.

Bed 2 receives approximately six hours of observed sunlight.

The user recorded poor tomato performance there.

The user's soil pH measurement was 6.7.


General knowledge may be used to interpret garden data.

Garden data must not silently alter general knowledge.


---

11. Plant Knowledge and Garden Plants

The application distinguishes between:

Plant knowledge

General information about a plant or cultivar.

Examples:

identity;

lifecycle;

requirements;

planting windows;

growth stages;

relationships;

problems;

interventions.


Garden plant/planting

The user's actual or planned occurrence of a plant.

Examples:

Tomato in Bed 2;

planned lettuce in Bed 3;

Black Krim tomato planted on a particular date.


The same plant knowledge may be associated with many garden plantings.


---

12. Planting as a Historical Record

Planting information must be treated as historical garden information rather than simply a current property of a growing space.

A planting can record:

plant;

cultivar where known;

location;

planned date;

actual date;

status;

source or provenance where relevant;

subsequent activities;

observations;

harvests;

outcomes.


Changing the current contents of a bed must not erase previous plantings.


---

13. Activities as Event Records

Activities represent things that were planned or actually occurred.

Examples:

planting;

watering;

feeding;

pruning;

harvest;

intervention;

other garden actions.


A planned activity and a completed activity are distinct states.

A plan does not constitute evidence that an activity occurred.

Actual completion should create or update an appropriate actual event record.


---

14. Partial Completion

The architecture must support partial completion.

For example:

A plan may apply to four beds.

Three beds may be completed while one remains outstanding.

The system must therefore avoid modelling a multi-target activity as simply:

complete = true

where that would lose meaningful information.

Completion must be capable of representing the relevant scope.


---

15. Observations

Observations are records of what the gardener noticed or recorded.

Examples:

leaves yellowing;

water pooling;

soil feeling compacted;

flowering;

fruiting;

visible pest damage.


An observation is not automatically a diagnosis.

The architecture must preserve the distinction between:

Observed:
leaves yellowing

and:

Possible explanation:
nutrient deficiency


---

16. Measurements

Measurements are structured evidence.

Examples:

pH;

temperature;

drainage duration;

sunlight hours;

rainfall;

quantity;

dimensions.


A measurement should retain relevant context such as:

value;

unit;

date;

location;

method;

source;

confidence where appropriate.


Measurements must not be represented merely as qualitative vocabulary values when the underlying information is numeric.


---

17. Conditions

Garden conditions may be represented through a combination of:

measurements;

observations;

estimates;

inferred values;

structured classifications.


Examples include:

light;

water;

moisture;

soil;

pH;

fertility;

temperature;

frost;

drainage.


The architecture must distinguish the underlying evidence from any derived interpretation.


---

18. Spatial Model

The garden has a spatial hierarchy but is not limited to a strict ownership hierarchy.

Potential objects include:

garden boundary;

area;

growing space;

plant location;

infrastructure;

irrigation;

root zone;

other spatial regions.


Objects may overlap.

The architecture must therefore support spatial relationships without requiring every object to belong exclusively to one parent space.


---

19. Geometry Independence

Spatial data should be represented independently of the map-rendering technology.

The domain should understand concepts such as:

point;

line;

polygon;

dimensions;

location;

spatial relationship.


The map UI is responsible for displaying and editing these concepts.

Changing map technology should not require rewriting the garden domain model.


---

20. Historical Spatial Information

Changing a growing space's geometry must not erase its history.

For example:

A bed may change from:

3 m × 1 m

to:

4 m × 1 m.

The current geometry should update.

Historical plantings and activities associated with the bed must remain interpretable.

Where historical geometry materially affects analysis, the architecture must retain sufficient historical information to distinguish current from historical spatial state.


---

21. Temporal Model

Dates and times are first-class information.

The architecture must distinguish:

planned;

actual;

historical;

current;

upcoming.


Temporal state should not replace actual dates.

For example:

planned_date = 2026-09-10
actual_date = null

is different from:

planned_date = 2026-09-10
actual_date = 2026-09-12


---

22. Seasonal Interpretation

Seasonal interpretation depends on geographic context.

The architecture must not hard-code assumptions such as:

March = spring

Instead, seasonal interpretation should use:

garden location;

hemisphere;

applicable climate/geographic context;

seasonal definitions.


Knowledge originating from another hemisphere may be adapted through explicit transformation rather than silently relabelled.


---

23. Vocabulary Architecture

Controlled vocabulary identifiers are part of the domain contract.

Stored values use stable canonical identifiers such as:

perennial
partial_shade
high
user_observation
planned

User-facing labels are supplied by localisation.

Business logic must operate on identifiers rather than translated text.


---

24. Vocabulary Boundaries

The application separates:

Core vocabularies
Garden vocabularies
Plant vocabularies
Soil vocabularies
Activity vocabularies
Problem vocabularies
Planning vocabularies
Provenance vocabularies

Feature-specific classifications should not be promoted into Core merely for convenience.

This keeps the architecture modular.


---

25. Provenance

Important knowledge and evidence should retain provenance where appropriate.

The architecture supports distinctions such as:

verified reference;

curated guidance;

published research;

institutional guidance;

expert guidance;

community observation;

user observation;

imported dataset;

experimental result;

derived analysis.


Provenance is distinct from confidence.

A source being authoritative does not automatically mean that every application inference based on it is highly confident.


---

26. Adapted Knowledge

When knowledge is adapted from another context, the relationship must remain explicit.

For example:

Source guidance
    ↓
adapted
    ↓
application-specific guidance

The adapted information should preserve relevant source context.

This is particularly important for:

hemisphere;

climate;

season;

geographic applicability;

growing method.



---

27. Recommendation Architecture

Recommendations are produced by deterministic reasoning over structured information.

Conceptually:

Garden context
        +
Plant knowledge
        +
Conditions
        +
Timing
        +
Spatial constraints
        +
Relationships
        +
Rotation
        +
History
        +
Preferences
        ↓
Recommendation Engine
        ↓
Recommendation
        +
Factors
        +
Reasons
        +
Confidence
        +
Missing information
        +
Conditions

The recommendation engine must not depend on UI wording.


---

28. Recommendation Results

A recommendation should be more than a single score.

It should be capable of expressing:

recommendation status;

factor results;

reasons;

confidence;

missing information;

conditions;

applicable knowledge;

user preference effects;

personal history effects.


For example, a plant may be:

status:
recommended

while one factor is:

temperature:
marginal

and confidence is:

moderate

because winter temperature information is incomplete.


---

29. Recommendation Factors

Factors describe how individual considerations affect a recommendation.

Potential factors include:

light;

water;

moisture;

soil;

pH;

fertility;

nutrients;

temperature;

frost;

timing;

space;

rotation;

relationships;

infrastructure;

history.


The engine should evaluate only factors relevant to the specific recommendation.


---

30. Recommendation Reasoning

Rules should produce structured reasoning results.

Examples:

light_match
rotation_risk
space_constraint
positive_history
missing_information
temperature_constraint

The UI converts these into understandable explanations.

The recommendation engine should not generate user-facing prose as its primary output.


---

31. Recommendation Confidence

Suitability and confidence are separate concepts.

For example:

Suitability:
good

Confidence:
moderate

A recommendation may be highly suitable but poorly supported because important garden information is unknown.

Conversely, a recommendation may have high confidence that a plant is unsuitable because a decisive constraint is well established.


---

32. Conditional Recommendations

The architecture must support conditional results.

Examples:

recommended_if_condition_met

or equivalent structured representation.

Examples of user-facing outcomes include:

suitable if drainage is improved;

suitable once frost risk decreases;

potentially suitable, but pH should be confirmed.


A recommendation does not have to be reduced to yes/no.


---

33. Insufficient Information

Missing information must be capable of affecting a recommendation explicitly.

The engine should be able to report:

insufficient_information

when missing information prevents a reliable conclusion.

It may also produce a usable conditional or provisional recommendation where appropriate.

The engine must not invent missing values simply to produce a result.


---

34. Personal History

Garden history may influence recommendations.

For example:

General suitability:
good

Garden history:
poor results in this space for two seasons

The recommendation should identify personal history as a separate evidence source.

Personal history must not silently rewrite general horticultural knowledge.


---

35. User Preferences

Preferences are inputs to recommendation reasoning.

Examples include:

plants to avoid;

preferred plants;

water conservation;

low-maintenance gardening;

native preference;

pollinator preference;

food production priorities.


A preference should normally affect recommendation presentation rather than corrupting the underlying suitability assessment.

For example:

Suitability:
excellent

User preference:
excluded

This preserves transparency.


---

36. User Decisions

Recommendations do not automatically create actions.

The architecture separates:

Recommendation
    ↓
User Decision
    ↓
Optional Activity / Plan

The user may:

accept;

reject;

modify;

override;

defer;

cancel.


A decision records the gardener's choice.

It does not prove that the intended action subsequently occurred.


---

37. Inbox Architecture

Inbox items are generated from meaningful garden conditions, plans, recommendations and other relevant events.

An Inbox item may represent:

action;

warning;

observation request;

information request;

experiment;

useful knowledge.


Inbox is a prioritised presentation of actionable information, not the canonical source of garden truth.

The underlying garden records remain authoritative.


---

38. Inbox State

Inbox state must distinguish concepts such as:

active;

completed/resolved;

muted;

dismissed.


Muting and dismissal have different meanings.

Muting means:

> Do not surface this prominently for now.



Dismissal means:

> This item has been dealt with or is no longer relevant.



Neither should delete the underlying garden information.


---

39. Experiments

Experiments are an optional advanced layer over ordinary garden records.

An experiment may define:

question;

hypothesis;

treatment;

control;

timeframe;

measurements;

observations;

outcome;

conclusion.


Experiments should reuse ordinary activities, observations and measurements where possible rather than creating a separate parallel record system.


---

40. Experiment Conclusions

The architecture must distinguish:

Observed result

from:

Conclusion

A conclusion may be:

tentative;

supported;

inconclusive.


The application must not turn a single garden experiment into a universal horticultural rule automatically.


---

41. Diagnostic Architecture

Problems and diagnoses must remain distinct from observations.

Conceptually:

Observation
    ↓
Evidence
    ↓
Possible explanation(s)
    ↓
Assessment

A diagnosis may contain:

candidate problem;

supporting evidence;

contradicting evidence;

confidence;

affected plant/location;

suggested investigation;

suggested intervention.


Recording a symptom must never silently create a confirmed diagnosis.


---

42. Intervention Architecture

Interventions represent actions taken to address a condition or problem.

An intervention should be recorded as an actual garden event when it occurs.

It may be associated with:

target;

reason;

method;

date;

quantity where appropriate;

outcome;

observations.


The architecture should preserve the distinction between:

recommended intervention

and:

intervention performed


---

43. Data State versus Derived State

The architecture distinguishes stored evidence from derived information.

Stored information

Examples:

pH measurement;

planting date;

observed pooling;

harvest quantity;

user decision.


Derived information

Examples:

estimated drainage classification;

suitability;

recommendation confidence;

seasonal interpretation;

risk assessment.


Derived information should be reproducible from its inputs wherever practical.

The application should avoid storing derived results as though they were direct observations.


---

44. Derived Results and Explainability

When derived information is stored or cached for performance, it should remain possible to identify:

what it represents;

when it was calculated;

what inputs or rules produced it;

whether it may need recalculation.


A cached recommendation must never become indistinguishable from a user-recorded fact.


---

45. Rule Engine

The rule system is deterministic.

Rules should operate on:

canonical identifiers;

structured values;

relationships;

dates;

spatial information;

user records;

provenance where relevant.


Rules may:

support;

contradict;

increase risk;

decrease risk;

recommend;

discourage;

require information;

have no effect.


Rules should be individually identifiable and versionable.


---

46. Rule Lifecycle

Rules may have states such as:

active
experimental
deprecated
disabled

Experimental rules must be distinguishable from established rules.

Changing a rule must not silently alter the historical meaning of an already recorded user decision or event.

Where historical recommendations need to remain reproducible, sufficient rule/version information must be retained.


---

47. Search and Filtering

Search and filtering operate over canonical domain concepts.

Search may cover:

plants;

cultivars;

growing spaces;

activities;

observations;

problems;

plans;

history.


Aliases may map user terms to canonical concepts.

Search must not require users to know canonical identifiers.


---

48. Import and Export

Import/export operates through the application data model rather than directly exposing persistence implementation.

Imports must:

1. parse;


2. validate;


3. identify warnings/errors/conflicts;


4. present a summary;


5. allow cancellation;


6. apply accepted changes;


7. report results.



Imported vocabulary values must use canonical identifiers.

Display labels must not be treated as authoritative identifiers.


---

49. Import Safety

Invalid or conflicting data must not silently overwrite existing garden records.

The import process must preserve:

historical information;

identifiers where possible;

provenance;

vocabulary compatibility;

relationships.


Migration and import logic should be separate concerns.


---

50. Data Integrity

The architecture should enforce important invariants.

Examples:

a completed activity must have an appropriate actual occurrence;

a planned activity must not automatically become completed;

historical records must retain their temporal context;

a measurement must retain its unit;

a translated label must not become the canonical identifier;

a recommendation must not be treated as a user decision;

an observation must not automatically become a diagnosis;

a user preference must not silently alter general knowledge;

deleted UI references must not orphan important historical information.



---

51. Identity

Domain records require stable identifiers.

Identifiers should remain independent of:

display names;

translations;

screen locations;

database row positions where possible.


Renaming an object should not create a new conceptual object unless the domain meaning has actually changed.


---

52. Relationships

Relationships should be explicit where they carry domain meaning.

Examples:

plant belongs to plant family;

planting occurs in growing space;

activity affects plant;

observation concerns plant;

observation concerns soil;

harvest derives from planting;

recommendation concerns candidate plant and context;

decision responds to recommendation.


The architecture should avoid unnecessary relationship objects when a direct relationship is sufficient.


---

53. Avoiding Over-Engineering

The architecture should not introduce a separate abstraction for every concept.

A new domain object should generally exist when it:

has its own identity;

has meaningful lifecycle/history;

participates in important relationships;

requires independent provenance;

is reused by multiple modules;

requires independent reasoning.


Simple values should remain simple values.


---

54. Configuration versus Domain Data

Application configuration should remain separate from garden records.

Examples of configuration:

interface preferences;

notification settings;

display preferences;

localisation;

unit preferences.


Examples of garden data:

bed dimensions;

soil observations;

plantings;

harvests.


Changing a UI preference must not alter garden history.


---

55. Offline Architecture

Core use cases should operate entirely against local data.

The application should not require a network request for:

opening Home;

opening Garden;

viewing plants;

recording activities;

recording observations;

recording harvests;

reviewing history;

running local recommendation rules.


Future external services must be isolated so that their failure does not prevent unrelated local functionality.


---

56. Optional External Services

Potential future services include:

weather;

external plant databases;

knowledge updates;

community knowledge;

backup/synchronisation.


These should be implemented as optional infrastructure integrations.

The domain must not become dependent on their availability.


---

57. Synchronisation and Future Cloud Support

V1 does not require cloud synchronisation.

If synchronisation is introduced later, it must be designed around the existing domain identifiers and historical records.

Cloud synchronisation must not require redesigning the core garden model.

Conflicts must be handled explicitly rather than silently overwriting records.


---

58. Localisation Architecture

Localisation applies to presentation.

The architecture should keep:

canonical identifiers;

internal relationships;

database values;

rule inputs;

import/export identifiers


independent of interface language.

Text displayed to the user belongs to the localisation layer.


---

59. Unit Architecture

Domain measurements should retain canonical units or an explicitly defined standard representation.

The presentation layer may convert values for the user's preferred unit system.

For example:

Stored:
3 metres

Displayed:
3 m

or an equivalent local preference.

Conversion must not silently reduce meaningful precision.


---

60. Accessibility Architecture

Accessibility is a presentation concern but must be supported by domain semantics.

For example, the domain should provide:

recommendation_status = recommended

rather than only a colour.

The UI can then represent the state through:

text;

icons;

colour;

accessible labels.


Important information must never depend solely on colour.


---

61. Performance Architecture

The architecture should prioritise fast local operations.

Common operations include:

opening Home;

opening Garden;

viewing a plant;

recording an activity;

saving an observation;

opening Quick Add.


Complex recommendation calculations may be performed separately from immediate UI interaction where necessary.

Caching may be used for derived information, provided cached results remain distinguishable from authoritative records.


---

62. Error Isolation

Failure of an optional module must not unnecessarily disable unrelated core functionality.

For example:

Weather unavailable
        ↓
Weather information unavailable

but:

Garden available
Plants available
History available
Local recommendations available

The architecture should favour graceful degradation.


---

63. Core Module Boundaries

At a high level, V1 consists of:

Garden / Spatial
Plant Knowledge
Garden Plants / Plantings
Activities
Observations / Conditions
Soil
Problems / Diagnostics
Planning
Recommendations
History
Inbox
Experiments
Provenance
Import / Export

These are conceptual boundaries.

They do not require every boundary to become a separate package or executable component.


---

64. Dependency Direction

Dependencies should generally flow toward stable domain concepts.

A simplified direction is:

UI
 ↓
Application Services
 ↓
Domain Services
 ↓
Domain Models
 ↓
Persistence Interfaces
 ↓
Infrastructure

The domain should not depend on:

screen widgets;

navigation;

localisation;

database-specific APIs;

network-specific APIs.



---

65. Cross-Module Communication

Modules should communicate through:

domain objects;

identifiers;

repository interfaces;

application services;

domain services;

structured events where appropriate.


A module should not reach into another module's persistence tables or internal implementation merely to obtain information.


---

66. Shared Domain Concepts

Some concepts intentionally cross module boundaries.

Examples:

Plant;

Cultivar;

Growing Space;

Date/time;

Measurement;

Observation;

Activity;

Provenance;

Confidence;

Recommendation;

User Decision;

Vocabulary identifiers.


These should have stable definitions.


---

67. Avoiding Duplicate Concepts

The architecture must avoid creating multiple competing representations of the same concept.

For example, the application should not have separate unrelated concepts for:

a plant in the Plant Library;

a plant used by Planner;

a plant used by Recommendation Engine;

a plant used by Search.


They should refer to the same underlying plant knowledge concept.

Similarly, an actual watering event should remain the same activity regardless of whether it is viewed from:

Home;

Garden;

Plant detail;

History;

Inbox.



---

68. History as a Cross-Cutting Capability

History is not merely a screen.

It is the ability to retrieve and interpret previous domain records.

History may be accessed through:

Garden;

Plant;

Growing Space;

Activity;

Harvest;

Observation;

Recommendation;

Experiment.


The architecture should therefore preserve historical records in their originating domains while providing shared historical querying.


---

69. Current State versus History

Current state may be derived from historical records.

For example:

Current plants in Bed 2

may be determined from active plantings.

Past plants in Bed 2

may be determined from historical plantings.

The application should avoid storing multiple independent sources of truth where one can be derived reliably.


---

70. Record Corrections

Correcting a record should not unnecessarily destroy its historical meaning.

Simple corrections may update the record directly.

Where the correction materially changes a historical fact, the architecture should support recording:

original information;

corrected information;

correction reason;

modification date;

modifying source.


This is particularly important for measurements and historical observations.


---

71. Deletion and Archival

The architecture should favour:

archival;

deactivation;

soft deletion;

explicit historical correction


over destructive deletion where records have historical significance.

Deleting a current representation must not unintentionally remove historical evidence.


---

72. Data Migration

Future versions must migrate existing records rather than discard unsupported information.

A migration should:

1. identify the old structure;


2. transform it into the new structure;


3. preserve meaning;


4. preserve identifiers where possible;


5. record migration provenance where appropriate;


6. validate the result.



Migrations must not invent information that did not exist in the original data.


---

73. Architectural Treatment of Unknowns

Unknown information is valid domain state.

For example:

winter_sunlight = unknown

does not mean:

winter_sunlight = poor

Recommendation rules must explicitly determine how unknown information affects their result.

Different rules may reasonably respond differently.


---

74. Architectural Treatment of Estimates

An estimate must retain its evidential nature.

For example:

estimated drainage = slow

is not equivalent to:

measured drainage = slow

The underlying observations or measurements supporting the estimate should remain available where appropriate.


---

75. Architectural Treatment of Confidence

Confidence belongs to a claim or assessment, not necessarily to the underlying object.

For example:

Observation:
water pools after rain

Observation confidence:
high

Derived estimate:
slow drainage

Inference confidence:
moderate

The architecture should not attach a single generic confidence value to an entire garden record when different claims have different evidential strength.


---

76. Recommendation Reproducibility

Where practical, important recommendations should be reproducible from:

the relevant garden state;

applicable plant knowledge;

rule versions;

vocabulary meanings;

relevant dates;

user preferences;

personal history.


This is especially important for debugging and understanding why a recommendation changed.


---

77. Recommendation History

A current recommendation should not overwrite the fact that a different recommendation was previously produced.

Where recommendation history is retained, it should be possible to distinguish:

recommendation at the time;

context at the time;

user decision;

later outcome.


This allows the application to learn from garden history without rewriting the past.


---

78. Personal Learning Boundary

Personal learning is derived from the user's own garden history.

It may identify patterns such as:

> Poor establishment repeatedly occurred under recorded poor-drainage conditions.



Such patterns should remain clearly labelled as:

personal history

or equivalent.

They must not automatically become universal horticultural knowledge.


---

79. Community Knowledge Boundary

Future community functionality must remain separate from private garden operation.

A user's private records should not become shared knowledge without explicit participation.

Shared knowledge must retain provenance and appropriate geographic precision.

The community system must not be required for V1 garden functionality.


---

80. Security and Privacy Boundary

Garden data should be treated as private by default.

Potentially sensitive information includes:

exact garden location;

household location;

personal notes;

private observations;

garden history.


Future sharing functionality must explicitly control what is shared and at what geographic precision.


---

81. Architecture and User Experience

The architecture exists to support the UX principles rather than expose itself to the gardener.

Users should experience:

Add plant
Record observation
What could I grow here?
Why?
Record harvest
View history

rather than:

Create PlantInstance
Create ObservationEntity
Execute RecommendationRuleSet

Internal complexity must remain behind stable user-facing workflows.


---

82. Architecture and Progressive Disclosure

The domain model may contain considerably more information than the primary UI displays.

The architecture must therefore support retrieving:

immediate information;

explanatory information;

technical information


without requiring all levels to appear simultaneously.


---

83. Architecture and Quick Add

Quick Add is a presentation/application workflow over existing domain primitives.

It should not create a parallel simplified database.

For example:

Quick Add → Water

should create the same underlying activity concept used elsewhere in the application.

This prevents duplicate activity histories.


---

84. Architecture and Planner

Planner is a consumer and coordinator of:

garden state;

plant knowledge;

spatial information;

timing;

recommendations;

user decisions.


Planner should not duplicate recommendation logic.

Suitability belongs to the recommendation domain.

Planning determines how the gardener may act on that information.


---

85. Architecture and Home

Home is a prioritised view over existing domain information.

It should not become a separate source of truth.

Home may surface:

recommendations;

upcoming plans;

Inbox items;

recent activity;

garden status;

experiments.


The underlying records remain owned by their respective domains.


---

86. Architecture and Inbox

Inbox should likewise be a presentation/work-queue layer over meaningful domain conditions.

An Inbox item should reference the underlying reason or record rather than duplicate the complete underlying information.


---

87. Architecture and Map

The map is a spatial interface over garden domain information.

The map must not become the authoritative owner of:

plant history;

activities;

observations;

soil records;

recommendations.


It should provide spatial context for those records.


---

88. Architecture and Plant Library

Plant Library is the presentation and retrieval interface for general plant knowledge.

It should not store user-specific garden state inside plant knowledge records.

User-specific information belongs to the garden's plantings and related records.


---

89. Architecture and My Plants

My Plants is the user-facing representation of actual and planned plantings.

It should retrieve general information from Plant Knowledge rather than copying it.

This prevents plant knowledge from becoming inconsistent between plantings.


---

90. Architecture and Soil

Soil functionality should combine:

soil observations;

measurements;

estimates;

classifications;

recommendations.


The architecture must preserve the evidence chain.

For example:

Observation
    ↓
Water pools after rain

Inference
    ↓
Likely slow drainage

Recommendation
    ↓
Improve drainage before planting

Each stage remains distinguishable.


---

91. Architecture and Diagnostics

Diagnostics should consume observations and other evidence.

They should not replace the original observations.

For example:

Observation:
yellow leaves

Possible problems:
nutrient deficiency
water stress
disease

The possible problems remain assessments rather than rewriting the original record.


---

92. Architecture and Experiments

Experiments should reference ordinary garden records.

For example:

Experiment
    ↓
Treatment
    ↓
Activities
    ↓
Observations
    ↓
Measurements
    ↓
Harvests
    ↓
Outcome
    ↓
Conclusion

This avoids creating a parallel experimental data system.


---

93. Architecture and Import/Export

Import/export must operate at the domain boundary.

The file format should represent stable domain concepts rather than database implementation details.

This allows the persistence technology to change without making the user's exported garden data obsolete.


---

94. Architecture and Testing

The architecture should make important behaviour testable independently of the UI.

Particularly:

recommendation rules;

suitability calculations;

seasonal interpretation;

vocabulary validation;

historical queries;

activity state transitions;

import validation;

data migrations;

provenance handling.


Deterministic domain logic should be testable with controlled inputs and expected outputs.


---

95. Architectural Non-Goals

The core architecture does not currently require:

AI;

cloud-first operation;

mandatory accounts;

social networking;

professional GIS;

real-time collaboration;

automated garden design;

sophisticated weather infrastructure;

complex statistical analytics;

gamification.


These should not influence V1 architecture unless they become necessary to core product functionality.


---

96. Simplification Rules

When architectural complexity is introduced, apply the following questions:

1. Is this needed for a V1 workflow?


2. Is this a real domain concept or an implementation convenience?


3. Can an existing concept represent it?


4. Does it preserve historical meaning?


5. Does it improve explainability?


6. Does it introduce unnecessary coupling?


7. Can it remain optional?


8. Can it be tested independently?


9. Does it require a new vocabulary?


10. Is the additional complexity justified?



If not, prefer the simpler design.


---

97. Architectural Decision Hierarchy

When architectural choices conflict, prioritise:

1. Core garden functionality


2. Data integrity


3. Historical preservation


4. Offline operation


5. Explainability


6. Modularity


7. Testability


8. Progressive precision


9. Future extensibility


10. Implementation convenience



Implementation convenience must not compromise domain integrity.


---

98. V1 Architectural Boundary

V1 should establish a reliable foundation for:

Garden
    ↓
Growing Spaces
    ↓
Plants / Plantings
    ↓
Activities
    ↓
Observations / Conditions
    ↓
Planning
    ↓
Recommendations
    ↓
User Decisions
    ↓
History

with supporting:

Plant Knowledge
Soil
Problems
Provenance
Inbox
Experiments
Import / Export

The architecture should be sufficient for these workflows without attempting to solve every future gardening problem.


---

99. Final Architectural Principle

The architecture should make the application:

> Simple to use, rigorous underneath.



The gardener should not need to understand:

entities;

repositories;

rule engines;

vocabulary identifiers;

provenance models;

spatial data structures;

recommendation calculations.


Those concepts exist to make the application reliable.

The architecture should therefore favour:

a small number of strong domain concepts;

explicit relationships;

stable identifiers;

preserved history;

deterministic reasoning;

clear evidence;

explicit uncertainty;

modular boundaries;

local-first operation;

simple workflows.


The goal is not to build the most elaborate garden-management architecture possible.

The goal is to build the simplest architecture capable of making the garden progressively more useful to the gardener without sacrificing the integrity of the information on which that usefulness depends.