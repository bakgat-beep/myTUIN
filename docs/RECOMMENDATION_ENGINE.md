Garden Planner & Manager — Recommendation Engine Specification

Document: docs/RECOMMENDATION_ENGINE.md
Version: 0.2
Status: Working specification
Last updated: August 2026


---

1. Purpose

The Recommendation Engine determines how the Garden Planner & Manager evaluates possible gardening decisions and explains the results to the user.

It supports questions such as:

What could I grow here?

Is this plant suitable for this space?

When should I plant it?

Which of several options is most suitable?

What is limiting this option?

What information is missing?

What would change the recommendation?

Does my own garden history provide a reason for caution?


The engine must produce recommendations that are:

useful;

explainable;

deterministic where rules are deterministic;

explicit about uncertainty;

geographically and seasonally appropriate;

respectful of user preferences;

informed by personal history without becoming opaque;

usable offline with available local data.


The engine is an advisory system.

It does not make gardening decisions on behalf of the user.


---

2. Core principle

The Recommendation Engine should answer:

> "Given what we know about this garden, what appears suitable, why, and how confident are we?"



It must not answer merely:

> "What has the highest score?"



A numerical score may be used internally where useful, but the user-facing result must remain understandable without knowing the scoring algorithm.


---

3. Recommendation architecture

The engine should conceptually operate as:

Garden context
      +
Plant knowledge
      +
Environmental conditions
      +
Timing
      +
Spatial constraints
      +
Rotation/history
      +
Relationships
      +
User preferences
      +
Evidence/provenance
      ↓
Evaluation
      ↓
Factor results
      ↓
Rules
      ↓
Recommendation
      ↓
Explanation

The engine should keep these stages logically separate.


---

4. Recommendation inputs

A recommendation may use the following information.

4.1 Garden context

garden location;

latitude/longitude where available;

hemisphere;

climate/geographic context;

current date;

season;

local growing conditions.



---

4.2 Growing-space context

location;

geometry;

dimensions;

available area;

current plants;

planned plants;

historical planting;

light;

water;

soil;

infrastructure;

other relevant conditions.



---

4.3 Plant knowledge

Potential requirements include:

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

lifecycle;

growth stage;

growing method;

relationships;

rotation group;

cultivar characteristics.


Not every plant will have every requirement.


---

4.4 Personal history

Where relevant:

previous plantings;

observations;

harvests;

failures;

outcomes;

interventions;

experiments;

repeated patterns.


Personal history supplements general knowledge.

It must not silently replace it.


---

4.5 User preferences

Potential inputs include:

favourite plants;

plants to avoid;

food production priority;

native preference;

pollinator preference;

low-maintenance preference;

water conservation;

experimentation;

space utilisation.


Preferences influence recommendations but should remain transparent.


---

5. Missing information

Missing information is a first-class input state.

The engine must distinguish:

unknown;

not recorded;

not measured;

not applicable.


It must not silently convert missing information into:

suitable;

unsuitable;

average;

zero;

negative.



---

6. Missing information should be relevant, not exhaustive

The engine should only care about missing information when it materially affects the recommendation.

For example:

Winter sunlight: Unknown

may be irrelevant when evaluating a summer-only crop.

The engine should not lower confidence simply because information exists in the database schema but is irrelevant to the current decision.


---

7. Recommendation evaluation stages

The engine should conceptually evaluate a candidate in the following order.

Stage 1 — Applicability

Determine whether the plant/knowledge applies to the current context.

Consider:

geography;

climate;

hemisphere;

season;

growing method;

cultivar;

other applicable context.



---

Stage 2 — Hard constraints

Identify conditions that may make an option unsuitable.

Examples:

insufficient available space;

incompatible growing method;

extreme temperature incompatibility;

impossible timing;

infrastructure requirement that cannot be met.


Hard constraints should be distinguished from softer disadvantages.


---

Stage 3 — Factor evaluation

Evaluate relevant dimensions such as:

light;

water;

moisture;

soil;

pH;

fertility;

temperature;

frost;

timing;

space;

rotation;

relationships;

infrastructure.


