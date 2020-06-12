#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# persistence_post - executes for each persistence post integration steps
persistence_post(){
    PERSISTENCE="${1}"
    if [ ! -z "${PERSISTENCE}" ] && [ -f build/persistence/${PERSISTENCE}/post-integration-test.sh ]
    then 
        echo "Running [${PERSISTENCE}] post-integration-test"
        bash build/persistence/${PERSISTENCE}/post-integration-test.sh
    fi
}

###############################################################################
# Check if the workspace is set.
if [ -z "${WORKSPACE}" ]
then 
    echo "The WORKSPACE value is unset"
    exit -1
fi 

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the persistence_post
cd "${WORKSPACE}"

persistence_post ${1}

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################