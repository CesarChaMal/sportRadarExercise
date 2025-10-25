#!/bin/bash

# SportRadar Exercise Runner Script
# Sets Java 22 using SDKMAN and runs the Spring Boot project

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
echo ""
echo "🌟 Starting Spring Boot Application..."
echo "📍 Application will be available at: http://localhost:8080"
echo "🏥 Health check: http://localhost:8080/actuator/health"
echo "📊 H2 Console: http://localhost:8080/h2-console"
echo "🔗 API Base: http://localhost:8080/api/matches"
echo ""
echo "Press Ctrl+C to stop the application"
echo "=============================="

# Run Spring Boot application
./mvnw spring-boot:run