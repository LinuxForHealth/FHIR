#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################


# If Build is for 

${BUILD_ID}
GIT_COMMIT
GIT_BRANCH
GIT_URL
FHIR_GPG_PASSPHRASE
FHIR_GPG_KEYNAME
GPG_PASSPHRASE
GPG_KEYNAME

# Check the BUILD_PASSWORD is set
if [ ! -z "${BUILD_PASSWORD}" ]
then 
    info "BUILD PASSWORD is set!"
else 
    warn "BUILD PASSWORD is NOT set!"
fi

# Check the BUILD_PASSWORD is set
if [ ! -z "${BUILD_USERNAME}" ]
then 
    info "BUILD USERNAME is set!"
else 
    warn "BUILD USERNAME is NOT set!"
fi

JAVA_HOME



# Set the global Maven options
build/release/bin/00_prep/1_set_maven_opts.sh


BUILD_NEW_VERSION
BUILD_VERSION

#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

###############################################################################
# Function Declaration

# set_build_variable - take 3 parameters 
# 1 - the variable name 
# 2 - the default value
# 3 - the preferred, if not empty, value
function set_build_variable {
    if [ ! -z "${3}" ] # check to see if the variable is already not empty.
    then 
        if [ ! -z "${3}" ]
        then 
            outputEnvironment "${1}=${3}"
        else
            outputEnvironment "${1}=${2}"
        fi 
        debugging "Environment Variable is set [${1}]"
    else 
        debugging "Environment Variable is already set [${1}]"
    fi
}

# set_build_type - sets the type of the build and the build id
# 1 - tag 
# 2 - github_ref
# 3 - github_sha 
function set_build_type {
    if [ "${BUILD_ID}" == *"Integration"* ] || [ -z "${BUILD_ID}" ]
    then
        if [ "${1}-" != "---" ]
        then 
            # not empty and is tag build
            if [[ "${1}" == *"RC"* ]]
            then
                outputEnvironment "export BUILD_TYPE=\"RELEASE_CANDIDATE\""
                info "Build_Type is RELEASE_CANDIDATE"
            else
                outputEnvironment "export BUILD_TYPE=\"RELEASE\"" 
                info "Build_Type is RELEASE"
            fi
            set_build_variable BUILD_ID "" "${1}"
        fi
    else 
        # SNAPSHOT build
        set_build_variable BUILD_ID "" "Integration_${2}_${3}"
        outputEnvironment "export BUILD_TYPE=\"SNAPSHOT\"" 
    fi
}

function outputEnvironment { 
    echo "export ${1}" >> bootstrap.env
}

###############################################################################
# Setup Git - makes sure there is a gitignore which puts the log in this folder. 
debugging "Current directory is $(pwd)"

###############################################################################
# wipe out existing bootstrap.env files 
if [ -f bootstrap.env ]
then 
    rm -f bootstrap.env
fi

###############################################################################
# Setup Default Variables
set_build_variable BUILD_VERSION "" "${TRAVIS_TAG}"
set_build_variable BUILD_VERSION "development" "$(echo ${GITHUB_REF} | sed 's|refs/tags/||g')"

set_build_variable GIT_BRANCH "${TRAVIS_PULL_REQUEST_BRANCH}" "${TRAVIS_BRANCH}"
set_build_variable GIT_BRANCH "${GITHUB_REF}" "${GITHUB_REF}"

set_build_variable GIT_COMMIT "${GITHUB_SHA}" "${GITHUB_SHA}"
set_build_variable GIT_COMMIT "${TRAVIS_COMMIT}" "${TRAVIS_COMMIT}"

# Embedded in the 
set_build_variable GIT_URL "" ${TRAVIS_REPO_SLUG}
set_build_variable GIT_URL "development" "${GITHUB_REPOSITORY}"

# Create Build ID - Check if integration build
# sets BUILD_TYPE and BUILD_ID (naturally one of these cannot be empty to hold true.)
# - Check type for Travis build
if [ ! -z "${TRAVIS_TAG}" ]
then
    set_build_type "-${TRAVIS_TAG}-" "${GITHUB_REF}" "${GITHUB_SHA}"
fi 


# - Check type for GitHub Actions build
set_build_type "-${TRAVIS_TAG}-" "${TRAVIS_BUILD_NUMBER}" "${TRAVIS_BRANCH}"
if [[ "$(echo ${GITHUB_REF} | sed 's|refs/tags/||g')" == *"RC"* ]]
then
    outputEnvironment "export BUILD_TYPE=\"RELEASE_CANDIDATE\""
    info "Build_Type is RELEASE_CANDIDATE"
    set_build_variable BUILD_ID "" "$(echo ${GITHUB_REF} | sed 's|refs/tags/||g')"
elif [ ! -z "$(echo ${GITHUB_REF} | sed 's|refs/tags/||g')" ]
then
    outputEnvironment "export BUILD_TYPE=\"RELEASE\"" 
    info "Build_Type is RELEASE"
    set_build_variable BUILD_ID "" "$(echo ${GITHUB_REF} | sed 's|refs/tags/||g')"
fi

# - Check type for LOCAL build
set_build_type "-${FHIR_GIT_TAG}-" "${FHIR_GIT_BUILD_NUMBER}" "${FHIR_GIT_BRANCH}"

echo "export BUILD_DISPLAY_NAME=\"${BUILD_ID}\"" >> bootstrap.env




# Reset to Original Directory
popd > /dev/null

# EOF

# EOF