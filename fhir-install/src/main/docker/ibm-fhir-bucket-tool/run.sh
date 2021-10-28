#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

set -e -o pipefail

/opt/java/openjdk/bin/java -jar /opt/ibm/fhir/bucket/fhir-bucket-*-cli.jar "$@"
# EOF
