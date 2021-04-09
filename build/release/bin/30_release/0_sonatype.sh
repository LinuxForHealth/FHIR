#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# https://central.sonatype.org/pages/releasing-the-deployment.html
# https://central.sonatype.org/publish/publish-maven/
mvn deploy -f fhir-examples -DskipTests
mvn deploy -f fhir-tools -DskipTests
mvn deploy -f fhir-parent -DskipTests

# EOF