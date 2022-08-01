#!/usr/bin/env sh
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi

TEST_RESOURCES="${WORKSPACE}/fhir-server-test/src/test/resources"

BULKDATA="fhir-server/bulkdata/"
mkdir -p ${BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import.ndjson ${BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import-skip.ndjson ${BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import-neg.ndjson ${BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import-mismatch.ndjson ${BULKDATA}

# Enable bulkdata export/import tests and set the path
sed -i -e 's/test.bulkdata.export.enabled = false/test.bulkdata.export.enabled = true/g' ${TEST_RESOURCES}/test.properties
sed -i -e 's/test.bulkdata.import.enabled = false/test.bulkdata.import.enabled = true/g' ${TEST_RESOURCES}/test.properties
sed -i -e "s:test.bulkdata.path = .*:test.bulkdata.path = ${PWD}/${BULKDATA}:" ${TEST_RESOURCES}/test.properties