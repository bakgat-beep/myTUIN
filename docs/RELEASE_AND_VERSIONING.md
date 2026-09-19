Garden Planner & Manager — Release and Versioning

Document: docs/RELEASE_AND_VERSIONING.md
Version: 0.1
Status: Working specification
Last updated: August 2026

1. Purpose

This document defines how the Garden Planner & Manager should be versioned, released, upgraded and maintained over time.

The primary objectives are:

protect user data;

protect the reliability of the core product;

make releases understandable;

make upgrades predictable;

support future expansion from V1 to V2 and beyond;

prevent changes to optional features from destabilising the core application;

ensure persistent data survives application upgrades;

provide a clear framework for deciding what constitutes a release.



---

2. Release philosophy

The project should favour:

small, reliable, understandable releases

over:

large, risky releases containing many unrelated changes.

A release should represent a coherent improvement to the product.


---

3. Core release principle

No release should knowingly compromise the reliability or integrity of the core product merely to introduce additional functionality.

When there is a conflict between:

a new feature; and

core reliability,


core reliability takes priority.


---

4. Versioning scheme

The project should use semantic versioning as the primary application versioning model:

MAJOR.MINOR.PATCH

For example:

1.0.0

1.1.0

1.1.1


---

5. Version component meanings

MAJOR

A major version represents a significant product or compatibility milestone.

Examples:

1.x.x → 2.0.0

may indicate:

substantial product evolution;

intentionally breaking changes;

major architectural evolution;

a major user-facing redesign;

significant changes to supported behaviour.


A major version change should not be used merely because many small features were added.


---

6. MINOR

A minor version adds functionality while maintaining the intended compatibility of the existing product.

Examples:

1.0.0 → 1.1.0

may represent:

a new feature module;

significant enhancement;

additional supported workflows;

expanded functionality;

new optional capabilities.


Minor releases should preserve existing user data.


---

7. PATCH

A patch version represents a maintenance release.

Examples:

1.1.0 → 1.1.1

may contain:

bug fixes;

security fixes;

performance fixes;

small UX corrections;

reliability improvements.


Patch releases should not introduce substantial new functionality.


---

8. Pre-release versions

Before a stable release, development versions may use identifiers such as:

1.0.0-alpha.1

1.0.0-beta.1

1.0.0-rc.1

The exact development workflow may evolve.

The important distinction is that pre-release software must not be treated as equivalent to a stable release.


---

9. Alpha

An alpha release is primarily for development and early testing.

Alpha software may contain:

incomplete features;

unstable UX;

significant bugs;

changing data structures.


Alpha releases should not be treated as fully reliable production releases.


---

10. Beta

A beta release should represent a substantially usable product.

Remaining issues may exist, but:

core functionality should be stable;

major workflows should work;

data handling should be tested;

known limitations should be documented.



---

11. Release candidate

A release candidate should be considered feature-complete for the intended release.

Only appropriate fixes should normally be introduced after reaching release-candidate status.


---

12. Stable release

A stable release should meet the project's definition of done.

At minimum:

core functionality works;

critical user journeys work;

persistent data is safe;

migration paths are tested;

important regressions have been addressed;

release documentation is complete.



---

13. V1 milestone

The first stable major release should be:

1.0.0

V1 represents the first release of the defined core product.

It does not need to contain every planned future module.


---

14. V1 philosophy

V1 should provide a complete and useful core experience rather than an incomplete collection of future functionality.

The architecture should nevertheless preserve reasonable boundaries for later expansion.


---

15. V1 does not mean "finished"

1.0.0 should not imply that the product is complete.

It means:

> The core product has reached a stable, intentionally supported baseline.



Future functionality can then be added without redefining the foundation unnecessarily.


---

16. V1 baseline

Once 1.0.0 is released, its behaviour should become an important compatibility baseline.

Future releases should preserve:

user data;

established canonical identifiers;

core workflows;

important user expectations,


unless a deliberate breaking change is approved.


---

17. Data compatibility is more important than application compatibility

For this project, persistent user data is particularly important.

A future application release should not casually invalidate:

garden plans;

planting records;

observations;

historical information;

tasks;

notes;

measurements;

user-created structures.



---

18. Data migration principle

An application upgrade must not silently discard existing user data.

If a schema change requires transformation, the application should perform an explicit migration according to:

docs/DATA_MIGRATION_STRATEGY.md


---

19. Upgrade principle

The intended normal upgrade path is:

Existing application + existing user data

↓

New application version

↓

Migration if necessary

↓

Same user data, represented in the new schema

↓

New functionality available


---

20. Migration before feature use

