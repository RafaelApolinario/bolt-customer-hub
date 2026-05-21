$ErrorActionPreference = "Stop"

$env:MAVEN_USER_HOME = Join-Path (Get-Location) ".m2"
$localTemp = Join-Path $env:MAVEN_USER_HOME "tmp"
New-Item -ItemType Directory -Force -Path $env:MAVEN_USER_HOME, $localTemp | Out-Null
$env:TEMP = $localTemp
$env:TMP = $localTemp

.\mvnw.cmd spring-boot:run
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
