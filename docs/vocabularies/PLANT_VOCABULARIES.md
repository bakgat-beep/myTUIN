# Garden Planner & Manager — Plant Controlled Vocabularies

**Document:** `docs/vocabularies/PLANT_VOCABULARIES.md`
**Version:** 0.7
**Status:** Working specification
**Last updated:** October 2026

---

## 1. Purpose

This document defines controlled vocabularies used specifically for plant-related information in the Garden Planner & Manager.

Plant vocabularies classify plant characteristics. They do not contain detailed plant knowledge.

Use structured data, relationships, ranges, dates or knowledge records where those provide a better representation.

---

## 2. Relationship to other vocabularies

Plant vocabularies operate alongside:

* **CORE_VOCABULARIES** — shared application concepts
* **SOIL_VOCABULARIES** — soil classifications
* **ACTIVITY_VOCABULARIES** — garden activity classifications
* **PROBLEM_VOCABULARIES** — problems, symptoms and diagnostic classifications
* **PLANNING_VOCABULARIES** — planning and recommendation classifications
* **PROVENANCE_VOCABULARIES** — evidence and source classifications

Plant-specific concepts belong here only where they are genuinely plant classifications.

---

## 3. Plant biological lifecycle

Lifecycle describes the broad biological persistence of a plant.
This concerns the biological persistence of the plant and not PlantInstance lifecycle.

### Values

```text
annual
biennial
perennial
unknown
```

### Definitions

**annual**
Normally completes its biological lifecycle within one growing season or year.

**biennial**
Normally requires two growing seasons to complete its lifecycle.

**perennial**
Normally survives for multiple growing seasons.

**unknown**
Lifecycle is not currently known.

Lifespan beyond this broad classification remains plant knowledge or structured data.

---

## 3a. Plant Instance Lifecycle

### Definitions

**planned**
The plant instance has been planned but has not yet been planted/started.

**planted**
The plant has been planted or sown and the instance is active.

**established**
The plant instance has successfully established in its location after planting/germination.

**dormant**
The instance remains active but the plant is currently dormant.

**harvested**
The instance has reached its harvest endpoint, where harvesting represents completion of the intended production cycle.

**removed**
The plant was deliberately removed before the normal end of its lifecycle.

**failed**
The plant instance failed to establish or survive, or otherwise became non-viable.

**completed**
The planned lifecycle has ended successfully without `harvested` being the appropriate terminal state.                  |

---

## 4. Plant role

Plant role describes a plant's principal functional or garden purpose.

A plant may have multiple roles.

### Values

```text
food_crop
fruit_crop
herb
leafy_green
root_crop
bulb_crop
tuber_crop
legume
grain_crop
cover_crop
green_manure
ornamental
pollinator_support
wildlife_support
medicinal
aromatic
culinary
nitrogen_fixer
companion
hedge
screening
windbreak
ground_cover
soil_improvement
unknown
```

`herb` is a garden/culinary role here, not a botanical growth-form classification.

---

## 5. Food-production category

Describes the edible part harvested from the plant.

### Values

```text
fruit
leaf
stem
flower
root
bulb
tuber
seed
grain
pod
shoot
whole_plant
multiple
unknown
```

Multiple values may apply.

This describes the harvested edible part, not botanical classification.

---

## 6. Growth stage

Describes a plant's developmental stage where stage-specific information is relevant.

### Values

```text
seed
germination
seedling
establishment
vegetative
bud_formation
flowering
fruiting
seed_development
maturity
dormancy
senescence
harvestable
unknown
```

Not every plant uses every stage. Stage-specific timing and requirements should remain separate structured information where appropriate.

---

## 7. Growth habit

Describes general physical growth form and behaviour.

### Values

```text
upright
spreading
bushy
clumping
rosette
trailing
climbing
vining
twining
creeping
prostrate
mounding
tufted
tree
shrub
subshrub
unknown
```

Multiple values may apply.

Growth habit does not replace measured mature dimensions.

---

## 8. Growth rate

