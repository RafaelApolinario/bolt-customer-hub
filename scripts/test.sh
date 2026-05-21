#!/bin/bash
set -e

export MAVEN_USER_HOME="${MAVEN_USER_HOME:-"$PWD/.m2"}"
export TMPDIR="${TMPDIR:-"$MAVEN_USER_HOME/tmp"}"
mkdir -p "$MAVEN_USER_HOME" "$TMPDIR"

./mvnw test
