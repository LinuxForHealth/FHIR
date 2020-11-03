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
rm -f $OVERRIDES/datasource-*.xml 2> /dev/null
mkdir -p $OVERRIDES
cp -p ${WORKSPACE}/fhir-server/liberty-config/configDropins/overrides/datasource-bootstrap.xml $OVERRIDES
cp -p ${WORKSPACE}/fhir-server/liberty-config/configDropins/overrides/datasource-db2.xml $OVERRIDES

echo "Finished copying the server config."
