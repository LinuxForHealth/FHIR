/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.prior;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEMS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEM_NAME;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMMON_TOKEN_VALUES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMMON_TOKEN_VALUE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMPARTMENT_LOGICAL_RESOURCE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMPARTMENT_NAME_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_END;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_START;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_VALUES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_VALUE_DROPPED_COLUMN;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FHIR_REF_SEQUENCE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FHIR_SEQUENCE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FK;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.IDX;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LAST_UPDATED;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_COMPARTMENTS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MAX_SEARCH_STRING_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MAX_TOKEN_VALUE_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MT_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAME;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAMES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAME_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.REF_VERSION_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.REINDEX_TSTAMP;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.REINDEX_TXID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_TOKEN_REFS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_TYPE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_TYPES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_TYPE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.STR_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.STR_VALUES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.STR_VALUE_LCASE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANTS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_HASH;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_KEYS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_KEY_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_NAME;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_SALT;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_SEQUENCE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_STATUS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TOKEN_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TOKEN_VALUES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.common.AddColumn;
import com.ibm.fhir.database.utils.common.CreateIndexStatement;
import com.ibm.fhir.database.utils.common.DropColumn;
import com.ibm.fhir.database.utils.common.DropIndex;
import com.ibm.fhir.database.utils.model.AlterSequenceStartWith;
import com.ibm.fhir.database.utils.model.BaseObject;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.ColumnDefBuilder;
import com.ibm.fhir.database.utils.model.FunctionDef;
import com.ibm.fhir.database.utils.model.Generated;
import com.ibm.fhir.database.utils.model.GroupPrivilege;
import com.ibm.fhir.database.utils.model.IDatabaseObject;
import com.ibm.fhir.database.utils.model.NopObject;
import com.ibm.fhir.database.utils.model.ObjectGroup;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.ProcedureDef;
import com.ibm.fhir.database.utils.model.Sequence;
import com.ibm.fhir.database.utils.model.SessionVariableDef;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.Tablespace;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.FhirSchemaVersion;
import com.ibm.fhir.schema.control.SchemaGeneratorUtil;

/**
 * Encapsulates the generation of the FHIR schema artifacts
 */
public class FhirSchemaGenerator455 {
    // The schema holding all the data-bearing tables
    private final String schemaName;

    // The schema used for administration objects like the tenants table, variable etc
    private final String adminSchemaName;

    /// Build the multitenant variant of the schema
    private final boolean multitenant;

    private static final String ADD_CODE_SYSTEM = "ADD_CODE_SYSTEM";
    private static final String ADD_PARAMETER_NAME = "ADD_PARAMETER_NAME";
    private static final String ADD_RESOURCE_TYPE = "ADD_RESOURCE_TYPE";
    private static final String ADD_ANY_RESOURCE = "ADD_ANY_RESOURCE";

    // The tags we use to separate the schemas
    public static final String SCHEMA_GROUP_TAG = "SCHEMA_GROUP";
    public static final String FHIRDATA_GROUP = "FHIRDATA";
    public static final String ADMIN_GROUP = "FHIR_ADMIN";

    // ADMIN SCHEMA CONTENT

    // Sequence used by the admin tenant tables
    private Sequence tenantSequence;

    // The session variable used for row access control. All tables depend on this
    private SessionVariableDef sessionVariable;

    private Table tenantsTable;
    private Table tenantKeysTable;

    private static final String SET_TENANT = "SET_TENANT";

    // The set of dependencies common to all of our admin stored procedures
    private Set<IDatabaseObject> adminProcedureDependencies = new HashSet<>();

    // A NOP marker used to ensure procedures are only applied after all the create
    // table statements are applied - to avoid DB2 catalog deadlocks
    private IDatabaseObject allAdminTablesComplete;

    // Marker used to indicate that the admin schema is all done
    private IDatabaseObject adminSchemaComplete;

    // The resource types to generate schema for
    private final Set<String> resourceTypes;

    // The common sequence used for allocated resource ids
    private Sequence fhirSequence;

    // The sequence used for the reference tables (parameter_names, code_systems etc)
    private Sequence fhirRefSequence;

    // The set of dependencies common to all of our resource procedures
    private Set<IDatabaseObject> procedureDependencies = new HashSet<>();

    private Table codeSystemsTable;
    private Table parameterNamesTable;
    private Table resourceTypesTable;
    private Table commonTokenValuesTable;

