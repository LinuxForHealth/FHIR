#!/usr/bin/env bash

set -e

###############################################################################
# (C) Copyright IBM Corp. 2016, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Downloads the Schema JAR from the Releases entry on GitHub.com

SCHEMA_VERSION="4.3.0"

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
    curl https://github.com/LinuxForHealth/FHIR/releases/download/${SCHEMA_VERSION}/fhir-persistence-schema-${SCHEMA_VERSION}-cli.jar \
         --output ${SCHEMA}/fhir-persistence-schema-${SCHEMA_VERSION}-cli.jar \
         --location \
         --fail
fi

# EOF
