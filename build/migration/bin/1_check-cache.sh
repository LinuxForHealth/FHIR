#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -x

if [ -d ~/build/migration/.cache ]
then
    echo "Listing the files in the cache: "
    find ~/build/migration/.cache
fi

if [ -f "$GITHUB_ENV" ]
then
    echo "Output of the Environment variables: "
    cat "$GITHUB_ENV"
fi

if [ ! -f ~/build/migration/.cache/db.tgz ]
then
    echo "Skipping as the cached file does not exist"
    echo "migration_skip=false" >> $GITHUB_ENV
    exit 0
fi

if [ -z ~/build/migration/.cache/db.tgz ]
then
    echo "The cached file is empty"
    echo "migration_skip=false" >> $GITHUB_ENV
    exit 0
fi

if [ "$(file ~/build/migration/.cache/db.tgz | grep -q 'gzip compressed data' && echo yes || echo no)" == "no" ]
then
    echo "Invalid tgz format"
    echo "migration_skip=false" >> $GITHUB_ENV
    exit 0
else
    echo "migration_cache=false" >> $GITHUB_ENV
fi

if [ -f "$GITHUB_ENV" ]
then
    echo "Output of the Environment variables: "
    cat "$GITHUB_ENV"
fi

mkdir -p ${WORSKPACE}/build/migration/integration-test-results

# EOF
###############################################################################