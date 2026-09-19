Garden Planner & Manager — Soil Controlled Vocabularies

Document: docs/vocabularies/SOIL_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines controlled vocabularies used for soil-related information in the Garden Planner & Manager.

It covers qualitative classifications such as:

soil texture;

drainage;

structure;

compaction;

moisture;

salinity;

qualitative nutrient status;

soil observation methods;

soil testing methods;

amendment categories.


It does not replace measured or structured soil data.

Values such as:

pH;

electrical conductivity;

temperature;

moisture percentage;

nutrient concentrations;

organic matter percentage;

drainage duration;


remain measurements, ranges or other structured data.

The objective is to allow users to record useful soil information at different levels of precision without requiring formal soil testing.


---

2. Relationship to other vocabularies

Soil vocabularies operate alongside:

CORE_VOCABULARIES
    Shared information-state, confidence and provenance concepts

GARDEN_VOCABULARIES
    Garden and spatial classifications

PLANT_VOCABULARIES
    Plant requirements and characteristics

SOIL_VOCABULARIES
    Soil-specific classifications

ACTIVITY_VOCABULARIES
    Soil-related activities and interventions

PROBLEM_VOCABULARIES
    Soil-related problems and diagnostic concepts

PLANNING_VOCABULARIES
    Planning and recommendation concepts

A soil classification should remain here only when it is genuinely soil-specific.


---

3. Soil texture

Soil texture describes the relative proportions of mineral particle-size classes.

For user-facing classification, the core values are:

sand
sandy_loam
loam
silt_loam
silt
clay_loam
sandy_clay
silty_clay
clay
unknown

Where a formal soil classification system is used, the underlying system and method should be recorded separately.

A user should not be required to identify formal soil texture if they do not know it.


---

4. Soil texture observation

For users who cannot formally determine texture, qualitative observations may be recorded as:

very_gritty
gritty
balanced
smooth
sticky
very_sticky
unknown

These are observations, not formal texture classifications.

The application must not silently convert them into a formal soil texture.

Where an estimate is produced, it should be represented as:

estimated

with appropriate confidence and explanation.


---

5. Drainage classification

Drainage describes how readily excess water leaves the soil.

Core values:

very_fast
fast
moderate
slow
very_slow
unknown

This is a qualitative classification.

Actual observations such as:

water remained for approximately 6 hours

should be stored as structured observations or measurements where possible.


---

6. Drainage observation

Useful user-observable drainage states include:

no_visible_pooling
brief_pooling
persistent_pooling
frequent_pooling
waterlogged
unknown

These describe observed conditions rather than a definitive physical drainage class.

The application may use them to generate an estimate, for example:

Estimated drainage:
Probably slow

Confidence:
Low

Why?
Based on repeated observations of persistent pooling after rainfall.

The estimate must remain distinguishable from a measured or formally classified property.


---

7. Soil structure

Broad soil structure classifications:

granular
crumb
blocky
prismatic
platy
massive
single_grain
unknown

These should generally be used where the user or a reliable source can reasonably identify the structure.

For ordinary users, the application may instead ask simpler observational questions.


---

8. Soil structure condition

A simplified condition classification may be used when appropriate:

poor
fair
good
very_good
unknown

This should not be confused with the structural type itself.

For example:

structure_type = granular
structure_condition = good

may both be valid.


---

9. Compaction

Qualitative compaction classification:

none
low
moderate
high
severe
unknown

Compaction may vary substantially by depth and location.

Where possible, the record should identify:

location;

depth;

observation date;

observation method.


A single garden-wide value should not be assumed to describe every growing space.


---

10. Soil moisture status

Current or observed soil moisture may be classified as:

very_dry
dry
slightly_dry
moist
wet
very_wet
saturated
unknown

This is an observation/state, not a precise moisture measurement.

A moisture percentage, volumetric water content or other instrument-derived value remains structured measurement data.


---

11. Moisture persistence

