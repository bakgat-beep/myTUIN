Garden Planner & Manager — Provenance Controlled Vocabularies

Document: docs/vocabularies/PROVENANCE_VOCABULARIES.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines controlled vocabularies for describing:

where information came from;

what kind of evidence supports it;

how information relates to a source;

how information entered the system;

how well information applies to a context;

the maturity of knowledge;

contribution and sharing status;

geographic sharing precision;

verification and validation;

import processing.


These concepts support:

explainable recommendations;

historical interpretation;

deterministic reasoning;

user observations;

imported knowledge;

future community knowledge;

data quality;

import/export.


The provenance system must distinguish what information says from where it came from and how confident the application is in it.


---

2. Provenance principles

Provenance must answer, where relevant:

1. What is this information?


2. Where did it come from?


3. How was it obtained?


4. When was it obtained or published?


5. What geographic or environmental context does it apply to?


6. Has it been verified?


7. How was it transformed?


8. Is it appropriate for the current context?


9. Is it private or eligible for sharing?



Provenance information should support explanation without overwhelming ordinary users.


---

3. Relationship to other vocabularies

This document does not own concepts that belong elsewhere.

In particular:

confidence belongs to CORE_VOCABULARIES.md;

information state belongs to CORE_VOCABULARIES.md;

recommendation status belongs to PLANNING_VOCABULARIES.md;

plant classifications belong to PLANT_VOCABULARIES.md;

soil classifications belong to SOIL_VOCABULARIES.md;

activity classifications belong to ACTIVITY_VOCABULARIES.md;

problems and diagnostic classifications belong to PROBLEM_VOCABULARIES.md.


Provenance describes the origin, evidence and handling of information, not its substantive meaning.


---

4. Stable identifiers

Canonical provenance identifiers must normally:

use lowercase;

use snake_case;

contain no spaces;

be language-independent;

remain stable after release;

be suitable for import/export.


Example:

user_observation
published_research
adapted
system_derived
verified

UI labels are supplied through localisation.


---

5. Evidence type

Evidence type describes the broad nature of evidence supporting a claim or knowledge record.

verified_reference
published_research
institutional_guidance
expert_guidance
curated_guidance
community_observation
user_observation
imported_dataset
experimental_result
derived_analysis

Meaning

verified_reference

A source or reference that has undergone an appropriate verification process.

published_research

Evidence originating from published scientific or technical research.

institutional_guidance

Guidance produced by a recognised institution or organisation.

expert_guidance

Guidance attributed to a suitably qualified expert where the underlying evidence is not better represented by another category.

curated_guidance

Information reviewed and deliberately incorporated into the application's curated knowledge base.

community_observation

Observations contributed by multiple users or a community knowledge system.

user_observation

An observation recorded by the individual gardener.

imported_dataset

Information imported from an external structured dataset.

experimental_result

A result generated through a recorded experiment.

derived_analysis

Information calculated or derived from existing records.

Evidence type does not indicate confidence.


---

6. Source type

A Source record may be classified as:

research
institution
government
extension_service
expert
community
user
dataset
application_generated
other

Source type identifies the kind of source, while evidence_type describes the nature of evidence.

For example:

source_type:
government

evidence_type:
institutional_guidance


---

7. Source status

Source status describes the application's current assessment of a source.

unverified
verified
partially_verified
outdated
superseded
disputed
unavailable

Meaning

unverified

The source has not yet been checked by the application's verification process.

verified

The relevant source identity and content have been sufficiently checked.

partially_verified

Some aspects have been verified, but verification is incomplete.

outdated

The source may no longer represent current guidance.

superseded

A newer or more authoritative source has replaced it.

disputed

The source or its relevant claims are subject to unresolved disagreement.

unavailable

The source can no longer be accessed or otherwise verified.

Source status does not automatically determine whether every claim from the source is true or applicable.


---

8. Source relationship

Describes how a knowledge record relates to its source.

direct
adapted
derived
summarised
inferred
unknown

direct

The stored information represents the source information without substantive transformation.

adapted

The information has been deliberately adjusted for another context.

Example:

Northern Hemisphere planting guidance
        ↓
adapted
        ↓
Southern Hemisphere timing

derived

The information was calculated or transformed from source information.

summarised

The information is a concise representation of a larger source.

inferred

The information was inferred rather than directly stated by the source.

unknown

The relationship is not known.

Adapted and derived information should retain sufficient provenance to identify the underlying source.


---

9. Data origin

Data origin describes how information entered the application.

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

Important distinction

user_observed and user_measured describe information entered from the user's garden.

system_derived and system_inferred describe information generated by application logic.

Data origin does not replace evidence type.

For example:

origin:
user_measured

evidence_type:
user_observation

may be appropriate for a garden soil measurement.


---

10. Applicability

Applicability describes how well a piece of knowledge or evidence applies to a particular context.

applicable
partially_applicable
not_applicable
unknown

Applicability should be assessed against the relevant context, which may include:

