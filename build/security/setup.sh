#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Create the artifacts and the docker image so that the layer can be compiled in 

# Only run if workspace is set
if [ -z "${WORKSPACE}" ]
then 
    echo 'Export Variable is not set "${WORKSPACE}"'
else
    pushd `pwd`
    cd ${WORKSPACE}
    echo "Starting the setup"

    # Creates the Temporary Folder to park intermediate results
    if [ -d build/security/logs/tmp ]
    then 
        rm -rf build/security/logs/tmp
    fi
    if [ -d build/security/logs/output ]
    then 
        rm -rf build/security/logs/output
    fi
    mkdir -p build/security/logs/tmp
    mkdir -p build/security/logs/output

    mvn clean install -f fhir-examples/ -DskipTests -B -ntp
    mvn clean install -f fhir-parent/ -DskipTests  -B -ntp
    docker build fhir-install -t ibmcom/ibm-fhir-server
    popd `pwd`
    echo "Finished the setup"
fi
# EOF