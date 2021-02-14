#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# notifications_pre - executes for each notifications pre integration steps
notifications_pre(){
    notifications="${1}"
    if [ ! -z "${notifications}" ] && [ -f build/notifications/${notifications}/pre-integration-test.sh ]
    then 
        echo "Running [${notifications}] pre-integration-test"
        bash build/notifications/${notifications}/pre-integration-test.sh
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

# Change to the notifications_pre
cd "${WORKSPACE}"

notifications_pre "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################