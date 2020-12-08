#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

docker run --env ENV_TOOL_INPUT=$(cat db2-onboard.json | base64) \
    ibmcom/ibm-fhir-schematool:latest

docker run --env ENV_TOOL_INPUT=$(cat postgres-onboard.json | base64) \
    ibmcom/ibm-fhir-schematool:latest

docker run --env ENV_TOOL_INPUT=$(cat db2-onboard-noschema.json | base64) \
    ibmcom/ibm-fhir-schematool:latest

docker run --env ENV_TOOL_INPUT=$(cat postgres-onboard-noschema.json | base64) \
    ibmcom/ibm-fhir-schematool:latest