Describes broad growth rate toward mature size under suitable conditions.

### Values

```text
slow
moderate
fast
variable
unknown
```

This is a planning classification only. It does not replace measured growth rates, mature dimensions or cultivar-specific information.

---

## 9. Growing method

Describes broad cultivation methods.

### Values

```text
direct_sow
transplant
container
raised_bed
in_ground
protected_culture
greenhouse
indoors
outdoors
hydroponic
aquaponic
vertical
unknown
```

Multiple values may apply.

Specific garden infrastructure belongs to garden/planning data.

---

## 10. Propagation method

Describes how a plant can be propagated.

### Values

```text
seed
division
cutting
layering
grafting
budding
offset
runner
tissue_culture
unknown
```

Multiple values may apply.

Detailed propagation instructions belong to plant knowledge.

---

## 11. Maturity classification

Provides a broad qualitative maturity category where useful.

### Values

```text
early
mid_season
late
unknown
```

This must not replace actual days-to-maturity, dates or ranges.

Where maturity differs substantially by cultivar, store it at cultivar level.

---

## 12. Frost sensitivity

Describes relative sensitivity to frost.

### Values

```text
very_sensitive
sensitive
moderately_sensitive
tolerant
very_tolerant
unknown
```

This is qualitative. Actual temperature thresholds remain structured data.

---

## 13. Heat tolerance

Describes relative tolerance of high temperatures.

### Values

```text
very_sensitive
sensitive
moderate
tolerant
very_tolerant
unknown
```

Actual temperature requirements and limits remain structured data.

---

## 14. Drought tolerance

Describes relative tolerance of dry conditions.

### Values

```text
very_low
low
moderate
high
very_high
unknown
```

This does not define irrigation requirements.

---

## 15. Waterlogging tolerance

Describes relative tolerance of prolonged saturated conditions.

### Values

```text
very_low
low
moderate
high
very_high
unknown
```

This does not describe soil drainage itself.

---

## 16. Salinity tolerance

Describes relative tolerance of saline conditions.

### Values

```text
very_sensitive
sensitive
moderate
tolerant
very_tolerant
unknown
```

Actual salinity thresholds remain structured data.

---

## 17. Light requirement

Describes the preferred growing light level.

### Values

```text
full_sun
partial_shade
full_shade
variable
unknown
```

### Definitions

**full_sun**
Normally performs best with substantial direct sunlight.

**partial_shade**
Normally performs best with some direct sunlight and/or significant periods of shade.

**full_shade**
Can perform satisfactorily with little or no direct sunlight.

**variable**
Acceptable conditions vary substantially by cultivar, climate, growth stage or other context.

**unknown**
Preferred light requirement is not currently known.

This describes preferred light, not tolerance. `shade_tolerance` remains a separate classification.

Actual garden light conditions belong to environmental/garden data.

---

## 18. Shade tolerance

Describes relative tolerance of reduced light.

### Values

```text
very_low
low
moderate
high
very_high
unknown
```

A plant may prefer full sun while still having some shade tolerance.

---

## 19. Plant temperature classification

Provides a broad seasonal temperature preference.

### Values

```text
cool_season
warm_season
intermediate
variable
unknown
```

This is a broad planning classification only.

Actual temperature requirements remain structured data and may vary by growth stage.

---

## 20. Plant establishment difficulty

Describes broad difficulty of establishing the plant.

### Values

```text
easy
moderate
difficult
unknown
```

This is a user-facing summary, not a substitute for propagation or cultivation requirements.

---

## 21. Maintenance demand

Describes broad overall maintenance demand.

### Values

```text
low
moderate
high
unknown
```

This is a summary classification. Specific maintenance requirements remain plant knowledge and activity data.

---

## 22. Root behaviour

Describes broad root-growth characteristics relevant to planning.

### Values

```text
shallow
moderate
deep
spreading
unknown
```

Multiple values may apply where necessary.

Specific root depth and spread remain structured data.

---

## 23. Support requirement

Describes whether physical support is generally required or beneficial.

### Values

