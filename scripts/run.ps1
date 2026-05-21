param(
    [string]$ServerPort = ""
)

$ErrorActionPreference = "Stop"

$env:MAVEN_USER_HOME = Join-Path (Get-Location) ".m2"
$localTemp = Join-Path $env:MAVEN_USER_HOME "tmp"
New-Item -ItemType Directory -Force -Path $env:MAVEN_USER_HOME, $localTemp | Out-Null
$env:TEMP = $localTemp
$env:TMP = $localTemp

if ([string]::IsNullOrWhiteSpace($ServerPort)) {
    .\mvnw.cmd spring-boot:run
} else {
    .\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=$ServerPort"
}
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
