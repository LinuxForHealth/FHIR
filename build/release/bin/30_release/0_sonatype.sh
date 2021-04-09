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

# fhir-examples
export BUILD_PROFILES="deploy-to-sonatype"
mvn -T2C deploy -f fhir-examples -P "${BUILD_PROFILES}" -DskipTests

# fhir-parent
export BUILD_PROFILES="deploy-to-sonatype"
mvn -T2C deploy -f fhir-parent -P "${BUILD_PROFILES}" -DskipTests

# EOF