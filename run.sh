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
echo "📊 H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb, User: sa, Password: [empty])"
echo "📋 Match Summary: http://localhost:8080/api/matches/summary"
echo ""
echo "Press Ctrl+C to stop the application"
echo "=============================="

# Run Spring Boot application in background
./mvnw spring-boot:run &
SERVER_PID=$!

echo "⏳ Waiting for server to start..."
sleep 10

echo "🧪 Testing API endpoints..."
echo "📝 Creating match..."
curl -s -X POST http://localhost:8080/api/matches -H "Content-Type: application/json" -d '{"homeTeamName":"Team A","awayTeamName":"Team B","matchType":"FOOTBALL"}'
echo ""

echo "📊 Getting summary..."
curl -s http://localhost:8080/api/matches/summary
echo ""

echo "⚽ Updating score..."
curl -s -X PUT http://localhost:8080/api/matches/1/score -H "Content-Type: application/json" -d '{"homeScore":2,"awayScore":1}'
echo ""

echo "↩️ Testing undo..."
curl -s -X POST http://localhost:8080/api/matches/undo
echo ""

echo "✅ API tests completed!"
echo "🔄 Server continues running. Press Ctrl+C to stop."
wait $SERVER_PID