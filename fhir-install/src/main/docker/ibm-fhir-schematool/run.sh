#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

set -e -o pipefail

# When debugging -x is an accepted practice, when not debugging, please comment out.
# set -x

##############################################################################
# The global variables used are: 

SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"

# Override this when testing locally.
SCHEMA_TOOL_LOCATION="/opt/schematool"

# Creates the temporary TOOL_INPUT lifecycle is the running script.
TOOL_INPUT_FILE=$(mktemp) || { echo "Failed to create the temporary properties file"; exit 1; }
TOOL_INPUT_USED="${ENV_TOOL_INPUT}"

# Tool Input - Acceptable is console or file
TOOL_OUTPUT_USED=${ENV_TOOL_OUTPUT}
[ -z "${TOOL_OUTPUT_USED}" ] && TOOL_OUTPUT_USED="file"
TOOL_OUTPUT_FILE="/opt/schematool/output.`date +%F_%H-%M-%S`"

# Debug - Flags if DEBUG is true or anything else
TOOL_DEBUG=${ENV_TOOL_DEBUG}
[ -z "${TOOL_DEBUG}" ] && TOOL_DEBUG="false"

# SKIP - initial value is forced to lowercase
TOOL_SKIP=`echo "${ENV_SKIP}" | tr '[:upper:]' '[:lower:]'`
[ -z "${TOOL_SKIP}" ] && TOOL_SKIP="false"

# Arguments:
TOOL_ARGS="$@"

##############################################################################
# Helper Functions

# debug - labels messages as debug
function debug {
    if [ "$TOOL_DEBUG" = "true" ]
    then
        echo "${SCRIPT_NAME} - [DEBUG]: $(date +"%Y-%m-%d_%T") - ${1}" 
    fi
}

# info - labels messages as info
function info {  
    echo "${SCRIPT_NAME} - [INFO]: $(date +"%Y-%m-%d_%T") - ${1}" 
}

# error_warn - labels messages as error
function error_warn {  
    echo "${SCRIPT_NAME} - [ERROR]: $(date +"%Y-%m-%d_%T") - ${1}" 
}

# get_property - gets the property value from the input_file
# returns (echo) the property from the INPUT_FILE
function get_property {
    PROP_NAME="${1}"
    JQ="${2}"
    PROP_VALUE=`cat ${TOOL_INPUT_FILE} | grep ${PROP_NAME}= | sed 's|=| |' | awk '{print $2}'`
    if [ ! -z "${PROP_VALUE}" ]
    then
        echo ${PROP_VALUE}
    else
        # only run if the file exists
        if [ -f /opt/schematool/workarea/persistence.json ]
        then
            PROP_VALUE=$(/opt/schematool/jq -r ${JQ} /opt/schematool/workarea/persistence.json)
            echo ${PROP_VALUE}
        fi
    fi
}

##############################################################################
# Environment Processing Functions

