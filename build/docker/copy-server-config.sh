#!/usr/bin/env bash
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

echo "Removing the old server config..."
CONFIG="${WORKSPACE}/build/docker/fhir-server/volumes/config"
rm -rf $CONFIG/* 2> /dev/null
mkdir -p $CONFIG

BULKDATA="${WORKSPACE}/build/docker/fhir-server/volumes/bulkdata/"
mkdir -p ${BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import.ndjson ${BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import-skip.ndjson ${BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import-neg.ndjson ${BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import-mismatch.ndjson ${BULKDATA}

S3_BULKDATA="${WORKSPACE}/build/docker/minio/miniodata/fhirbulkdata/"
mkdir -p ${S3_BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import.ndjson ${S3_BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import-skip.ndjson ${S3_BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import-neg.ndjson ${S3_BULKDATA}
cp ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/import-operation/test-import-mismatch.ndjson ${S3_BULKDATA}

# Appending the path
echo "test.bulkdata.path = ${BULKDATA}" >> ${WORKSPACE}/fhir-server-test/src/test/resources/test.properties

echo "Copying the server config files..."
cp -r ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config/* ${CONFIG}
cp -r ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/config/* ${CONFIG}
bash ${WORKSPACE}/build/update-server-registry-resource.sh ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config/default/fhir-server-config-db2.json
cp -r ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config/default/fhir-server-config-db2.json ${CONFIG}/default/fhir-server-config.json

echo "Replacing datasource content in server configDropins..."
OVERRIDES="${WORKSPACE}/build/docker/fhir-server/volumes/overrides"
mkdir -p ${OVERRIDES}

# Just in case it already exists, let's wipe the datsource*.xml files
rm -f ${OVERRIDES}/datasource*.xml

# Copy over both the db2 (default_default) and derby (tenant1_*) datasource definitions
cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/datasource-db2.xml ${OVERRIDES}/
cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/db2/bulkdata.xml ${OVERRIDES}/
cp ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/configDropins/overrides/datasource-derby.xml ${OVERRIDES}/

echo "Finished copying the server config."
