#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
MAVEN_VERSION="3.9.15"
MAVEN_DIR_NAME="apache-maven-$MAVEN_VERSION"
MAVEN_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/$MAVEN_DIR_NAME-bin.zip"
MAVEN_BASE="$REPO_ROOT/.m2/tools"
MAVEN_HOME_LOCAL="$MAVEN_BASE/$MAVEN_DIR_NAME"
DOWNLOAD_DIR="$REPO_ROOT/.m2/tmp"
ARCHIVE_PATH="$DOWNLOAD_DIR/$MAVEN_DIR_NAME-bin.zip"

export MAVEN_USER_HOME="${MAVEN_USER_HOME:-"$REPO_ROOT/.m2"}"
export TMPDIR="${TMPDIR:-"$DOWNLOAD_DIR"}"

mkdir -p "$MAVEN_BASE" "$DOWNLOAD_DIR" "$MAVEN_USER_HOME"

if [ ! -x "$MAVEN_HOME_LOCAL/bin/mvn" ]; then
  echo "Preparing local Maven $MAVEN_VERSION..."

  if [ ! -f "$ARCHIVE_PATH" ]; then
    if command -v curl >/dev/null 2>&1; then
      curl -fL "$MAVEN_URL" -o "$ARCHIVE_PATH"
    elif command -v wget >/dev/null 2>&1; then
      wget "$MAVEN_URL" -O "$ARCHIVE_PATH"
    else
      echo "curl or wget is required to download Maven." >&2
      exit 1
    fi
  fi

  rm -rf "$MAVEN_HOME_LOCAL"

  if command -v unzip >/dev/null 2>&1; then
    unzip -q "$ARCHIVE_PATH" -d "$MAVEN_BASE"
  elif command -v jar >/dev/null 2>&1; then
    (cd "$MAVEN_BASE" && jar xf "$ARCHIVE_PATH")
  else
    echo "unzip or jar is required to extract Maven." >&2
    exit 1
  fi
fi

"$MAVEN_HOME_LOCAL/bin/mvn" "$@"
