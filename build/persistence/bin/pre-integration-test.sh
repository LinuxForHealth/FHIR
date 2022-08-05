#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# persistence_pre - executes for each persistence pre integration steps
persistence_pre(){
    PERSISTENCE="${1}"
    if [ ! -z "${PERSISTENCE}" ] && [ -f build/persistence/${PERSISTENCE}/pre-integration-test.sh ]
    then
        echo "Running [${PERSISTENCE}] pre-integration-test"
        bash build/persistence/${PERSISTENCE}/pre-integration-test.sh
    fi
}

###############################################################################
# Check if the workspace is set.
if [ -z "${WORKSPACE}" ]
then
    echo "The WORKSPACE value is unset"
    exit -1
fi

. ${WORKSPACE}/build/common/set-tenant1-datastore-vars.sh

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the persistence_pre
cd "${WORKSPACE}"

persistence_pre "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################
