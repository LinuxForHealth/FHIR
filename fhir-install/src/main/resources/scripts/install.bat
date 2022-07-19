@echo off
@REM ----------------------------------------------------------------------------
@REM (C) Copyright IBM Corp. 2017, 2022
@REM
@REM SPDX-License-Identifier: Apache-2.0
@REM ----------------------------------------------------------------------------

SETLOCAL ENABLEDELAYEDEXPANSION

set LIBERTY_VERSION=22.0.0.7

echo Executing %0 to deploy the fhir-server web application...

@REM Make sure that JAVA_HOME is set
if "-%JAVA_HOME%-"=="--" (
    echo "Error: JAVA_HOME not set; make sure JAVA_HOME points to a Java 11 JVM (or above) and then re-try."
    set rc=1
    goto :exit
) else (
    set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
    if not exist !JAVA_CMD! (
        echo Error: Incorrect JAVA_HOME value: %JAVA_HOME%
    set rc=1
    goto :exit
    )
)

@REM echo JAVA_HOME: %JAVA_HOME%
@REM echo JAVA_CMD: %JAVA_CMD%
@REM set rc=1
@REM goto :exit

@REM Determine the location of this script.
set BASEDIR=%~dp0

@REM Remove any trailing \ from BASEDIR
if %BASEDIR:~-1%==\ set BASEDIR=%BASEDIR:~0,-1%

@REM Default liberty install location
cd %BASEDIR%\..
set UNZIP_LOC=%CD%
set WLP_INSTALL_DIR=%UNZIP_LOC%\liberty-runtime

@REM Allow user to override default install location
if not "-%1-"=="--" set WLP_INSTALL_DIR=%1

@REM Add a trailing \ to WLP_INSTALL_DIR if needed
if not "%WLP_INSTALL_DIR:~-1%"=="\" set WLP_INSTALL_DIR=%WLP_INSTALL_DIR%\

echo Deploying fhir-server in location: %WLP_INSTALL_DIR%

@REM If the liberty install directory doesnt exist, then create it.
if not exist %WLP_INSTALL_DIR% (
    echo The Liberty installation directory does not exist; attempting to create it...
    mkdir %WLP_INSTALL_DIR%
    if errorlevel 1 (
        set rc=%ERRORLEVEL%
        echo Error creating installation directory: %rc%
        goto :exit
    )
)

@REM Unzip liberty runtime zip
echo Extracting the Liberty runtime...
call :UnZip  %BASEDIR%\openliberty-runtime-%LIBERTY_VERSION%.zip\  %WLP_INSTALL_DIR%
if %rc% neq 0 (
    echo Error extracting liberty runtime: %rc%
    goto :exit
)

@REM Save the liberty home directory.
set WLP_ROOT=%WLP_INSTALL_DIR%wlp

@REM Create our server
echo Creating the Liberty defaultServer...
%COMSPEC% /c %WLP_ROOT%\bin\server.bat create defaultServer
if errorlevel 1 (
    set rc=%ERRORLEVEL%
    echo Error creating server definition: %rc%
    goto :exit
)

@REM Copy our server assets
echo Deploying fhir-server assets to the server runtime environment.
xcopy /S /Y /Q %BASEDIR%\artifacts\* %WLP_ROOT%\usr\
if errorlevel 1 (
    set rc=%ERRORLEVEL%
    echo Error deploying fhir-server assets to server runtime environment: %rc%
    goto :exit
)


echo The FHIR Server has been successfully deployed to the
echo Liberty runtime located at: %WLP_ROOT%
echo The following manual steps must be completed before the server can be started:
echo 1. The fhir-server application requires Java 11.
echo    If you do not have one, a copy of the Java 11 SDK can be obtained at https://adoptium.net.
echo    Set the JAVA_HOME environment variable to your Java installation
echo    before starting the server.
echo 2. Make sure that your selected database (e.g. PostgreSQL) is active and
echo    ready to accept requests.
echo 3. Deploy the schema via the fhir-persistence-schema cli jar under %BASEDIR%\tools
echo    and grant necessary permissions.
echo 4. Modify the Liberty server config (server.xml) by adding/removing/modifying the xml snippets under
echo    %WLP_ROOT%/usr/servers/defaultServer/configDropins to configure datasource definitions, 
echo    TLS configuration (keystores), authentication, and more.
echo 5. Modify the FHIR server config (fhir-server-config.json) under
echo    %WLP_ROOT%/usr/servers/defaultServer/config to configure the persistence, resource endpoints,
echo    and related FHIR server features.
echo You can start and stop the server with these commands:
echo    %WLP_ROOT%\bin\server start
echo    %WLP_ROOT%\bin\server stop
set rc=0
goto :exit


@REM This function will unzip %1 into the directory %2
@REM by creating a VB script and executing it.
:UnZip
set vbs="%temp%\_.vbs"
if exist %vbs% del /f /q %vbs%
>%vbs% echo Set fso = CreateObject("Scripting.FileSystemObject")
>>%vbs% echo strDest = "%2"
>>%vbs% echo strZipFileName = "%1"
>>%vbs% echo If NOT fso.FolderExists(strDest) Then
>>%vbs% echo     fso.CreateFolder(strDest)
>>%vbs% echo End If
>>%vbs% echo set objShell = CreateObject("Shell.Application")
>>%vbs% echo set FilesInZip=objShell.NameSpace(strZipFileName).items
>>%vbs% echo objShell.NameSpace(strDest).CopyHere(FilesInZip)
>>%vbs% echo Set fso = Nothing
>>%vbs% echo Set objShell = Nothing
cscript //nologo %vbs%
set rc=%ERRORLEVEL%
if exist %vbs% del /f /q %vbs%
goto :eof

:exit
%COMSPEC% /c exit /b %rc%
