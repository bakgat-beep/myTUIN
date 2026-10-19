#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Detects and repairs corrupt Room schema JSON files.

.DESCRIPTION
    Room writes one JSON file per schema version under
    app/schemas/<package>/ when KSP runs. An interrupted build can
    leave a schema file empty or truncated. On the next build KSP
    fails with "Empty schema file" or "JsonSyntaxException /
    EOFException", and the build cannot proceed until the bad file
    is deleted and KSP is rerun.

    Detection is based on JSON validity, not file size. A truncated
    file may be several KB; a complete file may be small.

    SAFETY: this script takes no action by default. In Interactive
    mode, pressing Enter at a prompt means SKIP, not delete. To
    delete or back up a corrupt file you must explicitly type D or
    B. Use -Mode Report for a completely non-interactive dry run.
    Use -Mode Force to act without prompting.

.PARAMETER Path
    Schema directory, relative to the repository root. Defaults to
    the standard Room schema path for this project.

.PARAMETER Mode
    Interactive  — default; prompts for each corrupt file, default
                   is Skip.
    Force        — delete or back up without prompting.
    Report       — report only, take no action.

.PARAMETER Backup
    When set, moves corrupt files to a timestamped backup folder
    instead of deleting them. In Interactive mode you may also
    choose [B]ackup per-file even without this flag.

.PARAMETER ThenCheck
    When set and any corrupt file was fixed, run
    `gradlew clean check` after the repair completes.

.EXAMPLE
    .\tools\Repair-RoomSchemas.ps1
    # Dry run with a prompt per file; default is skip.

.EXAMPLE
    .\tools\Repair-RoomSchemas.ps1 -Mode Report
    # Report only, no prompts, exit code 0 (clean) or 2 (corrupt).

.EXAMPLE
    .\tools\Repair-RoomSchemas.ps1 -Mode Force -Backup
    # Back up every corrupt file, no prompts.

.EXAMPLE
    .\tools\Repair-RoomSchemas.ps1 -Mode Force -ThenCheck
    # Delete corrupt files, then run gradlew clean check.

.NOTES
    Exit codes:
      0 — no corrupt files found
      1 — corrupt file(s) found and fixed
      2 — corrupt file(s) found but not fixed
      3 — bad input (schema directory not found)

    Works on Windows PowerShell 5.1 and PowerShell 7+.
#>

