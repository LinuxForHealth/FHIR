#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

bash ${WORKSPACE}/build/release/bin/10_build/0_cache.sh
bash ${WORKSPACE}/build/release/bin/10_build/1_clean.sh
bash ${WORKSPACE}/build/release/bin/10_build/2_version.sh
bash ${WORKSPACE}/build/release/bin/10_build/3_build.sh

# EOF