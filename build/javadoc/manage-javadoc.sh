#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# The purpose of this script is to setup the JAVA_HOME (no matter what, and then optionally)
# Manage Existing JavaDocs 

# Check for JAVA_HOME and it must be set. 
if [ -z "${JAVA_HOME}" ] 
then 
    # The next command is really designed for Linux images to more quickly find the JAVA_HOME
    export JAVA_HOME=$(readlink $(which java)/../..)
fi 
echo "JAVA_HOME: ${JAVA_HOME}"

# Create a gitignored cache location. 
mkdir -p build/javadoc/.cache/

# RELEASE CANDIDATE
# MUST NOT be TAG_RELEASE
# If this is an RC Build... drop into latest only 
if [[ $TRAVIS_EVENT_TYPE == "push" && $TRAVIS_TAG == "" ]]
then 
    echo "Executing a RELEASE CANDIDATE build"
    # Find fhir-apidocs
    echo "CANDIDATE" > JAVADOC_STATUS 
fi

# RELEASE 
# TRAVIS_TAG must be set. 
# If this is a release, drop into the RELEASE 
# and drop into latest only 
if [[ $TRAVIS_EVENT_TYPE == "push" && $TRAVIS_TAG != "" ]]
then 
    echo "Executing a RELEASE build"

    echo "RELEASE" > JAVADOC_STATUS
fi

# EOF 