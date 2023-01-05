#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2022
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Issue - https://github.com/LinuxForHealth/FHIR/issues/1802

# Pre Condition:
# 1 - linuxforhealth/fhir-schematool must be built based on the latest.
#       docker build -t linuxforhealth/fhir-schematool:latest .

# Post Condition: 
# 1 - Postgres should be started
# 2 - No Errors and the schema should work.
# 3 - Check Version History 

# 1 - Start the db
docker-compose up -d db

# 2 - Startup
docker-compose run -e ENV_TOOL_INPUT=$(cat 1802-onboarding.json | base64) tool

# The schemas should exist
echo SELECT schema_name FROM information_schema.schemata | docker-compose exec -T -e PGPASSWORD=change-password db psql -h db -U postgres fhirdb

# Should be 2700+
echo 'SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY' | docker-compose exec -T -e PGPASSWORD=change-password db psql -h db -U postgres fhirdb

docker-compose down -t 1
# EOF