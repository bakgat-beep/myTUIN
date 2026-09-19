Worked for 33s

Garden Planner & Manager — V1 Database Schema

Document: docs/V1_DATABASE_SCHEMA.md
Version: 0.2
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the concrete persistence model for Version 1 of the Garden Planner & Manager.

It translates the conceptual model defined in:

docs/DATA_MODEL.md

into a practical V1 database structure.

The conceptual data model remains authoritative for meaning and relationships.

This document defines:

what needs persistent storage;

the principal records and relationships;

which concepts should remain separate;

which concepts can share a generic record;

historical-data requirements;

provenance requirements;

indexing and integrity principles;

what is intentionally deferred from V1.


It does not prescribe a particular database engine, ORM or programming language.


---

2. Relationship to the conceptual data model

The documents have different responsibilities.

DATA_MODEL.md

Defines:

conceptual entities;

semantic distinctions;

relationships;

historical principles;

information states;

provenance;

uncertainty.


V1_DATABASE_SCHEMA.md

Defines:

practical V1 persistence structures;

required and optional fields;

relationships;

indexes;

constraints;

implementation simplifications.


The database schema must not introduce conceptual distinctions that contradict the data model.

Where the conceptual model permits several implementation approaches, V1 should prefer the simplest approach that preserves the required semantics.


---

3. V1 database principles

The database must prioritise:

1. data integrity;


2. historical preservation;


3. simple relationships;


4. offline operation;


5. fast local recording;


6. explainable derived information;


7. import/export compatibility;


8. future extensibility;


9. minimal unnecessary complexity.



The database should not attempt to implement future functionality merely because the conceptual model allows it.


---

4. Persistence strategy

V1 should use a local-first relational database.

The exact database technology is defined by the technical architecture rather than this document.

The database should support:

stable IDs;

foreign keys;

transactions;

indexes;

date/time values;

numeric measurements;

structured JSON or equivalent fields where genuinely useful;

offline operation.


The application may maintain derived or cached values for performance, but historical source records remain authoritative.


---

5. Stable identifiers

Every persistent entity must have a stable unique identifier.

IDs must not depend on:

display names;

translations;

database row order;

screen position;

geographic coordinates.


IDs should normally be generated application-side so that records can be created offline.

Example conceptual ID:

01J...

The exact ID format is an implementation decision.

Renaming or translating an entity must never change its ID.


---

6. Common fields

Persistent entities should normally include:

id

created_at

updated_at


Where relevant, records may additionally contain:

status

data_origin

modification_reason


User-owned historical records should normally also retain:

created_by where multi-user functionality eventually requires it.


V1 does not require a full user-account system.


---

7. Dates and times

Dates and timestamps must be stored in a form that preserves the actual recorded time.

Where an event has a meaningful time:

store a timestamp.


Where only a calendar date is known:

store a date without inventing a time.


The application must not fabricate precision.

Examples:

Known:

2026-08-15 09:30

Known only:

2026-08-15

Unknown time must remain unknown.


---

8. Status and deletion

Important garden records should not normally be physically deleted.

Where appropriate, records should use states such as:

active

inactive

archived

deprecated

superseded


A separate deletion mechanism may exist for genuinely removable records.

Deletion must not silently destroy historical information or break references from surviving records.

Knowledge records should generally be deprecated rather than deleted when historical recommendations or imported data still depend on them.


---

9. Canonical data language

Stored vocabulary values use the canonical English identifiers defined in the vocabulary documents.

Example:

perennial

not:

Perennial

and not a translated UI label.

The database must never depend on user-interface wording.


---

10. V1 entity overview

The principal V1 persistence structures are:

Garden structure

Garden

Area

GrowingSpace

SpatialObject


Plant knowledge

Plant

PlantAlias

Cultivar

PlantKnowledge

PlantRelationship

PlantProblemRelationship


Personal plants

PlantInstance


Garden records

Activity

Observation

Measurement

Problem

Harvest

HarvestLoss

Plan

UserDecision


Soil

SoilRecord


Recommendations

