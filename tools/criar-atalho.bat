@echo off
setlocal

title AutoClicker-Pro - Criar Atalho

echo ========================================
echo  Criar Atalho na Area de Trabalho
echo ========================================
echo.

set "ROOT=%~dp0.."
set "INICIAR_BAT=%~dp0iniciar.bat"
set "DESKTOP=%USERPROFILE%\Desktop"
set "SHORTCUT_BAT=%DESKTOP%\AutoClicker-Pro.bat"

if not exist "%INICIAR_BAT%" (
    echo [ERRO] iniciar.bat nao encontrado!
    pause
    exit /b 1
)

echo Criando atalho...

echo @echo off > "%SHORTCUT_BAT%"
echo cd /d "%ROOT%" >> "%SHORTCUT_BAT%"
echo call tools\iniciar.bat >> "%SHORTCUT_BAT%"

if not exist "%SHORTCUT_BAT%" (
    echo.
    echo [ERRO] Falha ao criar atalho.
    pause
    exit /b 1
)

echo.
echo ✓ Atalho criado com sucesso!
echo   Local: %DESKTOP%\AutoClicker-Pro.bat
echo.
echo Agora voce pode clicar duas vezes no arquivo para executar.
echo.
pause
endlocal

