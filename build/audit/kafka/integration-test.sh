#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

run_tests(){
echo "Run Tests"
mvn -B test -DskipTests=false -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress

docker-compose exec kafka-1 cat << EOF > /etc/kafka/client-ssl.properties
bootstrap.servers=kafka-1:19092,kafka-2:29092
security.protocol=SSL
ssl.protocol=TLSv1.2
ssl.keystore.filename=kafka.broker1.keystore.jks
ssl.key.credentials=broker1_sslkey_creds
ssl.keystore.location=/etc/kafka/secrets/kafka.broker1.keystore.jks
ssl.truststore.location=/etc/kafka/secrets/kafka.broker1.truststore.jks
ssl.client.auth=requested
ssl.keystore.credentials=broker1_keystore_creds
ssl.keystore.password=change-password
security.inter.broker.protocol=SSL
ssl.key.password=change-password
ssl.truststore.password=change-password
ssl.truststore.filename=kafka.broker1.truststore.jks
ssl.truststore.credentials=broker1_truststore_creds
ssl.endpoint.identification.algorithm=
EOF

timeout 120 docker-compose exec kafka-1 bash /bin/kafka-console-consumer --bootstrap-server=kafka-1:19092,kafka-2:29092 --topic FHIR_AUDIT --max-messages 25 --property print.timestamp=true --consumer.config /etc/kafka/client-ssl.properties > workarea/fhir_audit-messages.log

if [ "$(wc  -l workarea/fhir_audit-messages.log)" != "25" ]
then 
    echo "Not FHIR_AUDIT = 25"
    cat workarea/fhir_audit-messages.log
    exit 25
fi
}

###############################################################################
# Store the current directory
pushd $(pwd) > /dev/null

# Change to the AUDIT/bin directory
cd "${WORKSPACE}"

run_tests "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################