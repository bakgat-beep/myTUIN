Garden Planner & Manager — Garden Vocabularies

Document: docs/vocabularies/GARDEN_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines controlled vocabularies used to describe the physical and spatial structure of a user's garden.

It covers classifications that are useful across:

garden setup;

map display;

growing spaces;

infrastructure;

spatial planning;

filtering;

recommendations;

import/export;

historical garden records.


It does not define:

geographic coordinates;

dimensions;

geometry data;

map rendering;

individual garden names;

plant identities;

soil measurements;

recommendation logic.


Those are represented elsewhere in the data model.

The vocabulary set is deliberately small. A classification is included only where a stable controlled value provides useful application behaviour.


---

2. Design principles

Garden vocabularies must:

use stable English canonical identifiers;

remain independent of map-rendering technology;

support historical records;

work offline;

support import/export;

remain understandable to future application versions;

avoid duplicating structured spatial data;

avoid creating classifications where free-form or numeric data is more appropriate.



---

3. Area type

An Area represents a broad spatial region within a garden.

Canonical values:

garden
zone
section

garden

The overall garden or property context.

zone

A meaningful subdivision of the garden used for organisation or planning.

Examples:

north garden;

orchard zone;

vegetable zone;

native planting zone.


section

A smaller organisational subdivision within a garden or zone.

The application should not require users to create areas simply to satisfy the data model.


---

4. Growing-space type

A Growing Space is a location in which plants may be grown or managed.

Canonical values:

garden_bed
raised_bed
in_ground_bed
container
pot
planter
greenhouse
polytunnel
orchard
row
border
vertical_space
nursery_area
lawn
mixed_area
other

Notes

These values describe the type of growing space, not the growing method of an individual plant.

For example:

Growing space:
raised_bed

Plant:
tomato

Growing method:
outdoor

The application should not assume that a growing-space type determines every environmental characteristic.


---

5. Infrastructure type

Infrastructure represents physical or functional garden features that may affect planning or plant suitability.

Canonical values:

path
fence
wall
gate
building
shed
greenhouse_structure
irrigation
water_source
drain
compost_area
storage_area
support_structure
trellis
shade_structure
lighting
utility
other

Infrastructure may be spatially represented and may influence recommendations.

Examples:

a trellis may support climbing plants;

irrigation may affect water availability;

a fence may affect light or wind;

a drain may affect drainage.


The vocabulary does not define those effects. They belong to garden-condition and recommendation logic.


---

6. Spatial object type

Where the application needs a common classification for objects displayed on the garden map, the following conceptual types may be used:

area
growing_space
plant
infrastructure
soil_area
experiment_area
other

This classification is intended for application-level spatial organisation.

It must not be confused with the specific database entity type or with a map-rendering implementation.


---

7. Geometry type

Where geometry type must be explicitly classified, use:

point
line
polygon

point

A location represented primarily by a single position.

Examples:

water tap;

individual tree;

sensor.


line

A linear feature.

Examples:

path;

irrigation line;

fence.


polygon

An area with a defined boundary.

Examples:

growing bed;

orchard;

garden zone.


Geometry itself remains structured spatial data and must not be encoded as a vocabulary value.


---

8. Spatial relationship type

Only relationships that have meaningful application semantics should be classified.

Canonical values:

contains
inside
adjacent_to
overlaps
intersects
near
attached_to
supports

These relationships describe spatial or functional relationships between garden objects.

They should not be used merely because two objects happen to be visually close on the map.


---

9. Access classification

Where access affects garden planning, an object may have:

accessible
limited_access
inaccessible
unknown

This is particularly useful where physical access may influence:

maintenance;

planting;

harvesting;

infrastructure placement.


Access should not be recorded unless it has practical relevance.


---

10. Orientation

Orientation is represented primarily as structured directional data rather than a vocabulary.

Where a coarse orientation classification is genuinely required for filtering or reasoning:

north
north_east
east
south_east
south
south_west
west
north_west
variable
unknown

The application should retain more precise orientation data where available rather than replacing it with this classification.

For example:

aspect = 147°

is more precise than:

south_east

The category may be derived from the underlying value.


---

11. Map layer type

Map layers are a UI concept and should remain deliberately lightweight.

Core conceptual layers:

growing_spaces
plants
planned_planting
historical_planting
infrastructure
soil
conditions
problems
experiments

A layer may be hidden or unavailable depending on the user's data.

Map layer identifiers should not be treated as permanent database entities.

Future map implementations may combine or subdivide layers without changing underlying garden records.


---

12. Spatial state

When a spatial object needs to distinguish its current lifecycle state:

planned
current
historical
archived

Where possible, actual dates should be used instead of relying solely on state labels.

For example:

created_at
retired_at

provides more durable historical information than simply storing:

historical


---

13. Garden condition categories

Garden conditions should generally be represented as structured observations or measurements rather than a large vocabulary.

Where a condition needs a broad classification for application behaviour, the following categories may be used:

light
water
moisture
temperature
wind
drainage
soil
shelter

These are condition domains, not condition ratings.

For example:

condition_domain = light

does not mean:

light = high

The actual observation, measurement or estimate is stored separately.


---

14. Condition rating

A general rating should only be used where the underlying domain has deliberately adopted a common qualitative scale.

Canonical values:

very_low
low
moderate
high
very_high
unknown

This should not be used automatically for every garden condition.

Where a domain requires a different scale, its domain-specific vocabulary should define it.

For example, sunlight may be represented by actual hours rather than a generic rating.


---

15. Wind exposure

