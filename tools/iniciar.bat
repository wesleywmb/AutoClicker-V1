@echo off
setlocal
title AutoClicker-Pro - Iniciar

echo ========================================
echo   AutoClicker-Pro - Iniciar
echo ========================================
echo.

cd /d "%~dp0.."

if not exist "build\autoclicker-pro.jar" (
    echo Compilando...
    call tools\compilar.bat
    if errorlevel 1 (
        echo.
        echo [ERRO] Build falhou.
        pause
        exit /b 1
    )
)

if not exist "lib\jnativehook-2.2.2.jar" (
    echo Baixando JNativeHook...
    if not exist "lib" mkdir "lib"
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/github/kwhat/jnativehook/2.2.2/jnativehook-2.2.2.jar' -OutFile 'lib\jnativehook-2.2.2.jar'"
)

if not exist "lib\flatlaf-3.2.5.jar" (
    echo Baixando FlatLaf...
    if not exist "lib" mkdir "lib"
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.5/flatlaf-3.2.5.jar' -OutFile 'lib\flatlaf-3.2.5.jar'"
)

echo Iniciando AutoClicker-Pro...
echo Hotkey: F6 (funciona minimizado)
echo.

java -cp "build\autoclicker-pro.jar;lib\jnativehook-2.2.2.jar;lib\flatlaf-3.2.5.jar" com.autoclicker.ui.AutoClickerUI

