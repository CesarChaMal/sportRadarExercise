@echo off
REM SportRadar Exercise Runner Script for Windows
REM Sets Java 22 using SDKMAN and runs the project

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
pause