#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

cp -f ${WORKSPACE}/build/certificates/tmp/kafka.client.truststore.p12 \
    ${WORKSPACE}/fhir-server/liberty-config/resources/security/kafka.client.truststore.p12
cp -f ${WORKSPACE}/build/certificates/tmp/kafka.client.keystore.p12 \
    ${WORKSPACE}/fhir-server/liberty-config/resources/security/kafka.client.keystore.p12

# EOF