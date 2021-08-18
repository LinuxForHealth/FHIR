#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export BUILD_TYPE="release"

if [ -z "${DOCKERHUB_USERNAME}" ]
then
    echo "DockerHub Username not set"
    exit 1
fi

if [ -z "${DOCKERHUB_TOKEN}" ]
then
    echo "DockerHub Token not set"
    exit 2
fi

if [ -z "${GPG_PASSPHRASE}" ]
then
    echo "GPG_PASSPHRASE not set"
    exit 3
fi

if [ -z "${GPG_KEYNAME}" ]
then
    echo "GPG_KEYNAME not set"
    exit 4
fi

if [ -n "${GPG_KEY_FILE}" ]
then
    echo "${GPG_KEY_FILE}" | base64 -d > private.key
    gpg --batch --import private.key || true
    rm -rf private.key || true
else
    echo "GPG_KEY_FILE not set"
    exit 5
fi

if [ -z "${JAVA_HOME}" ]
then
    echo "JAVA_HOME not set"
    exit 6
fi

export BUILD_VERSION="$(echo "${GITHUB_REF}" | sed 's|refs/tags/||g')"
export BUILD_ID="$(echo "${GITHUB_REF}" | sed 's|refs/tags/||g')"
export GIT_BRANCH="${GITHUB_REF}"
export GIT_COMMIT="${GITHUB_SHA}"
export GIT_URL="${GITHUB_REPOSITORY}"
export BUILD_DISPLAY_NAME="${BUILD_ID}"

# EOF