location;

climate;

season;

growing method;

plant;

cultivar;

soil;

growth stage.


Applicability is not confidence.

A highly reliable source may still be poorly applicable to a particular garden.


---

11. Applicability context

Identifies the primary contextual dimension affecting applicability.

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

This may be used to explain why otherwise reliable information was only partially applicable.


---

12. Knowledge maturity

Knowledge maturity describes the degree to which information has progressed through the application's knowledge-development process.

personal_observation
potential_contribution
aggregated_observations
emerging_pattern
local_finding
reviewed_guidance
established_knowledge

Meaning

personal_observation

A record originating from an individual garden observation.

potential_contribution

Information identified as potentially useful beyond the user's private garden.

aggregated_observations

Multiple observations have been combined for analysis.

emerging_pattern

Repeated evidence suggests a potentially meaningful pattern, but it is not yet established knowledge.

local_finding

Evidence supports a finding within a defined local or contextual scope.

reviewed_guidance

Information has undergone deliberate review before being treated as guidance.

established_knowledge

Information has sufficient support and review to be treated as established within the application's knowledge system.

Promotion between levels must be deliberate.

A single observation must never automatically become established knowledge.


---

13. Contribution status

Contribution status applies to optional future knowledge-sharing functionality.

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

Meaning

private

The information remains private to the user's garden.

eligible

The information could potentially be offered for contribution.

offered

The application has offered the user an opportunity to contribute it.

approved_by_user

The user has explicitly approved contribution.

submitted

The information has been submitted for consideration.

under_review

The contribution is being evaluated.

accepted

The contribution has been accepted into the relevant knowledge process.

rejected

The contribution was not accepted.

withdrawn

The user withdrew the contribution.

not_available

Contribution is not available for this record or context.

Contribution must always be explicit and user-controlled.


---

14. Geographic sharing precision

Defines the maximum geographic precision that may be shared through future contribution functionality.

exact
property
locality
region
country

Default principle

Exact garden location should remain private unless the user explicitly chooses otherwise.

The stored garden location and the permitted sharing precision are separate concepts.

For example:

garden_location:
exact coordinates

sharing_precision:
locality

The latter does not require exposing the exact coordinates.


---

15. Verification status

Verification status describes whether information itself has been reviewed.

unverified
partially_verified
verified
rejected
unknown

This differs from source_status.

A source may be verified while a particular claim derived from it remains unverified.

Likewise, a user observation may be completely valid as a record while remaining unverified as general horticultural knowledge.


---

16. Validation status

Validation status describes whether stored data passes the application's structural or semantic validation.

valid
warning
invalid
unknown

Validation concerns data integrity, not truth.

For example:

pH = 6.7
validation = valid
verification = unverified

means the data is structurally acceptable but has not been independently verified.


---

17. Data modification reason

When significant changes require a recorded reason:

user_created
user_updated
imported
system_derived
migration
curation
unknown

This should be used only where modification provenance materially matters.

Ordinary editing does not require excessive audit records.


---

18. Import validation severity

Import validation may classify an issue as:

information
warning
error
critical

information

No corrective action is required.

warning

The data may be imported, but the user should be made aware of a potential issue.

error

The affected record cannot safely be imported without correction.

critical

The import cannot safely continue.


---

19. Import record status

Individual imported records may have:

pending
validated
warning
rejected
imported
updated
skipped
conflict

Meaning

pending

Awaiting validation.

validated

Passed validation and is eligible for import.

warning

Passed basic validation but contains a condition requiring user attention.

rejected

Cannot safely be imported.

imported

Successfully created in the destination data.

updated

Successfully updated an existing destination record.

skipped

Intentionally not imported.

conflict

Conflicts with existing data and requires a resolution decision.

Import processing should occur before modifying the user's actual garden data.


---

20. Import source type

Where useful, imported data may identify its source as:

application_export
structured_dataset
spreadsheet
csv
json
other

The actual file format should normally be stored as metadata rather than expanded into a large vocabulary.


---

21. Knowledge transformation

Where information moves through multiple processing stages, provenance should be chainable.

Conceptually:

source
    ↓
direct claim
    ↓
adapted claim
    ↓
system-derived assessment
    ↓
recommendation

Each transformation should preserve a link to the preceding information where required for explanation and auditability.

The application should avoid replacing the original provenance with only the latest transformation.


---

22. Adapted knowledge

Adapted information is particularly important for geographic and hemispheric translation.

Example:

Source:
Northern Hemisphere horticultural guidance

Relationship:
adapted

Application context:
Southern Hemisphere

Adaptation:
seasonal timing translated

Applicability:
partially_applicable

The original source context should remain accessible.

The application must not present adapted timing as though it were directly stated by the original source.


---

23. User observations

User observations are first-class evidence for the user's own garden.

They should retain:

who recorded them, where applicable;

date/time;

location/context;

observation;

information state;

provenance;

confidence where appropriate.


