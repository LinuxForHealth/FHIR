#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020, 2022
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

docker run --env ENV_TOOL_INPUT=$(cat postgres-onboard.json | base64) \
    ibmcom/ibm-fhir-schematool:latest

docker run --env ENV_TOOL_INPUT=$(cat postgres-onboard-noschema.json | base64) \
    ibmcom/ibm-fhir-schematool:latest
