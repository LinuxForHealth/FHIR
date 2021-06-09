#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

# reindex_pre - executes for each reindex pre integration steps
reindex_pre(){
    reindex="${1}"
    if [ ! -z "${reindex}" ] && [ -f build/reindex/${reindex}/pre-integration-test.sh ]
    then 
        echo "Running [${reindex}] pre-integration-test"
        bash build/reindex/${reindex}/pre-integration-test.sh
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

# Change to the reindex_pre
cd "${WORKSPACE}"

reindex_pre "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################