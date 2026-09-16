#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Search source files for code symbols, ignoring comments.

.DESCRIPTION
    Verifies that one or more regex patterns are either present or absent
    in the code content of source files. Block comments, line comments
    and trailing comments are skipped so that prose in a comment cannot
    produce a false match. String literals are preserved so that patterns
    such as "NORTHERN" can be checked.

    Intended for architectural-boundary checks in the myTUIN project:

      - Domain layer must not import Android, Room or Hilt.
      - Snake_case schema identifiers must not leak into the domain.
      - Uppercase enum constant names must not appear as string literals.

.PARAMETER Path
    One or more file or directory paths. Directories are searched
    recursively using -Include.

.PARAMETER Pattern
    One or more .NET regex patterns. A line matches if any pattern
    matches the code content of that line.

.PARAMETER Mode
    'Absent'  — fail if any pattern matches (default).
    'Present' — fail if no pattern matches anything.

.PARAMETER Include
    File pattern for directory searches. Default: *.kt.

.PARAMETER IgnoreCase
    Perform case-insensitive matching. Default: case-sensitive.

.EXAMPLE
    # Domain layer must not import Android, Compose, Room or Hilt.
    .\tools\Verify-CodeSymbols.ps1 `
      -Path app/src/main/java/com/mytuin/gardenplanner/domain `
      -Pattern '^\s*import\s+(android\.|androidx\.|dagger\.|hilt)'

.EXAMPLE
    # Snake_case schema identifiers must not leak into the domain.
    .\tools\Verify-CodeSymbols.ps1 `
      -Path app/src/main/java/com/mytuin/gardenplanner/domain `
      -Pattern '\b(canonical_name|scientific_name|created_at|updated_at|garden_id|space_type)\b'

.EXAMPLE
    # Uppercase enum constant names must not appear as string literals.
    .\tools\Verify-CodeSymbols.ps1 `
      -Path app/src/main `
      -Pattern '"(ANNUAL|PERENNIAL|NORTHERN|SOUTHERN|DRAFT|ACTIVE|RAISED_BED)"'

