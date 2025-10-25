@echo off
REM SportRadar Exercise Test Runner Script for Windows
REM Sets Java 22 using SDKMAN and runs tests only

echo 🧪 SportRadar Exercise Test Runner
echo ==================================

REM Initialize SDKMAN for Windows
call %USERPROFILE%\.sdkman\bin\sdkman-init.bat

REM Use Java 22
echo ☕ Setting Java 22...
sdk use java 22-zulu

REM Run tests only
echo 🧪 Running tests...
mvnw.cmd clean test

echo ✅ Tests completed successfully!
pause