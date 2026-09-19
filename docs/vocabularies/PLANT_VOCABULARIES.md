Garden Planner & Manager — Plant Controlled Vocabularies

Document: docs/vocabularies/PLANT_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines controlled vocabularies used specifically for plant-related information in the Garden Planner & Manager.

It covers classifications such as:

plant lifecycle;

plant roles;

growth stages;

growth habits;

growing methods;

propagation methods;

maturity;

frost sensitivity;

selected plant tolerance characteristics.


It does not define the identity or general knowledge of individual plants.

Plant identity, cultivars, pests, diseases and other substantive horticultural knowledge are reference data or knowledge entities.

The vocabulary system should remain deliberately small. Where information is better represented as a measurement, range, relationship or knowledge record, it should not be converted into a vocabulary merely for convenience.


---

2. Relationship to other vocabularies

Plant vocabularies operate alongside:

CORE_VOCABULARIES
    Shared application concepts

PLANT_VOCABULARIES
    Plant-specific classifications

SOIL_VOCABULARIES
    Soil classifications

ACTIVITY_VOCABULARIES
    Garden activity classifications

PROBLEM_VOCABULARIES
    Problems, symptoms and diagnostic classifications

PLANNING_VOCABULARIES
    Planning and recommendation classifications

PROVENANCE_VOCABULARIES
    Evidence and source classifications

Plant-specific concepts should remain here unless they are genuinely shared across multiple domains.


---

3. Plant lifecycle

Lifecycle describes the broad biological persistence of a plant.

Values

annual
biennial
perennial
short_lived_perennial
woody_perennial
unknown

Definitions

annual

Normally completes its biological lifecycle within one growing season or year.

biennial

Normally requires two growing seasons to complete its lifecycle.

perennial

Normally survives for multiple growing seasons.

short_lived_perennial

A perennial that normally persists for only a relatively limited number of years.

This distinction should only be used where the underlying plant knowledge supports it.

woody_perennial

A perennial with persistent woody growth.

This may be useful for planning and spatial behaviour but should not replace more detailed plant growth-form information where that is required.

unknown

Lifecycle is not currently known.


---

4. Plant role

Plant role describes the principal purpose or functional role assigned to a plant in the garden.

A plant may have more than one role.

Potential values:

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
flowering
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

These values describe roles rather than plant identity.

Not every plant role needs to be assigned to every plant.

Where several roles apply, the underlying data should support multiple values rather than forcing one primary classification.


---

5. Food-production category

Where a plant is being classified specifically for food production, the application may use:

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

This classification should describe the harvested edible part rather than the botanical classification of the plant.

For example, a plant producing an edible root should not be classified as a root merely because its entire plant is commonly called a root vegetable.

Multiple edible parts may be recorded where appropriate.


---

6. Growth stage

Growth stage describes a plant's current or relevant developmental stage.

Core values:

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

Not every plant uses every stage.

The application should not force a plant into a growth stage when the available information does not support it.

Where timing or requirements vary substantially by growth stage, the stage should be represented explicitly rather than inferred solely from the calendar.


---

7. Growth habit

Growth habit describes the general physical form and growth behaviour of a plant.

Core values:

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

A plant may have more than one applicable characteristic where necessary.

These classifications should not be used as a substitute for measured dimensions.

For example, a plant's growth habit may be spreading, while its mature width remains a structured range.


---

8. Growing method

Growing method describes the general method by which the plant is established or grown.

Core values:

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

These values may be used in knowledge and planning contexts.

Where a plant can be grown using multiple methods, the data should support multiple applicable values.

Specific infrastructure should remain represented through garden/spatial data rather than being embedded into the plant vocabulary.


---

9. Propagation method

Propagation method describes how a plant can be propagated.

Core values:

seed
division
cutting
layering
grafting
budding
offset
runner
root_division
tissue_culture
unknown

Where seed is used, it means propagation from seed rather than merely the fact that the plant produces seeds.

