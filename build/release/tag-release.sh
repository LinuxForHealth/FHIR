#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# tag functions include to get all

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

# tag_release - create a tag 
# Parameters: 
#   VERSION - MAJOR.MINOR.INCREMENTALVERSION
# Reference 
#   https://maven.apache.org/pom.html#Dependency_Version_Requirement_Specification
#   https://www.mojohaus.org/versions-maven-plugin/version-rules.html
#   https://cwiki.apache.org/confluence/display/MAVENOLD/Versioning
function tag_release { 
    announce "${FUNCNAME[0]}"

    VERSION="${1}"
    git tag "${VERSION}" -m "Releasing version ${VERSION} - ${BUILD_DATETIME}"
    git push --tags 
}

# build_type - set the global build type
function build_type { 
    TYPE="${1}"
    echo "Checking Type - ${1}"
    TAGS=`git tag --list`
}

###############################################################################

tag_release ${1}

# EOF