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

echo "Removing old test operations..."
USERLIB="${WORKSPACE}/build/docker/liberty/userlib"
rm -rf $USERLIB/* 2> /dev/null
mkdir -p $USERLIB

echo "Copying test artifacts to install location..."
cp -pr ${WORKSPACE}/fhir-operation/target/fhir-operation-*-tests.jar ${USERLIB}

echo "Finished copying test operations..."
