@Write-Output off

###############################################################################
# (C) Copyright IBM Corp. 2019, 2020
# 
# SPDX-License-Identifier: Apache-2.0
###############################################################################

<# 
 This script will install the fhir server on the local machine and then
 start it up so that we can run server integration tests
 $WORKSPACE - top level directory of the Jenkins workspace
 $WORKSPACE/SIT - holds everything related to server integration tests
 $WORKSPACE/SIT/fhir-server-dist - installer contents (after unzipping)
 $WORKSPACE/SIT/wlp - fhir server installation 
#>

# Initial wait time after the "server start" command returns
$SERVER_WAITTIME="30"

# Sleep interval after each "metadata" invocation
$SLEEP_INTERVAL="10"

# Max number of "metadata" tries to detect server is running
$MAX_TRIES=10

Write-Output "Preparing environment for fhir-server integration tests..."
if ([string]::isNullOrWhitespace($Env:WORKSPACE)) {
    Write-Output "ERROR: WORKSPACE environment variable not set!"
    exit 2
}

# Collect the installers and config files in a common place (same as the docker process)
cd ${WORKSPACE}/fhir-install/docker
& copy-dependencies.ps1

# Remove the entire SIT file tree if it exists
[string]$SIT=$Env:WORKSPACE/SIT
if(![System.IO.File]::Exists($SIT)){
    Write-Output "Removing %SIT%"
    Remove-Item –path %SIT% –recurse -force
}

# Create the directory
new-item -Name $SIT -ItemType directory

# Install a fresh copy of the fhir server
Write-Output "Unzipping fhir-server installer..."
Expand-Archive -LiteralPath $Env:WORKSPACE/fhir-install/docker/volumes/dist/fhir-server-distribution.zip -DestinationPath $SIT

Write-Output "Installing fhir server in Integration Tests"
$SIT/fhir-server-dist/install.bat $SIT

Write-Output "Copying configuration to install location..."
rm -fr $SIT/wlp/usr/servers/fhir-server/config/*
Copy-Item $Env:WORKSPACE/fhir-install/docker/volumes/dist/config/* -Destination $Env:WORKSPACE/wlp/usr/servers/fhir-server/config/ -Recurse

Write-Output "Copying test artifacts to install location..."
Copy-Item $Env:WORKSPACE/fhir-operation/target/fhir-operation-*-tests.jar -Destination $Env:WORKSPACE/wlp/usr/servers/fhir-server/userlib/

# Start up the fhir server
$DATE_PS=[System.TimeZoneInfo]::ConvertTimeBySystemTimeZoneId((Get-Date), 'Greenwich Standard Time').ToString('t')
Write-Output "
>>> Current time: " $DATE_PS
Write-Output "Starting fhir server..."
$SIT/wlp/bin/server start fhir-server
Write-Output ">>> Current time: " DATE_PS

# Sleep for a bit to let the server startup
Write-Output "Sleeping $SERVER_WAITTIME to let the server start..."
Start-Sleep -s $SERVER_WAITTIME

# Next, we'll invoke the metadata API to detect when the
# server is ready to accept requests.
Write-Output "Waiting for fhir-server to complete initialization..."
$metadata_url="https://localhost:9443/fhir-server/api/v4/metadata"
$tries=0
$status=0
while [ $status -ne 200 -a $tries -lt ${MAX_TRIES} ]; do
    tries++
    cmd="curl -sS -k -o ${WORKSPACE}/metadata.json -I -w %{http_code} -u fhiruser:change-password $metadata_url "
    Write-Output "Executing[$tries]: $cmd"
    status=$($cmd)
    Write-Output "Status code: $status"
    if [ $status -ne 200 ]
    then
       Write-Output "Sleeping for ${SLEEP_INTERVAL} secs..."
       sleep ${SLEEP_INTERVAL}
    fi
done

# Gather server logs in case there was a problem starting up the server
Write-Output "Collecting pre-test server logs..."
set pre_it_logs=%SIT%/pre-it-logs
set zip_file=${WORKSPACE}/pre-it-logs.zip
rm -fr ${pre_it_logs} 2>/dev/null
mkdir -p ${pre_it_logs}
rm -f ${zip_file} 2>/dev/null
cp -pr %SIT%/wlp/usr/servers/fhir-server/logs ${pre_it_logs}
zip -r ${zip_file} ${pre_it_logs}

# If we weren't able to detect the fhir server ready within the allotted timeframe,
# then exit now...
if [ $status -ne 200 ]
then
    Write-Output "Could not establish a connection to the fhir-server within $tries REST API invocations!"
    exit 1
fi

Write-Output "The fhir-server appears to be running..."
exit 0

# End of Script 