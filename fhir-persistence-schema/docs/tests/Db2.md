For db2
# Prerequisites
Drop the db, and recreate.

db2 terminate
db2 drop db fhirdb
db2 CREATE DB FHIRDB using codeset UTF-8 territory us PAGESIZE 32768

# Test 1 - Existing --create-schemas
--create-schemas
--prop-file fhir-persistence-schema/db2.properties

[db2inst1@4dda34a66a99 ~]$ db2 select schemaname from syscat.schemata
SCHEMANAME
-------------
FHIRDATA
FHIR_ADMIN
FHIR_JBATCH
FHIR_OAUTH

# Test 2 -- new --create-schema-fhir (mixed)
--prop-file fhir-persistence-schema/db2.properties
--create-schemas
--create-schema-batch fhir_batch1
--create-schema-fhir fhirdata1
--create-schema-oauth fhir_oauth1

[db2inst1@4dda34a66a99 ~]$ db2 select schemaname from syscat.schemata
SCHEMANAME
---------------
FHIRDATA
FHIRDATA1
FHIR_ADMIN
FHIR_BATCH1
FHIR_JBATCH
FHIR_OAUTH
FHIR_OAUTH1

# Test 3 -- new --create-schema-fhir - single schema
--prop-file fhir-persistence-schema/db2.properties
--create-schema-batch fhir_batch1

[db2inst1@4dda34a66a99 ~]$ db2 select schemaname from syscat.schemata
SCHEMANAME
---------------
FHIRDATA  
FHIRDATA1 
FHIRDATA2 
FHIR_ADMIN
FHIR_BATCH1 
FHIR_BATCH2 
FHIR_JBATCH 
FHIR_OAUTH
FHIR_OAUTH1 
FHIR_OAUTH2  

# Test 4 - Existing Update Schema
--prop-file fhir-persistence-schema/db2.properties
--update-schema
--schema-name fhirdata
db2 "SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY"
1          
-----------
       1385

[db2inst1@4dda34a66a99 ~]$ db2 "SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY WHERE SCHEMA_NAME = 'FHIR_OAUTH'"
1          
-----------
          3
  1 record(s) selected.

[db2inst1@4dda34a66a99 ~]$ db2 "SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY WHERE SCHEMA_NAME = 'FHIR_JBATCH'"
1          
-----------
          7

  1 record(s) selected.

# Test 5 -- Check-Compatibility
--prop-file fhir-persistence-schema/db2.properties
--check-compatibility

2020-05-20 12:05:40.568 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:db2://localhost:50000/FHIRDB
2020-05-20 12:05:41.260 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   0.748 s
2020-05-20 12:05:41.261 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK

# Test 6 - Existing Update Schema - fhir data
--prop-file fhir-persistence-schema/db2.properties
--update-schema-fhir fhir_fudge
[db2inst1@4dda34a66a99 ~]$ db2 "SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY WHERE SCHEMA_NAME = 'fhir_fudge'"
1          
-----------
       1351
  1 record(s) selected.

# Test 6 - Existing Update Schema Batch
--prop-file fhir-persistence-schema/db2.properties
--update-schema-batch fhir_fudge_batch
2020-05-20 12:18:34.108 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:db2://localhost:50000/FHIRDB
2020-05-20 12:18:34.786 00000001    INFO   com.ibm.fhir.schema.app.Main Collecting model update tasks
2020-05-20 12:18:34.790 00000001    INFO   com.ibm.fhir.schema.app.Main Starting model updates
2020-05-20 12:18:35.253 0000000c    INFO hir.task.core.impl.TaskManager Task complete callback for taskId: TABLE:fhir_fudge_batch.JOBINSTANCE
2020-05-20 12:18:35.721 0000000c    INFO hir.task.core.impl.TaskManager Task complete callback for taskId: TABLE:fhir_fudge_batch.GROUPASSOCIATION
2020-05-20 12:18:35.950 0000000c    INFO hir.task.core.impl.TaskManager Task complete callback for taskId: TABLE:fhir_fudge_batch.JOBEXECUTION
2020-05-20 12:18:36.230 0000000c    INFO hir.task.core.impl.TaskManager Task complete callback for taskId: TABLE:fhir_fudge_batch.STEPTHREADEXECUTION
2020-05-20 12:18:36.346 0000000c    INFO hir.task.core.impl.TaskManager Task complete callback for taskId: TABLE:fhir_fudge_batch.JOBPARAMETER
2020-05-20 12:18:36.601 0000000c    INFO hir.task.core.impl.TaskManager Task complete callback for taskId: TABLE:fhir_fudge_batch.STEPTHREADINSTANCE
2020-05-20 12:18:36.873 0000000c    INFO hir.task.core.impl.TaskManager Task complete callback for taskId: TABLE:fhir_fudge_batch.REMOTABLEPARTITION_TABLE
2020-05-20 12:18:36.875 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   2.856 s
2020-05-20 12:18:36.875 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK

