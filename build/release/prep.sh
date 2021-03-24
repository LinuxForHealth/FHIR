#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

jq --arg GHT "123" --arg GHR "124" '.GITHUB_TOKEN = $GHT | .GITHUB_REPOSITORY = $GHR' config/release.json.template
{
  "CI": true,
  "JAVA_HOME": "/opt/hostedtoolcache/AdoptOpenJDK/1.0.0-ga-11-jdk-hotspot-linux-x64-normal-latest/x64",
  "GITHUB_ACTOR": "",
  "GITHUB_TOKEN": "123",
  "GITHUB_REPOSITORY": "124"
}