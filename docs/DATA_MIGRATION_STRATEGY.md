Garden Planner & Manager — Data Migration Strategy

Document: docs/DATA_MIGRATION_STRATEGY.md
Version: 0.1
Status: Working specification
Last updated: August 2026

1. Purpose

This document defines the strategy for preserving, migrating, validating and evolving Garden Planner & Manager user data as the application develops from V1 into future versions.

The central objective is:

> Application evolution must not unnecessarily destroy, corrupt, reinterpret or orphan the user's accumulated garden data.



The application is intended to become more valuable over time because the user accumulates:

garden plans;

garden structure;

planting records;

tasks;

observations;

events;

outcomes;

preferences;

historical information.


That accumulated information is therefore a core product asset.

Data migration is consequently a first-class architectural concern rather than an implementation detail to be addressed only when a database schema changes.


---

2. Core migration principles

All future migration work should follow these principles.

2.1 Preserve user data

Existing meaningful user data should survive application upgrades wherever reasonably possible.

2.2 Never silently discard data

If information cannot be migrated automatically, the application should identify the problem rather than silently deleting the information.

2.3 Preserve historical meaning

Migration must not silently change the meaning of historical records.

2.4 Prefer additive evolution

Where practical, evolve the data model by adding new capabilities rather than destructively replacing existing concepts.

2.5 Make migrations deterministic

Given the same source database and migration version, the migration should produce the same result.

2.6 Version migrations

Every schema migration must have an identifiable version.

2.7 Validate after migration

A migration is not complete merely because the application opens the database successfully.

The resulting data must be validated.

2.8 Keep migrations separate from normal application logic

Migration code should have a clear responsibility and should not become entangled with ordinary feature behaviour.

2.9 Test migrations before release

Migration paths must be tested using representative databases, including realistic historical data.

2.10 Preserve portability

The data model and migration strategy should support reliable export/import as an additional protection against data loss.


---

3. Scope

This document covers migration of persistent application data, including:

database schema;

user-generated records;

relationships;

controlled vocabulary references;

knowledge references where applicable;

application metadata stored with user data;

historical records;

migration metadata;

imported/exported data where relevant.


It does not define:

the complete data model;

individual entity structures;

the detailed recommendation engine;

the complete knowledge-base schema;

the UI for migration.


Those responsibilities belong to their respective documents.


---

4. What counts as user data

For migration purposes, user data includes information whose loss would materially reduce the value of the user's garden record.

Examples include:

gardens;

garden areas;

plants;

plantings;

plans;

planned actions;

completed actions;

observations;

historical events;

harvest records;

notes;

environmental observations;

user-entered measurements;

user classifications;

user preferences that materially affect their garden data;

relationships between user records.


Future modules may add additional durable user data.

Those modules must define their migration requirements before introducing persistent storage.


---

5. Types of persistent information

The application should distinguish conceptually between several classes of persistent data.

5.1 Core user data

Information directly created or maintained by the user.

Examples:

gardens;

areas;

plantings;

observations;

plans.


This receives the highest preservation priority.

5.2 Historical data

Records describing what happened in the past.

Historical information should generally be immutable or carefully versioned.

5.3 Reference data

General information supplied by the application.

Examples:

controlled vocabularies;

plant knowledge;

rules;

classifications.


Reference data may evolve independently from user data.

5.4 Application metadata

Information required for application operation.

Examples:

schema version;

migration version;

internal indexes;

cached information.


Application metadata may be recreated where appropriate.


---

6. Data ownership and migration priority

A useful migration priority hierarchy is:

1. user-created historical data;


2. user-created current data;


3. user-created relationships;


4. user preferences;


5. user-specific derived information;


6. application reference data;


7. caches and rebuildable data.



When migration decisions require trade-offs, higher-priority data should receive greater protection.


---

7. Schema versioning

The persistent data schema must have an explicit version.

For example:

schema_version = 1

A future release might use:

schema_version = 2

The exact implementation mechanism will depend on the selected persistence technology, but the conceptual requirement is mandatory.


---

8. Migration versioning

Schema version and application version should not be assumed to be identical.

For example:

Application: V2.4

Database schema: 7

Knowledge base: KB-2028.02

This distinction allows:

application releases without schema changes;

multiple schema migrations within one application release;

knowledge updates without database migrations.



---

9. Sequential migrations

Migrations should normally be sequential.

For example:

Schema 1 → Schema 2 → Schema 3 → Schema 4

rather than requiring every version to contain a completely separate migration path:

