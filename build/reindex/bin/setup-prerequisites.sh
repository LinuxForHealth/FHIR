#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

# required_build - executes for every build
required_build(){
    # Clean up the packages and docker files not on the Mac
    if [[ "$OSTYPE" != "darwin"* ]]
    then
        sudo apt clean
        docker rmi $(docker image ls -aq)
        df -h
    fi

    # build binaries
    mvn -T2C -B install --file fhir-examples --no-transfer-progress
    mvn -T2C -B install --file fhir-parent -DskipTests -P include-fhir-igs,integration --no-transfer-progress

    # create and remove a 1 GB file to make sure we have the room needed later
    df -h
    dd if=/dev/urandom oflag=direct of=balloon.dat bs=1024k count=1000
    rm -f balloon.dat
    sudo apt clean
    docker system prune -f
    df -h

    docker version

    # Build dockerfile
    cd fhir-install
    docker build -t ibmcom/ibm-fhir-server:latest .
    cd ..
}

# reindex_build - executes for each reindex type.
reindex_build(){
    reindex="${1}"
    if [ -f "build/reindex/${reindex}/setup-prerequisites.sh" ]
    then
        echo "Running [${reindex}] setting setup prerequisites"
        bash build/reindex/${reindex}/setup-prerequisites.sh
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

if [ -z "${WORKSPACE}" ]
then 
    echo "The WORKSPACE value is unset"
    exit -1
fi 

# Change to the release directory
cd "${WORKSPACE}"

required_build
reindex_build "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################