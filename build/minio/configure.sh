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

CONFIG="${WORKSPACE}/build/minio/fhir-server/config"
TEST_RESOURCES="${WORKSPACE}/fhir-server-test/src/test/resources"

# Set the fhir-server-config
cp ${CONFIG}/default/fhir-server-config-postgresql-minio.json ${CONFIG}/default/fhir-server-config.json

# Enable the S3-based import/export tests
sed -i -e 's/test.bulkdata.import.s3.enabled = false/test.bulkdata.import.s3.enabled = true/g' ${TEST_RESOURCES}/test.properties
sed -i -e 's/test.bulkdata.export.s3.enabled = false/test.bulkdata.export.s3.enabled = true/g' ${TEST_RESOURCES}/test.properties

TEST_RESOURCES="${WORKSPACE}/fhir-server-test/src/test/resources"

S3_BULKDATA="${WORKSPACE}/build/minio/minio/miniodata/bulkdata/"
mkdir -p ${S3_BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import.ndjson ${S3_BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import-skip.ndjson ${S3_BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import-neg.ndjson ${S3_BULKDATA}
cp ${TEST_RESOURCES}/testdata/import-operation/test-import-mismatch.ndjson ${S3_BULKDATA}