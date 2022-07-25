#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi

echo "Removing the old server config..."
CONFIG="${WORKSPACE}/build/docker/fhir-server/config"
rm -rf ${CONFIG}/* 2> /dev/null
mkdir -p ${CONFIG}

echo "Copying the server config files..."
cp -r ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config/* ${CONFIG}/
cp -r ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/config/* ${CONFIG}/
cp ${CONFIG}/default/fhir-server-config-postgresql-complete.json ${CONFIG}/default/fhir-server-config.json

echo "Replacing datasource content in server configDropins..."
OVERRIDES="${WORKSPACE}/build/docker/fhir-server/configDropins/overrides"
rm -rf ${OVERRIDES}/* 2> /dev/null
mkdir -p ${OVERRIDES}

# Copy over both the postgres (default_default) and derby (tenant1_*) datasource definitions
cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/datasource-postgresql.xml ${OVERRIDES}/
cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/postgres/bulkdata.xml ${OVERRIDES}/
cp ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/configDropins/overrides/datasource-derby.xml ${OVERRIDES}/

echo "Finished copying the server config."
