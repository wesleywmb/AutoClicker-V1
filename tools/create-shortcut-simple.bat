@echo off
setlocal

title AutoClicker-V1 - Criar Atalho

echo ========================================
echo  Criar Atalho na Area de Trabalho
echo ========================================
echo.

set "ROOT=%~dp0.."
set "START_BAT=%~dp0START.bat"
set "DESKTOP=%USERPROFILE%\Desktop"
set "SHORTCUT_BAT=%DESKTOP%\AutoClicker-V1.bat"

if not exist "%START_BAT%" (
    echo [ERRO] START.bat nao encontrado!
    pause
    exit /b 1
)

echo Criando atalho...

echo @echo off > "%SHORTCUT_BAT%"
echo cd /d "%ROOT%" >> "%SHORTCUT_BAT%"
echo call tools\START.bat >> "%SHORTCUT_BAT%"

if not exist "%SHORTCUT_BAT%" (
    echo.
    echo [ERRO] Falha ao criar atalho.
    pause
    exit /b 1
)

echo.
echo ✓ Atalho criado com sucesso!
echo   Local: %DESKTOP%\AutoClicker-V1.bat
echo.
echo Agora voce pode clicar duas vezes no arquivo para executar.
echo.
pause
endlocal

