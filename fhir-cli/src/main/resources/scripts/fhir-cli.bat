@echo off
@REM ----------------------------------------------------------------------------
@REM (C) Copyright IBM Corp. 2016,2017,2019
@REM
@REM SPDX-License-Identifier: Apache-2.0
@REM ----------------------------------------------------------------------------

if "%OS%" == "Windows_NT" setlocal

setlocal enabledelayedexpansion


@REM Determine the location of this script.
set BASEDIR=%~dp0

@REM Remove any trailing \ from BASEDIR
if %BASEDIR:~-1%==\ set BASEDIR=%BASEDIR:~0,-1%

@REM build classpath from all jars in lib
set CP=.
for /R %BASEDIR%\lib %%f in (*.jar) do set CP=!CP!;%%f

set CMD_LINE_ARGS=%1
if ""%1""=="""" goto done
shift
:setup
if ""%1""=="""" goto done
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setup
:done

IF NOT DEFINED JAVA_OPTS set JAVA_OPTS=
@REM echo classpath: %CP%
@REM echo arguments: %CMD_LINE_ARGS%

java -cp "%CP%" %JAVA_OPTS% com.ibm.watson.health.fhir.cli.FHIRCLI %CMD_LINE_ARGS%
