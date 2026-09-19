Garden Planner & Manager — Vocabulary Index

Document: docs/VOCABULARY_INDEX.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines how controlled vocabularies are organised and used by Garden Planner & Manager.

Controlled vocabularies provide stable machine-readable terminology shared between:

the database;

application modules;

user interface;

recommendation and reasoning rules;

import/export;

reporting;

future knowledge systems;

localisation.


The vocabulary system should remain smaller and more stable than the application's broader knowledge base.


---

2. Canonical Language

Application data uses stable English canonical identifiers.

Example:

perennial
partial_shade
high
user_observation

User-facing labels are provided separately through localisation.

Example:

Canonical:
perennial

English:
Perennial

Afrikaans:
Meerjarige plant

The database retains the canonical identifier regardless of the selected interface language.

Changing language must not require database migration.


---

3. Vocabulary Types

Each controlled concept should be classified as one of the following.

3.1 Fixed enum

A small, stable set whose semantics are part of application behaviour.

Examples:

hemisphere;

activity status;

recommendation state.


New values require deliberate design review.

3.2 Reference data

Structured knowledge that can grow independently of application code.

Examples:

plants;

cultivars;

sources;

interventions;

rotation groups;

plant roles.


Reference data is not necessarily a vocabulary. It should be represented as an entity when it has its own attributes, relationships, provenance or history.

3.3 User-extensible reference data

System-provided values exist, but users may create additional garden-specific values where appropriate.

Examples:

custom rotation groups;

custom categories;

organisational tags.


User-defined values must remain distinguishable from system knowledge and must not automatically become global knowledge.

3.4 Structured value

Information that should be stored as a number, range, date, quantity, measurement or other structured value.

Examples:

pH;

temperature;

dimensions;

sunlight hours;

rainfall;

quantity;

coordinates.


A categorical interpretation may be derived for presentation or reasoning, but must not replace the underlying structured value.


---

4. Vocabulary Ownership

Domain vocabularies are separated to prevent the Core vocabulary from becoming unnecessarily large or tightly coupled to individual modules.

docs/
├── VOCABULARY_INDEX.md
└── vocabularies/
    ├── CORE_VOCABULARIES.md
    ├── GARDEN_VOCABULARIES.md
    ├── PLANT_VOCABULARIES.md
    ├── SOIL_VOCABULARIES.md
    ├── ACTIVITY_VOCABULARIES.md
    ├── PROBLEM_VOCABULARIES.md
    ├── PLANNING_VOCABULARIES.md
    └── PROVENANCE_VOCABULARIES.md

A concept belongs in Core only when it is genuinely shared across multiple domains.


---

5. Core Vocabularies

File: docs/vocabularies/CORE_VOCABULARIES.md

Contains foundational concepts shared across multiple modules, including:

information state;

confidence;

record lifecycle;

temporal state;

hemisphere;

season;

geographic scope;

applicability;

planning status where shared;

user decision;

recommendation/status concepts where genuinely shared;

other stable application-wide concepts.



---

6. Garden Vocabularies

File: docs/vocabularies/GARDEN_VOCABULARIES.md

Contains garden and spatial classifications such as:

area type;

growing-space type;

spatial object type;

geometry type;

infrastructure type;

access classification;

map layer type;

spatial relationship type.


These values must remain independent of any particular map-rendering technology.


---

7. Plant Vocabularies

File: docs/vocabularies/PLANT_VOCABULARIES.md

Contains plant-specific classifications such as:

lifecycle;

plant role;

growth stage;

growing method;

propagation method;

growth habit;

maturity classification;

frost sensitivity;

plant-specific tolerance classifications.


Plant identity and cultivar identity are reference data, not vocabulary values.


---

8. Soil Vocabularies

File: docs/vocabularies/SOIL_VOCABULARIES.md

Contains classifications such as:

soil texture;

drainage;

soil structure;

compaction;

moisture status;

salinity;

qualitative nutrient status;

soil observation method;

soil test method;

amendment category.


Numeric soil measurements remain structured values.