Schema 1 → Schema 4

Schema 2 → Schema 4

Schema 3 → Schema 4

Sequential migrations reduce the number of migration paths that need to be maintained.


---

10. Migration from old versions

The application should support upgrading from the range of previous versions that the project explicitly promises to support.

The supported upgrade range should be documented for each release.

The project should not implicitly promise unlimited support for arbitrarily old databases.


---

11. Direct migration versus staged migration

The default strategy should be staged migration through known schema versions.

However, very old versions may eventually require special handling.

If a direct migration is introduced for performance or maintenance reasons, it must still:

preserve supported data;

be tested;

have a documented migration path;

produce the same valid target schema.



---

12. Additive schema evolution

Where practical, new functionality should initially be added without destroying existing fields.

For example:

An existing record may gain:

a new optional field;

a new related entity;

a new relationship;

additional metadata.


This is generally safer than replacing the original representation.


---

13. Avoid destructive schema changes

Destructive changes require explicit review.

Examples include:

deleting a column;

changing the meaning of a field;

replacing identifiers;

merging entities;

removing a relationship;

changing units;

changing semantic interpretation.


Before making such a change, determine whether historical data can still be represented accurately.


---

14. Nullability and unknown values

Migration must distinguish between:

a value that was never recorded;

a value that is known to be negative;

a value that is not applicable;

a value that cannot be determined from the old schema.


A migration must not fill missing information with arbitrary defaults simply to satisfy a new required field.


---

15. Default values

Defaults may be used when:

the default is semantically correct;

the field genuinely has a safe default;

applying the default cannot misrepresent historical information.


Defaults should not be used to manufacture false historical facts.

For example, if an old record did not record whether an action was completed, migration should not automatically mark it as completed merely because a new schema requires a status.


---

16. Historical records

Historical records receive special protection.

Migration should preserve, wherever possible:

original date/time;

original event type;

original subject;

original values;

original relationships;

original notes;

original observations;

relevant provenance.


If a new representation cannot fully express an old record, the old information should be retained in another supported form rather than discarded.


---

17. Historical semantics

Migration must preserve meaning, not merely data fields.

For example, if an old schema recorded:

> "planned planting date"



and the new schema distinguishes:

planned date;

actual planting date;


the old value must remain a planned date.

It must not be migrated into the actual date simply because that field is available.


---

18. Plans versus events

The distinction between planned and actual activity must survive migration.

A migration must never automatically turn:

planned;

suggested;

scheduled


into:

completed;

observed;

confirmed.


This is a core data-integrity requirement.


---

19. Observations

Observations must remain observations.

If a user recorded:

> "Leaves appeared yellow."



migration must not convert this into a diagnosis merely because a later schema supports diagnoses.

The original observation should remain intact.


---

20. User-entered text

User-entered text should receive particularly strong preservation guarantees.

Examples:

notes;

comments;

observations;

descriptions.


Text should not be silently rewritten merely to conform to new terminology.

Where structured classification is added later, the original text should normally remain available.


---

21. Units and measurements

Changes to measurement representation require special care.

If the application changes units, migration must preserve the underlying quantity accurately.

For example:

A stored value in one unit may be converted into another unit during migration.

The conversion must:

use a known conversion;

preserve appropriate precision;

record the resulting unit;

avoid rounding that materially changes the meaning.


The original value may be retained where necessary for auditability.


---

22. Dates and times

Date/time migrations must preserve the original semantic meaning.

The application should distinguish where relevant between:

date-only information;

local date/time;

timestamp;

recurring date;

planned date;

actual event date.


Timezone conversion must not accidentally move an event to a different calendar date.


---

23. Location data

Where garden locations or geographic information are stored, migration must preserve:

location identity;

relevant coordinates;

units/reference system where applicable;

user-entered location descriptions.


Location data must not be silently converted into a different geographic meaning.


---

24. Identifiers

Persistent identifiers should be stable.

When possible, migration should preserve existing IDs.

Changing an entity's display name should not require changing its persistent identifier.


---

25. Foreign keys and relationships

Relationships are part of user data.

A migration must validate that:

referenced records still exist;

foreign-key relationships remain valid;

deleted/merged entities are handled explicitly;

orphaned records are detected.


A migration that preserves all individual rows but breaks their relationships is not considered successful.


---

26. Entity merging

If two entities are later determined to represent the same canonical entity, they may need to be merged.

This requires careful handling of:

user records;

references;

historical records;

identifiers;

aliases;

provenance.


Merging should never silently discard information.


---

