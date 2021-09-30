#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -e
set +x

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

    # 12.6.0 no longer exists.
    # com.azure:azure-storage-blob:12.13.0
    # com.azure:azure-core-http-okhttp:1.7.2
    # com.azure:azure-core:1.17.0
    sudo apt-get install -f libxml-xpath-perl
    AZURE_STORAGE_BLOB_VERSION=$(xpath -q -e  './project/dependencyManagement/dependencies/dependency[./groupId/text() = "com.azure" and ./artifactId/text() = "azure-storage-blob"]/version/text()' fhir-parent/pom.xml)
    if [ "${AZURE_STORAGE_BLOB_VERSION}" = "12.6.0" ]
    then
        mvn versions:use-dep-version -Dincludes=com.azure:azure-storage-blob -DdepVersion=12.13.0 -DforceVersion=true -f fhir-parent/pom.xml
        mvn versions:use-dep-version -Dincludes=com.azure:azure-core-http-okhttp -DdepVersion=1.7.2 -DforceVersion=true -f fhir-parent/pom.xml
        mvn versions:use-dep-version -Dincludes=com.azure:azure-core -DdepVersion=1.17.0 -DforceVersion=true -f fhir-parent/pom.xml
    fi

    mvn -T2C -B install --file fhir-parent -DskipTests -P include-fhir-igs,integration --no-transfer-progress

    # create and remove a 1 GB file to make sure we have the room needed later
    df -h
    dd if=/dev/urandom oflag=direct of=balloon.dat bs=1024k count=1000
    rm -f balloon.dat
    sudo apt clean
    docker system prune -f
    df -h

    docker version
}

# migration_build - executes for each migration type.
migration_build(){
    migration="${1}"
    if [ -f "build/migration/${migration}/1_previous-setup.sh" ]
    then
        echo "Running [${migration}] setting up prerequisites"
        bash build/migration/${migration}/1_previous-setup.sh
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
cd "${WORKSPACE}/prev"

required_build
migration_build "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################