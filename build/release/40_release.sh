#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

echo "::group::Preparing the Build"

        bash build/release/bootstrap-env.sh
        . ./build/release/bootstrap.env
        bash build/release/version.sh
        bash build/release/clean.sh
        bash build/release/build.sh
        bash build/release/code-coverage.sh
        bash build/release/release-to-mc.sh
        bash build/release/create_repo.sh
        bash build/release/docker-push.sh
        bash build/release/release-commit-details.sh
        bash build/release/diagnostics.sh

echo "::endgroup::"
# EOF