.EXAMPLE
    # Hilt annotations must be present somewhere.
    .\tools\Verify-CodeSymbols.ps1 `
      -Path app/src/main `
      -Pattern '@HiltAndroidApp|@AndroidEntryPoint' `
      -Mode Present

.NOTES
    Exit codes:
      0 — check passed
      1 — check failed
      2 — bad input (path not found, empty pattern)

    Run directly, not dot-sourced:
      .\tools\Verify-CodeSymbols.ps1 ...
    Dot-sourcing would let exit terminate your shell.

    Known limitations:
      - Multi-line Kotlin raw strings (""" ... """) are not tracked
        across lines. A // or /* inside a raw string spanning lines
        would be misread.
      - Kotlin char literals ('x') are not tracked. A char literal
        containing '/' is very unlikely in practice.
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string[]]$Path,

    [Parameter(Mandatory = $true)]
    [string[]]$Pattern,

    [ValidateSet('Present', 'Absent')]
    [string]$Mode = 'Absent',

    [string]$Include = '*.kt',

    [switch]$IgnoreCase
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# --- Validate input ------------------------------------------------------

foreach ($p in $Pattern) {
    if ([string]::IsNullOrWhiteSpace($p)) {
        Write-Host "ERROR: empty pattern supplied." -ForegroundColor Red
        exit 2
    }
}

# --- Expand paths to files ----------------------------------------------

$files = New-Object System.Collections.Generic.List[System.IO.FileInfo]
foreach ($p in $Path) {
    if (Test-Path -LiteralPath $p -PathType Container) {
        Get-ChildItem -LiteralPath $p -Recurse -File -Filter $Include |
            ForEach-Object { [void]$files.Add($_) }
    }
    elseif (Test-Path -LiteralPath $p -PathType Leaf) {
        [void]$files.Add((Get-Item -LiteralPath $p))
    }
    else {
        Write-Host "ERROR: path not found: $p" -ForegroundColor Red
        exit 2
    }
}

if ($files.Count -eq 0) {
    Write-Host "ERROR: no files matched under the supplied paths." -ForegroundColor Red
    exit 2
}

# --- Compile regexes -----------------------------------------------------

$options = [System.Text.RegularExpressions.RegexOptions]::None
if ($IgnoreCase) {
    $options = [System.Text.RegularExpressions.RegexOptions]::IgnoreCase
}
$regexes = @()
foreach ($p in $Pattern) {
    $regexes += [System.Text.RegularExpressions.Regex]::new($p, $options)
}

# --- Comment / string stripping -----------------------------------------

function ConvertTo-CodeOnlyLines {
    param(
        [Parameter(Mandatory = $true)]
        [AllowEmptyCollection()]
        [AllowEmptyString()]
        [string[]]$Lines
    )

    $result = New-Object System.Collections.Generic.List[string]
    $inBlock = $false

    foreach ($line in $Lines) {
        $sb = [System.Text.StringBuilder]::new()
        $j = 0
        while ($j -lt $line.Length) {

            if ($inBlock) {
                if ($j + 1 -lt $line.Length -and
                    $line[$j] -eq '*' -and $line[$j + 1] -eq '/') {
                    $inBlock = $false
                    $j += 2
                } else {
                    $j += 1
                }
                continue
            }

            # Line comment start outside a string.
            if ($j + 1 -lt $line.Length -and
                $line[$j] -eq '/' -and $line[$j + 1] -eq '/') {
                break
            }

            # Block comment start.
            if ($j + 1 -lt $line.Length -and
                $line[$j] -eq '/' -and $line[$j + 1] -eq '*') {
                $inBlock = $true
                $j += 2
                continue
            }

            # String literal — preserved verbatim.
            if ($line[$j] -eq '"') {
                [void]$sb.Append($line[$j])
                $j += 1
                while ($j -lt $line.Length) {
                    if ($line[$j] -eq '\' -and $j + 1 -lt $line.Length) {
                        [void]$sb.Append($line[$j])
                        [void]$sb.Append($line[$j + 1])
                        $j += 2
                        continue
                    }
                    if ($line[$j] -eq '"') {
                        [void]$sb.Append($line[$j])
                        $j += 1
                        break
                    }
                    [void]$sb.Append($line[$j])
                    $j += 1
                }
                continue
            }

            [void]$sb.Append($line[$j])
            $j += 1
        }
        [void]$result.Add($sb.ToString())
    }
    return $result
}

# --- Scan ----------------------------------------------------------------

$matches = New-Object System.Collections.Generic.List[object]

foreach ($file in $files) {
    $rawLines = @(Get-Content -LiteralPath $file.FullName)
    $codeLines = ConvertTo-CodeOnlyLines -Lines $rawLines

    for ($i = 0; $i -lt $codeLines.Count; $i++) {
        $code = $codeLines[$i]
        foreach ($rx in $regexes) {
            if ($rx.IsMatch($code)) {
                [void]$matches.Add([PSCustomObject]@{
                    File    = $file.FullName
                    Line    = $i + 1
                    Text    = $code.Trim()
                    Pattern = $rx.ToString()
                })
                break
            }
        }
    }
}

# --- Report --------------------------------------------------------------

if ($Mode -eq 'Absent') {
    if ($matches.Count -eq 0) {
        Write-Host ("PASS: no forbidden symbols found across {0} file(s)." `
            -f $files.Count) -ForegroundColor Green
        exit 0
    }

    Write-Host ("FAIL: forbidden symbols found ({0} match(es)):" `
        -f $matches.Count) -ForegroundColor Red
    foreach ($m in $matches) {
        Write-Host ("  {0}:{1}: {2}" -f $m.File, $m.Line, $m.Text)
        Write-Host ("      matched pattern: {0}" -f $m.Pattern) -ForegroundColor DarkGray
    }
    exit 1
}
else {
    if ($matches.Count -gt 0) {
        Write-Host ("PASS: required symbols present ({0} match(es) across {1} file(s))." `
            -f $matches.Count, $files.Count) -ForegroundColor Green
        exit 0
    }

    Write-Host ("FAIL: required symbols not found across {0} file(s)." `
        -f $files.Count) -ForegroundColor Red
    Write-Host ("  patterns searched: {0}" -f ($Pattern -join ', '))
    exit 1
}