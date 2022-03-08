#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# required_build - executes for every build
required_build(){
    # Clean up the packages and docker files not on the Mac
    if [[ "$OSTYPE" != "darwin"* ]]
    then
        sudo apt clean
        docker rmi $(docker image ls -aq)
        df -h
    fi

    # build binaries
    mvn -B install --file fhir-examples --no-transfer-progress
    mvn -B install --file fhir-parent -DskipTests -P include-fhir-igs,integration --no-transfer-progress

    # Build dockerfile
    mvn -B dockerfile:build -f fhir-install --no-transfer-progress
}

# notifications_build - executes for each notifications type.
notifications_build(){
    notifications="${1}"
    if [ -f "build/notifications/${notifications}/setup-prerequisites.sh" ]
    then
        echo "Running [${notifications}] setting setup prerequisites"
        bash build/notifications/${notifications}/setup-prerequisites.sh
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

if [ -z "${WORKSPACE}" ]
then
    echo "The WORKSPACE value is unset"
    exit -1
fi

# Change to the release directory
cd "${WORKSPACE}"
. ${WORKSPACE}/build/common/set_tenant1_datastore_vars.sh

required_build
notifications_build "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################
