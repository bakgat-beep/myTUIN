Garden Planner & Manager — Knowledge Base Strategy

Document: docs/KNOWLEDGE_BASE_STRATEGY.md
Version: 0.1
Status: Working specification
Last updated: August 2026

1. Purpose

This document defines the strategy for the Garden Planner & Manager's gardening knowledge base.

The knowledge base is intended to provide structured gardening knowledge that can eventually support:

plant information;

garden planning;

planting recommendations;

task generation;

environmental interpretation;

problem identification;

explanations;

comparisons;

future recommendation and decision-support systems.


The knowledge base is deliberately separated from individual user garden data.

It should be possible to improve, expand, correct and localise the knowledge base without rewriting the user's garden history.


---

2. Core principle

The knowledge base represents generalised gardening knowledge.

A user's garden represents what is true, planned, observed or recorded about that particular garden.

These are different categories of information and must remain separate.

For example:

Knowledge base

A tomato generally prefers:

adequate sunlight;

suitable temperatures;

sufficient water;

appropriate soil conditions.


User garden

The user planted tomatoes in Garden Area A on a particular date.

User observation

The user observed that those plants developed poorly in that location.

Recommendation

The application may infer that the location or growing conditions deserve investigation.

The recommendation must not silently turn the user's observation into a universal fact about tomatoes.


---

3. Relationship to the data model

The knowledge base is a source of structured reference information.

It should not be treated as a second copy of the user's garden database.

The system should conceptually distinguish:

Knowledge

→ what is generally known.

User data

→ what this gardener has recorded.

Inference

→ what the application concludes from available evidence.

Recommendation

→ what the application suggests the user consider doing.

These layers may interact, but they should remain distinguishable.


---

4. Knowledge hierarchy

The knowledge system should support several levels of information.

A useful conceptual hierarchy is:

1. Domain


2. Category


3. Entity


4. Attribute


5. Relationship


6. Rule


7. Evidence


8. Confidence


9. Source


10. Localisation



For example:

Plant

→ Tomato

→ prefers

→ high light

→ under appropriate conditions

→ with supporting evidence from a defined source.


---

5. Canonical language

The knowledge base uses the same canonical data language as the rest of the application.

English canonical identifiers should be used for:

plant identifiers;

attribute identifiers;

relationship identifiers;

categories;

rule identifiers;

controlled vocabulary values.


User-facing descriptions may subsequently be translated.

Canonical identifiers must remain independent of localisation.


---

6. Knowledge versus presentation

The knowledge base should contain structured meaning rather than UI-specific presentation wherever practical.

For example, the knowledge layer should represent:

light_requirement = full_sun

rather than:

"This plant needs lots of sunshine!"

The application can then present that information appropriately in:

English;

Afrikaans;

other future languages;

concise cards;

detailed explanations;

recommendations;

accessibility-oriented interfaces.



---

7. Knowledge types

The knowledge base should eventually support several broad categories.

7.1 Plant knowledge

Examples:

identity;

common names;

botanical classification;

lifecycle;

growth habit;

mature characteristics;

environmental preferences;

soil preferences;

water requirements;

light requirements;

temperature tolerance;

planting information;

propagation information;

harvesting information;

common problems;

companion relationships;

succession information.


Not every plant needs every field.

Unknown information should remain unknown.


---

7.2 Environmental knowledge

Examples:

light;

temperature;

rainfall;

frost;

wind;

humidity;

soil characteristics;

drainage;

salinity;

pH;

organic matter;

nutrient availability.


Environmental knowledge should be represented as structured concepts wherever possible.


---

7.3 Gardening practice knowledge

Examples:

sowing;

transplanting;

watering;

fertilising;

pruning;

mulching;

harvesting;

propagation;

crop rotation;

succession planting;

pest management.


Practices should be represented as concepts that can potentially be linked to:

plants;

environmental conditions;

seasons;

garden areas;

development stages.



---

7.4 Problem knowledge

Future versions may represent:

pests;

diseases;

nutrient deficiencies;

environmental stress;

physical damage;

symptoms.


This should be implemented carefully.

A symptom observed by a user is not automatically a diagnosis.


---

7.5 Relationship knowledge

The knowledge base should eventually represent relationships such as:

prefers;

