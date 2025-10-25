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

# Function to test if server is ready
wait_for_server() {
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
            echo "✅ Server is ready!"
            return 0
        fi
        echo "Attempt $attempt/$max_attempts - waiting for server..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo "❌ Server failed to start within 60 seconds"
    return 1
}

# Start Spring Boot in background
echo "🚀 Starting Spring Boot application..."
./mvnw spring-boot:run > spring-boot.log 2>&1 &
SERVER_PID=$!

# Wait for server to be ready
if wait_for_server; then
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
else
    echo "❌ API tests skipped - server not ready"
fi

echo "🔄 Server continues running. Press Ctrl+C to stop."
echo "Server PID: $SERVER_PID"
echo "Logs: tail -f spring-boot.log"

# Trap to cleanup on exit
trap "kill $SERVER_PID 2>/dev/null; rm -f spring-boot.log" EXIT

wait $SERVER_PID