Where useful for understanding soil behaviour:

dries_quickly
dries_moderately
retains_moisture
retains_moisture_long
unknown

This should generally be derived from repeated observations or recorded measurements rather than assumed from texture alone.


---

12. Water-holding capacity

Where a broad qualitative classification is useful:

very_low
low
moderate
high
very_high
unknown

Water-holding capacity is distinct from current moisture.

For example:

water_holding_capacity = high
current_moisture = dry

is entirely possible.

Where measured or modelled data exists, the structured value should remain available.


---

13. Soil aeration

Where relevant to plant suitability:

poor
limited
moderate
good
very_good
unknown

Aeration is affected by several factors, including:

drainage;

compaction;

structure;

moisture.


The application should avoid presenting this classification as a direct measurement unless it actually was measured.


---

14. Soil organic matter

Where qualitative information is available without measurement:

very_low
low
moderate
high
very_high
unknown

This is an estimate/classification.

If organic matter has been measured, the measured value must remain the underlying evidence.


---

15. Soil fertility

Broad qualitative fertility classification:

very_low
low
moderate
high
very_high
unknown

Fertility is an overall interpretation and must not be treated as equivalent to a specific nutrient measurement.

For example:

fertility = low

does not establish:

nitrogen = low
phosphorus = low
potassium = low

without supporting evidence.


---

16. Nutrient status

Where individual nutrients are assessed qualitatively, use:

very_low
low
adequate
high
very_high
unknown

The vocabulary should be applied separately to the relevant nutrient.

Potential nutrients include:

nitrogen
phosphorus
potassium
calcium
magnesium
sulfur

and other nutrients where the knowledge model supports them.

Actual concentrations or laboratory categories remain structured measurement/test results.


---

17. Soil pH classification

The underlying pH must always remain numeric where measured.

A derived qualitative classification may use:

strongly_acidic
acidic
slightly_acidic
near_neutral
slightly_alkaline
alkaline
strongly_alkaline
unknown

The boundaries between categories should be defined by the recommendation/knowledge system rather than being implicitly determined by the vocabulary name.

A classification must never replace the original pH measurement.


---

18. Salinity classification

Broad qualitative salinity status:

very_low
low
moderate
high
very_high
unknown

Where salinity is measured, the underlying measurement and method must be retained.

The classification should not imply a particular electrical conductivity threshold unless the applicable knowledge rule defines it.


---

19. Soil biological activity

Where useful as a qualitative observation:

very_low
low
moderate
high
very_high
unknown

This may be informed by observations such as:

visible biological activity;

decomposition;

earthworm activity;

soil condition.


However, the application must distinguish:

observed biological activity

from:

measured biological property

and must not treat visual observations as a definitive measurement of soil biology.


---

20. Earthworm observation

Where users record visible earthworm activity:

none_observed
low
moderate
high
very_high
unknown

This is an observation, not a direct measure of overall soil health.

The absence of observed earthworms must not automatically be interpreted as poor soil health.


---

21. Soil surface condition

Useful observational classifications:

bare
mulched
crusted
eroded
vegetated
waterlogged
unknown

A growing space may have multiple relevant conditions over time.

Historical observations should therefore be preserved rather than overwritten.


---

22. Erosion status

Broad erosion classification:

none_observed
minor
moderate
severe
unknown

This describes observed erosion.

Actual soil loss measurements, if available, remain structured data.


---

23. Soil test status

Where the user is recording whether formal testing has occurred:

not_tested
testing_planned
sample_taken
test_pending
tested
unknown

This describes the testing process rather than the soil property itself.


---

24. Soil observation method

Methods for qualitative or observational soil information may include:

visual_observation
touch_test
digging_observation
rainfall_observation
drainage_observation
plant_response
historical_record
user_estimate
other
unknown

The method should be stored where it materially affects interpretation.


---

25. Soil test method

Potential formal or semi-formal methods:

