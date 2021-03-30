#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Store the current directory to reset to
pushd "$(pwd)" > /dev/null || true

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

export JAVA_HOME="$(jq -r '.java.JAVA_HOME' config/release.json)"
echo "Working with the Java Home configuration: [${JAVA_HOME}]"

# Reset to Original Directory
popd > /dev/null || true

# EOF