27. Entity splitting

If one old entity must become multiple new entities, migration may not be able to determine the correct destination automatically.

In such cases the migration should:

preserve the original information;

make the ambiguity explicit;

apply a safe mapping where possible;

defer user clarification where appropriate.


It should not invent certainty.


---

28. Controlled vocabulary changes

Controlled vocabularies are persistent semantic dependencies.

If a vocabulary value changes, migration must determine whether the change is:

a display-label change;

an identifier change;

a semantic change.


A display-label change generally should not require data migration.

A semantic change does.


---

29. Vocabulary aliases

Where a vocabulary identifier must eventually be retired, the system should preferably maintain a mapping from:

old identifier

→

new identifier

rather than immediately deleting the old value.

This allows older exports and databases to remain interpretable.


---

30. Canonical English identifiers

Because English is the canonical data language, migrations must preserve canonical identifiers independently of localisation.

A language change must never be treated as a database migration.

For example:

full_sun

remains:

full_sun

whether the UI displays it in:

English;

Afrikaans;

another future language.



---

31. Localisation data

Translation data may evolve independently from core user data.

A translation change should not require rewriting the user's garden records.

If a translation is removed or changed, the underlying canonical concept should remain intact.


---

32. Reference-data changes

Reference data may change independently from user data.

For example:

A future knowledge update may change a plant's general recommended conditions.

That should not rewrite the user's historical observation that they experienced something different.


---

33. Knowledge versioning

Knowledge-base versions should be tracked separately where practical.

A database migration should not automatically assume that the current knowledge version is the same as the version that existed when a historical record was created.


---

34. Recommendations and migration

If recommendations become persistent historical records, the system should preserve sufficient information to understand how the recommendation was generated.

Where practical, this should include:

recommendation timestamp;

rule identifier;

relevant inputs;

knowledge version;

recommendation status.


A future knowledge update should not silently rewrite historical recommendations.


---

35. Derived data

Some application data may be derived from durable records.

Examples may include:

indexes;

summaries;

calculated statistics;

cached recommendations;

search indexes.


Where derived data can be safely regenerated, it should generally not receive the same migration priority as original user data.


---

36. Rebuildable data

If a data structure can be completely regenerated from authoritative persistent data, it should preferably be treated as rebuildable.

This reduces migration complexity.

For example:

Authoritative records

→ rebuild

search index

The search index itself does not necessarily need to be migrated.


---

37. Cached data

Caches should generally not be treated as authoritative.

A migration may delete and rebuild a cache if doing so is safe.

The project should document which data is:

authoritative;

derived;

cached.



---

38. Migration transactionality

Where supported by the persistence technology, migrations should be transactional.

The preferred behaviour is:

Migration begins

↓

Migration operations execute

↓

Validation succeeds

↓

Migration commits

If migration fails:

Migration rolls back

rather than leaving the database partially migrated.


---

39. Partial migration protection

The application must protect against interrupted migration caused by:

application termination;

device shutdown;

battery loss;

storage problems;

unexpected errors.


A partially migrated database should not be treated as a successfully upgraded database.


---

40. Migration state

The database should be able to identify whether a migration is:

not started;

in progress;

completed;

failed/requires recovery.


The exact mechanism depends on the persistence implementation.


---

41. Backup before migration

Before performing a potentially destructive or irreversible migration, the application should ideally create or ensure the availability of a recoverable backup.

The exact backup mechanism depends on the final storage architecture.

At minimum, the project should have a strategy for recovering the user's data if a migration fails.


---

42. Export as a migration safety mechanism

The application's import/export capability should provide an additional recovery mechanism.

A user should ideally be able to:

1. export data;


2. upgrade;


3. import/recover data if necessary.



This is not a substitute for reliable migrations, but it provides an additional layer of protection.


---

43. Migration testing

Migration tests should use actual databases from previous schema versions wherever practical.

Tests should verify:

migration succeeds;

data remains present;

relationships remain valid;

historical meaning remains correct;

new fields receive appropriate values;

invalid assumptions are not introduced;

the resulting database conforms to the target schema.



---

44. Migration test fixtures

The project should maintain representative test databases for supported historical schema versions.

Fixtures should include examples such as:

empty database;

minimal garden;

garden with multiple areas;

plantings;

completed events;

planned events;

observations;

notes;

relationships;

unusual/edge-case values;

incomplete records.



---

45. Realistic migration fixtures

At least some fixtures should resemble real-world usage rather than only artificially perfect records.

This should include:

missing optional information;

