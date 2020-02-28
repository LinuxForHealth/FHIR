###############################################################################
# (C) Copyright IBM Corp. 2020
# 
# SPDX-License-Identifier: Apache-2.0
###############################################################################
$ErrorActionPreference = 'Stop'
Write-Host 'Performing Tests'
& mvn -B test -DskipTests=false -f .\fhir-server-test\pom.xml -DskipWebSocketTest=true --no-transfer-progress && exit 0

# End of Script
