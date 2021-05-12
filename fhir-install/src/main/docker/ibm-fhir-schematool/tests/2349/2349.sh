#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Issue - https://github.com/IBM/FHIR/issues/2349

# 1 - Start the db
docker-compose up -d db

# Create the right pagesize in the db
docker exec -it 2349_db_1 /bin/bash

# In the terminal run the following: 
useradd -g db2iadm1 testuser
echo "testuser:password" | chpasswd
su - db2inst1
db2 drop db fhirdb
db2 CREATE DB FHIRDB using codeset UTF-8 territory us PAGESIZE 32768
db2 grant connect on database TO USER testuser

# 2 - use tool / exercise without TENANT_KEY
docker-compose run tool --tool.behavior=onboard --db.host=192.168.86.29 --db.port=50000 --user=db2inst1 --password=change-password --db.database=fhirdb --sslConnection=false --db.type=db2 --schema.name.fhir=CLINIC --grant.to=testuser     --tenant.name=CLINICA

# Check the TENANTS table
docker-compose exec -it db su - db2inst1 -c "db2 connect to fhirdb; db2 SELECT COUNT(*) FROM FHIR_ADMIN.TENANTS"
# You should see the tenant listed, and you should see the end CLINIC

# EOF
