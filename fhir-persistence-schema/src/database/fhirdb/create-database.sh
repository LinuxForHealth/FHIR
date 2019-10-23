#!/usr/bin/env bash

# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
#

# Set up TLS connectivity using a self-signed certificate
# Create the FHIR database and give fhiruser access
# Note 32K pagesize is required

# only do the key creation once
if [[ ! -f /database/config/db2inst1/sqllib/security/keystore/fhirdb.kdb ]]; then
     su - db2inst1 -c "mkdir -p /database/config/db2inst1/sqllib/security/keystore" \
  && su - db2inst1 -c "cd /database/config/db2inst1/sqllib/security/keystore && gsk8capicmd_64 -keydb -create -db fhirdb.kdb -pw change-password -stash" \
  && su - db2inst1 -c "cd /database/config/db2inst1/sqllib/security/keystore && gsk8capicmd_64 -cert -create -db fhirdb.kdb -pw change-password -label fhirdbselfsigned -dn CN=dkr.fhir.ibm.com,OU=FHIR,O=IBM,ST=NY,C=US -size 2048 -sigalg SHA256_WITH_RSA" \
  && su - db2inst1 -c "cd /database/config/db2inst1/sqllib/security/keystore && gsk8capicmd_64 -cert -extract -db fhirdb.kdb -pw "change-password" -label fhirdbselfsigned -target fhirdb.arm -format ascii -fips" 
fi

# Always update the configuration in case DB2 has reset anything
     su - db2inst1 -c "db2 update dbm cfg using SSL_SVR_KEYDB /database/config/db2inst1/sqllib/security/keystore/fhirdb.kdb" \
  && su - db2inst1 -c "db2 update dbm cfg using SSL_SVR_STASH /database/config/db2inst1/sqllib/security/keystore/fhirdb.sth" \
  && su - db2inst1 -c "db2 update dbm cfg using SSL_SVR_LABEL fhirdbselfsigned" \
  && su - db2inst1 -c "db2 update dbm cfg using SSL_SVCENAME 50001" \
  && su - db2inst1 -c "db2 update dbm cfg using SSL_VERSIONS TLSV12" \
  && su - db2inst1 -c "db2set -i db2inst1 DB2COMM=SSL" \
  && su - db2inst1 -c "db2stop" \
  && su - db2inst1 -c "db2start" 

# Finally create the database if it doesn't yet exist
if [[ $(su - db2inst1 -c "db2 list db directory" | grep alias | grep FHIRDB | awk '{print $4}') == "FHIRDB" ]]; then
  echo "FHIRDB exists - skipping create"
else
  su - db2inst1 -c "db2 CREATE DB FHIRDB using codeset UTF-8 territory us PAGESIZE 32768" 
fi

# To be properly idempotent, we always want to execute this, just in case it failed the first time
  su - db2inst1 -c "db2 \"connect to fhirdb\" && db2 \"grant connect on database TO USER fhiruser\""

# Set the instance_memory to avoid memory allocation failures. The value
# of 50 represents 50%"
# su - db2inst1 -c "db2 update dbm cfg using INSTANCE_MEMORY 50" 
