Worked for 16s

Garden Planner & Manager — Data Model

Document: docs/DATA_MODEL.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the conceptual data model for Version 1 of the Garden Planner & Manager.

It describes:

the information the application stores;

the relationships between information;

the distinction between garden facts, observations, knowledge and recommendations;

how history is preserved;

how uncertainty is represented;

how planning differs from actual events;

how user decisions are recorded;

how the model can expand in future versions.


This is a conceptual model, not a final database schema.

Implementation details such as table names, ORM classes, indexes and storage technology should be defined separately.


---

2. Core design principles

The data model follows these principles.

2.1 The garden is the user's source of truth

The application stores the user's garden and its history separately from general horticultural knowledge.

General knowledge must not overwrite user records.


---

2.2 Facts, observations, interpretations and recommendations are different

The model must distinguish between:

Fact / measurement
        ↓
Observation
        ↓
Inference / interpretation
        ↓
Recommendation
        ↓
User decision
        ↓
Actual activity
        ↓
Outcome

These stages must not be silently collapsed into one record.

For example:

Observation:
Leaves are yellowing.

Possible inference:
Water stress — moderate confidence.

Recommendation:
Check drainage before increasing fertiliser.

User decision:
Deferred.

Actual activity:
None recorded.

The system must not treat the recommendation or decision as evidence that an activity occurred.


---

3. Core entities

The V1 conceptual model is centred around the following entities:

Garden
 ├── Area
 │    └── Growing Space
 │
 ├── Spatial Object
 │
 ├── Plant Instance
 │
 ├── Activity
 ├── Observation
 ├── Problem
 ├── Harvest
 ├── Soil Record
 ├── Plan
 ├── Recommendation
 ├── User Decision
 └── Experiment

Plant
 └── Cultivar

Knowledge
 ├── Plant requirements
 ├── Problems
 ├── Relationships
 ├── Timing
 ├── Sources
 └── Other reference information

Not every entity needs to be implemented as an independent database table in V1.

The important requirement is that the conceptual distinctions remain clear.


---

4. Identity and audit fields

Persistent entities should normally have:

id
created_at
updated_at
status

Where historical or provenance requirements justify it, records may additionally contain:

created_by
modified_by
data_origin
modification_reason

Identifiers must be stable and must not encode user-facing names.

Example:

id:
garden_8f2...

name:
Back Garden

Renaming the garden must not change its identity.


---

5. Garden

A Garden represents the user's overall managed garden.

Minimum conceptual properties:

Garden
- id
- name
- location
- hemisphere
- timezone
- status
- created_at
- updated_at

Additional information may include:

geographic coordinates;

address or locality;

climate context;

preferred units;

user preferences;

notes.


Location information should be sufficiently precise for garden planning without requiring unnecessary personal location exposure.


---

6. Garden location

Location is structured data, not a vocabulary.

Potential properties include:

latitude
longitude
country
region
locality
timezone
hemisphere

Not every field needs to be populated.

The system must distinguish:

known
unknown
estimated

where uncertainty is meaningful.

The application must not fabricate geographic precision.


---

7. Area

An Area represents a meaningful spatial subdivision of the garden.

Examples:

Back Garden
Orchard
Vegetable Garden
North Side
Greenhouse Area

An Area may contain:

growing spaces;

plants;

infrastructure;

other spatial objects.


Areas may be organisational as well as geographic.


---

8. Growing Space

A Growing Space represents a location in which plants may be grown.

Examples:

Bed 1
Bed 2
Raised Bed A
Greenhouse Bench
Pot 4
Orchard Row 2

Conceptual properties:

GrowingSpace
- id
- garden_id
- area_id
- name
- type
- geometry
- dimensions
- status

Optional properties may include:

soil characteristics;

light characteristics;

water characteristics;

drainage;

fertility;

access;

notes.


A Growing Space may change shape or dimensions over time.

Changing its current geometry must not erase historical records associated with it.


---

9. Spatial objects

The garden may contain spatial objects that are not growing spaces.

Examples:

tree
irrigation_line
tap
path
structure
fence
compost_area
root_zone
shade_structure

Spatial objects should use the same general spatial framework as growing spaces where practical.

They may overlap.

The data model must not require every spatial object to belong to exactly one other object.


---

10. Geometry

Geometry is structured spatial data rather than a vocabulary.