If a release requires a database migration, migration should be completed successfully before the application relies on the new schema.


---

21. Migration failure

A failed migration must be treated as a serious error.

The application should not simply continue while pretending that migration succeeded.

Where practical, the application should:

preserve the original data;

report the problem clearly;

prevent unsafe partial operation;

provide a recovery path.



---

22. Backups before risky migrations

For migrations that present meaningful risk, an appropriate backup or rollback strategy should exist before modifying persistent data.

The exact implementation depends on the storage architecture.


---

23. Backward compatibility

Where practical, new application versions should continue to understand data created by previous supported versions.

This is particularly important for:

stored identifiers;

historical records;

imported data;

exported data.



---

24. Forward compatibility

The project should not assume that older application versions can understand data created by newer versions.

Where this is not supported, it should be clearly documented.


---

25. Export as a safety mechanism

The import/export system should eventually provide users with a reliable way to preserve and transfer their data independently of a particular application installation.

This supports:

backup;

migration;

device transfer;

future application versions;

recovery.


See:

docs/IMPORT_EXPORT_SPECIFICATION.md

and:

docs/DATA_MIGRATION_STRATEGY.md


---

26. Canonical identifiers and versioning

Canonical identifiers should remain stable wherever possible.

Changing an identifier can affect:

existing records;

imports;

exports;

recommendations;

localisation;

historical data.


Identifier changes therefore require deliberate migration planning.


---

27. Identifier deprecation

If an identifier must eventually be replaced:

1. identify all existing uses;


2. define the replacement;


3. support migration;


4. update knowledge/rules;


5. update localisation;


6. update import/export;


7. test historical records;


8. document the change.




---

28. Never change identifier meaning silently

An existing identifier should not be reused to mean something substantially different.

If the meaning changes materially, introduce a new identifier and migrate where appropriate.


---

29. Database schema version

The persistent data layer should maintain its own schema version.

Application version and database schema version are related but should not be assumed to be identical.

For example:

Application:

1.4.0

Database:

schema 7

This is perfectly acceptable.


---

30. Why database versioning is separate

A single application release may:

not change the database at all;

require one migration;

require several migrations;

introduce data transformations.


Therefore database schema evolution needs its own explicit tracking.


---

31. Migration chain

Where practical, migrations should form an understandable sequence.

For example:

schema 1 → schema 2 → schema 3 → schema 4

The project should avoid relying on undocumented manual database transformations.


---

32. Migration testing

Every migration should be tested using representative data from the previous schema.

Tests should verify:

records survive;

relationships survive;

identifiers remain correct;

historical data remains meaningful;

new fields receive appropriate values;

no unexpected records disappear.



---

33. Empty database testing

Migration testing should also account for users who have little or no data.

The application should handle:

completely new installations;

empty databases;

partially populated databases;

heavily populated databases.



---

34. Realistic historical data testing

Where practical, migration tests should include realistic long-lived garden data.

This is important because the application is intended to accumulate information over years.


---

35. Release notes

Each user-facing release should have release notes.

Release notes should communicate:

what changed;

what was fixed;

what was added;

any important migration considerations;

any known limitations;

any action users need to take.



---

36. Release notes should be user-oriented

Release notes should describe meaningful user impact rather than internal implementation details.

For example:

Better:

"Garden observations can now include soil moisture."

rather than:

"Added ObservationRepository.saveSoilMoisture()."


---

37. Internal technical notes

Where technical changes are significant, they may also be recorded separately for developers.

The user-facing release notes should remain understandable to ordinary users.


---

38. Breaking changes

A breaking change is any change that materially invalidates an established contract.

Examples may include:

incompatible data formats;

changed import/export semantics;

removal of supported workflows;

changed identifiers;

unsupported API contracts;

substantial behaviour changes.



---

39. Breaking changes require deliberate approval

Breaking changes should not occur accidentally as part of routine feature development.

They should be explicitly identified and assessed.


---

40. Major version consideration

Breaking changes may justify a major version increment.

However, the project should not automatically classify every internal refactor as a major release.

The important question is whether the supported contract has materially changed.


---

41. Deprecation before removal

Where practical, functionality should be deprecated before removal.

A deprecation process should:

1. identify the deprecated feature;


2. explain why;


3. provide a replacement where appropriate;


4. preserve compatibility temporarily;


5. remove it only in an appropriately planned release.




---

42. Removing optional features

Optional features can sometimes be removed without requiring a major release if their removal does not break supported core behaviour or data.

However, the impact should still be assessed.


---

43. Core feature removal

Removing or substantially changing a core feature requires much greater scrutiny.

It should involve:

product review;

user impact assessment;