```text
none
optional
recommended
required
unknown
```

This applies to climbing, vining, sprawling or structurally weak plants as appropriate.

The type of support is separate from this vocabulary. Examples include trellis, stake, cage, fence or frame.

Specific support relationships belong to garden/planning data.

---

## 24. Pollination requirement

Describes the broad biological requirement for pollination.

### Values

```text
self_fertile
partially_self_fertile
cross_pollination_required
unknown
```

Specific compatible pollination partners remain plant relationships/knowledge.

A plant that benefits from a partner despite being self-fertile should not receive a separate requirement value solely for that reason.

---

## 25. Pollination method

Describes the broad pollination mechanism.

### Values

```text
wind
insect
bird
other_animal
water
self
multiple
unknown
```

Specific pollinators remain plant knowledge or relationship data.

This does not imply that a particular pollinator is present in the user's garden.

---

## 26. Planting stock type

Describes the planting material used when establishing a plant instance.

### Values

```text
seed
seedling
plug
bare_root
bulb
tuber
rhizome
cutting
division
grafted_plant
established_plant
unknown
```

This is distinct from `propagation_method`: planting stock describes what is planted, while propagation method describes how the plant was propagated.

---

## 27. Resistance and susceptibility

Pest and disease response should be represented as a relationship between:

```text
plant/cultivar
    ↓
problem
    ↓
response characteristic
```

Where a controlled classification is required for that relationship:

```text
highly_susceptible
susceptible
moderately_susceptible
tolerant
resistant
highly_resistant
unknown
```

`resistant` does not mean immune.

---

## 28. Cultivar-specific classifications

A classification should be stored at cultivar level where it is known to differ materially between cultivars.

Examples include:

```text
maturity
frost_sensitivity
heat_tolerance
growth_habit
growth_rate
size
support_requirement
resistance
```

General species-level knowledge should not be incorrectly copied into every cultivar.

---

## 29. Plant knowledge versus vocabulary

The following are plant knowledge, reference data, measurements, ranges or relationships rather than vocabulary values:

* species and scientific names
* common names and synonyms
* cultivars
* botanical families
* pests and diseases
* companion and antagonistic relationships
* planting windows
* maturity ranges
* temperature ranges
* light and water requirements
* soil requirements
* nutrient requirements
* spacing recommendations
* harvest information
* propagation instructions
* yield
* storage and preservation
* toxicity
* regional information

For example:

```text
Tomato
```

is plant reference data, while:

```text
annual
```

is a vocabulary value.

---

## 30. Regional and seasonal information

Plant vocabularies must not encode geographically universal assumptions where the characteristic depends on location.

Regional attributes such as native status belong to regional plant data.

Seasonal attributes such as:

```text
sowing_period
transplanting_period
planting_period
flowering_period
fruiting_period
harvest_period
```

belong in structured temporal data.

Planting recommendations should consider location, climate, hemisphere, season, plant requirements and cultivar where relevant.

---

## 31. Context-dependent classifications

Plant characteristics may depend on:

* cultivar
* location
* climate
* season
* growth stage
* growing method
* soil
* water availability

Broad vocabulary values should therefore never override more precise contextual knowledge.

For example:

```text
drought_tolerance = high
```

does not imply:

```text
no_irrigation_required
```

during establishment.

---

## 32. Multiple values

Where the real-world concept allows multiple classifications, the data should support multiple values.

Examples:

```text
roles:
    food_crop
    culinary
    pollinator_support
```

```text
propagation_methods:
    seed
    cutting
```

```text
growth_habit:
    climbing
    vining
```

The system should not force an artificial single classification.

---

## 33. Unknown information

Unknown information must remain explicit.

Examples:

```text
lifecycle = unknown
frost_sensitivity = unknown
light_requirement = unknown
pollination_requirement = unknown
```

`unknown` must not be interpreted as average, moderate, suitable, safe or tolerant.

How an unknown value affects a recommendation is determined by the recommendation logic.

---

## 34. Historical compatibility

Once a vocabulary identifier is used in stored data:

* its meaning must remain stable;
* it must not be renamed merely for UI purposes;
* it must not be deleted while historical records depend on it.

Where a meaning must change materially, introduce a new identifier.

Deprecated identifiers remain available for interpreting historical records.

---

## 35. Localisation

Vocabulary identifiers are canonical English identifiers.

Example:

```text
Stored:
cool_season

English:
Cool-season

Afrikaans:
Koelseisoen
```

Application logic uses the canonical identifier. The UI uses the localisation layer.

---

## 36. Recommendation use

Plant vocabulary values may provide inputs to recommendations, including:

```text
lifecycle
growth_habit
growth_rate
light_requirement
shade_tolerance
frost_sensitivity
heat_tolerance
drought_tolerance
support_requirement
```

Vocabulary values are inputs, not complete recommendation rules.

Recommendations should use relevant contextual information and account for material unknowns or limitations.

---

## 37. No false precision

A qualitative value must not be converted into a precise measurement unless supported by separate evidence.

For example:

```text
frost_sensitivity = sensitive
```

must not automatically become:

```text
minimum_temperature = 0 °C
```

Precise thresholds belong in structured evidence-based data.

---

## 38. Vocabulary review checklist

Before adding a plant vocabulary value, ask:

1. Is it genuinely a plant classification?
2. Does it already exist in Core or another appropriate vocabulary?
3. Should it instead be structured data, a relationship or plant knowledge?
4. Does it depend on cultivar, location, season, growth stage or other context?
5. Can multiple values apply?
6. Will it materially improve filtering, validation, planning or recommendations?
7. Can its meaning remain stable over time?

If these questions do not establish a clear need, do not add the value.

---

## 39. Summary of plant vocabularies

| Vocabulary                 | Purpose                                                 |
|----------------------------|---------------------------------------------------------|
| Biological lifecycle       | Broad biological persistence                            |
| Instance lifecycle         | Lifecycle state of a PlantInstance in the user's garden |
| Plant role                 | Functional/garden role                                  |
| Food-production category   | Edible plant part                                       |
| Growth stage               | Developmental stage                                     |
| Growth habit               | Physical growth form                                    |
| Growth rate                | Broad rate of growth                                    |
| Growing method             | Broad cultivation method                                |
| Propagation method         | Propagation approach                                    |
| Maturity classification    | Broad maturity timing                                   |
| Frost sensitivity          | Relative frost sensitivity                              |
| Heat tolerance             | Relative heat tolerance                                 |
| Drought tolerance          | Relative drought tolerance                              |
| Waterlogging tolerance     | Relative tolerance of saturated conditions              |
| Salinity tolerance         | Relative salinity tolerance                             |
| Light requirement          | Preferred light level                                   |
| Shade tolerance            | Relative tolerance of reduced light                     |
| Temperature classification | Broad cool/warm season behaviour                        |
| Establishment difficulty   | Broad establishment difficulty                          |
| Maintenance demand         | Broad maintenance demand                                |
| Root behaviour             | Broad root-growth characteristic                        |
| Support requirement        | Degree of physical support requirement                  |
| Pollination requirement    | Broad pollination requirement                           |
| Pollination method         | Broad pollination mechanism                             |
| Planting stock type        | Type of planting material                               |
| Resistance/susceptibility  | Problem-specific response classification                |

---

## 40. Guiding principle

> **Plant vocabularies classify plant characteristics; they do not attempt to contain plant knowledge.**

The vocabulary system should remain:

* small;
* stable;
* contextual;
* extensible;
* presentation-localised;
* compatible with historical records;
* useful to planning and recommendations;
* separate from detailed horticultural knowledge.

## 41. Plant field set and storage structure

The information the application holds about a plant is described
by the field list this section previously presented. That list has
been reorganised by storage category. Each category reflects the
shape of the fields it contains: a scalar classification, a
value that may hold multiple entries, a numeric range with a unit,
a recurring window, free text, a relationship to another entity, or
a reference to system data.