tolerates;

avoids;

compatible_with;

incompatible_with;

commonly_affected_by;

susceptible_to;

resistant_to;

follows;

precedes;

supports;

competes_with.


Relationships should use controlled identifiers rather than arbitrary strings.


---

8. Knowledge granularity

Knowledge should be structured at a useful level of granularity.

Avoid storing all knowledge as large blocks of prose.

For example, instead of storing only:

> "Carrots like loose, well-drained soil and can tolerate cooler conditions."



represent the underlying concepts separately where practical:

preferred soil structure;

drainage preference;

temperature tolerance.


The application can then generate different presentations from the same structured knowledge.


---

9. Narrative knowledge

Structured knowledge should not completely replace narrative content.

Some information is naturally better represented as explanatory text.

The knowledge system should therefore support both:

Structured knowledge

Machine-readable facts, relationships and rules.

Narrative knowledge

Human-readable explanations, context and educational content.

The two should be linked where appropriate.


---

10. Source provenance

Knowledge should have provenance wherever practical.

Important knowledge records should be traceable to their source.

A source may include:

publication;

government agency;

university;

horticultural organisation;

research paper;

recognised gardening authority;

expert-reviewed reference;

other explicitly identified source.


The system should avoid presenting unsupported claims as established fact.


---

11. Source metadata

Where practical, a knowledge record should be capable of recording:

source identifier;

source title;

organisation/author;

publication date where available;

URL or other reference;

date accessed;

relevant citation information;

confidence/reliability assessment;

notes about applicability.


The exact source model should be defined before large-scale knowledge ingestion begins.


---

12. Source hierarchy

Not all sources should automatically receive equal weight.

A future source-quality framework should distinguish categories such as:

Primary scientific evidence

Research and experimental evidence.

Government/institutional guidance

Government departments, universities and recognised institutions.

Expert horticultural references

Established horticultural organisations and recognised experts.

Secondary references

Well-supported educational resources.

Community/user knowledge

Valuable for experience and observations but requiring different treatment from authoritative evidence.

This hierarchy should inform confidence and recommendation behaviour.


---

13. Conflicting knowledge

Gardening knowledge can legitimately vary between sources.

The knowledge system must not assume that every source will agree.

Where credible sources conflict, the system should be able to preserve the disagreement rather than arbitrarily deleting one side.

Possible approaches include:

recording multiple values;

recording applicable conditions;

recording geographic context;

recording source confidence;

recording date;

recording that evidence is mixed.



---

14. Context dependence

Gardening recommendations are often context-dependent.

A statement may be valid:

in one climate;

in another season;

for one soil type;

for one cultivar;

at one growth stage;

under one management system.


Knowledge should therefore support contextual applicability.

Where practical, knowledge records should be able to specify relevant context.


---

15. Geographic context

Knowledge may vary geographically.

Examples include:

planting seasons;

frost risk;

daylight;

rainfall;

pests;

diseases;

cultivar suitability;

regulations;

local gardening practices.


The knowledge base should therefore be capable of representing geographic applicability without duplicating the entire knowledge set for every region.


---

16. Cultivar and variety distinctions

Where relevant, the system should distinguish between:

species;

subspecies;

varieties;

cultivars;

common-name groupings.


However, V1 should not attempt to create an unnecessarily comprehensive botanical taxonomy.

The data model should allow greater specificity to be added later.


---

17. Plant identity

Plant identity should not depend solely on common names.

Common names can:

vary by country;

vary by language;

refer to multiple species;

have multiple names for one plant.


Where appropriate, the knowledge model should support stable plant identifiers and more precise botanical identity.

Common names should be treated as user-facing/reference terminology rather than the sole canonical identifier.


---

18. Synonyms

The knowledge base should support synonyms.

Examples include:

alternative common names;

regional names;

historical names;

spelling variants;

botanical synonyms.


Synonyms should point toward a canonical concept rather than creating unnecessary duplicate entities.


---

19. Localisation of knowledge

Knowledge should be localisable without duplicating the underlying entity.

For example:

One canonical plant:

tomato

may have:

English common name;

Afrikaans common name;

other future language names.


The plant remains the same canonical entity.


---

20. Localisation of narrative content

Narrative knowledge should be translated separately from its canonical structure.