Recommendation

RecommendationFactor

RecommendationEvidence


Experiments

Experiment


Inbox

InboxItem


Provenance

Source


Not every conceptual distinction requires a separate table.


---

11. Garden

Entity

Garden

Represents the user's overall managed garden.

Required fields

id

name

created_at

updated_at

status


Recommended fields

description

country_code

region

locality

latitude

longitude

timezone

hemisphere


Notes

Location is structured data, not a vocabulary.

Exact coordinates are private garden information.

The database must not assume that location information can later be shared publicly.


---

12. Area

Entity

Area

Represents a meaningful subdivision or organisational area within a garden.

Examples:

Vegetable Garden

Orchard

Greenhouse Area

North Side

Back Garden


Required fields

id

garden_id

name

area_type

created_at

updated_at


Optional fields

description

geometry

status


An Area may overlap other spatial objects.


---

13. GrowingSpace

Entity

GrowingSpace

Represents a location in which plants may be grown.

Examples:

Raised Bed 1

Pot 4

Greenhouse Bench

Orchard Row

Border


Required fields

id

garden_id

name

space_type

created_at

updated_at

status


Spatial fields

geometry_type

geometry_data


V1 geometry should support:

point

line

polygon


The exact geometry representation is an implementation decision.

Optional physical fields

length

width

height

diameter

area

volume


Only applicable dimensions should be populated.

Optional fields

area_id

description

notes


Environmental information should normally be represented through related observations, measurements or soil records rather than permanently embedding every condition in the GrowingSpace record.


---

14. SpatialObject

Entity

SpatialObject

Represents a physical or conceptual spatial object that is not itself a GrowingSpace.

Examples:

tree;

irrigation line;

tap;

path;

fence;

compost area;

root zone;

shade structure.


Required fields

id

garden_id

object_type

geometry_type

geometry_data

created_at

updated_at

status


Optional fields

name

description

area_id

notes


Spatial objects may overlap.

The schema must not require every spatial object to belong to exactly one parent object.


---

15. Spatial history

Current geometry must not erase historical meaning.

Where geometry changes materially, the implementation should preserve enough information to reconstruct historical state.

V1 may implement this using:

geometry history records;

effective dates;

versioned geometry;

or another equivalent mechanism.


The implementation must support questions such as:

> What did this growing space look like when the 2025 crop was planted?



The exact mechanism is an implementation decision.


---

16. Plant

Entity

Plant

Represents a canonical plant knowledge entity.

Examples:

Tomato

Carrot

Lettuce

Apple

Basil


Required fields

id

canonical_name

created_at

updated_at

status


Optional fields

scientific_name

genus

species

family

lifecycle

description


Plant identity is reference data.

A Plant record does not represent something growing in the user's garden.


---

17. PlantAlias

Entity

PlantAlias

Provides searchable alternative names for a Plant.

Fields

id

plant_id

alias

alias_type

language


Examples may include:

common names;

regional names;

synonyms;

plural forms;

search terms.


Aliases must resolve to the appropriate canonical Plant rather than creating duplicate Plant records.


---

18. Cultivar

Entity

Cultivar

Represents a named cultivar associated with a Plant.

Required fields

id

plant_id

name

created_at

updated_at

status


Optional fields

description

notes


Cultivar-specific requirements and guidance should be represented through knowledge records where practical rather than continually expanding the Cultivar table.

A user may record an unknown cultivar.


---

19. PlantInstance

Entity

PlantInstance

Represents an actual or intended occurrence of a Plant in the user's garden.

Example:

Plant:

Tomato

PlantInstance:

Tomato in Bed 2

Required fields

id

garden_id

plant_id

created_at

updated_at

status


Optional fields

cultivar_id

growing_space_id

spatial_object_id

name

quantity

planned_date

planted_date

expected_end_date

removed_date

notes


A PlantInstance may represent:

one plant;

a group of identical plants;

a row;

a planting batch.


The user must not be forced to create one record per plant when group tracking is sufficient.


---

20. PlantInstance lifecycle

