@echo off
REM SportRadar Exercise Runner Script for Windows
REM Sets Java 22 using SDKMAN and runs the Spring Boot project

echo 🚀 SportRadar Exercise Runner
echo ==============================

REM Initialize SDKMAN for Windows
call %USERPROFILE%\.sdkman\bin\sdkman-init.bat

REM Install and use Java 22 if not available
sdk list java | findstr "22.*zulu.*installed" >nul
if errorlevel 1 (
    echo 📦 Installing Java 22 (Zulu)...
    sdk install java 22-zulu
)

echo ☕ Setting Java 22...
sdk use java 22-zulu

REM Verify Java version
echo ✅ Java version:
java -version

REM Clean and build project
echo 🔨 Building project...
mvnw.cmd clean compile

REM Run tests
echo 🧪 Running tests...
mvnw.cmd test

echo ✅ Build and tests completed successfully!
echo.
echo 🌟 Starting Spring Boot Application...
echo 📍 Application will be available at: http://localhost:8080
echo 🏥 Health check: http://localhost:8080/actuator/health
echo 📊 H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb, User: sa, Password: [empty])
echo 📋 Match Summary: http://localhost:8080/api/matches/summary
echo.
echo Press Ctrl+C to stop the application
echo ==============================

REM Run Spring Boot application in background
start /B mvnw.cmd spring-boot:run

echo ⏳ Waiting for server to start...
timeout /t 10 /nobreak >nul

echo 🧪 Testing API endpoints...
echo 📝 Creating match...
curl -s -X POST http://localhost:8080/api/matches -H "Content-Type: application/json" -d "{\"homeTeamName\":\"Team A\",\"awayTeamName\":\"Team B\",\"matchType\":\"FOOTBALL\"}"
echo.

echo 📊 Getting summary...
curl -s http://localhost:8080/api/matches/summary
echo.

echo ⚽ Updating score...
curl -s -X PUT http://localhost:8080/api/matches/1/score -H "Content-Type: application/json" -d "{\"homeScore\":2,\"awayScore\":1}"
echo.

echo ↩️ Testing undo...
curl -s -X POST http://localhost:8080/api/matches/undo
echo.

echo ✅ API tests completed!
echo 🔄 Server continues running. Press Ctrl+C to stop.
pause