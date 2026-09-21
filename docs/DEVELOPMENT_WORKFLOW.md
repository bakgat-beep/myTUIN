# Development workflow

**Document:** `docs/DEVELOPMENT_WORKFLOW.md`
**Version:** 0.2
**Status:** Working specification
**Last updated:** September 2026

---

## 1. Purpose

Defines the repeatable command sequence for validating a change.

`PHASE_0_PROJECT_FOUNDATION.md` §18 requires a repeatable development
workflow. §30 item 26 names the sequence:

> format → static analysis → unit tests → data/repository tests → Compose tests

This document is the implementation guide §18 refers to. It is updated
as each stage is added.

---

## 2. The sequence

Run stages in order. Do not skip ahead: a formatting failure makes every 
later result harder to interpret.

format
↓
static analysis
↓
unit tests
↓
data / repository tests
↓
Compose tests


---

## 3. Commands

| Stage | Command | Device required |
| --- | --- | --- |
| Format — apply | `.\gradlew spotlessApply` | no |
| Format — verify | `.\gradlew spotlessCheck` | no |
| Static analysis | `.\gradlew detekt` | no |
| Unit tests | `.\gradlew testDebugUnitTest` | no |
| All local checks | `.\gradlew check` | no |
| Instrumented tests | `.\gradlew connectedDebugAndroidTest` | **yes** |
| Offline verification | `.\tools\Verify-Offline.ps1` | **yes** |

`check` runs `spotlessCheck`, `detekt` and `testDebugUnitTest`, in that
order.

`connectedDebugAndroidTest` runs every instrumented test: repository
tests, migration tests, component tests, and Compose UI tests. It is not
part of `check` because it requires a running emulator or device, and
`check` must pass on a machine without one.

`Verify-Offline.ps1` is not part of `check`. It requires a device, it
temporarily disables the device's connectivity, and it performs a
structural check against the release manifest. Run it before a release
or after any change to network-related code. See
`docs/OFFLINE_VERIFICATION.md` for the procedure and the interpretation
of results.

---

## 4. The two commands to run before committing

.\gradlew check
.\gradlew connectedDebugAndroidTest


`check` catches formatting, compilation and unit-test problems.
`connectedDebugAndroidTest` catches everything that needs an Android
runtime. Both are required before a commit that touches production code.

A commit that only changes documentation needs neither, but running
`check` costs little and confirms the build is still intact.

---

## 5. When a stage fails

**`spotlessCheck` fails.**
Run `spotlessApply`. Review the diff before committing it: the formatter
should only have changed whitespace, import order and line breaks. If it
changed anything semantic, stop and investigate.

**`detekt` fails.**
The HTML report at `app/build/reports/detekt/detekt.html` groups findings
by rule and file. A finding is resolved in one of three ways:
- fix the code;
- add a rule override in `config/detekt/detekt.yml` with a stated reason,
  if the rule conflicts with a project idiom;
- add `@Suppress("RuleName")` with a comment, if the finding is a genuine
  exception in one place.

Raising `maxIssues` is not one of the options.

**`testDebugUnitTest` fails.**
The failing test names the file and line. Unit tests have no Android
dependency; they run in a few seconds.

**`connectedDebugAndroidTest` fails.**
Check that a device or emulator is attached (`adb devices`). A failure
that is not about device availability is a real failure.

---

## 6. Continuous integration

Not configured in Phase 0. When it is, the pipeline runs `check` on every
push, and `connectedDebugAndroidTest` on a schedule or on pull requests
that touch production code.

---

## 7. Relationship to other documents

- `CONTRIBUTION_AND_DEVELOPMENT_GUIDELINES.md` §18 names this file as the
  implementation guide.
- `TESTING_STRATEGY.md` §57 defines the sequence and notes that the exact
  command sequence is an implementation detail.
- `PHASE_0_PROJECT_FOUNDATION.md` §30 item 26 requires it.

---

