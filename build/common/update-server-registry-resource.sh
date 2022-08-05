#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Enables Features in the fhir-server-config.json using jq 

tmp=$(mktemp)
jq '.fhirServer.core.serverRegistryResourceProviderEnabled = true' "${1}" > "$tmp" && mv "$tmp" "${1}"

# EOF