older data structures;

partially completed plans;

historical records;

unusual dates;

user-entered text;

records created before newer features existed.



---

46. Migration invariants

Each migration should define invariants that must remain true.

Examples:

number of gardens does not unexpectedly decrease;

historical observations remain present;

no completed event becomes merely planned;

no planned event becomes completed;

persistent IDs remain stable where required;

no orphaned relationships are introduced;

user-entered notes remain unchanged.


The exact invariants should be defined for each migration.


---

47. Record-count validation

Record counts can be useful as a basic migration check.

However, identical record counts are not sufficient evidence of successful migration.

For example, one record could be duplicated while another is lost.

Therefore record-count checks should be supplemented with:

ID checks;

relationship checks;

field-level checks;

semantic checks.



---

48. Checksums and integrity validation

Where useful, the migration system may use checksums or hashes to detect unintended changes to important data.

This is particularly useful for:

exported files;

migration fixtures;

critical historical records.


The exact implementation is deferred until the storage technology is finalised.


---

49. Migration idempotence

Where practical, migration operations should be designed so that accidentally attempting the same migration twice does not corrupt the database.

The primary protection should still be migration-version tracking.


---

50. Migration logging

Migration activity should produce useful diagnostic information.

At minimum, logs should identify:

source schema version;

target schema version;

migration steps;

success/failure;

validation result;

error details where safe.


Logs must not unnecessarily expose sensitive user data.


---

51. User-facing migration errors

If migration fails, the user should receive a clear explanation.

The UI should avoid presenting technical database jargon as the primary message.

A useful message should communicate:

that the upgrade could not be completed;

whether the user's existing data remains safe;

what the user should do next;

whether support/recovery is required.



---

52. Never hide migration failure

The application must not silently:

start with an empty database;

discard incompatible records;

create a new database beside the old one;

overwrite the original data


merely because migration failed.

A migration failure is preferable to silent data loss.


---

53. Recovery mode

The application should eventually have a safe recovery path for serious migration failures.

Potential recovery actions include:

retry migration;

restore backup;

export old data;

repair/rebuild derived information;

contact support.


The exact UI is a future implementation concern.


---

54. Unsupported old versions

If a database is too old to migrate automatically, the application should identify the limitation explicitly.

Possible approaches include:

requiring an intermediate upgrade;

importing through a supported export format;

providing a conversion utility.


The application should not pretend that unsupported migration succeeded.


---

55. Data deletion

Deletion is distinct from migration.

A migration must not interpret schema changes as permission to delete user data.

For example:

A feature being removed does not automatically mean historical data created by that feature should be deleted.


---

56. Deprecated features

When a feature is deprecated, its data should generally remain available until there is a deliberate decision about:

migration;

archival;

export;

deletion.


Removing a feature from the UI does not necessarily justify removing its data.


---

57. Archived data

Future versions may need to archive rarely used information.

Archived data must remain:

identifiable;

recoverable where promised;

associated with its original meaning.


Archiving must not become an excuse for silent deletion.


---

58. Data model evolution and module independence

A future module should own or clearly define its persistent data.

Adding a new module should not require widespread changes to unrelated tables/entities unless there is a genuine domain relationship.

This supports the project's modular architecture.


---

59. Module migration responsibility

Each module that introduces persistent data should document:

its entities;

schema version requirements;

migration requirements;

dependencies;

data-preservation requirements;

downgrade/recovery considerations.


The core migration system should coordinate these migrations without embedding the entire module's business logic into the core.


---

60. Optional modules

Optional modules should not make core migration dependent on functionality that is not installed or enabled.

A user's core garden data must remain migratable even if an optional module is:

disabled;

removed;

not yet installed;

temporarily unavailable.



---

61. Module removal

If a future module is removed from the application, its persistent data must be handled deliberately.

Possible strategies include:

retaining the data;

migrating it to a replacement module;

exporting it;

archiving it.


The default assumption should be preservation rather than deletion.


---

62. Cross-module relationships

If a module creates relationships to core entities, those relationships must be explicitly defined.

Migration must know what happens if:

the module changes;

the core entity changes;

the module is removed;

the relationship becomes obsolete.


Cross-module dependencies should be minimised.


---

63. Import/export compatibility

The import/export format should have its own version.

For example:

Export format: 1

This should not necessarily equal:

Database schema: 5

An export may remain compatible across multiple application versions.


---

64. Importing older exports

The application should support importing older supported export formats.

The importer should:

identify the format version;

validate the input;

migrate it into the current internal representation;

