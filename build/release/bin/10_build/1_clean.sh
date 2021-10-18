#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# cleans the given projects
# Reference https://maven.apache.org/plugins/maven-clean-plugin/

mvn -T2C clean -f fhir-tools
mvn -T2C clean -f fhir-examples
mvn -T2C clean -f fhir-parent

# EOF