data impact assessment;

migration analysis;

documentation;

explicit decision recording.



---

44. Feature additions

New features should normally enter through minor releases once the application has reached stable V1.

For example:

1.0.0 → 1.1.0


---

45. Feature modules

Future modules should ideally be introduced without requiring fundamental changes to unrelated core modules.

A successful module addition should feel like:

core + module

rather than:

rewrite core because module exists.


---

46. Module release independence

Where practical, module development may proceed independently.

However, a module should only become part of a stable release when:

its dependencies are stable;

integration is tested;

core functionality remains reliable;

documentation is updated.



---

47. Experimental modules

Experimental modules should not automatically become part of the stable product.

They may be developed separately until they meet the required stability standard.


---

48. Release branches

The exact version-control branching strategy may evolve with project size.

The project should nevertheless maintain a clear distinction between:

active development;

release candidates;

stable releases.


Avoid introducing a complicated branching model before it is necessary.


---

49. Release tagging

Stable releases should be tagged in version control where version control is used.

Tags should correspond clearly to the application version.

For example:

v1.0.0

v1.1.0

v1.1.1


---

50. Reproducible releases

A release should, as far as practical, be reproducible from its source and documented build environment.

This helps with:

debugging;

regression analysis;

future maintenance.



---

51. Build metadata

Where practical, builds should be identifiable by:

application version;

build number;

release channel;

relevant commit/version identifier.


This makes user-reported problems easier to diagnose.


---

52. Build number

The application may maintain a separate monotonically increasing build number where required by the target platform.

The build number is not a substitute for semantic application versioning.


---

53. Development builds

Development builds should be clearly distinguishable from stable releases where practical.

They may include:

debug indicators;

development-only logging;

test data;

experimental functionality.


Such builds must not be confused with production releases.


---

54. Release channels

The project may eventually support channels such as:

development;

alpha;

beta;

stable.


The number of channels should remain limited until there is a real need for them.


---

55. Release gates

A release should pass appropriate gates before being considered stable.

At minimum:

Functional gate

Core user journeys work.

Data gate

Existing user data is preserved.

Migration gate

Required migrations succeed.

Testing gate

Required tests pass.

Documentation gate

Relevant documentation is current.


---

56. Core regression gate

Every release should specifically test the core product.

New functionality must not be assumed safe merely because its own tests pass.


---

57. Upgrade testing

Before a release containing schema changes, test an upgrade from at least one representative previous version.

For important milestones, test upgrades from multiple supported versions.


---

58. Fresh-install testing

Every release should also be tested as a fresh installation.

This catches problems that upgrade testing may not reveal.


---

59. Upgrade matrix

As the project grows, an upgrade test matrix may become useful.

For example:

From	To	Required

1.0.x	1.1.x	Yes
1.1.x	1.2.x	Yes
1.x	2.x	If supported
Unsupported legacy version	Current	No, unless explicitly supported


The exact matrix should be defined when release history becomes substantial.


---

60. Supported upgrade range

The project should eventually define which previous versions can upgrade directly to the current release.

If an old version is no longer directly supported, an intermediate migration path or export/import process may be required.


---

61. No destructive upgrades

Application upgrades should not intentionally destroy user data.

If a migration cannot preserve some information, that limitation must be identified before release and explicitly documented.


---

62. Data-loss risk escalation

Any change that could cause user data loss should be considered release-blocking until adequately addressed.


---

63. Rollback

Application rollback should be considered separately from database rollback.

It may be possible to reinstall an earlier application version while still being unable to safely downgrade the database.

Therefore:

Application downgrade ≠ database downgrade.


---

64. Database downgrade

Database downgrade should not be assumed to be supported.

If a migration is one-way, the project should clearly document that.


---

65. Safe rollback strategy

Where a release carries significant migration risk, the release process should define how users can recover if the upgrade fails.

This may involve:

backups;

exported data;

migration snapshots;

transactional migration mechanisms.


The implementation should follow the actual storage architecture.


---

66. Failed release

If a serious problem is discovered after release:

1. assess severity;


2. determine whether user data is affected;


3. stop or limit distribution if necessary;


4. identify the cause;


5. prepare a corrective release;


6. communicate appropriately.




---

67. Hotfix releases

Critical problems may be fixed through a patch release.

For example:

1.2.0 → 1.2.1

Hotfixes should remain focused on the critical problem wherever possible.


---

68. Security fixes

Security fixes may require expedited release procedures.

A security issue should not be delayed merely to bundle it with unrelated features.


---

69. Release prioritisation

When preparing a release, prioritise:

1. data integrity;


2. core reliability;


3. critical security;