Each applicable factor produces a structured result.


---

Stage 4 — Personal history

Determine whether the user's own garden history materially changes the recommendation.

Personal history should be supplementary evidence unless the user explicitly chooses a history-based decision rule.


---

Stage 5 — Preferences

Apply user preferences transparently.

A preference should not masquerade as horticultural unsuitability.


---

Stage 6 — Confidence

Determine how strongly the overall recommendation is supported by the available information.


---

Stage 7 — Result

Produce:

recommendation state;

factor results;

confidence;

reasons;

missing information;

relevant evidence;

possible conditions or actions.



---

8. Factor results

Individual recommendation factors use:

excellent
good
marginal
poor
unknown

These describe the result of evaluating a specific recommendation dimension.

They are not universal descriptions of all garden properties.


---

9. Factor applicability

A factor should only be evaluated when it is relevant.

For example:

A frost-sensitive annual may require frost evaluation.

A tropical perennial being considered for an indoor heated environment may require a different temperature evaluation.

The engine should not generate meaningless factor results merely because a field exists.


---

10. Overall recommendation states

The engine supports:

recommended
conditional
possible
marginal
not_recommended
unsuitable
insufficient_information
expired

These states must have clear semantics.


---

11. Recommendation state semantics

recommended

Available evidence indicates that the candidate is a strong option in the current context.

conditional

The candidate may be suitable if a specified condition is met.

possible

There is reasonable potential, but evidence or suitability is limited.

marginal

The candidate may work, but one or more meaningful factors are weak.

not_recommended

The candidate is possible but significant concerns make it a poor choice.

unsuitable

A meaningful constraint makes the candidate inappropriate for the current context.

insufficient_information

Important information is missing and the engine cannot reasonably determine suitability.

expired

The recommendation is no longer applicable because its time/context has passed.


---

12. Recommendation state is not a score

The application should not expose a single unexplained numerical score as the primary recommendation.

If an internal score is used, it is an implementation detail.

The user should see:

Good candidate

rather than:

87.4

unless a future feature has a specific reason to expose numerical scoring.


---

13. Hard constraints versus soft factors

The engine must distinguish between:

Hard constraints

Conditions that may make a candidate unsuitable.

Examples:

insufficient physical space;

impossible growing method;

severe environmental incompatibility.


Soft factors

Conditions that reduce suitability without necessarily preventing success.

Examples:

less-than-ideal light;

imperfect timing;

moderate rotation concern;

uncertain soil conditions.


This distinction is important for both recommendation results and explanations.


---

14. Factor weighting

Different factors should not automatically receive equal importance.

For example:

A severe frost incompatibility may matter more than a mild relationship disadvantage.

Factor importance should be determined by:

plant requirements;

context;

rule definitions;

severity;

evidence quality.


The weighting system must remain explicit and inspectable.


---

15. No hidden weighting

If factor weighting materially changes a recommendation, the explanation system must be able to identify the important factors.

The user does not need to see mathematical weights.

They should be able to understand:

> Temperature is currently the main limitation.



rather than receiving an unexplained overall rating.


---

16. Light evaluation

Light suitability may consider:

required light level;

available light;

seasonal variation;

time of year;

growing-space position;

available observations or measurements;

confidence in the light information.


Possible result:

Light — Good

or:

Light — Unknown

if insufficient information exists.


---

17. Water evaluation

Water suitability may consider:

plant demand;

available water;

irrigation;

rainfall information where available;

user observations;

water conservation preferences.


The engine should distinguish water availability from soil moisture where the data supports that distinction.


---

18. Moisture and drainage

Moisture suitability may consider:

observed moisture;

estimated drainage;

measured conditions;

pooling;

water retention;

plant tolerance.


Estimated soil characteristics must remain identifiable as estimates.


---

19. Soil evaluation

Soil evaluation may consider:

soil type/texture;

structure;

drainage;

organic matter;

fertility;

nutrients;

salinity;

pH;

user observations;

measured values;

estimated values.


The engine should not require complete soil information for ordinary recommendations.


---

20. pH evaluation

Where a plant has a meaningful pH requirement:

1. use a measured value when available;


