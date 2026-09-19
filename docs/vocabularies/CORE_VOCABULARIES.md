Garden Planner & Manager — Core Controlled Vocabularies

Document: docs/vocabularies/CORE_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines controlled vocabularies that are genuinely shared across multiple modules of the Garden Planner & Manager application.

These are foundational application concepts.

They must therefore be:

stable;

unambiguous;

machine-readable;

language-independent;

suitable for historical records;

suitable for import/export;

sufficiently extensible;

usable by multiple application modules.


Feature-specific vocabularies belong in their respective domain vocabulary documents.

In particular, planning and recommendation vocabularies are defined in:

docs/vocabularies/PLANNING_VOCABULARIES.md


---

2. Canonical Data Language

2.1 English is canonical

The underlying application data model always uses English canonical identifiers.

Examples:

perennial
high
observed
planned

These identifiers are not user-interface translations.


---

2.2 Localisation is presentation-only

The application may eventually support:

English;

Afrikaans;

other languages.


The underlying stored identifier remains unchanged.

Example:

Stored:
perennial

English UI:
Perennial

Afrikaans UI:
Meerjarige plant

The database always retains:

perennial

Changing language must not require migrating or rewriting garden data.


---

2.3 Stable identifier format

Canonical vocabulary IDs should normally:

be lowercase;

use snake_case;

contain no spaces;

contain no translated words;

remain stable once released.


Examples:

short_lived_perennial
partial_shade
very_high
user_observation
not_applicable


---

3. Active/deprecated status

Vocabulary values should conceptually have a lifecycle.

active

May be used for new records.

deprecated

Retained for interpreting existing records but should not normally be used for new records.

A deprecated value should not be physically deleted when historical records depend on it.


---

4. Information-State Vocabulary

Information state describes what kind of information has actually been obtained.

Values

unknown
not_recorded
not_applicable
not_measured
observed
measured
estimated
inferred

unknown

Relevant information is currently unavailable.

not_recorded

The information may have been available but was not recorded.

not_applicable

The concept does not meaningfully apply.

not_measured

A measurement could be made, but has not been made.

observed

The information was directly observed.

measured

The information was obtained through measurement.

estimated

The value is an approximation rather than a direct measurement.

inferred

The value was derived from other information.

These states must not be treated as equivalent.

For example:

unknown

must not automatically be interpreted as:

not_suitable

and:

estimated

must not be treated as:

measured


---

5. Confidence Vocabulary

Confidence represents how strongly a particular claim or assessment is supported.

Values

very_low
low
moderate
high
very_high
not_assessed

Confidence is contextual.

The application should distinguish where applicable:

observation confidence;

source confidence;

inference confidence;

assessment confidence.


A high-confidence observation does not automatically produce a high-confidence inference.

Confidence must therefore be associated with the claim or assessment to which it applies rather than treated as a general property of a record.


---

6. Record Status Vocabulary

General record lifecycle:

draft
active
inactive
deprecated
archived

deleted should generally not be used as a meaningful historical record state for important garden information.

Where data must no longer be visible in ordinary workflows, archival or another appropriate lifecycle state should normally be preferred.


---

7. Temporal Status Vocabulary

Where a record needs an explicit temporal classification:

past
current
upcoming
historical
unknown

Temporal status is not a substitute for actual dates.

Where exact dates are available, they remain the authoritative temporal information.

The vocabulary is intended for classification and presentation rather than replacing structured temporal data.


---

8. Hemisphere Vocabulary

Canonical values:

northern
southern
equatorial
global
unknown

Hemisphere may affect:

season interpretation;

planting windows;

growing guidance;

adapted knowledge;

seasonal analysis.


The hemisphere of a garden should normally be derived from or associated with its geographic context where possible rather than manually entered when reliable location information already establishes it.


---

9. Season Vocabulary

Canonical conceptual seasons:

spring
summer
autumn
winter
year_round
unknown

Season is interpreted according to geographic context and hemisphere.

The application must not assume that the same months represent the same season everywhere.

Season is a conceptual classification. Actual dates and planting windows remain structured temporal data.


---

10. Geographic Scope Vocabulary

Defines the geographic level to which information applies.

global
hemisphere
country
region
climate_zone
locality
garden
unknown

The actual location is represented through structured geographic data rather than treating every place as an enum.

Geographic scope describes the intended applicability of information; it does not replace coordinates, addresses, regions, or other structured geographic representations.


---

11. Evidence Type Vocabulary

Defines the nature of supporting evidence.

verified_reference
curated_guidance
published_research
institutional_guidance
expert_guidance
community_observation
user_observation
imported_dataset
experimental_result
derived_analysis

These describe evidence origin/type, not confidence.

A particular evidence type does not automatically imply a particular confidence level.

For example:

published_research

does not automatically mean:

very_high

and:

user_observation

does not automatically mean:

low

Confidence must be assessed separately.


---

12. Source Status Vocabulary

Defines the current state of a Source record.