report incompatibilities;

preserve data where possible.



---

65. Import validation

Imported data must be validated before it modifies existing user data.

Validation should include:

format version;

schema;

identifiers;

relationships;

controlled vocabularies;

required fields;

data types;

duplicate handling.



---

66. Import isolation

Where practical, imported data should first be processed in an isolated/staging context.

Only after validation succeeds should it be merged into the user's persistent data.

The exact implementation will depend on the final persistence architecture.


---

67. Import conflicts

Importing data into an existing garden may produce conflicts.

Examples:

same persistent ID;

same entity represented differently;

conflicting values;

duplicate observations;

duplicate events.


The conflict strategy must be explicit.

The application should not silently overwrite existing user data.


---

68. Export completeness

An export intended as a full backup should contain enough information to reconstruct the user's meaningful garden data.

This includes relationships, not merely individual entities.

A file containing only plant names and garden names would not qualify as a complete backup.


---

69. Export of derived data

Derived information does not necessarily need to be exported if it can be reconstructed exactly.

However, the export specification must identify which data is:

authoritative and exported;

derived and rebuildable;

cached and omitted.



---

70. Export and localisation

Exports should use canonical identifiers rather than translated UI labels as the authoritative machine-readable representation.

A future import process should therefore remain independent of the language used when the export was created.


---

71. Export and knowledge versions

Where historical recommendations or other derived knowledge-dependent records are exported, the relevant knowledge version should be preserved where necessary to maintain interpretability.


---

72. Round-trip testing

The project should test:

Database

→ export

→ import

→ reconstructed database

and verify that meaningful user data remains equivalent.

This is one of the most important long-term data-integrity tests.


---

73. Semantic equivalence

Round-trip testing should not require byte-for-byte equality.

For example:

database indexes may differ;

internal ordering may differ;

cached data may differ.


The requirement is that the meaningful user data and relationships remain equivalent.


---

74. Migration and backups

Backups and migrations serve different purposes.

Backup

Protects against unexpected loss.

Migration

Changes the structure/representation of data while preserving its meaning.

Both are required.


---

75. Backup retention

The exact backup retention policy is deferred until the storage and backup architecture is finalised.

However, the system should avoid immediately destroying the only recoverable copy of pre-migration data when a risky migration occurs.


---

76. Migration of settings

Application settings should be classified according to whether they are:

user preferences;

device-specific settings;

application defaults;

temporary UI state.


Only meaningful user preferences should necessarily receive long-term migration guarantees.

Temporary UI state may be discarded or rebuilt.


---

77. Migration of user preferences

User preferences that materially affect application behaviour should be preserved where practical.

If a setting is renamed or replaced, the migration should map it deliberately.

If no safe equivalent exists, the application should use a documented default rather than inventing a potentially misleading value.


---

78. Migration of permissions and security data

Security-sensitive data should be treated separately from ordinary garden data.

Authentication credentials, tokens and similar information should not be migrated as ordinary user records unless the security architecture explicitly requires it.

The final security architecture should define the relevant rules.


---

79. Migration and device changes

The project should distinguish between:

upgrading an application on the same device;

restoring data to a new device;

importing an export;

synchronising data between devices.


These may use different mechanisms but should ultimately protect the same durable user data.


---

80. Future synchronisation

If cloud/device synchronisation is added later, migration must not assume that there is only ever one copy of a user's data.

Future synchronisation architecture will need to address:

conflict resolution;

identifiers;

timestamps;

versioning;

deletion;

concurrent edits.


This is deliberately outside V1.


---

81. Migration and concurrency

V1 is expected to be primarily local/offline.

If future versions support simultaneous editing or synchronisation, the migration strategy will need additional versioning mechanisms.

Those should be designed when synchronisation becomes a real requirement.


---

82. Schema deprecation

Before removing a schema element, the project should determine:

1. whether any supported data still uses it;


2. whether it can be mapped;


3. whether it is historical;


4. whether export must preserve it;


5. whether users need access to the old information.



Only then should removal be considered.


---

83. Long-lived compatibility

The project should aim for durable compatibility without requiring every future application version to retain every obsolete implementation detail forever.

The strategy is:

preserve meaningful user data;

preserve semantic meaning;

provide explicit migration paths;

retire obsolete implementation structures when safe.



---

84. Migration documentation

Every schema-changing release should document:

old schema version;

new schema version;

migration purpose;

affected entities;

transformations;

assumptions;

data-preservation guarantees;

known limitations;

test coverage.



---

