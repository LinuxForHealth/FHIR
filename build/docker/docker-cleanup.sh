#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

echo "[Check Status for Disk space]"
docker system df

echo "[Clean up images]"
docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
sudo apt clean

echo "[Docker Images on System]"
docker image ls -aq

echo "[Docker Containers]"
docker container ps

echo "[Docker Prune]"
docker system prune -f