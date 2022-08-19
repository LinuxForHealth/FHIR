#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# https://central.sonatype.org/pages/releasing-the-deployment.html
# https://central.sonatype.org/publish/publish-maven/

# We've opted not to deploy fhir-tools 

# The relevant keys for this signature are published, and can be confirmed at 
# http://keys.gnupg.net/pks/lookup?search=fhir&fingerprint=on&op=index
# with gpg:sign

# fhir-examples
# Reconcile the versions.
FHIR_PARENT_VERSION=$(cat fhir-parent/pom.xml | xpath -q -e project/properties/fhir-examples.version/text\(\))
FHIR_EXAMPLES_VERSION=$(cat fhir-examples/pom.xml | xpath -q -e project/version/text\(\))
if [[ "${FHIR_PARENT_VERSION}" == "${FHIR_EXAMPLES_VERSION}" ]]
then
    export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-examples").profiles | map(.) | join(",")' build/release/config/release.json)"
    export BUILD_PROFILES="${BUILD_PROFILES},deploy-to-sonatype"
    mvn -T2C source:jar source:test-jar javadoc:jar gpg:sign deploy \
        -DadditionalJOption=-Xdoclint:none \
        -f fhir-examples -P "${BUILD_PROFILES}" -DskipTests
else 
    echo "Skipping fhir-examples"
fi

# fhir-parent
# we hit 'gpg: signing failed: Cannot allocate memory' when running -T2C
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-parent").profiles | map(.) | join(",")' build/release/config/release.json)"
export BUILD_PROFILES="${BUILD_PROFILES},deploy-to-sonatype"
mvn package source:jar source:test-jar javadoc:jar gpg:sign deploy \
    -DadditionalJOption=-Xdoclint:none \
    -DretryFailedDeploymentCount=3 \
    -f fhir-parent -P "${BUILD_PROFILES}" -DskipTests

# EOF