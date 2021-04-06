#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# setup the maven default options

# maven.config
export TMP_MAVEN_OPTS=" $(jq -r '.maven | map(.setting)| join(" ")' build/release/config/release.json)"
echo "Working with the configuration: ${TMP_MAVEN_OPTS}"

mkdir -p .mvn/
echo "${TMP_MAVEN_OPTS}" > .mvn/maven.config

# jvm.config
export TMP_MAVEN_JVM_OPTS=" $(jq -r '.maven_jvm | map(.setting)| join(" ")' build/release/config/release.json)"
echo "Working with the configuration: ${TMP_MAVEN_JVM_OPTS}"
echo "${TMP_MAVEN_JVM_OPTS}" > .mvn/jvm.config

# EOF