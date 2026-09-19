Garden Planner & Manager — Import & Export Specification

Document: docs/IMPORT_EXPORT_SPECIFICATION.md
Version: 0.2
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the V1 import and export behaviour for Garden Planner & Manager.

Import/export exists primarily to provide:

data portability;

backup and recovery;

migration between application versions;

bulk data entry;

interoperability;

protection against application or device failure;

future extensibility.


Import/export is an advanced capability.

Ordinary users should not need to understand the application's internal data model to use it.


---

2. Core principles

Import/export must be:

1. Loss-aware
The application must not silently discard information.


2. Historically safe
Existing garden history must remain interpretable.


3. Validated
Imported data must be checked before it modifies the garden.


4. Reversible where practical
Users should be able to cancel an import before committing it.


5. Explicit about conflicts
Conflicting records must not be silently overwritten.


6. Versioned
Exported files must identify the schema/version they use.


7. Human-understandable
Validation errors should explain what is wrong and what can be done.


8. Offline-first
Core import/export functionality should not require Internet access.


9. Modular
Import/export must not become tightly coupled to individual feature modules.


10. Forward-compatible where practical
Future application versions should be able to recognise older exports.




---

3. V1 objectives

V1 import/export must support:

complete garden export;

complete garden import;

backup/recovery;

selective export where practical;

validated bulk import;

preservation of canonical vocabulary identifiers;

preservation of historical records;

import validation and conflict reporting;

schema/version identification;

safe cancellation before commit.


V1 does not need to provide a universal interchange format for other gardening applications.


---

4. What can be exported

A complete garden export should contain, where applicable:

Garden configuration

garden identity;

garden name;

location;

geographic coordinates;

hemisphere;

relevant regional information;

user preferences;

application-specific settings relevant to garden interpretation.


Spatial data

areas;

growing spaces;

infrastructure;

spatial objects;

geometry;

dimensions;

spatial relationships;

relevant spatial metadata.


Plant data

plant references;

cultivars;

user's plant instances;

planned plants;

planting locations;

planting dates;

lifecycle information;

plant history.


Activities

planting;

watering;

feeding;

pruning;

interventions;

observations;

harvesting;

losses;

other supported activities.


Soil and environmental information

observations;

measurements;

estimates;

inferred values;

sources;

dates;

locations;

confidence;

methods.


Planning

plans;

planned activities;

recommendations where retained as records;

user decisions;

overrides;

experiments;

experiment observations and outcomes.


Problems

recorded problems;

symptoms;

observations;

diagnoses where explicitly recorded;

interventions;

outcomes.


Historical information

Where supported by the data model:

historical plantings;

historical observations;

historical measurements;

historical harvests;

historical decisions;

historical experiments;

relevant recommendation records.



---

5. What should not normally be exported

A garden export should not unnecessarily contain:

cached UI state;

temporary application state;

authentication credentials;

access tokens;

device-specific settings;

temporary files;

generated caches;

internal database indexes;

implementation-specific cache identifiers;

secrets.


Future cloud functionality must define separately what account or authentication information can be exported.


---

6. Export types

V1 should support two conceptual export types.

6.1 Complete Garden Export

Contains the garden and its associated user-owned data required to reconstruct the garden.

Primary purpose:

backup;

migration;

recovery;

transfer.


This should be the recommended export for most users.


---

6.2 Selective Export

Allows an advanced user to export a subset of their data.

Potential selections include:

selected growing spaces;

selected plants;

selected date range;

selected activities;

selected observations;

selected harvests;

selected experiments.


Selective export must clearly identify that it is not a complete backup.


---

7. Export format

The canonical V1 interchange format should be JSON.

A complete export should be a structured document rather than a raw database dump.

Conceptually:

{
  "format": "garden_planner_export",
  "format_version": "1.0",
  "application_version": "1.0.0",
  "exported_at": "...",
  "garden": {},
  "spaces": [],
  "plants": [],
  "activities": [],
  "observations": [],
  "plans": [],
  "experiments": []
}