laboratory_test
home_test_kit
electronic_meter
field_test
professional_assessment
published_analysis
imported_result
unknown

The specific test performed should be stored separately where necessary.

For example:

method = laboratory_test
property = ph
result = 6.7

The vocabulary does not attempt to encode every possible commercial test.


---

26. Soil sample source

Where soil measurements are associated with samples:

user_collected
professional_collected
laboratory_collected
imported
unknown

This is provenance information and may overlap conceptually with Core/Provenance vocabularies.

Where the Core provenance vocabulary adequately represents the concept, it should be reused rather than duplicated.


---

27. Soil amendment category

Broad amendment categories:

organic_matter
compost
manure
mulch
lime
sulfur
fertiliser
biochar
gypsum
soil_conditioner
other
unknown

These are categories of interventions/materials.

Specific products, formulations and application rates are not vocabulary values.


---

28. Amendment purpose

Where an intervention needs a broad intended purpose:

improve_structure
improve_drainage
increase_water_retention
increase_organic_matter
adjust_ph
supply_nutrients
reduce_compaction
reduce_erosion
reduce_salinity
suppress_weeds
protect_surface
other
unknown

The purpose represents the intended effect.

It does not prove that the intervention achieved that effect.

Actual outcomes should be recorded separately.


---

29. Soil condition interpretation

Where the application needs a broad overall interpretation:

very_poor
poor
fair
good
very_good
unknown

This should be used sparingly.

The application should prefer showing the individual relevant soil properties rather than reducing the entire soil condition to one score.

A single overall soil-quality classification can easily create false precision.


---

30. Soil suitability factor

When soil is being assessed specifically for a plant or planning decision, individual factors may use:

excellent
good
marginal
poor
unknown

This follows the recommendation-factor semantics defined in Core.

It should not create a separate soil-specific suitability scale.


---

31. Soil observations versus soil classifications

The system must preserve the distinction between:

Observation

and:

Classification

For example:

Observation:
Water pooled for approximately 6 hours.

Estimated classification:
slow drainage

Confidence:
low

The observation is evidence.

The classification is an interpretation.

Neither should be silently converted into the other.


---

32. Soil measurements versus estimates

The application must distinguish:

Measured:
pH = 6.7

from:

Estimated:
pH = probably mildly acidic

and:

Observed:
soil appears acidic based on plant response

These should retain their respective information states and confidence.

The application must never present an estimate as though it were a measured fact.


---

33. Progressive precision

Soil information should support increasing precision over time.

For example:

Unknown
    ↓
Observed pooling
    ↓
Estimated slow drainage
    ↓
Measured drainage duration
    ↓
Repeated measurements

Historical observations should remain available.

A later measurement should supplement rather than erase the earlier observation.


---

34. Spatial scope

Soil information should be associated with an appropriate spatial context.

Potential scopes include:

garden
area
growing_space
sub_area
sample_location

The exact spatial entities are defined by the garden/data model.

A soil measurement from one bed must not automatically be treated as though it applies to the entire garden.


---

35. Depth

Soil properties may vary with depth.

Where relevant, records should support structured depth information rather than creating vocabulary values such as:

shallow
medium
deep

unless the concept is specifically a qualitative soil classification.

For example:

sample_depth = 300 mm

is preferable to:

sample_depth = medium


---

36. Soil classification versus garden condition

The application must distinguish between:

soil property

and:

current garden condition

For example:

drainage = slow

describes a soil/site characteristic.

current_moisture = saturated

describes a current condition.

The two may be related but are not interchangeable.


---

37. Unknown and unavailable information

Unknown information must remain explicit.

Examples:

texture = unknown
ph = unknown
salinity = unknown
fertility = unknown
drainage = unknown

The application should determine whether the missing value actually matters to the current task.

Users should not be forced to obtain soil information merely because the database can store it.


---

38. Localisation

Soil vocabulary identifiers are canonical English identifiers.

Example:

Stored:
sandy_loam

