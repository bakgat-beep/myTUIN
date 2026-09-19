Garden Planner & Manager — Problem Controlled Vocabularies

Document: docs/vocabularies/PROBLEM_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines controlled vocabularies used to classify garden problems, symptoms, damage, diagnostic reasoning and outcomes.

The problem system must support the distinction between:

what the gardener observed;

what problem may be present;

what evidence supports that possibility;

what action was taken;

what outcome occurred.


The application must never silently convert an observation into a diagnosis.


---

2. Design principles

Problem vocabularies must:

use stable English canonical identifiers;

remain independent of UI wording;

preserve historical records;

distinguish observation from diagnosis;

distinguish diagnosis from treatment/intervention;

support uncertain identification;

support multiple possible causes;

avoid encoding detailed biological knowledge into fixed enums;

remain small enough for maintainability.


The fundamental distinction is:

> An observation is evidence. A problem is an interpretation or classification of that evidence.




---

3. Problem categories

The broad category of a recorded problem.

V1 values:

pest
disease
nutrient_issue
water_stress
environmental_stress
physical_damage
weed
growth_problem
soil_problem
weather_damage
animal_damage
other
unknown

These categories provide broad classification only.

Actual pests, diseases and other specific problems should be represented as knowledge entities rather than vocabulary values.


---

4. Pest category

pest

Represents a problem attributed to an organism causing harm through feeding, infestation or other direct interaction.

The specific organism should be represented as a pest knowledge entity where known.

Example:

problem_category = pest
problem = aphids

The vocabulary must not contain every individual pest species.


---

5. Disease category

disease

Represents a problem attributed to a plant disease.

The specific disease should be represented as a disease knowledge entity where known.

Example:

problem_category = disease
problem = powdery_mildew

A disease diagnosis must not be inferred merely because a symptom resembles a known disease.


---

6. Nutrient issue category

nutrient_issue

Represents a suspected or observed problem associated with nutrient availability or imbalance.

Examples:

suspected nitrogen deficiency;

suspected iron deficiency;

excessive nutrient availability.


Specific nutrient problems should be represented as knowledge entities where appropriate.


---

7. Water stress category

water_stress

Represents problems associated with water availability or soil moisture.

Examples:

suspected drought stress;

waterlogging;

inconsistent moisture.


Where the underlying evidence is simply:

> Soil feels dry.



the user should record an observation rather than automatically creating a water-stress diagnosis.


---

8. Environmental stress category

environmental_stress

Represents problems associated with environmental conditions such as:

excessive heat;

cold;

wind;

inadequate light;

unsuitable humidity;

other environmental conditions.


The specific environmental factor should be represented separately where useful.


---

9. Physical damage category

physical_damage

Represents physical damage that is not necessarily attributed to a biological or environmental cause.

Examples:

broken stem;

damaged leaves;

accidental damage;

mechanical damage.



---

10. Weed category

weed

Represents unwanted plant growth.

The actual plant should be represented as a plant knowledge entity where identified.

The application should distinguish:

> Plant identified as growing where unwanted



from:

> Specific weed species identified.




---

11. Growth problem category

growth_problem

Represents an abnormal or undesirable growth outcome where the cause is not yet established.

Examples:

poor establishment;

stunted growth;

poor flowering;

poor fruiting;

abnormal growth.


This category is particularly useful when the user knows that something is wrong but the cause is uncertain.


---

12. Soil problem category

soil_problem

Represents a suspected or observed soil-related problem.

Examples:

poor drainage;

compaction;

unsuitable soil structure;

suspected salinity;

unsuitable pH.


Where the issue is directly measured, the measurement should remain the primary evidence.


---

13. Weather damage category

weather_damage

Represents damage associated with a weather event.

Examples:

frost damage;

hail damage;

wind damage;

storm damage;

excessive rain damage.


Weather observations and actual weather data should remain separate from the problem classification.


---

14. Animal damage category

animal_damage

Represents damage attributed to animals.

Examples:

browsing;

digging;

trampling;

feeding damage.


The specific animal should be represented separately when known.


---

15. Problem severity

Severity describes the apparent or assessed significance of a problem.

V1 values:

minor
moderate
major
critical
unknown

Severity is not the same as confidence.

For example:

severity = major
confidence = low

may legitimately mean:

> The suspected problem could be serious, but the diagnosis is uncertain.




