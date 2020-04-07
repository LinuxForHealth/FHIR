#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -e

SCHEMA_VERSION="4.0.1"

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 2
fi

echo "Removing old dependencies..."
DIST="${WORKSPACE}/build/docker/liberty/dist"
CONFIG="${WORKSPACE}/build/docker/liberty/config"
SCHEMA="${WORKSPACE}/build/docker/schema"
rm -rf $DIST/* 2> /dev/null
rm -rf $CONFIG/* 2> /dev/null
rm -rf $SCHEMA/* 2> /dev/null
mkdir -p $DIST
mkdir -p $CONFIG
mkdir -p $SCHEMA

echo "Copying installation zip files..."
cp -p ${WORKSPACE}/fhir-install/target/fhir-server-distribution.zip ${DIST}

echo "Copying fhir configuration files..."
cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/* ${CONFIG}
cp -pr ${WORKSPACE}/fhir-server/liberty-config-tenants/config/* ${CONFIG}
cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/default/fhir-server-config-db2.json ${CONFIG}/default/fhir-server-config.json

echo "Copying fhir-persistence-schema tool..."
curl https://dl.bintray.com/ibm-watson-health/ibm-fhir-server-releases/com/ibm/fhir/fhir-persistence-schema/${SCHEMA_VERSION}/fhir-persistence-schema-${SCHEMA_VERSION}-cli.jar \
     --output ${SCHEMA}/fhir-persistence-schema-${SCHEMA_VERSION}-cli.jar \
     --location \
     --fail

echo "Finished copying fhir-server dependencies..."