unverified
verified
partially_verified
outdated
superseded
disputed
unavailable

Source status describes the state of the source itself.

It must not be confused with:

evidence type;

source relationship;

confidence;

applicability.



---

13. Source Relationship Vocabulary

Describes how a knowledge record relates to a source.

direct
adapted
derived
summarised
inferred
unknown

For example:

Northern Hemisphere guidance
    ↓
adapted
    ↓
Southern Hemisphere application

Adapted records must retain their source context.

The relationship describes how the application uses the source; it does not itself establish whether the resulting information is correct or highly confident.


---

14. Data Origin Vocabulary

Describes how information entered the system.

built_in
user_created
user_observed
user_measured
user_imported
external_dataset
community_contributed
curated
system_derived
system_inferred

Data origin describes the pathway by which information entered the application.

It should not be used as a substitute for evidence type or confidence.

For example:

user_measured

describes how the data entered the system.

The associated evidence may additionally record:

user_observation

and:

high

confidence.


---

15. Applicability Vocabulary

Describes how well information applies to a particular context.

applicable
partially_applicable
not_applicable
unknown

Applicability should be assessed against a defined context.

For example, general tomato guidance may be:

applicable

to one garden while being:

partially_applicable

to another because of different climate, growing method or conditions.

Applicability does not represent recommendation suitability.

Recommendation-specific suitability classifications belong in:

PLANNING_VOCABULARIES.md


---

16. Applicability Context Vocabulary

Describes what type of context materially affects applicability.

general
climate
geography
season
growth_stage
growing_method
cultivar
soil
environment
multiple
unknown

This vocabulary describes the context in which applicability is being considered.

It does not describe the outcome of a recommendation.


---

17. Knowledge Maturity Vocabulary

Used by the future local/community knowledge system.

personal_observation
potential_contribution
aggregated_observations
emerging_pattern
local_finding
reviewed_guidance
established_knowledge

These represent levels of evidential maturity, not automatic promotion.

A single personal observation must not automatically become established knowledge.

Movement between maturity levels requires appropriate review, aggregation, evidence or other defined validation processes.


---

18. Contribution Status Vocabulary

For future optional knowledge-sharing functionality:

private
eligible
offered
approved_by_user
submitted
under_review
accepted
rejected
withdrawn
not_available

Contribution must always be explicit and user-controlled.

Private garden information must not silently become shared knowledge.

A contribution status describes the state of a potential contribution, not whether the underlying garden information is true or reliable.


---

19. Privacy/Location-Sharing Vocabulary

For future contribution functionality:

exact
property
locality
region
country

These values define the precision of geographic information that may be shared.

Exact location should remain private by default.

The sharing precision should not alter the underlying garden location stored for the user's own garden.


---

20. Data Modification Reason Vocabulary

Used when significant changes to stored records need to be classified.

user_created
user_updated
imported
system_derived
migration
curation
unknown

This vocabulary records why a significant data change occurred.

It does not replace the record's ordinary lifecycle status.

Where historical corrections are important, the modification reason may be accompanied by an audit/history record.


---

21. Import Validation Severity

For bulk import:

information
warning
error
critical

information

Informational only.

warning

Potential issue that does not necessarily prevent import.

error

Affected record cannot safely be imported without correction or explicit handling.

critical

The import process cannot safely continue.

Import validation must occur before modifying the actual garden or knowledge data.


---

22. Import Record Status

Potential import statuses:

pending
validated
warning
rejected
imported
updated
skipped
conflict

Import validation must occur before modifying the actual garden or knowledge data.

An import record status describes what happened to the imported record during the import process.

It does not replace the status of the resulting garden or knowledge record.


---

23. User Decision Types

Core decision classifications:

accepted
rejected
modified
overridden
deferred
cancelled

A UserDecision records the gardener's choice.

It does not prove that the resulting activity occurred.

For example:

Recommendation
    ↓
accepted
    ↓
Plan
    ↓
actual activity

The acceptance of a recommendation and completion of the resulting activity remain separate facts.


---

24. Rule Status

Shared rule lifecycle concepts may be used by deterministic reasoning modules:

active
deprecated
experimental
disabled

active

The rule may be used in ordinary application reasoning.

deprecated

Retained for historical interpretation but should not normally be used for new reasoning.

experimental

The rule is being evaluated and must not silently be represented as established knowledge.

disabled

The rule is retained but currently prevented from producing ordinary application results.

Rule-specific inputs and outputs belong to the domain that owns the rule.


---

25. General-Purpose Principle

A vocabulary should exist only where controlled terminology materially benefits:

consistency;

reasoning;

searching;

filtering;

importing;

exporting;

reporting;

historical interpretation;

localisation.


Do not create a vocabulary merely because a set of labels exists in the UI.

Feature-specific concepts should remain in the vocabulary document for the feature or domain that owns them.


---

26. Numeric Values Must Remain Numeric

Do not replace inherently numeric information with a categorical vocabulary.

