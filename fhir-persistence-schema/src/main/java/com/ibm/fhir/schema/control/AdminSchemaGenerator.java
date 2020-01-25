/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.FK;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.IDX;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MT_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANTS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_HASH;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_KEYS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_KEY_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_NAME;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_SALT;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_SEQUENCE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_STATUS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.database.utils.model.GroupPrivilege;
import com.ibm.fhir.database.utils.model.IDatabaseObject;
import com.ibm.fhir.database.utils.model.NopObject;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.Sequence;
import com.ibm.fhir.database.utils.model.SessionVariableDef;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.Tablespace;

/**
 * Generates the FHIR_ADMIN schema which holds the TENANTS
 * and TENANT_KEYS tables and supporting stored procedures
 */
public class AdminSchemaGenerator {
    private final String adminSchemaName;

    // Sequence used by the admin tenant tables
    private Sequence tenantSequence;

    // The session variable used for row access control. All tables depend on this
    private SessionVariableDef sessionVariable;

    // The default non-tenant tablespace where everything goes
    private Tablespace fhirTablespace;

    private Table tenantsTable;
    private Table tenantKeysTable;

    // Privileges needed for reading the sv_tenant_id variable
    List<GroupPrivilege> variablePrivileges = new ArrayList<>();

    // Privileges needed for using the fhir sequence
    List<GroupPrivilege> sequencePrivileges = new ArrayList<>();

    // Privileges needed by the stored procedures
    private List<GroupPrivilege> procedurePrivileges = new ArrayList<>();


    private static final String SET_TENANT = "SET_TENANT";

    // The set of dependencies common to all of our stored procedures
    private Set<IDatabaseObject> procedureDependencies = new HashSet<>();

    // A NOP marker used to ensure procedures are only applied after all the create
    // table statements are applied - to avoid DB2 catalog deadlocks
    private IDatabaseObject allTablesComplete;

    public AdminSchemaGenerator(String adminSchemaName) {
        this.adminSchemaName = adminSchemaName;
    }

    public Tablespace getFhirTablespace() {
        return this.fhirTablespace;
    }

    public SessionVariableDef getSessionVariable() {
        return this.sessionVariable;
    }

    public void buildSchema(PhysicalDataModel model) {
        // Set up the privileges required for the FHIR USER to access objects in the
        // admin schema
        procedurePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.EXECUTE));
        variablePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.READ));
        sequencePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.USAGE));


        // All tables are added to this new tablespace (which has a small extent size.
        // Each tenant partition gets its own tablespace
        fhirTablespace = new Tablespace(FhirSchemaConstants.FHIR_TS, FhirSchemaConstants.INITIAL_VERSION, FhirSchemaConstants.FHIR_TS_EXTENT_KB);
        model.addObject(fhirTablespace);

        addTenantSequence(model);
        addTenantTable(model);
        addTenantKeysTable(model);
        addVariable(model);

        this.allTablesComplete = new NopObject(adminSchemaName, "allTablesComplete");
        this.allTablesComplete.addDependencies(procedureDependencies);
        model.addObject(allTablesComplete);

        model.addProcedure(this.adminSchemaName, SET_TENANT, FhirSchemaConstants.INITIAL_VERSION, () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, adminSchemaName, SET_TENANT.toLowerCase() + ".sql", null), Arrays.asList(allTablesComplete), procedurePrivileges);

    }

    /**
     * Add the session variable we need
     * @param model
     */
    public void addVariable(PhysicalDataModel model) {
        sessionVariable = new SessionVariableDef(adminSchemaName, "SV_TENANT_ID", FhirSchemaConstants.INITIAL_VERSION);
        variablePrivileges.forEach(p -> p.addToObject(sessionVariable));
        model.addObject(sessionVariable);
    }

    /**
     * Create a table to manage the list of tenants. The tenant id is used
     * as a partition value for all the other tables
     * @param model
     */
    protected void addTenantTable(PhysicalDataModel model) {

        this.tenantsTable = Table.builder(adminSchemaName, TENANTS)
                .addIntColumn(            MT_ID,             false)
                .addVarcharColumn(      TENANT_NAME,        36,  false) // probably a UUID
                .addVarcharColumn(    TENANT_STATUS,        16,  false)
                .addUniqueIndex(IDX + "TENANT_TN", TENANT_NAME)
                .addPrimaryKey("TENANT_PK", MT_ID)
                .setTablespace(fhirTablespace)
                .build(model);

        this.procedureDependencies.add(tenantsTable);
        model.addTable(tenantsTable);
        model.addObject(tenantsTable);
    }

    /**
     * Each tenant can have multiple access keys which are used to authenticate and authorize
     * clients for access to the data for a given tenant. We support multiple keys per tenant
     * as a way to allow key rotation in the configuration without impacting service continuity
     * @param model
     */
    protected void addTenantKeysTable(PhysicalDataModel model) {

        this.tenantKeysTable = Table.builder(adminSchemaName, TENANT_KEYS)
                .addIntColumn(        TENANT_KEY_ID,             false) // PK
                .addIntColumn(            MT_ID,             false) // FK to TENANTS
                .addVarcharColumn(      TENANT_SALT,        44,  false) // 32 bytes == 44 Base64 symbols
                .addVarbinaryColumn(    TENANT_HASH,        32,  false) // SHA-256 => 32 bytes
                .addUniqueIndex(IDX + "TENANT_KEY_SALT", TENANT_SALT)   // we want every salt to be unique
                .addUniqueIndex(IDX + "TENANT_KEY_TIDH", MT_ID, TENANT_HASH)   // for set_tenant query
                .addPrimaryKey("TENANT_KEY_PK", TENANT_KEY_ID)
                .addForeignKeyConstraint(FK + TENANT_KEYS + "_TNID", adminSchemaName, TENANTS, MT_ID) // dependency
                .setTablespace(fhirTablespace)
                .build(model);

        this.procedureDependencies.add(tenantKeysTable);
        model.addTable(tenantKeysTable);
        model.addObject(tenantKeysTable);
    }

    /**
    CREATE SEQUENCE fhir_sequence
                 AS BIGINT
         START WITH 1
              CACHE 1000
           NO CYCLE;
     * 
     * @param pdm
     */
    protected void addTenantSequence(PhysicalDataModel pdm) {
        this.tenantSequence = new Sequence(adminSchemaName, TENANT_SEQUENCE, FhirSchemaConstants.INITIAL_VERSION, 1000);
        procedureDependencies.add(tenantSequence);
        sequencePrivileges.forEach(p -> p.addToObject(tenantSequence));

        pdm.addObject(tenantSequence);
    }

}