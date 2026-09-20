# Garden Planner & Manager — Plant Controlled Vocabularies

**Document:** `docs/vocabularies/PLANT_VOCABULARIES.md`
**Version:** 0.4
**Status:** Working specification
**Last updated:** September 2026

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

## 3. Plant lifecycle

Lifecycle describes the broad biological persistence of a plant.

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

| Vocabulary                 | Purpose                                    |
| -------------------------- | ------------------------------------------ |
| Lifecycle                  | Broad biological persistence               |
| Plant role                 | Functional/garden role                     |
| Food-production category   | Edible plant part                          |
| Growth stage               | Developmental stage                        |
| Growth habit               | Physical growth form                       |
| Growth rate                | Broad rate of growth                       |
| Growing method             | Broad cultivation method                   |
| Propagation method         | Propagation approach                       |
| Maturity classification    | Broad maturity timing                      |
| Frost sensitivity          | Relative frost sensitivity                 |
| Heat tolerance             | Relative heat tolerance                    |
| Drought tolerance          | Relative drought tolerance                 |
| Waterlogging tolerance     | Relative tolerance of saturated conditions |
| Salinity tolerance         | Relative salinity tolerance                |
| Light requirement          | Preferred light level                      |
| Shade tolerance            | Relative tolerance of reduced light        |
| Temperature classification | Broad cool/warm season behaviour           |
| Establishment difficulty   | Broad establishment difficulty             |
| Maintenance demand         | Broad maintenance demand                   |
| Root behaviour             | Broad root-growth characteristic           |
| Support requirement        | Degree of physical support requirement     |
| Pollination requirement    | Broad pollination requirement              |
| Pollination method         | Broad pollination mechanism                |
| Planting stock type        | Type of planting material                  |
| Resistance/susceptibility  | Problem-specific response classification   |

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

## 41. Plant database field set

| Field                     | Brief descriptor                                                                 |
| ------------------------- | -------------------------------------------------------------------------------- |
| `id`                      | Stable unique identifier for the plant record.                                   |
| `common_name`             | Primary common name used by myTUIN.                                              |
| `other_common_names`      | Other recognised common names.                                                   |
| `scientific_name`         | Accepted scientific/botanical name.                                              |
| `family`                  | Botanical family.                                                                |
| `description`             | Concise general description of the plant.                                        |
| `roles`                   | One or more plant roles from the controlled vocabulary.                          |
| `lifecycle`               | Annual, biennial or perennial classification.                                    |
| `growth_habit`            | General physical growth form/behaviour.                                          |
| `growth_rate`             | Broad growth-rate classification.                                                |
| `mature_height`           | Typical mature height, preferably as a structured range.                         |
| `mature_width`            | Typical mature width/spread, preferably as a structured range.                   |
| `root_behaviour`          | Broad root-growth classification.                                                |
| `root_depth`              | Typical root depth/range where meaningful.                                       |
| `support_requirement`     | Degree of physical support normally required or beneficial.                      |
| `support_type`            | Types of support appropriate to the plant.                                       |
| `light_requirement`       | Preferred light level.                                                           |
| `shade_tolerance`         | Tolerance of reduced light.                                                      |
| `temperature_class`       | Broad cool-/warm-season classification.                                          |
| `minimum_temperature`     | Approximate minimum temperature tolerated where sufficiently established.        |
| `maximum_temperature`     | Approximate upper temperature limit where sufficiently established.              |
| `frost_sensitivity`       | Relative frost sensitivity.                                                      |
| `heat_tolerance`          | Relative heat tolerance.                                                         |
| `drought_tolerance`       | Relative drought tolerance.                                                      |
| `waterlogging_tolerance`  | Relative tolerance of prolonged saturated conditions.                            |
| `water_requirement`       | Typical water requirement, represented as structured knowledge.                  |
| `preferred_soil`          | Suitable/preferred soil characteristics.                                         |
| `soil_drainage`           | Preferred/tolerated drainage conditions.                                         |
| `preferred_ph`            | Preferred soil pH range.                                                         |
| `fertility_requirement`   | Broad fertility/nutrient requirement.                                            |
| `growing_methods`         | Suitable cultivation methods.                                                    |
| `propagation_methods`     | Supported propagation methods.                                                   |
| `planting_stock`          | Common forms of planting material.                                               |
| `seed_depth`              | Recommended sowing depth/range.                                                  |
| `germination_temperature` | Suitable germination temperature/range.                                          |
| `germination_time`        | Typical germination time/range.                                                  |
| `transplanting_notes`     | Important transplanting considerations.                                          |
| `spacing`                 | Recommended plant spacing/range.                                                 |
| `row_spacing`             | Recommended row spacing/range where applicable.                                  |
| `container_suitable`      | Whether container cultivation is suitable.                                       |
| `container_size`          | Appropriate container size/range where relevant.                                 |
| `sowing_period`           | Suitable sowing periods, expressed contextually by region/season.                |
| `transplanting_period`    | Suitable transplanting periods.                                                  |
| `planting_period`         | Suitable planting periods.                                                       |
| `flowering_period`        | Typical flowering period.                                                        |
| `fruiting_period`         | Typical fruiting period where applicable.                                        |
| `harvest_period`          | Typical harvest period.                                                          |
| `days_to_maturity`        | Typical time from the defined starting point to maturity, where meaningful.      |
| `maturity`                | Broad early/mid-season/late classification.                                      |
| `maintenance_demand`      | Broad overall maintenance demand.                                                |
| `feeding`                 | Feeding/fertilisation requirements.                                              |
| `watering`                | Practical watering requirements or guidance.                                     |
| `pruning`                 | Pruning requirements and timing.                                                 |
| `other_maintenance`       | Other significant maintenance requirements.                                      |
| `harvestable_parts`       | Edible/harvested plant parts.                                                    |
| `harvest_indicators`      | Signs that the crop is ready to harvest.                                         |
| `harvest_method`          | How the harvest is normally performed.                                           |
| `harvest_frequency`       | Typical frequency or repeat-harvest behaviour.                                   |
| `yield`                   | Typical yield information/range where meaningful.                                |
| `storage`                 | Storage requirements and characteristics.                                        |
| `culinary_uses`           | Common culinary uses.                                                            |
| `preservation_methods`    | Suitable preservation methods.                                                   |
| `pollination_requirement` | Broad pollination requirement classification.                                    |
| `pollination_method`      | Broad pollination mechanism.                                                     |
| `pollination_partners`    | Plants/cultivars that provide useful compatible cross-pollination.               |
| `pollination_notes`       | Additional relevant pollination information.                                     |
| `common_pests`            | Common associated pests.                                                         |
| `common_diseases`         | Common associated diseases.                                                      |
| `common_problems`         | Other common cultivation problems.                                               |
| `susceptibilities`        | Problem-specific susceptibility relationships.                                   |
| `resistances`             | Problem-specific resistance/tolerance relationships.                             |
| `companion_plants`        | Plants with a documented beneficial association.                                 |
| `plants_to_avoid`         | Plants with a documented undesirable association.                                |
| `rotation_group`          | Crop-rotation grouping where applicable.                                         |
| `rotation_notes`          | Relevant rotation considerations.                                                |
| `native_status`           | Regional native/introduced status, rather than a universal plant classification. |
| `regional_notes`          | Region-specific plant information.                                               |
| `climate_notes`           | Important climate-dependent considerations.                                      |
| `toxicity`                | General toxicity/safety classification.                                          |
| `toxic_parts`             | Plant parts associated with toxicity.                                            |
| `safety_notes`            | Important safety information.                                                    |
| `sources`                 | Sources supporting the plant record and its information.                         |
