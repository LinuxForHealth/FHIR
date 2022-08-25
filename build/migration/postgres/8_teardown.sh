#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2021, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

echo "- Dropping Schema"
java -jar ${WORKSPACE}/fhir/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
    --db-type postgresql --prop db.host=localhost --prop db.port=5432 --prop db.database=fhirdb \
    --prop user=fhiradmin --prop password=change-password \
    --schema-name FHIRDATA --drop-schema-fhir --confirm-drop --pool-size 1