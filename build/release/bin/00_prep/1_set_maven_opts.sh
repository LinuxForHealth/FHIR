#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export MAVEN_OPTS=" $(jq -r '.maven | map(.setting)| join(" ")' build/release/config/release.json)"
echo "Working with the configuration: ${MAVEN_OPTS}"

mkdir -p .m2/
echo "${MAVEN_OPTS}" > maven.config

# EOF