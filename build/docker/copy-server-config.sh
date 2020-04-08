#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -e

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 2
fi

echo "Removing the old server config..."
CONFIG="${WORKSPACE}/build/docker/fhir-server/volumes/config"
rm -rf $CONFIG/* 2> /dev/null
mkdir -p $CONFIG

echo "Copying the server config files..."
cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/* ${CONFIG}
cp -pr ${WORKSPACE}/fhir-server/liberty-config-tenants/config/* ${CONFIG}
cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/default/fhir-server-config-db2.json ${CONFIG}/default/fhir-server-config.json

echo "Finished copying the server config."
