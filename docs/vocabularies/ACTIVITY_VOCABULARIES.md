Garden Planner & Manager — Activity Controlled Vocabularies

Document: docs/vocabularies/ACTIVITY_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines controlled vocabularies used to classify activities and events recorded in the Garden Planner & Manager.

Activities represent things that:

happened;

are planned to happen;

were partially completed;

were skipped or cancelled;

were recorded as part of normal garden management.


The Activity model should remain a simple, reusable event primitive.

Activity-specific detail belongs in structured fields or related entities rather than being encoded into an unnecessarily large activity-type vocabulary.


---

2. Design principles

Activity vocabularies must:

use stable English canonical identifiers;

remain independent of UI wording;

support historical records;

distinguish planned activity from completed activity;

avoid encoding quantities or measurements as vocabulary values;

allow additional detail through structured fields;

support Quick Add;

support future modules without requiring the Activity model to be redesigned.


The fundamental distinction is:

> An activity describes what was done or intended. Structured data describes the details.



For example:

activity_type = watering
quantity = 20
unit = litre
method = hand_watered

rather than creating activity types such as:

watering_20_litres


---

3. Activity type

The primary classification of an activity.

3.1 Core V1 activity types

planting
watering
feeding
pruning
harvesting
observation
intervention
soil_work
maintenance
moving
removal

These should cover the majority of ordinary V1 garden activities.


---

3.2 Planting

planting

Used when a plant is:

planted;

transplanted into a growing space;

established from a propagated plant;

otherwise intentionally placed into its growing location.


The specific method should be represented separately where useful.

Examples:

activity_type = planting
planting_method = transplant

or:

activity_type = planting
planting_method = direct_sow


---

3.3 Watering

watering

Used when water is intentionally supplied to plants or growing spaces.

Details may include:

quantity;

unit;

method;

target;

duration;

source.


The activity type should not encode the watering method.


---

3.4 Feeding

feeding

Used when nutrients or fertilising material are intentionally supplied.

Examples include:

fertiliser;

compost application;

liquid feed;

other nutrient inputs.


The specific material and quantity should be stored separately.


---

3.5 Pruning

pruning

Used for intentional removal or alteration of plant growth for purposes such as:

shaping;

maintenance;

size control;

production;

plant health.


The specific pruning category may be represented separately.


---

3.6 Harvesting

harvesting

Used when produce is collected from the garden.

Harvest quantity must be represented using structured quantity data.

Examples:

quantity = 8
unit = count

or:

quantity = 2
unit = bunch


---

3.7 Observation

observation

Used when the gardener records something observed without necessarily performing an intervention.

Examples:

soil appears dry;

leaves are yellowing;

flowers have appeared;

water pooled after rain;

aphids observed.


An observation is evidence.

It must not automatically become a diagnosis, recommendation or intervention.


---

3.8 Intervention

intervention

Used for an intentional action taken to address a problem or influence garden conditions that does not fit a more specific core activity type.

Examples may include:

pest control;

disease management;

physical protection;

corrective treatment.


Specific intervention categories belong in the Problem/Intervention domain where required.


---

3.9 Soil work

soil_work

Used for physical actions performed on soil or growing media.

Examples:

digging;

loosening;

incorporating material;

preparing a bed;

improving drainage.


The specific operation should be represented separately when useful.


---

3.10 Maintenance

maintenance

Used for general garden maintenance that does not fit a more specific activity.

Examples:

repairing a bed;

clearing a path;

maintaining infrastructure;

cleaning garden equipment.



---

3.11 Moving

moving

Used when an existing plant, object or garden element is moved from one location to another.

A move should preserve the identity and history of the existing object rather than creating a new unrelated record.


---

3.12 Removal

removal

Used when a plant or garden element is intentionally removed.

Removal should not automatically mean deletion of its historical record.

The historical object should remain interpretable where appropriate.


---

4. Activity subtypes

Subtypes may be used where the distinction is useful to application behaviour.

They should remain smaller than the full set of possible gardening actions.

Potential V1 subtype groups include:

planting_method
watering_method
feeding_method
pruning_method
soil_work_method
harvest_method
observation_type
intervention_type

These groups should only be implemented where the application actually needs the distinction.


