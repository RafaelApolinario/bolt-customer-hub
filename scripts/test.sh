#!/bin/bash
set -e

export MAVEN_USER_HOME="${MAVEN_USER_HOME:-"$PWD/.m2"}"
./mvnw test
