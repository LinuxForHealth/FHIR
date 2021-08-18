#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# deploys the binaries to HTTPS endpoint

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

# Creates a temporary output file
OUTPUT_FILE=`mktemp`

# Reset to Original Directory
popd > /dev/null

###############################################################################
# Function Declarations:

# upload_to_https - uploads to HTTPS
function upload_to_https {
    TYPE="releases"
    MODULE="${1}"
    FILE="${2}"
    FILE_TARGET_PATH="${3}"

    # output with a new line
    echo " "
    echo "Uploading: [${MODULE}][${FILE}]"
    
    STATUS=$(curl -T "${FILE}" -u${HTTPS_USERNAME}:${HTTPS_PASSWORD} -H "X-HTTPS-Package:${MODULE}" -H "X-HTTPS-Version:${BUILD_VERSION}" https://api.HTTPS.com/content/ibm-watson-health/ibm-fhir-server-${TYPE}${FILE_TARGET_PATH} -o ${OUTPUT_FILE} -w '%{http_code}')
    if [ "${STATUS}" -ne "201" ]
    then 
        echo "Debug Information for Upload Failure" 
        cat ${OUTPUT_FILE}
    fi
    
    if [ "${STATUS}" == "413" ]
    then
        # File is too big (over 300M)
        exit -413
    fi
    echo "[${STATUS}] - Done uploading jar file to ${FILE_TARGET_PATH}"
}

# deploy_via_curl - uploads each artifact via curl
function deploy_via_curl {
    TYPE="${1}"
    for PROJ in `find . -type d -maxdepth 3 -name 'target' | sed 's|\.\/||g' | grep -v '\.' `
    do
        MODULE_DIRECTORY=`dirname ${PROJ}`
        MODULE=`basename ${MODULE_DIRECTORY}`
        echo "Processing [${PROJ}] files"
        # Upload Project File
        POM_FILE="${MODULE_DIRECTORY}/pom.xml"
        if [ -f ${POM_FILE} ]
        then
            FILE="${POM_FILE}"
            FILE_TARGET_PATH="/com/ibm/fhir/${MODULE}/${BUILD_VERSION}/${MODULE}-${BUILD_VERSION}.pom"
            upload_to_https "${MODULE}" "${FILE}" "${FILE_TARGET_PATH}"
        fi

        # Sources
        for SOURCES_JAR in `find ${PROJ} -iname "*-sources.jar" -maxdepth 1 -exec basename {} \;`
        do
            FILE="${PROJ}/${SOURCES_JAR}"
            FILE_TARGET_PATH="/com/ibm/fhir/${MODULE}/${BUILD_VERSION}/${MODULE}-${BUILD_VERSION}-sources.jar"
            upload_to_https "${MODULE}" "${FILE}" "${FILE_TARGET_PATH}"
        done 

        # JavaDoc
        for JAVADOC_JAR in `find ${PROJ} -iname "*${BUILD_VERSION}-javadoc.jar" -maxdepth 1 -exec basename {} \;`
        do
            FILE="${PROJ}/${JAVADOC_JAR}"
            FILE_TARGET_PATH="/com/ibm/fhir/${MODULE}/${BUILD_VERSION}/${MODULE}-${BUILD_VERSION}-javadoc.jar"
            upload_to_https "${MODULE}" "${FILE}" "${FILE_TARGET_PATH}"
        done

        # Tests Jar
        for TESTS_JAR in `find ${PROJ} -iname "*${BUILD_VERSION}-tests.jar" -maxdepth 1 -exec basename {} \;`
        do
            FILE="${PROJ}/${TESTS_JAR}"
            FILE_TARGET_PATH="/com/ibm/fhir/${MODULE}/${BUILD_VERSION}/${MODULE}-${BUILD_VERSION}-tests.jar"
            upload_to_https "${MODULE}" "${FILE}" "${FILE_TARGET_PATH}"
        done

        # The following files have potentials for MULTIPLE matching files. 
        # Jar
        for JAR in `find ${PROJ} -maxdepth 1 -not -name '*-tests.jar' -and -not -name '*-javadoc.jar' -and -not -name '*-sources.jar' -and -not -name '*orginal*.jar' -and -name '*.jar' -exec basename {} \;`
        do
            FILE="${PROJ}/${JAR}"
            FILE_TARGET_PATH="/com/ibm/fhir/${MODULE}/${BUILD_VERSION}/${JAR}"
            upload_to_https "${MODULE}" "${FILE}" "${FILE_TARGET_PATH}"
        done

        echo "Finished Upload for [${MODULE}]"
    done

    deploy_zip_files 
}

# deploy_zip_files - uploads the release specific zip files. 
# --- don't add files that could be greater than 300M
function deploy_zip_files { 
    FILE=fhir-validation/target/fhir-validation-distribution.zip
    MODULE=fhir-validation
    FILE_TARGET_PATH="/com/ibm/fhir/${MODULE}/${BUILD_VERSION}/fhir-validation-distribution.zip"
    upload_to_https "${MODULE}" "${FILE}" "${FILE_TARGET_PATH}"
}

###############################################################################
# check to see if mvn exists
if which mvn | grep -i mvn
then
    debugging 'mvn is found!'
else
    warn 'mvn is not found!'
fi

#RELEASE_CANDIDATE or RELEASE or SNAPSHOT or EXISTING
case $BUILD_TYPE in
    RELEASE_CANDIDATE)
        TYPE="snapshots"
        deploy_via_curl "${TYPE}"
        header_line
    ;;
    RELEASE)
        TYPE="releases"
        deploy_via_curl "${TYPE}"
        header_line
    ;;
    SNAPSHOT)
        info "SNAPSHOT build is not set"
        header_line
    ;;
    EXISTING)
        info "EXISTING build is not set"
        header_line
    ;;
    *)
        warn "invalid function called, dropping through "
    ;;
esac

# EOF
