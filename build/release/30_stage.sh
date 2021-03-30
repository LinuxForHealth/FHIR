#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

echo "::group::Preparing the Build"

fhir-persistence-schema/target/fhir-persistence-schema-${{ github.workspace }}-cli.jar
          fhir-swagger-generator/target/fhir-swagger-generator-${{ github.workspace }}-cli.jar
          fhir-path/target/fhir-path-${{ github.workspace }}-cli.jar
          fhir-bucket/target/fhir-bucket-${{ github.workspace }}-cli.jar
          fhir-validation/target/fhir-validation-distribution.zip
          fhir-cli/target/fhir-cli.zip
          fhir-install/target/fhir-server-distribution.zip
          build/release/repo/release-repository-${{ github.tag }}.zip

echo "::endgroup::"
# EOF