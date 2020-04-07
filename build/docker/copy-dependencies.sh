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

echo "Removing old dependencies..."
DIST="${WORKSPACE}/build/docker/liberty/dist"
CONFIG="${WORKSPACE}/build/docker/liberty/config"
rm -rf $DIST/* 2> /dev/null
rm -rf $CONFIG/* 2> /dev/null
mkdir -p $DIST
mkdir -p $CONFIG

echo "Copying installation zip files..."
cp -p ${WORKSPACE}/fhir-install/target/fhir-server-distribution.zip ${DIST}

echo "Copying fhir configuration files..."
cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/* ${CONFIG}
cp -pr ${WORKSPACE}/fhir-server/liberty-config-tenants/config/* ${CONFIG}

echo "Finished copying fhir-server dependencies..."
