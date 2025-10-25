#!/bin/bash

# SportRadar Exercise Test Runner Script
# Sets Java 22 using SDKMAN and runs tests only

set -e

echo "🧪 SportRadar Exercise Test Runner"
echo "=================================="

# Source SDKMAN
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Use Java 22
echo "☕ Setting Java 22..."
sdk use java 22-zulu

# Run tests only
echo "🧪 Running tests..."
./mvnw clean test

echo "✅ Tests completed successfully!"