Where a simple qualitative wind-exposure classification is needed:

sheltered
partially_exposed
exposed
highly_exposed
unknown

This is an estimate/classification, not a wind-speed measurement.

Measured wind information remains structured data.


---

16. Light exposure

Light should preferably be represented using measurable or structured information where available.

Where a coarse classification is required:

full_sun
partial_sun
partial_shade
full_shade
variable
unknown

These values describe broad exposure conditions.

They should not be interpreted as universal fixed sunlight-hour thresholds without the recommendation system defining the applicable context.


---

17. Water availability

Where a broad classification of water availability is needed:

very_low
low
moderate
high
very_high
unknown

Actual water information may instead be represented through:

irrigation availability;

watering records;

rainfall;

soil moisture observations;

measured data.


The classification should therefore be derived where possible rather than treated as the sole source of truth.


---

18. V1 simplification rules

The application should not create separate garden vocabularies for every possible spatial concept.

The following should remain structured data or relationships:

latitude;

longitude;

altitude;

dimensions;

area;

perimeter;

geometry;

orientation angle;

slope;

distances;

dates;

environmental measurements;

spatial coordinates.


Similarly, the application should not create vocabulary values for individual:

gardens;

beds;

trees;

paths;

fences;

irrigation systems;

buildings.


Those are actual garden records.


---

19. User-defined garden types

Users may need garden-specific organisational labels.

These should be represented as user-extensible reference data, not as additions to this controlled vocabulary.

Examples:

Cutting garden
Kids' garden
Native garden
Food garden
Trial area

A user-created classification must remain associated with its creator/garden context.

It must not automatically become a global system vocabulary.


---

20. Historical spatial information

Changing the geometry or classification of a spatial object must not destroy historical records associated with that object.

For example:

Bed 2
2025:
3 m × 1 m

2026:
4 m × 1 m

The application should retain the historical state where it is relevant to:

previous planting;

spatial analysis;

recommendations;

garden history.


The vocabulary system itself does not store these historical changes.


---

21. Relationship to the data model

The distinction is:

Garden vocabulary
        ↓
Controlled classification

Garden record
        ↓
Actual user-owned object

Structured spatial data
        ↓
Geometry, dimensions, coordinates, measurements

Spatial relationship
        ↓
Relationship between objects

Example:

type = raised_bed

name = Bed 2

length = 4 m

width = 1 m

geometry = polygon

location = stored coordinates

Only raised_bed is a controlled vocabulary value.


---

22. Relationship to recommendations

Garden classifications may provide inputs to the recommendation engine.

For example:

growing_space_type = raised_bed
light_exposure = full_sun
wind_exposure = exposed

The recommendation engine must not assume that these classifications alone determine suitability.

Recommendations should consider the complete available evidence and its confidence.


---

23. Unknown information

Missing spatial or environmental information must remain explicit.

Examples:

light_exposure = unknown
wind_exposure = unknown
access = unknown

The application must not silently convert unknown information into a presumed value.

If an unknown value materially affects a recommendation, the recommendation system may identify the uncertainty.


---

24. Vocabulary boundaries

The following concepts belong elsewhere:

Concept	Location

Plant identity	Plant reference data
Cultivar	Plant reference data
Soil texture	Soil vocabulary
Soil pH	Structured measurement
Planting activity	Activity vocabulary
Recommendation status	Planning vocabulary
Evidence source	Provenance vocabulary
Garden coordinates	Structured spatial data
Bed dimensions	Structured spatial data
Individual garden bed	Garden record
User's custom category	User-extensible reference data


This prevents Garden Vocabularies from becoming a general-purpose classification system.


---

25. V1 vocabulary set

The minimum V1 Garden vocabulary set is:

Area type

garden
zone
section

Growing-space type

garden_bed
raised_bed
in_ground_bed
container
pot
planter
greenhouse
polytunnel
orchard
row
border
vertical_space
nursery_area
lawn
mixed_area
other

Infrastructure type

path
fence
wall
gate
building
shed
greenhouse_structure
irrigation
water_source
drain
compost_area
storage_area
support_structure
trellis
shade_structure
lighting
utility
other

Geometry type

point
line
polygon

Spatial relationship type

contains
inside
adjacent_to
overlaps
intersects
near
attached_to
supports

Access

accessible
limited_access
inaccessible
unknown

Broad environmental exposure

full_sun
partial_sun
partial_shade
full_shade
variable
unknown

Wind exposure

sheltered
partially_exposed
exposed
highly_exposed
unknown


---

26. Values deliberately excluded

The following should not be added as Garden vocabulary values merely for completeness:

every possible garden feature;

every possible map layer;

every environmental measurement;

every soil characteristic;

every spatial measurement;

every user-created category;

every gardening method;

every plant characteristic.


If a concept does not provide meaningful controlled semantics, it should remain ordinary data.


---

27. Vocabulary review rules

Before adding a Garden vocabulary value, ask:

1. Is this a stable classification rather than an individual record?


2. Will multiple modules use it?


3. Does application behaviour depend on the distinction?


4. Could the information be stored more accurately as structured data?


5. Does the concept belong in another domain vocabulary?


6. Does the value need to remain stable for historical records?


7. Will it be useful in import/export or filtering?


8. Can a casual gardener ignore it when it is not relevant?



If the answer to these questions is generally no, the value should not be added.


---

28. Guiding principle

> Classify only what the application needs to understand. Store everything else as garden data.



The Garden vocabulary system should remain small enough that developers can understand it, stable enough that historical records remain interpretable, and useful enough that classifications genuinely improve garden planning and management.