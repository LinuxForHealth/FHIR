#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# https://central.sonatype.org/pages/releasing-the-deployment.html
# https://central.sonatype.org/publish/publish-maven/

# fhir-examples
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-examples").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C deploy -f fhir-examples -P "${BUILD_PROFILES}" -DskipTests

# fhir-tools
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-tools").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C deploy -f fhir-tools -P "${BUILD_PROFILES}" -DskipTests

# fhir-parent
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-parent").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C deploy -f fhir-parent -P "${BUILD_PROFILES}" -DskipTests

# EOF