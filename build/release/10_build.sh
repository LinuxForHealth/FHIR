#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

bash build/release/10_build/0_cache.sh
bash build/release/10_build/1_clean.sh
bash build/release/10_build/2_version.sh
bash build/release/10_build/3_build.sh

# EOF