PlantInstance lifecycle is separate from generic record status.

Potential states include:

planned

planted

established

harvested

removed

failed

dormant

completed


The exact controlled values belong to the Plant vocabulary.

A planned PlantInstance must not automatically become evidence that planting occurred.

Actual planting is recorded through an Activity.


---

21. Knowledge records

General horticultural knowledge must remain separate from personal garden records.

V1 knowledge may include:

plant requirements;

growing guidance;

planting timing;

relationships;

problem relationships;

cultivar-specific information;

environmental tolerances;

cultivation guidance.


A lightweight generic knowledge structure may be used where appropriate rather than creating a separate table for every type of horticultural fact.

Conceptual fields may include

id

subject_type

subject_id

knowledge_type

value

unit

growth_stage

geographic_scope

applicability

applicability_context

source_id

source_relationship

confidence

status


The implementation may split frequently queried knowledge types into specialised tables where this materially improves validation or performance.

The conceptual meaning must remain consistent.


---

22. Plant relationships

Plant-to-plant knowledge relationships should be represented explicitly.

Relationship structure

Potential fields:

id

plant_id

related_plant_id

relationship_type

strength

context

reason

source_id

confidence

status


Examples:

companion;

competitive;

rotation-related;

succession;

supports.


Relationship values are controlled by the appropriate vocabulary.


---

23. Plant-problem relationships

Knowledge about plant susceptibility, resistance or association with problems should be represented separately from user observations.

Potential fields:

id

plant_id

cultivar_id

problem_id

relationship_type

growth_stage

severity

reason

source_id

confidence

status


Examples:

susceptible_to;

resistant_to;

associated_with.


This is knowledge about a plant.

It does not mean that the user's plant currently has the problem.


---

24. Source

Entity

Source

Represents the origin of knowledge or imported information.

Required fields

id

source_type

status

created_at


Optional fields

title

author_or_organisation

publication_date

access_date

url

geographic_scope

hemisphere

context

notes


URLs are optional.

The application must remain useful offline.

Source type and source status use the provenance vocabularies.


---

25. Activity

Entity

Activity

Activity is the core record of something that actually happened in the garden.

Examples:

planting;

watering;

feeding;

pruning;

soil amendment;

intervention;

maintenance;

other garden actions.


Required fields

id

garden_id

activity_type

occurred_at

created_at


Optional fields

area_id

growing_space_id

spatial_object_id

plant_instance_id

quantity

unit

notes

data_origin

status


Activity details may be stored in structured fields or related records where necessary.

The database should not create a separate fundamental event table for every possible gardening action.


---

26. Planting

Planting is an Activity.

A planting Activity may additionally record:

planting method;

quantity;

depth;

spacing;

propagation source;

planting context.


This replaces the earlier requirement for a separate PlantingEvent entity.

The conceptual distinction remains:

Plant:

what the plant is.

PlantInstance:

the user's planting.

Activity:

what actually happened.

This is simpler and avoids duplicating event systems.


---

27. Planned versus actual activity

A Plan is not an Activity.

Example:

Plan:

Water Bed 2 tomorrow.

Activity:

Watered Bed 2 at 09:15.

Only the Activity is evidence that watering occurred.

A Plan may be linked to one or more Activities.

Completing a Plan must not automatically create an Activity unless the user explicitly confirms that the activity occurred.


---

28. Repeated activities

Recurring plans may generate planned occurrences.

Each completed occurrence should result in its own Activity.

Example:

Plan:

Water every 3 days.

Occurrences:

completed;

completed;

skipped.


The recurring plan itself is not evidence that all occurrences happened.


---

29. Partial completion

A Plan may have multiple targets.

Example:

Water Beds 1–4.

The database must support:

Beds 1–3 completed;

Bed 4 not completed.


This may be represented through plan-target records or equivalent structured target data.

The implementation must not falsely mark all targets complete when only some were completed.


---

30. Observation

Entity

Observation

Represents something observed in the user's garden.

Required fields

id

garden_id

observed_at

observation_type

confidence

created_at


Optional fields