The storage structures themselves are defined in
`docs/V1_DATABASE_SCHEMA.md`. This section names the tables so the
two documents can be read together, but the schema document remains
authoritative for column types, constraints and indexes.

### 41.1 Scalar classifications on `plant`

Each of these fields holds at most one value per plant. They are
stored as typed columns on the `plant` table.

| Field | Vocabulary |
| --- | --- |
| `growth_rate` | Growth rate (§8) |
| `support_requirement` | Support requirement (§23) |
| `light_requirement` | Light requirement (§17) |
| `shade_tolerance` | Shade tolerance (§18) |
| `temperature_class` | Plant temperature classification (§19) |
| `frost_sensitivity` | Frost sensitivity (§12) |
| `heat_tolerance` | Heat tolerance (§13) |
| `drought_tolerance` | Drought tolerance (§14) |
| `salinity_tolerance` | Salinity tolerance (§16) |
| `waterlogging_tolerance` | Waterlogging tolerance (§15) |
| `maturity` | Maturity classification (§11) |
| `maintenance_demand` | Maintenance demand (§21) |
| `pollination_requirement` | Pollination requirement (§24) |
| `container_suitable` | (none — nullable Boolean) |
| `fertility_requirement` | Fertility requirement |
| `toxicity` | Toxicity |

All fields are nullable. `null` means "not recorded"; the `unknown`
value within a vocabulary means "recorded as unknown"
(CORE_VOCABULARIES §4). `container_suitable` is a nullable Boolean
because §41 does not provide a vocabulary for it and a three-value
enum would be indistinguishable from the nullable Boolean.

### 41.2 Multi-value classifications in `plant_multi_value`

Each of these fields may hold zero or more values per plant. They
are stored as rows in the `plant_multi_value` join table, one row
per (plant, kind, value). The `kind` column is the discriminator
that names which of the following fields the row represents.

| Field | Vocabulary |
| --- | --- |
| `roles` | Plant role (§4) |
| `food_production_category` | Food-production category (§5) |
| `growth_habit` | Growth habit (§7) |
| `root_behaviour` | Root behaviour (§22) |
| `growing_methods` | Growing method (§9) |
| `propagation_methods` | Propagation method (§10) |
| `planting_stock` | Planting stock type (§26) |
| `water_requirement` | Water requirement |
| `preferred_soil` | Soil texture (SOIL_VOCABULARIES §3) |
| `soil_drainage` | Soil drainage (SOIL_VOCABULARIES §5) |
| `rotation_group` | Rotation group (see §41.7) |
| `toxic_parts` | Toxic part |
| `preservation_methods` | Preservation method |
| `support_type` | Support type |

`planting_stock` here is distinct from the single-value
`PlantInstance.plantingStock`: this field records the forms in which
the plant can commonly be established, whereas the instance records
what was actually planted for one specific plant instance.

The `rotation_group` kind is different from the others: its
`value_id` is a RotationGroup entity id, not a vocabulary id. See
§41.7.

### 41.3 Numeric facts in `plant_numeric_fact`

Each of these fields holds a numeric value, a range, or both, with
an optional unit. They are stored as rows in the
`plant_numeric_fact` table, one row per (plant, kind).

| Field | Typical unit |
| --- | --- |
| `mature_height` | Length |
| `mature_width` | Length |
| `root_depth` | Length |
| `minimum_temperature` | Temperature |
| `maximum_temperature` | Temperature |
| `preferred_ph` | pH |
| `spacing` | Length |
| `row_spacing` | Length |
| `seed_depth` | Length |
| `germination_temperature` | Temperature |
| `germination_time` | Time |
| `days_to_maturity` | Days |
| `yield` | Mass or count |
| `container_size` | Volume |
| `harvest_frequency` | Time |

Each row carries `min_value`, `max_value` and `typical_value`, all
nullable, of which at least one must be non-null. If both `min_value`
and `max_value` are set, `min_value` must not exceed `max_value`.
This shape lets a fact be a single number, a range, or a range with
a preferred typical value.