A translated explanation should retain the same underlying concept.

The localisation system should therefore distinguish:

canonical content identifier;

language;

translated text.


This prevents translation changes from altering data relationships.


---

21. User observations versus knowledge

User observations should never automatically become authoritative knowledge.

For example:

A user records:

> "Leaves developed yellow patches."



This should remain a user observation.

It may later contribute evidence to a recommendation or learning system.

It should not automatically become:

> "Yellow patches are caused by X."




---

22. User experience as evidence

User-generated data may eventually become valuable evidence.

Potential future uses include:

identifying recurring patterns;

comparing outcomes;

improving recommendations;

identifying regional differences;

detecting knowledge gaps.


However, this should be treated as a distinct evidence layer.

User experience should not silently rewrite the canonical knowledge base.


---

23. Privacy boundary

User garden information should remain separate from general knowledge.

Any future system that aggregates user observations for broader analysis must explicitly address:

consent;

anonymisation;

privacy;

data ownership;

aggregation;

security.


These matters should not be assumed away simply because the data is useful.


---

24. Knowledge confidence

Knowledge should eventually support confidence or evidence-strength information.

Confidence should not simply mean:

> "The developer thinks this is probably true."



It should be grounded in factors such as:

evidence quality;

source reliability;

consistency between sources;

context;

recency;

expert assessment.


The exact confidence model should be defined before it becomes a central part of recommendations.


---

25. Uncertainty

The knowledge base should be capable of representing uncertainty.

Possible states may include:

established;

likely;

variable;

context-dependent;

uncertain;

insufficient evidence.


The exact controlled vocabulary should be defined in the relevant vocabulary specification.


---

26. Temporal knowledge

Gardening knowledge can change.

Examples:

new research;

changing pest distributions;

revised recommendations;

updated cultivation techniques.


Knowledge records should therefore be capable of being updated without rewriting historical user records.

Where necessary, knowledge versions should be identifiable.


---

27. Knowledge versioning

The knowledge base should have its own versioning concept.

A future application version may contain a different knowledge-base version.

For example:

Application version: V2.1

Knowledge-base version: KB-2027.03

This allows the project to distinguish:

software changes;

data-model changes;

knowledge changes.



---

28. Knowledge updates

Knowledge updates should ideally be independently deployable from major application changes where technically practical.

This could eventually allow:

correcting plant information;

adding plants;

updating recommendations;

adding translations;

improving explanations


without requiring a complete application rewrite.

The exact update mechanism is deferred until implementation requirements justify it.


---

29. Knowledge migrations

Changes to knowledge structure should be handled carefully.

Examples:

splitting one concept into several concepts;

merging duplicate plants;

changing a relationship;

replacing a vocabulary value.


Historical user records should continue to resolve correctly.

Canonical identifiers should therefore be stable whenever possible.


---

30. Knowledge identifiers

Knowledge entities should have stable machine-readable identifiers.

Identifiers should be:

unique;

language-independent;

reasonably stable;

suitable for import/export;

independent of display names.


Changing a display name should not require changing the identifier.


---

31. Avoiding identifier churn

Identifiers should not be changed merely because:

a translation changes;

a display label improves;

terminology becomes more user-friendly;

the UI wording changes.


Identifier changes should be treated as data migrations.


---

32. Knowledge relationships

Relationships should be first-class where they are important to recommendations.

For example:

Plant A

compatible_with

Plant B

should be represented as structured data rather than buried inside prose.

This allows future features to query relationships.


---

33. Relationship qualification

Some relationships require context.

For example:

A plant may be:

compatible under one planting arrangement;

unsuitable under another;

beneficial only under certain conditions.


Where necessary, relationships should support qualifiers rather than forcing simplistic universal claims.


---

34. Rules versus facts

The knowledge base should distinguish between:

Facts

Examples:

a plant's typical lifecycle;

preferred light conditions.


Rules

Examples:

if condition X and condition Y are true, recommend considering action Z.


Rules should not be disguised as facts.

This distinction becomes increasingly important as the recommendation engine develops.


---

35. Knowledge rules should be explainable

A recommendation rule should be capable of identifying:

its inputs;

its conditions;

its output;

its source or rationale;

its confidence/limitations where applicable.