area_id

growing_space_id

spatial_object_id

plant_instance_id

problem_id

notes

structured_values


Potential observation types include:

plant;

soil;

water;

pest;

disease;

damage;

weather;

growth;

flowering;

fruiting;

general.


Only fields relevant to the selected observation type should be presented to the user.


---

31. Observation is not diagnosis

An Observation must not automatically become a diagnosis.

Example:

Observation:

Leaves are yellowing.

Possible interpretations:

water stress;

nutrient deficiency;

disease.


Those interpretations are separate derived information.

The original observation must remain intact.


---

32. Measurement

Entity

Measurement

Represents a measured value.

Required fields

id

garden_id

property

value

unit

measured_at

confidence

created_at


Optional fields

area_id

growing_space_id

spatial_object_id

plant_instance_id

method

source

notes


Examples:

pH = 6.7;

temperature = 18.4 °C;

sunlight = 6.5 hours;

drainage time = 6 hours.


Numeric values must remain numeric.


---

33. Measurement versus estimate

The database must distinguish:

Measured:

pH = 6.7

from:

Estimated:

soil is probably slightly acidic.

An estimate must never be stored as a measured value.

Derived estimates should reference the observations and/or measurements from which they were derived.


---

34. SoilRecord

Entity

SoilRecord

Represents the garden's recorded understanding of soil at a particular spatial context.

A SoilRecord may apply to:

the garden;

an Area;

a GrowingSpace.


Required fields

id

garden_id

status

created_at

updated_at


Optional fields

area_id

growing_space_id

name

notes


Soil characteristics should normally be represented through Observations and Measurements rather than treated as permanent immutable properties.

A SoilRecord may also contain cached current summaries for performance.

Those summaries must remain derivable from the underlying historical records.


---

35. Soil history

Soil information is time-dependent.

Examples:

2025:

pH = 6.2

2026:

pH = 6.7

or:

2025:

poor drainage

2026:

drainage improved.

Historical Observations and Measurements must retain their dates and locations.

Updating a current soil summary must not erase previous evidence.


---

36. Problems

Entity

Problem

Represents a problem or condition affecting the user's garden, or a problem identified by the application as a possible issue.

Examples:

aphids;

powdery mildew;

poor establishment;

frost damage;

yellowing leaves.


Required fields

id

garden_id

name

problem_type

status

created_at

updated_at


Optional fields

severity

confidence

description

area_id

growing_space_id

plant_instance_id

notes


A Problem may be linked to one or more Observations.

The existence of a Problem record does not automatically mean that a diagnosis is confirmed.


---

37. Intervention

Interventions are recorded as Activities.

Examples:

applied mulch;

improved drainage;

adjusted watering;

removed affected material;

applied a treatment;

changed spacing.


An Activity with:

activity_type = intervention

may additionally reference:

a Problem;

a purpose;

structured intervention details.


A separate fundamental Intervention event entity is not required for V1.

This keeps interventions within the same actual-event system as other garden actions.


---

38. Harvest

Entity

Harvest

Represents produce successfully collected from the garden.

A Harvest must be linked to an actual Activity.

Required fields

id

activity_id

garden_id

date

quantity

unit


Optional fields

plant_instance_id

growing_space_id

size_category

quality

notes


Supported units may include:

count;

bunch;

basket;

container;

other.


Weight is optional.

Counting must be a first-class use case.


---

39. Harvest loss

Entity

HarvestLoss

Represents produce that was lost rather than successfully harvested.

Required fields

id

garden_id

date

quantity

unit


Optional fields

plant_instance_id

growing_space_id

cause

severity

notes


Loss must remain separate from successful harvest.

The application must not infer an exact lost quantity unless the user records or deliberately estimates it.


---

40. Plan

Entity

Plan

Represents an intended future action or planting decision.

Required fields

id

garden_id

plan_type

status

created_at

updated_at


Optional fields

plant_id

cultivar_id

plant_instance_id

growing_space_id

planned_start

planned_end

quantity

priority

notes


Potential statuses include:

idea;

planned;