---

5. Planting method

Potential controlled values:

direct_sow
transplant
planting_bare_root
planting_container
division
propagation
other
unknown

Not every planting record needs a method.


---

6. Watering method

Potential controlled values:

hand_watered
hose
watering_can
drip
sprinkler
irrigation_system
rain
other
unknown

rain should only be used when the gardener intentionally records rainfall as the reason for a watering-related event.

Natural rainfall is not automatically an activity simply because it occurred.


---

7. Feeding method

Potential controlled values:

surface_application
incorporated
liquid_feed
foliar_feed
compost_application
other
unknown

The actual fertiliser or amendment should be represented separately.


---

8. Pruning method

Potential values:

deadheading
thinning
heading_back
shaping
removal_of_damaged_growth
rejuvenation
other
unknown

These values should not be interpreted as universally applicable to every plant.


---

9. Soil work method

Potential values:

digging
loosening
cultivation
bed_preparation
amendment_incorporation
mulching
drainage_work
other
unknown

Material quantities and measurements remain structured data.


---

10. Harvest classification

Harvest records may optionally classify the type of harvested material.

Potential values:

fruit
vegetable
leaf
root
tuber
bulb
stem
flower
seed
herb
other
unknown

This is a classification of the harvested material, not a plant taxonomy.


---

11. Observation type

Observations should be structured according to what is being observed.

Potential V1 categories:

plant
soil
water
weather
pest
disease
growth
flowering
fruiting
harvest
environment
infrastructure
general

The interface should normally present only relevant options based on context.

For example:

Observe → Soil

should present soil-related observations rather than the entire observation vocabulary.


---

12. Observation subject

Where necessary, an observation may identify its subject.

Potential subject types:

plant
growing_space
soil
infrastructure
garden
environment
other

The subject should normally be represented by a relationship to the relevant entity rather than as free text.


---

13. Observation result state

Observations may optionally describe the state observed.

Potential generic values:

normal
changed
improving
declining
present
absent
uncertain
unknown

These should only be used where the semantics are sufficiently clear.

Domain-specific observations should use more appropriate vocabularies where necessary.


---

14. Activity completion status

The shared planning status vocabulary in CORE_VOCABULARIES.md defines the lifecycle of planned work.

Activity records may use:

planned
scheduled
in_progress
partially_completed
completed
skipped
cancelled
expired

The distinction between planned and completed activity is mandatory.

A planned activity must never be treated as evidence that the activity occurred.


---

15. Activity status rules

15.1 Planned

The user intends the activity to occur.

It is not evidence that the activity happened.


---

15.2 Scheduled

The activity has an intended time or date.

It is still not evidence that the activity happened.


---

15.3 In progress

The user has indicated that work has begun but it has not been completed.


---

15.4 Partially completed

Only part of the intended activity was completed.

Example:

Plan:
Water Beds 1–4

Completed:
Beds 1–3

Remaining:
Bed 4

The system must preserve the distinction.


---

15.5 Completed

The activity was recorded as having occurred.

This is evidence of the user's recorded action, not necessarily independent verification.


---

15.6 Skipped

The planned activity was intentionally not performed.


---

15.7 Cancelled

The planned activity was cancelled and should no longer be treated as pending.


---

15.8 Expired

A planned activity passed its useful time without being completed or otherwise resolved.


---

16. Activity recurrence

Recurring activities should be represented as plans or schedules rather than by creating one permanent activity record.

Example:

Plan:
watering
frequency:
every_3_days

The system may generate planned occurrences.

Each actual completed occurrence must remain an independent historical record.


---

17. Recurrence frequency

Frequency should normally be represented structurally rather than through a large vocabulary.

Examples:

interval = 3
unit = day

or:

interval = 1
unit = week

Potential calendar units:

day
week
month
year

A vocabulary is appropriate for the unit; the numerical interval remains numeric.


---

18. Activity timing

Activity timing should use actual structured dates/times wherever possible.

Examples:

planned_date
completed_at
started_at
ended_at

The activity type must not encode timing.

For example, avoid:

spring_planting

as an activity type.

Use:

activity_type = planting

with the relevant date and planning context.


---

19. Activity quantity

Quantities must remain structured.

