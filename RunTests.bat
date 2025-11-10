@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"

:: ---- Config: jar paths ----
set JUNIT_CONSOLE=lib\junit-platform-console-standalone-1.9.3.jar
set JUNIT_API=lib\junit-jupiter-api-5.9.3.jar
set APIGUARDIAN=lib\apiguardian-api-1.1.2.jar
set H2=lib\h2-2.2.224.jar

:: ---- Check required jars ----
for %%F in ("%JUNIT_CONSOLE%" "%JUNIT_API%" "%APIGUARDIAN%" "%H2%") do (
  if not exist "%%~F" (
    echo [ERROR] Missing file: %%~F
    echo Make sure all jars exist under .\lib\
    pause
    exit /b 1
  )
)

:: ---- Prepare bin directory ----
if not exist "bin" mkdir bin

:: ---- Build source list (DB + TrustedReviewerList + tests) ----
if exist sources.lst del /q sources.lst
for /R "src\databasePart1" %%f in (*.java) do echo %%f>> sources.lst
if exist "src\application\TrustedReviewerList.java" echo src\application\TrustedReviewerList.java>> sources.lst
for /R "src\tests" %%f in (*.java) do echo %%f>> sources.lst

:: ---- Compile tests (no JavaFX needed) ----
set CP=.;%JUNIT_API%;%APIGUARDIAN%;src
echo Compiling with classpath: %CP%
javac -cp "%CP%" -d bin @sources.lst
if errorlevel 1 (
  echo.
  echo [ERROR] Compilation failed. See messages above.
  pause
  exit /b 1
)

:: ---- Run tests with JUnit Console and H2 driver ----
echo.
echo Running tests...
java -jar "%JUNIT_CONSOLE%" -cp "bin;%H2%" --scan-class-path

echo.
pause
endlocal
