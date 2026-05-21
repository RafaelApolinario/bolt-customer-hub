#!/bin/bash
set -e

export MAVEN_USER_HOME="${MAVEN_USER_HOME:-"$PWD/.m2"}"
export TMPDIR="${TMPDIR:-"$MAVEN_USER_HOME/tmp"}"
mkdir -p "$MAVEN_USER_HOME" "$TMPDIR"

if [ -n "${SERVER_PORT:-}" ]; then
  ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port="$SERVER_PORT"
else
  ./mvnw spring-boot:run
fi
