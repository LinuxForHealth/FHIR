#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# regression for IBM Java 

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"
source "$(dirname '$0')/release.properties"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

# Reset to Original Directory
popd > /dev/null

###############################################################################
# function declarations:  

# install_for_regression - on jdks without creating yet another build. 
# - OpenJDK 8, OpenJDK 11 - from https://adoptopenjdk.net/.
#   'openjdk11' is the default in the travis and github actions matrix
# - IBM SDK, Java Technology Edition, Version 8. 8.0.5.40
#   JDKS_VERS='https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/baseline/baseline_version.txt'
#   JDKS_META='https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/meta/sdk/linux/x86_64/index.yml'
JDKS=('ibmjdk8:ibm' )
function install_for_regression { 
    info "Starting the REGRESSION tests on specified jdks"
    FILE='~/ibm-java/ibm-java-archive.bin'

    for JDK in "${JDKS[@]}"
    do 
        JDK_INTERIM=`echo $JDK | sed 's|:| |g' | awk '{print $1}'`
        JDK_TYPE=`echo $JDK | sed 's|:| |g' | awk '{print $2}'`
    
        if [[ "${JDK_TYPE}" = "default" ]]
        then 
            info "AdoptOpenJDK JDK -> ${JDK_INTERIM}"
            #jdk_switcher use $JDK_INTERIM
        elif [[ "${JDK_TYPE}" = "ibm" ]]
        then 
            info "IBM JDK -> ${JDK_INTERIM}"
            
            # Setup Repsonse.properties
            info "INSTALLER_UI=silent" > ~/response.properties
            info "USER_INSTALL_DIR=~/ibm-java/${JDK_ITERIM}" >> ~/response.properties
            info "LICENSE_ACCEPTED=TRUE" >> ~/response.properties
            
            chmod +x ${FILE}

            # setup directory and install 
            mkdir -p ~/ibm-java/java80
            ${FILE} -i silent -f ~/response.properties 
            
            export JAVA_HOME="~/ibm-java/${JDK_ITERIM}"
            export PATH="${JAVA_HOME}/bin:${PATH}"
        fi
        header_line
    done 
}

# downloads the jdk 
# - SHA_SUM - the sha_sum in the trusted variable that is passed in here. 
# - DOWNLOAD_URI - uri
#
# Example:
# 1.8.0_sr5fp40:
#    uri: https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/8.0.5.40/linux/x86_64/ibm-java-sdk-8.0-5.40-x86_64-archive.bin
#    sha256sum: bc53faf476655e565f965dab3db37f9258bfc16bb8c5352c93d43d53860b79d3
# Originally from https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/meta/sdk/linux/x86_64/index.yml 
function download {
    announce "${FUNCNAME[0]}"

    SHA_SUM="$1"
    DOWNLOAD_URI="$2"
    
    # Create the directory
    mkdir -p ~/ibm-java

    FILE='~/ibm-java/ibm-java-archive.bin'
    if [ -f "${FILE}" ]
    then 
        # check sha256sum in the cached file
        LOCAL_SHA_SUM="$(sha256sum ${FILE} | awk '{print $1}')"
        info "ACTUAL_SHA_SUM -> ${LOCAL_SHA_SUM}"
        info "EXPECTED_SHA_SUM -> ${SHA_SUM}"
        if [ "${SHA_SUM}" != "${LOCAL_SHA_SUM}" ]
        then
            curl -o ${FILE} "${DOWNLOAD_URI}"
        fi

        # make sure it's executable. 
        chmod +x ${FILE}
    else 
        curl -o ${FILE} "${DOWNLOAD_URI}"
    fi 
}

download
install_for_regression
regression

mvn verify -f fhir-parent

# EOF