db2 -x "SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY WHERE SCHEMA_NAME = 'fhir_fudge_batch'"
          7

# Test 7 - Existing Update Schema OAuth
--prop-file fhir-persistence-schema/db2.properties
--update-schema-oauth fhir_fudge_oauth
 db2 -x "SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY WHERE SCHEMA_NAME = 'fhir_fudge_oauth'"
          3

# Test 8 - Existing Update Schema OAuth
--prop-file fhir-persistence-schema/db2.properties
--update-schema-fhir fhir_fudge_fhir
db2 -x "SELECT COUNT(*) FROM FHIR_ADMIN.VERSION_HISTORY WHERE SCHEMA_NAME = 'fhir_fudge_fhir'"
       1351

# Test 9 - Grant To fhirdata - FHIR
--prop-file fhir-persistence-schema/db2.properties
--grant-to fhirserver
--target DATA fhir_fudge_fhir

[db2inst1@4dda34a66a99 ~]$ db2 "SELECT DISTINCT GRANTOR,CONTROLAUTH, ALTERAUTH, DELETEAUTH, INDEXAUTH, INSERTAUTH, REFAUTH, SELECTAUTH, UPDATEAUTH FROM SYSCAT.TABAUTH WHERE TABSCHEMA = 'FHIRDATA'"

GRANTOR                                                                                                                          CONTROLAUTH ALTERAUTH DELETEAUTH INDEXAUTH INSERTAUTH REFAUTH SELECTAUTH UPDATEAUTH
-------------------------------------------------------------------------------------------------------------------------------- ----------- --------- ---------- --------- ---------- ------- ---------- ----------
SYSIBM                                                                                                                           Y           G         G          G         G          G       G          G         
DB2INST1                                                                                                                         N           N         Y          N         Y          N       Y          Y         

  2 record(s) selected.

# Test 10 - Grant To OAUTH - FHIR
--prop-file fhir-persistence-schema/db2.properties
--grant-to fhirserver
--target OAUTH FHIR_OAUTH

2020-05-20 13:07:43.917 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_OAUTH.OAUTH20CACHE TO FHIRSERVER
2020-05-20 13:07:43.978 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:db2://localhost:50000/FHIRDB
2020-05-20 13:07:45.054 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_OAUTH.OAUTH20CLIENTCONFIG TO FHIRSERVER
2020-05-20 13:07:45.087 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_OAUTH.OAUTH20CONSENTCACHE TO FHIRSERVER
2020-05-20 13:07:45.135 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   1.404 s
2020-05-20 13:07:45.136 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK

 db2 "SELECT DISTINCT GRANTOR,CONTROLAUTH, ALTERAUTH, DELETEAUTH, INDEXAUTH, INSERTAUTH, REFAUTH, SELECTAUTH, UPDATEAUTH FROM SYSCAT.TABAUTH WHERE TABSCHEMA = 'FHIR_OAUTH'"

GRANTOR                                                                                                                          CONTROLAUTH ALTERAUTH DELETEAUTH INDEXAUTH INSERTAUTH REFAUTH SELECTAUTH UPDATEAUTH
-------------------------------------------------------------------------------------------------------------------------------- ----------- --------- ---------- --------- ---------- ------- ---------- ----------
SYSIBM                                                                                                                           Y           G         G          G         G          G       G          G         
DB2INST1                                                                                                                         N           N         Y          N         Y          N       Y          Y         

  2 record(s) selected.


# Test 11 - Grant To BATCH - FHIR_JBATCH
--prop-file fhir-persistence-schema/db2.properties
--grant-to fhirserver
--target BATCH FHIR_JBATCH
2020-05-20 13:09:05.052 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_JBATCH.JOBINSTANCE TO FHIRSERVER
2020-05-20 13:09:05.085 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:db2://localhost:50000/FHIRDB
2020-05-20 13:09:05.945 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_JBATCH.JOBEXECUTION TO FHIRSERVER
2020-05-20 13:09:05.960 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_JBATCH.STEPTHREADEXECUTION TO FHIRSERVER
2020-05-20 13:09:05.974 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_JBATCH.STEPTHREADINSTANCE TO FHIRSERVER
2020-05-20 13:09:05.990 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_JBATCH.REMOTABLEPARTITION_TABLE TO FHIRSERVER
2020-05-20 13:09:06.002 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_JBATCH.GROUPASSOCIATION TO FHIRSERVER
2020-05-20 13:09:06.012 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT UPDATE,DELETE,INSERT,SELECT ON FHIR_JBATCH.JOBPARAMETER TO FHIRSERVER
2020-05-20 13:09:06.038 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   1.139 s
2020-05-20 13:09:06.039 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK

 db2 "SELECT DISTINCT GRANTOR,CONTROLAUTH, ALTERAUTH, DELETEAUTH, INDEXAUTH, INSERTAUTH, REFAUTH, SELECTAUTH, UPDATEAUTH FROM SYSCAT.TABAUTH WHERE TABSCHEMA = 'FHIR_JBATCH'"

