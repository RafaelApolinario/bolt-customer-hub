#!/bin/bash
set -e

echo "Running validation..."

./mvnw clean test

echo "Validation completed successfully."