English:
Sandy loam

Afrikaans:
Sanderige leem

The database, rules and import/export systems use:

sandy_loam

The UI obtains the appropriate translated label through localisation.


---

39. Historical compatibility

Once a soil vocabulary identifier is used in stored data:

its meaning should remain stable;

it should not be renamed merely for presentation;

it should not be deleted while historical records depend on it;

a materially different concept should receive a new identifier.


Deprecated values remain interpretable for historical records.


---

40. Recommendation use

Soil vocabularies may inform:

plant suitability;

planting recommendations;

rotation decisions;

intervention suggestions;

investigation prompts.


However, a single soil classification should not automatically determine a recommendation.

Where relevant, the recommendation engine should consider:

plant requirements;

cultivar;

growth stage;

current conditions;

soil measurements;

observations;

confidence;

geographic context;

user preferences.



---

41. Avoiding false precision

The application must not imply that a qualitative soil classification is equivalent to a precise measurement.

For example:

drainage = slow

does not establish:

drainage_time = 6 hours

unless the latter was actually measured or deliberately estimated.

Likewise:

fertility = high

does not establish specific nutrient concentrations.


---

42. Vocabulary duplication rules

Before adding a soil value, check whether the concept belongs elsewhere.

Examples:

unknown
measured
estimated
high
moderate
low

may already be represented by Core concepts.

Similarly,:

excellent
good
marginal
poor
unknown

should use the existing recommendation-factor vocabulary where the semantics are the same.

Do not create duplicate scales simply because they are being displayed on a soil screen.


---

43. Vocabulary review checklist

Before adding a soil vocabulary value, ask:

1. Is this genuinely a soil classification?


2. Is it already represented in Core?


3. Should it instead be an observation?


4. Should it instead be a measurement?


5. Should it instead be an estimate or inference?


6. Is it a property or a current condition?


7. Does it vary spatially or by depth?


8. Can more than one value apply?


9. Will recommendations depend on it?


10. Does it preserve useful historical information?


11. Does it reduce complexity for the user rather than increase it?



If the answer is unclear, do not add the value until the underlying representation is resolved.


---

44. Summary of soil vocabularies

Vocabulary	Purpose

Soil texture	Broad mineral texture classification
Texture observation	User-observable texture characteristics
Drainage	General drainage behaviour
Drainage observation	Observable pooling/water behaviour
Soil structure	Structural classification
Structure condition	Broad structural condition
Compaction	Degree of compaction
Moisture status	Current qualitative moisture state
Moisture persistence	General moisture-retention behaviour
Water-holding capacity	Broad capacity classification
Soil aeration	Broad aeration condition
Organic matter	Qualitative organic matter status
Soil fertility	Broad overall fertility
Nutrient status	Qualitative individual nutrient status
pH classification	Derived qualitative pH category
Salinity	Qualitative salinity status
Biological activity	Qualitative biological activity
Earthworm observation	Observed earthworm activity
Surface condition	Qualitative soil-surface state
Erosion	Observed erosion severity
Soil test status	State of soil testing
Observation method	Method used for soil observation
Test method	Method used for soil testing
Sample source	Source of soil sample
Amendment category	Broad amendment type
Amendment purpose	Intended intervention purpose
Overall soil condition	Optional broad summary
Soil suitability factor	Recommendation-specific soil assessment



---

45. Guiding principle

> Record what the gardener knows, distinguish it from what the system estimates, and preserve the underlying evidence.



The soil vocabulary system should make it possible to move naturally from:

“I don't know my soil.”

to:

“I have noticed water pooling.”

to:

“The application estimates slow drainage.”

to:

“I measured drainage duration.”

without forcing the gardener to pretend that an observation is a measurement.

The vocabulary architecture should therefore remain:

simple;

explicit about uncertainty;

compatible with measurements;

spatially aware;

historically durable;

useful to recommendations;

accessible to non-experts;

extensible without becoming a soil-science database in its own right.