[CmdletBinding()]
param(
    [string]$Path = 'app/schemas/com.mytuin.gardenplanner.data.database.GardenDatabase',

    [ValidateSet('Interactive', 'Force', 'Report')]
    [string]$Mode = 'Interactive',

    [switch]$Backup,

    [switch]$ThenCheck
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# --- Resolve paths -------------------------------------------------------

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptDir
$schemaDir = Join-Path $repoRoot $Path

if (-not (Test-Path -LiteralPath $schemaDir -PathType Container)) {
    Write-Host "ERROR: schema directory not found: $schemaDir" -ForegroundColor Red
    exit 3
}

$jsonFiles = @(Get-ChildItem -LiteralPath $schemaDir -Filter '*.json' -File)

if ($jsonFiles.Count -eq 0) {
    Write-Host "No schema files found in $schemaDir" -ForegroundColor Yellow
    exit 0
}

# --- Detection -----------------------------------------------------------

function Get-SchemaProblem {
    param(
        [Parameter(Mandatory = $true)]
        [System.IO.FileInfo]$File
    )

    if ($File.Length -eq 0) {
        return 'empty file (0 bytes)'
    }

    # Parse with ConvertFrom-Json. Compatible with PS 5.1 (returns
    # PSCustomObject) and PS 7+ (same by default). No -AsHashtable.
    try {
        $content = Get-Content -LiteralPath $File.FullName -Raw -ErrorAction Stop
        $json = ConvertFrom-Json -InputObject $content -ErrorAction Stop
    } catch {
        return "not valid JSON: $($_.Exception.Message)"
    }

    # Property existence via PSObject.Properties. Works on both
    # PSCustomObject (PS 5.1) and PS 7+ output.
    $databaseProp = $json.PSObject.Properties['database']
    if ($null -eq $databaseProp) {
        return "missing 'database' field"
    }
    $database = $databaseProp.Value
    if ($null -eq $database) {
        return "'database' field is null"
    }

    $versionProp = $database.PSObject.Properties['version']
    if ($null -eq $versionProp) {
        return "missing 'database.version' field"
    }
    $declaredVersion = $versionProp.Value
    if (-not ($declaredVersion -is [int] -or $declaredVersion -is [long])) {
        return "'database.version' is not an integer"
    }

    if ($File.BaseName -notmatch '^\d+$') {
        return "filename is not a version number"
    }
    $expectedVersion = [int]$File.BaseName
    if ([int]$declaredVersion -ne $expectedVersion) {
        return "declared version ($declaredVersion) does not match filename ($expectedVersion)"
    }

    return $null
}

$corrupt = [System.Collections.Generic.List[object]]::new()
foreach ($file in $jsonFiles) {
    $problem = Get-SchemaProblem -File $file
    if ($problem) {
        $corrupt.Add([PSCustomObject]@{
            File    = $file
            Problem = $problem
        })
    }
}

# --- Report --------------------------------------------------------------

if ($corrupt.Count -eq 0) {
    Write-Host "PASS: all $($jsonFiles.Count) schema files are valid." -ForegroundColor Green
    exit 0
}

Write-Host ""
Write-Host "Found $($corrupt.Count) corrupt schema file(s):" -ForegroundColor Yellow
foreach ($c in $corrupt) {
    Write-Host "  $($c.File.Name): $($c.Problem)" -ForegroundColor Yellow
}
Write-Host ""

if ($Mode -eq 'Report') {
    Write-Host "Report mode: no action taken." -ForegroundColor Cyan
    exit 2
}

# --- Act -----------------------------------------------------------------

$script:backupDir = $null
function Ensure-BackupDir {
    if (-not $script:backupDir) {
        $script:backupDir = Join-Path $schemaDir ("backup-" + (Get-Date -Format 'yyyyMMdd-HHmmss'))
        New-Item -ItemType Directory -Path $script:backupDir | Out-Null
        Write-Host "Backup folder: $($script:backupDir)" -ForegroundColor Cyan
    }
}

$fixed = 0
$skipped = 0

foreach ($c in $corrupt) {
    $action = if ($Backup) { 'Backup' } else { 'Delete' }

    if ($Mode -eq 'Interactive') {
        # Default is Skip. Only D or B take an action.
        Write-Host ""
        Write-Host "  $($c.File.Name): $($c.Problem)"
        $response = Read-Host "    [D]elete / [B]ackup / [S]kip (default S)"
        switch -Regex ([string]$response) {
            '^\s*[Bb]' { $action = 'Backup' }
            '^\s*[Dd]' { $action = 'Delete' }
            default    { $action = 'Skip'   }
        }
    }

    switch ($action) {
        'Delete' {
            Remove-Item -LiteralPath $c.File.FullName -Force
            Write-Host "  DELETED: $($c.File.Name)" -ForegroundColor Green
            $fixed += 1
        }
        'Backup' {
            Ensure-BackupDir
            Move-Item -LiteralPath $c.File.FullName -Destination $script:backupDir
            Write-Host "  BACKED UP: $($c.File.Name)" -ForegroundColor Green
            $fixed += 1
        }
        'Skip' {
            Write-Host "  SKIPPED: $($c.File.Name)" -ForegroundColor DarkGray
            $skipped += 1
        }
    }
}

# --- Summary -------------------------------------------------------------

Write-Host ""
Write-Host "Fixed: $fixed, skipped: $skipped" -ForegroundColor Cyan

if ($fixed -eq 0) {
    exit 2
}

Write-Host ""
Write-Host "IMPORTANT: Room will not regenerate deleted schema files" -ForegroundColor Yellow
Write-Host "unless KSP runs. You must clean before the next build:" -ForegroundColor Yellow
Write-Host ""
Write-Host "    .\gradlew clean" -ForegroundColor White
Write-Host ""

if ($ThenCheck) {
    Write-Host "Running gradlew clean check..." -ForegroundColor Cyan
    $gradlew = Join-Path $repoRoot 'gradlew.bat'
    Push-Location $repoRoot
    try {
        & $gradlew clean check
        exit $LASTEXITCODE
    } finally {
        Pop-Location
    }
}

exit 1
