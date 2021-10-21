#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# verify and generate code coverage jacoco.exec and xml files

# fhir-tools
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-tools").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C test org.jacoco:jacoco-maven-plugin:0.8.6:report-aggregate -f fhir-tools

# fhir-examples
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-examples").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C test org.jacoco:jacoco-maven-plugin:0.8.6:report-aggregate -f fhir-examples

# fhir-parent
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-parent").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C test org.jacoco:jacoco-maven-plugin:0.8.6:report-aggregate -f fhir-parent -P "${BUILD_PROFILES}"

# EOF
