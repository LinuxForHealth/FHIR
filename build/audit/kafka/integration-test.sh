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
CONTAINER_ID=$(docker ps | grep kafka_kafka-1_1 |  awk '{print $1}')
chmod +x ${WORKSPACE}/build/audit/kafka/resources/get_results.sh
docker-compose -f build/audit/kafka/docker-compose.yml exec -T kafka-1 /etc/kafka/secrets/get_results.sh

# When in doubt check the file /var/lib/kafka/data/FHIR_AUDIT-0/00000000000000000000.log
if [ "$(cat ${WORKSPACE}/build/audit/kafka/workarea/output/fhir_audit-messages.log | grep -c 'CreateTime:')" != "25" ]
then 
    echo "Not FHIR_AUDIT = 25"
    echo "Exported Audit Messages"
    docker-compose -f build/audit/kafka/docker-compose.yml exec -T kafka-1 wc -l /var/lib/kafka/data/FHIR_AUDIT-0/00000000000000000000.log
    docker-compose -f build/audit/kafka/docker-compose.yml exec -T kafka-2 wc -l /var/lib/kafka/data/FHIR_AUDIT-0/00000000000000000000.log
    cat ${WORKSPACE}/build/audit/kafka/workarea/output/fhir_audit-messages.log
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