85. Migration decision records

Significant migration decisions should also be recorded in:

docs/DECISION_LOG.md

Examples include:

deliberately removing obsolete data;

changing identifier semantics;

choosing a new unit representation;

changing historical semantics;

deciding how a difficult legacy structure is preserved.



---

86. Migration code organisation

Migration code should be separated from ordinary feature code as far as practical.

A conceptual structure might be:

Migration system

→ migration 1 → 2

→ migration 2 → 3

→ migration 3 → 4

rather than scattering migration logic throughout application services.

The exact directory structure will be defined by the technical architecture.


---

87. Migration and modular architecture

Migration boundaries should generally follow domain/module boundaries where practical.

However, cross-entity migrations must be coordinated centrally when relationships require it.

The goal is not to make every migration completely independent; the goal is to minimise unnecessary coupling.


---

88. Migration performance

Migration performance matters because users may eventually accumulate substantial amounts of garden history.

The system should avoid unnecessarily expensive operations on every upgrade.

However, correctness and data preservation take precedence over optimisation.


---

89. Large datasets

Future users may have:

many years of history;

many garden areas;

many observations;

extensive notes;

substantial knowledge-linked records.


Migration tests should eventually include larger representative datasets.


---

90. Migration progress

If a migration can take long enough to require user-visible progress, the UI should provide clear progress information.

It should not appear that the application has frozen.

The exact threshold depends on actual migration performance.


---

91. Interrupting migration

Where possible, migrations should not be cancellable once a non-transactional destructive phase has begun.

If cancellation is supported, it must leave the database in a known recoverable state.

The default should favour safe completion over arbitrary interruption.


---

92. Migration failure severity

Migration failures should be categorised.

Conceptually:

Recoverable

The migration can be retried safely.

Recoverable with backup

The database requires restoration or recovery.

Data ambiguity

The application cannot safely determine how to interpret some legacy data.

Unsupported

The source version is outside the supported migration range.

Corrupt source

The existing database itself is damaged or invalid.

The exact implementation can evolve later.


---

93. Data ambiguity

When legacy data cannot be safely mapped to a new structure, preserving the original information is preferable to making an unsupported guess.

Possible approaches include:

preserving a legacy field;

creating an "unknown" state;

retaining a migration note;

requesting user clarification;

postponing conversion.



---

94. Never manufacture historical certainty

This is a fundamental rule.

Migration must never convert uncertainty into certainty simply because the new schema requires a more specific value.

For example:

Old:

planting_date = unknown

must not become:

planting_date = 2026-08-15

because a date field is now required.


---

95. Migration and validation rules

The migration system should distinguish between:

Schema validation

Is the database structurally valid?

Data validation

Are values valid?

Semantic validation

Do values still mean what they are supposed to mean?

All three may be necessary.


---

96. Post-migration validation

After migration, validation should verify at minimum:

target schema version;

database integrity;

entity counts;

relationship integrity;

required fields;

controlled vocabulary validity;

historical record integrity;

absence of unexpected orphan records.



---

97. Migration acceptance criteria

A migration should not be considered complete until:

the target schema is reached;

migration reports success;

validation succeeds;

supported user data is preserved;

critical relationships remain intact;

tests pass.



---

98. Migration regression suite

The project should maintain a migration regression suite covering every supported source schema.

When a new migration is added, older migration tests should continue to pass.

This protects against accidentally breaking previously supported upgrade paths.


---

99. Migration testing in CI

Migration tests should eventually run automatically as part of the development/test pipeline.

A release candidate should not be considered ready if supported migration tests fail.


---

100. Migration and release process

A release containing a schema change should explicitly identify:

schema change;

migration availability;

supported upgrade paths;

backup/recovery expectations;

testing status.


This should eventually be incorporated into docs/RELEASE_AND_VERSIONING.md.


---

101. Data migration versus knowledge migration

The project should distinguish:

User data migration

from:

Knowledge migration/update.

A new knowledge version may require no user database migration.

Conversely, a user database schema change may occur without changing gardening knowledge.


---

102. Data migration versus localisation

Changing the UI language should not require migrating the user's garden data.

Localisation is a presentation-layer concern.

Canonical identifiers remain stable.


---

103. Migration and application modules

When a module is upgraded independently, its data migration should remain isolated where practical.

The module should expose a clear migration contract to the core application.

This supports the project's broader goal of semi-independent module development.


---

104. Future module migration template

Every future persistent module should eventually document:

Module identity

What module owns the data?

Persistent entities

What data is stored?

