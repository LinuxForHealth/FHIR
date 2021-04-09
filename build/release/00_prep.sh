#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Install missing packages
bash ${WORKSPACE}/build/release/bin/00_prep/0_install_packages.sh

# EOF