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

# Loop up to 4
not_ready=true
retry_count=0
while [ $not_ready = true ]
do 
  LOG_OUT=$(java -jar schema/fhir-persistence-schema-*-cli.jar \
    --prop-file db2.properties --schema-name FHIRDATA --create-schemas)
  EXIT_CODE=$NF
  if [ $EXIT_CODE -eq 0 ]
  then 
    # We now just send out the output and stop the loop
    echo $LOG_OUT
    not_ready=false
  elif [ $EXIT_CODE -eq 4 ] || [ echo $LOG_OUT | grep -q "SQLCODE=-1035, SQLSTATE=57019" ]
  then
    # EXIT_NOT_READY = 4 - we know in certain versions that this is to be automatically re-tried
    retry_count=$((retry_count + 1))
    if [ $retry_count -lt 4 ]
    then
      echo "Waiting for the Database to be ready - Sleeping"
      sleep 60
    else
      echo "Reached Limit while waiting"
      echo $LOG_OUT
      exit $NF
    fi
  else
    # We need to check and/or fail. 
    echo $LOG_OUT
  fi
done

java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --update-schema --pool-size 2

java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --grant-to FHIRSERVER --pool-size 2

java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --allocate-tenant default --pool-size 2

# The regex in the following command will output the capture group between "key=" and "]"
# With GNU grep, the following would work as well:  grep -oP 'key=\K\S+(?=])'
tenantKey=$(java -jar schema/fhir-persistence-schema-*-cli.jar \
  --prop-file db2.properties --schema-name FHIRDATA --add-tenant-key default 2>&1 \
  | grep "key=" | sed -e 's/.*key\=\(.*\)\].*/\1/')

# Creating a backup file is the easiest way to make in-place sed portable across OSX and Linux
sed -i'.bak' -e 's%"default": {%"default": { "tenantKey":"'${tenantKey}'",%' \
  fhir-server/volumes/config/default/fhir-server-config.json
