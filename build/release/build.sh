#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# build binaries - jar, source and javadoc-jar

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

# _mvn - executes mvn with a set goals
# Reference https://maven.apache.org/plugins/maven-javadoc-plugin/
# Reference https://maven.apache.org/plugins/maven-source-plugin/
function _mvn { 
    announce "${FUNCNAME[0]}"
    PROJECT_PATH="$1"
    PROFILES="$2"

    # Batch mode without the transfer updates.
    mvn ${THREAD_COUNT} -ntp -B "${PROFILES}" source:jar source:test-jar javadoc:jar install \
        -DadditionalJOption=-Xdoclint:none -f ${PROJECT_PATH}

    check_and_fail $? "${FUNCNAME[0]} - stopped - ${PROJECT_PATH}"
}

# _mvn2 - executes mvn to prep the install
function _mvn2 { 
    announce "${FUNCNAME[0]}"
    PROJECT_PATH="$1"
    PROFILES="$2"

    # Batch mode without the transfer updates.
    mvn ${THREAD_COUNT} -ntp -B "${PROFILES}" install -DskipTests -f ${PROJECT_PATH}
    check_and_fail $? "${FUNCNAME[0]} - stopped - ${PROJECT_PATH}"
}

# build_all - build all versions 
function build_all { 
    _mvn 'fhir-tools' '-Pdeploy-bintray,fhir-javadocs'
    _mvn 'fhir-examples' '-Pdeploy-bintray,fhir-javadocs'
    _mvn2 'fhir-parent' '-Pdeploy-bintray'

    PROFILES_ARR=(integration)
    PROFILES_ARR+=(model-all-tests)
    PROFILES_ARR+=(validation-all-tests)
    PROFILES_ARR+=(search-all-tests)
    PROFILES_ARR+=(jdbc-all-tests)
    PROFILES=$(IFS=, ; echo "${PROFILES_ARR[*]}")
    _mvn 'fhir-parent' "-Pdeploy-bintray,fhir-javadocs,fhir-validation-distribution,fhir-ig-carin-bb,fhir-ig-davinci-pdex-plan-net,fhir-ig-mcode,fhir-ig-us-core,fhir-term,${PROFILES}"
}

###############################################################################
# check to see if mvn exists
if which mvn | grep -i mvn
then 
    debugging 'mvn is found!'
else 
    warn 'mvn is not found!'
fi

build_all

# EOF