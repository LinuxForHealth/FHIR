#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

echo "[Cleaning up Apt]"
sudo apt clean

echo "[System Disk Space]"
df -h

echo "[Space in Git Repo]"
du -sh

echo "[Check Status for Disk space]"
docker system df

echo "[Docker Images on System]"
docker image ls -a

echo "[Docker Containers]"
docker container ps

echo "[Clean up images]"
if [ ! -z "$(docker images --filter "dangling=true" -q --no-trunc)" ]
then
    docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
fi 

echo "[Docker Prune]"
docker system prune -f