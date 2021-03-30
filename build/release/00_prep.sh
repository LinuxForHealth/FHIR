#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

echo "::group::Preparing the Build"

# Install missing packages
bash build/release/00_prep/0_install_packages.sh

# Set the global Maven options
build/release/bin/00_prep/1_set_maven_opts.sh

echo "::endgroup::"

# EOF