scheduled;

in_progress;

partially_completed;

completed;

skipped;

cancelled;

expired;

superseded.


The canonical values belong to the planning vocabulary.

A Plan represents intention, not evidence.


---

41. Plan targets

A plan may target:

a Plant;

a PlantInstance;

a GrowingSpace;

an Area;

another relevant garden object.


Where a plan has multiple targets, the implementation should use a relationship table rather than duplicating the Plan.

This supports partial completion and avoids forcing every plan into a single-target structure.


---

42. Recommendation

Entity

Recommendation

Represents a system-generated assessment or suggestion.

Required fields

id

garden_id

created_at

subject_type

subject_id

recommendation_type

status

confidence


Optional fields

overall_result

expires_at

context

notes


A Recommendation is a historical assessment.

It is not an authoritative garden fact.


---

43. RecommendationFactor

Entity

RecommendationFactor

Represents an individual dimension contributing to a Recommendation.

Required fields

id

recommendation_id

dimension

result

confidence


Optional fields

weight

reason

notes


Examples:

Light:

excellent

Water:

good

Temperature:

marginal

Rotation:

poor

Only relevant dimensions should be stored.


---

44. RecommendationEvidence

Entity

RecommendationEvidence

Links a Recommendation to the records or knowledge that materially influenced it.

Fields

id

recommendation_id

source_type

source_id

role

notes


Evidence may refer to:

Plant knowledge;

GrowingSpace conditions;

Soil observations;

Measurements;

previous PlantInstances;

Activities;

user preferences;

Problems;

other relevant records.


This avoids copying all evidence into the Recommendation itself.


---

45. Recommendation history

Recommendations should be treated as historical assessments.

Example:

Previous:

Good candidate.

New information:

Poor drainage recorded.

New recommendation:

Conditional.

The database should retain historical recommendations where they are important for:

explainability;

user decisions;

auditing;

personal learning;

understanding changes.


A current recommendation may be cached for performance.

The cached version must not become the only record.


---

46. UserDecision

Entity

UserDecision

Represents the gardener's response to a recommendation or proposed action.

Required fields

id

garden_id

decision_type

decided_at

created_at


Optional fields

recommendation_id

plan_id

reason

notes


Decision types include:

accepted;

rejected;

modified;

overridden;

deferred;

cancelled.


A UserDecision does not prove that the resulting activity occurred.


---

47. User override

An override is a legitimate UserDecision.

Example:

Recommendation:

Do not plant tomatoes here because of rotation risk.

Decision:

Overridden.

Reason:

User wants to conduct an experiment.

The application must not treat an override as an error.

Where useful, the decision should remain part of garden history.


---

48. User preferences

Preferences should be stored separately from horticultural knowledge.

Potential preference data includes:

favourite plants;

plants to avoid;

food production priority;

pollinator priority;

low-maintenance preference;

water conservation;

experimentation;

space utilisation.


Preferences may influence Recommendations.

A preference should not silently delete otherwise relevant recommendation results.

Example:

Lettuce:

Excellent suitability.

User preference:

Avoid.

Result:

Excluded by preference.


---

49. Experiment

Entity

Experiment

Represents an optional advanced structure for deliberate comparison or testing.

Required fields

id

garden_id

name

question

status

created_at


Optional fields

hypothesis

start_date

end_date

notes


An Experiment may link to:

Activities;

Observations;

Measurements;

Plans;

Recommendations.


Experiments must reuse ordinary garden records.

They must not create a separate parallel activity or observation system.


---

50. Experiment results

V1 does not require a separate Outcome entity.

Experiment results can be represented using:

Observations;

Measurements;

Activities;

derived findings;

experiment notes/conclusions.


For example:

Observed result:

The mulched bed required fewer recorded watering events.

Conclusion:

Mulch may have reduced watering requirements in this garden.

The observation and conclusion must remain distinguishable.

A single experiment must not automatically become universal horticultural knowledge.


---

51. Personal findings

V1 may represent derived personal patterns without creating a complex learning system.

A personal finding should retain:

subject;

statement;

supporting records;

confidence;

creation date.


The implementation may use a lightweight derived-record structure or generate findings dynamically.

The underlying garden records remain authoritative.

Personal findings must be clearly identified as being based on the user's own garden history.


---

52. InboxItem

Entity

InboxItem

The Inbox is a user-facing prioritised work queue.

An InboxItem may represent:

action;

warning;

observation request;

information request;

experiment;

knowledge.


Required fields

id

garden_id

item_type

priority

status

created_at

updated_at


Optional fields

title

description

related_entity_type

related_entity_id

due_at

muted_until


Inbox items may be generated from:

Plans;

Recommendations;

Problems;

observations requiring follow-up;

experiments.


Inbox state is user-facing workflow state, not core garden evidence.

Completing or dismissing an InboxItem must not alter the underlying historical record unless the user explicitly performs that action.


---

53. Mute and dismiss

Mute and dismiss are separate states.

Mute means:

> Do not bother me about this for now.



Dismiss means:

> I have dealt with or otherwise resolved this item.



A dismissed InboxItem must not automatically mean:

> Never show this information again.



Muted information should remain reviewable.


---

54. Relationships between major entities

The principal V1 relationships are:

Garden

→ Areas

→ Growing Spaces

→ Spatial Objects

→ Plant Instances

→ Activities

→ Observations

→ Measurements

→ Problems

→ Harvests

→ Plans

→ Recommendations

→ User Decisions

→ Experiments

Plant knowledge:

Plant

→ Cultivars

→ Knowledge

→ Plant Relationships

→ Plant-Problem Relationships

Personal plants:

Plant

→ PlantInstance

PlantInstance

→ GrowingSpace

PlantInstance

→ Activities

PlantInstance

→ Observations

PlantInstance

→ Harvests

Planning:

Recommendation

→ Recommendation Factors

Recommendation

→ Recommendation Evidence

Recommendation

→ User Decision

User Decision

→ Plan

Plan

→ Activity

Soil:

SoilRecord

→ Observations

SoilRecord

→ Measurements

Problems:

Problem

→ Observations

Problem

→ Activities


---

55. Many-to-many relationships

Where a relationship naturally involves multiple records, use explicit relationship structures.

Examples include:

Plant ↔ Plant;

Plant ↔ Problem;

Recommendation ↔ Evidence;

Experiment ↔ Activity;

Experiment ↔ Observation;

Plan ↔ target;

PlantInstance ↔ spatial context where necessary.


Do not duplicate complete records merely to represent relationships.


---

56. Evidence references

Derived records should be able to reference their supporting evidence.

Example:

Recommendation

→ Plant requirement

→ GrowingSpace observation

→ Soil measurement

→ Previous PlantInstance

→ User preference

This enables explainability without duplicating the underlying information.


---

57. Provenance

User information and derived information should retain origin where meaningful.

Examples:

user_created

user_observed

user_measured

user_imported

system_derived

system_inferred

curated

external_dataset


The database must not imply that a system-derived value was directly measured by the user.

Detailed provenance vocabularies are defined separately.


---

58. Information state

Fields that may legitimately be unknown must support explicit information states.

Examples:

unknown;

not_recorded;

not_measured;

not_applicable.


Unknown must not be represented as:

zero;

empty string;

false;

arbitrary default value.


Where a database column uses NULL, the application must still know the semantic reason where that distinction matters.


---

59. Confidence

Confidence is contextual.

It may apply separately to:

observations;

measurements;

knowledge;

inferences;

recommendations;

personal findings.


A high-confidence observation does not automatically produce a high-confidence inference.

Confidence values are defined in the Core vocabulary.


---

60. Historical preservation

Historical records must remain available where they materially describe the garden's past.

This includes:

previous plantings;

PlantInstances;

geometry changes;

Activities;

Observations;

Measurements;

Harvests;

Harvest losses;

Problems;

Plans;

Recommendations;

User decisions;

Experiments;

personal findings.


Current state may be derived from historical records.

It must not replace them.


---