Examples:

pH = 6.7
temperature = 21.5 °C
sunlight = 6.5 hours
bed_length = 3 m
harvest_weight = 1.2 kg

Categories may be derived for interpretation:

pH_class = slightly_acidic

but the original measurement remains the underlying evidence.


---

27. Historical Compatibility

Once a vocabulary identifier is used by stored data:

do not change its meaning casually;

do not rename it merely for presentation;

do not delete it without a migration strategy;

preserve its historical interpretation.


If a concept truly needs to change:

1. create a new identifier;


2. deprecate the old identifier;


3. define the replacement;


4. migrate records where appropriate;


5. retain historical compatibility.



A vocabulary identifier is part of the data contract once it is used in persisted data or interchange formats.


---

28. Localisation Architecture

The relationship between canonical data and UI localisation is:

Canonical data
    ↓
Stable English identifiers
    ↓
Localisation layer
    ↓
User-facing language

For example:

Stored:
partial_shade

English:
Partial shade

Afrikaans:
Gedeeltelike skadu

The recommendation engine, database and import/export system use:

partial_shade

not the translated labels.


---

29. Localisation Must Not Affect Business Logic

Rules must never depend on:

"Partial shade"
"Gedeeltelike skadu"

They must depend on:

partial_shade

Changing language must therefore have no effect on:

recommendations;

calculations;

validation;

historical interpretation;

database relationships;

imports;

exports.



---

30. Vocabulary Extensibility

New values may be added when genuinely required.

Before adding a new value, ask:

1. Is the distinction actually useful?


2. Does an existing value already represent the concept?


3. Is the concept numeric instead?


4. Is the concept better represented as a relationship?


5. Is it actually a UI label rather than a data classification?


6. Will application logic depend on it?


7. Does it need to survive across future versions?



New values should be added to the vocabulary document that owns the concept.

Do not move a feature-specific value into Core merely because multiple modules currently use it.


---

31. Core Vocabulary Should Remain Small

Only genuinely shared concepts belong in the Core vocabulary.

Feature-specific values belong to their feature/domain vocabulary documents.

This protects the Core from becoming unnecessarily coupled to future modules.

In particular:

CORE
    Shared application semantics

PLANNING
    Planning and recommendation semantics

GARDEN
    Garden and spatial semantics

PLANT
    Plant semantics

SOIL
    Soil semantics

ACTIVITY
    Activity and logging semantics

PROBLEM
    Problem and diagnostic semantics

PROVENANCE
    Provenance and knowledge-sharing semantics

The same Core vocabulary may of course be referenced by multiple domain modules.


---

32. Core Versus Feature Vocabularies

The division is:

CORE
    Shared application semantics

GARDEN
    Garden/spatial classifications

PLANT
    Plant-specific classifications

SOIL
    Soil classifications

ACTIVITY
    Activity/logging classifications

PROBLEM
    Problem/diagnostic classifications

PLANNING
    Planning/recommendation classifications

PROVENANCE
    Source/evidence/import/contribution classifications

Core should contain only concepts whose meaning remains useful independently of any one feature.

For example:

high

may be a valid value in several contexts, but the existence of a shared word does not make every high classification the same vocabulary.

Context-specific concepts should remain context-specific.


---

33. Relationship to Domain Vocabularies

The Core vocabulary provides shared primitives used by domain vocabularies.

For example:

CORE
    confidence
    information_state
    applicability

PLANNING
    recommendation_status
    recommendation_factor_result
    suitability_dimension

SOIL
    drainage
    soil_texture
    soil_observation_method

ACTIVITY
    activity_type
    watering_method
    harvest_category

Domain vocabularies may reference Core values where appropriate rather than redefining them.

For example, a planning assessment may use:

confidence = moderate

without defining another Planning-specific confidence vocabulary.


---

34. Rule Ownership

Core defines the lifecycle of rules but does not define domain-specific rule outputs.

For example:

CORE
    rule_status

PLANNING
    recommendation_status
    recommendation_factor_result
    recommendation_reason_type

PROBLEM
    diagnostic reasoning states

This prevents the Core vocabulary from becoming coupled to the recommendation engine or diagnostic system.


---

35. Vocabulary Review Requirement

Before a vocabulary is considered final, it should be checked against:

the data model;

database schema;

import/export;

recommendation rules;

localisation;

historical data;

future module boundaries;

user experience.


A vocabulary should be removed or simplified if it introduces complexity without delivering useful application behaviour.

When a proposed value appears to belong in Core, first ask whether it can remain within an existing domain vocabulary without reducing consistency.


---

36. Guiding Principle

> Store stable English concepts. Translate only what the user sees.



The Core vocabulary architecture must allow the application to remain:

language-independent internally;

multilingual at the UI;

backward compatible;

explainable;

importable/exportable;

modular;

extensible;

historically durable.


Core should remain deliberately small.

Its purpose is to provide stable shared semantics, not to become a catalogue of every controlled value used by the application.