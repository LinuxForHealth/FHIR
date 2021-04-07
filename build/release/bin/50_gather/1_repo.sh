#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Don't deploy the zip.

mkdir -p build/release/workarea/release_files

export RELEASE_FILES=" $(jq -r '.release_files | join(" ")' build/release/config/release.json)"
for RELEASE_FILE in $(echo "${RELEASE_FILES}")
do
    cp "${RELEASE_FILE}" build/release/workarea/release_files || true
done

mkdir -p build/release/workarea/release_files/build_files
for CACHE_FILE in $(find . -name '*.jar*' -or -name '*.zip' -or -name '*.asc' -or -name '*.war' -or -name 'pom.xml')
do
    mv "${RELEASE_FILE}" build/release/workarea/release_files/build_files || true
done

# EOF