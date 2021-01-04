#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# To execute a change make a call like the following:
# bash build/release/change-version.sh "4.5.0-SNAPSHOT" "4.6.0-SNAPSHOT"

# If you want to check the processed files, run this command: 
# diff <(find . -iname pom.xml -not -path '**/target/*'   | sed 's|\./||g' | sort -u) <(git status  | grep -i modif | awk '{print $NF}')

PREVIOUS=$1
NEXT=$2
find . -iname pom.xml -not -path '**/target/*' -exec sed -i '' s/${PREVIOUS}/${NEXT}/g {} +