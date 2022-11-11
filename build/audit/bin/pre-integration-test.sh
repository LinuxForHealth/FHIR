#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# audit_pre - executes for each audit pre integration steps
audit_pre(){
    AUDIT="${1}"
    if [ ! -z "${AUDIT}" ] && [ -f build/audit/${AUDIT}/pre-integration-test.sh ]
    then 
        echo "Running [${AUDIT}] pre-integration-test"
        bash build/audit/${AUDIT}/pre-integration-test.sh
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

# Change to the audit_pre
cd "${WORKSPACE}"

# Source the tenant1 datastore variables
. ${WORKSPACE}/build/common/set-tenant1-datastore-vars.sh

audit_pre "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################