This makes rules:

testable;

reviewable;

explainable;

replaceable.



---

36. Recommendation independence

The knowledge base should provide information to the recommendation engine.

The knowledge base should not become tightly coupled to one specific UI or recommendation presentation.

The same knowledge should potentially support:

planning;

plant information;

search;

education;

recommendations;

future modules.



---

37. Knowledge ingestion

Large-scale knowledge ingestion should be treated as a separate process from application development.

The application should not require developers to manually edit large quantities of source code whenever a new plant is added.

Where practical, knowledge should eventually be maintained as structured data that can be validated and imported.


---

38. Knowledge validation

Knowledge imports should be validated before being accepted.

Validation should detect:

duplicate identifiers;

missing required fields;

invalid vocabulary values;

broken relationships;

invalid references;

malformed records;

unsupported versions.


Invalid knowledge should not silently enter the production knowledge base.


---

39. Knowledge quality assurance

A knowledge record should ideally pass through:

1. creation;


2. validation;


3. source/provenance review;


4. consistency checks;


5. recommendation impact review where relevant;


6. publication.



The exact workflow may become more sophisticated later.


---

40. Automated knowledge validation

Automated validation should eventually check:

schema compliance;

identifier uniqueness;

valid references;

valid controlled vocabularies;

relationship consistency;

localisation completeness where required;

duplicate concepts;

impossible combinations where rules exist.



---

41. Human review

Automation should not be expected to determine whether gardening knowledge is scientifically or horticulturally correct in every circumstance.

Important knowledge should have appropriate human review.

The required level of review should depend on:

importance;

uncertainty;

potential harm;

evidence quality;

impact on recommendations.



---

42. Safety-sensitive knowledge

Some gardening information can have safety implications.

Examples may include:

chemical use;

toxic plants;

food safety;

dangerous machinery;

potentially harmful treatments.


Such information should receive a higher standard of evidence and review.

The knowledge base should avoid presenting uncertain safety information as established fact.


---

43. Recommendation impact

A knowledge change can have consequences beyond informational display.

For example, changing:

> preferred planting period



may change:

recommendations;

tasks;

planning suggestions.


Knowledge updates should therefore be capable of identifying potentially affected recommendation rules.


---

44. Knowledge and user-generated learning

The long-term product vision may allow the system to learn from garden history.

The architecture should distinguish between:

knowledge update

and:

personal learning.

A user may learn:

> "This variety performs poorly in my shaded garden."



That personal learning can be highly useful without becoming a universal rule.


---

45. Personalised knowledge

Future versions may maintain user-specific knowledge such as:

preferred varieties;

successful planting locations;

recurring problems;

preferred practices;

garden-specific patterns.


This should be represented as user-specific information rather than altering the global knowledge base.


---

46. Knowledge precedence

When multiple information sources exist, the application may eventually need a precedence model.

A conceptual precedence order could involve:

1. explicit user-provided information;


2. directly observed information;


3. contextual/local information;


4. high-confidence reference knowledge;


5. lower-confidence/generalised knowledge;


6. assumptions.



This is a conceptual principle, not yet a final algorithm.

The exact precedence rules should be defined when the recommendation engine is designed.


---

47. Explicit user information should matter

If the user explicitly records information that differs from general knowledge, the system should not silently overwrite it.

For example:

General knowledge:

> Plant X generally prefers condition A.



User:

> "My garden area does not have condition A, but I have successfully grown this plant here."



The system should preserve both pieces of information.


---

48. Context-aware recommendations

The long-term knowledge architecture should support recommendations based on combinations of:

plant;

garden;

area;

environmental conditions;

season/date;

user history;

observations;

general knowledge;

uncertainty.


This is one reason the knowledge base should be structured rather than purely textual.


---

49. Do not overbuild the knowledge system in V1

V1 should not attempt to create an encyclopaedia of every plant and gardening practice.

The initial knowledge set should be sufficient to support the V1 core product.

The architecture should allow expansion later.


---

50. V1 knowledge requirements

V1 should establish:

the canonical knowledge structure;

a manageable initial plant set;

core environmental classifications;

core gardening concepts;

basic relationships required by V1;

enough information to support V1 planning and recommendations where applicable;

validation mechanisms;