2. use a justified estimate where available;


3. otherwise mark the factor unknown.



Do not invent a pH value.

If pH uncertainty materially affects the recommendation:

Potentially suitable

Soil pH is unknown.

A soil test could improve confidence.


---

21. Temperature evaluation

Temperature suitability may consider:

current conditions;

seasonal timing;

geographic context;

frost risk;

plant requirements;

growing method.


The engine must distinguish current temperature constraints from broader climate suitability.


---

22. Frost evaluation

Frost-sensitive plants require special consideration of:

local frost context where available;

season;

planned date;

plant frost tolerance;

protection/infrastructure.


A recommendation may be conditional:

> Suitable once frost risk decreases.




---

23. Timing evaluation

Timing must consider:

current date;

location;

hemisphere;

season;

plant timing requirements;

growth stage;

propagation method;

cultivar where relevant.


Northern Hemisphere guidance may need geographic or seasonal adaptation before being used in a Southern Hemisphere context.


---

24. Adapted timing

When timing has been adapted from another geographic or hemispheric source, the recommendation must retain that provenance.

The user-facing explanation may state:

> This planting window has been seasonally translated from Northern Hemisphere guidance for your garden.



The original source context remains available in technical detail.


---

25. Space evaluation

Space suitability may consider:

available geometry;

required spacing;

mature dimensions;

number of plants;

planned occupancy;

access requirements;

infrastructure conflicts.


The engine should distinguish:

Space available

from:

Space available but constrained

where appropriate.


---

26. Rotation evaluation

Rotation evaluation may consider:

previous crops;

rotation groups;

dates;

growing-space history;

plant family/group;

known rotation requirements;

confidence in historical data.


A rotation concern should normally be presented as a risk rather than an automatic prohibition unless the relevant rule defines a hard constraint.


---

27. Relationship evaluation

Relationships may include:

supportive relationships;

incompatible relationships;

known dependencies;

spatial relationships.


Relationships must be evidence-aware.

A weakly supported relationship should not be presented as established fact.


---

28. Infrastructure evaluation

Where relevant, consider:

irrigation;

support structures;

protected growing environments;

trellises;

shade structures;

other required infrastructure.


A missing infrastructure requirement may produce:

Conditional

Requires support structure.


---

29. Personal history evaluation

Personal history should be treated as a distinct evidence source.

Example:

General suitability
Good

Your garden history
Poor results with this cultivar in Bed 2
during the previous two seasons.

Recommendation
Consider another cultivar or growing space.

The user must be able to inspect the relevant records.


---

30. Personal patterns

The system may identify patterns such as:

> Carrots have had poor establishment in this bed during three recorded seasons when drainage was poor.



Such patterns must be labelled as personal garden evidence.

They are not automatically universal horticultural rules.


---

31. No opaque learning

The Recommendation Engine must not silently change its behaviour because of garden history.

If personal history materially affects a result, the output should identify:

Personal history affected this recommendation.

The user can then inspect the supporting records.


---

32. User preferences

Preferences influence recommendation presentation or selection.

For example:

Tomato
Suitability: Good

Preference:
You have marked tomatoes as a favourite.

or:

Lettuce
Suitability: Excellent

Preference:
You marked lettuce as a plant to avoid.

Result:
Excluded by preference.

The system must distinguish preference effects from horticultural suitability.


---

33. Preference exclusions

A user preference should generally not transform:

suitable

into:

unsuitable

It should instead produce a separate preference effect.

This preserves transparency.


---

34. Recommendation confidence

Confidence is separate from suitability.

Example:

Suitability
Good

Confidence
Moderate

Reason
Winter sunlight information is incomplete.

A candidate may be highly suitable based on available evidence while the confidence remains moderate because important information is uncertain.


---

35. Confidence inputs

Confidence may consider:

completeness of relevant information;

quality of source evidence;

applicability of evidence;

consistency between evidence;

measurement quality;

reliability of user observations;

strength of inference;

personal-history sample size.


Confidence should not simply equal the number of filled fields.


---

36. Confidence is contextual

The engine should distinguish, where appropriate:

