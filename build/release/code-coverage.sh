#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# verify and generate code coverage jacoco.exec and xml files

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"
source "$(dirname '$0')/release.properties"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

# Reset to Original Directory
popd > /dev/null

###############################################################################
# Function Declarations:

# code_coverage - executes mvn with a set of goals
function code_coverage { 
    announce "${FUNCNAME[0]}"
    PROJECT_PATH="$1"
    PROFILES="$2"

    # Batch mode without the transfer updates.
    mvn ${THREAD_COUNT} -ntp -B "${PROFILES}" test jacoco:report-aggregate -f ${PROJECT_PATH} -Dmaven.wagon.http.retryHandler.count=3
    check_and_fail $? "${FUNCNAME[0]} - stopped - ${PROJECT_PATH}"
}

# upload_to_codecov - uploads to codecov.io
function upload_to_codecov { 
    announce "${FUNCNAME[0]}"
    TOKEN="$1"
    cd fhir-install/target
    curl -s https://codecov.io/bash -o codecov.sh -U "--http1.1"
    bash codecov.sh -t "${TOKEN}" -f '!*.json'
}

###############################################################################
# check to see if mvn exists
if which mvn | grep -i mvn
then 
    debugging 'mvn is found!'
else 
    warn 'mvn is not found!'
fi

# build up the list of test profiles to execute and call code coverage
PROFILES_ARR=(model-all-tests)
PROFILES_ARR+=(validation-all-tests)
PROFILES_ARR+=(search-all-tests)
PROFILES_ARR+=(jdbc-all-tests)
PROFILES_ARR+=(aggregate-report) # this one is required to aggregate all of the dependencies
PROFILES=$(IFS=, ; echo "${PROFILES_ARR[*]}")
code_coverage 'fhir-parent' "-P${PROFILES}"

# Uploads to CodeCov
if [ ! -z "${CODECOV_RUNME}" ]
then 
    upload_to_codecov "${CODECOV_TOKEN}"
fi

# EOF