$ErrorActionPreference = "Stop"

$env:MAVEN_USER_HOME = Join-Path (Get-Location) ".m2"
$localTemp = Join-Path $env:MAVEN_USER_HOME "tmp"
New-Item -ItemType Directory -Force -Path $env:MAVEN_USER_HOME, $localTemp | Out-Null
$env:TEMP = $localTemp
$env:TMP = $localTemp

.\mvnw.cmd test
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
