param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$MavenArgs
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$mavenVersion = "3.9.15"
$mavenDirectoryName = "apache-maven-$mavenVersion"
$mavenUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$mavenVersion/$mavenDirectoryName-bin.zip"
$mavenBase = Join-Path $repoRoot ".m2\tools"
$mavenHome = Join-Path $mavenBase $mavenDirectoryName
$mavenCommand = Join-Path $mavenHome "bin\mvn.cmd"
$downloadDir = Join-Path $repoRoot ".m2\tmp"
$archivePath = Join-Path $downloadDir "$mavenDirectoryName-bin.zip"

$env:MAVEN_USER_HOME = Join-Path $repoRoot ".m2"
$env:TEMP = $downloadDir
$env:TMP = $downloadDir

New-Item -ItemType Directory -Force -Path $mavenBase, $downloadDir, $env:MAVEN_USER_HOME | Out-Null

if (-not (Test-Path -LiteralPath $mavenCommand)) {
    Write-Host "Preparing local Maven $mavenVersion..."

    if (-not (Test-Path -LiteralPath $archivePath)) {
        Invoke-WebRequest -Uri $mavenUrl -OutFile $archivePath -UseBasicParsing
    }

    if (Test-Path -LiteralPath $mavenHome) {
        Remove-Item -LiteralPath $mavenHome -Recurse -Force
    }

    tar.exe -xf $archivePath -C $mavenBase

    if (-not (Test-Path -LiteralPath $mavenCommand)) {
        throw "Local Maven was not prepared correctly at $mavenCommand"
    }
}

& $mavenCommand @MavenArgs
exit $LASTEXITCODE