The exact schema should be defined separately from this UX/behaviour specification.


---

8. Export metadata

Every export should contain sufficient metadata to identify:

export format;

format version;

application version where available;

export timestamp;

garden identifier;

relevant schema version;

data scope.


Example conceptual fields:

format
format_version
application_version
schema_version
exported_at
garden_id
export_scope

Export metadata must not contain secrets.


---

9. Canonical identifiers

Exported controlled vocabulary values must use canonical identifiers.

Example:

{
  "lifecycle": "perennial",
  "confidence": "high",
  "status": "completed"
}

The export must not depend on UI labels such as:

Perennial
High
Completed

This ensures that exports remain independent of language and presentation.


---

10. Localisation

Exported data must use canonical language-independent identifiers.

The export should not normally contain translated UI labels as authoritative values.

For example:

partial_shade

rather than:

Partial shade

or:

Gedeeltelike skadu

A future export format may optionally contain display labels for human convenience, but such labels must never be treated as the canonical value.


---

11. Identifiers

Exported records must retain stable identifiers where those identifiers are part of the data model.

This is particularly important for:

relationships;

historical records;

spatial objects;

plant instances;

activities;

observations;

experiments.


References must remain internally consistent.


---

12. External knowledge references

Garden records may reference system knowledge such as:

plants;

cultivars;

problems;

sources;

rotation groups;

interventions.


Exports should preserve those references where possible.

However, a garden export must not assume that every version of the application contains exactly the same knowledge base.

Where necessary, the export should include enough information to identify the referenced knowledge item and detect when it is unavailable or changed.


---

13. User-owned versus system-owned data

The export process must distinguish conceptually between:

User-owned information

Examples:

observations;

measurements;

plantings;

harvests;

garden geometry;

decisions;

preferences;

experiments.


System knowledge

Examples:

general plant information;

curated horticultural guidance;

recommendation rules;

canonical vocabularies.


A complete garden export should primarily preserve user-owned information.

System knowledge should not be duplicated unnecessarily when it can be referenced by stable identifiers.


---

14. Provenance preservation

Where provenance exists, it should be exported.

Examples:

data origin;

evidence type;

source;

source relationship;

confidence;

measurement method;

observation status;

modification reason.


Import must not convert:

user_observation

into:

verified_reference

or otherwise increase evidential status merely because data was imported.


---

15. Unknown information

Unknown values must remain distinguishable from missing export fields.

Where the data model supports explicit information states, preserve them.

Examples:

unknown
not_recorded
not_measured
not_applicable

Import must not silently turn an explicit unknown into a known value.


---

16. Dates and times

Dates and timestamps must be exported in an unambiguous machine-readable form.

Where a timestamp represents an actual event, sufficient timezone information should be retained where required.

Date-only information must remain date-only.

The application must not invent a time merely because the export format supports timestamps.


---

17. Geographic data

Geographic information should preserve:

coordinates where present;

coordinate reference information where necessary;

geographic precision where relevant;

associated garden/spatial object relationships.


Import must not silently change coordinates or interpret them using the wrong coordinate system.

Where a future format supports only a subset of spatial information, the user must be informed before data is lost.


---

18. Geometry

Spatial geometry should be exported using a stable application-defined representation.

The representation must preserve, where applicable:

object identity;

geometry;

dimensions;

position;

spatial relationships;

relevant historical associations.


Import must validate geometry before committing it.

Invalid geometry must not be silently repaired in a way that changes the user's garden without explanation.

Where automatic repair is safe, the application may offer:

> Geometry adjusted to a valid representation.



The user should be able to inspect the issue where practical.


---

19. Historical records

Historical records are first-class data.

Export must preserve historical information rather than exporting only the current state.

For example, if a bed currently contains tomatoes but previously contained:

carrots;

beans;

lettuce;


the export must retain the relevant historical planting records.

Import must reconstruct those records rather than reducing them to the current state.


---

20. Recommendation history

Recommendations should not normally be treated as permanent horticultural facts.

If recommendation records are exported, they should retain relevant context such as:

date;

subject;

recommendation result;

factors;

confidence;

relevant user decision;

applicable rule/knowledge version where available.


A future application version may produce a different recommendation from the same garden data.

That does not mean the historical recommendation record was invalid.


---

21. User decisions

Where a user accepted, rejected, modified, deferred or overrode a recommendation, the decision should be preserved where recorded.

A decision must remain distinct from the resulting activity.

For example:

Recommendation:
Plant tomatoes.

User decision:
Overridden.

Activity:
Tomatoes planted anyway.

These are separate facts.


---

22. Planned versus completed

Import/export must preserve the distinction between:

planned;

scheduled;

completed;

partially completed;

skipped;

cancelled.


A planned activity must never become a completed activity simply because it was exported and imported.


---

23. Partial completion

Partial completion must be preserved.

For example:

Plan:
Water Beds 1–4

Completed:
Beds 1–3

Remaining:
Bed 4

The export must not flatten this into either:

completed

or:

not completed

when the underlying model supports partial completion.


---

24. Relationships

Relationships between exported records must be represented using stable identifiers.

Examples:

plant instance → growing space
plant instance → cultivar
activity → plant instance
activity → growing space
observation → plant
observation → soil
experiment → treatment
experiment → observations

Import validation must ensure that referenced records exist or can be resolved.


---

25. Import workflow

The user-facing import workflow is:

1. Select file.


2. Read file metadata.


3. Validate format.


4. Validate schema.


5. Validate identifiers and references.


6. Validate controlled vocabulary values.


7. Validate structured values.


8. Detect conflicts.


9. Present import summary.


10. Allow cancellation.


11. Confirm import.


12. Apply changes.


13. Report result.



No garden data should be modified before validation reaches the commit stage.


---

26. Import preview

Before committing an import, the application should provide a useful summary.

Example:

> Import ready



Garden: Wellington Garden

Growing spaces
8 new
2 updated

Plants
24 new
6 updated

Activities
147 new

Observations
38 new

Warnings
2

Errors
0

The user should be able to inspect warnings and errors before committing.


---

27. Validation stages

Validation should occur in layers.

27.1 File validation

Check:

file exists;

file is readable;

format is recognised;

file is not corrupted.


27.2 Schema validation

Check:

required structures;

supported version;

valid field types;

valid relationships.


27.3 Vocabulary validation

Check:

canonical IDs;

active/deprecated values;

appropriate vocabulary ownership;

invalid or unknown values.


27.4 Referential validation

Check:

referenced records exist;

IDs are unique where required;

relationships are valid.


27.5 Domain validation

Check:

dates;

quantities;

dimensions;

geometry;

measurements;

logical constraints.


27.6 Conflict detection

Check whether imported records conflict with existing records.


---

28. Validation severity

Validation results use:

information
warning
error
critical

Information

Does not require action.

Warning

The import can potentially continue, but the user should understand the issue.

Error

One or more affected records cannot safely be imported without resolution or omission.

Critical

The import cannot safely proceed.


---

29. Import record status

Individual imported records may have:

pending
validated
warning
rejected
imported
updated
skipped
conflict

These statuses describe the import process.

They must not overwrite the normal status of the garden record being imported.


---

30. Unknown vocabulary values

If an import contains a vocabulary value unknown to the current application, the application must not silently substitute another value.

Possible responses:

recognise a documented deprecated value;

migrate it using a known compatibility mapping;

retain it as unresolved where the data model permits;

reject the affected record.


The application should explain what occurred.


---

31. Deprecated vocabulary values

If an imported value is deprecated but still understood:

1. recognise it;


