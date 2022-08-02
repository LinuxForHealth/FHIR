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

CONFIG="${WORKSPACE}/build/azurite/fhir-server/config"
TEST_RESOURCES="${WORKSPACE}/fhir-server-test/src/test/resources"

# Set the fhir-server-config
cp ${CONFIG}/default/fhir-server-config-postgresql-azurite.json ${CONFIG}/default/fhir-server-config.json

# Enable the Azure-based export tests
sed -i -e 's/test.bulkdata.export.azure.enabled = false/test.bulkdata.export.azure.enabled = true/g' ${TEST_RESOURCES}/test.properties
# uncomment to enable azure import tests; need to add automated import test setup (by seeding the container with NDJSON)
#sed -i -e 's/test.bulkdata.import.azure.enabled = false/test.bulkdata.import.azure.enabled = true/g' ${TEST_RESOURCES}/test.properties