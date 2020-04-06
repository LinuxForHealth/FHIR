#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

java -jar volumes/schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --update-schema