61. Current state and cached data

For performance, the application may maintain convenient current-state values.

Examples:

current plants in a GrowingSpace;

current soil summary;

current open InboxItems;

current recommendation.


These are derived or cached representations.

Where historical accuracy matters, the underlying historical records remain authoritative.

A cache must be rebuildable.


---

62. Historical corrections

Ordinary corrections are permitted.

Example:

Recorded harvest:

5 tomatoes.

Corrected:

8 tomatoes.

Where a correction materially affects historical interpretation, the original value or modification history should remain recoverable.

The implementation may use:

audit records;

revision history;

append-only corrections;

another equivalent mechanism.


The user should not be forced to manage technical versioning manually.


---

63. Geometry history

Geometry changes should preserve historical meaning.

Example:

Bed 2

2025:

3 m × 1 m

2026:

4 m × 1 m

Historical planting records remain associated with the same GrowingSpace identity.

The application must not retroactively pretend that the 2025 bed was always 4 m × 1 m.


---

64. Conflicting information

The database must permit conflicting information.

Example:

General knowledge:

Tomatoes prefer high fertility.

Personal observation:

Tomatoes performed poorly despite high measured fertility.

Both records remain valid records of what was known or observed.

The recommendation system may consider both.

The database must not automatically delete or overwrite one because the other exists.


---

65. Derived information

Derived information must retain its relationship to its supporting evidence where practical.

Example:

Observations:

Water pools after heavy rain.

Water remains for approximately 6 hours.

Derived estimate:

Probably slow drainage.

Confidence:

Low.

The original observations remain authoritative evidence.

Derived information can become stale when its evidence changes.

The implementation should therefore be able to invalidate or regenerate derived records.


---

66. Recommendation reproducibility

Important Recommendations should retain enough context to determine:

what was evaluated;

when it was evaluated;

which garden and space were involved;

which plant or cultivar was evaluated;

relevant conditions;

relevant preferences;

applicable rules or knowledge;

factor results;

confidence;

reasons;

supporting evidence.


The database does not need to store a complete copy of the entire garden inside each Recommendation.


---

67. Referential integrity

Foreign-key relationships should be enforced where practical.

Examples:

a PlantInstance must reference an existing Plant;

a Cultivar must reference an existing Plant;

an Activity must reference an existing Garden;

a Harvest must reference an existing Activity;

a RecommendationFactor must reference an existing Recommendation;

a UserDecision must reference an existing Recommendation or Plan where one is specified.


Deletion rules must protect historical records.

Where a referenced record is archived or deprecated, existing references should remain valid.


---

68. Indexing principles

Indexes should support the application's most common operations.

At minimum, consider indexes for:

Garden ownership

garden_id


Spatial lookup

garden_id

area_id

growing_space_id


Plant lookup

plant_id

cultivar_id


Historical queries

occurred_at

observed_at

measured_at

date


combined with relevant garden or location IDs.

Planning

status

planned_start

planned_end


Recommendations

garden_id

subject_type

subject_id

created_at


Inbox

garden_id

status

priority

due_at


Indexes should be added based on actual query patterns rather than indiscriminately indexing every field.


---

69. Transactions

Operations that create related records should be atomic.

Examples:

Recording a planting

Creating:

PlantInstance;

planting Activity;

relevant Plan completion/update


should either complete consistently or fail without leaving contradictory partial data.

Recording a harvest

Creating:

Activity;

Harvest


should occur within a transaction.

Import

Validation should occur before committing imported records.


---

70. Offline-first requirements

All core personal garden records must be creatable and readable offline.

This includes:

Garden;

Areas;

GrowingSpaces;

PlantInstances;

Activities;

Observations;

Measurements;

Harvests;

Plans;

User decisions;

locally available recommendations;

history.


Internet access must not be required for ordinary garden recording.

Future network-dependent functionality must be isolated from the core database.


---

71. Import/export compatibility

The schema must support stable import/export identifiers.

Exports should use:

stable entity IDs;

canonical vocabulary IDs;

structured dates;

structured measurements;

