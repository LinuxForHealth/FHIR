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
    if [ ! -f ${WORKSPACE}/build/notifications/kafka/workarea/kafka.tgz ]
    then 
        curl -L -o ${WORKSPACE}/build/notifications/kafka/workarea/kafka.tgz http://www-us.apache.org/dist/kafka/2.7.0/kafka_2.13-2.7.0.tgz
        cd ${WORKSPACE}/build/notifications/kafka/workarea/
        tar xvf kafka.tgz
        cd -
    fi 
}

# run_tests - executes the standard integration tests, and then checks that we have output
run_tests(){

mvn -B -nsu -ntp test -DskipTests=false -f fhir-server-test -DskipWebSocketTest=true

# The following test should always Run
echo "TEST_CONFIGURATION: check that there is output and the configuration works"
docker-compose -f build/notifications/kafka/docker-compose.yml exec kafka-1 bash /bin/kafka-console-consumer --timeout-ms 60000 --bootstrap-server=kafka-1:19092,kafka-2:29092 \
    --topic FHIR_NOTIFICATIONS --max-messages 10 --property print.timestamp=true --offset earliest \
    --consumer.config /etc/kafka/secrets/client-ssl.properties \
    --partition 1 > ${WORKSPACE}/build/notifications/kafka/workarea/output/fhir_notifications-messages.log

# When in doubt check the file /var/lib/kafka/data/FHIR_notifications-0/00000000000000000000.log
mkdir -p build/notifications/kafka/integration-test-results/
cp ${WORKSPACE}/build/notifications/kafka/workarea/output/fhir_notifications-messages.log build/notifications/kafka/integration-test-results

if [ "$(cat ${WORKSPACE}/build/notifications/kafka/workarea/output/fhir_notifications-messages.log | grep -c 'CreateTime:')" != "10" ]
then 
    echo "Not FHIR_NOTIFICATIONS = 10"
    echo "Exported notifications Messages"
    docker-compose -f build/notifications/kafka/docker-compose.yml exec -T kafka-1 wc -l /var/lib/kafka/data/FHIR_notifications-0/00000000000000000000.log
    cat ${WORKSPACE}/build/notifications/kafka/workarea/output/fhir_notifications-messages.log
    exit 10
else 
    echo "Passed 'TEST_CONFIGURATION'!"
fi

}

###############################################################################
# Store the current directory
pushd $(pwd) > /dev/null

# Change to the notifications/bin directory
cd "${WORKSPACE}"

run_tests

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################