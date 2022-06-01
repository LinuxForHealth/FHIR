/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.CANONICAL_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CANONICAL_URL_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CHANGE_TSTAMP;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CHANGE_TYPE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEMS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEM_NAME;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMMON_CANONICAL_VALUES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMMON_TOKEN_VALUES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMMON_TOKEN_VALUE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMPARTMENT_LOGICAL_RESOURCE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.COMPARTMENT_NAME_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_END;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_START;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_VALUES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_VALUE_DROPPED_COLUMN;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.ERASED_RESOURCES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.ERASED_RESOURCE_GROUP_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FHIR_CHANGE_SEQUENCE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FHIR_REF_SEQUENCE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FHIR_SEQUENCE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FK;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FRAGMENT;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FRAGMENT_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.IDX;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.IS_DELETED;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LAST_UPDATED;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_COMPARTMENTS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_IDENT;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_PROFILES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_SECURITY;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_TAGS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MAX_SEARCH_STRING_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MAX_TOKEN_VALUE_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MT_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_HASH;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_HASH_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAME;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAMES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAME_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.REF_VERSION_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.REINDEX_TSTAMP;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.REINDEX_TXID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_CHANGE_LOG;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_ID;
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
import static com.ibm.fhir.schema.control.FhirSchemaConstants.URL;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.VERSION;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.VERSION_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.VERSION_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.DistributionType;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.common.AddColumn;
import com.ibm.fhir.database.utils.common.CreateIndexStatement;
import com.ibm.fhir.database.utils.common.DropColumn;
import com.ibm.fhir.database.utils.common.DropIndex;
import com.ibm.fhir.database.utils.common.DropTable;
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
import com.ibm.fhir.database.utils.model.With;
import com.ibm.fhir.database.utils.postgres.PostgresFillfactorSettingDAO;
import com.ibm.fhir.database.utils.postgres.PostgresVacuumSettingDAO;
import com.ibm.fhir.model.util.ModelSupport;

/**
 * Encapsulates the generation of the FHIR schema artifacts
 */
public class FhirSchemaGenerator {
    private static final Logger logger = Logger.getLogger(FhirSchemaGenerator.class.getName());

    // The schema holding all the data-bearing tables
    private final String schemaName;

    // The schema used for administration objects like the tenants table, variable etc
    private final String adminSchemaName;

    // Which variant of the schema do we want to build
    private final SchemaType schemaType;

    // No abstract types
    private static final Set<String> ALL_RESOURCE_TYPES = ModelSupport.getResourceTypes(false).stream()
            .map(t -> ModelSupport.getTypeName(t).toUpperCase())
            .collect(Collectors.toSet());

    private static final String ADD_CODE_SYSTEM = "ADD_CODE_SYSTEM";
    private static final String ADD_PARAMETER_NAME = "ADD_PARAMETER_NAME";
    private static final String ADD_RESOURCE_TYPE = "ADD_RESOURCE_TYPE";
    private static final String ADD_ANY_RESOURCE = "ADD_ANY_RESOURCE";
    
    // Special procedure for Citus database support
    private static final String ADD_LOGICAL_RESOURCE = "ADD_LOGICAL_RESOURCE";
    private static final String DELETE_RESOURCE_PARAMETERS = "DELETE_RESOURCE_PARAMETERS";
    private static final String ERASE_RESOURCE = "ERASE_RESOURCE";

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

    // The sequence for tracking change history in distributed schemas like Citus
    private Sequence fhirChangeSequence;

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
    public FhirSchemaGenerator(String adminSchemaName, String schemaName, SchemaType schemaType) {
        this(adminSchemaName, schemaName, schemaType, ALL_RESOURCE_TYPES);
    }

    /**
     * Generate the IBM FHIR Server Schema with just the given resourceTypes
     *
     * @param adminSchemaName
     * @param schemaName
     */
    public FhirSchemaGenerator(String adminSchemaName, String schemaName, SchemaType schemaType, Set<String> resourceTypes) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName = schemaName;
        this.schemaType = schemaType;

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
        addLogicalResourceIdent(model);
        addLogicalResources(model); // for system-level parameter search
        addReferencesSequence(model);
        addLogicalResourceCompartments(model);
        addResourceChangeLog(model); // track changes for easier export
        addCommonCanonicalValues(model);   // V0014
        addLogicalResourceProfiles(model); // V0014
        addLogicalResourceTags(model);     // V0014
        addLogicalResourceSecurity(model); // V0016
        addErasedResources(model);  // V0023

