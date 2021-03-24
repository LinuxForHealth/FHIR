#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# cleans the given projects

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
# Functions

# build_clean - Build source from a Project PATH
# Reference https://maven.apache.org/plugins/maven-clean-plugin/
function build_clean { 
    announce "${FUNCNAME[0]}"

    PROJECT_PATH="$1"

    mvn ${THREAD_COUNT} clean -ntp -B -f ${PROJECT_PATH} -Dmaven.wagon.http.retryHandler.count=3
    check_and_fail $? "build_clean - finish"
}

# clean_all_projects - clean all versions 
function clean_all_projects { 
    build_clean "fhir-examples"
    build_clean "fhir-tools"
    build_clean "fhir-parent" 
}

###############################################################################
# check to see if mvn exists
if which mvn | grep -i mvn
then 
    debugging 'mvn is found!'
else 
    warn 'mvn is not found!'
fi

clean_all_projects

# EOF