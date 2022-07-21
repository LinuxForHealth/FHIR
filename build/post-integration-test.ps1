###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

Write-Host 'Performing integration test post-processing...'

# The default path to the workspace
# we have to account for the build directory
$DIR_WORKSPACE= Split-Path $PSScriptRoot -Parent

# Remove the entire SIT file tree if it exists
[string]$SIT=$DIR_WORKSPACE + '\SIT'
[string]$SIT = Resolve-Path $SIT

# Stop the fhir server.
Write-Host 'Stopping the fhir-server'
$PROC=$SIT + '\wlp\bin\server'
& $PROC stop fhir-server
Write-Host 'The fhir-server is stopped'

Write-Host 'Clean up existing integration test results'
$it_results='SIT/integration-test-results'
If (Test-Path -Path $it_results) {
  Remove-Item -path $it_results -Recurse -Force
}

Write-Host 'Create destinations for the files/folders'
$DST_SL='server-logs'
$DST_ST='fhir-server-test'
New-Item -Path $it_results -Name $DST_SL -ItemType 'directory' -Force
New-Item -Path $it_results -Name $DST_ST -ItemType 'directory' -Force

Write-Host 'Gathering post-test server logs'
$SRC_ST='SIT/wlp/usr/servers/defaultServer/logs'
$DST_SL_F=$it_results + '/' + $DST_SL
Copy-Item -Path $SRC_ST -Destination $DST_SL_F -Recurse -Force

Write-Host 'Gathering integration test output'
$SRC_ST='./fhir-server-test/target/surefire-reports'
$DST_ST_F=$it_results + '/' + $DST_ST
Copy-Item -Path $SRC_ST -Destination $DST_ST_F -Recurse -Force

Copy-Item -Path stdout.txt -Destination $DST_ST_F -Force
Copy-Item -Path stderr.txt -Destination $DST_ST_F -Force

exit 0

# End of Script
