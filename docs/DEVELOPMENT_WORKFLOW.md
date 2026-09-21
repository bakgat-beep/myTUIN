# Development workflow

**Document:** `docs/DEVELOPMENT_WORKFLOW.md`
**Version:** 0.1
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
