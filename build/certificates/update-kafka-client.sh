#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

##############################################################################################################
# Kafka Test - Regenerates the Kafka Test p12s
# TYPE   : KAFKA-TEST-TRUSTSTORE
# FILE   : fhir-server/liberty-config/resources/security/kafka.client.truststore.p12
# ALIAS  : caroot
# SHASUM : CE:D3:36:4A:4C:87:E8:E5:90:F5:2B:B5:AF:D2:E7:46:6C:EF:3E:AF:B8:0C:A5:9E:72:E8:87:57:29:13:D3:70
# LINKS  : <TO KEYSTORE>

# TYPE   : KAFKA-TEST-KEYSTORE
# FILE   : fhir-server/liberty-config/resources/security/kafka.client.keystore.p12
# ALIAS  : caroot
#          localhost
# SHASUM : CE:D3:36:4A:4C:87:E8:E5:90:F5:2B:B5:AF:D2:E7:46:6C:EF:3E:AF:B8:0C:A5:9E:72:E8:87:57:29:13:D3:70
#          14:4C:9F:B7:7E:FC:1E:29:35:3A:8D:F2:EC:31:8F:8C:7E:FD:61:97:57:EF:19:B0:F5:CE:2F:70:5F:B6:7B:2C
# LINKS  : <To Trust Store only and a Devs old test machine>

create_kafka_test(){
    keytool -list -keystore ${WORKSPACE}/fhir-server/liberty-config/resources/security/kafka.client.truststore.p12 -storepass ${CHANGE_PASSWORD}
    keytool -list -keystore ${WORKSPACE}/fhir-server/liberty-config/resources/security/kafka.client.keystore.p12 -storepass ${CHANGE_PASSWORD}

    cd tmp
    openssl req -new -newkey rsa:4096 -x509  -keyout client.key -out client.crt -days 10960 -subj '/CN=ca-kafka-client.test.fhir.ibm.com/OU=TEST/O=IBM FHIR SERVER/L=CAMBRIDGE/ST=MA/C=US' -passin pass:change-password -passout pass:change-password

    keytool -genkey -noprompt \
                    -alias kafka-test \
                    -dname "CN=kafka-client.test.fhir.ibm.com, OU=TEST, O=IBM FHIR SERVER, L=CAMBRIDGE, ST=MA, C=US" \
                    -keystore kafka.client.keystore.p12 \
                    -keyalg RSA \
                    -storetype PKCS12 \
                    -keysize 4096 \
                    -validity 10960 \
                    -storepass change-password \
                    -keypass change-password

    keytool -keystore kafka.client.keystore.p12 -alias kafka-test -certreq -file client.csr -storepass change-password -keypass change-password

    openssl x509 -req -CA client.crt -CAkey client.key -in client.csr -out client-signed.crt -days 10960 -CAcreateserial -passin pass:change-password

    keytool -keystore kafka.client.keystore.p12 -alias CARoot -import -file client.crt -storepass change-password -keypass change-password -noprompt

    keytool -keystore kafka.client.keystore.p12 -alias client -import -file client-signed.crt -storepass change-password -keypass change-password -noprompt

    keytool -keystore kafka.client.truststore.p12 -alias CARoot -import -file client.crt -storepass change-password -keypass change-password -noprompt

    cd ..
    mv ${WORKSPACE}/build/certificates/tmp/kafka.client.truststore.p12 ${WORKSPACE}/fhir-server/liberty-config/resources/security/kafka.client.truststore.p12
    mv ${WORKSPACE}/build/certificates/tmp/kafka.client.keystore.p12 ${WORKSPACE}/fhir-server/liberty-config/resources/security/kafka.client.keystore.p12
}

create_kafka_test

# EOF