Supported V1 geometry should be kept deliberately simple.

Potential geometry types:

point
line
polygon

The exact geometry implementation is an architectural decision.

The conceptual model must support:

position;

size;

shape;

editing;

overlap;

historical association.


Spatial calculations should be derived from geometry rather than manually duplicated wherever possible.


---

11. Spatial history

Spatial objects should retain their identity when their geometry changes.

For example:

Bed 2
2025:
3 m × 1 m

2026:
4 m × 1 m

The application should be able to determine the historical state of the space when necessary.

A geometry change must not rewrite historical planting records as though the old geometry never existed.


---

12. Plant knowledge entity

A Plant represents a canonical plant concept in the application's knowledge base.

Examples:

Tomato
Carrot
Lettuce
Kale
Apple
Basil

A Plant contains general knowledge, not a particular occurrence in the user's garden.

Conceptual properties may include:

Plant
- id
- canonical_name
- scientific_name
- lifecycle
- description
- status

Additional knowledge may be associated through separate records.


---

13. Plant aliases

A Plant may have multiple names.

Examples:

canonical name
common name
regional name
synonym
search alias

Aliases should resolve to the same canonical Plant where appropriate.

The database should not duplicate a Plant merely because users search using different common names.


---

14. Cultivar

A Cultivar represents a specific cultivar associated with a Plant.

Example:

Plant:
Tomato

Cultivar:
Black Krim

A cultivar may have characteristics that differ from the general Plant.

The model must distinguish:

Plant knowledge

from:

Cultivar knowledge

A cultivar is optional.

The user may record:

Tomato
Cultivar: Unknown

without being blocked.


---

15. Plant instance

A Plant Instance represents the user's actual or intended planting.

This is distinct from the Plant knowledge entity.

Example:

Plant:
Tomato

Plant Instance:
Tomato in Bed 2

Conceptual properties:

PlantInstance
- id
- plant_id
- cultivar_id (optional)
- growing_space_id (optional)
- status
- planned_date (optional)
- planted_date (optional)
- removed_date (optional)

Additional information may include:

quantity;

source;

notes;

propagation method;

planting method.



---

16. Plant instance lifecycle

A Plant Instance may represent:

planned
planted
established
harvested
removed
failed
completed

The exact lifecycle should be kept separate from general record status.

A planned plant must not automatically become planted.

A planned planting becomes evidence of planting only when an actual planting event is recorded.


---

17. Plant location

A Plant Instance may be associated with:

a Growing Space;

another spatial object;

a specific point or area within a space;

no location.


Location should remain flexible enough for:

one plant
a group of plants
a row
a bed
a container

The application should not require unnecessary precision.


---

18. Quantity of plants

Plant quantity should be represented separately from the identity of the Plant.

Examples:

10 lettuce plants
1 tomato plant
20 carrot plants

Where individual plant tracking is unnecessary, a Plant Instance may represent a group.

The model should not force the user to create twenty separate records for twenty identical plants.


---

19. Knowledge records

General horticultural knowledge should be stored separately from garden records.

Examples:

Tomatoes prefer high fertility.
Tomatoes are frost-sensitive.
Carrots generally prefer loose soil.

Knowledge records may describe:

requirements;

timing;

relationships;

problems;

interventions;

growth stages;

environmental tolerances;

cultivation methods.


Each knowledge record should retain appropriate provenance.


---

20. Knowledge applicability

Knowledge may apply differently depending on:

geography;

climate;

hemisphere;

season;

growth stage;

growing method;

cultivar;

soil;

environment.


The model should therefore avoid treating every knowledge statement as universally applicable.

Where appropriate, a knowledge record should contain:

applicability
applicability_context
geographic_scope
source
source_relationship
confidence


---

21. Sources and provenance

Knowledge records may reference a Source.

A Source represents the origin of information.

Potential source properties:

Source
- id
- title
- author_or_organisation
- source_type
- location
- publication_date
- access_date
- status

The exact fields may vary by source type.

The model should preserve enough information to explain where important knowledge originated.


---

22. Provenance does not equal confidence

The model must distinguish:

where information came from

from:

how strongly it is supported

For example:

Source:
institutional guidance

Confidence:
moderate

or:

Source:
user observation

Confidence:
high

These are different properties.


---

23. Observation

An Observation records something that was observed in the user's garden.

Examples:

Leaves are yellowing.
Water pools after heavy rain.
Soil feels compacted.
Plant is flowering.
Aphids visible on leaves.

Conceptual properties:

Observation
- id
- date/time
- observer
- context
- observation_type
- structured_values
- notes
- confidence
- location
- related_plant_instance

Only relevant fields should be required for a particular observation type.


---

24. Observation is not diagnosis

An Observation records what was observed.

It must not automatically become:

a disease diagnosis;

a pest identification;

a nutrient deficiency;

a causal explanation.


For example:

Observation:
Leaves are yellowing.

Possible interpretations:
Water stress
Nutrient deficiency
Disease

The interpretations remain separate.


---

25. Structured observations

Observations should support structured fields where useful.

Examples:

Soil observation

moisture
pooling
texture
compaction
visible_organic_matter

Plant observation

growth
flowering
fruiting
damage
symptoms

The application should avoid creating a universal observation form containing every possible field.


---

26. Observation evidence

An observation may include:

structured values;

free-text notes;

photographs;

measurements;

location;

date/time.


Photos and attachments should be references to stored media rather than embedded directly into the conceptual record.


---

27. Measurement

A Measurement records a measured value.

Examples:

pH = 6.7
temperature = 18.4 °C
soil moisture = 32 %
drainage time = 6 hours

Conceptual properties:

Measurement
- id
- property
- value
- unit
- date/time
- method
- source
- location
- confidence

The numeric value and unit remain structured data.


---

28. Measurement versus estimate

The model must preserve the distinction between:

Measured:
pH = 6.7

and:

Estimated:
pH probably slightly acidic

An estimate must never be stored as though it were a measurement.


---

29. Derived information

The system may derive information from observations and measurements.

Example:

Observations:
Water pools after heavy rain.
Water remains for approximately 6 hours.

Derived estimate:
Probably slow drainage.

Confidence:
Low.

Derived information should retain its relationship to the evidence from which it was derived.

The original observations and measurements must remain intact.


---

30. Soil records

Soil information may exist at different spatial levels.

Examples:

garden-wide soil information
area soil information
growing-space soil information
specific observation
specific measurement

The model should avoid forcing all soil information into one permanent property of a Growing Space.

Soil changes over time.


---

31. Soil condition history

A Growing Space may have different soil conditions across time.

Examples:

2025:
pH 6.2

2026:
pH 6.7

or:

2025:
poor drainage

2026:
drainage improved after intervention

Historical measurements and observations must remain associated with their dates and locations.


---

32. Activity

Activity is the core record of something that actually happened in the garden.

Examples:

planting
watering
feeding
pruning
intervention
maintenance

Conceptual properties:

Activity
- id
- activity_type
- date/time
- status
- location
- plant_instance
- quantity
- notes

Activity is intentionally generic.

Domain-specific details should be represented through structured fields or related records rather than creating a separate fundamental event system for every action.


---

33. Planned activity versus actual activity

A plan is not an activity.

Example:

Plan:
Water Bed 2 tomorrow.

Activity:
Watered Bed 2 at 09:15.

The activity is evidence that the action occurred.

The plan is only an intention.

The two may be linked but must remain separate records.


---

34. Repeated activities

Recurring plans may generate planned occurrences.

Each completed occurrence should become an independent actual Activity.

Example:

Plan:
Water every 3 days.

Occurrence 1:
Completed.

Occurrence 2:
Completed.

Occurrence 3:
Skipped.

The recurring plan must not itself be treated as evidence that all occurrences happened.


---

35. Partial completion

A planned action may apply to multiple targets.

Example:

Plan:
Water Beds 1–4.

Actual:
Bed 1 — completed
Bed 2 — completed
Bed 3 — completed
Bed 4 — not completed

The model must support partial completion without falsifying the plan's history.


---

36. Harvest

A Harvest records produce collected from the garden.

Conceptual properties:

Harvest
- id
- date/time
- plant_instance
- location
- quantity
- unit
- size_category
- notes

Weight is optional.

Supported units may include:

count
bunch
basket
container
other

The actual controlled values should be defined in the relevant activity vocabulary.


---

37. Harvest loss

Loss should be recorded separately from harvested produce.

Example:

Harvest:
12 tomatoes

Loss:
3 tomatoes
Cause:
rot

The application must not silently infer exact lost quantities or causes.