observation confidence;

source confidence;

inference confidence;

recommendation confidence.


A high-confidence observation does not automatically create a high-confidence recommendation.


---

37. Evidence provenance

Recommendation results should retain sufficient provenance to explain:

what knowledge was used;

where it came from;

whether it was adapted;

whether it was user-provided;

whether it was measured;

whether it was inferred;

when it was applicable.


The primary UI need not display all of this.

It must remain available through explanation/detail views.


---

38. Recommendation reasons

Structured recommendation reasons may include:

light_match
water_match
soil_match
ph_match
fertility_match
temperature_match
timing_match
space_available
rotation_compatible
infrastructure_available
positive_history
negative_history
relationship_support
user_preference
missing_information
environmental_constraint
rotation_risk
space_constraint
soil_constraint
timing_constraint
temperature_constraint
water_constraint
perennial_maturity_constraint

The UI converts these into readable explanations.


---

39. Explanation requirements

Every significant recommendation must be explainable.

The explanation should answer, where relevant:

1. What information was used?


2. Which factors helped?


3. Which factors hurt?


4. What information is missing?


5. How confident is the result?


6. Did personal history affect it?


7. Did user preferences affect it?


8. What could change the recommendation?




---

40. Recommendation explanation example

A useful explanation might be structured as:

Why?

Light — Excellent
Your bed receives sufficient summer sunlight.

Soil — Good
Your recorded soil conditions are suitable.

Rotation — Poor
Tomatoes were grown here recently.

Temperature — Marginal
Current temperatures are still below the preferred range.

Confidence — Moderate
Winter/spring temperature information is incomplete.

The explanation should prioritise the factors that materially affected the result.


---

41. Conditional recommendations

The engine should be able to express:

Good candidate if drainage is improved.

Suitable once frost risk decreases.

Potentially suitable, but soil pH should be confirmed.

Conditions should be represented structurally rather than only as generated prose.


---

42. Missing-information recommendations

The engine may recommend obtaining information when it would materially improve the decision.

Examples:

soil pH test;

drainage observation;

sunlight observation;

temperature information.


This should be presented as an optional action.

The engine must not block ordinary planning merely because information is missing unless the lack of information genuinely makes the decision impossible to evaluate safely or meaningfully.


---

43. "Do nothing" result

The engine may return:

Nothing suitable right now.

or:

Wait before planting.

This is a legitimate recommendation result.

The explanation should state what is limiting the current options.


---

44. Recommendation candidate generation

When asked:

> What could I grow here?



the engine should first identify plausible candidates rather than evaluate every plant in the database unnecessarily.

Candidate generation may use:

season;

lifecycle;

growing method;

geographic applicability;

broad environmental requirements;

user preferences;

available space.


The exact optimisation strategy is an implementation concern.


---

45. Candidate evaluation

Each candidate is then evaluated against the current context.

Conceptually:

Candidate
    ↓
Applicability
    ↓
Constraints
    ↓
Relevant factors
    ↓
History
    ↓
Preferences
    ↓
Confidence
    ↓
Recommendation


---

46. Candidate ranking

Candidates may be ranked for presentation.

Ranking should consider:

overall suitability;

meaningful constraints;

confidence;

timing;

space;

user preferences;

personal history.


Ranking must not hide important unsuitable or excluded candidates where the user expects transparency.


---

47. Ranking versus exclusion

The engine should distinguish:

not shown because irrelevant

from:

shown but ranked lower

and:

excluded by user preference

and:

excluded by a hard constraint.

Where useful, the UI should allow the user to understand these distinctions.


---

48. Recommendation comparison

When comparing candidates, the UI should present the factors that materially distinguish them.

Example:

Tomato
Good
Excellent light
Poor rotation

Lettuce
Excellent
Good light
Good rotation

Avoid presenting dozens of identical factors.


---

49. Recommendation freshness

Recommendations are contextual.

A recommendation should be considered stale when important inputs change, such as:

date;

season;

garden conditions;

plant requirements;

growing-space geometry;

planting history;

relevant source knowledge;

user preferences.


The engine should recalculate rather than relying indefinitely on an old result.


