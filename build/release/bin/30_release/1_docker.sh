#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Assumes that fhir-install has already been built
# and that the user is logged into the configured container registry.

# If the '--latest' flag is passed, then tag the docker image as the latest when we push it later
# Reminder: in bash true evaluates to '0' and false evaluates to '1'; that lets us use 'if $LATEST' below
if [[ "$1" = "--latest" ]]; then
  LATEST=true
else
  LATEST=false
fi

# Executes the build for the linuxforhealth/fhir-schematool.
# This build runs first so that the FAILURE comes before and stops the subsequent builds/releases.
export WORKSPACE=$(pwd)
bash ${WORKSPACE}/fhir-install/src/main/docker/fhir-schematool/build.sh

bash ${WORKSPACE}/fhir-install/src/main/docker/fhir-term-graph-loader/build.sh

bash ${WORKSPACE}/fhir-install/src/main/docker/fhir-bucket-tool/build.sh

# Create and remove a 1 GB file to make sure we have the room needed later.
df -h
dd if=/dev/urandom oflag=direct of=balloon.dat bs=1024k count=1000
rm -f balloon.dat
sudo apt clean
docker system prune -f
df -h

mvn -B dockerfile:build -f fhir-install --no-transfer-progress -Dmaven.wagon.http.retryHandler.count=3
mvn dockerfile:tag@tag-version -f fhir-install
mvn dockerfile:push@push-version -f fhir-install

if $LATEST; then
  mvn dockerfile:push@push-latest -f fhir-install
fi
# EOF
