@echo off
setlocal EnableExtensions

title AutoClicker-V1

set "ROOT=%~dp0.."
set "APP_JAR=%ROOT%\app\autoclicker-v1.jar"
set "LIB=%ROOT%\lib"
set "JNATIVEHOOK_JAR=%LIB%\jnativehook-2.2.2.jar"
set "JNATIVEHOOK_URL=https://repo1.maven.org/maven2/com/github/kwhat/jnativehook/2.2.2/jnativehook-2.2.2.jar"

echo ========================================
echo   AutoClicker-V1
echo ========================================
echo.

REM Portable mode (app/autoclicker-v1.jar existe)
if exist "%APP_JAR%" goto PORTABLE

REM Dev mode (compila src/main/java)
set "SRC=%ROOT%\src\main\java"
set "TARGET=%ROOT%\target\classes"

if not exist "%LIB%" mkdir "%LIB%"
if not exist "%TARGET%" mkdir "%TARGET%"

if exist "%JNATIVEHOOK_JAR%" goto HAVE_JAR

echo [1/3] Baixando JNativeHook...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%JNATIVEHOOK_URL%' -OutFile '%JNATIVEHOOK_JAR%'"
if errorlevel 1 goto DL_FAIL

goto HAVE_JAR

:DL_FAIL
echo.
echo [ERRO] Falha ao baixar JNativeHook.
echo.
pause
exit /b 1

:HAVE_JAR
echo [1/3] Dependencias OK.

echo [2/3] Compilando...
pushd "%SRC%" >nul
javac -encoding UTF-8 -d "%TARGET%" -cp "%JNATIVEHOOK_JAR%" com\autoclicker\config\*.java com\autoclicker\engine\*.java com\autoclicker\listener\*.java com\autoclicker\ui\*.java
if errorlevel 1 goto COMPILE_FAIL
popd >nul

echo [3/3] Executando...
echo Hotkey: F6 (funciona minimizado)
echo.
java -cp "%TARGET%;%JNATIVEHOOK_JAR%" com.autoclicker.ui.AutoClickerUI

goto END

:COMPILE_FAIL
popd >nul
echo.
echo [ERRO] Falha na compilacao.
echo.
pause
exit /b 1

:PORTABLE
if not exist "%LIB%" mkdir "%LIB%"

if exist "%JNATIVEHOOK_JAR%" goto RUN_PORTABLE

echo Baixando JNativeHook...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%JNATIVEHOOK_URL%' -OutFile '%JNATIVEHOOK_JAR%'"
if errorlevel 1 (
  echo.
  echo [ERRO] Falha ao baixar JNativeHook.
  echo.
  pause
  exit /b 1
)

:RUN_PORTABLE
echo Iniciando AutoClicker-V1...
echo Hotkey: F6 (funciona minimizado)
echo.
java -cp "%APP_JAR%;%JNATIVEHOOK_JAR%" com.autoclicker.ui.AutoClickerUI

:END
endlocal