---

50. Recommendation expiration

A recommendation may have an expired state when its valid planning window has passed.

Example:

Plant tomatoes this month.

Once the relevant period has passed, the recommendation should not continue to appear as though it were current.

Historical recommendation records may remain preserved.


---

51. Determinism

V1 recommendation rules should be deterministic.

Given the same:

inputs;

knowledge version;

rule version;

date/context;


the engine should produce the same result.

This supports:

testing;

debugging;

explainability;

historical interpretation.



---

52. Rule versioning

Recommendation rules must be versioned.

A recommendation should be traceable to the rule/knowledge version used to produce it where necessary for historical interpretation.

Changing a rule must not make historical recommendations impossible to interpret.


---

53. Rule status

Rules may have:

active
deprecated
experimental
disabled

Experimental rules must be distinguishable from established rules internally and, where relevant, in the UI.


---

54. Rule outputs

Rules may produce structured results such as:

supports
contradicts
increases_risk
decreases_risk
recommends
discourages
requires_information
no_effect

These are reasoning outputs.

They are not themselves horticultural knowledge entities.


---

55. Rule composition

Multiple rules may contribute to one recommendation.

Example:

Rule A
Light is suitable
→ supports

Rule B
Recent related crop
→ increases_risk

Rule C
Temperature currently marginal
→ decreases_suitability

The engine combines these into an overall result while preserving the individual reasons.


---

56. Conflicting evidence

Evidence may disagree.

For example:

Source A
Tomatoes prefer high fertility.

User observation
This bed has produced healthy tomatoes despite low recorded fertility.

The engine should not silently discard either source.

Where conflict materially affects a recommendation, the explanation should acknowledge uncertainty.


---

57. Source quality versus suitability

The engine must distinguish:

> How suitable is this condition?



from:

> How trustworthy is the information describing the condition?



A highly suitable value supported by weak evidence should not automatically create high confidence.


---

58. Geographic adaptation

Knowledge may originate from another geographic context.

The engine should determine whether it can be:

directly applied;

adapted;

partially applied;

rejected as inapplicable;

treated as uncertain.


Adaptation must be explicit.


---

59. Hemisphere handling

The engine must never assume:

March = spring

universally.

Seasonal reasoning must use:

geographic context;

hemisphere;

local seasonal definitions.



---

60. Growing method

Recommendations should account for the intended method where relevant.

Examples:

outdoor;

indoor;

greenhouse/protected;

container;

raised bed;

other supported methods.


A plant unsuitable outdoors may still be suitable under protected conditions.


---

61. Cultivar-specific recommendations

When cultivar information exists, the engine should prefer cultivar-specific requirements where applicable.

Otherwise, it may fall back to parent plant knowledge.

The explanation should distinguish:

Cultivar-specific information

from:

General plant information

when that distinction affects the result.


---

62. Growth-stage considerations

Where requirements vary significantly by growth stage, the engine may evaluate:

establishment;

vegetative growth;

flowering;

fruiting;

dormancy;

other supported stages.


The current stage should be used where known.

If stage is unknown and materially important, confidence may be reduced.


---

63. Relationships and recommendations

Relationship information should influence recommendations only where relevant.

Examples:

beneficial nearby plant;

incompatible neighbour;

required pollination relationship;

known rotation relationship.


The engine must not turn a weak association into a hard constraint without appropriate evidence.


---

64. Experiments and recommendations

Experiment results may contribute personal evidence.

They should be labelled as:

experimental_result

and should not automatically override broader knowledge.

Example:

> Your experiment suggests mulch reduced watering needs in this garden.



Not:

> Mulch always reduces watering requirements.




---

65. Observation-derived recommendations

Observations may inform recommendations without becoming diagnoses.

Example:

Observation:
Water pools after heavy rain.

Derived assessment:
Drainage probably slow.
Confidence: Low.

The recommendation engine can use the estimated drainage while retaining the distinction between:

observed fact;

derived assessment;

recommendation.



---

66. Recommendation auditability

For debugging and advanced inspection, the engine should be able to retain or reproduce:

inputs;

