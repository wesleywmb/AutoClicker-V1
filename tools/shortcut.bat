@echo off
setlocal EnableExtensions

title AutoClicker-V1 - Desktop Shortcut

set "ROOT=%~dp0.."
set "APPDIR=%ROOT%\AutoClicker-V1"
set "EXE=%APPDIR%\AutoClicker-V1.exe"
set "PS1=%~dp0create_shortcut.ps1"
set "BUILD=%~dp0build.bat"
set "OUTJAR=%ROOT%\build\autoclicker-v1.jar"

echo ========================================
echo  AutoClicker-V1 - Desktop Shortcut
echo ========================================
echo.

echo [1/3] Building JAR...
if not exist "%BUILD%" (
  echo [ERRO] Script nao encontrado: %BUILD%
  pause
  exit /b 1
)
call "%BUILD%"
if errorlevel 1 (
  echo.
  echo [ERRO] Build falhou.
  pause
  exit /b 1
)
if not exist "%OUTJAR%" (
  echo.
  echo [ERRO] JAR nao encontrado: %OUTJAR%
  pause
  exit /b 1
)

echo.
echo [2/3] Preparando pacote portatil...
if not exist "%APPDIR%" mkdir "%APPDIR%"
if not exist "%APPDIR%\lib" mkdir "%APPDIR%\lib"
if not exist "%APPDIR%\app" mkdir "%APPDIR%\app"

copy /Y "%ROOT%\AutoClicker-V1.exe" "%EXE%" >nul
copy /Y "%~dp0run-portable.bat" "%APPDIR%\run.bat" >nul
copy /Y "%OUTJAR%" "%APPDIR%\app\autoclicker-v1.jar" >nul

if exist "%ROOT%lib\jnativehook-2.2.2.jar" copy /Y "%ROOT%lib\jnativehook-2.2.2.jar" "%APPDIR%\lib\jnativehook-2.2.2.jar" >nul

echo Pacote criado:
echo   %APPDIR%
echo.

echo [3/3] Criando atalho na Area de Trabalho...
if not exist "%PS1%" (
  echo [ERRO] Script nao encontrado: %PS1%
  pause
  exit /b 1
)

powershell -NoProfile -ExecutionPolicy Bypass -File "%PS1%" -TargetPath "%EXE%" -WorkingDirectory "%APPDIR%" -ShortcutName "AutoClicker-V1.lnk"
if errorlevel 1 (
  echo.
  echo [ERRO] Falha ao criar atalho.
  echo.
  pause
  exit /b 1
)

echo.
echo Concluido! Atalho "AutoClicker-V1" criado na Area de Trabalho.
echo.
pause
endlocal

