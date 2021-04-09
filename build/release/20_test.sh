#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

bash ${WORKSPACE}/build/release/bin/20_test/0_check_dependency_security.sh
bash ${WORKSPACE}/build/release/bin/20_test/1_code_coverage.sh

# EOF