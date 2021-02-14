#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# notifications_post - executes for each notifications post integration steps
notifications_post(){
    notifications="${1}"
    if [ ! -z "${notifications}" ] && [ -f build/notifications/${notifications}/post-integration-test.sh ]
    then 
        echo "Running [${notifications}] post-integration-test"
        bash build/notifications/${notifications}/post-integration-test.sh
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

cd "${WORKSPACE}"

notifications_post ${1}

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################