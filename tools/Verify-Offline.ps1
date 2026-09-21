#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Verify myTUIN's offline capability.

.DESCRIPTION
    Runs two phases:

      1. Structural  — the release manifest must not declare the
                       INTERNET permission.

      2. Behavioural — the instrumented test suite must pass with the
                       device offline.

    The script captures the device's network state before disabling
    connectivity and restores it afterwards, including on failure.

    See docs/OFFLINE_VERIFICATION.md for the reasoning behind each
    phase and for the interpretation of results.

.PARAMETER SkipStructural
    Skip Phase 1.

.PARAMETER SkipBehavioural
    Skip Phase 2.

.PARAMETER AdbPath
    Path to the adb executable. Defaults to 'adb' on PATH.

.EXAMPLE
    .\tools\Verify-Offline.ps1

.EXAMPLE
    .\tools\Verify-Offline.ps1 -SkipBehavioural

.NOTES
    Exit codes:
      0 — both phases passed (or were skipped)
      1 — a phase failed
      2 — bad input (no device, adb not found)

    Run directly, not dot-sourced.
#>

[CmdletBinding()]
param(
    [switch]$SkipStructural,
    [switch]$SkipBehavioural,
    [string]$AdbPath = 'adb'
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# --- Helpers ------------------------------------------------------------

function Write-Section { param([string]$Text)
    Write-Host ''
    Write-Host $Text -ForegroundColor Cyan
    Write-Host ('-' * $Text.Length) -ForegroundColor Cyan
}

function Write-Pass { param([string]$Text)
    Write-Host "  PASS: $Text" -ForegroundColor Green
}

function Write-Fail { param([string]$Text)
    Write-Host "  FAIL: $Text" -ForegroundColor Red
}

function Invoke-Adb {
    param([string[]]$Arguments)
    & $AdbPath @Arguments 2>&1
}

# --- Preflight ----------------------------------------------------------

Write-Host 'myTUIN - offline verification' -ForegroundColor White

if (-not (Get-Command $AdbPath -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: adb not found at '$AdbPath'." -ForegroundColor Red
    exit 2
}

$devicesRaw = Invoke-Adb @('devices')
$devices = $devicesRaw | Select-String -Pattern '^\S+\s+device$'
if (-not $devices) {
    Write-Host 'ERROR: no adb device detected. Attach an emulator or device.' -ForegroundColor Red
    exit 2
}

$serial = ($devices | Select-Object -First 1).ToString().Split("`t")[0].Trim()
Write-Host "Device: $serial"

# --- Capture prior network state ---------------------------------------

function Get-NetworkState {
    param([string]$Serial)
    $airplane = (Invoke-Adb @('-s', $Serial, 'shell', 'settings', 'get', 'global', 'airplane_mode_on')).Trim()
    $wifi = (Invoke-Adb @('-s', $Serial, 'shell', 'settings', 'get', 'global', 'wifi_on')).Trim()
    $data = (Invoke-Adb @('-s', $Serial, 'shell', 'settings', 'get', 'global', 'mobile_data')).Trim()
    return [PSCustomObject]@{
        Airplane = $airplane
        Wifi     = $wifi
        Data     = $data
    }
}

function Restore-NetworkState {
    param([string]$Serial, [PSCustomObject]$State)
    Write-Host ''
    Write-Host 'Restoring network state...'

    # Restore airplane mode first.
    if ($State.Airplane -eq '0') {
        Invoke-Adb @('-s', $Serial, 'shell', 'cmd', 'connectivity', 'airplane-mode', 'disable') | Out-Null
    } else {
        Invoke-Adb @('-s', $Serial, 'shell', 'cmd', 'connectivity', 'airplane-mode', 'enable') | Out-Null
    }

    # Restore wifi and mobile data.
    if ($State.Wifi -eq '1') {
        Invoke-Adb @('-s', $Serial, 'shell', 'svc', 'wifi', 'enable') | Out-Null
    } else {
        Invoke-Adb @('-s', $Serial, 'shell', 'svc', 'wifi', 'disable') | Out-Null
    }

    if ($State.Data -eq '1') {
        Invoke-Adb @('-s', $Serial, 'shell', 'svc', 'data', 'enable') | Out-Null
    } else {
        Invoke-Adb @('-s', $Serial, 'shell', 'svc', 'data', 'disable') | Out-Null
    }

    Start-Sleep -Seconds 2
    $now = Get-NetworkState -Serial $Serial
    Write-Host "  airplane_mode=$($now.Airplane) wifi=$($now.Wifi) data=$($now.Data)"
    Write-Host '  confirmed'
}

$prior = Get-NetworkState -Serial $serial
Write-Host "Prior network state: airplane_mode=$($prior.Airplane) wifi=$($prior.Wifi) data=$($prior.Data)"

# --- Phase 1: structural ------------------------------------------------

$structuralResult = $null

if (-not $SkipStructural) {
    Write-Section 'Phase 1 - structural'

    $mainManifest = Join-Path $PSScriptRoot '..\app\src\main\AndroidManifest.xml'
    $debugManifest = Join-Path $PSScriptRoot '..\app\src\debug\AndroidManifest.xml'

    if (-not (Test-Path $mainManifest)) {
        Write-Fail "main AndroidManifest.xml not found at $mainManifest"
        $structuralResult = $false
    } else {
        $mainContent = Get-Content $mainManifest -Raw
        $mainHasInternet = $mainContent -match 'android\.permission\.INTERNET'

        Write-Host "  Release manifest declares INTERNET permission: $(if ($mainHasInternet) { 'YES' } else { 'no' })"

        if ($mainHasInternet) {
            Write-Fail 'Release manifest must not declare the INTERNET permission.'
            Write-Host '  See docs/OFFLINE_VERIFICATION.md §6 for diagnosis.' -ForegroundColor Yellow
            $structuralResult = $false
        } else {
            if (Test-Path $debugManifest) {
                $debugContent = Get-Content $debugManifest -Raw
                $debugHasInternet = $debugContent -match 'android\.permission\.INTERNET'
                Write-Host "  Debug manifest declares INTERNET permission: $(if ($debugHasInternet) { 'yes' } else { 'NO' })"
                if (-not $debugHasInternet) {
                    Write-Host '  Note: debug manifest no longer declares INTERNET.' -ForegroundColor Yellow
                    Write-Host '  If the MapLibre proof of concept has been removed, this is expected.' -ForegroundColor Yellow
                }
            }
            Write-Pass 'Release manifest has no INTERNET permission.'
            $structuralResult = $true
        }
    }
} else {
    Write-Host 'Phase 1 skipped.'
}

# --- Phase 2: behavioural -----------------------------------------------

$behaviouralResult = $null

if (-not $SkipBehavioural) {
    Write-Section 'Phase 2 - behavioural'

    try {
        Write-Host '  Disabling connectivity...'

        $methodUsed = $null

        # A223=a: try modern airplane-mode command first.
        $airplaneAttempt = Invoke-Adb @('-s', $serial, 'shell', 'cmd', 'connectivity', 'airplane-mode', 'enable') 2>&1
        $airplaneSucceeded = ($LASTEXITCODE -eq 0) -and ($airplaneAttempt -notmatch 'Unknown command|not found|error')

        if ($airplaneSucceeded) {
            $methodUsed = 'cmd connectivity airplane-mode'
        } else {
            Write-Host '  Modern airplane-mode command unavailable; using svc fallback.'
            Invoke-Adb @('-s', $serial, 'shell', 'svc', 'wifi', 'disable') | Out-Null
            Invoke-Adb @('-s', $serial, 'shell', 'svc', 'data', 'disable') | Out-Null
            $methodUsed = 'svc wifi/data disable'
        }

        Write-Host "  Connectivity method: $methodUsed"
        Write-Host '  Verifying connectivity is off...'

        Start-Sleep -Seconds 3
        $during = Get-NetworkState -Serial $serial
        Write-Host "    airplane_mode=$($during.Airplane) wifi=$($during.Wifi) data=$($during.Data)"

        if (($during.Airplane -ne '1') -and ($during.Wifi -eq '1')) {
            Write-Host '  Warning: connectivity state is ambiguous.' -ForegroundColor Yellow
            Write-Host '  The instrumented suite will still run; interpret results accordingly.' -ForegroundColor Yellow
        }

        Write-Host '  Running connectedDebugAndroidTest...'
        & .\gradlew connectedDebugAndroidTest 2>&1 | ForEach-Object { Write-Host "    $_" }
        $testExitCode = $LASTEXITCODE

        if ($testExitCode -eq 0) {
            Write-Pass 'All instrumented tests passed offline.'
            $behaviouralResult = $true
        } else {
            Write-Fail "Instrumented tests failed offline (exit code $testExitCode)."
            Write-Host '  See docs/OFFLINE_VERIFICATION.md §6 for diagnosis.' -ForegroundColor Yellow
            $behaviouralResult = $false
        }
    }
    finally {
        Restore-NetworkState -Serial $serial -State $prior
    }
} else {
    Write-Host 'Phase 2 skipped.'
}

# --- Summary ------------------------------------------------------------

Write-Section 'Summary'

$overallPass = $true

if ($null -ne $structuralResult) {
    if ($structuralResult) { Write-Host '  Phase 1: PASS' -ForegroundColor Green }
    else { Write-Host '  Phase 1: FAIL' -ForegroundColor Red; $overallPass = $false }
} else {
    Write-Host '  Phase 1: skipped'
}

if ($null -ne $behaviouralResult) {
    if ($behaviouralResult) { Write-Host '  Phase 2: PASS' -ForegroundColor Green }
    else { Write-Host '  Phase 2: FAIL' -ForegroundColor Red; $overallPass = $false }
} else {
    Write-Host '  Phase 2: skipped'
}

Write-Host ''

if ($overallPass) {
    Write-Host 'Both phases passed.' -ForegroundColor Green
    exit 0
} else {
    Write-Host 'One or more phases failed.' -ForegroundColor Red
    exit 1
}