An estimated loss must be explicitly identified as estimated.


---

38. Problems

A Problem represents a garden issue or condition that may require attention.

Examples:

aphids
powdery mildew
yellowing leaves
poor establishment
frost damage

Problems are distinct from observations.

An observation may provide evidence for a possible problem.

A Problem may have:

status
severity
confidence
evidence
affected plants
affected spaces


---

39. Problem diagnosis

The system may associate possible diagnoses with observations.

Example:

Observation:
Leaves yellowing.

Possible causes:
Water stress — moderate confidence
Nutrient deficiency — low confidence
Disease — low confidence

A possible diagnosis must not be treated as a confirmed fact unless sufficient evidence exists.

The original observation remains available.


---

40. Intervention

An Intervention records an action intended to address a problem or change a garden condition.

Examples:

applied mulch
removed affected leaves
improved drainage
applied treatment
changed watering practice

Interventions should normally be represented as Activities with relevant structured details rather than creating an entirely separate event model.

If an intervention has additional diagnostic or experimental significance, it may also be linked to a Problem or Experiment.


---

41. Plan

A Plan represents an intended future action or planting decision.

Examples:

Plant tomatoes in Bed 2.
Water Bed 3 tomorrow.
Sow carrots next week.

Conceptual properties:

Plan
- id
- plan_type
- target
- planned_date/range
- status
- priority
- notes

A Plan may originate from:

the user;

a recommendation;

a recurring schedule;

an experiment.



---

42. Plan and recommendation relationship

A Recommendation suggests an action or option.

A Plan represents the user's intention to act.

Example:

Recommendation:
Tomatoes are a good candidate for Bed 2.

User decision:
Accepted.

Plan:
Plant tomatoes in Bed 2 on 15 September.

These must remain distinct.

The user may accept a recommendation without creating a plan.

The user may create a plan without a recommendation.


---

43. Recommendation

A Recommendation represents a system-generated assessment or suggestion.

Conceptual properties:

Recommendation
- id
- created_at
- context
- subject
- status
- confidence
- factors
- reasons
- missing_information
- source_context

A recommendation should be reproducible or explainable from the information available at the time it was generated.


---

44. Recommendation factors

A Recommendation may contain factor results such as:

light: excellent
water: good
soil: good
temperature: marginal
rotation: poor
space: excellent

Only relevant factors should be included.

Each factor may retain:

dimension
result
evidence
confidence
reason


---

45. Recommendation evidence

Where practical, recommendations should identify the information that materially influenced them.

Example:

Light:
Good

Evidence:
Bed 2 receives approximately 7 hours of summer sunlight.

Rotation:
Poor

Evidence:
Solanaceae were grown here last season.

This allows the UI to answer:

> Why?



without exposing the internal rule engine.


---

46. Recommendation uncertainty

A recommendation must be able to distinguish:

suitability

from:

confidence

Example:

Suitability:
Good

Confidence:
Moderate

Reason:
Winter temperature information is incomplete.

High suitability does not imply high confidence.


---

47. Conditional recommendations

A Recommendation may depend on a condition.

Example:

Good candidate if drainage is improved.

The condition should be represented explicitly where practical.

The recommendation should not be reduced to a misleading binary:

yes / no


---

48. Insufficient information

A recommendation may legitimately return:

insufficient_information

when missing information materially prevents a reliable assessment.

The system should identify:

what is missing;

why it matters;

whether the user can continue without it;

what could be done to improve confidence.


Missing information must not automatically be interpreted as unsuitable.


---

49. Recommendation history

Recommendations should be treated as historical assessments rather than permanently changing truths.

If a recommendation changes because garden information changes:

Previous:
Good candidate.

New information:
Poor drainage recorded.

Updated:
Conditional.

The application should retain sufficient history to explain the change when this is useful.


---

50. User decision

A User Decision records the gardener's response to a recommendation or suggested action.

Examples:

accepted
rejected
modified
overridden
deferred
cancelled

A User Decision may contain:

decision_type
date/time
reason
related_recommendation
related_plan
notes

The reason is optional.


---

51. Override

An override is a valid user decision.

Example:

Recommendation:
Do not plant tomatoes here because of rotation risk.

User decision:
Overridden.

Reason:
User wants to conduct an experiment.

The application must not treat an override as an error.

Where useful, it should preserve the override as part of garden history.


---

52. User preferences