explicit provenance where relevant.


Display labels must not be required for reconstructing the underlying data.

Imports must validate:

IDs;

relationships;

vocabulary values;

required fields;

units;

dates;

references.


Invalid data must not silently enter the database.


---

72. Migration compatibility

Database migrations must preserve existing garden data.

When a future version introduces:

new fields;

new vocabulary values;

new relationships;

richer knowledge;

additional spatial information;


existing records should remain valid.

New information should become:

unknown

where it cannot legitimately be reconstructed.

Historical detail must not be fabricated during migration.


---

73. What is intentionally not a V1 database entity

The following should not be implemented as full V1 systems unless later requirements demonstrate a genuine need:

WeatherObservation;

WeatherForecast;

LocalKnowledge;

CommunityContribution;

EvidenceAggregation;

StatisticalModel;

SolarModel;

PestRiskModel;

DiseaseRiskModel;

ChemicalProduct;

ApplicationRestriction;

UserAccount;

SynchronisationRecord;

full GrowthStage entity;

full Symptom ontology;

advanced GIS objects;

automated garden-design model.


V1 should not create infrastructure for future features merely for theoretical completeness.


---

74. Deliberate V1 simplifications

The following simplifications are intentional.

Planting

Planting is an Activity rather than a separate event system.

Interventions

Interventions are Activities with additional context rather than a separate fundamental event system.

Experiment results

Experiment results use ordinary Observations, Measurements and Activities rather than a separate Outcome entity.

Soil

Soil information uses SoilRecords plus historical Observations and Measurements rather than treating soil characteristics as permanent immutable properties.

Recommendations

Recommendation factors and evidence are stored separately so explanations remain structured without creating a large rule-specific schema.

Inbox

Inbox state is workflow state and does not become a source of garden truth.

These simplifications reduce implementation complexity while preserving the distinctions required by the conceptual model.


---

75. V1 database invariants

The following must remain true.

Invariant 1

A Plant is not a PlantInstance.

Invariant 2

A PlantInstance is not evidence that planting occurred unless an actual planting Activity exists.

Invariant 3

A Plan is not an Activity.

Invariant 4

A completed Plan does not automatically prove that an Activity occurred unless the user confirms the actual event.

Invariant 5

An Observation is not a diagnosis.

Invariant 6

An estimate is not a measurement.

Invariant 7

A Recommendation is not a fact.

Invariant 8

A Recommendation is not a UserDecision.

Invariant 9

A UserDecision is not evidence that an Activity occurred.

Invariant 10

A user preference is not horticultural knowledge.

Invariant 11

Personal history does not automatically become general knowledge.

Invariant 12

Current state must not erase historical state.

Invariant 13

Unknown information must not be fabricated.

Invariant 14

Deleting or deprecating knowledge must not invalidate historical garden records.

Invariant 15

A derived value must not silently replace its supporting evidence.


---

76. V1 database success criteria

The database implementation should allow the application to reliably answer:

Garden

What gardens exist?

What areas and growing spaces exist?

What is the current and historical geometry?


Plants

What plants are known?

What cultivars exist?

What plants are actually or potentially growing in the garden?

Where are they located?

What happened to them?


Activities

What actually happened?

When?

Where?

To what plant or space?


Soil

What is known about this soil?

What was observed?

What was measured?

When?

How confident is the information?


Planning

What is planned?

What was recommended?

What did the gardener decide?

What actually happened?


History

What was previously planted here?

What was observed?

What was harvested?

What problems occurred?

What interventions occurred?


Recommendations

What was recommended?

Why?

Based on what evidence?

How confident was the recommendation?

What did the user decide?


The database should answer these questions without requiring reconstruction from opaque or overwritten records.


---

77. Guiding principle

> The database should preserve what happened, what was observed, what was measured, what was inferred, what was recommended, and what the gardener decided — without confusing any of them.



V1 should use the simplest database structure that preserves those distinctions.

The database should be sophisticated enough to protect the garden's history and support explainable recommendations, but simple enough that it can be implemented, tested and maintained reliably.