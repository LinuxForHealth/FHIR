#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2016, 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

nvm use 14.0

nvm install 14.0 

npm install yarn -g

yarn install

yarn run build:dev