Units come from `QuantityUnit` (see
`docs/vocabularies/ACTIVITY_VOCABULARIES.md` for the origin values
and DEC-046 for the unification).

### 41.4 Temporal facts in `plant_temporal_fact`

Each of these fields describes a recurring annual window. They are
stored as rows in the `plant_temporal_fact` table, one row per
(plant, kind).

| Field |
| --- |
| `sowing_period` |
| `transplanting_period` |
| `planting_period` |
| `flowering_period` |
| `fruiting_period` |
| `harvest_period` |

Each row carries `start_month`, `start_day`, `end_month` and
`end_day`, plus an optional `season` classification. No year is
stored. The window is a recurring annual one.

Rows may carry optional `hemisphere` and `geographic_scope` context
to indicate which region the window applies to. A generic "typical
temperate climate" row leaves both null. If a plant has materially
different windows in different hemispheres, that distinction is a
follow-up; V1 permits one row per (plant, kind).

### 41.5 Free-text notes in `plant_note`

Each of these fields holds free text. They are stored as rows in the
`plant_note` table, one row per (plant, kind).

| Field |
| --- |
| `transplanting_notes` |
| `feeding` |
| `watering` |
| `pruning` |
| `other_maintenance` |
| `harvest_indicators` |
| `harvest_method` |
| `storage` |
| `culinary_uses` |
| `pollination_notes` |
| `rotation_notes` |
| `regional_notes` |
| `climate_notes` |
| `safety_notes` |

One note per kind. If a plant genuinely needs multiple notes per
kind, a single note with subheadings is the V1 answer. Text is
user-entered and receives strong preservation guarantees; no length
limit and no content validation.

### 41.6 Relationships

Three fields of §41 are relationships to other entities, not
attributes of the plant itself. Each is stored in its own table.

| Field | Table | Target |
| --- | --- | --- |
| `companion_plants`, `plants_to_avoid`, `pollination_partners` | `plant_relationship` | Another Plant |
| `common_pests`, `common_diseases`, `common_problems`, `susceptibilities`, `resistances` | `plant_problem_relationship` | A Problem |
| `sources` | `plant_source` | A Source |

`plant_relationship` uses `PlantRelationshipType` (§29 of
`V1_DATABASE_SCHEMA.md`) to classify the nature of the link between
two plants.

`plant_problem_relationship` carries both a relationship type
(`associated_with`, `susceptible_to`, `resistant_to`) and an
optional finer response characteristic (see §27). It may also carry
an optional cultivar, growth stage and severity.

`plant_source` records that a source supports knowledge about the
plant, with an optional source relationship (direct, adapted,
derived, summarised, inferred, unknown; see
PROVENANCE_VOCABULARIES §8).

### 41.7 Reference data

`rotation_group` names a crop-rotation classification. Unlike the
other §41 fields, its values are not fixed vocabulary entries: a
rotation group is a reference entity with its own id, name, type
(system or user-defined) and garden scope. System rotation groups
are application-wide; user-defined rotation groups belong to one
garden.

A plant's association to a rotation group is stored as a
`plant_multi_value` row with kind `rotation_group` (see §41.2). The
`value_id` for that row is the rotation group's id, not a vocabulary
id.

The `rotation_group` entity itself is described in
`docs/V1_DATABASE_SCHEMA.md` and
`docs/vocabularies/PLANNING_VOCABULARIES.md` §17.

### 41.8 Fields deferred

No §41 fields are deferred. All fields listed in the vocabulary are
now implemented.

### 41.9 Adding a new §41 field

When a new field is added to this list, choose the category that
matches its shape:

- A single value per plant, drawn from a vocabulary → §41.1.
- Zero or more values per plant, drawn from a vocabulary → §41.2.
- A number, a range, or a number with a unit → §41.3.
- A recurring annual window → §41.4.
- Free text → §41.5.
- A relationship to another entity → §41.6.
- Reference data with its own identity → §41.7.

If a new field does not fit any category, that is a signal that the
category list itself needs review. Record such a case as a decision
rather than forcing the field into a category that does not match.
