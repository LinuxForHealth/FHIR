@echo off

@REM ###############################################################################
@REM (C) Copyright IBM Corp. 2016, 2020
@REM 
@REM SPDX-License-Identifier: Apache-2.0
@REM ##############################################################################

echo "Removing old dependencies..."
DIST="volumes/dist"
rm -rf $DIST/* 2> /dev/null

echo "Copying installation zip files..."
cp -p ../target/fhir-server-distribution.zip $DIST

echo "Copying fhir configuration files..."
cp -pr ../../fhir-server/liberty-config/config $DIST
cp -pr ../../fhir-server/liberty-config-tenants/config/* $DIST/config

echo "Finished copying dependencies..."

@REM End of Script