2. identify the replacement if one exists;


3. migrate where safe;


4. preserve historical meaning;


5. report the migration where appropriate.



Example:

Imported value:
old_value

Mapped to:
new_value

A deprecated value must not be treated as invalid merely because it is no longer recommended for new records.


---

32. Missing vocabulary values

If an imported field contains no value:

preserve explicit unknown/not-recorded states where supplied;

use the field's defined default only where a default is semantically safe;

otherwise leave it unknown.


The application must not invent information.


---

33. Conflict types

Potential conflicts include:

duplicate identifiers;

same record changed in both datasets;

conflicting geometry;

conflicting measurements;

incompatible vocabulary values;

deleted versus updated records;

duplicate garden entities;

incompatible schema versions.


Conflicts should be classified rather than presented as generic errors.


---

34. Conflict handling

The application should prefer safe outcomes.

Possible options include:

keep existing;

use imported;

keep both;

review manually;

skip record.


The application must not silently overwrite existing garden history.


---

35. Duplicate records

The application should distinguish between:

Same record

The imported record has the same stable identity as an existing record.

Similar record

The records appear to describe the same real-world event but have different identities.

Similarity must not automatically be treated as identity.

Where uncertain, the user should be able to review the conflict.


---

36. Update semantics

When an imported record matches an existing stable identifier, the application may update the existing record if:

the schema permits it;

the import is valid;

the update does not destroy historical information;

the user has approved the import.


The application should report updated records in the import summary.


---

37. Historical corrections

Importing a corrected historical record must not unnecessarily erase the previous state.

Where the data model supports modification history, the correction should preserve:

original record;

corrected value;

modification reason;

modification date;

source of correction.


The exact mechanism is defined by the data model.


---

38. Import transaction

Import should behave as a transaction where practical.

The preferred behaviour is:

Read
  ↓
Validate
  ↓
Preview
  ↓
Confirm
  ↓
Commit

If a critical failure occurs during commit, the application should avoid leaving the garden in a partially corrupted state.


---

39. Partial import

V1 should support safe partial import where practical.

For example:

> 98 records can be imported.
2 records require attention.



The user may be allowed to:

import the valid records;

exclude problematic records;

cancel the entire import.


The default should favour preventing unintended data loss.


---

40. Import report

After import, the application should provide a summary.

Example:

> Import complete



98 records imported
12 records updated
3 records skipped
2 warnings
0 errors

The user should be able to inspect the affected records where practical.


---

41. Failed imports

If an import cannot safely proceed:

> Import could not be completed.



Then explain:

what prevented the import;

which records are affected;

what the user can do next.


Avoid exposing raw database or parser errors as the primary message.

Technical details may be available through:

> Details




---

42. Import safety

The application must not:

silently overwrite records;

silently discard invalid records;

silently convert unknown values;

silently change historical dates;

silently change measurements;

silently change spatial geometry;

silently mark plans as completed;

silently change provenance;

silently increase confidence;

silently convert observations into diagnoses.



---

43. Backup philosophy

A complete export should be usable as a practical backup.

The application should make it clear that:

> Complete Garden Export



is the appropriate choice for backup.

Selective exports should clearly indicate:

> This is not a complete backup.




---

44. Restore workflow

Restoring a garden from an export should use the normal import validation system.

The workflow should be:

Select backup
    ↓
Validate
    ↓
Review garden
    ↓
Confirm restore/import
    ↓
Commit

Restoring should not require an Internet connection.


---

45. Export integrity

Where practical, the application should provide integrity information such as:

file size;

record count;

export timestamp;

format version;

optional checksum.


The purpose is to help detect incomplete or corrupted files.


---

46. File naming

The application should generate readable filenames.

Example:

garden_planner_wellington_garden_2026-08-15.garden.json

The exact naming convention may evolve, but filenames should make it easy for users to identify:

application;

garden;

date;

export type where useful.