Preferences influence recommendations but are not hard constraints unless explicitly defined as such.

Examples:

favourite plants
plants to avoid
low maintenance
water conservation
pollinator priority
food production priority
experimentation

Preferences should be stored separately from general knowledge.

A recommendation affected by a preference should remain explainable.

Example:

Lettuce:
Excellent suitability.

Result:
Excluded by user preference.


---

53. Personal history

The application may derive patterns from the user's own garden history.

Example:

Carrots have shown poor establishment in this bed during
three recorded seasons with poor drainage.

A personal-history finding should identify that it is based on the user's records.

It must not automatically be presented as universal horticultural knowledge.


---

54. Personal learning

Personal learning should be represented as derived information linked to its evidence.

Conceptually:

Personal Finding
- id
- subject
- statement
- evidence_records
- confidence
- created_at

The underlying observations, activities and outcomes must remain accessible.

The application must not silently rewrite general plant knowledge based on personal history.


---

55. Experiment

An Experiment is an optional advanced structure for deliberate comparison or testing.

Conceptual properties:

Experiment
- id
- name
- question
- hypothesis
- start_date
- end_date
- status

An experiment may additionally define:

treatment
control
measurements
observations
activities
outcomes

Experiments should reuse ordinary garden records.

They should not create a parallel activity or observation system.


---

56. Experiment results

An experiment should distinguish:

Observed result

from:

Conclusion

Example:

Observed:
The mulched bed required fewer recorded watering events.

Conclusion:
Mulch may have reduced watering requirements in this garden.

The system must not turn a single experiment into a universal rule automatically.


---

57. Relationships

Relationships between entities should be explicit where they have meaning.

Examples:

Plant → Cultivar
Plant Instance → Plant
Plant Instance → Growing Space
Activity → Plant Instance
Activity → Growing Space
Observation → Plant Instance
Observation → Growing Space
Harvest → Plant Instance
Problem → Plant Instance
Recommendation → Plant / Plant Instance / Growing Space
Plan → Plant / Plant Instance / Growing Space
User Decision → Recommendation / Plan
Experiment → Activities / Observations / Measurements
Knowledge → Plant / Cultivar / Problem / Source

The exact implementation may use foreign keys, join entities or another mechanism.


---

58. Many-to-many relationships

Some relationships naturally require multiple targets.

Examples:

Plant ↔ Plant
Plant ↔ Problem
Plant ↔ Plant
Experiment ↔ Activity
Recommendation ↔ Evidence
Observation ↔ Plant Instance

The model should use explicit relationship records where necessary rather than duplicating data.


---

59. Plant relationships

Plant relationships may include:

beneficial
compatible
incompatible
companion
rotation_related

The exact controlled relationship types belong in the plant/planning vocabularies.

Relationships are knowledge records, not user assumptions unless explicitly recorded as such.


---

60. Rotation

Rotation information should be represented using stable groups or relationships rather than hard-coding plant names into recommendation logic.

Example:

Tomato
    ↓
Solanaceae rotation group

The application can then determine whether related crops were recently grown in a space.

Historical planting records must remain intact even if rotation classifications later change.


---

61. Evidence references

Derived information and recommendations should be able to reference their supporting records.

For example:

Recommendation
    ↓
Evidence
    ├── Plant knowledge
    ├── Growing Space condition
    ├── Soil measurement
    ├── Previous planting
    └── User preference

This supports explainability without duplicating all evidence inside the recommendation.


---

62. Provenance of user information

User-created information should retain its origin where useful.

Examples:

user_observed
user_measured
user_imported
system_derived
system_inferred

The system must not imply that a system-derived value was directly measured by the user.


---

63. Historical preservation

Historical records are a first-class requirement.

The model must preserve, where relevant:

previous planting;

previous geometry;

observations;

measurements;

activities;

harvests;

losses;

problems;

interventions;

experiments;

recommendations;

user decisions;

outcomes.


Current state may be derived from historical records.

It should not replace them.


---

64. Current state versus history

The application may maintain convenient current-state information for performance.

However, current state must not become the only source of truth where historical information matters.

For example:

Current:
Bed 2 contains tomatoes.

History:
2024 — lettuce
2025 — tomatoes
2026 — tomatoes

The current state alone cannot answer rotation or historical questions.


---

65. Corrections to historical data

Ordinary corrections are permitted.