A plant may support multiple propagation methods.

Detailed propagation instructions belong to plant knowledge rather than this vocabulary.


---

10. Maturity classification

Maturity classifications provide broad qualitative categories where a categorical distinction is useful.

early
mid_season
late
unknown

These values must not replace actual maturity dates, days-to-maturity estimates or other structured timing information.

Where cultivar-specific maturity differs substantially, the relevant value should be associated with the cultivar rather than incorrectly assigned to the entire species.


---

11. Frost sensitivity

Frost sensitivity describes a plant's general sensitivity to frost conditions.

Core values:

very_sensitive
sensitive
moderately_sensitive
tolerant
very_tolerant
unknown

This is a qualitative classification.

Actual temperature thresholds should be stored as structured knowledge or ranges where available.

The classification must not be interpreted as a universal temperature threshold.

For example:

frost_sensitivity = sensitive

does not itself mean:

minimum_temperature = 0 °C


---

12. Heat tolerance

Where heat tolerance is sufficiently useful and supported by reliable knowledge:

very_sensitive
sensitive
moderate
tolerant
very_tolerant
unknown

This vocabulary is context-dependent.

Actual temperature requirements or limits should remain structured values or ranges.

Heat tolerance may vary with:

cultivar;

growth stage;

water availability;

humidity;

growing method;

acclimatisation.


The application should not present the classification as an absolute threshold.


---

13. Drought tolerance

Broad drought tolerance may be classified as:

very_low
low
moderate
high
very_high
unknown

This classification describes relative tolerance, not recommended irrigation quantity.

Water requirements remain separate knowledge and recommendation data.

Drought tolerance should not be interpreted as meaning a plant requires little water under all circumstances.


---

14. Waterlogging tolerance

Where useful:

very_low
low
moderate
high
very_high
unknown

This describes tolerance of prolonged saturated conditions.

It does not describe soil drainage itself.

Soil drainage belongs to the soil domain.


---

15. Salinity tolerance

Where supported by appropriate knowledge:

very_sensitive
sensitive
moderate
tolerant
very_tolerant
unknown

Actual salinity thresholds should be represented as structured knowledge or measurements where available.

This classification should not be treated as evidence that a particular garden has suitable salinity conditions.


---

16. Shade tolerance

Shade tolerance may be represented using:

very_low
low
moderate
high
very_high
unknown

This describes tolerance rather than the plant's preferred light requirement.

The application's light-requirement vocabulary and recommendation system should distinguish:

preferred_light

from:

shade_tolerance

where the distinction is materially important.

Actual light conditions in the garden remain garden/environment data.


---

17. Plant temperature classification

Where a broad temperature preference is required, use:

cool_season
warm_season
intermediate
variable
unknown

This should be used only as a broad planning classification.

Actual temperature requirements and ranges remain structured knowledge.

A plant may have different temperature requirements during:

germination;

establishment;

vegetative growth;

flowering;

fruiting.


Those requirements should not be flattened into a single classification where doing so would materially reduce accuracy.


---

18. Plant establishment difficulty

Where a broad user-facing classification is genuinely useful:

easy
moderate
difficult
unknown

This is intended for general planning and user guidance.

It should not be used as a substitute for specific propagation, germination or cultivation requirements.


---

19. Maintenance demand

A broad qualitative classification may be used for user preference and planning:

low
moderate
high
unknown

This is a summary classification only.

The application should not infer specific maintenance activities from this value alone.

Specific requirements should come from plant knowledge and applicable rules.


---

20. Plant size classification

Broad size classifications may be useful for filtering and planning:

very_small
small
medium
large
very_large
unknown

This must not replace actual mature dimensions.

Where available, mature:

height;

width;

spread;

root-zone dimensions;


should remain structured values or ranges.


---

21. Root behaviour

Where root behaviour materially affects garden planning, a broad classification may be used:

shallow
moderate
deep
spreading
unknown

