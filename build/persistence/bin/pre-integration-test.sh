#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# persistence_pre - executes for each persistence pre integration steps
persistence_pre(){
    PERSISTENCE="${1}"
    if [ -z "${PERSISTENCE}" && -f ../${PERSISTENCE}/pre-integration-test.sh ]
    then 
        # 
        echo "Running [${PERSISTENCE}] pre-integration-test"
        bash ../${PERSISTENCE}/pre-integration-test.sh
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the persistence_pre
cd "$(dirname ${BASH_SOURCE[0]})"

persistence_pre ${1}

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################