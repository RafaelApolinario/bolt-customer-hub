$ErrorActionPreference = "Stop"

$env:MAVEN_USER_HOME = Join-Path (Get-Location) ".m2"

.\mvnw.cmd spring-boot:run