This should only be used where the distinction is supported by meaningful horticultural information.

Specific root depth or spread should remain structured data where available.


---

22. Climbing support requirement

For plants whose growth habit may require physical support:

none
optional
recommended
required
unknown

This is useful to planning because it can interact with garden infrastructure.

It does not define the type of support required.

Support requirements may include relationships to:

trellis;

stakes;

fences;

frames;

other structures.


Those relationships belong in garden and infrastructure data.


---

23. Pollination requirement

Where relevant to plant planning:

self_fertile
partially_self_fertile
cross_pollination_required
pollination_partner_recommended
pollination_method_specific
unknown

This is a biological/planning classification.

The specific compatible pollination partners remain plant knowledge and relationship data.


---

24. Pollination method

Where relevant:

wind
insect
bee
bird
other_animal
water
self
multiple
unknown

This should not be used to imply that a particular pollinator is present in the user's garden.

Actual garden observations remain separate records.


---

25. Planting stock type

Where useful for recording planting material:

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

This may be associated with planting activities or plant instances.

It should not be confused with the plant's propagation methods.


---

26. Cultivar-specific classifications

Cultivars may have values that differ from the species or broader plant record.

Where a classification is known to be cultivar-specific, it should be stored at cultivar level rather than copied into the general plant record.

Examples include:

maturity
frost_sensitivity
heat_tolerance
disease_resistance
growth_habit
size
fruit_characteristics

The application should preserve the distinction between:

species/general knowledge

and:

cultivar-specific knowledge


---

27. Resistance and susceptibility

Disease and pest response should generally not be represented as a simple plant-wide vocabulary.

Instead, relationships should connect:

plant/cultivar
    ↓
problem
    ↓
response characteristic

Where a controlled classification is required for that relationship, potential values are:

highly_susceptible
susceptible
moderately_susceptible
tolerant
resistant
highly_resistant
unknown

resistant must not be interpreted as immune.

The actual problem to which the classification applies must always be retained.


---

28. Plant knowledge versus vocabulary

The following are not plant vocabulary values.

They should be represented as plant knowledge/reference data:

plant species;

accepted scientific names;

common names;

synonyms;

cultivars;

botanical families;

pests;

diseases;

companion relationships;

antagonistic relationships;

planting windows;

maturity ranges;

temperature ranges;

light requirements;

water requirements;

soil requirements;

nutrient requirements;

spacing recommendations;

harvest information;

propagation instructions.


For example:

Tomato

is reference data.

annual

is a vocabulary value.


---

29. Avoiding duplicated classifications

Plant classifications should not duplicate concepts already defined in Core.

For example, do not create another plant-specific version of:

unknown
high
moderate
low
observed
measured
estimated

unless the plant domain requires genuinely different semantics.

Where a Core vocabulary adequately represents the concept, use the Core value.


---

30. Multiple classifications

Plant records should support multiple applicable values where the real-world concept permits them.

Examples:

A plant may be:

food_crop
pollinator_support
herb

A plant may support:

seed
cutting
division

A plant may be:

climbing
vining

The system should not force users or knowledge curators to choose a single value when multiple values are valid.


---

31. Unknown information

Unknown plant information must remain explicit.

Examples:

lifecycle = unknown
frost_sensitivity = unknown
pollination_requirement = unknown
growth_habit = unknown

Unknown must not automatically mean:

average;

moderate;

unsuitable;

safe;

tolerant.


The recommendation system decides how materially an unknown value affects a particular decision.


---

32. Context-dependent classifications

Plant characteristics may depend on context.

Relevant context may include:

cultivar;

climate;

location;

hemisphere;

season;

growth stage;

growing method;

soil;

water availability.


A broad vocabulary value should therefore not override more precise contextual knowledge.

For example:

drought_tolerance = high

does not mean the plant can be grown without irrigation during establishment.


---

33. Geographic and seasonal interpretation

