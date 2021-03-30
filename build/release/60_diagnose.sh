#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

echo "::group::Preparing the Build"

# gather diagnostics and relevant files

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"
source "$(dirname '$0')/release.properties"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

# Reset to Original Directory
popd > /dev/null

###############################################################################

diagnostic_details > diagnostic_details.log

# To include the distribution, include ' -or -iname fhir-server-distribution.zip'
# or '-or -iname surefire-reports -or -iname jacoco.exec'
tar -czf release-test-coverage-and-logs.tgz $(find . -iname diagnostic_details.log -or -iname jacoco-aggregate -or -iname release-commit-details.txt)

echo "::endgroup::"
# EOF