---

9. Activity Vocabularies

File: docs/vocabularies/ACTIVITY_VOCABULARIES.md

Contains activity classifications such as:

activity type;

planting type;

watering method;

application method;

pruning category;

harvest category;

intervention category;

activity status.


The Activity entity remains the stable event primitive.


---

10. Problem Vocabularies

File: docs/vocabularies/PROBLEM_VOCABULARIES.md

Contains classifications such as:

problem category;

pest category;

disease category;

damage category;

severity;

failure category;

symptom classification;

diagnostic evidence direction;

diagnostic reasoning state.


Specific pests, diseases, symptoms and problems are knowledge entities when they require their own identity, relationships or attributes.


---

11. Planning Vocabularies

File: docs/vocabularies/PLANNING_VOCABULARIES.md

Contains planning and recommendation classifications such as:

timing type;

planning status;

recommendation status;

suitability classification;

suitability dimension;

recommendation reason type;

experiment status;

planning priority.


Specific rotation groups are reference data rather than vocabulary values.


---

12. Provenance Vocabularies

File: docs/vocabularies/PROVENANCE_VOCABULARIES.md

Contains classifications relating to evidence and data history, including:

evidence type;

source type;

source status;

source relationship;

data origin;

knowledge maturity;

contribution status;

privacy/location-sharing level;

validation status;

verification status;

modification reason.



---

13. Cross-Vocabulary Rules

13.1 Avoid duplicate concepts

Before adding a value, check whether the concept already exists.

Do not create multiple independent vocabularies for the same semantic concept merely because it appears in different screens.

However, similar words must not automatically be treated as equivalent.

For example:

high confidence
high water demand
high priority
high salinity

are different concepts and may require different vocabularies.


---

13.2 Keep context-specific meanings separate

A vocabulary value must have a clear semantic definition.

Do not reuse a value merely because the displayed label is similar.

For example, high should not be treated as universally interchangeable across confidence, water demand, priority and salinity.


---

13.3 Keep numeric information numeric

Do not create categorical values where the underlying information is inherently numeric.

Examples:

pH
temperature
rainfall
sunlight_hours
distance
area
volume
weight

Derived classifications may exist where useful, but the original measurement remains the underlying evidence.


---

13.4 Represent unknown information explicitly

Missing information must not be silently interpreted as a meaningful value.

Where applicable, distinguish between:

unknown
not_recorded
not_applicable
not_measured

These values are not interchangeable.

The appropriate state depends on the semantics of the field.


---

14. Stable Identifiers

Canonical vocabulary identifiers should normally:

use lowercase;

use snake_case;

contain no spaces;

contain no translated words;

remain stable after release;

be suitable for imports and exports;

be usable by deterministic application logic.


Examples:

partial_shade
very_high
user_observation
not_applicable

User-facing labels must not be used as machine identifiers.


---

15. Localisation

Localisation is presentation-only.

Example:

Canonical:
partial_shade

English:
Partial shade

Afrikaans:
Gedeeltelike skadu

Rules, validation, database relationships and import/export must use:

partial_shade

not the translated label.

The UI may group, rename, abbreviate or explain a vocabulary value without changing its stored identifier.


---

16. Vocabulary Lifecycle

Vocabulary values should normally have a lifecycle such as:

active
deprecated

Active

May be used for new records.

Deprecated

Retained so existing records remain interpretable, but should not normally be used for new records.

A deprecated value should not be physically deleted when historical records depend on it.

Where appropriate, a deprecated value should identify its replacement.

Example:

old_value
status: deprecated
replacement: new_value


---

17. Versioning

Meaningful vocabulary changes must be documented.

A change record should identify:

vocabulary;

identifier;

previous meaning;

new meaning;

reason;

migration requirements;

compatibility implications.


Adding a genuinely new value is normally backward compatible.

Changing the meaning of an existing value may not be.

If a concept needs a materially different meaning, prefer:

1. create a new identifier;


2. deprecate the old identifier;


3. define the replacement;


