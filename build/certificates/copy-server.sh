#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

cp -f ${WORKSPACE}/build/certificates/tmp/fhirKeyStore.p12 \
    ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirKeyStore.p12
cp -f ${WORKSPACE}/build/certificates/tmp/fhirTrustStore.p12 \
    ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirTrustStore.p12

# EOF