relevant factors;

rule results;

rule version;

knowledge version;

recommendation result;

confidence;

reasons.


The ordinary user should not need to see the full audit trail.


---

67. Offline operation

The engine should support offline recommendations using locally available:

plant knowledge;

garden information;

rules;

user history;

preferences.


Features requiring external live data should be optional.

For example, absence of live weather should not prevent recommendations based on stored garden information.


---

68. Live external information

If a future module supplies live information such as weather:

Weather data
      ↓
Optional recommendation input

The recommendation engine should treat it as an additional input rather than making the entire engine dependent on the service.

If unavailable, the engine should fall back to stored information where possible.


---

69. Recommendation persistence

Recommendations should not necessarily be stored as permanent truth.

The underlying evidence and decisions must be preserved.

A generated recommendation may be:

recalculated;

superseded;

expired;

retained as historical decision context.


The system must distinguish:

what the engine recommended

from:

what the gardener actually did.


---

70. User decisions

If a user acts on a recommendation, the system may record:

accepted
rejected
modified
overridden
deferred
cancelled

A UserDecision does not prove that the resulting physical activity occurred.

For example:

Recommendation:
Plant tomato.

Decision:
Accepted.

Activity:
No planting activity recorded.

The application must not infer that planting occurred.


---

71. Recommendation versus activity

These concepts remain separate:

Recommendation
"What might be suitable?"

Decision
"What did the gardener choose?"

Plan
"What does the gardener intend to do?"

Activity
"What actually happened?"

This distinction is fundamental.


---

72. Recommendation and Inbox

The Recommendation Engine may generate Inbox items when a recommendation creates a useful actionable prompt.

Example:

Inbox

Planting window approaching

Tomatoes are suitable for Bed 2.

The Inbox should not duplicate the full recommendation engine.

The user can open the recommendation for details.


---

73. Recommendation and Planner

Planner is the primary consumer of recommendation results.

Planner may ask:

What can I plant here?

The engine returns candidates.

Planner presents them in a user-friendly way.

The engine should remain independent of Planner's visual presentation.


---

74. Recommendation and Plant Library

Plant Library may request suitability information for a selected plant.

Example:

Tomato
→
Could this grow in Bed 2?

The engine evaluates the selected plant against the context.


---

75. Recommendation and Garden

Garden may request spatial recommendations.

Example:

Bed 2
→
What could grow here?

The engine evaluates the growing-space context.


---

76. Recommendation and History

History provides evidence.

The engine may use:

previous planting;

observations;

harvests;

outcomes;

experiments.


The engine should be able to identify which historical records materially affected the recommendation.


---

77. Recommendation API concept

The exact implementation may vary, but the conceptual interface should support an input resembling:

RecommendationRequest
    garden_context
    growing_space
    candidate_plant
    date/context
    optional_preferences

and return:

RecommendationResult
    status
    confidence
    factors[]
    reasons[]
    missing_information[]
    conditions[]
    evidence[]
    personal_history_effects[]
    rule_version
    knowledge_version

The exact class/schema names are implementation details.


---

78. Recommendation factor structure

Conceptually:

RecommendationFactor
    dimension
    result
    influence
    explanation
    evidence[]
    confidence

For example:

dimension: light
result: good
influence: supports
explanation: Sufficient summer sunlight recorded for this bed.


---

79. Recommendation conditions

Conditional recommendations should expose structured conditions.

Conceptually:

Condition
    type
    description
    satisfied
    required_action

Example:

type:
frost

description:
Plant after local frost risk decreases.

satisfied:
false


---

80. Recommendation evidence

Evidence may reference:

plant knowledge;

source records;

user observations;

measurements;

personal history;

experiments;

derived assessments.


Evidence should retain its provenance.


---

81. Recommendation explanation generation

User-facing explanations should be generated from structured recommendation results rather than being manually embedded in individual UI screens.

This keeps explanations:

consistent;

localisable;

testable;

reusable across screens.



---

82. Explanation language

Explanations should use cautious language where evidence is uncertain.

Prefer:

> This may indicate poor drainage.



rather than:

> Your soil has poor drainage.



