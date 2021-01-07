#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# audit_post - executes for each AUDIT post integration steps
audit_post(){
    AUDIT="${1}"
    if [ ! -z "${AUDIT}" ] && [ -f build/audit/${AUDIT}/post-integration-test.sh ]
    then 
        echo "Running [${AUDIT}] post-integration-test"
        bash build/audit/${AUDIT}/post-integration-test.sh
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

audit_post ${1}

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################