For example:

Harvest:
5 tomatoes

Corrected:
8 tomatoes

Where the correction materially affects historical interpretation, the original record or modification history should remain recoverable.

A historical measurement should not be silently rewritten when doing so would obscure what was previously recorded.


---

66. Deletion and archival

Important garden history should generally not be physically deleted.

Preferred approaches are:

archive
inactive
superseded
corrected

Permanent deletion should be reserved for cases where it is genuinely appropriate, such as:

accidental duplicate records;

user-requested removal where no preservation requirement applies;

invalid data.


Bulk or destructive deletion must be handled carefully.


---

67. Unknown information

Unknown information is valid data.

Examples:

cultivar:
unknown

soil pH:
unknown

winter sunlight:
unknown

The model must not require users to invent values merely to satisfy a schema.

Where appropriate, use the relevant information-state value:

unknown
not_recorded
not_measured
not_applicable


---

68. Progressive precision

The model must allow information to become more precise over time.

Example:

Initial:
Drainage unknown.

Later:
Observation — water pools after heavy rain.

Later:
Estimated drainage — probably slow.

Later:
Measurement — approximately 6 hours.

Later:
Repeated measurements — typically 5–7 hours.

The new information must supplement rather than erase the earlier evidence.


---

69. Inheritance and derived conditions

The application may derive conditions from broader contexts.

For example:

Garden condition
        ↓
Area condition
        ↓
Growing Space condition
        ↓
Plant suitability

However, derived values must not overwrite more specific user information.

A growing space may have its own observed condition that differs from a garden-wide estimate.


---

70. Conflicting information

The model must allow conflicting records.

Example:

General knowledge:
Tomatoes require high fertility.

User observation:
Tomatoes performed poorly despite high measured fertility.

The system should retain both.

It should not automatically decide that one record is false.

Recommendations may consider both general knowledge and personal history.


---

71. Confidence propagation

Confidence should not be blindly copied through the model.

For example:

Observation confidence:
High

Inference confidence:
Moderate

Recommendation confidence:
Low

Each stage may have different confidence because additional uncertainty can be introduced.


---

72. Recommendation reproducibility

Important recommendations should retain enough context to determine:

what was being evaluated;

when;

which garden/space;

relevant plant or cultivar;

important conditions;

relevant preferences;

rules or knowledge used;

resulting factors;

confidence;

reasons.


This does not require storing a complete copy of the entire database inside each recommendation.

References to supporting records are preferred.


---

73. Versioning of knowledge and rules

Knowledge and recommendation rules may change over time.

Where a recommendation's historical meaning matters, the application should retain sufficient information to identify the knowledge/rule version used.

This prevents a historical recommendation from becoming impossible to explain after future rule changes.


---

74. Import/export identity

Imported and exported records should use stable identifiers and canonical vocabulary values.

User-facing labels must not become the primary identifiers.

Imports should be able to preserve:

entity IDs where appropriate;

relationships;

dates;

vocabulary identifiers;

provenance;

historical records;

measurements;

confidence;

source information.



---

75. Data validation

Validation should distinguish between:

required
optional
unknown
not_applicable
invalid

A missing optional value is not an error.

An unknown value is not an invalid value.

Validation must not force precision that the user does not possess.


---

76. Offline-first data model

Core garden data must be locally usable without network access.

The data model must therefore avoid requiring a live external service for:

viewing the garden;

viewing plants;

recording activities;

recording observations;

viewing history;

basic planning;

saved recommendations based on local data.


Future network-dependent capabilities must be layered on top rather than becoming prerequisites for ordinary garden operation.


---

77. Sync and future cloud functionality

V1 does not require mandatory cloud synchronisation.

If synchronisation is introduced later, entities should have stable identifiers and sufficient metadata to support conflict handling.

The data model should not assume that a cloud account is the owner of the garden.

The user's local garden remains the primary conceptual object.


---

78. User ownership and privacy

Garden records belong to the user's garden.

The model should distinguish:

private garden data

from:

general application knowledge

and, in future:

explicitly contributed knowledge

Garden data must not automatically enter community knowledge systems.


---

79. Localisation

Stored data should use canonical identifiers.

Translated labels belong in localisation resources.

The data model must not store language-specific vocabulary as the primary meaning of a record.

This allows:

English
Afrikaans
future languages

to use the same underlying garden data.


---

