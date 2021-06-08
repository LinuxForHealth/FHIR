#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

# downloads and extracts kafka to a directory
download(){
    if [ ! -f ${WORKSPACE}/build/reindex/kafka/workarea/kafka.tgz ]
    then 
        curl -L -o ${WORKSPACE}/build/reindex/kafka/workarea/kafka.tgz http://www-us.apache.org/dist/kafka/2.7.0/kafka_2.13-2.7.0.tgz
        cd ${WORKSPACE}/build/reindex/kafka/workarea/
        tar xvf kafka.tgz
        cd -
    fi 
}

# run_tests - executes the standard integration tests, and then checks that we have output
run_tests(){

mvn -B -nsu -ntp test -DskipTests=false -f fhir-server-test -DskipWebSocketTest=true

# The following test should always Run
echo "TEST_CONFIGURATION: check that there is output and the configuration works"
docker-compose -f build/reindex/kafka/docker-compose.yml exec kafka-1 bash /bin/kafka-console-consumer --timeout-ms 60000 --bootstrap-server=kafka-1:19092,kafka-2:29092 \
    --topic FHIR_reindex --max-messages 25 --property print.timestamp=true --offset earliest \
    --consumer.config /etc/kafka/secrets/client-ssl.properties \
    --partition 1 > ${WORKSPACE}/build/reindex/kafka/workarea/output/fhir_reindex-messages.log

# When in doubt check the file /var/lib/kafka/data/FHIR_reindex-0/00000000000000000000.log
if [ "$(cat ${WORKSPACE}/build/reindex/kafka/workarea/output/fhir_reindex-messages.log | grep -c 'CreateTime:')" != "25" ]
then 
    echo "Not FHIR_reindex = 25"
    echo "Exported reindex Messages"
    docker-compose -f build/reindex/kafka/docker-compose.yml exec -T kafka-1 wc -l /var/lib/kafka/data/FHIR_reindex-0/00000000000000000000.log
    cat ${WORKSPACE}/build/reindex/kafka/workarea/output/fhir_reindex-messages.log
    exit 25
else 
    echo "Passed 'TEST_CONFIGURATION'!"
fi
}

###############################################################################
# Store the current directory
pushd $(pwd) > /dev/null

# Change to the reindex/bin directory
cd "${WORKSPACE}"

run_tests

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################