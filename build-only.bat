@echo off
REM SportRadar Exercise Build Script for Windows
REM Sets Java 22 using SDKMAN and builds the project

echo 🔨 SportRadar Exercise Build Script
echo ===================================

REM Initialize SDKMAN for Windows
call %USERPROFILE%\.sdkman\bin\sdkman-init.bat

REM Use Java 22
echo ☕ Setting Java 22...
sdk use java 22-zulu

REM Build project
echo 🔨 Building project...
mvnw.cmd clean compile package -DskipTests

echo ✅ Build completed successfully!
echo 📦 JAR file created: target/sportRadarExercise-0.4.0.jar
pause