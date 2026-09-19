Garden Planner & Manager — Planning Controlled Vocabularies

Document: docs/vocabularies/PLANNING_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the controlled vocabularies specific to planning, suitability assessment, recommendations, and experiments in the Garden Planner & Manager.

It is the sole authoritative location for planning/recommendation-specific vocabulary definitions.

These vocabularies are used by:

the Planner;

recommendation logic;

suitability assessment;

planting plans;

user decisions;

experiments;

planning-related Inbox items;

recommendation explanations;

import/export;

future planning analysis.


This document does not redefine genuinely shared concepts owned by CORE_VOCABULARIES.md or provenance concepts owned by PROVENANCE_VOCABULARIES.md.


---

2. Vocabulary ownership

The planning vocabulary is responsible for concepts whose primary meaning concerns:

planning;

recommendations;

suitability;

planning factors;

recommendation explanations;

experiments.


Shared concepts remain owned by their respective domain documents.

The general ownership model is:

CORE
    Shared application semantics

GARDEN
    Garden and spatial classifications

PLANT
    Plant-specific classifications

SOIL
    Soil classifications

ACTIVITY
    Activity and logging classifications

PROBLEM
    Problems and diagnostic classifications

PLANNING
    Planning and recommendation classifications

PROVENANCE
    Evidence, source, import and contribution classifications

A concept must have one authoritative vocabulary definition.

Other documents may reference that concept but must not redefine it.


---

3. Relationship to Core vocabularies

Planning records may use shared Core vocabularies.

Examples include:

confidence
information_state
record_status
temporal_status
hemisphere
season
geographic_scope
applicability
applicability_context
user_decision

These are defined by CORE_VOCABULARIES.md.

This document must not duplicate their definitions.

For example, a recommendation may contain:

status:
conditional

confidence:
moderate

where:

conditional

is defined here, while:

moderate

is defined by Core.


---

4. Planning status

Planning status describes the lifecycle of a planned action, planting plan, or other planning record.

Values

idea
planned
scheduled
in_progress
partially_completed
completed
skipped
cancelled
expired
superseded

idea

A possible future action that has not yet been committed to as a plan.

planned

The user has decided that the action should occur, but it is not necessarily scheduled for a particular time.

scheduled

The action has an intended date or time.

in_progress

The planned action has begun but is not complete.

partially_completed

Some but not all of the planned action has been completed.

completed

The planned action has been completed.

This status does not itself replace the actual activity record.

skipped

The planned action was deliberately not carried out.

cancelled

The plan was explicitly cancelled.

expired

The planned opportunity or timing passed without completion.

superseded

The plan was replaced by another plan or decision.

A planning status describes the plan. It does not constitute evidence that a real-world activity occurred.

Actual activities must be recorded through the Activity model.


---

5. Recommendation status

Recommendation status describes the application's overall assessment of an option.

Values

recommended
conditional
possible
marginal
not_recommended
unsuitable
insufficient_information
expired

recommended

The available information supports proceeding with the option.

conditional

The option is suitable if one or more specified conditions are met.

possible

The option may be suitable, but the available evidence is not strong enough for a stronger recommendation.

marginal

The option has meaningful limitations but may still be viable.

not_recommended

The option is possible but has sufficiently significant disadvantages that the application advises against it.

unsuitable

The available information indicates that the option should not be used in the specified context.

insufficient_information

Important information is missing and the application cannot make a sufficiently reliable assessment.

expired

The recommendation was valid for a particular planning context or time window that has passed.

Recommendation status must not be interpreted as a numerical score.


---

6. Recommendation factor result

Recommendation factor result describes how an individual suitability factor affects an option.

Values

excellent
good
marginal
poor
unknown

These values may be used for factors such as:

light;

water;

soil;

pH;

fertility;

temperature;

frost;

timing;

space;

rotation;

relationships;

infrastructure;

history.


The applicable dimensions are defined separately under Suitability Dimensions.

unknown means that the factor cannot currently be assessed with sufficient information.

A factor result must not be interpreted as confidence.

For example:

Light:
excellent

Confidence:
moderate

means that the available information indicates excellent light suitability, while confidence in that assessment is only moderate.

Confidence is defined by CORE_VOCABULARIES.md.


---

7. Suitability dimensions

Suitability dimensions identify the factors that may materially influence a recommendation.

Values

light
water
moisture
soil
ph
fertility
nutrients
temperature
frost
timing
space
rotation
relationships
infrastructure
history

A recommendation does not need to evaluate every dimension.

Only materially relevant dimensions should normally be presented to the user.

For example, a recommendation may contain:

light: excellent
water: good
soil: good
rotation: poor
space: excellent

rather than displaying every possible dimension.

Unknown dimensions should remain explicit when their absence materially affects the recommendation or its confidence.