    // A NOP marker used to ensure procedures are only applied after all the create
    // table statements are applied - to avoid DB2 catalog deadlocks
    private IDatabaseObject allTablesComplete;

    // Privileges needed by the stored procedures
    private List<GroupPrivilege> procedurePrivileges = new ArrayList<>();

    // Privileges needed for access to the FHIR resource data tables
    private List<GroupPrivilege> resourceTablePrivileges = new ArrayList<>();

    // Privileges needed for reading the sv_tenant_id variable
    private List<GroupPrivilege> variablePrivileges = new ArrayList<>();

    // Privileges needed for using the fhir sequence
    private List<GroupPrivilege> sequencePrivileges = new ArrayList<>();

    // The default tablespace used for everything not specific to a tenant
    private Tablespace fhirTablespace;

    /**
     * Generate the IBM FHIR Server Schema for all resourceTypes
     *
     * @param adminSchemaName
     * @param schemaName
     */
    public FhirSchemaGenerator455(String adminSchemaName, String schemaName, boolean multitenant) {
        this(adminSchemaName, schemaName, multitenant, Arrays.stream(ResourceType.values())
                .map(ResourceType::value)
                .collect(Collectors.toSet()));
    }

    /**
     * Generate the IBM FHIR Server Schema with just the given resourceTypes
     *
     * @param adminSchemaName
     * @param schemaName
     */
    public FhirSchemaGenerator455(String adminSchemaName, String schemaName, boolean multitenant, Set<String> resourceTypes) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName = schemaName;
        this.multitenant = multitenant;

        // The FHIR user (e.g. "FHIRSERVER") will need these privileges to be granted to it. Note that
        // we use the group identified by FHIR_USER_GRANT_GROUP here - these privileges can be applied
        // to any DB2 user using an admin user, or another user with sufficient GRANT TO privileges.


