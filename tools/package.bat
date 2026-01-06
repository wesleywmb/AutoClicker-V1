@echo off
setlocal

title AutoClicker-V1 - Gerar EXE
color 0A

set "ROOT=%~dp0.."
set "CS=%~dp0AutoClickerLauncher.cs"
set "OUT=%ROOT%\AutoClicker-V1.exe"

echo ========================================
echo   AutoClicker-V1 - Gerador de EXE
echo ========================================
echo.

if not exist "%CS%" (
  echo [ERRO] Arquivo nao encontrado: %CS%
  pause
  exit /b 1
)

echo Tentando compilar com csc (Visual C# Compiler)...

REM Caminho comum do csc no Windows (pode variar)
set "CSC=%WINDIR%\Microsoft.NET\Framework\v4.0.30319\csc.exe"
if exist "%CSC%" goto HAVE_CSC

set "CSC=%WINDIR%\Microsoft.NET\Framework64\v4.0.30319\csc.exe"
if exist "%CSC%" goto HAVE_CSC

echo.
echo [ERRO] csc.exe nao encontrado.
echo Instale o .NET Framework (normalmente ja existe no Windows) ou use o IntelliJ com Maven para gerar um jar.
echo.
pause
exit /b 1

:HAVE_CSC
echo Usando: %CSC%

"%CSC%" /nologo /target:exe /out:"%OUT%" "%CS%"
if errorlevel 1 (
  echo.
  echo [ERRO] Falha ao compilar o launcher.
  pause
  exit /b 1
)

echo.
echo OK! Gerado: %OUT%
echo.
echo Dica: Use shortcut.bat para criar o pacote completo com atalho na Area de Trabalho.
echo.
pause
endlocal