Examples:

quantity = 5
unit = count

quantity = 10
unit = litre

quantity = 2.5
unit = kilogram

The activity type must never encode quantity.


---

20. Activity quantity units

A small shared unit vocabulary should be used where the same units occur across multiple modules.

Potential V1 units include:

count
bunch
basket
container
litre
millilitre
kilogram
gram
metre
square_metre
hour
minute
other

Where a quantity has no meaningful standard unit, the user may select an appropriate supported unit.

The underlying numeric value must remain separate from the unit.


---

21. Harvest size category

Harvest size may optionally be recorded using:

very_small
small
regular
large
very_large

This is an observational classification and must not be interpreted as a precise measurement.


---

22. Harvest loss

Harvest loss must be recorded separately from successful harvest.

A loss record may contain:

quantity
unit
cause
date
plant
location
notes

Potential loss causes may include:

rot
pest
disease
weather
physical_damage
overripe
animal
unknown
other

These are initial classifications only. More detailed problem vocabularies may be used where appropriate.

The application must not infer an exact lost yield unless the user explicitly provides or estimates it.


---

23. Activity relationship to plants

An activity may relate to:

a plant;

a plant instance;

a cultivar;

a growing space;

an area;

the whole garden;

infrastructure;

another relevant entity.


The relationship should identify the actual subject of the activity.

Example:

activity:
watering

target:
Bed 2

or:

activity:
pruning

target:
Tomato plant instance

The Activity type itself should not encode the target.


---

24. Activity relationship to location

Activities may be associated with a spatial context.

Possible targets include:

garden
area
growing_space
point_location
plant_location
infrastructure

Location is a relationship, not an activity subtype.


---

25. Quick Add mapping

The V1 Quick Add interface should map common user actions to activity types.

Quick Add action	Activity type

Plant	planting
Water	watering
Harvest	harvesting
Observe	observation
Feed	feeding
Prune	pruning
Intervention	intervention


Additional activity types may be exposed through:

More

The Quick Add interface must not require users to understand the underlying vocabulary.


---

26. Context-sensitive Quick Add

Quick Add should inherit useful context where possible.

Example:

Garden → Bed 2 → + Add → Water

should preselect:

target = Bed 2
activity_type = watering

The user must be able to change the target.

Context is assistance, not a restriction.


---

27. Activity notes

Free-text notes may supplement structured information.

Notes should not be used as the sole representation of information that the application needs to:

search;

filter;

reason about;

calculate;

report;

compare historically.


For example, instead of:

> watered quite a lot



use structured data where possible:

quantity = 20
unit = litre

with the note retained as optional additional context.


---

28. Activity provenance

Activity records should be able to distinguish how they were recorded.

Examples:

data_origin = user_observed

or:

data_origin = user_imported

A completed activity recorded by the gardener is evidence of the gardener's record, not automatically independently verified fact.


---

29. Planned versus actual records

The application must distinguish:

planned activity

from:

actual activity

Where a plan results in completion, the completed activity should reference the originating plan where appropriate.

This preserves the difference between:

> I intended to water Bed 2.



and:

> I watered Bed 2.




---

30. Partial completion

Partial completion must preserve both:

what was intended;

what was actually completed.


The system should not simply convert:

partially_completed

into:

completed

because most of the work was done.

Where practical, completed portions should be represented as separate actual activity records.


---

31. Corrections to activities

Users must be able to correct ordinary recording mistakes.

Example:

Harvest recorded:
5 tomatoes

may be corrected to:

8 tomatoes

Where the change materially affects historical interpretation, the application should preserve an appropriate modification history.


---

32. Deletion and historical preservation

Activity records are part of the garden's historical record.

The application should favour:

correction;

archival;

cancellation;

explicit removal;


over silent destruction.

Deleting an activity must not accidentally remove associated:

observations;

harvest outcomes;

recommendations;

experiments;

historical context.



---

33. Activity and observation distinction

An observation records:

> What did I notice?



An intervention/activity records:

> What did I do?



These must remain conceptually separate.

Example:

Observation:
Leaves yellowing.

Possible inference:
Nutrient deficiency.

Activity:
Applied fertiliser.

The application must not silently transform one into another.


---