# process_cmd_properties - takes the environment properties
# and serializes to a temporary file in the container, which is destroyed at the end of the 
# schell script.
function process_cmd_properties {
    if [ ! -z "${TOOL_INPUT_USED}" ]
    then
        OS_TYPE="$(uname -s)"
        case "${OS_TYPE}" in
            Linux*)
                # Since the pipe is used in a couple places where it can fail
                # we don't want it to kill this script, and have overriden (temporarily)
                # the fail on pipe and errexit, we'll control the exits.
                set +o errexit
                set +o pipefail
                # only run if the file doesn't exist (we default to the existing file as it's been mounted)
                if [ ! -f /opt/schematool/workarea/persistence.json ]
                then
                    # originally there was error handling following this to check if the contents are valid json.
                    # We  have opted to verify inline to each call.
                    echo -n "${TOOL_INPUT_USED}" | base64 -d > /opt/schematool/workarea/persistence.json 2> /dev/null || true
                    # This is to check if we have possible plain text.
                    RC=$(cat /opt/schematool/workarea/persistence.json | wc -l )
                    if [ "${RC}" = "0" ]
                    then
                        echo "${TOOL_INPUT_USED}" | /opt/schematool/jq -r '.' > /opt/schematool/workarea/persistence.json
                    fi
                fi
                set -o errexit
                set -o pipefail
                ;;
            Darwin*)
                # Since the pipe is used in a couple places where it can fail
                # we don't want it to kill this script, and have overriden (temporarily)
                # the fail on pipe and errexit, we'll control the exits.
                set +o errexit
                set +o pipefail
                # only run if the file doesn't exist (we default to the existing file as it's been mounted)
                if [ ! -f /opt/schematool/workarea/persistence.json ]
                then
                    echo -n "${TOOL_INPUT_USED}" | base64 --decode > /opt/schematool/workarea/persistence.json 2> /dev/null || true
                    # This is to check if we have possible plain text.
                    RC=$(cat /opt/schematool/workarea/persistence.json | wc -l )
                    if [ "${RC}" = "0" ]
                    then
                        echo "${TOOL_INPUT_USED}" | /opt/schematool/jq -r '.' > /opt/schematool/workarea/persistence.json
                    fi
                fi
                set -o errexit
                set -o pipefail
                ;;
            *)  
                exit 1
                ;;
        esac
    fi

    # Take the commandline and dump it out to the temporary inputfile.
    echo -n "" > ${TOOL_INPUT_FILE}
    for TOOL_ARG in ${TOOL_ARGS}
    do 
        echo "${TOOL_ARG}" | sed 's|--||g' >> ${TOOL_INPUT_FILE}
    done
}

# process_cert - extracts the cert from the JSON file
# This is only valid if it's postgres
function process_cert {
    DB_CERT=$(get_property db.cert .persistence[0].db.certificate_base64)
    if [ ! -z "${DB_CERT}" ] && [ $DB_CERT != 'empty' ]
    then
        echo ${DB_CERT} | base64 -d > /opt/schematool/workarea/db.cert
    fi
}

##############################################################################
# Database Specific Wrapper for Commmon Calls

# _call_postgres - local function to call postgres
function _call_postgres {
    INPUT="$1"

    # Get the variables
    DB_HOSTNAME=$(get_property db.host .persistence[0].db.host)
    DB_PORT=$(get_property db.port .persistence[0].db.port)
    DB_NAME=$(get_property db.database .persistence[0].db.database)
    DB_USER=$(get_property user .persistence[0].db.user)
    DB_PASSWORD=$(get_property password .persistence[0].db.password)

    # Check the SSL config and create the SSL_STANZA
    DB_SSL_PG=$(get_property ssl .persistence[0].db.ssl)
    SSL_STANZA=""
    if [ "${DB_SSL_PG}" = "true" ]
    then
        SSL_STANZA="--prop ssl=true --prop sslmode=verify-full --prop sslrootcert=/opt/schematool/workarea/db.cert "
    fi

    # since we are generating, we can debug this... with set +x
    set -x
    /opt/java/openjdk/bin/java -Dlog.dir=${SCHEMA_TOOL_LOCATION}/workarea -jar ${SCHEMA_TOOL_LOCATION}/fhir-persistence-schema-*-cli.jar \
        --prop "db.host=${DB_HOSTNAME}" \
        --prop "db.port=${DB_PORT}" \
        --prop "db.database=${DB_NAME}" \
        --prop "user=${DB_USER}" \
        --prop "password=${DB_PASSWORD}" \
        ${SSL_STANZA} \
        --db-type postgresql \
        ${INPUT} 2>&1 | tee ${SCHEMA_TOOL_LOCATION}/workarea/out.log
    set +x
}

##############################################################################
# General Database Functions

