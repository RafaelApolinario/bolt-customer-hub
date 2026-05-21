$ErrorActionPreference = "Stop"

$env:MAVEN_USER_HOME = Join-Path (Get-Location) ".m2"

Write-Host "Running validation..."
.\mvnw.cmd clean test
Write-Host "Validation completed successfully."
