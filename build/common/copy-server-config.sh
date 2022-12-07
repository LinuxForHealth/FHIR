#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi

echo "Removing the old server config..."
CONFIG="fhir-server/config"
rm -rf ${CONFIG}/* 2> /dev/null
mkdir -p ${CONFIG}

echo "Copying the server config files..."
cp -r ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config/* ${CONFIG}/
cp -r ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/config/* ${CONFIG}/
cp ${CONFIG}/default/fhir-server-config-postgresql-minio.json ${CONFIG}/default/fhir-server-config.json

echo "Replacing datasource content in server configDropins..."
OVERRIDES="fhir-server/configDropins/overrides"
BULKDATA_OVERRIDES="fhir-bulkdata-server/configDropins/overrides"
rm -rf ${OVERRIDES}/* 2> /dev/null
mkdir -p ${OVERRIDES}

echo "Create overrides directory for bulkdata db config..."
rm -rf ${BULKDATA_OVERRIDES}/* 2> /dev/null
mkdir -p ${BULKDATA_OVERRIDES}

# Copy over both the postgres (default_default) and derby (tenant1_*) datasource definitions
cp ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/configDropins/overrides/datasource-derby.xml ${OVERRIDES}/
cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/datasource-postgresql.xml ${OVERRIDES}/

echo "Copy over both the postgres (default_default) and derby (tenant1_*) datasource definitions to bulkdata_overrides..."
cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/bulkdata/disabled/postgres/datasource-bulkdata-postgres.xml ${BULKDATA_OVERRIDES}/
cp ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/configDropins/overrides/datasource-derby.xml ${BULKDATA_OVERRIDES}/
cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/datasource-postgresql.xml ${BULKDATA_OVERRIDES}/

echo "Finished copying the server config."
