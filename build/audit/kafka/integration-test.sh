#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

# run_tests - executes the standard integration tests, and then checks that we have output
run_tests(){

mvn -B -nsu -ntp test -DskipTests=false -f fhir-server-test -DskipWebSocketTest=true

# The following test should always Run
echo "TEST_CONFIGURATION: check that there is output and the configuration works"
docker-compose -f build/audit/kafka/docker-compose.yml exec kafka-1 bash /bin/kafka-console-consumer --timeout-ms 60000 --bootstrap-server=kafka-1:19092,kafka-2:29092 \
    --topic FHIR_AUDIT --max-messages 25 --property print.timestamp=true --offset earliest \
    --consumer.config /etc/kafka/secrets/client-ssl.properties \
    --partition 1 > ${WORKSPACE}/build/audit/kafka/workarea/fhir_audit-messages.log || true

# When in doubt check the file /var/lib/kafka/data/FHIR_AUDIT-0/00000000000000000000.log
if [ "$(cat ${WORKSPACE}/build/audit/kafka/workarea/fhir_audit-messages.log | grep -c 'CreateTime:')" != "25" ]
then 
    echo "Not FHIR_AUDIT = 25"
    cat ${WORKSPACE}/build/audit/kafka/workarea/fhir_audit-messages.log
    exit 25
else 
    echo "Passed 'TEST_CONFIGURATION'!"
fi
}

###############################################################################
# Store the current directory
pushd $(pwd) > /dev/null

# Change to the AUDIT/bin directory
cd "${WORKSPACE}"

run_tests

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################