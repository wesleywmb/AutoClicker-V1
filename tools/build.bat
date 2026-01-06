@echo off
setlocal EnableExtensions

title AutoClicker-V1 - Build

set "ROOT=%~dp0.."
set "SRC=%ROOT%\src\main\java"
set "TARGET=%ROOT%\target"
set "BUILD=%ROOT%\build"
set "LIB=%ROOT%\lib"
set "JNATIVEHOOK_JAR=%LIB%\jnativehook-2.2.2.jar"
set "JNATIVEHOOK_URL=https://repo1.maven.org/maven2/com/github/kwhat/jnativehook/2.2.2/jnativehook-2.2.2.jar"
set "OUT_JAR=%BUILD%\autoclicker-v1.jar"

echo ========================================
echo   AutoClicker-V1 - Build
echo ========================================
echo.

if not exist "%BUILD%" mkdir "%BUILD%"
if not exist "%LIB%" mkdir "%LIB%"
if not exist "%TARGET%\classes" mkdir "%TARGET%\classes"

if exist "%JNATIVEHOOK_JAR%" goto HAVE_DEP

echo [1/3] Baixando JNativeHook 2.2.2...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%JNATIVEHOOK_URL%' -OutFile '%JNATIVEHOOK_JAR%'"
if errorlevel 1 goto DL_FAIL

goto HAVE_DEP

:DL_FAIL
echo.
echo [ERRO] Falha ao baixar JNativeHook.
echo.
pause
exit /b 1

:HAVE_DEP
echo [1/3] Dependencias OK.

echo [2/3] Compilando...
pushd "%SRC%" >nul
javac -encoding UTF-8 -d "%TARGET%\classes" -cp "%JNATIVEHOOK_JAR%" com\autoclicker\config\*.java com\autoclicker\engine\*.java com\autoclicker\listener\*.java com\autoclicker\ui\*.java
if errorlevel 1 goto COMPILE_FAIL
popd >nul

goto MAKE_JAR

:COMPILE_FAIL
popd >nul
echo.
echo [ERRO] Falha na compilacao.
echo.
pause
exit /b 1

:MAKE_JAR
echo [3/3] Gerando JAR...
if exist "%OUT_JAR%" del /Q "%OUT_JAR%" >nul 2>&1
jar cfe "%OUT_JAR%" com.autoclicker.ui.AutoClickerUI -C "%TARGET%\classes" .
if errorlevel 1 goto JAR_FAIL

echo.
echo OK! JAR criado:
echo   %OUT_JAR%
echo.
exit /b 0

:JAR_FAIL
echo.
echo [ERRO] Falha ao criar o JAR.
echo.
pause
exit /b 1

