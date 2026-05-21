$ErrorActionPreference = "Stop"

Write-Host "Running validation..."
& "$PSScriptRoot\mvn-local.ps1" clean test
Write-Host "Validation completed successfully."
