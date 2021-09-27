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

echo "Removing old test operations..."
USERLIB="${WORKSPACE}/build/docker/fhir-server/volumes/userlib"
rm -rf $USERLIB/* 2> /dev/null
mkdir -p $USERLIB

echo "Copying test artifacts to install location..."
find ${WORKSPACE}/conformance -iname 'fhir-ig*.jar' -not -iname 'fhir*-tests.jar' -not -iname 'fhir*-test-*.jar' -exec cp -f {} ${USERLIB} \;
cp -pr ${WORKSPACE}/operation/fhir-operation-test/target/fhir-operation-*-tests.jar ${USERLIB}
cp -pr ${WORKSPACE}/term/operation/fhir-operation-term-cache/target/fhir-operation-*.jar ${USERLIB}

echo "Finished copying test operations."
