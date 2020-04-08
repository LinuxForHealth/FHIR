#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

echo "Removing old dependencies..."
DIST="volumes/dist"
SCHEMA="volumes/schema"
rm -rf $DIST/* 2> /dev/null
rm -rf $SCHEMA/* 2> /dev/null
mkdir -p $DIST
mkdir -p $SCHEMA

echo "Copying installation zip files..."
cp -p ../target/fhir-server-distribution.zip $DIST

echo "Copying fhir configuration files..."
cp -pr ../../fhir-server/liberty-config/config $DIST
cp -pr ../../fhir-server/liberty-config-tenants/config/* $DIST/config
cp -pr ../../fhir-server/liberty-config/config/default/fhir-server-config-db2.json $DIST/config/default/fhir-server-config.json

echo "Copying fhir-persistence-schema tool..."
cp -pr ../../fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar $SCHEMA

echo "Finished copying fhir-server dependencies..."