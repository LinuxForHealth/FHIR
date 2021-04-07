#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# gather diagnostics and relevant files
# To include the distribution, include ' -or -iname fhir-server-distribution.zip'
# or '-or -iname surefire-reports -or -iname jacoco.exec'

mkdir -p build/release/workarea/release_files/test_coverage
tar -czf build/release/workarea/release_files/test_coverage/release-test-coverage-and-logs.tgz $(find . -or -iname jacoco-aggregate -or -iname release-commit-details.txt)

# EOF