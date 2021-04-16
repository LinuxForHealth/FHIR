#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Don't deploy the zip.

mkdir -p ${WORKSPACE}/build/release/workarea/release_files

export RELEASE_FILES=" $(jq -r '.release_files | join(" ")' ${WORKSPACE}/build/release/config/release.json)"
for RELEASE_FILE in $(echo "${RELEASE_FILES}")
do
    cp "${RELEASE_FILE}" ${WORKSPACE}/build/release/workarea/release_files || true
done

mkdir -p ${WORKSPACE}/build/release/workarea/release_files/build_files
for CACHE_FILE in $(find . -name 'fhir*.jar*' -or -name 'fhir*.zip' -or -name 'fhir*.asc' -or -name 'fhir*.war')
do
    mv "${CACHE_FILE}" ${WORKSPACE}/build/release/workarea/release_files/build_files || true
done

# This is to copy over the pom files uniquely.
# we mangle the input so it is the <module>_pom.xml
for CACHE_FILE in $(find . -name 'pom.xml')
do
    CACHE_F=$(echo ${CACHE_FILE} | sed 's|/| |g' | awk '{print $(NF-1)"_pom.xml"}')
    mv "${CACHE_FILE}" ${WORKSPACE}/build/release/workarea/release_files/build_files/${CACHE_F} || true
done

# EOF