stable identifiers.


The exact initial vocabulary/content set should be defined separately.


---

51. Knowledge should not block core functionality

The application should remain useful when knowledge is incomplete.

For example, if a plant has incomplete reference information:

the user should still be able to record the plant;

the user should still be able to record observations;

the user should still be able to plan where possible;

the application should not invent missing knowledge.


This is an important resilience principle.


---

52. Missing knowledge

When information is unavailable, the application should distinguish between:

known;

unknown;

not applicable;

not yet researched.


The application should not automatically substitute a generic assumption without an explicit rule.


---

53. Knowledge and UI resilience

The UI should tolerate incomplete knowledge.

For example, a plant information screen should not assume every plant has:

every soil preference;

every planting date;

every disease relationship;

every companion relationship.


Missing information should result in an appropriate empty/unknown state.


---

54. Knowledge search

Future knowledge search should operate on canonical concepts as well as user-facing names.

A user searching for a translated plant name should still resolve to the same canonical plant entity.

Search should eventually support:

common names;

synonyms;

translated names;

botanical names;

relevant categories.



---

55. Knowledge indexing

If the knowledge base becomes large, it may require indexing for efficient retrieval.

The architecture should allow this without changing the conceptual knowledge model.

Performance optimisation should not alter canonical semantics.


---

56. Knowledge packaging

The knowledge base should eventually be capable of being distributed as structured data independently from application code where practical.

Potential future forms include:

bundled local knowledge;

versioned knowledge packages;

updateable knowledge datasets.


The exact packaging mechanism is deferred.


---

57. Offline knowledge

Core knowledge required by the V1 application should be available offline.

A user should not lose basic plant information or core recommendation functionality simply because the device is disconnected.


---

58. Online knowledge

Future online knowledge updates may provide:

new plants;

corrected information;

expanded relationships;

new translations;

updated recommendations.


Online updates must not make the core application dependent on permanent connectivity.


---

59. Knowledge licensing

Knowledge sources must be reviewed for appropriate usage rights before substantial ingestion.

The project should not assume that information found online can simply be copied into the application's database.

Where appropriate, the project should use:

openly licensed data;

appropriately licensed datasets;

original structured knowledge;

properly attributed material;

information that can legally be incorporated.


Licensing requirements should be recorded with the relevant knowledge source.


---

60. Avoiding copied-content dependency

The knowledge base should prefer structured facts and original synthesis over wholesale reproduction of external publications.

External sources should provide evidence and provenance.

The application should not become a container for copied third-party articles.


---

61. Source changes

External sources may:

change;

disappear;

move;

update their recommendations.


The knowledge base should therefore preserve enough source metadata to understand where information originated.

Where appropriate, important source content should be reviewed when sources change.


---

62. Knowledge auditability

It should eventually be possible to answer:

> Why does the application believe this?



For important recommendations, the system should ideally be able to trace:

Recommendation

→ rule

→ inputs

→ knowledge used

→ source/evidence

→ confidence/limitations.

This is a long-term design goal.


---

63. Knowledge change impact analysis

Before publishing significant knowledge changes, consider:

which plants are affected;

which relationships are affected;

which recommendation rules are affected;

which user-facing content changes;

whether historical records should remain unchanged.


Knowledge changes should not silently rewrite user history.


---

64. Historical knowledge versus current knowledge

Where a historical recommendation matters, the application may eventually need to know which knowledge version produced it.

For example:

A recommendation generated in 2027 should not necessarily appear to have been generated using a 2030 knowledge base.

This is particularly important if recommendations become part of historical records.

The exact implementation is deferred until recommendation history requires it.


---

65. Knowledge provenance in historical recommendations

If a recommendation is stored as a historical record, it should eventually be possible to identify:

when it was generated;

which rule was used;

which knowledge version was used;

what inputs were considered.


This protects historical interpretability.


---

66. Knowledge and database migrations

Knowledge structure changes and user database schema changes should be treated as related but distinct concerns.

A knowledge update should not normally require rewriting user history.

A database migration should not silently alter the meaning of historical observations.


---

67. Knowledge testing

The knowledge base should be tested at several levels.

Structural tests

Does the data conform to the schema?

Referential tests

Do referenced entities exist?