        // The FHIRSERVER user gets EXECUTE privilege specifically on the SET_TENANT procedure, which is
        // owned by the admin user, not the FHIRSERVER user.
        procedurePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.EXECUTE));

        // FHIRSERVER needs INSERT, SELECT, UPDATE and DELETE on all the resource data tables
        resourceTablePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.INSERT));
        resourceTablePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.SELECT));
        resourceTablePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.UPDATE));
        resourceTablePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.DELETE));

        // FHIRSERVER gets only READ privilege to the SV_TENANT_ID variable. The only way FHIRSERVER can
        // set (write to) SV_TENANT_ID is by calling the SET_TENANT stored procedure, which requires
        // both TENANT_NAME and TENANT_KEY to be provided.
        variablePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.READ));

        // FHIRSERVER gets to use the FHIR sequence
        sequencePrivileges.add(new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.USAGE));

        this.resourceTypes = resourceTypes;
    }

    /**
     * Build the admin part of the schema. One admin schema can support multiple FHIRDATA
     * schemas. It is also possible to have multiple admin schemas (on a dev system,
     * for example, although in production there would probably be just one admin schema
     * in a given database
     * @param model
     */
    public void buildAdminSchema(PhysicalDataModel model) {
        // All tables are added to this new tablespace (which has a small extent size.
        // Each tenant partition gets its own tablespace
        fhirTablespace = new Tablespace(FhirSchemaConstants.FHIR_TS, FhirSchemaVersion.V0001.vid(), FhirSchemaConstants.FHIR_TS_EXTENT_KB);
        fhirTablespace.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        model.addObject(fhirTablespace);

        addTenantSequence(model);
        addTenantTable(model);
        addTenantKeysTable(model);
        addVariable(model);

        // Add a NopObject which acts as a single dependency marker for the procedure objects to depend on
        this.allAdminTablesComplete = new NopObject(adminSchemaName, "allAdminTablesComplete");
        this.allAdminTablesComplete.addDependencies(adminProcedureDependencies);
        this.allAdminTablesComplete.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        model.addObject(allAdminTablesComplete);

        // The set_tenant procedure can be created after all the admin tables are done
        final String ROOT_DIR = "db2/";
        ProcedureDef setTenant = model.addProcedure(this.adminSchemaName, SET_TENANT, 2,
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, adminSchemaName,
                    ROOT_DIR + SET_TENANT.toLowerCase() + ".sql", null),
                Arrays.asList(allAdminTablesComplete),
                procedurePrivileges);
        setTenant.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);

        // A final marker which is used to block any FHIR data schema activity until the admin schema is completed
        this.adminSchemaComplete = new NopObject(adminSchemaName, "adminSchemaComplete");
        this.adminSchemaComplete.addDependencies(Arrays.asList(setTenant));
        this.adminSchemaComplete.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        model.addObject(adminSchemaComplete);
    }

    /**
     * Add the session variable we need. This variable is used to support multi-tenancy
     * via the row-based access control permission predicate.
     * @param model
     */
    public void addVariable(PhysicalDataModel model) {
        this.sessionVariable = new SessionVariableDef(adminSchemaName, "SV_TENANT_ID", FhirSchemaVersion.V0001.vid());
        this.sessionVariable.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        variablePrivileges.forEach(p -> p.addToObject(this.sessionVariable));

        // Make sure any admin procedures are built after the session variable
        adminProcedureDependencies.add(this.sessionVariable);
        model.addObject(this.sessionVariable);
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

        this.tenantsTable.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        this.adminProcedureDependencies.add(tenantsTable);
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
                .addIntColumn(                MT_ID,             false) // FK to TENANTS
                .addVarcharColumn(      TENANT_SALT,        44,  false) // 32 bytes == 44 Base64 symbols
                .addVarbinaryColumn(    TENANT_HASH,        32,  false) // SHA-256 => 32 bytes
                .addUniqueIndex(IDX + "TENANT_KEY_SALT", TENANT_SALT)   // we want every salt to be unique
                .addUniqueIndex(IDX + "TENANT_KEY_TIDH", MT_ID, TENANT_HASH)   // for set_tenant query
                .addPrimaryKey("TENANT_KEY_PK", TENANT_KEY_ID)
                .addForeignKeyConstraint(FK + TENANT_KEYS + "_TNID", adminSchemaName, TENANTS, MT_ID) // dependency
                .setTablespace(fhirTablespace)
                .build(model);

        this.tenantKeysTable.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        this.adminProcedureDependencies.add(tenantKeysTable);
        model.addTable(tenantKeysTable);
        model.addObject(tenantKeysTable);
    }

    /**
    <pre>
    CREATE SEQUENCE fhir_sequence
             AS BIGINT
     START WITH 1
          CACHE 1000
       NO CYCLE;
     </pre>
     *
     * @param pdm
     */
    protected void addTenantSequence(PhysicalDataModel pdm) {
        this.tenantSequence = new Sequence(adminSchemaName, TENANT_SEQUENCE, FhirSchemaVersion.V0001.vid(), 1, 1000);
        this.tenantSequence.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        adminProcedureDependencies.add(tenantSequence);
        sequencePrivileges.forEach(p -> p.addToObject(tenantSequence));

        pdm.addObject(tenantSequence);
    }

    /**
     * Create the schema using the given target
     * @param model
     */
    public void buildSchema(PhysicalDataModel model) {
        // Build the complete physical model so that we know it's consistent
        buildAdminSchema(model);
        addFhirSequence(model);
        addFhirRefSequence(model);
        addParameterNames(model);
        addCodeSystems(model);
        addCommonTokenValues(model);
        addResourceTypes(model);
        addLogicalResources(model); // for system-level parameter search
        addReferencesSequence(model);
        addLogicalResourceCompartments(model);

        Table globalTokenValues = addResourceTokenValues(model); // for system-level _tag and _security parameters
        Table globalStrValues = addResourceStrValues(model); // for system-level _profile parameters
        Table globalDateValues = addResourceDateValues(model); // for system-level date parameters

        // new normalized table for supporting token data (replaces TOKEN_VALUES)
        Table globalResourceTokenRefs = addResourceTokenRefs(model);

        // The three "global" tables aren't true dependencies, but this was the easiest way to force sequential processing
        // and avoid a pesky deadlock issue we were hitting while adding foreign key constraints on the global tables
        addResourceTables(model, globalTokenValues, globalStrValues, globalDateValues, globalResourceTokenRefs);

        // All the table objects and types should be ready now, so create our NOP
        // which is used as a single dependency for all procedures. This means
        // procedures won't start until all the create table/type etc statements
        // are done...hopefully reducing the number of deadlocks we see.
        this.allTablesComplete = new NopObject(schemaName, "allTablesComplete");
        this.allTablesComplete.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.allTablesComplete.addDependencies(procedureDependencies);
        model.addObject(allTablesComplete);
    }

    public void buildDatabaseSpecificArtifactsDb2(PhysicalDataModel model) {
        // These procedures just depend on the table they are manipulating and the fhir sequence. But
        // to avoid deadlocks, we only apply them after all the tables are done, so we make all
        // procedures depend on the allTablesComplete marker.
        final String ROOT_DIR = "db2/";
        ProcedureDef pd = model.addProcedure(this.schemaName,
                ADD_CODE_SYSTEM,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_CODE_SYSTEM.toLowerCase() + ".sql", null),
                Arrays.asList(fhirSequence, codeSystemsTable, allTablesComplete),
                procedurePrivileges);
        pd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        pd = model.addProcedure(this.schemaName,
                ADD_PARAMETER_NAME,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_PARAMETER_NAME.toLowerCase() + ".sql", null),
                Arrays.asList(fhirSequence, parameterNamesTable, allTablesComplete),
                procedurePrivileges);
        pd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        pd = model.addProcedure(this.schemaName,
                ADD_RESOURCE_TYPE,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_RESOURCE_TYPE.toLowerCase() + ".sql", null),
                Arrays.asList(fhirSequence, resourceTypesTable, allTablesComplete),
                procedurePrivileges);
        pd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        pd = model.addProcedure(this.schemaName,
                ADD_ANY_RESOURCE,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_ANY_RESOURCE.toLowerCase() + ".sql", null),
                Arrays.asList(fhirSequence, resourceTypesTable, allTablesComplete),
                procedurePrivileges);
        pd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
    }

    public void buildDatabaseSpecificArtifactsPostgres(PhysicalDataModel model) {
        // Add stored procedures/functions for postgresql.
        // Have to use different object names from DB2, because the group processing doesn't support 2 objects with the same name.
        final String ROOT_DIR = "postgres/";
        FunctionDef fd = model.addFunction(this.schemaName,
                ADD_CODE_SYSTEM,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_CODE_SYSTEM.toLowerCase() + ".sql", null),
                Arrays.asList(fhirSequence, codeSystemsTable, allTablesComplete),
                procedurePrivileges);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        fd = model.addFunction(this.schemaName,
                ADD_PARAMETER_NAME,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_PARAMETER_NAME.toLowerCase()
                        + ".sql", null),
                Arrays.asList(fhirSequence, parameterNamesTable, allTablesComplete), procedurePrivileges);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        fd = model.addFunction(this.schemaName,
                ADD_RESOURCE_TYPE,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_RESOURCE_TYPE.toLowerCase()
                        + ".sql", null),
                Arrays.asList(fhirSequence, resourceTypesTable, allTablesComplete), procedurePrivileges);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        fd = model.addFunction(this.schemaName,
                ADD_ANY_RESOURCE,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_ANY_RESOURCE.toLowerCase()
                        + ".sql", null),
                Arrays.asList(fhirSequence, resourceTypesTable, allTablesComplete), procedurePrivileges);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
    }

    /**
     * Add the system-wide logical_resources table. Note that LOGICAL_ID is
     * denormalized, stored in both LOGICAL_RESOURCES and <RESOURCE_TYPE>_LOGICAL_RESOURCES.
     * This avoids an additional join, and simplifies the migration to this
     * new schema model.
     * @param pdm
     */
    public void addLogicalResources(PhysicalDataModel pdm) {
        final String tableName = LOGICAL_RESOURCES;

        final String IDX_LOGICAL_RESOURCES_RITS = "IDX_" + LOGICAL_RESOURCES + "_RITS";

        Table tbl = Table.builder(schemaName, tableName)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(LOGICAL_RESOURCE_ID, false)
                .addIntColumn(RESOURCE_TYPE_ID, false)
                .addVarcharColumn(LOGICAL_ID, LOGICAL_ID_BYTES, false)
                .addTimestampColumn(REINDEX_TSTAMP, false, "CURRENT_TIMESTAMP") // new column for V0006
                .addBigIntColumn(REINDEX_TXID, false, "0")                      // new column for V0006
                .addPrimaryKey(tableName + "_PK", LOGICAL_RESOURCE_ID)
                .addUniqueIndex("UNQ_" + LOGICAL_RESOURCES, RESOURCE_TYPE_ID, LOGICAL_ID)
                .addIndex(IDX_LOGICAL_RESOURCES_RITS, new OrderedColumnDef(REINDEX_TSTAMP, OrderedColumnDef.Direction.DESC, null))
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addForeignKeyConstraint(FK + tableName + "_RTID", schemaName, RESOURCE_TYPES, RESOURCE_TYPE_ID)
                .enableAccessControl(this.sessionVariable)
                .setVersion(FhirSchemaVersion.V0006.vid())
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == FhirSchemaVersion.V0001.vid()) {
                        // Add statements to migrate from version V0001 to V0006 of this object
                        List<ColumnBase> cols = ColumnDefBuilder.builder()
                                .addTimestampColumn(REINDEX_TSTAMP, false, "CURRENT_TIMESTAMP")
                                .addBigIntColumn(REINDEX_TXID, false, "0")
                                .buildColumns();

                        statements.add(new AddColumn(schemaName, tableName, cols.get(0)));
                        statements.add(new AddColumn(schemaName, tableName, cols.get(1)));

                        // Add the new index on REINDEX_TSTAMP. This index is special because it's the
                        // first index in our schema to use DESC.
                        final String mtId = this.multitenant ? MT_ID : null;
                        List<OrderedColumnDef> indexCols = Arrays.asList(new OrderedColumnDef(REINDEX_TSTAMP, OrderedColumnDef.Direction.DESC, null));
                        statements.add(new CreateIndexStatement(schemaName, IDX_LOGICAL_RESOURCES_RITS, tableName, mtId, indexCols));
                    }
                    return statements;
                })
                .build(pdm);

        // TODO should not need to add as a table and an object. Get the table to add itself?
        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);
        pdm.addTable(tbl);
        pdm.addObject(tbl);
    }

    /**
     * Add the system-wide TOKEN_VALUES table which is used for
     * _tag and _security search properties in R4
     * @param pdm
     * @return Table the table that was added to the PhysicalDataModel
     */
    public Table addResourceTokenValues(PhysicalDataModel pdm) {

        final String tableName = TOKEN_VALUES;
        final int tvb = MAX_TOKEN_VALUE_BYTES;

        // logical_resources (0|1) ---- (*) token_values
        Table tbl = Table.builder(schemaName, tableName)
                .setTenantColumnName(MT_ID)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addIntColumn(        CODE_SYSTEM_ID,      false)
                .addVarcharColumn(       TOKEN_VALUE, tvb,  true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PNCSCV", PARAMETER_NAME_ID, CODE_SYSTEM_ID, TOKEN_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE_SYSTEM_ID, TOKEN_VALUE)
                .addForeignKeyConstraint(FK + tableName + "_CS", schemaName, CODE_SYSTEMS, CODE_SYSTEM_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(pdm);

        // TODO should not need to add as a table and an object. Get the table to add itself?
        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);
        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }

    /**
     * Adds the system level logical_resource_compartments table which identifies to
     * which compartments a give resource belongs. A resource may belong to many
     * compartments.
     * @param pdm
     * @return Table the table that was added to the PhysicalDataModel
     */
    public Table addLogicalResourceCompartments(PhysicalDataModel pdm) {

        final String tableName = LOGICAL_RESOURCE_COMPARTMENTS;

        // note COMPARTMENT_LOGICAL_RESOURCE_ID represents the compartment (e.g. the Patient)
        // that this resource exists within. This compartment resource may be a ghost resource...i.e. one
        // which has a record in LOGICAL_RESOURCES but currently does not have any resource
        // versions because we haven't yet loaded the resource itself. The timestamp is included
        // because it makes it very easy to find the most recent changes to resources associated with
        // a given patient (for example).
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0006.vid())
                .setTenantColumnName(MT_ID)
                .addIntColumn(     COMPARTMENT_NAME_ID,      false)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addTimestampColumn(LAST_UPDATED, false)
                .addBigIntColumn(COMPARTMENT_LOGICAL_RESOURCE_ID, false)
                .addUniqueIndex(IDX + tableName + "_LRNMLR", LOGICAL_RESOURCE_ID, COMPARTMENT_NAME_ID, COMPARTMENT_LOGICAL_RESOURCE_ID)
                .addUniqueIndex(IDX + tableName + "_NMCOMPLULR", COMPARTMENT_NAME_ID, COMPARTMENT_LOGICAL_RESOURCE_ID, LAST_UPDATED, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + tableName + "_COMP", schemaName, LOGICAL_RESOURCES, COMPARTMENT_LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(pdm);

        // TODO should not need to add as a table and an object. Get the table to add itself?
        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }



    /**
     * Add system-wide RESOURCE_STR_VALUES table to support _profile
     * properties (which are of type REFERENCE).
     * @param pdm
     * @return Table the table that was added to the PhysicalDataModel
     */
    public Table addResourceStrValues(PhysicalDataModel pdm) {
        final int msb = MAX_SEARCH_STRING_BYTES;

        Table tbl = Table.builder(schemaName, STR_VALUES)
                .setTenantColumnName(MT_ID)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addVarcharColumn(         STR_VALUE, msb,  true)
                .addVarcharColumn(   STR_VALUE_LCASE, msb,  true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + STR_VALUES + "_PSR", PARAMETER_NAME_ID, STR_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + STR_VALUES + "_PLR", PARAMETER_NAME_ID, STR_VALUE_LCASE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + STR_VALUES + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE)
                .addIndex(IDX + STR_VALUES + "_RPL", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE_LCASE)
                .addForeignKeyConstraint(FK + STR_VALUES + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + STR_VALUES + "_RID", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(pdm);

        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);
        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }

    /**
     * Add the table for data search parameters at the (system-wide) resource level
     * @param model
     * @return Table the table that was added to the PhysicalDataModel
     */
    public Table addResourceDateValues(PhysicalDataModel model) {
        final String tableName = DATE_VALUES;
        final String logicalResourcesTable = LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(2)
                .setTenantColumnName(MT_ID)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addTimestampColumn(      DATE_START,6,    true)
                .addTimestampColumn(        DATE_END,6,    true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PSER", PARAMETER_NAME_ID, DATE_START, DATE_END, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PESR", PARAMETER_NAME_ID, DATE_END, DATE_START, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPSE", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, DATE_START, DATE_END)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_R", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == 1) {
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_PVR"));
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_RPV"));
                        statements.add(new DropColumn(schemaName, tableName, DATE_VALUE_DROPPED_COLUMN));
                    }
                    return statements;
                })
                .build(model);

        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);
        model.addTable(tbl);
        model.addObject(tbl);

        return tbl;
    }


    /**
     * <pre>
        CREATE TABLE resource_types (
            resource_type_id INT NOT NULL
            CONSTRAINT pk_resource_type PRIMARY KEY,
            resource_type   VARCHAR(64) NOT NULL
        );

        -- make sure resource_type values are unique
        CREATE UNIQUE INDEX unq_resource_types_rt ON resource_types(resource_type);
        </pre>
     *
     * @param model
     */
    protected void addResourceTypes(PhysicalDataModel model) {

        resourceTypesTable = Table.builder(schemaName, RESOURCE_TYPES)
                .setTenantColumnName(MT_ID)
                .addIntColumn(    RESOURCE_TYPE_ID,      false)
                .addVarcharColumn(   RESOURCE_TYPE,  64, false)
                .addUniqueIndex(IDX + "unq_resource_types_rt", RESOURCE_TYPE)
                .addPrimaryKey(RESOURCE_TYPES + "_PK", RESOURCE_TYPE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(model);

        // TODO Table should be immutable, so add support to the Builder for this
        this.resourceTypesTable.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(resourceTypesTable);
        model.addTable(resourceTypesTable);
        model.addObject(resourceTypesTable);
    }

    /**
     * Add the collection of tables for each of the listed
     * FHIR resource types
     * @param model
     */
    protected void addResourceTables(PhysicalDataModel model, IDatabaseObject... dependency) {
        if (this.sessionVariable == null) {
            throw new IllegalStateException("Session variable must be defined before adding resource tables");
        }

        // The sessionVariable is used to enable access control on every table, so we
        // provide it as a dependency
        FhirResourceTableGroup455 frg = new FhirResourceTableGroup455(model, this.schemaName, this.multitenant, sessionVariable, this.procedureDependencies, this.fhirTablespace, this.resourceTablePrivileges);
        for (String resourceType: this.resourceTypes) {
            ObjectGroup group = frg.addResourceType(resourceType);
            group.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

            // Add additional dependencies the group doesn't yet know about
            group.addDependencies(Arrays.asList(this.codeSystemsTable, this.parameterNamesTable, this.resourceTypesTable, this.commonTokenValuesTable));

            // Add all other dependencies that were explicitly passed
            group.addDependencies(Arrays.asList(dependency));

            // Make this group a dependency for all the stored procedures.
            this.procedureDependencies.add(group);
            model.addObject(group);
        }
    }

    /**
     *
     *
    CREATE TABLE parameter_names (
      parameter_name_id INT NOT NULL
                    CONSTRAINT pk_parameter_name PRIMARY KEY,
      parameter_name   VARCHAR(255 OCTETS) NOT NULL
    );

    CREATE UNIQUE INDEX unq_parameter_name_rtnm ON parameter_names(parameter_name) INCLUDE (parameter_name_id);

     * @param model
     */
    protected void addParameterNames(PhysicalDataModel model) {

        // The index which also used by the database to support the primary key constraint
        String[] prfIndexCols = {PARAMETER_NAME};
        String[] prfIncludeCols = {PARAMETER_NAME_ID};

        parameterNamesTable = Table.builder(schemaName, PARAMETER_NAMES)
                .setTenantColumnName(MT_ID)
                .addIntColumn(     PARAMETER_NAME_ID,              false)
                .addVarcharColumn(    PARAMETER_NAME,         255, false)
                .addUniqueIndex(IDX + "PARAMETER_NAME_RTNM", Arrays.asList(prfIndexCols), Arrays.asList(prfIncludeCols))
                .addPrimaryKey(PARAMETER_NAMES + "_PK", PARAMETER_NAME_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(model);

        this.parameterNamesTable.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(parameterNamesTable);

        model.addTable(parameterNamesTable);
        model.addObject(parameterNamesTable);
    }

    /**
     * Add the code_systems table to the database schema
    CREATE TABLE code_systems (
      code_system_id         INT NOT NULL
       CONSTRAINT pk_code_system PRIMARY KEY,
      code_system_name       VARCHAR(255 OCTETS) NOT NULL
    );

    CREATE UNIQUE INDEX unq_code_system_cinm ON code_systems(code_system_name);

     * @param model
     */
    protected void addCodeSystems(PhysicalDataModel model) {

        codeSystemsTable = Table.builder(schemaName, CODE_SYSTEMS)
                .setTenantColumnName(MT_ID)
                .addIntColumn(      CODE_SYSTEM_ID,         false)
                .addVarcharColumn(CODE_SYSTEM_NAME,    255, false)
                .addUniqueIndex(IDX + "CODE_SYSTEM_CINM", CODE_SYSTEM_NAME)
                .addPrimaryKey(CODE_SYSTEMS + "_PK", CODE_SYSTEM_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(model);

        this.codeSystemsTable.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(codeSystemsTable);
        model.addTable(codeSystemsTable);
        model.addObject(codeSystemsTable);

    }

    /**
     * Table used to store normalized values for tokens, shared by all the
     * <RESOURCE_TYPE>_TOKEN_VALUES tables. Although this requires an additional
     * join, it cuts down on space by avoiding repeating long strings (e.g. urls).
     * This also helps to reduce the total sizes of the indexes, helping to improve
     * cache hit rates for a given buffer cache size.
     * Token values may or may not have an associated code system, in which case,
     * it assigned a default system. This is why CODE_SYSTEM_ID is not nullable and
     * has a FK constraint.
     *
     * We never need to find all token values for a given code-system, so there's no need
     * for a second index (CODE_SYSTEM_ID, TOKEN_VALUE). Do not add it.
     *
     * Because different parameter names may reference the same token value (e.g.
     * 'Observation.subject' and 'Claim.patient' are both patient references), the
     * common token value is not distinguished by a parameter_name_id.
     *
     * Where common token values are used to represent local relationships between two resources,
     * the code_system encodes the resource type of the referenced resource and
     * the token_value represents its logical_id. This approach simplifies query writing when
     * following references.
     *
     * @param pdm
     * @return the table definition
     */
    public void addCommonTokenValues(PhysicalDataModel pdm) {
        final String tableName = COMMON_TOKEN_VALUES;
        commonTokenValuesTable = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0006.vid())
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(     COMMON_TOKEN_VALUE_ID,                          false)
                .setIdentityColumn(   COMMON_TOKEN_VALUE_ID, Generated.ALWAYS)
                .addIntColumn(               CODE_SYSTEM_ID,                          false)
                .addVarcharColumn(              TOKEN_VALUE, MAX_TOKEN_VALUE_BYTES,   false)
                .addUniqueIndex(IDX + tableName + "_TVCP", TOKEN_VALUE, CODE_SYSTEM_ID)
                .addPrimaryKey(tableName + "_PK", COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_CSID", schemaName, CODE_SYSTEMS, CODE_SYSTEM_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(pdm);

        // TODO should not need to add as a table and an object. Get the table to add itself?
        commonTokenValuesTable.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        pdm.addTable(commonTokenValuesTable);
        pdm.addObject(commonTokenValuesTable);
    }

    /**
     * Add the system-wide RESOURCE_TOKEN_REFS table which is used for
     * _tag and _security search properties in R4 (new table
     * for issue #1366 V0006 schema change). Replaces the
     * previous TOKEN_VALUES table. All token values are now
     * normalized in the COMMON_TOKEN_VALUES table
     * @param pdm
     * @return Table the table that was added to the PhysicalDataModel
     */
    public Table addResourceTokenRefs(PhysicalDataModel pdm) {

        final String tableName = RESOURCE_TOKEN_REFS;

        // logical_resources (0|1) ---- (*) resource_token_refs
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0006.vid())
                .setTenantColumnName(MT_ID)
                .addIntColumn(       PARAMETER_NAME_ID,    false)
                .addBigIntColumn(COMMON_TOKEN_VALUE_ID,     true) // support for null token value entries
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,    false)
                .addIntColumn(          REF_VERSION_ID,     true) // for when the referenced value is a logical resource with a version
                .addIndex(IDX + tableName + "_TVLR", COMMON_TOKEN_VALUE_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_LRTV", LOGICAL_RESOURCE_ID, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_CTV", schemaName, COMMON_TOKEN_VALUES, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + tableName + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .build(pdm);

        // TODO should not need to add as a table and an object. Get the table to add itself?
        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);
        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }

    /**
     * <pre>
    CREATE SEQUENCE fhir_sequence
             AS BIGINT
     START WITH 1
          CACHE 20000
       NO CYCLE;
     * </pre>
     *
     * @param pdm
     */
    protected void addFhirSequence(PhysicalDataModel pdm) {
        this.fhirSequence = new Sequence(schemaName, FHIR_SEQUENCE, FhirSchemaVersion.V0001.vid(), 1, 1000);
        this.fhirSequence.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        procedureDependencies.add(fhirSequence);
        sequencePrivileges.forEach(p -> p.addToObject(fhirSequence));

        pdm.addObject(fhirSequence);
    }

    protected void addFhirRefSequence(PhysicalDataModel pdm) {
        this.fhirRefSequence = new Sequence(schemaName, FHIR_REF_SEQUENCE, FhirSchemaVersion.V0001.vid(), FhirSchemaConstants.FHIR_REF_SEQUENCE_START, FhirSchemaConstants.FHIR_REF_SEQUENCE_CACHE);
        this.fhirRefSequence.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        procedureDependencies.add(fhirRefSequence);
        sequencePrivileges.forEach(p -> p.addToObject(fhirRefSequence));
        pdm.addObject(fhirRefSequence);

        // Schema V0003 does an alter to bump up the start value of the reference sequence
        // to avoid a conflict with parameter names not in the pre-populated set
        // fix for issue-1263. This will only be applied if the current version of the
        // the FHIR_REF_SEQUENCE is <= 2.
        BaseObject alter = new AlterSequenceStartWith(schemaName, FHIR_REF_SEQUENCE, FhirSchemaVersion.V0003.vid(),
            FhirSchemaConstants.FHIR_REF_SEQUENCE_START, FhirSchemaConstants.FHIR_REF_SEQUENCE_CACHE, 1);
        alter.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        procedureDependencies.add(alter);
        alter.addDependency(fhirRefSequence); // only alter after the sequence is initially created

        // Because the sequence might be dropped and recreated, we need to inject privileges
        // so that they are applied when this ALTER SEQUENCE is processed.
        sequencePrivileges.forEach(p -> p.addToObject(alter));
        pdm.addObject(alter);
    }


    /**
     * Add the sequence used by the new local/external references data model
     * @param pdm
     */
    protected void addReferencesSequence(PhysicalDataModel pdm) {
        Sequence seq = new Sequence(schemaName, FhirSchemaConstants.REFERENCES_SEQUENCE, FhirSchemaVersion.V0001.vid(), FhirSchemaConstants.REFERENCES_SEQUENCE_START, FhirSchemaConstants.REFERENCES_SEQUENCE_CACHE, FhirSchemaConstants.REFERENCES_SEQUENCE_INCREMENT);
        seq.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        procedureDependencies.add(seq);
        sequencePrivileges.forEach(p -> p.addToObject(seq));
        pdm.addObject(seq);
    }
}
