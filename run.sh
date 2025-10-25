#!/bin/bash

# SportRadar Exercise Runner Script
# Sets Java 22 using SDKMAN and runs the project

set -e

echo "🚀 SportRadar Exercise Runner"
echo "=============================="

# Source SDKMAN
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install and use Java 22 if not available
if ! sdk list java | grep -q "22.*zulu.*installed"; then
    echo "📦 Installing Java 22 (Zulu)..."
    sdk install java 22-zulu
fi

echo "☕ Setting Java 22..."
sdk use java 22-zulu

# Verify Java version
echo "✅ Java version:"
java -version

# Clean and build project
echo "🔨 Building project..."
./mvnw clean compile

# Run tests
echo "🧪 Running tests..."
./mvnw test

echo "✅ Build and tests completed successfully!"