[db2inst1@4dda34a66a99 ~]$ db2 "SELECT DISTINCT TRIM(GRANTEE || ' '), SELECTAUTH|| UPDATEAUTH||DELETEAUTH||INSERTAUTH,CONTROLAUTH||ALTERAUTH||  INDEXAUTH||  REFAUTH  FROM SYSCAT.TABAUTH WHERE TABSCHEMA = 'FHIR_JBATCH'"

1                                                                                                                                 2    3   
--------------------------------------------------------------------------------------------------------------------------------- ---- ----
FHIRSERVER                                                                                                                        YYYY NNNN
DB2INST1                                                                                                                          GGGG YGGG     

# Test 12 - Grant To All Default
--prop-file fhir-persistence-schema/db2.properties
--grant-to fhirserver

db2 "SELECT DISTINCT TRIM(GRANTEE || ' '), SELECTAUTH|| UPDATEAUTH||DELETEAUTH||INSERTAUTH,CONTROLAUTH||ALTERAUTH||  INDEXAUTH||  REFAUTH  FROM SYSCAT.TABAUTH WHERE TABSCHEMA IN ('FHIRDATA','FHIR_OAUTH','FHIR_JBATCH')"

2020-05-20 13:17:32.847 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT EXECUTE ON PROCEDURE FHIRDATA.ADD_RESOURCE_TYPE TO FHIRSERVER
2020-05-20 13:17:32.858 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT EXECUTE ON PROCEDURE FHIRDATA.ADD_ANY_RESOURCE TO FHIRSERVER
2020-05-20 13:17:32.868 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_OAUTH.OAUTH20CACHE TO FHIRSERVER
2020-05-20 13:17:32.891 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_OAUTH.OAUTH20CLIENTCONFIG TO FHIRSERVER
2020-05-20 13:17:32.924 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_OAUTH.OAUTH20CONSENTCACHE TO FHIRSERVER
2020-05-20 13:17:32.943 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_JBATCH.JOBINSTANCE TO FHIRSERVER
2020-05-20 13:17:32.969 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_JBATCH.JOBEXECUTION TO FHIRSERVER
2020-05-20 13:17:32.992 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_JBATCH.STEPTHREADEXECUTION TO FHIRSERVER
2020-05-20 13:17:33.017 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_JBATCH.STEPTHREADINSTANCE TO FHIRSERVER
2020-05-20 13:17:33.046 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_JBATCH.REMOTABLEPARTITION_TABLE TO FHIRSERVER
2020-05-20 13:17:33.079 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_JBATCH.GROUPASSOCIATION TO FHIRSERVER
2020-05-20 13:17:33.105 00000001    INFO s.common.CommonDatabaseAdapter Applying: GRANT SELECT,INSERT,UPDATE,DELETE ON FHIR_JBATCH.JOBPARAMETER TO FHIRSERVER
2020-05-20 13:17:33.140 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:  35.505 s
2020-05-20 13:17:33.140 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK

[db2inst1@4dda34a66a99 ~]$ db2 "SELECT DISTINCT TRIM(GRANTEE || ' '), SELECTAUTH|| UPDATEAUTH||DELETEAUTH||INSERTAUTH,CONTROLAUTH||ALTERAUTH||  INDEXAUTH||  REFAUTH  FROM SYSCAT.TABAUTH WHERE TABSCHEMA IN ('FHIRDATA','FHIR_OAUTH','FHIR_JBATCH')"
1                                                                                                                                 2    3   
--------------------------------------------------------------------------------------------------------------------------------- ---- ----
FHIRSERVER                                                                                                                        YYYY NNNN
DB2INST1                                                                                                                          GGGG YGGG

  2 record(s) selected.

# Test 13 - Allocate Tenant - FHIRDATA
--prop-file fhir-persistence-schema/db2.properties
--schema-name FHIRDATA
--allocate-tenant default
2020-05-20 13:26:22.393 00000001    INFO   com.ibm.fhir.schema.app.Main tenantId [1] is being pre-populated with lookup table data.
2020-05-20 13:26:22.606 00000001    INFO   com.ibm.fhir.schema.app.Main Finished prepopulating the resource type and search parameter code/name tables tables
2020-05-20 13:26:22.649 00000001    INFO   com.ibm.fhir.schema.app.Main Allocated tenant: default [key=DOd7xmwhkx2FhJMyPLwaHeU8PAUWcpGj7VjYgMf2hy0=] with Id = 1
2020-05-20 13:26:22.651 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took: 270.022 s
2020-05-20 13:26:22.651 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK