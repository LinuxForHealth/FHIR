#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# required_build - executes for every build
required_build(){
    # Clean up the packages and docker files
    sudo apt clean
    docker rmi $(docker image ls -aq)
    df -h

    # build binaries
    mvn -B install --file fhir-examples --no-transfer-progress
    mvn -B install --file fhir-parent -DskipTests -P integration --no-transfer-progress

    # Build dockerfile
    mvn -B dockerfile:build -f fhir-install --no-transfer-progress
}

# persistence_build - executes for each persistence build triggering the persistence layer's required steps.
persistence_build(){
    PERSISTENCE="${1}"
    if [ -z "${PERSISTENCE}" && -f ../${PERSISTENCE}/setup-prerequisites.sh ]
    then 
        # 
        echo "Running [${PERSISTENCE}] setting setup prerequisites"
        bash ../${PERSISTENCE}/setup-prerequisites.sh
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

required_build
persistence_build ${1}

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################