---

16. Problem status

The lifecycle of a recorded problem.

V1 values:

suspected
confirmed
active
improving
resolved
recurring
unknown

These values should be used carefully.

A problem may be:

suspected

when the gardener has evidence but no reliable diagnosis.

confirmed should indicate that there is sufficient evidence or explicit user confirmation for the application's intended use.

The application should not imply scientific certainty merely because a user selected confirmed.


---

17. Problem priority

Where a problem needs to be prioritised for action:

low
moderate
high
urgent

Priority is different from severity.

For example:

severity = moderate
priority = high

may be appropriate when a moderate problem requires prompt intervention because of timing.


---

18. Symptom category

Symptoms describe observable manifestations.

Potential V1 categories:

discolouration
wilting
leaf_damage
leaf_drop
stunted_growth
abnormal_growth
poor_flowering
poor_fruiting
fruit_damage
stem_damage
root_damage
deformation
spots
lesions
mould
dieback
pest_presence
poor_establishment
water_pooling
soil_crusting
soil_compaction
other
unknown

These are symptom classifications, not diagnoses.


---

19. Symptom observation state

Where useful, symptoms may have an observed state:

present
absent
improving
worsening
unchanged
uncertain
unknown

The underlying observation date should remain separate.


---

20. Symptom severity

Where a symptom itself requires classification:

minor
moderate
severe
unknown

Symptom severity should not automatically be treated as overall problem severity.


---

21. Damage type

Potential V1 damage classifications:

feeding
chewing
sucking
boring
tearing
breakage
abrasion
trampling
browsing
scorching
frost_damage
water_damage
wind_damage
hail_damage
other
unknown

These classifications describe the apparent form of damage.

They do not necessarily identify its cause.


---

22. Diagnostic evidence direction

Where evidence is used by the recommendation or diagnostic system, it may be classified as:

supports
weakly_supports
neutral
weakly_contradicts
contradicts
unknown

These values describe the relationship between a piece of evidence and a hypothesis.

They are not confidence levels.

Example:

Evidence:
white powder visible on leaves

Hypothesis:
powdery mildew

Evidence direction:
supports

The system must retain the underlying observation.


---

23. Diagnostic reasoning state

The application may use structured reasoning states:

not_assessed
possible
plausible
unlikely
supported
contradicted
insufficient_information

These describe the current state of an inference.

They should not be presented to the user as absolute biological certainty.


---

24. Diagnosis confidence

Diagnostic confidence should use the shared confidence vocabulary from CORE_VOCABULARIES.md:

very_low
low
moderate
high
very_high
not_assessed

Confidence must be attached to the specific inference.

Example:

Possible cause:
nutrient deficiency

confidence:
low

A high-confidence observation can still result in a low-confidence diagnosis.


---

25. Possible cause type

Where the system suggests possible causes, causes may be classified as:

pest
disease
nutrient
water
soil
temperature
light
weather
physical
animal
cultivar
growing_method
unknown
multiple

These are reasoning categories.

They should not replace actual knowledge entities where a specific cause is known.


---

26. Problem outcome

The result of an intervention or period of observation may be classified as:

improved
unchanged
worsened
resolved
recurring
inconclusive
unknown

Outcome classification must be based on recorded evidence where possible.

The application should not claim an intervention caused an improvement merely because the improvement followed it.


---

27. Intervention relationship

Problems may be associated with interventions.

For example:

Problem:
aphids

Intervention:
pest-control action

Outcome:
improved

The intervention itself belongs to the Activity system.

The problem vocabulary should describe the problem and its diagnostic state, not create separate activity types for every possible treatment.


---

28. Problem cause versus problem classification

The application must distinguish:

Problem:
poor growth

Possible cause:
water stress

Evidence:
soil repeatedly observed as dry

from:

Problem:
water stress

The second should only be used when the available evidence justifies treating water stress as the problem classification.


---

29. Observation versus symptom

An observation is a recorded event.

A symptom is a classification of what was observed.

Example:

Observation:
Leaves have yellow areas.

Symptom:
discolouration

Potential problem:
nutrient_issue

Possible cause:
nitrogen deficiency

Confidence:
low

These layers must remain distinguishable.


---

30. Symptom versus diagnosis

The application must not equate a symptom with a diagnosis.

For example:

symptom = leaf_damage

