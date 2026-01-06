@echo off
setlocal EnableExtensions

title AutoClicker-V1

set "ROOT=%~dp0"
set "APP_JAR=%ROOT%app\autoclicker-v1.jar"
set "LIB=%ROOT%lib"
set "JNATIVEHOOK_JAR=%LIB%\jnativehook-2.2.2.jar"
set "JNATIVEHOOK_URL=https://repo1.maven.org/maven2/com/github/kwhat/jnativehook/2.2.2/jnativehook-2.2.2.jar"

echo ========================================
echo   AutoClicker-V1
echo ========================================
echo.

if not exist "%APP_JAR%" (
  echo [ERRO] JAR nao encontrado: %APP_JAR%
  echo.
  pause
  exit /b 1
)

if not exist "%LIB%" mkdir "%LIB%"

if exist "%JNATIVEHOOK_JAR%" goto RUN

echo Baixando JNativeHook...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%JNATIVEHOOK_URL%' -OutFile '%JNATIVEHOOK_JAR%'"
if errorlevel 1 (
  echo.
  echo [ERRO] Falha ao baixar JNativeHook.
  echo.
  pause
  exit /b 1
)

:RUN
echo Iniciando AutoClicker-V1...
echo Hotkey: F6 (funciona minimizado)
echo.
java -cp "%APP_JAR%;%JNATIVEHOOK_JAR%" com.autoclicker.ui.AutoClickerUI

endlocal

