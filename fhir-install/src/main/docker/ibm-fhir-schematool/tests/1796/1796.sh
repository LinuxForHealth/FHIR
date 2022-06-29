#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Issue - https://github.com/LinuxForHealth/FHIR/issues/1796

# Don't log out the file isn't found warning.

# Pre Condition:
# 1 - ibmcom/ibm-fhir-schematool must be built based on the latest.
#       docker build -t ibmcom/ibm-fhir-schematool:latest .

# Post Condition: 
# 1 - Postgres should be started
# 2 - No Error Message like 'jq: error: Could not open file /opt/schematool/workarea/persistence.json'

docker-compose up -d db

# Startup
docker-compose run tool --tool.behavior=onboard --db.type=postgresql  \
    --db.host=db --db.port=5432 --db.database=fhirdb --schema.name.fhir=fhirdata3 \
    --user=postgres --password=change-password

docker-compose down -t 1
# EOF