@echo off
echo ========================================
echo   AutoClicker-V1 - Executar
echo ========================================
echo.

cd /d "%~dp0.."

if not exist "build\autoclicker-v1.jar" (
    echo Compilando...
    call tools\build.bat
    if errorlevel 1 (
        echo.
        echo [ERRO] Build falhou.
        pause
        exit /b 1
    )
)

if not exist "lib\jnativehook-2.2.2.jar" (
    echo Baixando dependencia...
    if not exist "lib" mkdir "lib"
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/github/kwhat/jnativehook/2.2.2/jnativehook-2.2.2.jar' -OutFile 'lib\jnativehook-2.2.2.jar'"
)

echo Iniciando AutoClicker-V1...
echo Hotkey: F6 (funciona minimizado)
echo.

java -cp "build\autoclicker-v1.jar;lib\jnativehook-2.2.2.jar" com.autoclicker.ui.AutoClickerUI