---

47. Large exports

The application should support larger gardens without requiring the user to understand technical limitations.

If an export becomes large:

progress should be shown;

the application should remain responsive where practical;

failure should be reported clearly;

the user should not receive a corrupted or incomplete file as though it were valid.



---

48. Bulk data entry

Import may be used as an advanced method of entering large quantities of information.

Potential future sources include:

CSV;

spreadsheet exports;

structured JSON;

application backups.


V1 should prioritise the canonical JSON format.

CSV/spreadsheet import should only be added when its mapping and validation can be made sufficiently reliable.


---

49. CSV considerations

If CSV import is added later, it must not attempt to represent the entire garden model as a single flat table.

Separate import templates should be considered for simple use cases such as:

plant lists;

planting records;

harvest records;

observations.


Complex relationships should remain supported by the canonical structured format.


---

50. Human-readable export

The canonical JSON export is intended primarily for data portability, not casual reading.

A future human-readable report may provide:

garden summary;

plant list;

growing-space list;

history;

harvest summary;

observations.


Such reports should not replace the canonical backup format.


---

51. Version compatibility

Every importable file must identify its format version.

The application should support importing older compatible versions.

Conceptually:

V1.0 → supported
V1.1 → supported
V1.2 → supported

Future versions should define compatibility explicitly.


---

52. Future-version imports

If a file comes from a newer application version than the current application supports:

The application must not pretend it can fully understand it.

It should:

1. identify the unsupported version;


2. determine whether a compatible subset can safely be imported;


3. warn about unsupported information;


4. allow cancellation;


5. avoid silently discarding unsupported data.



Where safe partial compatibility exists, it should be clearly identified.


---

53. Schema migrations

Schema migrations must be explicit.

A migration may:

rename an internal field;

convert a deprecated vocabulary value;

restructure a relationship;

add a newly required representation.


A migration must not fabricate information that did not exist in the source data.


---

54. Unknown future fields

Where technically practical, the importer should tolerate additional fields that it does not recognise when those fields are not required for safe interpretation.

Unknown information must not be silently represented as though it were understood.

For complete forward compatibility, future versions may preserve extension data separately.


---

55. Export of application knowledge

The application should not automatically export its entire internal knowledge base with every garden backup.

This would:

unnecessarily increase file size;

duplicate built-in knowledge;

complicate licensing/provenance;

create versioning problems.


Garden exports should reference system knowledge where practical.

A separate knowledge export format may be considered later if required.


---

56. Privacy

Exports may contain sensitive garden information, including:

exact location;

garden layout;

property information;

personal notes;

potentially sensitive observations.


The application should warn users before sharing an export if it contains exact or potentially sensitive location information.

The application must not automatically publish or upload exports.


---

57. Contribution data

Future community knowledge sharing must remain separate from ordinary garden backup/export.

A private garden export should not imply:

> Share this garden's observations.



Contribution must require an explicit user action and must follow the provenance/privacy model.


---

58. Importing community or external data

External datasets should not automatically be treated as personal garden observations.

Imported knowledge should retain its origin.

For example:

data_origin:
external_dataset

evidence_type:
community_observation

must remain distinct from:

data_origin:
user_observed


---

59. Security

Import files are untrusted input.

The application should:

validate file structure;

validate field types;

validate lengths and ranges;

reject malformed data;

avoid executing imported content;

avoid interpreting imported strings as executable instructions;

avoid exposing local files or secrets through import processing.


Imported data must be treated as data only.


---

60. Error handling

Errors should be written in user-oriented language.

Avoid:

> Null reference in PlantInstanceRelationship.



Prefer:

> One plant record refers to a growing space that could not be found.



Then:

> The affected plant was not imported.



Technical details may be available separately for troubleshooting.


---

61. Undo and recovery

Import is a significant operation.

Where practical, the application should support recovery from an incorrect import through:

transactional rollback;

import history;

