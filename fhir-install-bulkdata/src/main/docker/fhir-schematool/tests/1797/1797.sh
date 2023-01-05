#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2022
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Issue - https://github.com/LinuxForHealth/FHIR/issues/1797

# Don't log out the --pool-size

# Pre Condition:
# 1 - linuxforhealth/fhir-schematool must be built based on the latest.
#       docker build -t linuxforhealth/fhir-schematool:latest .

# Post Condition: 
# 1 - Postgres should be started
# 2 - No Error Message like 'java.lang.IllegalArgumentException: Invalid SQL object name: --pool-size'

docker-compose up -d db

# Startup
docker-compose run tool --tool.behavior=onboard --db.type=postgresql  \
    --db.host=db --db.port=5432 --db.database=fhirdb --schema.name.fhir=fhirdata \
    --user=postgres --password=change-password

# Check the log

docker-compose down -t 1
# EOF