34. Activity and diagnosis distinction

Recording:

pest observed

does not establish:

specific pest diagnosis

Where the application proposes a diagnosis, it should remain a separate inference or recommendation with its own confidence and provenance.


---

35. Activity and recommendation distinction

A recommendation may suggest:

Water Bed 2.

This does not create a watering activity.

Only the user recording or confirming an actual action should establish that the watering occurred.


---

36. Activity and experiment distinction

Experiments should reuse ordinary activities.

For example:

Experiment:
Does mulch reduce watering requirements?

Activities:
watering
watering
watering

The experiment provides context for interpreting the activities.

It should not require a separate activity system.


---

37. V1 implementation boundary

V1 should implement only activity classifications needed to support the core workflows.

Minimum required:

planting
watering
feeding
pruning
harvesting
observation
intervention

The following may initially be supported as additional general activities:

soil_work
maintenance
moving
removal

Additional specialised activity types should only be introduced when a real workflow requires them.


---

38. Avoiding vocabulary expansion

Do not create separate activity types for minor variations.

Avoid:

watering_bed
watering_plant
watering_tree
watering_seedling
watering_greenhouse

Use:

activity_type = watering

with an appropriate target relationship.

Similarly, avoid:

harvest_tomato
harvest_lettuce
harvest_carrot

Use:

activity_type = harvesting

with the relevant plant or produce relationship.


---

39. Relationship to other vocabularies

Activity vocabularies should remain separate from:

plant vocabularies;

soil vocabularies;

problem vocabularies;

planning vocabularies;

provenance vocabularies.


For example:

Activity:
intervention

Problem:
aphids

Target:
Tomato in Bed 2

This is preferable to creating:

aphid_treatment

as a universal activity type.


---

40. Localisation

All activity identifiers are canonical English identifiers.

Example:

Stored:
watering

English:
Watering

Afrikaans:
Natmaak

The UI localisation layer is responsible for user-facing labels.

Activity identifiers must never depend on translated text.


---

41. Import/export

Imports and exports must use canonical identifiers.

Example:

{
  "activity_type": "watering",
  "status": "completed"
}

not:

{
  "activity_type": "Watering",
  "status": "Completed"
}

Display labels may vary by language without affecting the data format.


---

42. Validation

Import validation should verify that:

the activity type exists;

the value is active or historically valid;

the activity status is valid;

required relationships exist;

quantities use valid units;

dates are valid;

deprecated values are handled appropriately;

planned and completed semantics are not contradictory.


For example, an activity cannot be interpreted as completed merely because its planned date has passed.


---

43. Historical compatibility

Activity vocabulary values must remain interpretable across application versions.

If a value becomes inappropriate:

1. mark it deprecated;


2. retain it for existing records;


3. define a replacement if necessary;


4. migrate records only when the semantic mapping is safe.



Do not change the meaning of an existing identifier simply to make the current UI cleaner.


---

44. Vocabulary review checklist

Before adding an activity value, ask:

1. Is this genuinely a different type of activity?


2. Could the distinction instead be represented by a target relationship?


3. Could it be a subtype?


4. Could it be structured data?


5. Does it affect application behaviour?


6. Does it need to appear in Quick Add?


7. Will it be useful for search or filtering?


8. Will it need to survive historically?


9. Does another vocabulary already contain the concept?


10. Would adding it make the activity model unnecessarily complex?




---

45. V1 activity vocabulary summary

The recommended V1 activity types are:

planting
watering
feeding
pruning
harvesting
observation
intervention
soil_work
maintenance
moving
removal

Common supporting classifications include:

planting_method
watering_method
feeding_method
pruning_method
soil_work_method
harvest_classification
observation_type
observation_subject
harvest_size_category
harvest_loss_cause

These should be implemented only where required by the corresponding V1 workflows.


---

46. Guiding principle

> Keep the Activity vocabulary small; represent detail through relationships and structured data.



The activity system should make it easy to answer:

> What happened?



> What was planned?



> What was actually completed?



> Where did it happen?



> What was it done to?



> What did the gardener observe?



> What resulted?



without turning every possible gardening action into a separate database concept.

The gardener should experience a simple activity log.

The application should retain the structured information necessary to make that history useful.