A user observation does not become a universal horticultural rule merely because it is stored.


---

24. Measurements

Measured values should preserve sufficient provenance to understand the measurement.

Where applicable, retain:

measured value;

unit;

date;

location;

method;

source/device;

information state;

confidence;

provenance.


Example:

pH:
6.7

information_state:
measured

data_origin:
user_measured

method:
home_test_kit

The method itself should be represented through an appropriate domain vocabulary or reference data rather than being encoded into provenance.


---

25. Derived information

System-derived information should identify that it was calculated from existing information.

Example:

data_origin:
system_derived

evidence_type:
derived_analysis

The application should retain enough dependency information to explain the derivation when it materially affects a recommendation or historical conclusion.


---

26. Inferred information

System-inferred information should remain distinguishable from directly observed or measured information.

Example:

Observed:
water pools after heavy rain

Inferred:
drainage probably slow

data_origin:
system_inferred

information_state:
inferred

The inference should not overwrite the original observation.


---

27. Confidence versus provenance

These concepts must remain separate.

Example:

Evidence:
institutional_guidance

Source status:
verified

Applicability:
partially_applicable

Confidence:
moderate

This is valid.

A verified source does not guarantee that its information is highly applicable to every garden.

Likewise:

Evidence:
user_observation

Confidence:
high

can be valid for an observation while still being insufficient to establish a universal horticultural rule.


---

28. Provenance versus truth

Provenance records where information came from and how it was handled.

It does not itself prove that information is true.

The application should avoid presenting labels such as:

> Verified



as equivalent to:

> Guaranteed correct.



Where useful, the UI should distinguish:

source verified;

information verified;

applicable;

confidence.



---

29. Provenance in recommendations

Significant recommendations should be able to identify the provenance of important evidence.

For example:

Recommendation:
conditional

Important evidence:
soil pH

Origin:
user_measured

Source:
home_test_kit

Applicability:
applicable

Confidence:
moderate

The user should normally see a concise explanation first.

Technical provenance can be exposed through progressive disclosure.


---

30. Privacy principle

Private garden data must remain private by default.

The provenance architecture must never assume that:

useful

means:

shareable

A user observation can be highly valuable while remaining completely private.

Any future sharing feature must use explicit consent and an appropriate geographic precision setting.


---

31. Import/export

Canonical identifiers must be used for provenance fields.

Example:

{
  "evidence_type": "user_observation",
  "data_origin": "user_observed",
  "applicability": "applicable",
  "verification_status": "unverified"
}

Translated labels must not be used as canonical import values.

Import validation must reject or flag unknown identifiers according to the import specification.


---

32. Localisation

Canonical identifiers remain English.

Example:

Canonical:
user_observation

English:
User observation

Afrikaans:
Gebruikerwaarneming

The UI may simplify terminology for ordinary users.

For example, the primary interface may simply say:

> Your observation



while the technical provenance panel can expose:

> User observation




---

33. Historical compatibility

Provenance values must remain interpretable across application versions.

If a value becomes unsuitable:

1. deprecate it;


2. retain it for historical records;


3. define a replacement where appropriate;


4. migrate only where semantic meaning permits;


5. preserve the original provenance chain.



Historical provenance must not be discarded merely because a newer provenance model is introduced.


---

34. Avoiding unnecessary provenance

Not every record requires every provenance field.

The application should record provenance where it materially improves:

explainability;

data integrity;

historical interpretation;

recommendation quality;

source management;

future knowledge aggregation;

import/export reliability.


The system should not turn ordinary gardening into an administrative exercise.


---

35. Minimum provenance principle

For significant external or derived knowledge, the application should generally be able to determine:

what
where from
when
how obtained
how transformed
how applicable

The exact fields required depend on the entity and context.


---

36. V1 scope

V1 requires provenance support for:

user observations;

user measurements;

built-in knowledge;

imported knowledge;

system-derived recommendations;

source relationships;

applicability;

verification;

confidence linkage;

historical preservation.


Future community contribution and sharing infrastructure may use:

contribution status;

geographic sharing precision;

knowledge maturity.


These should not require community functionality to be implemented in V1.


---

37. Vocabulary review checklist

Before adding or changing a provenance value, ask:

1. Does this describe origin, evidence, source, applicability or handling?


2. Does the concept already belong in Core?


3. Is this actually confidence?


4. Is this actually information state?


5. Is this substantive knowledge that belongs in another domain?


6. Does the distinction improve explainability?


7. Does it need to survive historical records?


8. Does it need to appear in import/export?


9. Could the same information be represented through a relationship or structured field?


10. Does the additional complexity provide meaningful value?




---

38. Guiding principle

> Provenance explains where information came from and how it was handled; it does not replace the information itself.



The provenance architecture should remain:

explicit;

lightweight;

explainable;

privacy-preserving;

historically durable;

compatible with deterministic reasoning;

suitable for imported and user-generated information;

ready for future community knowledge without requiring community functionality in V1.