Dependencies

Which core entities does it reference?

Schema versions

What versions exist?

Migration path

How does its data evolve?

Preservation requirements

What information must never be lost?

Derived data

What can be rebuilt?

Export/import

What must be included?

Testing

What migration tests are required?


---

105. Data ownership

Each persistent field/entity should have a clear conceptual owner.

A module should not modify another module's persistent data directly merely because it needs some information from it.

Instead, modules should use defined interfaces or relationships.

This reduces migration coupling.


---

106. Shared core entities

Some entities will naturally be used by multiple modules.

Shared entities require particular care because schema changes can have a broad blast radius.

Before changing such an entity, identify:

consuming modules;

relationships;

historical records;

exports;

recommendation dependencies;

migration implications.



---

107. Stable interfaces

Where multiple modules depend on a core entity, its external interface should be kept relatively stable.

Internal implementation may evolve behind the interface where practical.

This reduces the number of modules affected by migrations.


---

108. Migration and application upgrades

The application upgrade process should conceptually be:

Detect existing version

↓

Validate current database

↓

Create recovery point where appropriate

↓

Run required migrations sequentially

↓

Validate migrated database

↓

Update schema version

↓

Start application

The exact implementation may vary.


---

109. Failed upgrade behaviour

If migration cannot complete safely:

The application should prefer:

preserve existing data + report failure

over:

launch with incomplete/empty data.

This should be treated as a core reliability requirement.


---

110. Data integrity priority

When faced with a choice between:

preserving feature compatibility;

preserving user data;


preserving user data generally takes priority.

A feature may temporarily be unavailable.

Irrecoverable garden history cannot easily be recreated.


---

111. Migration and deletion of obsolete reference data

Obsolete reference data may sometimes be removed if:

no user data depends on it;

exports remain interpretable;

historical records do not require it;

the change is documented.


Otherwise it should be retained or aliased.


---

112. Tombstones and retired identifiers

Where necessary, retired identifiers may be retained as tombstones or aliases.

This allows historical records and older exports to remain interpretable.

The exact mechanism will be determined by the data architecture.


---

113. Migration and duplicate prevention

Migrations must avoid creating duplicate records.

This is particularly important when:

adding new entities;

importing data;

merging reference data;

reconstructing relationships.


Stable identifiers should be preferred over matching by display name.


---

114. Matching by name

Names should not normally be treated as reliable unique identifiers.

For example:

Two plants may share a common name.

One plant may have several names.

Migration should therefore prefer stable identifiers and explicit mappings.


---

115. Migration and aliases

Aliases can help preserve compatibility when names or terminology change.

An old identifier or name may map to a canonical concept without requiring historical records to be rewritten.


---

116. Data preservation when a feature changes

If a feature evolves substantially, first identify what underlying information remains valuable.

The project should preserve the underlying data even if the UI representation changes.

For example:

A simple "watering note" feature might later become a structured watering event system.

The original information should be migrated into the richer structure without losing the original meaning.


---

117. Data preservation when a feature is replaced

Replacing a feature should not automatically imply replacing its historical data.

The project should attempt:

old data

→ new representation

where a semantically valid mapping exists.

If no valid mapping exists, preserve the old information separately.


---

118. Migration and future-proofing

Future-proofing does not mean predicting every future feature.

Instead, the project should:

preserve stable identifiers;

preserve meaningful history;

separate modules;

avoid destructive semantics;

version schemas;

maintain migration paths;

maintain exportability.


These provide resilience without requiring the current schema to anticipate every future requirement.


---

119. Migration documentation in source control

Migration definitions, tests and documentation should be version-controlled with the application.

A migration should never exist only as an informal instruction in a development chat.


---

120. Migration reproducibility

A developer should be able to obtain:

a known source database;

a known application/migration version;

the migration code;


and reproduce the expected target database.

This is important for debugging and future maintenance.


---

121. Migration auditability

For significant migrations, it should be possible to determine:

what changed;

why it changed;

which migration performed the change;

what data transformations occurred;

how the migration was tested.


This information should come from source-controlled migration code and documentation rather than relying on memory.


---

122. Migration rollback

Rollback should be considered separately from backup/recovery.

Some migrations may be reversible.

Others may not be safely reversible.

The project should never assume that every migration can simply be rolled back.

Where a migration is irreversible, a recoverable pre-migration copy becomes particularly important.


---

123. Forward-only migrations

The preferred long-term strategy may be forward-only migrations:

N → N+1

with recovery provided through:

backups;

exports;

