#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

DIST="${WORKSPACE}/build/persistence/postgres/workarea/volumes/dist"
SCHEMA="${WORKSPACE}/build/persistence/postgres/workarea/schema"

# pre_integration -
pre_integration(){
    cleanup_prior
    cleanup_existing_docker
    bring_up_database
    copy_schema_jar
    deploy_schema
    copy_server_config
    bringup_fhir
}

# cleanup_prior - Cleans up the prior files
cleanup_prior(){
    echo "Removing old dependencies..."
    if [ -d $DIST ]
    then
        rm -rf $DIST/* 2> /dev/null
    fi

    if [ -d $SCHEMA ]
    then
        rm -rf $SCHEMA/* 2> /dev/null
    fi
    mkdir -p $DIST
    mkdir -p $SCHEMA
}

# copy_server_config - Copy assembled files
copy_server_config(){
    echo "Copying installation zip files..."
    cp ${WORKSPACE}/fhir-install/target/fhir-server-distribution.zip ${DIST}

    echo "Copying fhir configuration files..."
    cp -r ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config ${DIST}
    cp -r ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/config/* ${DIST}/config
    bash ${WORKSPACE}/build/common/update-server-registry-resource.sh ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config/default/fhir-server-config-postgresql.json
    cp -r ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config/default/fhir-server-config-postgresql.json $DIST/config/default/fhir-server-config.json

    # Note the overrides folder is specifically mounted to the docker image under configDropins/overrides
    echo "Creating an overrides folder in $DIST"
    mkdir -p $DIST/overrides

    # Copy over both the postgres (default_default) and derby (tenant1_*) datasource definitions
    cp ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/datasource-postgresql.xml $DIST/overrides/
    cp ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/configDropins/overrides/datasource-derby.xml $DIST/overrides/

    USERLIB="${DIST}/userlib"
    mkdir -p $USERLIB

    echo "Copying test artifacts to install location..."
    find ${WORKSPACE}/conformance -iname 'fhir-ig*.jar' -not -iname 'fhir*-tests.jar' -not -iname 'fhir*-test-*.jar' -exec cp -f {} ${USERLIB} \;
    cp ${WORKSPACE}/operation/fhir-operation-test/target/fhir-operation-*.jar ${USERLIB}
    cp ${WORKSPACE}/term/operation/fhir-operation-term-cache/target/fhir-operation-*.jar ${USERLIB}
    echo "Finished copying fhir-server dependencies..."
}

# cleanup_existing_docker - cleanup existing docker
cleanup_existing_docker(){
    # Stand up a docker container running the fhir server configured for integration tests
    echo "Bringing down any containers that might already be running as a precaution"
    docker-compose kill
    docker-compose rm -f
}

# bring_up_database - brings up database
# - remove the existing db directory
# - build postgres and wait
bring_up_database(){
    if [ -d ${WORKSPACE}/build/persistence/postgres/db ]
    then
        rm -rf ${WORKSPACE}/build/persistence/postgres/db
    fi

    mkdir -p ${WORKSPACE}/build/persistence/postgres/db
    echo "Bringing up postgres... be patient, this will take a minute"
    docker-compose build --pull postgres
    docker-compose up --remove-orphans -d postgres
    echo ">>> Current time: " $(date)

    # Waiting to startup
    count=0
    echo "Waiting while starting up..."
    while [ `docker-compose logs --timestamps postgres | grep -c 'database system is ready to accept connections'` -ne 1 ] && [ "${count}" -ne 120 ]
    do
        echo "... Waiting ... - ${count}"
        docker-compose logs --timestamps postgres
        sleep 5
        count=$((count+1))
    done
}

# copy_schema_jar
copy_schema_jar(){
    echo "Copying fhir-persistence-schema tool..."
    cp -pr ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar $SCHEMA
}

# setup_schema - sets up the schema (in concert with the db)
deploy_schema(){
    cat << EOF > ${WORKSPACE}/build/persistence/postgres/workarea/postgres.properties
db.host=localhost
db.port=5432
db.database=fhirdb
user=fhiradmin
password=change-password
EOF
    echo "Waiting..."
    sleep 60

    # Pool-size is set to 1, because of... org.postgresql.util.PSQLException: ERROR: deadlock detected
    echo "- Create Schemas"
    java -jar ${SCHEMA}/fhir-persistence-schema-*-cli.jar --db-type postgresql \
        --prop-file workarea/postgres.properties --schema-name FHIRDATA --create-schemas --pool-size 1

    echo "- Update Schema"
    java -jar ${SCHEMA}/fhir-persistence-schema-*-cli.jar --db-type postgresql \
        --prop-file workarea/postgres.properties --schema-name FHIRDATA --update-schema --pool-size 1

    echo "- Grants to FHIRSERVER"
    java -jar ${SCHEMA}/fhir-persistence-schema-*-cli.jar --db-type postgresql \
       --prop-file workarea/postgres.properties --schema-name FHIRDATA --grant-to FHIRSERVER --pool-size 1
}

# bringup_fhir
bringup_fhir(){
    echo "Bringing up the FHIR server... be patient, this will take a minute"
    docker-compose up --remove-orphans -d fhir-server
    echo ">>> Current time: " $(date)

    # TODO wait for it to be healthy instead of just Sleeping
    (docker-compose logs --timestamps --follow fhir-server & P=$! && sleep 60 && kill $P)

    # Gather up all the server logs so we can trouble-shoot any problems during startup
    cd -
    pre_it_logs=${WORKSPACE}/pre-it-logs
    zip_file=${WORKSPACE}/pre-it-logs.zip
    rm -rf ${pre_it_logs} 2>/dev/null
    mkdir -p ${pre_it_logs}
    rm -f ${zip_file}

    echo "
    Docker container status:"
    docker ps -a

    containerId=$(docker ps -a | grep fhir | cut -d ' ' -f 1)
    if [[ -z "${containerId}" ]]; then
        echo "Warning: Could not find the fhir container!!!"
    else
        echo "fhir container id: $containerId"

        # Grab the container's console log
        docker logs $containerId  >& ${pre_it_logs}/docker-console.txt

        echo "Gathering pre-test server logs from docker container: $containerId"
        docker cp -L $containerId:/logs ${pre_it_logs}

        echo "Zipping up pre-test server logs"
        zip -r ${zip_file} ${pre_it_logs}
    fi

    # Wait until the fhir server is up and running...
    echo "Waiting for fhir-server to complete initialization..."
    healthcheck_url='https://localhost:9443/fhir-server/api/v4/$healthcheck'
    tries=0
    status=0
    while [ $status -ne 200 -a $tries -lt 30 ]; do
        tries=$((tries + 1))
        set +o errexit
        cmd="curl -k -o ${WORKSPACE}/health.json --max-time 5 -I -w "%{http_code}" -u fhiruser:change-password $healthcheck_url"
        echo "Executing[$tries]: $cmd"
        status=$($cmd)
        set -o errexit
        echo "Status code: $status"
        if [ $status -ne 200 ]
        then
        echo "Sleeping 30 secs..."
        sleep 30
        fi
    done

    if [ $status -ne 200 ]
    then
        echo "Could not establish a connection to the fhir-server within $tries REST API invocations!"
        exit 1
    fi

    echo "The fhir-server appears to be running..."
    exit 0
}

# is_ready_to_run - is this ready to run?
is_ready_to_run(){
    echo "Preparing environment for fhir-server integration tests..."
    if [ -z "${WORKSPACE}" ]
    then
        echo "ERROR: WORKSPACE environment variable not set!"
        exit 1
    fi
}

###############################################################################
is_ready_to_run

cd build/persistence/postgres
pre_integration

# EOF
###############################################################################