4. migrate records where appropriate;


5. preserve historical interpretation.




---

18. Historical Compatibility

Vocabulary design must support future versions without requiring existing garden records to be rewritten unnecessarily.

If a future version introduces information that V1 could not capture:

retain the original V1 information;

leave newly introduced fields unknown where appropriate;

allow richer information to be added later;

never fabricate historical detail.


Vocabulary identifiers must remain interpretable across application versions.


---

19. User-Extensible Values

Where a domain intentionally supports user-defined values, those values must have separate identity and provenance from system-defined values.

Example:

System rotation group:
solanaceae

User rotation group:
plants_i_want_to_avoid_after_disease

The user-defined value may be meaningful within that garden without becoming universal horticultural knowledge.


---

20. Import and Export

Imports and exports must use canonical vocabulary identifiers.

Example:

{
  "lifecycle": "perennial",
  "confidence": "high"
}

Display labels must not be treated as canonical values.

Import validation must check vocabulary identifiers against the appropriate vocabulary and version.

An imported label such as:

Perennial

must not be assumed to be a valid canonical identifier.


---

21. Relationship to Application Rules

Recommendation, validation and diagnostic rules must operate on canonical identifiers.

Rules must depend on:

perennial

rather than:

"Perennial"

or:

"Meerjarige plant"

This ensures that changes to:

UI wording;

localisation;

typography;

display grouping


do not alter application logic.


---

22. Relationship to the UI

The UI may transform vocabulary values for presentation without changing their stored meaning.

For example:

Stored:
moderate_high

UI:
Moderate–high

The UI may also provide explanatory text where a value is not self-explanatory.


---

23. Vocabulary Versus Knowledge

A controlled vocabulary should describe a stable classification.

It should not be used to model a knowledge entity that requires:

its own attributes;

relationships;

provenance;

geographic applicability;

temporal validity;

historical records;

detailed descriptions.


For example:

perennial may be a vocabulary value.

A specific plant such as Tomato should be represented as reference/knowledge data.

Similarly, a specific pest, disease, cultivar or source should generally be represented as an entity rather than a vocabulary value.


---

24. Vocabulary Versus Structured Data

Use structured data when the information itself has a measurable or continuous value.

For example:

pH = 6.7
temperature = 21.5 °C
sunlight_hours = 6.5
bed_length = 3 m
harvest_weight = 1.2 kg

A derived classification such as:

slightly_acidic

may be useful for reasoning or presentation, but must not replace the underlying measurement.


---

25. Vocabulary Review Checklist

Before adding or changing a vocabulary, ask:

1. Is this genuinely a classification?


2. Could it instead be structured data?


3. Does the concept already exist elsewhere?


4. Does the meaning require a specific context?


5. Should it be fixed or extensible?


6. Is it actually a knowledge entity?


7. Will rules depend on it?


8. Will it appear in import/export?


9. Does it need localisation?


10. Must it survive across future versions?


11. Does it improve the gardener's experience?



If the answer does not justify controlled terminology, do not create a new vocabulary.


---

26. Document Status

Document	Status

VOCABULARY_INDEX.md	Current working specification
CORE_VOCABULARIES.md	Current working specification
GARDEN_VOCABULARIES.md	Current working specification
PLANT_VOCABULARIES.md	Current working specification
SOIL_VOCABULARIES.md	Current working specification
ACTIVITY_VOCABULARIES.md Current working specification
PROBLEM_VOCABULARIES.md	Current working specification
PLANNING_VOCABULARIES.md	Current working specification
PROVENANCE_VOCABULARIES.md	Current working specification


These domain documents should be created only when their contents have been sufficiently specified. The index does not require placeholder documents merely because they are listed here.


---

27. Guiding Principle

> Store stable English concepts. Translate only what the user sees.



The vocabulary architecture should remain:

small enough to understand;

stable enough for long-term storage;

precise enough for application logic;

flexible enough for future modules;

compatible with import/export;

independent of UI language;

protective of historical meaning;

simple enough that developers do not create unnecessary classifications.