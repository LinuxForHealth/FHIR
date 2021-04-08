#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

mkdir -p build/release/workarea/release_files/test_coverage
tar -czf build/release/workarea/release_files/test_coverage/release-test-coverage-and-logs.tgz $(find . -or -iname jacoco-aggregate -or -iname release-commit-details.txt)

# This is a good location to link with the codecov.io

# EOF