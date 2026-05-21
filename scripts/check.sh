#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Running validation..."
bash "$SCRIPT_DIR/mvn-local.sh" clean test

echo "Validation completed successfully."
