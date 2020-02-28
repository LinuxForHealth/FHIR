#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -e

echo "Removing old dependencies..."
DIST="volumes/dist"
rm -rf $DIST/* 2> /dev/null
mkdir -p $DIST

echo "Copying installation zip files..."
cp -p ../target/fhir-server-distribution.zip $DIST

echo "Copying fhir configuration files..."
cp -pr ../../fhir-server/liberty-config/config $DIST
cp -pr ../../fhir-server/liberty-config-tenants/config/* $DIST/config

echo "Finished copying dependencies..."
