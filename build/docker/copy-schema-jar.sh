#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -e

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi

echo "Removing the old fhir-persistence-schema tool..."
SCHEMA="${WORKSPACE}/build/docker/schema"
rm -rf $SCHEMA/* 2> /dev/null
mkdir -p $SCHEMA

echo "Copying fhir-persistence-schema tool..."
cp -pr ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar ${SCHEMA}

echo "Finished copying the fhir-persistence-schema tool."
