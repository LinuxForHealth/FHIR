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

mvn versions:set  -f "fhir-examples" -DoldVersion="*" -DnewVersion="${BUILD_VERSION}"
mvn versions:set  -f "fhir-tools" -DoldVersion="*" -DnewVersion="${BUILD_VERSION}"
mvn versions:set  -f "fhir-parent"  -DoldVersion="*" -DnewVersion="${BUILD_VERSION}"

# Reconcile the versions.
FHIR_PARENT_VERSION=$(cat fhir-parent/pom.xml | xpath -q -e project/properties/fhir-examples.version/text\(\))
FHIR_EXAMPLES_VERSION=$(cat fhir-examples/pom.xml | xpath -q -e project/version/text\(\))
if [[ "${FHIR_PARENT_VERSION}" == "${FHIR_EXAMPLES_VERSION}" ]]
then
    mvn org.codehaus.mojo:versions-maven-plugin:2.7:set-property -Dproperty=fhir-examples.version -DnewVersion="${BUILD_VERSION}" -f fhir-parent
fi
mvn org.codehaus.mojo:versions-maven-plugin:2.7:set-property -Dproperty=fhir-tools.version -DnewVersion="${BUILD_VERSION}" -f fhir-parent

# EOF