---

8. Recommendation reason types

Recommendation reason types provide structured explanations for why a recommendation received its result.

Positive or supporting reasons

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
relationship_support
user_preference

Negative or limiting reasons

rotation_risk
space_constraint
soil_constraint
timing_constraint
temperature_constraint
water_constraint
perennial_maturity_constraint
environmental_constraint
negative_history

Information-related reasons

missing_information

These are structured reasoning concepts.

The user interface should translate them into understandable explanations.

For example:

rotation_risk

might be presented as:

> Tomatoes were grown in this space recently, creating a rotation concern.



The recommendation engine should not require UI wording to be stored as part of the vocabulary.


---

9. Recommendation factor and reason distinction

Recommendation factor results and recommendation reasons serve different purposes.

A factor result answers:

> How suitable is this dimension?



For example:

rotation:
poor

A reason answers:

> Why did this affect the recommendation?



For example:

rotation_risk

Both may be present.

Example:

Rotation:
    result: poor
    reason: rotation_risk

This distinction should be retained because a single factor result may arise from different underlying reasons.


---

10. Recommendation structure

A recommendation may conceptually contain:

Recommendation
├── status
├── confidence
├── suitability factors
│   ├── dimension
│   └── factor result
├── reasons
├── conditions
├── missing information
└── affected preferences/history

Not every recommendation requires every component.

confidence uses the shared Core confidence vocabulary.

Information-state concepts use the shared Core information-state vocabulary.

Evidence and source concepts use the provenance vocabularies.


---

11. Conditional recommendations

The conditional recommendation status should be used where an option becomes more suitable if a stated condition is met.

Examples include:

good candidate if drainage is improved
suitable once frost risk decreases
potentially suitable if soil pH is confirmed

The condition should be represented as structured information where practical.

The application should explain:

what condition applies;

why it matters;

what would change the recommendation.



---

12. Insufficient information

insufficient_information should be used when missing information materially prevents a reliable recommendation.

It should not be used simply because the database contains optional fields that have not been completed.

For example:

Winter sunlight:
unknown

does not necessarily make a summer crop recommendation insufficient.

The recommendation engine should determine whether the missing information is materially relevant.


---

13. Recommendation confidence

Recommendation confidence uses the shared Core confidence vocabulary.

It must not be replaced by a planning-specific confidence scale.

Example:

status:
recommended

confidence:
moderate

This means the recommendation is favourable based on the available information, but confidence in that conclusion is moderate.

Confidence and recommendation status must remain separate.


---

14. User decisions

User decision types are defined in CORE_VOCABULARIES.md.

Planning records may use them to record the gardener's response to a recommendation.

Examples include:

accepted
rejected
modified
overridden
deferred
cancelled

The planning system must not redefine these values.

A decision records what the gardener chose.

It does not prove that the corresponding real-world action occurred.

For example:

Recommendation:
Tomatoes — recommended

User decision:
overridden

is valid.

The application must not treat the override as an error.


---

15. Recommendation result versus user decision

These concepts must remain separate.

Recommendation

What the application recommends.

Example:

recommended

User decision

What the gardener chooses.

Example:

overridden

The gardener may reasonably reject a recommendation or choose an option with a lower suitability result.

This is part of normal application behaviour.


---

16. Planning priority

Planning priority identifies how important a planning item is relative to other planning work.

Values

low
normal
high
critical

These values are specific to planning priority.

They must not be interpreted as general confidence, suitability, severity, or risk.

Where an item also has a confidence value, that confidence should use the Core vocabulary.


---

17. Rotation group type

Rotation groups classify the type of rotation grouping being used.

Values

system
user_defined

System-defined rotation groups represent established application knowledge.

User-defined rotation groups represent the gardener's own organisational or experimental grouping.

User-defined groups must not automatically become global horticultural knowledge.

Actual rotation groups are reference data rather than fixed vocabulary values.


---

18. Experiment status

Experiment status describes the lifecycle of a garden experiment.

Values

draft
planned
active
completed
cancelled
abandoned

draft

The experiment is being defined but is not yet active.

planned

The experiment has been defined and is intended to begin.

active

The experiment is currently being conducted.

completed

The planned observation period or experimental activity has finished.

cancelled

The experiment was deliberately cancelled before completion.

abandoned

The experiment began but was discontinued without reaching its intended completion.

Experiment status describes the experiment itself.

Observed results must be represented through ordinary garden records and experiment relationships.


---

19. Experiment result interpretation

Experiment results should distinguish between observation and conclusion.

The vocabulary system must not imply that an observed result is automatically a general horticultural rule.

For example:

Observed:
mulched bed required fewer recorded watering events

Interpretation:
mulching may have reduced watering requirements in this garden

