#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# generates a list of commits since last tag

mkdir -p ${WORKSPACE}/build/release/workarea/release_files
git log $(git describe --tags --abbrev=0 @^)..@ > ${WORKSPACE}/build/release/workarea/release_files/release-commit-details.txt

# EOF