-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- This file contains the unit tests for the set_tenant stored procedure.
-- The unit tests test the edge cases.

-- ***Test Data***
-- TENANT_NAME: FRED
-- TENANT_KEY: TESTKEY
-- TENANT_SALT: PEPPER

-- ***Generates the TENANT_SALT (HASHED) value***
-- Result: 
-- 432EAD7D846F2E1761E5E288C897DAB650E6EA11FFB70BFC4982C3CD27A76CC9
WITH TENANT_HASHED(HASHED) AS (
        VALUES( sysibm.hash('PEPPER' || 'TESTKEY', 2))
    ) SELECT HASHED FROM TENANT_HASHED;

-- ***Load the Unit Test Data***
-- TEST - TENANT1,KEY1
-- TEST - TENANT1,KEY2
-- TEST - TENANT2,KEY2
-- TEST - TENANT3,KEY3
INSERT INTO FHIR_ADMIN.TENANTS(mt_id,tenant_name,tenant_status)
VALUES(10000,'TENANT1','ALLOCATED');
INSERT INTO FHIR_ADMIN.TENANTS(mt_id,tenant_name,tenant_status)
VALUES(10010,'TENANT2','ALLOCATED');
INSERT INTO FHIR_ADMIN.TENANTS(mt_id,tenant_name,tenant_status)
VALUES(10020,'TENANT3','ALLOCATED');

-- When creating, the MT_ID and the SALT are required to be unique.
INSERT INTO FHIR_ADMIN.TENANT_KEYS(tenant_key_id, mt_id, tenant_salt, tenant_hash)
VALUES (next value for FHIR_ADMIN.TENANT_SEQUENCE, 10000, 'PEPPER1', SYSIBM.HASH('PEPPER1' || 'TESTKEY1', 2));
INSERT INTO FHIR_ADMIN.TENANT_KEYS(tenant_key_id, mt_id, tenant_salt, tenant_hash)
VALUES (next value for FHIR_ADMIN.TENANT_SEQUENCE, 10000, 'PEPPER4', SYSIBM.HASH('PEPPER4' || 'TESTKEY2', 2));
INSERT INTO FHIR_ADMIN.TENANT_KEYS(tenant_key_id, mt_id, tenant_salt, tenant_hash)
VALUES (next value for FHIR_ADMIN.TENANT_SEQUENCE, 10010, 'PEPPER2', SYSIBM.HASH('PEPPER2' || 'TESTKEY2', 2));
INSERT INTO FHIR_ADMIN.TENANT_KEYS(tenant_key_id, mt_id, tenant_salt, tenant_hash)
VALUES (next value for FHIR_ADMIN.TENANT_SEQUENCE, 10020, 'PEPPER3', SYSIBM.HASH('PEPPER3' || 'TESTKEY3', 2));

-- ***Tests***
-- Unit Test 1: Both Null Inputs - Flows through as it's most likely not multitenant.
-- INPUT: [TENANT_NAME: null, TENANT_KEY: null]
-- OUTPUT: Return Status = 0
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT(NULL,NULL);

-- Unit Test 2: Tenant Name Null, Tenant Key Not Null - Flows through as it's most likely not multitenant.
-- INPUT: [TENANT_NAME: null, TENANT_KEY: TESTKEY1]
-- OUTPUT: Return Status = 0
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT(NULL,'TESTKEY1');

-- Unit Test 3: Tenant Name Not Null, Tenant Key Null - Stops as it's a bad tenant key
-- INPUT: [TENANT_NAME: TENANT1, TENANT_KEY: null]
-- OUTPUT: "NOT AUTHORIZED: MISSING OR INVALID TENANT KEY".  SQLSTATE=99401
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT1', NULL);

-- Unit Test 4: Tenant Name Not Null, Tenant Key Empty VARCHAR - Stops as it's a bad tenant key (empty)
-- INPUT: [TENANT_NAME: TENANT1, TENANT_KEY: '']
-- OUTPUT: "NOT AUTHORIZED: MISSING OR INVALID TENANT KEY".  SQLSTATE=99401
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT1', '');

-- Unit Test 5: Tenant Name Not Null, Tenant Key Not Null - VALID showing a valid MT_ID
-- INPUT: [TENANT_NAME: TENANT1, TENANT_KEY: TESTKEY1]
-- OUTPUT: Return Status = 0
--         MT_ID
--         -----------
--         10000
--         1 record(s) selected.
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT1', 'TESTKEY1');
WITH TENANT_ID(MT_ID) AS (
       VALUES fhir_admin.sv_tenant_id
    ) SELECT MT_ID FROM TENANT_ID;

-- Unit Test 6: Tenant Name Not Null, Tenant Key Not Null - INVALID VALID showing a valid MT_ID
-- INPUT: [TENANT_NAME: TENANT1, TENANT_KEY: TESTKEY3]
-- OUTPUT: "NOT AUTHORIZED: MISSING OR INVALID TENANT KEY".  SQLSTATE=99401
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT1', 'TESTKEY3');

-- Unit Test 7: Tenant Name Not Null, Tenant Key Not Null - Same Tenant Multiple Keys
-- INPUT: [TENANT_NAME: TENANT1, TENANT_KEY: TESTKEY2]
-- OUTPUT: Return Status = 0
--         MT_ID
--         -----------
--         10000
--         1 record(s) selected.
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT1', 'TESTKEY2');
WITH TENANT_ID(MT_ID) AS (
       VALUES fhir_admin.sv_tenant_id
    ) SELECT MT_ID FROM TENANT_ID;

-- Unit Test 8: Tenant Name Not Null, Tenant Key Not Null - Different Tenant (MT_ID)
-- INPUT: [TENANT_NAME: TENANT2, TENANT_KEY: TESTKEY2]
-- OUTPUT: Return Status = 0
--         MT_ID
--         -----------
--         10010
--         1 record(s) selected.
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT2', 'TESTKEY2');
WITH TENANT_ID(MT_ID) AS (
       VALUES fhir_admin.sv_tenant_id
    ) SELECT MT_ID FROM TENANT_ID;

-- Unit Test 9: Tenant Name Not Null, Tenant Key Not Null - Different Tenant (MT_ID)
-- INPUT: [TENANT_NAME: TENANT3, TENANT_KEY: TESTKEY3]
-- OUTPUT: Return Status = 0
--         MT_ID
--         -----------
--         10020
--         1 record(s) selected.
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT3', 'TESTKEY3');
WITH TENANT_ID(MT_ID) AS (
       VALUES fhir_admin.sv_tenant_id
    ) SELECT MT_ID FROM TENANT_ID;

-- Unit Test 10: Tenant Name Not Null, Tenant Key Not Null - Different Tenant and VALID but wrong KEY
-- INPUT: [TENANT_NAME: TENANT3, TENANT_KEY: TESTKEY2]
-- OUTPUT: NOT AUTHORIZED: INVALID TENANT KEY".  SQLSTATE=99401
--         MT_ID
--         -----------
--         -
--         1 record(s) selected.
SET fhir_admin.sv_tenant_id = NULL;
CALL FHIR_ADMIN.SET_TENANT('TENANT3', 'TESTKEY2');
WITH TENANT_ID(MT_ID) AS (
       VALUES fhir_admin.sv_tenant_id
    ) SELECT MT_ID FROM TENANT_ID;

-- ***End of Tests***