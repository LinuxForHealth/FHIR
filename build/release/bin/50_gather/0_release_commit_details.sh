#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# generates a list of commits since last tag

git log $(git describe --tags --abbrev=0 @^)..@ > build/release/workarea/release_files/release-commit-details.txt

# EOF