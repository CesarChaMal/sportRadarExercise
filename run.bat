@echo off
setlocal enabledelayedexpansion

echo SportRadar Exercise Runner
echo ==============================

echo Java version:
java -version
if errorlevel 1 (
    echo Java not found! Please install Java 22
    pause
    exit /b 1
)

echo Building project...
call mvnw.cmd clean compile

echo Running tests...
call mvnw.cmd test

echo Build and tests completed successfully!
echo.
echo Starting Spring Boot Application...
echo Application available at: http://localhost:8080
echo Health check: http://localhost:8080/actuator/health
echo H2 Console: http://localhost:8080/h2-console
echo Match Summary: http://localhost:8080/api/matches/summary
echo.
echo ==============================

echo Starting Spring Boot application...
start /B cmd /c "mvnw.cmd spring-boot:run > spring-boot.log 2>&1"

set /a attempts=0
set /a max_attempts=30

:wait_loop
set /a attempts+=1
echo Attempt !attempts!/!max_attempts! - waiting for server...
timeout /t 2 /nobreak >nul
curl -s http://localhost:8080/actuator/health >nul 2>&1
if !errorlevel! equ 0 (
    echo Server is ready!
    goto run_tests
)
if !attempts! geq !max_attempts! (
    echo Server failed to start
    goto end
)
goto wait_loop

:run_tests
echo Testing API endpoints...
echo Creating match...
curl -s -X POST http://localhost:8080/api/matches -H "Content-Type: application/json" -d "{\"homeTeamName\":\"Team A\",\"awayTeamName\":\"Team B\",\"matchType\":\"FOOTBALL\"}"
echo.

echo Getting summary...
curl -s http://localhost:8080/api/matches/summary
echo.

echo Updating score...
curl -s -X PUT http://localhost:8080/api/matches/1/score -H "Content-Type: application/json" -d "{\"homeScore\":2,\"awayScore\":1}"
echo.

echo Testing undo...
curl -s -X POST http://localhost:8080/api/matches/undo
echo.

echo API tests completed!

:end
echo Server continues running. Press Ctrl+C to stop.
echo Server logs: type spring-boot.log
pause