The conclusion remains contextual and should retain its appropriate confidence and provenance.


---

20. Planning and activity separation

Planning vocabularies describe intended or recommended actions.

Activity vocabularies describe events that actually occurred.

For example:

Planning:
planned

Activity:
watering

A planned watering event does not become an actual watering event merely because its date has passed.

Likewise:

Planning:
completed

should not be used as the sole evidence that watering occurred.

The actual activity record remains authoritative for what happened.


---

21. Historical compatibility

Once a planning vocabulary value is used by stored data:

its meaning must remain stable;

it must not be renamed merely for presentation;

it must not be deleted while historical records depend on it;

changes must follow the project's vocabulary versioning process.


If a meaning genuinely needs to change:

1. introduce a new identifier;


2. deprecate the old identifier;


3. document the replacement;


4. migrate records where appropriate;


5. preserve historical interpretation.




---

22. Deprecation

Vocabulary values may be marked:

active
deprecated

Deprecated planning vocabulary values remain valid for interpreting historical records but should not normally be used for new records.

Deprecation must not silently alter the meaning of existing records.


---

23. Localisation

Planning vocabulary identifiers are canonical English identifiers.

They are translated only at the presentation layer.

Example:

Stored:
insufficient_information

English:
Insufficient information

Afrikaans:
Onvoldoende inligting

Rules, validation, imports and exports use:

insufficient_information

not the translated label.


---

24. Import/export

Planning vocabulary values used in import/export must use their canonical identifiers.

Example:

{
  "planning_status": "planned",
  "recommendation_status": "conditional",
  "priority": "high"
}

Display labels must not be used as canonical import values.

Import validation must reject or flag unknown planning vocabulary identifiers according to the import validation rules defined elsewhere.


---

25. UI presentation

The UI may:

group values;

hide irrelevant values;

provide explanatory text;

use icons;

provide translated labels;

present more understandable wording.


The stored vocabulary identifier must remain unchanged.

For example:

conditional

may be displayed as:

> Good candidate if certain conditions are met



without changing the underlying value.


---

26. Progressive disclosure

Not every planning vocabulary value needs to be visible at all times.

For ordinary users, a recommendation might simply show:

Good candidate

with:

Why?

revealing factor results and reasons.

Advanced users may be able to inspect:

suitability dimensions;

individual factor results;

recommendation confidence;

missing information;

reasoning factors;

personal history;

conditions.


The vocabulary architecture supports this without requiring all information to appear on the primary screen.


---

27. Vocabulary review rules

Before adding a planning vocabulary value, ask:

1. Is this genuinely a planning or recommendation classification?


2. Does the concept already exist in Core?


3. Does the concept belong in another domain vocabulary?


4. Could it instead be represented as structured data?


5. Is it actually reference data rather than a vocabulary?


6. Will recommendation logic depend on it?


7. Does it need to survive historical records?


8. Does it need localisation?


9. Does it need to appear in import/export?


10. Does the distinction materially improve planning behaviour?



A value should not be added merely because it creates a useful UI label.


---

28. Authoritative vocabulary ownership

The following planning/recommendation concepts are authoritative only in this document:

Concept	Authoritative location

Planning status	PLANNING_VOCABULARIES.md
Recommendation status	PLANNING_VOCABULARIES.md
Recommendation factor result	PLANNING_VOCABULARIES.md
Suitability dimensions	PLANNING_VOCABULARIES.md
Recommendation reason types	PLANNING_VOCABULARIES.md
Planning priority	PLANNING_VOCABULARIES.md
Rotation group type	PLANNING_VOCABULARIES.md
Experiment status	PLANNING_VOCABULARIES.md


Shared concepts remain authoritative elsewhere.

For example:

Concept	Authoritative location

Information state	CORE_VOCABULARIES.md
Confidence	CORE_VOCABULARIES.md
Record status	CORE_VOCABULARIES.md
Temporal status	CORE_VOCABULARIES.md
Hemisphere	CORE_VOCABULARIES.md
Season	CORE_VOCABULARIES.md
Geographic scope	CORE_VOCABULARIES.md
Applicability	CORE_VOCABULARIES.md
User decision	CORE_VOCABULARIES.md
Evidence type	PROVENANCE_VOCABULARIES.md
Source status	PROVENANCE_VOCABULARIES.md
Data origin	PROVENANCE_VOCABULARIES.md


No other vocabulary document should redefine these concepts.


---

29. Guiding principle

> Planning vocabularies describe what might happen, what is recommended, why it is recommended, and how planning decisions are managed.



The planning vocabulary system must remain:

small;

explicit;

explainable;

historically durable;

independent of UI wording;

independent of recommendation algorithms;

compatible with shared Core concepts;

suitable for deterministic rules;

suitable for import/export;

extensible without unnecessary complexity.