        Table globalStrValues = addResourceStrValues(model); // for system-level _profile parameters
        Table globalDateValues = addResourceDateValues(model); // for system-level date parameters

        // new normalized table for supporting token data (replaces TOKEN_VALUES)
        Table globalResourceTokenRefs = addResourceTokenRefs(model);

        // The three "global" tables aren't true dependencies, but this was the easiest way to force sequential processing
        // and avoid a pesky deadlock issue we were hitting while adding foreign key constraints on the global tables
        addResourceTables(model, globalStrValues, globalDateValues, globalResourceTokenRefs);

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
            DELETE_RESOURCE_PARAMETERS,
            FhirSchemaVersion.V0020.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + DELETE_RESOURCE_PARAMETERS.toLowerCase() + ".sql", null),
            Arrays.asList(fhirSequence, resourceTypesTable, allTablesComplete),
            procedurePrivileges);
        pd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        final ProcedureDef deleteResourceParameters = pd;

        pd = model.addProcedure(this.schemaName,
                ADD_ANY_RESOURCE,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ADD_ANY_RESOURCE.toLowerCase() + ".sql", null),
                Arrays.asList(fhirSequence, resourceTypesTable, deleteResourceParameters, allTablesComplete),
                procedurePrivileges);
        pd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        pd = model.addProcedure(this.schemaName,
            ERASE_RESOURCE,
            FhirSchemaVersion.V0013.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ERASE_RESOURCE.toLowerCase() + ".sql", null),
            Arrays.asList(fhirSequence, resourceTypesTable, deleteResourceParameters, allTablesComplete),
            procedurePrivileges);
        pd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
    }

    public void buildDatabaseSpecificArtifactsPostgres(PhysicalDataModel model) {
        // Add stored procedures/functions for PostgreSQL
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

        // We currently only support functions with PostgreSQL, although this is really just a procedure
        final String deleteResourceParametersScript;
        final String addAnyResourceScript;
        final String eraseResourceScript;
        final String schemaTypeSuffix = getSchemaTypeSuffix();
        addAnyResourceScript = ROOT_DIR + ADD_ANY_RESOURCE.toLowerCase() + schemaTypeSuffix;
        deleteResourceParametersScript = ROOT_DIR + DELETE_RESOURCE_PARAMETERS.toLowerCase() + ".sql";
        eraseResourceScript = ROOT_DIR + ERASE_RESOURCE.toLowerCase() + ".sql";

        FunctionDef deleteResourceParameters = model.addFunction(this.schemaName,
            DELETE_RESOURCE_PARAMETERS,
            FhirSchemaVersion.V0020.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, deleteResourceParametersScript, null),
            Arrays.asList(fhirSequence, resourceTypesTable, allTablesComplete),
            procedurePrivileges);
        deleteResourceParameters.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        fd = model.addFunction(this.schemaName,
                ADD_ANY_RESOURCE,
                FhirSchemaVersion.V0001.vid(),
                () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, addAnyResourceScript, null),
                Arrays.asList(fhirSequence, resourceTypesTable, deleteResourceParameters, allTablesComplete), procedurePrivileges);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        fd = model.addFunction(this.schemaName,
            ERASE_RESOURCE,
            FhirSchemaVersion.V0013.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, eraseResourceScript, null),
            Arrays.asList(fhirSequence, resourceTypesTable, deleteResourceParameters, allTablesComplete), procedurePrivileges);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
    }

    /**
     * Get the suffix to select the appropriate procedure/function script 
     * for the schema type
     * @return
     */
    private String getSchemaTypeSuffix() {
        switch (this.schemaType) {
        case DISTRIBUTED:
            return "_distributed.sql";
        case SHARDED:
            return "_sharded.sql";
        default:
            return ".sql";
        }
    }

    /**
     * @implNote following the current pattern, which is why all this stuff is replicated
     * @param model
     */
    public void buildDatabaseSpecificArtifactsCitus(PhysicalDataModel model) {
        // Add stored procedures/functions for postgresql and Citus
        // Have to use different object names from DB2, because the group processing doesn't support 2 objects with the same name.
        final String ROOT_DIR = "postgres/";
        final String CITUS_ROOT_DIR = "citus/";
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

        // Add the delete resource parameters function and distribute using logical_resource_id (param $2)
        FunctionDef deleteResourceParameters = model.addFunction(this.schemaName,
            DELETE_RESOURCE_PARAMETERS,
            FhirSchemaVersion.V0020.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + DELETE_RESOURCE_PARAMETERS.toLowerCase() + ".sql", null),
            Arrays.asList(fhirSequence, resourceTypesTable, allTablesComplete),
            procedurePrivileges, 2);
        deleteResourceParameters.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        // Use the Citus-specific function which is distributed using logical_resource_id (param $1)
        fd = model.addFunction(this.schemaName, ADD_LOGICAL_RESOURCE,
            FhirSchemaVersion.V0001.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, CITUS_ROOT_DIR + ADD_LOGICAL_RESOURCE.toLowerCase() + ".sql", null),
            Arrays.asList(fhirSequence, resourceTypesTable, deleteResourceParameters, allTablesComplete),
            procedurePrivileges, 1);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        final FunctionDef addLogicalResource = fd;

        // Use the Citus-specific variant of add_any_resource and distribute using logical_resource_id (param $1)
        fd = model.addFunction(this.schemaName, ADD_ANY_RESOURCE,
            FhirSchemaVersion.V0001.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, CITUS_ROOT_DIR + ADD_ANY_RESOURCE.toLowerCase()
                    + ".sql", null),
            Arrays.asList(fhirSequence, resourceTypesTable, deleteResourceParameters, allTablesComplete, addLogicalResource),
            procedurePrivileges, 1);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);

        fd = model.addFunction(this.schemaName,
            ERASE_RESOURCE,
            FhirSchemaVersion.V0013.vid(),
            () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ROOT_DIR + ERASE_RESOURCE.toLowerCase() + ".sql", null),
            Arrays.asList(fhirSequence, resourceTypesTable, deleteResourceParameters, allTablesComplete), procedurePrivileges);
        fd.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
    }

    /**
     * Are we building the Db2-specific multitenant schema variant
     * @return
     */
    private boolean isMultitenant() {
        return this.schemaType == SchemaType.MULTITENANT;
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
        final String mtId = isMultitenant() ? MT_ID : null;

        final String IDX_LOGICAL_RESOURCES_RITS = "IDX_" + LOGICAL_RESOURCES + "_RITS";
        final String IDX_LOGICAL_RESOURCES_LUPD = "IDX_" + LOGICAL_RESOURCES + "_LUPD";

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addBigIntColumn(LOGICAL_RESOURCE_ID, false)
                .addIntColumn(RESOURCE_TYPE_ID, false)
                .addVarcharColumn(LOGICAL_ID, LOGICAL_ID_BYTES, false)
                .addTimestampColumn(REINDEX_TSTAMP, false, "CURRENT_TIMESTAMP") // new column for V0006
                .addBigIntColumn(REINDEX_TXID, false, "0")                      // new column for V0006
                .addTimestampColumn(LAST_UPDATED, true)                         // new column for V0014
                .addCharColumn(IS_DELETED, 1, false, "'X'")
                .addVarcharColumn(PARAMETER_HASH, PARAMETER_HASH_BYTES, true)   // new column for V0015
                .addPrimaryKey(tableName + "_PK", LOGICAL_RESOURCE_ID)
                .addUniqueIndex("UNQ_" + LOGICAL_RESOURCES, RESOURCE_TYPE_ID, LOGICAL_ID)
                .addIndex(IDX_LOGICAL_RESOURCES_RITS, new OrderedColumnDef(REINDEX_TSTAMP, OrderedColumnDef.Direction.DESC, null))
                .addIndex(IDX_LOGICAL_RESOURCES_LUPD, new OrderedColumnDef(LAST_UPDATED, OrderedColumnDef.Direction.ASC, null))
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addForeignKeyConstraint(FK + tableName + "_RTID", schemaName, RESOURCE_TYPES, RESOURCE_TYPE_ID)
                .enableAccessControl(this.sessionVariable)
                .addWiths(addWiths()) // add table tuning
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
                        List<OrderedColumnDef> indexCols = Arrays.asList(new OrderedColumnDef(REINDEX_TSTAMP, OrderedColumnDef.Direction.DESC, null));
                        statements.add(new CreateIndexStatement(schemaName, IDX_LOGICAL_RESOURCES_RITS, tableName, mtId, indexCols));
                    }

                    if (priorVersion < FhirSchemaVersion.V0009.vid()) {
                        // Get rid of the old global token values parameter table which no longer
                        // used
                        statements.add(new DropTable(schemaName, "TOKEN_VALUES"));
                    }

                    if (priorVersion < FhirSchemaVersion.V0014.vid()) {
                        // Add LAST_UPDATED and IS_DELETED to whole-system logical_resources
                        List<ColumnBase> cols = ColumnDefBuilder.builder()
                                .addTimestampColumn(LAST_UPDATED, true)
                                .addCharColumn(IS_DELETED, 1, false, "'X'")
                                .buildColumns();

                        statements.add(new AddColumn(schemaName, tableName, cols.get(0)));
                        statements.add(new AddColumn(schemaName, tableName, cols.get(1)));

                        // New index on the LAST_UPDATED. We don't need to include resource-type. If
                        // you know the resource type, you'll be querying the resource-specific
                        // xx_logical_resources table instead
                        List<OrderedColumnDef> indexCols = Arrays.asList(new OrderedColumnDef(LAST_UPDATED, OrderedColumnDef.Direction.ASC, null));
                        statements.add(new CreateIndexStatement(schemaName, IDX_LOGICAL_RESOURCES_LUPD, tableName, mtId, indexCols));
                    }

                    if (priorVersion < FhirSchemaVersion.V0015.vid()) {
                        // Add PARAM_HASH logical_resources
                        List<ColumnBase> cols = ColumnDefBuilder.builder()
                                .addVarcharColumn(PARAMETER_HASH, PARAMETER_HASH_BYTES, true)
                                .buildColumns();
                        statements.add(new AddColumn(schemaName, tableName, cols.get(0)));
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }

                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
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
     * Adds a table to support logical identity management of resources
     * when using a distributed RDBMS such as Citus. It represents the
     * mapping:
     * 
     * (RESOURCE_TYPE_ID, LOGICAL_ID) -> (LOGICAL_RESOURCE_ID)
     * 
     * LOGICAL_RESOURCE_ID values are assigned from the sequence FHIR_SEQUENCE.
     * 
     * When using Citus (or similar), this table is distributed by LOGICAL_ID,
     * which means we can use a primary key of {RESOURCE_TYPE_ID, LOGICAL_ID}. 
     * This is required to ensure that we can lock the logical resource to 
     * avoid any concurrency issues.
     * 
     * LOGICAL_RESOURCE_IDENT records are also generated when the tuple 
     * (RESOURCE_TYPE_ID. LOGICAL_ID) is used as a local resource reference 
     * value. For example:
     *   "reference": "Patient/aPatientId"
     * will create a new LOGICAL_RESOURCE_IDENT record if the Patient resource
     * "aPatientId" has not yet been created. The LOGICAL_RESOURCES record is 
     * not created until the actual resource is created.
     * 
     * Note that there's no index on LOGICAL_RESOURCE_ID. This is intentional. 
     * An index is not required because LOGICAL_RESOURCE_ID is never used as an
     * access path for this table.
     * 
     * @param pdm
     */
    private void addLogicalResourceIdent(PhysicalDataModel pdm) {
        final String tableName = LOGICAL_RESOURCE_IDENT;
        final String mtId = isMultitenant() ? MT_ID : null;

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // add support for distribution/sharding
                .setTenantColumnName(mtId)
                .setDistributionType(DistributionType.DISTRIBUTED)
                .setDistributionColumnName(LOGICAL_ID)             // override distribution column for this table
                .addIntColumn(RESOURCE_TYPE_ID, false)
                .addVarcharColumn(LOGICAL_ID, MAX_SEARCH_STRING_BYTES, false) // used to also store absolute reference values
                .addBigIntColumn(LOGICAL_RESOURCE_ID, false)
                .addPrimaryKey(tableName + "_PK", LOGICAL_ID, RESOURCE_TYPE_ID) // do not change this order
                .addIndex("IDX_" + LOGICAL_RESOURCE_IDENT + "_LRID", LOGICAL_RESOURCE_ID) // non-unique to allow easy distribution
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addForeignKeyConstraint(FK + tableName + "_RTID", schemaName, RESOURCE_TYPES, RESOURCE_TYPE_ID)
                .enableAccessControl(this.sessionVariable)
                .addWiths(addWiths()) // add table tuning
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    // NOP for now
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
     * Create the COMMON_CANONICAL_VALUES table. Used from schema V0014 to normalize
     * meta.profile search parameters (similar to common_token_values). Only the url
     * is included by design. The (optional) version and fragment values are stored
     * in the parameter mapping table (logical_resource_profiles) in order to support
     * inequalities on version while still using a literal CANONICAL_ID = x predicate.
     * These canonical ids are cached in the server, so search queries won't need to
     * join to this table. The URL is typically a long string, so by normalizing and
     * storing/indexing it once, we reduce space consumption.
     * @param pdm
     */
    public void addCommonCanonicalValues(PhysicalDataModel pdm) {
        final String tableName = COMMON_CANONICAL_VALUES;
        final String unqCanonicalUrl = "UNQ_" + tableName + "_URL";
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .setDistributionType(DistributionType.REFERENCE) // V0027 support for sharding
                .addBigIntColumn(CANONICAL_ID, false)
                .addVarcharColumn(URL, CANONICAL_URL_BYTES, false)
                .addPrimaryKey(tableName + "_PK", CANONICAL_ID)
                .addUniqueIndex(unqCanonicalUrl, URL)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    // Intentionally NOP
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
     * A single-parameter table supporting _profile search parameter values
     * Add the LOGICAL_RESOURCE_PROFILES table to the given {@link PhysicalDataModel}.
     * This table maps logical resources to meta.profile values stored as canonical URIs
     * in COMMON_CANONICAL_VALUES. Canonical values can include optional version and fragment
     * values as described here: https://www.hl7.org/fhir/datatypes.html#canonical
     * @param pdm
     * @return
     */
    public Table addLogicalResourceProfiles(PhysicalDataModel pdm) {

        final String tableName = LOGICAL_RESOURCE_PROFILES;

        // logical_resources (1) ---- (*) logical_resource_profiles (*) ---- (1) common_canonical_values
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addBigIntColumn(         CANONICAL_ID,     false) // FK referencing COMMON_CANONICAL_VALUES
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,     false) // FK referencing LOGICAL_RESOURCES
                .addVarcharColumn(             VERSION,  VERSION_BYTES, true)
                .addVarcharColumn(            FRAGMENT, FRAGMENT_BYTES, true)
                .addIndex(IDX + tableName + "_CCVLR", CANONICAL_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_LRCCV", LOGICAL_RESOURCE_ID, CANONICAL_ID)
                .addForeignKeyConstraint(FK + tableName + "_CCV", schemaName, COMMON_CANONICAL_VALUES, CANONICAL_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .addWiths(addWiths()) // New Column for V0017
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(pdm);

        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);
        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }

    /**
     * A single-parameter table supporting _tag search parameter values.
     * Tags are tokens, but because they may not be very selective we use a
     * separate table in order to avoid messing up cardinality estimates
     * in the query optimizer.
     * @param pdm
     * @return
     */
    public Table addLogicalResourceTags(PhysicalDataModel pdm) {

        final String tableName = LOGICAL_RESOURCE_TAGS;

        // logical_resources (1) ---- (*) logical_resource_tags (*) ---- (1) common_token_values
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addBigIntColumn(COMMON_TOKEN_VALUE_ID,    false) // FK referencing COMMON_CANONICAL_VALUES
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,    false) // FK referencing LOGICAL_RESOURCES
                .addIndex(IDX + tableName + "_CCVLR", COMMON_TOKEN_VALUE_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_LRCCV", LOGICAL_RESOURCE_ID, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_CTV", schemaName, COMMON_TOKEN_VALUES, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .addWiths(addWiths()) // New Column for V0017
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(pdm);

        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);

        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }

    /**
     * Add the dedicated common_token_values mapping table for security search parameters
     * @param pdm
     * @return
     */
    public Table addLogicalResourceSecurity(PhysicalDataModel pdm) {
        final String tableName = LOGICAL_RESOURCE_SECURITY;

        // logical_resources (1) ---- (*) logical_resource_security (*) ---- (1) common_token_values
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addBigIntColumn(COMMON_TOKEN_VALUE_ID,    false) // FK referencing COMMON_CANONICAL_VALUES
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,    false) // FK referencing LOGICAL_RESOURCES
                .addIndex(IDX + tableName + "_CCVLR", COMMON_TOKEN_VALUE_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_LRCCV", LOGICAL_RESOURCE_ID, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_CTV", schemaName, COMMON_TOKEN_VALUES, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .addWiths(addWiths()) // New Column for V0017
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(pdm);

        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);

        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }

    /**
     * Add the resource_change_log table. This table supports tracking of every change made
     * to a resource at the global level, making it much easier to stream a list of changes
     * from a known point.
     * @param pdm
     */
    public void addResourceChangeLog(PhysicalDataModel pdm) {
        final String tableName = RESOURCE_CHANGE_LOG;

        // custom list of Withs because this table does not require fillfactor tuned in V0020
        List<With> customWiths = Arrays.asList(
            With.with("autovacuum_vacuum_scale_factor", "0.01"), // V0019
            With.with("autovacuum_vacuum_threshold", "1000"),    // V0019
            With.with("autovacuum_vacuum_cost_limit", "2000")    // V0019
            );

        // Each shard gets its own history
        Table tbl = Table.builder(schemaName, tableName)
                .setTenantColumnName(MT_ID)
                .setVersion(FhirSchemaVersion.V0019.vid()) // V0019: Updated to support Postgres vacuum changes
                .setDistributionType(DistributionType.NONE) // don't distribute the history log
                .addBigIntColumn(RESOURCE_ID, false)
                .addIntColumn(RESOURCE_TYPE_ID, false)
                .addBigIntColumn(LOGICAL_RESOURCE_ID, false)
                .addTimestampColumn(CHANGE_TSTAMP, false)
                .addIntColumn(VERSION_ID, false)
                .addCharColumn(CHANGE_TYPE, 1, false)
                .addPrimaryKey(tableName + "_PK", RESOURCE_ID)
                .addUniqueIndex("UNQ_" + RESOURCE_CHANGE_LOG + "_CTRTRI", CHANGE_TSTAMP, RESOURCE_TYPE_ID, RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .addWiths(customWiths) // Does not require fillfactor tuning
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
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
                .setCreate(false) // V0027 no longer used
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
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
                .addWiths(addWiths()) // New Column for V0017
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    if (priorVersion < FhirSchemaVersion.V0027.vid()) {
                        // This table is never used and the FK_LOGICAL_RESOURCE_COMPARTMENTS_COMP FK
                        // causes issues with Citus distribution, so it's time for it to go
                        statements.add(new DropTable(schemaName, tableName));
                    }
                    return statements;
                })
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
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
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
                .addWiths(addWiths()) // New Column for V0017
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, STR_VALUES, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, STR_VALUES, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
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
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
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
                .addWiths(addWiths()) // New Column for V0017
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == 1) {
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_PVR"));
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_RPV"));
                        statements.add(new DropColumn(schemaName, tableName, DATE_VALUE_DROPPED_COLUMN));
                    }
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
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
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .addIntColumn(    RESOURCE_TYPE_ID,      false)
                .addVarcharColumn(   RESOURCE_TYPE,  64, false)
                .addUniqueIndex(IDX + "unq_resource_types_rt", RESOURCE_TYPE)
                .addPrimaryKey(RESOURCE_TYPES + "_PK", RESOURCE_TYPE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .setDistributionType(DistributionType.REFERENCE) // V0027 supporting for sharding
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    // Intentionally a NOP
                    return statements;
                })
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
        FhirResourceTableGroup frg = new FhirResourceTableGroup(model, this.schemaName, isMultitenant(), sessionVariable,
                this.procedureDependencies, this.fhirTablespace, this.resourceTablePrivileges, addWiths());
        for (String resourceType: this.resourceTypes) {

            resourceType = resourceType.toUpperCase().trim();
            if (!ALL_RESOURCE_TYPES.contains(resourceType.toUpperCase())) {
                logger.warning("Passed resource type '" + resourceType + "' does not match any known FHIR resource types; creating anyway");
            }

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
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .addIntColumn(     PARAMETER_NAME_ID,              false)
                .addVarcharColumn(    PARAMETER_NAME,         255, false)
                .addUniqueIndex(IDX + "PARAMETER_NAME_RTNM", Arrays.asList(prfIndexCols), Arrays.asList(prfIncludeCols))
                .addPrimaryKey(PARAMETER_NAMES + "_PK", PARAMETER_NAME_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .setDistributionType(DistributionType.REFERENCE) // V0027 supporting for sharding
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    // Intentionally a NOP
                    return statements;
                })
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
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .addIntColumn(      CODE_SYSTEM_ID,         false)
                .addVarcharColumn(CODE_SYSTEM_NAME,    255, false)
                .addUniqueIndex(IDX + "CODE_SYSTEM_CINM", CODE_SYSTEM_NAME)
                .addPrimaryKey(CODE_SYSTEMS + "_PK", CODE_SYSTEM_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .setDistributionType(DistributionType.REFERENCE) // V0027 supporting for sharding
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, CODE_SYSTEMS, 2000, null, 1000));
                    }
                    return statements;
                })
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
     * If sharding is supported, this table is distributed by token_value which unfortunately
     * means that it cannot be the target of any foreign key constraint (which needs to use
     * the primary key COMMON_TOKEN_VALUE_ID).
     * @param pdm
     * @return the table definition
     */
    public void addCommonTokenValues(PhysicalDataModel pdm) {
        final String tableName = COMMON_TOKEN_VALUES;
        commonTokenValuesTable = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
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
                .setDistributionType(DistributionType.REFERENCE) // V0027 shard using token_value
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    // Intentionally a NOP
                    return statements;
                })
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
     * normalized in the COMMON_TOKEN_VALUES table. Because this
     * is for system-level params, there's no need to support
     * composite params
     * @param pdm
     * @return Table the table that was added to the PhysicalDataModel
     */
    public Table addResourceTokenRefs(PhysicalDataModel pdm) {

        final String tableName = RESOURCE_TOKEN_REFS;

        // logical_resources (0|1) ---- (*) resource_token_refs
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setTenantColumnName(MT_ID)
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addIntColumn(       PARAMETER_NAME_ID,    false)
                .addBigIntColumn(COMMON_TOKEN_VALUE_ID,     true) // support for null token value entries
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,    false)
                .addIntColumn(          REF_VERSION_ID,     true) // for when the referenced value is a logical resource with a version
                .addIndex(IDX + tableName + "_TPLR", COMMON_TOKEN_VALUE_ID, PARAMETER_NAME_ID, LOGICAL_RESOURCE_ID) // V0009 change
                .addIndex(IDX + tableName + "_LRPT", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, COMMON_TOKEN_VALUE_ID) // V0009 change
                .addForeignKeyConstraint(FK + tableName + "_CTV", schemaName, COMMON_TOKEN_VALUES, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + tableName + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .enableAccessControl(this.sessionVariable)
                .addWiths(addWiths()) // table tuning
                .addMigration(priorVersion -> {
                    // Replace the indexes initially defined in the V0006 version with better ones
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == FhirSchemaVersion.V0006.vid()) {
                        // Migrate the index definitions as part of the V0008 version of the schema
                        // This table was originally introduced as part of the V0006 schema, which
                        // is what we use as the match for the priorVersion
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_TVLR"));
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_LRTV"));

                        final String mtId = isMultitenant() ? MT_ID : null;
                        // Replace the original TVLR index on (common_token_value_id, parameter_name_id, logical_resource_id)
                        List<OrderedColumnDef> tplr = Arrays.asList(
                            new OrderedColumnDef(COMMON_TOKEN_VALUE_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(PARAMETER_NAME_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(LOGICAL_RESOURCE_ID, OrderedColumnDef.Direction.ASC, null)
                            );
                        statements.add(new CreateIndexStatement(schemaName, IDX + tableName + "_TPLR", tableName, mtId, tplr));

                        // Replace the original LRTV index with a new index on (logical_resource_id, parameter_name_id, common_token_value_id)
                        List<OrderedColumnDef> lrpt = Arrays.asList(
                            new OrderedColumnDef(LOGICAL_RESOURCE_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(PARAMETER_NAME_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(COMMON_TOKEN_VALUE_ID, OrderedColumnDef.Direction.ASC, null)
                            );
                        statements.add(new CreateIndexStatement(schemaName, IDX + tableName + "_LRPT", tableName, mtId, lrpt));
                    }
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(pdm);

        // TODO should not need to add as a table and an object. Get the table to add itself?
        tbl.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        this.procedureDependencies.add(tbl);
        pdm.addTable(tbl);
        pdm.addObject(tbl);

        return tbl;
    }

    /**
     * The erased_resources table is used to track which logical resources and corresponding
     * resource versions have been erased using the $erase operation. This table should
     * typically be empty and only used temporarily by the erase DAO/procedures to indicate
     * which rows have been erased. The entries in this table are then used to delete
     * any offloaded payload entries.
     * @param pdm
     */
    public void addErasedResources(PhysicalDataModel pdm) {
        final String tableName = ERASED_RESOURCES;
        final String mtId = isMultitenant() ? MT_ID : null;

        // Each erase operation is allocated an ERASED_RESOURCE_GROUP_ID
        // value which can be used to retrieve the resource and/or
        // resource-versions erased in a particular call. The rows
        // can then be deleted once the erasure of any offloaded
        // payload is confirmed. Note that we don't use logical_resource_id
        // or resource_id values here, because those records may have
        // already been deleted by $erase.
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid())
                .setTenantColumnName(mtId)
                .addBigIntColumn(ERASED_RESOURCE_GROUP_ID, false)
                .addIntColumn(RESOURCE_TYPE_ID, false)
                .addVarcharColumn(LOGICAL_ID, LOGICAL_ID_BYTES, false)
                .addIntColumn(VERSION_ID, true)
                .addIndex(IDX + tableName + "_GID", ERASED_RESOURCE_GROUP_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addForeignKeyConstraint(FK + tableName + "_RTID", schemaName, RESOURCE_TYPES, RESOURCE_TYPE_ID)
                .enableAccessControl(this.sessionVariable)
                .addWiths(addWiths()) // add table tuning
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    // Nothing yet
                    
                    // TODO migrate to simplified design (no PK, FK)
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

    /**
     * Adds a new sequence required for distributed databases like Citus
     * @param pdm
     */
    protected void addFhirChangeSequence(PhysicalDataModel pdm) {
        this.fhirChangeSequence = new Sequence(schemaName, FHIR_CHANGE_SEQUENCE, FhirSchemaVersion.V0027.vid(), 1, 1000);
        this.fhirChangeSequence.addTag(SCHEMA_GROUP_TAG, FHIRDATA_GROUP);
        procedureDependencies.add(fhirChangeSequence);
        sequencePrivileges.forEach(p -> p.addToObject(fhirChangeSequence));

        pdm.addObject(fhirChangeSequence);
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

    /**
     * The defaults with addWiths. Added to every table in a PostgreSQL schema
     * @return
     */
    protected List<With> addWiths() {
        // NOTE! If you change this table remember that you also need to bump the
        // schema version of every table that uses this list of Withs. This includes
        // adding a corresponding migration step.
        return Arrays.asList(
                With.with("autovacuum_vacuum_scale_factor", "0.01"), // V0019
                With.with("autovacuum_vacuum_threshold", "1000"),    // V0019
                With.with("autovacuum_vacuum_cost_limit", "2000"),   // V0019
                With.with(FhirSchemaConstants.PG_FILLFACTOR_PROP, Integer.toString(FhirSchemaConstants.PG_FILLFACTOR_VALUE)) // V0020
                );
    }
}