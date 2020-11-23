###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
$ErrorActionPreference = 'Stop'
Write-Host 'Performing Tests'
$PROC = Start-Process -FilePath "mvn.cmd" -ArgumentList "-B test -DskipTests=false -f .\fhir-server-test\pom.xml --no-transfer-progress" -RedirectStandardOutput stdout.txt -RedirectStandardError stderr.txt -PassThru -Wait

Write-Host '[Tests] --> Exit Code is' $PROC.ExitCode
if($PROC.ExitCode -ne 0){
  Write-Host "Exiting with Error Condition"
  [Environment]::Exit(1)
}
# End of Script
