#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# The full path to the directory of this script, no matter where its called from
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd ${DIR}

# For #1366 the migration hits deadlock issues if run in parallel, so
# to avoid this, serialize the steps using --pool-size 1
java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --update-schema \
  --pool-size 1

# Rerun grants to cover any new tables added by the above migration step 
java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --grant-to FHIRSERVER --pool-size 2

# And make sure that the new tables have partitions for existing tenants
java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --refresh-tenants

java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --grant-to FHIRSERVER \
  --pool-size 20 