does not mean:

diagnosis = disease

Multiple problems may produce the same symptom.


---

31. Diagnosis versus recommendation

A diagnostic hypothesis answers:

> What might be causing this?



A recommendation answers:

> What should I consider doing?



These are separate concepts.

Example:

Possible cause:
water stress

Recommendation:
check soil moisture before increasing watering

The recommendation should not silently convert the hypothesis into an established fact.


---

32. Multiple possible causes

The application should support multiple concurrent hypotheses.

Example:

Yellowing leaves

Possible causes:
water stress — moderate confidence
nutrient issue — low confidence
disease — low confidence

The system must not force a single diagnosis when the evidence does not support one.


---

33. Insufficient information

A diagnostic result may legitimately be:

insufficient_information

The application should explain what information would most help distinguish between the remaining possibilities.

Example:

> Several causes could explain this symptom. A closer inspection of the underside of the leaves may help distinguish them.



The user should be able to continue without providing the additional information.


---

34. Diagnostic evidence

Evidence used to assess a problem may come from:

user observations;

measurements;

photographs where supported;

plant history;

environmental information;

soil information;

published knowledge;

experimental results.


Evidence provenance should use the shared provenance system rather than creating duplicate evidence vocabularies.


---

35. User observation as evidence

A user's observation is valid evidence but must retain its provenance.

Example:

observation:
water pools after heavy rain

origin:
user_observed

confidence:
moderate

The application may use this to infer:

possible slow drainage

but should identify that as an inference.


---

36. Measurement as evidence

Measured information should remain distinct from observations and estimates.

Example:

soil pH:
6.7

information_state:
measured

method:
home_test_kit

date:
2026-08-10

A measurement may support or contradict a diagnostic hypothesis, but it does not itself become the diagnosis.


---

37. Estimated problem states

Estimates may be useful when measurements are unavailable.

Example:

Estimated drainage:
slow

information_state:
estimated

confidence:
low

The interface should make clear that this is an estimate based on available observations.


---

38. Problem history

Problems should retain their historical lifecycle.

For example:

2026-02:
suspected

2026-02:
confirmed

2026-03:
improving

2026-04:
resolved

The application must not overwrite the entire history simply because the current status changes.


---

39. Recurring problems

A recurring problem should remain distinguishable from one continuously active problem.

Example:

2025:
aphids
resolved

2026:
aphids
recurring

This allows the application to identify patterns without incorrectly treating separate occurrences as one uninterrupted event.


---

40. Problem relationship to plants

A problem may relate to:

a plant;

a plant instance;

a cultivar;

a growing space;

an area;

the whole garden.


The relationship should identify the actual subject.

The problem vocabulary should not encode location into the problem identifier.

Avoid:

aphids_bed_2

Use:

problem = aphids
target = Bed 2


---

41. Problem relationship to growing conditions

A problem may be associated with:

soil;

moisture;

light;

temperature;

drainage;

infrastructure;

weather.


These should be represented through relationships or structured observations rather than creating large numbers of problem vocabulary values.


---

42. V1 problem recording workflow

A simple V1 workflow should support:

1. Record what was observed.


2. Optionally classify the symptom.


3. Optionally identify a known problem.


4. If uncertain, record one or more possible causes.


5. Assign confidence where useful.


6. Optionally record an intervention.


7. Later record the outcome.



The user must be able to stop after step 1.

Recording a problem must never require a diagnosis.


---

43. Quick observation example

A gardener may record:

Observe → Plant

Symptom:
leaf_damage

Note:
Several leaves have holes.

The application may then offer:

Possible causes

Pest damage — moderate confidence
Physical damage — low confidence

The gardener may:

accept a possible problem;

investigate further;

record an intervention;

do nothing;

ignore the suggestions.



---

44. Recommendation interaction

A problem may affect planning recommendations.

Example:

Problem:
recent disease in Bed 2

Recommendation factor:
rotation

Effect:
increases_risk

The recommendation system should explain the relationship rather than simply displaying:

> Unsuitable.




---

45. Avoiding automatic diagnosis

The application must not automatically create a confirmed problem solely because:

a symptom was recorded;

a recommendation rule matched;

an image appears similar;

a user selected an observation category.


Where automated identification is introduced later, it must produce an inference with appropriate provenance and confidence.


---

46. Problem resolution