## 8. Phase 0 closure status

Phase 0 closed in September 2026. All 26 items of
`PHASE_0_PROJECT_FOUNDATION.md` §30 and all acceptance criteria in §31
are met.

### Pinned tool versions at closure

| Tool | Version | Purpose |
| --- | --- | --- |
| AGP | 8.7.3 | Android Gradle plugin |
| Kotlin | 2.1.0 | Language and compiler |
| Compose BOM | 2025.01.00 | Compose libraries |
| Room | 2.6.1 | Persistence |
| Hilt | 2.54 | Dependency injection |
| DataStore | 1.1.7 | Display preferences |
| MapLibre | 11.8.7 | Map rendering |
| Spotless | 8.9.0 | Formatter |
| ktlint | 1.5.0 | Kotlin style engine |
| detekt | 1.23.8 | Static analysis |
| JUnit 5 | 5.11.3 | `src/test` |
| JUnit 4 | 4.13.2 | `androidTest` |
| MockK | 1.13.13 | Mocking |
| Turbine | 1.2.0 | Flow testing |
| Robolectric | 4.14.1 | JVM Room tests |

Versions are pinned in `gradle/libs.versions.toml`. When bumping any
of them, re-run `.\gradlew check` and
`.\tools\Verify-Offline.ps1` and review the diff for behavioural
change, not just compilation success.

### Application and schema versions

- App version: `0.1.0`
- Database schema: `4`

The schema version is defined once, in `GARDEN_DATABASE_VERSION` in
`GardenDatabase.kt`. It is referenced by the `@Database` annotation
and by the debug diagnostic footer.

### What Phase 0 delivered

A working Android foundation: layered packages, Room with explicit
migrations, canonical vocabulary enforcement, repository and use-case
patterns, Hilt DI, the MapLibre proof of concept, an offline
verification method, and a green `check` pipeline that runs
formatting, static analysis, and the full test suite.

### What Phase 0 did not deliver

Phase 0 is a foundation. No V1 gardening workflow is end-to-end
functional in the UI. The five primary screens show empty states; the
Garden tab shows the MapLibre proof of concept in debug builds. The
vertical slice — Create Garden → Create Growing Space → Persist →
Reload → Verify history-safe edit — passes as an instrumented test,
not as a user journey.

Three specific gaps are recorded, none of them hidden:

1. **Export does not exist.** The Room/DataStore split required by
   `DEC-042` is in place, and the repository shapes support export.
   No export or import code exists yet. That is Phase 7 work per
   `IMPLEMENTATION_PLAN.md` §9.
2. **MapLibre drag-vertex-to-move is not implemented.** The other
   five §22 interactions are demonstrated. The gap is documented in
   `MapLibrePoc.kt`.
3. **Phase 0 artefacts are debug-gated and scheduled for removal.**
   `MapLibrePoc`, `DiagnosticsFooter`, and the debug `INTERNET`
   permission all disappear when their replacements land.

### Entering Phase 1

Phase 1 is the full V1 data and persistence foundation
(`IMPLEMENTATION_PLAN.md` §3). Its exit criterion is "core entities
and relationships survive create/update/archive/migration tests
without data loss."

Phase 1 will need specification documents that Phase 0 did not:

- `V1_DATABASE_SCHEMA.md` §§19–60 (Activity, Observation, Measurement,
  SoilRecord, Problem, Harvest, Plan, Recommendation, UserDecision,
  Experiment, InboxItem, Source)
- `ACTIVITY_VOCABULARIES.md`
- `SOIL_VOCABULARIES.md`
- `PROBLEM_VOCABULARIES.md`
- `IMPORT_EXPORT_SPECIFICATION.md`
- `RECOMMENDATION_ENGINE.md`
- `KNOWLEDGE_BASE_STRATEGY.md`

The same per-step discipline applies: ask for the specific sections a
step depends on, do not invent identifiers, do not resolve ambiguities
silently.
