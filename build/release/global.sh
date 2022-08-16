#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export WORKSPACE=$(pwd)
. build/release/bin/global/1_set_maven_opts.sh
. build/release/bin/global/2_verify_build_settings.sh

# EOF