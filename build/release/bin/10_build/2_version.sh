#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# set version per latest build_id 
# Parameters: 
#   PROJECT
#   NEW_VERISON
# Reference https://www.mojohaus.org/versions-maven-plugin/set-mojo.html
# use versions:revert - option to revert the change. 

mvn versions:set  -f "fhir-examples" -DoldVersion=${BUILD_VERSION} -DnewVersion=${BUILD_NEW_VERSION}
mvn versions:set  -f "fhir-tools" -DoldVersion=${BUILD_VERSION} -DnewVersion=${BUILD_NEW_VERSION}
mvn versions:set  -f "fhir-parent"  -DoldVersion=${BUILD_VERSION} -DnewVersion=${BUILD_NEW_VERSION}

# Reconcile the versions. 
mvn org.codehaus.mojo:versions-maven-plugin:2.7:set-property -Dproperty=fhir-examples.version -DnewVersion=${BUILD_NEW_VERSION} -f fhir-parent
mvn org.codehaus.mojo:versions-maven-plugin:2.7:set-property -Dproperty=fhir-tools.version -DnewVersion=${BUILD_NEW_VERSION} -f fhir-parent

# EOF