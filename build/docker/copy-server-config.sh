#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -e

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi

echo "Removing the old server config..."
CONFIG="${WORKSPACE}/build/docker/fhir-server/volumes/config"
rm -rf $CONFIG/* 2> /dev/null
mkdir -p $CONFIG

echo "Copying the server config files..."
cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/* ${CONFIG}
cp -pr ${WORKSPACE}/fhir-server/liberty-config-tenants/config/* ${CONFIG}
cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/default/fhir-server-config-db2.json ${CONFIG}/default/fhir-server-config.json

echo "Replacing datasource content in server configDropins..."
OVERRIDES="${WORKSPACE}/build/docker/fhir-server/volumes/overrides"
mkdir -p ${OVERRIDES}

# Just in case it already exists, let's wipe the datsource*.xml files
rm -f ${OVERRIDES}/datasource*.xml 2> /dev/null

# Copy over both the db2 (default_default) and derby (tenant1_*) datasource definitions
cp -p ${WORKSPACE}/fhir-server/liberty-config/configDropins/disabled/datasource-db2.xml ${OVERRIDES}/
cp -p ${WORKSPACE}/fhir-server/liberty-config/configDropins/disabled/datasource-derby.xml ${OVERRIDES}/

echo "Finished copying the server config."
