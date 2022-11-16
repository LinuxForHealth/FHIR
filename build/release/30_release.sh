#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

bash ${WORKSPACE}/build/release/bin/30_release/0_sonatype.sh

# This should always be last
bash ${WORKSPACE}/build/release/bin/30_release/1_docker.sh --latest

# EOF