#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -e
set +x
set -o pipefail

###############################################################################

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the migration/bin directory
cd "${WORKSPACE}/fhir/build/migration/db2"

# Startup db
docker-compose up --remove-orphans -d db

echo "Debug Details >>> "
docker container inspect db2_db_1 | jq -r '.[] | select (.Config.Hostname == "db2").State.Status'

echo "Debug All Details >>> "
docker container inspect db2_db_1 | jq -r '.[]'

cx=0
while [ $(docker container inspect db2_db_1 | jq -r '.[] | select (.Config.Hostname == "db2").State.Status' | wc -l) -gt 0 ] && [ $(docker container inspect db2_db_1 | jq -r '.[] | select (.Config.Hostname == "db2").State.Health.Status' | grep starting | wc -l) -eq 1 ]
do
    echo "Waiting on startup of db ${cx}"
    cx=$((cx + 1))
    if [ ${cx} -ge 300 ]
    then
        echo "Failed to start"
        break
    fi
    sleep 10
done

echo ">>> Persistence >>> current is being run"
echo 'change-password' > tenant.key
java -jar ${WORKSPACE}/fhir/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
    --db-type db2 --prop db.host=localhost --prop db.port=50000 --prop db.database=fhirdb \
    --prop user=db2inst1 --prop password=change-password \
    --update-schema --grant-to fhirserver
# This additional allocate-tenant step is required for migrating db2 instances to version 5.0 because
# we need to add new values to the resource_types table and that only happens during --allocate-tenant
# because it is partitioned by mt_id like the other fhirdata tables
java -jar ${WORKSPACE}/fhir/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
    --db-type db2 --prop db.host=localhost --prop db.port=50000 --prop db.database=fhirdb \
    --prop user=db2inst1 --prop password=change-password \
    --allocate-tenant default

echo ">>> Set up the derby databases for multidatastore scenarios"
DB_LOC="${WORKSPACE}/fhir/build/migration/db2/workarea/volumes/dist/derby"
rm -rf ${WORKSPACE}/fhir/build/migration/db2/workarea/volumes/dist/derby
mkdir -p ${DB_LOC}
java -jar ${WORKSPACE}/fhir/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
    --db-type derby --prop db.database=${DB_LOC}/profile --prop db.create=Y \
    --prop resourceTypes=${TENANT1_PROFILE_RESOURCE_TYPES} \
    --update-schema
java -jar ${WORKSPACE}/fhir/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
    --db-type derby --prop db.database=${DB_LOC}/reference --prop db.create=Y \
    --prop resourceTypes=${TENANT1_REFERENCE_RESOURCE_TYPES} \
    --update-schema
java -jar ${WORKSPACE}/fhir/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
    --db-type derby --prop db.database=${DB_LOC}/study1 --prop db.create=Y \
    --prop resourceTypes=${TENANT1_STUDY1_RESOURCE_TYPES} \
    --update-schema

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################
