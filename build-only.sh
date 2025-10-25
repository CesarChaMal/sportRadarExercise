#!/bin/bash

# SportRadar Exercise Build Script
# Sets Java 22 using SDKMAN and builds the project

set -e

echo "🔨 SportRadar Exercise Build Script"
echo "==================================="

# Source SDKMAN
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Use Java 22
echo "☕ Setting Java 22..."
sdk use java 22-zulu

# Build project
echo "🔨 Building project..."
./mvnw clean compile package -DskipTests

echo "✅ Build completed successfully!"
echo "📦 JAR file created: target/sportRadarExercise-0.4.0.jar"