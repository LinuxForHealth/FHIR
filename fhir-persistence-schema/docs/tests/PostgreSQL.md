# Test 1 - Existing --create-schemas
--create-schemas
--prop-file postgresql.properties
--db-type postgresql

555ffef73e0a:~$ psql -d fhirdb
psql (12.2)
Type "help" for help.

fhirdb=# \dn
    List of schemas
    Name     |  Owner   
-------------+----------
 fhir_admin  | fhiruser
 fhir_batch1 | postgres
 fhir_batch2 | postgres
 fhir_jbatch | postgres
 fhir_oauth  | fhiruser
 fhir_oauth1 | postgres
 fhir_oauth2 | postgres
 fhirdata    | fhiruser
 fhirdata1   | postgres
 fhirdata2   | postgres
 public      | postgres
(11 rows)

fhirdb=# 

# Test 2 -- new --create-schema-fhir (mixed)
--prop-file postgresql.properties
--db-type postgresql
--create-schemas
--create-schema-batch fhir_batch1
--create-schema-fhir fhirdata1
--create-schema-oauth fhir_oauth1

# Test 3 -- new --create-schema-fhir - single schema
--prop-file postgresql.properties
--db-type postgresql
--create-schema-batch fhir_batch1

# Test 4 - Existing Update Schema
--prop-file postgresql.properties
--db-type postgresql
--update-schema
--schema-name fhirdata3

# Test 5 -- Check-Compatibility
--prop-file postgresql.properties
--db-type postgresql
--check-compatibility

2020-05-20 15:42:38.764 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:postgresql://localhost:5432/fhirdb
2020-05-20 15:42:39.151 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   0.449 s
2020-05-20 15:42:39.152 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK

# Test 6 - Existing Update Schema - fhir data
--prop-file postgresql.properties
--db-type postgresql
--update-schema-fhir fhir_fudge

Run \dn on the db after

SELECT * FROM pg_catalog.pg_tables WHERE schemaname = 'fhir_fudge';
You'll see the tables (no batch no oauth)

# Test 6 - Existing Update Schema Batch
--prop-file postgresql.properties
--db-type postgresql
--update-schema-batch fhir_fudge_batch

fhirdb=# SELECT * FROM pg_catalog.pg_tables WHERE schemaname = 'fhir_fudge_batch';
 fhir_fudge_batch | jobinstance              | postgres   |            | t          | f        | t           | f
 fhir_fudge_batch | groupassociation         | postgres   |            | t          | f        | t           | f
 fhir_fudge_batch | jobexecution             | postgres   |            | t          | f        | t           | f
 fhir_fudge_batch | stepthreadexecution      | postgres   |            | t          | f        | t           | f
 fhir_fudge_batch | stepthreadinstance       | postgres   |            | t          | f        | t           | f
 fhir_fudge_batch | jobparameter             | postgres   |            | t          | f        | t           | f
 fhir_fudge_batch | remotablepartition_table | postgres   |            | t          | f        | t           | f

# Test 7 - Existing Update Schema OAuth
--prop-file postgresql.properties
--db-type postgresql
--update-schema-oauth fhir_fudge_oauth

Create schema fhir_fudge_oauth
Check tables SELECT * FROM pg_catalog.pg_tables WHERE schemaname = 'fhir_fudge_oauth';

# Test 8 - Existing Update Schema DATA
--prop-file postgresql.properties
--db-type postgresql
--update-schema-batch fhir_fudge_fdata

fhirdb=# create schema fhir_fudge_fdata;
CREATE SCHEMA
fhirdb=# SELECT * FROM pg_catalog.pg_tables WHERE schemaname = 'fhir_fudge_fdata'

# Test 9 - Grant To fhirdata - FHIR
--prop-file postgresql.properties
--db-type postgresql
--grant-to fhirserver
--target DATA fhir_fudge_fdata

# Test 10 - Grant To OAUTH - FHIR
--prop-file postgresql.properties
--grant-to fhirserver
--db-type postgresql
--target OAUTH FHIR_OAUTH

# Test 11 - Grant To BATCH - FHIR_JBATCH
--prop-file postgresql.properties
--grant-to fhirserver
--db-type postgresql
--target BATCH FHIR_JBATCH

# Test 12 - Grant To All Default
--prop-file postgresql.properties
--db-type postgresql
--grant-to fhirserver

# Test 13 - Allocate Tenant - FHIRDATA
--prop-file postgresql.properties
--schema-name FHIRDATA
--allocate-tenant default
--db-type postgresql