#!/bin/bash
set -e

echo "Running validation..."

export MAVEN_USER_HOME="${MAVEN_USER_HOME:-"$PWD/.m2"}"
./mvnw clean test

echo "Validation completed successfully."
