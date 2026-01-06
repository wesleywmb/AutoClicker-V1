@echo off
setlocal

title AutoClicker-V1 - Criar Atalho

echo ========================================
echo  Criar Atalho na Area de Trabalho
echo ========================================
echo.

set "ROOT=%~dp0.."
set "START_BAT=%ROOT%\START.bat"
set "DESKTOP=%USERPROFILE%\Desktop"
set "SHORTCUT=%DESKTOP%\AutoClicker-V1.lnk"

if not exist "%START_BAT%" (
    echo [ERRO] START.bat nao encontrado!
    pause
    exit /b 1
)

echo Criando atalho...

powershell -NoProfile -ExecutionPolicy Bypass -Command "$ws = New-Object -ComObject WScript.Shell; $s = $ws.CreateShortcut('%SHORTCUT%'); $s.TargetPath = '%START_BAT%'; $s.WorkingDirectory = '%ROOT%'; $s.Description = 'AutoClicker-V1 - Hotkey: F6'; $s.Save()"

if errorlevel 1 (
    echo.
    echo [ERRO] Falha ao criar atalho.
    pause
    exit /b 1
)

echo.
echo ✓ Atalho criado com sucesso!
echo   Local: %DESKTOP%\AutoClicker-V1.lnk
echo.
echo Agora voce pode clicar duas vezes no atalho para executar.
echo.
pause
endlocal