restore mechanisms.


This avoids maintaining complex reverse migrations.

The final choice should be confirmed against the selected database technology.


---

124. Migration and test data

Migration tests should never modify the user's actual production database.

Test migrations must operate on:

disposable copies;

fixtures;

test databases.



---

125. Migration safety principle

The migration system should follow:

> Do not modify production data until the migration path and recovery strategy are understood.



This applies particularly to experimental development.


---

126. Developer workflow for schema changes

Before changing persistent data structures, developers should:

1. identify affected entities;


2. identify affected modules;


3. identify historical records;


4. identify import/export implications;


5. identify knowledge/recommendation implications;


6. design the migration;


7. create migration tests;


8. update relevant documentation;


9. implement the schema change;


10. test upgrade scenarios.




---

127. Documentation dependencies

A schema change may require updates to:

DATA_MODEL.md;

CORE_VOCABULARIES.md;

VOCABULARY_INDEX.md;

IMPORT_EXPORT_SPECIFICATION.md;

V1_TECHNICAL_ARCHITECTURE.md;

module specifications;

TESTING_STRATEGY.md;

DECISION_LOG.md;

this document.


The exact set depends on the change.


---

128. V1 migration requirements

Even though V1 is the first release, its database should already be designed with migration in mind.

At minimum V1 should establish:

schema versioning;

stable identifiers;

clear entity ownership;

migration infrastructure;

data integrity constraints;

export capability where included in V1;

migration test strategy.



---

129. V1 should not create migration debt unnecessarily

Avoid introducing:

anonymous identifiers;

implicit relationships;

duplicated authoritative data;

hard-coded semantics;

irreversible transformations;

UI strings as database identifiers.


These choices create avoidable migration problems later.


---

130. V1 database as a long-term foundation

The V1 database should be treated as the first version of a long-lived data system.

It does not need to predict every future requirement.

It does need to provide:

clear semantics;

stable identity;

extensibility;

preservation of history;

safe evolution.



---

131. Data model review before implementation

Before finalising the V1 database implementation, the project should review the data model specifically for migration risk.

Questions should include:

Can every important entity be uniquely identified?

Are historical records distinguishable?

Are planned and actual events separate?

Are relationships explicit?

Are unknown values representable?

Are modules appropriately separated?

Are translated strings excluded from canonical identity?

Can future fields be added safely?

Can old records retain their meaning?



---

132. Migration readiness checklist

Before V1 development is considered migration-ready:

[ ] Database schema has an explicit version.

[ ] Persistent entities have stable identifiers.

[ ] Entity relationships are explicit.

[ ] Historical records have defined semantics.

[ ] Planned and actual records are distinguishable.

[ ] Unknown values can be represented.

[ ] Controlled vocabularies are version-conscious.

[ ] Localisation is separated from canonical data.

[ ] Import/export requirements are documented.

[ ] Migration architecture is defined.

[ ] Migration tests are planned.

[ ] Data-loss scenarios are identified.

[ ] Recovery strategy is documented.



---

133. Future migration readiness checklist

Before each significant future release:

[ ] Determine whether the schema changes.

[ ] Determine whether reference data changes.

[ ] Identify affected modules.

[ ] Identify affected historical records.

[ ] Define the migration.

[ ] Define invariants.

[ ] Create/update migration fixtures.

[ ] Test supported source versions.

[ ] Test relationships.

[ ] Test import/export compatibility.

[ ] Test failure/recovery behaviour where relevant.

[ ] Review data-loss risks.

[ ] Update documentation.

[ ] Record significant decisions.



---

134. Migration quality standard

A migration should be considered high quality when it is:

deterministic;

testable;

documented;

recoverable;

minimally destructive;

semantically correct;

appropriately performant;

isolated from unrelated application functionality.



---

135. Central migration principle

The most important migration rule is:

> The application may change how it represents information, but it must not casually change what the user's historical information means.




---

136. Long-term objective

The Garden Planner & Manager should allow a user's garden history to become increasingly valuable over time.

A user should be able to move from:

V1

→ V2

→ V3

→ V4

without having to start their garden history again from scratch.

Future functionality should build upon accumulated information rather than repeatedly resetting it.


---

137. Final strategy

The project's migration strategy can therefore be summarised as:

Stable identity


Explicit schema versions


Sequential migrations


Preservation of historical meaning


Modular ownership


Strong validation


Testing against real historical data


Reliable export/recovery

→

A garden data system capable of evolving for many years without sacrificing the user's accumulated knowledge and history.