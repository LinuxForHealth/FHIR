#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Make sure jq, podman, skopeo and docker are installed

OS_TYPE="$(uname -s)"
case "${OS_TYPE}" in
        Linux*)
        # update
        apt update

        # jq
        apt-get install jq

        # gh
        apt-key adv --keyserver keyserver.ubuntu.com --recv-key C99B11DEB97541F0
        apt-add-repository https://cli.github.com/packages
        apt update
        apt install gh

        # docker tools
        apt-get install docker skopeo podman
    ;;
        Darwin*)
        brew update
        # no keg for buildah
        brew install gh jq docker skopeo podman
    ;;
    *)
        echo "We don't build on this ${OS_TYPE}"
        exit 1
    ;;
esac

# EOF