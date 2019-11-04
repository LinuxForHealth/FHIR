#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2016, 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# Steps that need to be executed

# install gatsby and plugins
# npm install gatsby-plugin-slug --save
npm install 

gatsby build --prefix-paths

# Run gatsby serve to see what the site looks like. 