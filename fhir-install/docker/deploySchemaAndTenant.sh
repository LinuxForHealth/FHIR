#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

java -jar volumes/schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --create-schemas

java -jar volumes/schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --update-schema --pool-size 2

java -jar volumes/schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --grant-to FHIRSERVER --pool-size 2

java -jar volumes/schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --allocate-tenant default

# The regex in the following command will output the capture group between "key=" and "]"
# With GNU grep, the following would work as well:  grep -oP 'key=\K\S+(?=])'
tenantKey=$(java -jar volumes/schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --add-tenant-key default 2>&1 \
  | grep "key=" | sed -e 's/.*key\=\(.*\)\].*/\1/')

# sed -i'' -e ... - does not work on OS X 10.6 or lower
sed -i'' -e 's%"default": {%"default": { "tenantKey":"'${tenantKey}'",%' \
  volumes/dist/config/default/fhir-server-config.json