when the underlying information is only an inference.

Similarly:

> Your garden history suggests...



rather than:

> This plant cannot grow here.



unless the rule genuinely establishes that constraint.


---

83. No false precision

The engine must not produce a highly precise recommendation when the underlying information is approximate.

For example:

Confidence: Low

is preferable to presenting:

82.7% suitability

from weak evidence.


---

84. Recommendation transparency levels

The user experience should support three levels.

Level 1 — Result

Good candidate

Level 2 — Explanation

Good light
Good soil
Poor recent rotation

Level 3 — Technical detail

Requirement source
Geographic applicability
Evidence type
Rule version
Confidence
Adaptation
Source date

The technical level should not clutter normal use.


---

85. Recommendation safety and restraint

The engine should avoid overstating conclusions.

Where evidence is weak:

use conditional language;

reduce confidence;

identify missing information;

offer further investigation where useful.


The engine should not claim certainty that the available evidence cannot support.


---

86. V1 recommendation scope

V1 should support recommendations for:

plant suitability;

growing-space suitability;

planting timing;

basic rotation;

basic environmental conditions;

basic soil conditions;

basic infrastructure;

relevant relationships;

personal garden history;

user preferences.



---

87. V1 recommendation non-goals

V1 does not require:

autonomous garden design;

fully automated crop scheduling;

complex predictive yield modelling;

professional agronomic optimisation;

automated chemical treatment decisions;

sophisticated machine learning;

live weather dependency;

opaque AI-generated recommendations;

community-wide statistical learning.


These may be explored later.


---

88. Testing requirements

Recommendation rules must be testable independently of the UI.

Tests should cover:

known suitable conditions;

known unsuitable conditions;

missing information;

conflicting information;

different hemispheres;

different seasons;

cultivar-specific requirements;

hard constraints;

soft factors;

rotation history;

user preferences;

personal-history effects;

adapted knowledge;

confidence behaviour;

expired recommendations.



---

89. Explainability testing

For every significant recommendation scenario, tests should verify that the system can answer:

Why was this recommended?

Why was this rejected?

What information was missing?

Which factors mattered most?

How confident is the result?

Did personal history affect it?

Did user preferences affect it?

What could change the result?


A recommendation that cannot answer these questions should not be considered complete.


---

90. Historical testing

Changing recommendation rules must not destroy the interpretability of previously stored:

decisions;

plans;

activities;

recommendation records where retained;

historical evidence.


Historical garden facts remain authoritative records of what happened, regardless of later recommendation changes.


---

91. V1 acceptance criteria

The Recommendation Engine is acceptable when it can:

1. evaluate a plant against a growing space;


2. distinguish suitable, conditional, marginal and unsuitable outcomes;


3. identify important missing information;


4. distinguish suitability from confidence;


5. explain significant factors;


6. account for season and hemisphere;


7. account for relevant spatial constraints;


8. use rotation history;


9. use personal history without treating it as universal knowledge;


10. respect user preferences transparently;


11. distinguish recommendations from decisions, plans and activities;


12. support offline operation using stored information;


13. preserve provenance of important evidence;


14. produce deterministic results from identical inputs and versions;


15. expose enough structured information for the UI to explain the result.




---

92. Design test

Every new recommendation rule should be challenged with:

1. What gardening decision does this improve?


2. What evidence supports it?


3. Is the evidence applicable to this context?


4. Is the rule deterministic and testable?


5. What happens when the information is unknown?


6. Is this a hard constraint or a soft factor?


7. Can the result be explained?


8. Can confidence be distinguished from suitability?


9. Could personal history affect the result?


10. Could user preferences affect the result?


11. Does the rule preserve historical interpretability?


12. Can it work offline?


13. Does it introduce unnecessary complexity?




---

93. Guiding principle

> The Recommendation Engine should tell the gardener what appears suitable, why it appears suitable, how confident that conclusion is, and what could change it.



It should be sophisticated internally but restrained externally.

The gardener should never have to trust an unexplained score.

The system should make uncertainty visible, preserve the distinction between knowledge and inference, and leave the final gardening decision with the gardener.