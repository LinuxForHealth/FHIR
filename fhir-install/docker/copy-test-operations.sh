#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

DIST="volumes/dist"

echo "Copying test artifacts to install location..."
cp -pr ../../fhir-operation/target/fhir-operation-*-tests.jar $DIST/userlib/
