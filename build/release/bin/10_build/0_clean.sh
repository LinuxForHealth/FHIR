#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# cleans the given projects
# Reference https://maven.apache.org/plugins/maven-clean-plugin/

mvn clean -f fhir-examples
mvn clean -f fhir-tools
mvn clean -f fhir-parent

# EOF