A problem should be considered resolved only when the user or system has an appropriate basis for that status.

The application should not infer:

resolved

merely because no new observation has been recorded.

Silence is not evidence of resolution.


---

47. Problem recurrence

A problem may be marked:

recurring

when the same or sufficiently related problem has occurred again after resolution or a distinct period of absence.

Personal history may then influence future recommendations.

Example:

> Aphid problems have occurred in this space in three recorded seasons.



The application should distinguish this personal pattern from universal horticultural knowledge.


---

48. Problem and personal learning

Problem history may contribute to garden-specific learning.

Example:

Personal garden pattern:

Poor establishment has repeatedly occurred in this bed when
drainage observations indicate prolonged pooling.

The application should identify this as a garden-specific pattern rather than a universal rule.


---

49. Localisation

All problem identifiers are canonical English identifiers.

Example:

Stored:
water_stress

English:
Water stress

Afrikaans:
Waterstremming

The UI localisation layer is responsible for the translated label.

Rules and stored records must use:

water_stress

not the translated text.


---

50. Import/export

Imports and exports must use canonical identifiers.

Example:

{
  "problem_category": "pest",
  "status": "suspected",
  "confidence": "moderate"
}

Display labels must never be used as canonical import values.

Validation should check identifiers against the applicable vocabulary.


---

51. Historical compatibility

Problem vocabulary values must remain interpretable across versions.

If a value becomes inappropriate:

1. mark it deprecated;


2. retain it for historical records;


3. define a replacement where appropriate;


4. migrate only when the semantic mapping is safe.



Existing observations and diagnoses must remain understandable even when later versions introduce more detailed classifications.


---

52. Relationship to knowledge entities

The following are generally knowledge entities rather than vocabulary values:

aphids
powdery_mildew
tomato_mosaic_virus
nitrogen_deficiency
slugs

The vocabulary classifies the type of entity or problem.

For example:

problem_category = pest
problem_entity = aphids

This keeps the controlled vocabulary small while allowing the knowledge base to grow.


---

53. V1 implementation boundary

V1 should implement the minimum classifications required for:

recording observations;

recording symptoms;

recording garden problems;

supporting possible causes;

expressing confidence;

recording severity and status;

connecting problems to interventions;

explaining how problems affect recommendations;

preserving historical problem records.


The system does not need a comprehensive diagnostic taxonomy in V1.


---

54. Recommended V1 vocabulary summary

Problem categories

pest
disease
nutrient_issue
water_stress
environmental_stress
physical_damage
weed
growth_problem
soil_problem
weather_damage
animal_damage
other
unknown

Severity

minor
moderate
major
critical
unknown

Status

suspected
confirmed
active
improving
resolved
recurring
unknown

Priority

low
moderate
high
urgent

Symptom categories

discolouration
wilting
leaf_damage
leaf_drop
stunted_growth
abnormal_growth
poor_flowering
poor_fruiting
fruit_damage
stem_damage
root_damage
deformation
spots
lesions
mould
dieback
pest_presence
poor_establishment
water_pooling
soil_crusting
soil_compaction
other
unknown

Diagnostic reasoning state

not_assessed
possible
plausible
unlikely
supported
contradicted
insufficient_information

Evidence direction

supports
weakly_supports
neutral
weakly_contradicts
contradicts
unknown

Outcome

improved
unchanged
worsened
resolved
recurring
inconclusive
unknown


---

55. Vocabulary review checklist

Before adding a problem-related vocabulary value, ask:

1. Is this a classification rather than a knowledge entity?


2. Is it an observation, symptom, diagnosis, cause or recommendation?


3. Are those concepts being kept separate?


4. Could the information instead be represented as structured data?


5. Does a relevant vocabulary already contain the concept?


6. Does the value affect application behaviour?


7. Can multiple possible causes coexist?


8. Can the information remain meaningful historically?


9. Does the value need localisation?


10. Would adding it materially improve the gardener's experience?




---

56. Guiding principle

> Record what was observed before deciding what it means.



The problem system should allow the application to say:

> Something appears to be wrong.



then:

> Here is what was observed.



then, where justified:

> These are the possible causes.



and finally:

> Here is what you might consider doing.



The gardener should never be forced to choose a diagnosis merely to record an observation.

The application should remain useful when certainty is low, preserve uncertainty explicitly, and become more informative as better evidence is collected.