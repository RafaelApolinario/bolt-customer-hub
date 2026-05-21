#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [ -n "${SERVER_PORT:-}" ]; then
  bash "$SCRIPT_DIR/mvn-local.sh" spring-boot:run -Dspring-boot.run.arguments=--server.port="$SERVER_PORT"
else
  bash "$SCRIPT_DIR/mvn-local.sh" spring-boot:run
fi
