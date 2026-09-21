# Offline verification

**Document:** `docs/OFFLINE_VERIFICATION.md`
**Version:** 0.1
**Status:** Working specification
**Last updated:** September 2026

---

## 1. Purpose

Proves that myTUIN operates without network connectivity.

`PHASE_0_PROJECT_FOUNDATION.md` §19 requires six operations to work
offline. §30 item 23 requires the verification to be documented and
reproducible, "not an informal manual check". §31 requires that a
second developer can repeat it without verbal instruction.

---

## 2. Two phases

Offline capability is verified in two distinct phases. Each proves
something the other cannot.

### Phase 1 — Structural

The release build's merged manifest must not declare the `INTERNET`
permission.

This is the strongest guarantee available: an app without the
`INTERNET` permission cannot open a network socket, regardless of what
its code attempts. The check confirms that a future commit has not
silently added the permission to the release manifest.

The `INTERNET` permission is declared only in
`app/src/debug/AndroidManifest.xml`, where it exists so the MapLibre
proof of concept can fetch its demo style. It is absent from the main
manifest and therefore from the release build.

### Phase 2 — Behavioural

The instrumented test suite must pass with the device offline.

Every instrumented test that exercises a repository, a DAO, a
migration, or a UI component runs against local storage. If any of them
attempted a network call, disabling connectivity would make it fail.

Neither phase is sufficient alone. A structural pass without a
behavioural pass would allow an app that has `INTERNET` enabled by some
future dependency. A behavioural pass without a structural pass would
allow an app that happens to work offline today but could be changed to
require connectivity tomorrow without any test noticing.

---

## 3. What is verified

`PHASE_0_PROJECT_FOUNDATION.md` §19 requires six operations.

| §19 requirement | Exercised by |
| --- | --- |
| 1. Launch without network access | App install and launch via `connectedDebugAndroidTest` |
| 2. Open the local database | Every repository test, every migration test |
| 3. Read local data | `PlantRepositoryTest`, `GardenRepositoryTest`, `GrowingSpaceRepositoryTest`, `GrowingSpaceHistoryRepositoryTest`, `GardenPreferenceRepositoryTest` |
| 4. Create local data | Insert paths in the same suite |
| 5. Update local data | `GardenRepositoryTest.updateLocation_*`, `GrowingSpaceHistoryRepositoryTest`, `GrowingSpaceRepositoryTest` |
| 6. Display local data | `MapLibrePocTest`, `ShellAccessibilityTest`, `EmptyStateTest`, `ErrorStateTest`, `DiagnosticsFooterTest`, `DiagnosticsOnHomeTest` |

`TESTING_STRATEGY.md` §43 names further operations — Quick Add,
observations, harvests, planning, recommendations, search, filtering.
These features do not yet exist. When they do, their steps add
instrumented tests, and those tests are covered by Phase 2 with no
change to this procedure.

---

## 4. How to run

From the project root:

.\tools\Verify-Offline.ps1

The script performs both phases in sequence and prints a pass or fail
for each. It restores the device's network state when it finishes,
including on failure.

### Prerequisites

- An Android emulator or device connected via `adb devices`.
- The device is not already in airplane mode when the script starts, or
  the script records that it was and leaves it that way.
- `adb` is on `PATH`, or `-AdbPath` is supplied.

### Flags

| Flag | Effect |
| --- | --- |
| `-SkipStructural` | Skip Phase 1 |
| `-SkipBehavioural` | Skip Phase 2 |
| `-AdbPath <path>` | Use a specific adb executable |

Both phases are run by default.

---

## 5. Expected result

Device: emulator-5554
Prior network state: airplane_mode=0 wifi=1 data=1

Phase 1 — structural
Release manifest declares INTERNET permission: no
Debug manifest declares INTERNET permission: yes
PASS

Phase 2 — behavioural
Disabling connectivity...
Connectivity method: cmd connectivity airplane-mode
Verifying connectivity is off... confirmed
Running connectedDebugAndroidTest...
All instrumented tests passed offline.
PASS

Restoring network state...
airplane_mode=0 wifi=1 data=1
confirmed

Both phases passed.

---

## 6. When a phase fails

### Phase 1 fails

The release manifest declares `INTERNET`. Most likely cause: a new
dependency's manifest contributes it via manifest merging. Diagnose
with:

.\gradlew :app:processReleaseManifest
Get-Content app\build\intermediates\merged_manifest\release\processReleaseManifest\AndroidManifest.xml |
Select-String -Pattern 'INTERNET'


If the permission is present, either:
- remove the dependency that requires it;
- exclude the permission via `tools:node="remove"` in the main manifest,
  which requires a decision about whether the dependency's network use
  is acceptable;
- add an explicit decision record.

This phase failing is not a bug in the verification. It is the
verification catching a change.

### Phase 2 fails

An instrumented test failed with the device offline. The failing test
names itself. Diagnose by:

1. Reading the test output. If the failure is a network exception or a
   timeout, the test requires connectivity and should not.
2. Running the same test with the device online to confirm it passes.
   If it fails online too, the failure is unrelated to offline
   operation.

---

## 7. Known exceptions

### MapLibre proof of concept

`MapLibrePoc` loads MapLibre's demo style from
`https://demotiles.maplibre.org/style.json`. Offline, the fetch cannot
succeed.

**First-run finding (September 2026):** `MapLibrePocTest` passed with
the device offline. MapLibre's `setStyle(url)` with an unreachable URL
does not throw; the composable renders without error. The proof of
concept degrades silently — a user with no connectivity would see a
blank map with no explanation.

Implication for the real Garden screen: it must not rely on the
MapLibre SDK to surface network failure. It needs either to bundle
tiles for offline use, which `DEC-039` records as a supported MapLibre
capability, or to detect style-load failure and present an unavailable
state. Otherwise the release app — which cannot make network calls at
all — would always render an empty map.

This is not a Phase 0 blocker. `MapLibrePoc` is scheduled for deletion
when the real Garden screen lands. It is recorded here so the future
step inherits the finding rather than rediscovering it.

### Release build

Phase 1 checks the source manifest, not a built APK. Building and
inspecting the release APK is a stronger check but requires a signing
configuration that Phase 0 does not yet have. When a release signing
config exists, Phase 1 can be strengthened to inspect the APK with
`aapt2 dump permissions`. Recorded as a follow-up.

---

## 8. Relationship to other documents

- `PHASE_0_PROJECT_FOUNDATION.md` §19, §30 item 23, §31 (Offline)
- `TESTING_STRATEGY.md` §43, §66
- `V1_TECHNICAL_ARCHITECTURE.md` §50, §52
- `CORE_ARCHITECTURE.md` §55
- `V1_SCOPE.md` §48
- `CONTRIBUTION_AND_DEVELOPMENT_GUIDELINES.md` §99
- `docs/DEVELOPMENT_WORKFLOW.md` — where this procedure sits in the
  broader workflow

---

## 9. What this procedure does not prove

- That the app behaves correctly with a captive-portal network (one
  that accepts a connection but blocks traffic). Only air-gapped
  offline is tested.
- That the app behaves correctly when connectivity is lost mid-operation.
  Tests run entirely offline; they do not transition between states.
- That the app does not attempt network access that fails silently. A
  library could attempt a fetch, catch the exception, and continue. The
  structural check is the guarantee against this, not the behavioural
  check.
- That the app works on a device with no SIM and no Wi-Fi hardware.
  Emulators and typical test devices have both.
