# 
# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
#

# Set the instance_memory and enable to 90 days trail license to avoid memory allocation failures. 
  /opt/ibm/db2/V11.5/adm/db2licm -r db2dec && /opt/ibm/db2/V11.5/adm/db2licm -a /var/db2/db2trial.lic

  su - db2inst1 -c "db2 update dbm cfg using INSTANCE_MEMORY AUTOMATIC" \
  && su - db2inst1 -c "db2 update database cfg for LOGFILSIZE using 4000" \
  && su - db2inst1 -c "db2stop" \
  && su - db2inst1 -c "db2start"
  
# Create the FHIR database and give fhiruser access
# Note Update transaction log file size (logfilsiz) to avoid 
# Note 32K pagesize is required

# Create the database if it doesn't yet exist
if [[ $(su - db2inst1 -c "db2 list db directory" | grep alias | grep FHIRDB | awk '{print $4}') == "FHIRDB" ]]; then
  echo "FHIRDB exists - skipping create"
else
  su - db2inst1 -c "db2 CREATE DB FHIRDB using codeset UTF-8 territory us PAGESIZE 32768" 
fi

# To be properly idempotent, we always want to execute this, just in case it failed the first time
  su - db2inst1 -c "db2 \"connect to fhirdb\" && db2 \"grant connect on database TO USER fhiruser\""

