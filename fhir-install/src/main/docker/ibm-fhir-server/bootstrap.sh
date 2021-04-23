#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

##############################################################################
# Description:
# This script is the entrypoint used by the ibm-fhir-server docker image, and
# optionally bootstraps a derby database prior to running the IBM FHIR Server.

set -e -o pipefail

##############################################################################
# The global variables used are:

SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"

CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

FHIR_PERSISTENCE_SCHEMA_CLI_LOCATION="/opt/ibm-fhir-server/tools"

PERFORM_BOOTSTRAP_DB=${BOOTSTRAP_DB}
[ -z "${BOOTSTRAP_DB}" ] && PERFORM_BOOTSTRAP_DB="false"

##############################################################################
# Helper Functions

# info - - local function to echo info message
# ARGUMENTS:
#   String of message
function info {
    echo "${SCRIPT_NAME} - [INFO]: $(date +"%Y-%m-%d_%T") - ${1}"
}

# _call_derby_db - local function to call derby database
# ARGUMENTS:
#   String of additional parameters
function _call_derby_db {
    /opt/java/openjdk/bin/java -jar ${FHIR_PERSISTENCE_SCHEMA_CLI_LOCATION}/fhir-persistence-schema-*-cli.jar \
        --prop "db.create=Y" \
        --prop "db.database=/output/derby/fhirDB" \
        --db-type derby \
        ${1} 2>&1
}

# _bootstrap_db - local function to perform database bootstrapping
function _bootstrap_db {
    if [ "$PERFORM_BOOTSTRAP_DB" = "true" ]
    then
        info "Performing Derby database bootstrapping"
        _call_derby_db "--update-schema"
        info "Finished Derby database bootstrapping"
    else
        info "Skipping Derby database bootstrapping"
    fi
}

##############################################################################
# Script logic:

info "Current directory: $CUR_DIR"
_bootstrap_db

# Pass it on to the Liberty entrypoint
/opt/ol/helpers/runtime/docker-server.sh "$@"

# EOF
