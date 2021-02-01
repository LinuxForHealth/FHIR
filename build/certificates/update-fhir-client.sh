#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# truststore 
keytool -list -keystore ${WORKSPACE}/fhir-client/src/test/resources/fhirClientTrustStore.p12 -storepass ${CHANGE_PASSWORD} -v
cp ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirTrustStore.p12 ${WORKSPACE}/fhir-client/src/test/resources/fhirClientTrustStore.p12

keytool -list -keystore ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientTrustStore.p12 -storepass ${CHANGE_PASSWORD} -v
cp ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirTrustStore.p12 ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientTrustStore.p12

# fhir-client
keytool -list -keystore ${WORKSPACE}/fhir-client/src/test/resources/fhirClientKeyStore.p12 -storepass ${CHANGE_PASSWORD} -v
cp ${WORKSPACE}/build/certificates/tmp/fhiruserKeyStore.p12 ${WORKSPACE}/fhir-client/src/test/resources/fhirClientKeyStore.p12
keytool -delete -keystore ${WORKSPACE}/fhir-client/src/test/resources/fhirClientKeyStore.p12 -storepass ${CHANGE_PASSWORD} -alias caroot -noprompt

# fhir-server-test copy over the keystore
keytool -list -keystore ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientKeyStore.p12 -storepass ${CHANGE_PASSWORD} -v
cp ${WORKSPACE}/fhir-client/src/test/resources/fhirClientKeyStore.p12 ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientKeyStore.p12
# EOF