# 9f context bundle — run from repo root:  .\9f-context.ps1
$out = 'app/9f-context.txt'
$files = @(
  # --- MapLibre POC (understand, replace, or delete) ---
  'app/src/main/java/com/mytuin/gardenplanner/ui/garden/debug/MapLibrePoc.kt',
  'app/src/main/java/com/mytuin/gardenplanner/ui/garden/debug/MetresToWgs84.kt',
  'app/src/test/java/com/mytuin/gardenplanner/ui/garden/debug/MetresToWgs84Test.kt',
  'app/src/androidTest/java/com/mytuin/gardenplanner/ui/garden/debug/MapLibrePocTest.kt',

  # --- Garden tab currently ---
  'app/src/main/java/com/mytuin/gardenplanner/ui/garden/GardenScreen.kt',

  # --- Domain ---
  'app/src/main/java/com/mytuin/gardenplanner/domain/model/garden/Garden.kt',
  'app/src/main/java/com/mytuin/gardenplanner/domain/repository/GardenRepository.kt',
  'app/src/main/java/com/mytuin/gardenplanner/domain/usecases/garden/ObserveGarden.kt',
  'app/src/main/java/com/mytuin/gardenplanner/domain/usecases/garden/ObserveGrowingSpacesInGarden.kt',

  # --- Manifests (permissions state) ---
  'app/src/main/AndroidManifest.xml',
  'app/src/debug/AndroidManifest.xml',

  # --- Theme / map styling ---
  'app/src/main/java/com/mytuin/gardenplanner/ui/theme/Color.kt',

  # --- Map-relevant docs (may be large — see note below) ---
  'docs/DECISION_LOG.md',
  'docs/V1_SCREEN_SPECIFICATION.md'
)

'9f context bundle' | Set-Content $out
"Generated: $(Get-Date)" | Add-Content $out
'' | Add-Content $out

$missing = @()
foreach ($f in $files) {
  if (-not (Test-Path $f)) { $missing += $f; continue }
  "===== FILE: $f =====" | Add-Content $out
  '' | Add-Content $out
  Get-Content $f | Add-Content $out
  '' | Add-Content $out
}

'' | Add-Content $out
'===== MISSING FILES =====' | Add-Content $out
if ($missing.Count -eq 0) { '(none)' | Add-Content $out }
else { $missing | ForEach-Object { $_ | Add-Content $out } }

Write-Host "Wrote $out"
Write-Host "Missing: $($missing.Count)"
$missing | ForEach-Object { Write-Host "  - $_" }