80. What should not become an entity unnecessarily

The model should avoid creating separate entities merely because the UI has a screen or concept.

For example:

Quick Add
Home
Inbox
Plant Library
My Plants

are primarily UX concepts.

They do not automatically require database entities.

Similarly, a recommendation reason may be structured data within a Recommendation rather than a standalone entity unless future requirements justify one.


---

81. What should remain separate

The following distinctions are important enough that they should not be collapsed:

Plant
≠
Plant Instance

Plant Instance
≠
Planting Activity

Plan
≠
Activity

Observation
≠
Diagnosis

Measurement
≠
Estimate

Knowledge
≠
Garden Record

Recommendation
≠
User Decision

User Decision
≠
Activity

Harvest
≠
Harvest Loss

General Knowledge
≠
Personal Finding

Current State
≠
History

These distinctions protect the integrity and explainability of the application.


---

82. Simplification rule

The data model should prefer:

> a small number of stable primitives connected clearly



over:

> a large number of specialised entities representing every possible gardening concept.



The most important V1 primitives are:

Garden
Area
Growing Space
Spatial Object
Plant
Cultivar
Plant Instance
Activity
Observation
Measurement
Problem
Harvest
Plan
Recommendation
User Decision
Experiment
Source

Additional knowledge and relationships should build upon these primitives.


---

83. V1 implementation priority

The following should be considered core V1 data capabilities.

Essential

Garden
Growing Space
Plant
Cultivar
Plant Instance
Activity
Observation
Measurement
Harvest
Plan
Recommendation
User Decision

Important supporting capabilities

Area
Spatial Object
Problem
Source
Provenance
Personal history

Advanced V1 capability

Experiment

Experiments should remain optional and must not complicate ordinary garden use.


---

84. V1 data-model non-goals

The V1 data model does not need to fully support:

social networking;

mandatory user accounts;

community reputation systems;

automated global knowledge aggregation;

professional GIS;

complete weather history;

sophisticated statistical modelling;

automated disease diagnosis;

fully automated garden design;

complex chemical treatment modelling.


The model should remain extensible enough for these possibilities without making them prerequisites for V1.


---

85. Conceptual relationship summary

The core relationships can be understood as:

GARDEN
  │
  ├── contains ──> AREAS
  │                  │
  │                  └── contains ──> GROWING SPACES
  │
  ├── contains ──> SPATIAL OBJECTS
  │
  ├── contains ──> PLANT INSTANCES
  │                  │
  │                  ├── refers to ──> PLANT
  │                  │                  │
  │                  │                  └── may have ──> CULTIVAR
  │                  │
  │                  └── located in ──> GROWING SPACE
  │
  ├── records ──> ACTIVITIES
  ├── records ──> OBSERVATIONS
  ├── records ──> MEASUREMENTS
  ├── records ──> HARVESTS
  ├── records ──> PROBLEMS
  ├── contains ──> PLANS
  ├── receives ──> RECOMMENDATIONS
  ├── records ──> USER DECISIONS
  └── may contain ──> EXPERIMENTS


PLANT / CULTIVAR
  │
  └── has ──> GENERAL KNOWLEDGE
                 │
                 ├── supported by ──> SOURCES
                 ├── has ──> PROVENANCE
                 └── has ──> APPLICABILITY


RECOMMENDATION
  │
  ├── evaluates ──> SUBJECT
  ├── uses ──> EVIDENCE
  ├── produces ──> FACTORS
  ├── explains through ──> REASONS
  └── may lead to ──> USER DECISION
                            │
                            └── may lead to ──> PLAN
                                                     │
                                                     └── may result in ──> ACTIVITY


---

86. Final design principles

The data model should always preserve the following distinctions:

1. What the garden is.


2. What the gardener recorded.


3. What was measured.


4. What the application inferred.


5. What the application recommended.


6. What the gardener decided.


7. What actually happened.


8. What happened as a result.


9. What general knowledge supported the recommendation.


10. How certain each piece of information is.


11. Where important information came from.


12. What the garden looked like at the relevant point in time.



The model should remain deliberately simple at its core.

> Store the garden, its history, the evidence about it, and the decisions made about it. Keep knowledge, inference and recommendation separate from what actually happened.



This provides the foundation for a garden manager that can become increasingly intelligent without sacrificing data integrity, explainability, offline operation or the gardener's control.