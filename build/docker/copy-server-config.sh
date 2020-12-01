#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
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
mkdir -p $OVERRIDES

# Just in case it already exists, let's wipe the datsource*.xml files
rm -f $OVERRIDES/datasource*.xml 2> /dev/null

# Currently, the fhir-server-config-db2.json configuration uses the legacy
# proxy datasource connection strategy.
# Uncomment the next two lines when the Db2 fhir-server-config (above) is switched
# over to use the standard datasource configuration (see fhir-server-config-postgres.json).
#cp -p ${WORKSPACE}/fhir-server/liberty-config/configDropins/disabled/datasource-derby.xml $OVERRIDES/database.xml
#cp -p ${WORKSPACE}/fhir-server/liberty-config/configDropins/disabled/datasource-db2.xml $OVERRIDES/database.xml

echo "Finished copying the server config."
