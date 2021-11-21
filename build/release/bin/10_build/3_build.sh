#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Runs the build with all the preconfigured profiles

# fhir-tools
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-tools").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn install source:jar source:test-jar javadoc:jar -f fhir-tools \
        -DadditionalJOption=-Xdoclint:none \
        -f fhir-tools -P "${BUILD_PROFILES}" -DskipTests

# fhir-examples
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-examples").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn install source:jar source:test-jar javadoc:jar -f fhir-examples \
        -DadditionalJOption=-Xdoclint:none \
        -f fhir-examples -P "${BUILD_PROFILES}" -DskipTests

# fhir-parent
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-parent").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn install -f fhir-parent -DskipTests
mvn install source:jar source:test-jar javadoc:jar -f fhir-parent \
        -DadditionalJOption=-Xdoclint:none \
        -f fhir-parent -P "${BUILD_PROFILES}" -DskipTests

# EOF
