#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export WORKSPACE=$(pwd)
bash ${WORKSPACE}/build/release/bin/50_gather/0_release_commit_details.sh
bash ${WORKSPACE}/build/release/bin/50_gather/1_repo.sh
bash ${WORKSPACE}/build/release/bin/50_gather/2_codecov.sh

# EOF