Plant classifications must not encode assumptions that are geographically universal when they are not.

For example:

warm_season

is a broad biological classification.

It does not itself determine the planting month.

Planting windows must be calculated using:

location;

climate;

hemisphere;

season;

plant requirements;

cultivar where relevant.



---

34. Historical compatibility

Once a plant vocabulary identifier is used in stored data:

its meaning must remain stable;

it must not be renamed merely for UI purposes;

it must not be deleted while historical records depend on it;

changes in meaning should result in a new identifier where necessary.


Deprecated values remain available for interpreting historical records.


---

35. Localisation

Plant vocabulary identifiers are canonical English identifiers.

Example:

Stored:
cool_season

English:
Cool-season

Afrikaans:
Koelseisoen

The application logic uses:

cool_season

The UI uses the localisation layer.

Vocabulary identifiers must never be replaced with translated text in stored data.


---

36. User-facing simplification

The application does not need to expose every plant classification.

For example, the underlying system may know:

frost_sensitivity = moderately_sensitive

while the primary interface may simply display:

Frost: Sensitive

An explanation or technical detail view may provide more context.

This follows the progressive-disclosure principle established in the UX specification.


---

37. Recommendation use

Plant vocabulary values may provide inputs to recommendations.

For example:

frost_sensitivity
drought_tolerance
growth_habit
lifecycle
shade_tolerance
climbing_support_requirement

However, vocabulary values should not be treated as complete recommendation rules.

A recommendation should consider the relevant contextual information and explain material limitations.


---

38. No false precision

A qualitative classification should not imply a precision that the underlying knowledge does not support.

For example:

frost_sensitivity = sensitive

should not automatically become:

frost_limit = 0 °C

unless a separate evidence-based rule or source actually provides that threshold.

Where a precise range exists, preserve the range as structured knowledge.


---

39. Vocabulary review checklist

Before adding a plant vocabulary value, ask:

1. Is this genuinely a plant classification?


2. Does it already exist in Core?


3. Should it instead be plant reference data?


4. Should it instead be a measurement or range?


5. Is it cultivar-specific?


6. Does it depend on growth stage or context?


7. Can more than one value apply?


8. Will recommendations use it?


9. Does the value remain meaningful historically?


10. Does it improve the user's experience?



If the answer to these questions is unclear, do not add the value until the underlying data model is resolved.


---

40. Summary of core plant vocabularies

Vocabulary	Purpose

Lifecycle	How long the plant normally persists
Plant role	Functional/garden roles
Food-production category	Edible plant part
Growth stage	Developmental stage
Growth habit	General physical growth form
Growing method	Broad cultivation method
Propagation method	Propagation approach
Maturity classification	Broad maturity timing
Frost sensitivity	Relative frost sensitivity
Heat tolerance	Relative heat tolerance
Drought tolerance	Relative drought tolerance
Waterlogging tolerance	Relative tolerance of saturated conditions
Salinity tolerance	Relative salinity tolerance
Shade tolerance	Relative tolerance of low light
Temperature classification	Broad cool/warm season behaviour
Establishment difficulty	Broad establishment difficulty
Maintenance demand	Broad maintenance demand
Plant size classification	Broad size category
Root behaviour	Broad root-growth characteristic
Climbing support requirement	Degree of support requirement
Pollination requirement	Broad pollination requirement
Pollination method	Broad pollination mechanism
Planting stock type	Type of planting material
Resistance/susceptibility	Relationship-specific response classification


---

41. Guiding principle

> Plant vocabularies classify plant characteristics; they do not attempt to contain plant knowledge.


The plant vocabulary system should remain:

small;

stable;

contextual;

extensible;

multilingual at the presentation layer;

compatible with historical records;

useful to recommendation and planning;

separate from detailed horticultural knowledge;

simple enough that the underlying application remains understandable.


---

42 N. Plant alias type

Values:
common_name
regional_name
synonym
plural
search_term