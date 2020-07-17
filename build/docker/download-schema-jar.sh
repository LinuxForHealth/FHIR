#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -e

SCHEMA_VERSION="4.3.2"

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi

echo "Removing the old fhir-persistence-schema tool..."
SCHEMA="${WORKSPACE}/build/docker/schema"
rm -rf $SCHEMA/* 2> /dev/null
mkdir -p $SCHEMA

if [[ -f "${SCHEMA}/fhir-persistence-schema-${SCHEMA_VERSION}-cli.jar" ]]; then
    echo "Using previously downloaded version ${SCHEMA_VERSION} of the fhir-persistence-schema tool"
else
    echo "Downloading version ${SCHEMA_VERSION} of the fhir-persistence-schema tool..."
    curl https://dl.bintray.com/ibm-watson-health/ibm-fhir-server-releases/com/ibm/fhir/fhir-persistence-schema/${SCHEMA_VERSION}/fhir-persistence-schema-${SCHEMA_VERSION}-cli.jar \
         --output ${SCHEMA}/fhir-persistence-schema-${SCHEMA_VERSION}-cli.jar \
         --location \
         --fail
fi
