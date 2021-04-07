#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# set_version - set version per latest build_id 
# Parameters: 
#   PROJECT
#   NEW_VERISON
# Reference https://www.mojohaus.org/versions-maven-plugin/set-mojo.html
# use versions:revert - option to revert the change. 

set_version "fhir-examples"
set_version "fhir-tools"
set_version "fhir-parent" 

mvn ${THREAD_COUNT} versions:set  -f ${PROJECT_PATH} -DoldVersion=${OLD_VERSION} -DnewVersion=${NEW_VERSION}
mvn ${THREAD_COUNT} versions:set  -f ${PROJECT_PATH} -DoldVersion=${OLD_VERSION} -DnewVersion=${NEW_VERSION}
mvn ${THREAD_COUNT} versions:set  -f ${PROJECT_PATH} -DoldVersion=${OLD_VERSION} -DnewVersion=${NEW_VERSION}

# Reconcile the versions. 
mvn org.codehaus.mojo:versions-maven-plugin:2.7:set-property -Dproperty=fhir-examples.version -DnewVersion=${BUILD_VERSION} -f fhir-parent
mvn org.codehaus.mojo:versions-maven-plugin:2.7:set-property -Dproperty=fhir-tools.version -DnewVersion=${BUILD_VERSION} -f fhir-parent

# EOF