# grant_to_dbuser - grant dbuser so the IBM FHIR Server can use it.
function grant_to_dbuser {
    DB_TYPE=$(get_property db.type .persistence[0].db.type)

    # Get the tenant variable
    TARGET_USER=$(get_property grant.to .persistence[0].grant)
    # null is set here when the value doesn't actually exist.
    if [ -z "${TARGET_USER}" ] || [ "null" = "${TARGET_USER}" ]
    then
        error_warn "Target User is not set and we are skipping the grant phase, it is recommended to run"
    else
        # Pick off the schemas
        SCHEMA_OAUTH=$(get_property schema.name.oauth .persistence[0].schema.oauth)
        if [ -z "${SCHEMA_OAUTH}" ] || [ "null" = "${SCHEMA_OAUTH}" ]
        then
            SCHEMA_OAUTH="FHIR_OAUTH"
        fi
        SCHEMA_BATCH=$(get_property schema.name.batch .persistence[0].schema.batch)
        if [ -z "${SCHEMA_BATCH}" ] || [ "null" = "${SCHEMA_BATCH}" ]
        then
            SCHEMA_BATCH="FHIR_JBATCH"
        fi
        SCHEMA_FHIR=$(get_property schema.name.fhir .persistence[0].schema.fhir)
        if [ -z "${SCHEMA_FHIR}" ] || [ "null" = "${SCHEMA_FHIR}" ]
        then
            SCHEMA_FHIR="FHIRDATA"
        fi

        if [ "${DB_TYPE}" = "postgresql" ]
        then
            _call_postgres "--grant-to ${TARGET_USER} --target BATCH ${SCHEMA_BATCH} --target OAUTH "${SCHEMA_OAUTH}" --target DATA ${SCHEMA_FHIR} --pool-size 5"
        fi
    fi
}

# drop_schema - drops the schema
# SCHEMA - the command to drop the schema
function drop_schema {
    SCHEMA=$1
    DB_TYPE=$(get_property db.type .persistence[0].db.type)
    if [ "${DB_TYPE}" = "postgresql" ]
    then
        # Pick off the schemas
        SCHEMA_OAUTH=$(get_property schema.name.oauth .persistence[0].schema.oauth)
        if [ -z "${SCHEMA_OAUTH}" ] || [ "null" = "${SCHEMA_OAUTH}" ]
        then
            SCHEMA_OAUTH="FHIR_OAUTH"
        fi
        SCHEMA_BATCH=$(get_property schema.name.batch .persistence[0].schema.batch)
        if [ -z "${SCHEMA_BATCH}" ] || [ "null" = "${SCHEMA_BATCH}" ]
        then
            SCHEMA_BATCH="FHIR_JBATCH"
        fi
        SCHEMA_FHIR=$(get_property schema.name.fhir .persistence[0].schema.fhir)
        if [ -z "${SCHEMA_FHIR}" ] || [ "null" = "${SCHEMA_FHIR}" ]
        then
            SCHEMA_FHIR="FHIRDATA"
        fi
        _call_postgres "--drop-schema-fhir --schema-name ${SCHEMA_FHIR} --drop-schema-batch ${SCHEMA_BATCH} --drop-schema-oauth ${SCHEMA_OAUTH} --pool-size 2 --confirm-drop --drop-admin"
    fi
}

# drop_schema_fhir - calls the drop_schema for a specific schema
function drop_schema_fhir { 
    DB_TYPE=$(get_property db.type .persistence[0].db.type)
    MORE_TENANTS=$(has_more_tenants)

    SCHEMA_FHIR=$(get_property schema.name.fhir .persistence[0].schema.fhir)
    if [ -z "${MORE_TENANTS}" ]
    then
        if [ -z "${SCHEMA_FHIR}" ] || [ "null" = "${SCHEMA_FHIR}" ]
        then
            SCHEMA_FHIR="FHIRDATA"
        fi
        drop_schema "--drop-schema-fhir --schema-name ${SCHEMA_FHIR}"
    fi
}

# drop_schema_oauth - calls the drop_schema for a specific schema
function drop_schema_oauth { 
    DB_TYPE=$(get_property db.type .persistence[0].db.type)
    MORE_TENANTS=$(has_more_tenants)

    DB_SCHEMA=$(get_property schema.name.oauth .persistence[0].schema.oauth)
    if [ -z "${MORE_TENANTS}" ]
    then
        if [ -z "${DB_SCHEMA}" ] || [ "null" = "${DB_SCHEMA}" ]
        then
            DB_SCHEMA="FHIR_OAUTH"
        fi
        drop_schema "--drop-schema-oauth --schema-name ${DB_SCHEMA}"
    fi
}

# drop_schema_batch - calls the drop_schema for a specific schema
function drop_schema_batch { 
    DB_TYPE=$(get_property db.type .persistence[0].db.type)
    MORE_TENANTS=$(has_more_tenants)

    DB_SCHEMA=$(get_property schema.name.batch .persistence[0].schema.batch)
    if [ -z "${MORE_TENANTS}" ]
    then
        if [ -z "${DB_SCHEMA}" ] || [ "null" = "${DB_SCHEMA}" ]
        then
            DB_SCHEMA="FHIR_JBATCH"
        fi
        drop_schema "--drop-schema-fhir --schema-name ${DB_SCHEMA}"
    fi
}

# create_schema - creates the schemas
# reverts to the default schemas when it is not set.
function create_schema {
    info "creating the schema"
    DB_TYPE=$(get_property db.type .persistence[0].db.type)
    if [ "${DB_TYPE}" = "postgresql" ]
    then
        # Pick off the schemas
        SCHEMA_OAUTH=$(get_property schema.name.oauth .persistence[0].schema.oauth)
        if [ -z "${SCHEMA_OAUTH}" ] || [ "null" = "${SCHEMA_OAUTH}" ]
        then
            SCHEMA_OAUTH="FHIR_OAUTH"
        fi
        SCHEMA_BATCH=$(get_property schema.name.batch .persistence[0].schema.batch)
        if [ -z "${SCHEMA_BATCH}" ] || [ "null" = "${SCHEMA_BATCH}" ]
        then
            SCHEMA_BATCH="FHIR_JBATCH"
        fi
        SCHEMA_FHIR=$(get_property schema.name.fhir .persistence[0].schema.fhir)
        if [ -z "${SCHEMA_FHIR}" ] || [ "null" = "${SCHEMA_FHIR}" ]
        then
            SCHEMA_FHIR="FHIRDATA"
        fi
        _call_postgres "--create-schema-oauth ${SCHEMA_OAUTH} --create-schema-batch ${SCHEMA_BATCH} --create-schema-fhir ${SCHEMA_FHIR} --pool-size 2"
    fi
    info "done creating the schema"
}

# update_schema 
function update_schema {
    DB_TYPE=$(get_property db.type .persistence[0].db.type)
    if [ "${DB_TYPE}" = "postgresql" ]
    then
        SCHEMA_OAUTH=$(get_property schema.name.oauth .persistence[0].schema.oauth)
        if [ -z "${SCHEMA_OAUTH}" ] || [ "null" = "${SCHEMA_OAUTH}" ]
        then
            SCHEMA_OAUTH="FHIR_OAUTH"
        fi
        SCHEMA_BATCH=$(get_property schema.name.batch .persistence[0].schema.batch)
        if [ -z "${SCHEMA_BATCH}" ] || [ "null" = "${SCHEMA_BATCH}" ]
        then
            SCHEMA_BATCH="FHIR_JBATCH"
        fi
        SCHEMA_FHIR=$(get_property schema.name.fhir .persistence[0].schema.fhir)
        if [ -z "${SCHEMA_FHIR}" ] || [ "null" = "${SCHEMA_FHIR}" ]
        then
            SCHEMA_FHIR="FHIRDATA"
        fi
        _call_postgres "--update-schema-fhir ${SCHEMA_FHIR} --update-schema-batch ${SCHEMA_BATCH} --update-schema-oauth ${SCHEMA_OAUTH} --pool-size 1"
    fi
}

# create_database_configuration_file - create the database configuration
function create_database_configuration_file { 
    process_cmd_properties $@
    process_cert
}

# check_connectivity - checks the following connectivity: 
# 1 - network CONNECTED
# 2 - Database Credentials
function check_connectivity {
    check_network_path
    check_database_credentials
}

# check_network_path - checks the network path, if this fails a network path is not open.
function check_network_path { 
    DB_HOSTNAME=$(get_property db.host .persistence[0].db.host)
    DB_PORT=$(get_property db.port .persistence[0].db.port)
    DB_SSL_PG=$(get_property ssl .persistence[0].db.ssl)

    # intentionally using ftp as it's generally available, and make no difference on this check
    info "Activating Network Path Connection..."

    # Since the pipe is used in a couple places where it can fail
    # we don't want it to kill this script, and have overriden (temporarily)
    # the fail on pipe and errexit, we'll control the exits.
    set +o errexit
    set +o pipefail
    CONNECTED=$(echo '' | nc ${DB_HOSTNAME} ${DB_PORT} -v 2>&1)
    if [ "$(echo $CONNECTED | grep -c Connected)" = "0" ]
    then 
        info "[NETWORK_PATH_CHECK] - FAILURE - Unable to connect to database - ${DB_HOSTNAME}:${DB_PORT}"
        exit 5;
    else 
        info "[NETWORK_PATH_CHECK] - SUCCESS - Connected to the database - ${DB_HOSTNAME}:${DB_PORT}"
    fi
    set -o errexit
    set -o pipefail
}

# check_database_credentials - checks the database credentials using the schema tool
function check_database_credentials { 
    DB_TYPE=$(get_property db.type .persistence[0].db.type)
    if [ "${DB_TYPE}" = "postgresql" ]
    then 
        DB_HOSTNAME=$(get_property db.host .persistence[0].db.host)
        DB_PORT=$(get_property db.port .persistence[0].db.port)
        DB_USER=$(get_property user .persistence[0].db.user)
        DB_PASSWORD=$(get_property password .persistence[0].db.password)
        DB_NAME=$(get_property db.database .persistence[0].db.database)

        DB_SSL_PG=$(get_property ssl .persistence[0].db.ssl)
        SSL_CONNECTION="false"
        if [ "$DB_SSL_PG" = "true" ]
        then
            SSL_CONNECTION="true"
            echo "${SSL_CONNECTION} is set"
        fi

        # TODO: implement a nice little check here that verifies the user,password can connect.
        debug "postgres doesn't have as easy a tool to use"
    fi
}

# onboard_behavior - creates and updates a schema and apply grants
function onboard_behavior {
    if [ "${TOOL_SKIP}" != "true" ]
    then
        info "Starting the onboard behavior"
        check_connectivity
        create_schema
        update_schema
        grant_to_dbuser
    else
        info "The onboarding deployment flow is skipped"
    fi
}

# offboard_behavior - Removes data when there are no tenants left.
function offboard_behavior {
    if [ "${TOOL_SKIP}" != "true" ]
    then
        info "Starting the offboard behavior"

        # All Databases
        drop_schema_fhir
        drop_schema_batch
        drop_schema_oauth
    else
        info "The offboarding deployment flow is skipped"
    fi
}

# custom_behavior - runs a custom action
function custom_behavior {
    if [ "${TOOL_SKIP}" != "true" ]
    then
        info "Starting the custom behavior"

        if [ "${DB_TYPE}" = "postgresql" ]
        then
            _call_postgres "$@"
        fi
    else
        info "The custom behavior flow is skipped"
    fi
}

# debug_behavior - runs the debug action 
function debug_behavior {
    info "The files included with the tool are:"
    ls -alh /opt/schematool
    echo "The OpenSSL version is:" 
    openssl version
}

# process_behavior - translates the identified behavior to lowercase
# the behavior is picked up from the existing variable
# 
# @implNote each behavior that the tool is expected to have must have a case statement.
function process_behavior {
    TOOL_BEHAVIOR=$(get_property tool.behavior .persistence[0].behavior | tr '[:upper:]' '[:lower:]')
    echo "The tool behavior being executed is $TOOL_BEHAVIOR"
    case $TOOL_BEHAVIOR in
    onboard)
        onboard_behavior
    ;;
    offboard)
        offboard_behavior
    ;;
    custom)
        custom_behavior "$@"
    ;;
    debug)
        debug_behavior
    ;;
    *)
        info "invalid behavior called, dropping through - '${TOOL_BEHAVIOR}'"
    ;;
    esac
}

##############################################################################
# The logic is activated in the following calls:

debug "Script Name is ${SCRIPT_NAME}"

create_database_configuration_file "$@"
process_behavior "$@"

# EOF
