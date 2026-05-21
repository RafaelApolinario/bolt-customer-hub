param(
    [string]$ServerPort = ""
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($ServerPort)) {
    & "$PSScriptRoot\mvn-local.ps1" spring-boot:run
} else {
    & "$PSScriptRoot\mvn-local.ps1" spring-boot:run "-Dspring-boot.run.arguments=--server.port=$ServerPort"
}
