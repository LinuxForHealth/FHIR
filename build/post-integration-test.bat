@echo off

@REM ##############################################################################
@REM (C) Copyright IBM Corp. 2016, 2020
@REM 
@REM SPDX-License-Identifier: Apache-2.0
@REM ##############################################################################

echo "Performing integration test post-processing..."
if "%WORKSPACE%" == "" (
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 2
)

set SIT=%WORKSPACE%/SIT
if NOT EXIST %SIT% (
    echo "ERROR: ${SIT} not found!"
    exit 2
)

@REM Stop the fhir server.
echo "Stopping the fhir server..."
${SIT}/wlp/bin/server stop fhir-server


@REM Gather up all the log files and test results
set it_results=${SIT}/integration-test-results

IF EXISTS ${it_results}/server-logs (
    del ${it_results}
)

IF NOT EXISTS ${it_results}/server-logs (
    mkdir ${it_results}/server-logs
)

IF NOT EXISTS ${it_results}/fhir-server-test (
    mkdir -p ${it_results}/fhir-server-test
)

echo "Gathering post-test server logs..."
cp -pr ${SIT}/wlp/usr/servers/fhir-server/logs ${it_results}/server-logs

echo "Gathering integration test output"
cp -pr ${WORKSPACE}/fhir-server-test/target/surefire-reports/* ${it_results}/fhir-server-test

echo "Integration test post-processing completed!"

exit 0

@REM End of Script