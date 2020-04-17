#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

docker system df

sudo apt clean
docker rmi $(docker image ls -aq)
docker image prune -a --filter "until=12h" -f
docker system prune -f