undo;

restoring a pre-import backup.


The exact mechanism depends on the storage architecture.

At minimum, the application must not leave the garden in an unknowable partially modified state.


---

62. Export before major destructive operations

Where appropriate, the application may recommend creating an export before operations such as:

deleting the garden;

bulk replacement;

large imports;

major migrations.


This should be helpful rather than obstructive.


---

63. Import/export and offline operation

The following should work offline:

export;

complete garden backup;

import;

validation;

restore.


No network connection should be required for ordinary garden data portability.


---

64. User interface principles

Import/export UI should follow the broader UX principles:

progressive disclosure;

clear explanations;

no unnecessary technical terminology;

explicit consequences;

meaningful previews;

clear errors;

cancellation before commitment.


The user should not need to understand:

database tables;

entity relationships;

JSON structure;

schema internals;

vocabulary architecture.



---

65. Advanced terminology

Technical information may be available through an expandable details area.

For example:

> 2 records have unsupported vocabulary values.



Expanded:

Record: plant_1042
Field: lifecycle
Value: old_lifecycle_value
Status: deprecated
Replacement: perennial

The technical layer is useful for troubleshooting without becoming the primary experience.


---

66. Import/export and historical durability

The following must survive export/import where represented by the data model:

original event dates;

historical spatial relationships;

historical plantings;

observations;

measurements;

provenance;

confidence;

decisions;

experiments;

harvests;

losses;

corrections.


The goal is:

> Export → Import



should preserve the meaning of the garden, not merely its current appearance.


---

67. Minimum V1 export acceptance criteria

A complete garden export is acceptable when it can preserve:

Garden

identity;

location;

preferences.


Spatial model

growing spaces;

geometry;

dimensions;

relevant spatial relationships.


Plants

plant references;

plant instances;

cultivars;

locations;

planned/current/historical state.


Activities

activity type;

date/time;

context;

completion status;

quantities where applicable.


Observations

observation;

context;

date;

information state;

confidence where recorded.


Soil

observations;

measurements;

estimates;

provenance.


Planning

plans;

decisions;

recommendation records where retained.


Experiments

question;

hypothesis;

treatment/control;

observations;

outcomes.


Provenance

source;

origin;

evidence type;

confidence where applicable.



---

68. Minimum V1 import acceptance criteria

A V1 import is acceptable when it can:

1. recognise a valid export;


2. identify its version;


3. validate its structure;


4. validate vocabulary IDs;


5. validate relationships;


6. validate important structured values;


7. identify conflicts;


8. preview the result;


9. allow cancellation;


10. safely commit valid data;


11. preserve historical information;


12. report what happened.




---

69. Non-goals

V1 import/export does not attempt to provide:

automatic synchronisation between devices;

cloud backup;

community sharing;

real-time collaboration;

universal compatibility with third-party garden applications;

arbitrary database import;

automatic merging of unrelated gardens;

automatic reconciliation of contradictory horticultural knowledge;

complete export of internal application implementation details.


These may be addressed by future modules.


---

70. Relationship to other specifications

This document depends on:

the data model for entity structure and relationships;

the vocabulary specifications for canonical identifiers;

the UX specification for user interaction principles;

the recommendation specification for recommendation records;

the storage architecture for transactional behaviour.


This document does not redefine those systems.

Where a conflict exists, the underlying data model and canonical vocabulary definitions determine the actual stored representation.


---

71. Simplification principle

Import/export should preserve complexity internally while presenting simplicity externally.

The user should primarily experience:

> Export garden



and:

> Import garden



rather than a collection of technical database operations.

The system may perform extensive validation and migration behind these simple workflows.


---

72. Guiding principle

> Import/export must preserve the meaning and history of the garden, not merely copy its current data.



A successful export is one that allows the gardener to recover their garden without losing important knowledge.

A successful import is one that is safe, validated, explainable and explicit about anything it cannot preserve.