Vocabulary tests

Are identifiers valid?

Rule tests

Do rules behave correctly?

Scenario tests

Do representative garden situations produce expected outcomes?

Regression tests

Did a knowledge update unintentionally change important recommendations?


---

68. Knowledge regression testing

Representative scenarios should be maintained so that knowledge changes can be compared against previous behaviour.

If a knowledge update changes a recommendation, that change should be identifiable.

Unexpected recommendation changes should trigger review.


---

69. Knowledge update workflow

A mature workflow should resemble:

Create/update knowledge

↓

Validate structure

↓

Check identifiers and relationships

↓

Review source/provenance

↓

Run knowledge tests

↓

Run recommendation regression tests

↓

Review significant changes

↓

Publish knowledge version

↓

Record change


---

70. Knowledge changelog

Knowledge updates should eventually have their own changelog.

It should identify significant changes such as:

new plants;

corrected attributes;

changed relationships;

new evidence;

changed recommendations;

removed/merged concepts;

new translations.


This should be distinct from the software release notes where practical.


---

71. Knowledge ownership

The project should eventually establish responsibility for:

knowledge quality;

source review;

taxonomy;

vocabulary;

translations;

recommendation rules.


The exact ownership model is deferred while the project remains in foundational design.


---

72. Knowledge contribution

Future development may allow contributors to propose:

new plants;

new knowledge;

corrections;

relationships;

translations.


Contributions should enter a review/validation process rather than automatically becoming authoritative.


---

73. User feedback on knowledge

Users should eventually be able to identify problematic information.

Potential feedback categories include:

incorrect;

outdated;

not applicable to my region;

missing information;

unclear;

conflicting with experience.


Feedback should be treated as evidence for review rather than automatically changing the knowledge base.


---

74. Regional knowledge

Because gardening is strongly influenced by location, the knowledge architecture should eventually support regional applicability.

This may include:

climate;

seasons;

frost;

rainfall;

pests;

disease prevalence;

local planting windows.


The system should avoid hard-coding one region's gardening assumptions into supposedly universal knowledge.


---

75. New Zealand considerations

The project is initially being developed with a New Zealand context in mind.

However, the underlying knowledge architecture should not assume that New Zealand is the only supported region.

The architecture should allow regional information to be added without duplicating the entire global knowledge model.


---

76. Local gardening observations

User observations may be especially valuable for location-specific knowledge.

For example:

Multiple users in a region may observe a recurring seasonal pattern.

Such information may eventually support aggregated learning.

However, aggregation should remain a separate process from individual garden records.


---

77. Knowledge and weather/environmental data

Future modules may obtain environmental information from external services.

Such information should be treated as:

external observations/data;

contextual inputs;

potentially uncertain.


External environmental data should not be confused with permanent gardening knowledge.


---

78. Knowledge and sensor data

Future versions may incorporate:

soil sensors;

weather stations;

moisture sensors;

other devices.


Sensor observations should remain observations/data rather than automatically becoming knowledge.

The recommendation system may use them as inputs.


---

79. Knowledge and photographs

Future image-based features may help identify:

plants;

pests;

diseases;

symptoms.


Any such identification should be treated as an inference with uncertainty.

An image-based prediction should not automatically become a confirmed fact or update the knowledge base.


---

80. Knowledge and AI

AI may eventually assist with:

knowledge discovery;

summarisation;

translation;

classification;

recommendation;

anomaly detection;

natural-language explanations.


AI-generated information should not automatically become authoritative knowledge.

AI output should remain distinguishable from validated knowledge until appropriately reviewed.


---

81. AI-generated knowledge provenance

If AI is used in future knowledge workflows, records should be capable of identifying that AI assistance was involved.

This is particularly important when AI-generated information influences:

recommendations;

safety-related information;

structured knowledge.



---

82. Avoiding false certainty

The knowledge system should never make information appear more certain simply because it is stored in structured form.

A structured record is not automatically a verified fact.

Confidence and provenance must remain meaningful.


---

83. Knowledge display

The UI should present knowledge at an appropriate level of detail.

Simple information should remain simple.

Advanced evidence/provenance information may be available when the user wants more detail.

The knowledge architecture should support both without duplicating the underlying information.


---

84. Educational use

