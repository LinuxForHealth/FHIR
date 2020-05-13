#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# persistence_post - executes for each persistence post integration steps
persistence_post(){
    PERSISTENCE="${1}"
    if [ -z "${PERSISTENCE}" && -f ../${PERSISTENCE}/post-integration-test.sh ]
    then 
        # 
        echo "Running [${PERSISTENCE}] post-integration-test"
        bash ../${PERSISTENCE}/post-integration-test.sh
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the persistence_post
cd "$(dirname ${BASH_SOURCE[0]})"

persistence_post ${1}

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################