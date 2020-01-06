@echo off
@REM ##############################################################################
@REM (C) Copyright IBM Corp. 2016, 2018
@REM 
@REM SPDX-License-Identifier: Apache-2.0
@REM ##############################################################################

echo "Performing integration test post-processing..."
if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 2
fi

export SIT=${WORKSPACE}/SIT
if [ ! -d ${SIT} ]; then
    echo "ERROR: ${SIT} not found!"
    exit 2
fi

@REM Stop the fhir server.
echo "Stopping the fhir server..."
${SIT}/wlp/bin/server stop fhir-server


@REM Gather up all the log files and test results
set it_results=${SIT}/integration-test-results
rm -fr ${it_results} 2>/dev/null
mkdir -p ${it_results}/server-logs
mkdir -p ${it_results}/fhir-server-test

echo "Gathering post-test server logs..."
cp -pr ${SIT}/wlp/usr/servers/fhir-server/logs ${it_results}/server-logs

echo "Gathering integration test output"
cp -pr ${WORKSPACE}/fhir-server-test/target/surefire-reports/* ${it_results}/fhir-server-test

echo "Integration test post-processing completed!"

exit 0

@REM End of Script