4. serious regressions;


5. planned functionality;


6. minor improvements.




---

70. Release scope control

Once a release is approaching completion, avoid adding unrelated features simply because they are nearly ready.

This reduces late-stage regression risk.


---

71. Release freeze

A release may enter a temporary feature freeze before stable publication.

During the freeze:

new features are deferred;

fixes are prioritised;

testing is concentrated;

documentation is finalised.



---

72. Release candidate discipline

Once an RC is published, only changes that materially improve release readiness should normally be accepted.


---

73. Version numbers and documentation

Whenever the application version changes, relevant documentation should be checked for references to the previous version.

Particularly:

release notes;

migration documentation;

architecture decisions;

support information.



---

74. Documentation versioning

Project documents should retain their own version/status information where appropriate.

A documentation revision does not necessarily imply an application release.

For example:

CORE_ARCHITECTURE.md may change from version 0.4 to 0.5 without changing the application version.


---

75. Specification changes versus implementation changes

A specification change should be distinguished from an implementation change.

For example:

Specification:

"The user can record soil moisture."

Implementation:

"Added SoilMoistureRepository."

The two should not be conflated.


---

76. Decision log integration

Significant release-related decisions should be recorded in:

docs/DECISION_LOG.md

Examples:

changing supported upgrade versions;

introducing a major release;

changing versioning rules;

removing a feature;

changing migration compatibility.



---

77. Release documentation package

A substantial release should have an identifiable documentation set including, as appropriate:

release notes;

migration notes;

known issues;

testing status;

version information.



---

78. Release checklist

Before publishing a stable release:

[ ] Version number confirmed.

[ ] Release scope confirmed.

[ ] Core product tested.

[ ] New functionality tested.

[ ] Regression tests passed.

[ ] Fresh installation tested.

[ ] Upgrade tested where applicable.

[ ] Database migration tested where applicable.

[ ] Import/export tested where applicable.

[ ] User data preservation verified.

[ ] Release notes written.

[ ] Known issues documented.

[ ] Documentation updated.

[ ] Version-control tag created where applicable.

[ ] Build identified correctly.



---

79. Major release checklist

For a major release:

[ ] Breaking changes explicitly identified.

[ ] User impact assessed.

[ ] Data impact assessed.

[ ] Migration path defined.

[ ] Backup/recovery strategy reviewed.

[ ] Deprecations reviewed.

[ ] Core workflows revalidated.

[ ] Import/export compatibility reviewed.

[ ] Documentation comprehensively updated.

[ ] Decision log updated.



---

80. Minor release checklist

For a minor release:

[ ] New functionality identified.

[ ] Core functionality regression-tested.

[ ] Data compatibility assessed.

[ ] Migration implemented if required.

[ ] Tests added.

[ ] Documentation updated.

[ ] Release notes prepared.



---

81. Patch release checklist

For a patch release:

[ ] Problem clearly identified.

[ ] Fix narrowly scoped.

[ ] Regression test added where practical.

[ ] Core functionality tested.

[ ] No unintended feature changes introduced.

[ ] Release notes prepared.



---

82. Release notes template

A release may use the following structure:

Version X.Y.Z

Added

New functionality.


Improved

Improvements to existing functionality.


Fixed

Important bug fixes.


Data / Migration

Any changes affecting stored data.


Important

Any user action required.


Known Issues

Known limitations.



---

83. Version history

The project should eventually maintain a concise version history.

For each stable version, record:

version;

release date;

major additions;

important fixes;

migration requirements.



---

84. Long-term compatibility

The project should aim for:

> Long-lived user data with evolving application functionality.



The application should be designed so that a user can build a substantial history over many years without repeatedly having to restart their garden records because the software changed.


---

85. V2 principle

V2 should build upon V1 rather than treating V1 user data as disposable.

A V2 release should ideally look like:

V1 data


migration where necessary


new capabilities

rather than:

new application


empty database


---

86. Future major versions

The same principle applies beyond V2.

Major versions may substantially change the application, but they should preserve user data wherever technically and practically possible.


---

87. Data longevity principle

The project should consider user data to have a substantially longer expected lifetime than any particular application release.

Therefore:

Application versions are temporary.

User garden history is persistent.


---

88. Final release principle

The ultimate release objective is:

A user should be able to update the Garden Planner & Manager confidently, knowing that new functionality will not unnecessarily compromise the core product or erase the history they have built.

Every release should therefore be judged against four questions:

1. Does it preserve user data?


2. Does it preserve core reliability?


3. Does it provide the intended improvement?


4. Can we explain and support the change?



If the answer to all four is yes, the release is on the right path.