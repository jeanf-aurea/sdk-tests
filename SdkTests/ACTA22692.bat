@IF NOT DEFINED DEBUG_SCRIPT @ECHO OFF
SETLOCAL

CALL "%~dp0env.bat"

IF DEFINED SDK GOTO SdkIsSet

PUSHD "%~dp0..\ss.act910x\ss"
SET BUILD=%CD%
POPD

SET SDK=%BUILD%\build\debug\interceptor\actional-sdk.jar

:SdkIsSet
IF NOT DEFINED ORBS_ROOT SET ORBS_ROOT=f:\actional\ORBS_ROOT

"%ORBS_ROOT%\java\Windows\%JAVA_ARCH%\jdk1.7.0_51\bin\java.exe" -classpath "%SDK%;%~dp0bin" -Dcom.actional.lg.interceptor.debug=true bugs.ACTA22692