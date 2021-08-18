#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# truststore 
cp -f ${WORKSPACE}/build/certificates/tmp/fhirTrustStore.p12 \
    ${WORKSPACE}/fhir-client/src/test/resources/fhirClientTrustStore.p12

cp -f ${WORKSPACE}/build/certificates/tmp/fhirTrustStore.p12 \
    ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientTrustStore.p12

# fhir-client
cp -f ${WORKSPACE}/build/certificates/tmp/fhirClientKeyStore.p12 \
    ${WORKSPACE}/fhir-client/src/test/resources/fhirClientKeyStore.p12

# fhir-server-test copy over the keystore
cp -f ${WORKSPACE}/build/certificates/tmp/fhirClientKeyStore.p12 \
    ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientKeyStore.p12
# EOF