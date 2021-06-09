#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

# reindex_post - executes for each reindex post integration steps
reindex_post(){
    reindex="${1}"
    if [ ! -z "${reindex}" ] && [ -f build/reindex/${reindex}/post-integration-test.sh ]
    then 
        echo "Running [${reindex}] post-integration-test"
        bash build/reindex/${reindex}/post-integration-test.sh
    else
        cd build/reindex/${reindex}
        docker-compose down --remove-orphans --rmi local -v --timeout 30
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

reindex_post ${1}

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################