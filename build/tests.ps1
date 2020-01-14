###############################################################################
# (C) Copyright IBM Corp. 2020
# 
# SPDX-License-Identifier: Apache-2.0
###############################################################################

Write-Host 'Performing Tests'
& mvn -B test -DskipTests=false -f .\fhir-server-test\pom.xml -DskipWebSocketTest=true --log-file out.log --no-transfer-progress

If ( (Get-Content out.log -Tail 10 | Select-String -pattern "BUILD SUCCESS").Length > 0) {
    Write-Host 'Performing Tests - Success'
    exit 0
} else { 
    Write-Host 'Performing Tests - Fail'
    cat out.log
    exit -1
}

# End of Script