The knowledge base may eventually support educational experiences.

For example:

"Why does this plant need this?"

"What does full sun mean?"

"What does soil drainage mean?"

"Why might this plant struggle here?"


Educational explanations should use the same underlying concepts as the recommendation system where possible.


---

85. Knowledge as a platform capability

The knowledge base should eventually become a reusable platform capability for the application.

Multiple modules should be able to consume the same canonical knowledge rather than maintaining separate copies.

For example:

plant module;

planning module;

recommendation module;

observation module;

education module.



---

86. Avoiding duplicated knowledge

If two modules require the same concept, they should normally consume the same knowledge source.

For example, the definition of a plant's light requirement should not separately exist in:

plant UI;

recommendation engine;

planning engine.


Duplicated knowledge will eventually diverge.


---

87. Knowledge boundaries

The knowledge base should not become a dumping ground for every piece of application information.

The following generally belong elsewhere:

user's garden records;

user preferences;

task completion;

personal observations;

application settings;

UI state.


The knowledge base should contain general/reference knowledge.


---

88. Knowledge and application configuration

Application configuration should remain distinct from gardening knowledge.

For example:

> "The application displays five tasks per page"



is configuration.

> "This plant generally benefits from condition X"



is knowledge.

This distinction keeps the knowledge system focused.


---

89. Knowledge and controlled vocabularies

Controlled vocabularies provide the language used to express knowledge.

For example:

A knowledge record may use:

light_requirement = full_sun

The controlled vocabulary defines what full_sun means.

The knowledge record then applies it to a specific entity.


---

90. Knowledge schema evolution

The knowledge schema should evolve cautiously.

New fields should normally be additive where practical.

Removing or changing the meaning of an existing field should require explicit review.

Knowledge schema changes should be versioned where necessary.


---

91. Knowledge completeness

The project should not define "complete knowledge" as:

> every possible field populated for every plant.



A knowledge record may be perfectly valid while containing unknown values.

Completeness should instead be considered relative to:

the needs of the feature;

the reliability of the source;

the intended use.



---

92. Knowledge quality over quantity

A smaller set of reliable knowledge is preferable to a huge set of poorly supported claims.

This is particularly important for V1.

The project should prioritise:

correctness;

provenance;

consistency;

useful coverage.



---

93. Knowledge expansion strategy

After V1, expansion should generally prioritise knowledge that unlocks meaningful functionality.

For example:

1. knowledge needed by core planning;


2. knowledge needed by recommendations;


3. commonly used plants;


4. common gardening problems;


5. regional knowledge;


6. broader plant coverage;


7. advanced specialist knowledge.



This order may change as actual user needs become clearer.


---

94. Knowledge and product priorities

The knowledge base exists to support the product.

Knowledge expansion should therefore not become a substitute for building useful application functionality.

A massive encyclopaedia is not itself the product.


---

95. Knowledge system success criteria

The knowledge architecture will be successful if it allows the application to:

represent gardening concepts consistently;

add new plants without major code changes;

support multiple languages;

support regional variation;

provide explainable recommendations;

preserve provenance;

represent uncertainty;

evolve without corrupting historical user data;

operate offline for core functionality;

expand independently of unrelated application modules.



---

96. V1 boundaries

The following are deliberately outside the required V1 knowledge implementation unless needed by the final V1 scope:

comprehensive global plant taxonomy;

automated knowledge harvesting;

large-scale crowdsourced knowledge;

sophisticated machine learning;

automated scientific literature analysis;

cloud-based knowledge synchronisation;

advanced image-based identification;

complex personalised learning algorithms.


These may be considered later.


---

97. Long-term vision

The long-term goal is for the Garden Planner & Manager to become more than a static database.

It should eventually be capable of combining:

General knowledge


Garden context


User plans


Actual events


Observations


Environmental information


Historical outcomes

→

Useful, transparent, context-aware gardening guidance

The system should help the gardener make better decisions while remaining clear about what is known, what is observed, what is inferred and what is uncertain.


---

98. Guiding principle

The central knowledge-base principle is:

> Store knowledge as structured, attributable, contextual information that can be reused by many modules without becoming entangled with the user's personal garden history or the application's presentation layer.



The knowledge base should make the application more useful without making it more fragile.