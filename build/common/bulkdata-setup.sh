#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

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
