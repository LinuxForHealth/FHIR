#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -e

DIST="volumes/dist"

echo "Copying test artifacts to install location..."
mkdir -p $DIST/userlib/
cp -pr ../../fhir-operation/target/fhir-operation-*-tests.jar $DIST/userlib/
