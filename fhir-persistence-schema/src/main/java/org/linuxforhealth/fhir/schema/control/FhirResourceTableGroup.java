/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CANONICAL_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CODE;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEM_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.COMMON_CANONICAL_VALUES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.COMMON_TOKEN_VALUES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.COMMON_TOKEN_VALUE_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.COMPOSITE_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CURRENT_ALLERGIES_LIST;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CURRENT_DRUG_ALLERGIES_LIST;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CURRENT_MEDICATIONS_LIST;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CURRENT_PROBLEMS_LIST;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.CURRENT_RESOURCE_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.DATA;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.DATE_END;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.DATE_START;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.DATE_VALUE_DROPPED_COLUMN;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.FK;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.FRAGMENT;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.FRAGMENT_BYTES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.IDX;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.IS_DELETED;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.ITEM_LOGICAL_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LAST_UPDATED;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LATITUDE_VALUE;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LIST_LOGICAL_RESOURCES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LIST_LOGICAL_RESOURCE_ITEMS;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID_BYTES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_IDENT;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.LONGITUDE_VALUE;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.MAX_SEARCH_STRING_BYTES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.NUMBER_VALUE;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.NUMBER_VALUE_HIGH;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.NUMBER_VALUE_LOW;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAMES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAME_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.PATIENT_CURRENT_REFS;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.PATIENT_LOGICAL_RESOURCES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.PK;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.PROFILES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.QUANTITY_VALUE;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.QUANTITY_VALUE_HIGH;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.QUANTITY_VALUE_LOW;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.REF_LOGICAL_RESOURCE_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.REF_VALUES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.REF_VALUES_V;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.REF_VERSION_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.RESOURCE_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.RESOURCE_PAYLOAD_KEY;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.RESOURCE_TOKEN_REFS;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.RESOURCE_TYPES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.RESOURCE_TYPE_ID;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.SECURITY;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.STR_VALUE;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.STR_VALUE_LCASE;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.TAGS;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.TOKEN_VALUES_V;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.UUID_LEN;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.VERSION;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.VERSION_BYTES;
import static org.linuxforhealth.fhir.schema.control.FhirSchemaConstants.VERSION_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.linuxforhealth.fhir.database.utils.api.DistributionType;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.common.AddColumn;
import org.linuxforhealth.fhir.database.utils.common.CreateIndexStatement;
import org.linuxforhealth.fhir.database.utils.common.DropColumn;
import org.linuxforhealth.fhir.database.utils.common.DropIndex;
import org.linuxforhealth.fhir.database.utils.common.DropPrimaryKey;
import org.linuxforhealth.fhir.database.utils.common.DropTable;
import org.linuxforhealth.fhir.database.utils.common.DropView;
import org.linuxforhealth.fhir.database.utils.model.ColumnBase;
import org.linuxforhealth.fhir.database.utils.model.ColumnDefBuilder;
import org.linuxforhealth.fhir.database.utils.model.CreateIndex;
import org.linuxforhealth.fhir.database.utils.model.GroupPrivilege;
import org.linuxforhealth.fhir.database.utils.model.IDatabaseObject;
import org.linuxforhealth.fhir.database.utils.model.ObjectGroup;
import org.linuxforhealth.fhir.database.utils.model.OrderedColumnDef;
import org.linuxforhealth.fhir.database.utils.model.PhysicalDataModel;
import org.linuxforhealth.fhir.database.utils.model.Table;
import org.linuxforhealth.fhir.database.utils.model.Tablespace;
import org.linuxforhealth.fhir.database.utils.model.View;
import org.linuxforhealth.fhir.database.utils.model.With;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresFillfactorSettingDAO;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresVacuumSettingDAO;

/**
 * Utility to create all the tables associated with a particular resource type
 */
public class FhirResourceTableGroup {
    // The model containing all the tables for the entire schema
    private final PhysicalDataModel model;

    // The schema we place all of our tables into
    private final String schemaName;

    // All the tables created by this component
    @SuppressWarnings("unused")
    private final Set<IDatabaseObject> procedureDependencies;

    private final Tablespace fhirTablespace;

    // Privileges to be granted to each of the resource tables created by this class
    private final Collection<GroupPrivilege> resourceTablePrivileges;

    private final List<With> withs;

    private static final String _LOGICAL_RESOURCES = "_LOGICAL_RESOURCES";
    private static final String _RESOURCES = "_RESOURCES";

    private static final String ROW_ID = "ROW_ID";

    /**
     * Public constructor
     */
    public FhirResourceTableGroup(PhysicalDataModel model, String schemaName, 
            Set<IDatabaseObject> procedureDependencies, Tablespace fhirTablespace, 
            Collection<GroupPrivilege> privileges, List<With> withs) {
        this.model = model;
        this.schemaName = schemaName;
        this.procedureDependencies = procedureDependencies;
        this.fhirTablespace = fhirTablespace;
        this.resourceTablePrivileges = privileges;
        this.withs = withs;
    }

    /**
     * Add all the tables required for the given resource type. For example, if the
     * resourceTypeName is Patient, the following tables will be added:
     * <ul>
     * <li>patient_logical_resources
     * <li>patient_resources
     * <li>patient_str_values
     * <li>patient_date_values
     * <li>patient_token_values
     * <li>patient_number_values
     * <li>patient_latlng_values
     * <li>patient_quantity_values
     * </ul>
     * @param resourceTypeName
     */
    public ObjectGroup addResourceType(String resourceTypeName) {
        final String tablePrefix = resourceTypeName.toUpperCase();

        // Stick all the objects we want to create under one group which is executed
        // in the order in which they are defined (not parallelized)
        List<IDatabaseObject> group = new ArrayList<>();

        addLogicalResources(group, tablePrefix);
        addResources(group, tablePrefix);
        addStrValues(group, tablePrefix);
        addDateValues(group, tablePrefix);
        addNumberValues(group, tablePrefix);
        addLatLngValues(group, tablePrefix);
        addQuantityValues(group, tablePrefix);
        // composites table removed by issue-1683
        addResourceTokenRefs(group, tablePrefix);
        addRefValues(group, tablePrefix);
        addTokenValuesView(group, tablePrefix);
        addRefValuesView(group, tablePrefix);
        addProfiles(group, tablePrefix);
        addTags(group, tablePrefix);
        addSecurity(group, tablePrefix);

        // group all the tables under one object so that we can perform everything within one
        // transaction. This helps to eliminate deadlocks when adding the FK constraints due to
        // issues with DB2 managing its catalog
        return new ObjectGroup(schemaName, tablePrefix + "_RESOURCE_TABLE_GROUP", group);
    }

    /**
     * Add the logical_resources table definition for the given resource prefix
     * @param group
     * @param prefix
     */
    public void addLogicalResources(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_LOGICAL_RESOURCES";

        // This is the resource-specific instance of the logical resources table, and
        // shares a common primary key (logical_resource_id) with the system-wide table
        // We also have a FK constraint pointing back to that table to try and keep
        // things sensible.
        Table.Builder builder = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addBigIntColumn(LOGICAL_RESOURCE_ID, false)
                .addVarcharColumn(LOGICAL_ID, LOGICAL_ID_BYTES, false)
                .addBigIntColumn(CURRENT_RESOURCE_ID, true)
                .addCharColumn(IS_DELETED, 1, false, "'X'")
                .addTimestampColumn(LAST_UPDATED, true) // nullable has to match the migration add column
                .addIntColumn(VERSION_ID, true) // nullable has to match the migration add column
                .addPrimaryKey(tableName + "_PK", LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint("FK_" + tableName + "_LRID", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                // Add indexes to avoid dead lock issue of derby, and improve Db2 performance
                // Derby requires all columns used in where clause to be indexed, otherwise whole table lock will be
                // used instead of row lock, which can cause dead lock issue frequently during concurrent accesses.
                .addIndex(IDX + tableName + CURRENT_RESOURCE_ID, CURRENT_RESOURCE_ID)
                .addIndex(IDX + tableName + LOGICAL_ID, LOGICAL_ID)
                .addWiths(this.withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0009.vid()) {
                        // Yes, this looks a little weird but as part of the migration to our V0009
                        // schema, we have to drop the <resourceType>_COMPOSITES table which is no
                        // longer required. But we have to drop it before we get rid of the primary
                        // key and ROW_ID on any of the parameter tables, which is why we do this here
                        statements.add(new DropTable(schemaName, prefix + "_COMPOSITES"));

                        // Get rid of the old token values parameter table which is no longer used
                        statements.add(new DropTable(schemaName, prefix + "_TOKEN_VALUES"));
                    }

                    if (priorVersion < FhirSchemaVersion.V0012.vid()) {
                        addLogicalResourcesMigration(statements, tableName, priorVersion);
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                });

        Table tbl = builder.build(model);
        group.add(tbl);
        model.addTable(tbl);

        // Special case for LIST resource...we need a table to store the list items
        if ("LIST".equalsIgnoreCase(prefix)) {
            addListLogicalResourceItems(group, prefix);
        }

        // Extension table for patient to support references to current lists
        // such as $current-allergies
        // https://www.hl7.org/fhir/lifecycle.html#current
        if ("PATIENT".equalsIgnoreCase(prefix)) {
            addPatientCurrentRefs(group, prefix);
        }
    }

    /**
     * V0010: IS_DELETED is added to each xxx_LOGICAL_RESOURCES.
     * V0011: LAST_UPDATED is added to each xxx_LOGICAL_RESOURCES.
     * V0012: VERSION_ID is added to each xxx_LOGICAL_RESOURCES.
     *
     * Note that we don't attempt to perform the data migration here because
     * migration for Db2 multi-tenant schemas requires iterating over each
     * tenant. The data migration step is therefore left as an operation to
     * be applied by the schema tool.
     * @param statements
     * @param tableName
     */
    private void addLogicalResourcesMigration(List<IDatabaseStatement> statements, String tableName, int priorVersion) {
        ColumnDefBuilder builder = new ColumnDefBuilder();
        if (priorVersion < FhirSchemaVersion.V0010.vid()) {
            // Note that we use 'X' as the default value because this acts as a marker
            // for the schema tool to easily check to see if the table needs to be
            // migrated. This is fine as long as we made sure that inserts into
            // the xxx_logical_resources tables always include a proper value for
            // this column. For Db2 and PostgreSQL, this happens in the add_any_resource
            // stored procedures.
            builder.addCharColumn(IS_DELETED, 1, false, "'X'");
        }

        if (priorVersion < FhirSchemaVersion.V0011.vid()) {
            // Add the LAST_UPDATED column if needed. We have to allow null because
            // no default value is appropriate
            builder.addTimestampColumn(LAST_UPDATED, true);
        }

        if (priorVersion < FhirSchemaVersion.V0012.vid()) {
            // Add the VERSION_ID column if needed. We have to allow null because
            // no default value is appropriate
            builder.addIntColumn(VERSION_ID, true);
        }

        List<ColumnBase> columns = builder.buildColumns();
        if (columns.size() > 0) {
            for (ColumnBase column : columns) {
                statements.add(new AddColumn(schemaName, tableName, column));
            }
        }
    }

    /**
     * Add the resources table definition
     * <pre>
  resource_id            BIGINT             NOT NULL,
  logical_resource_id    BIGINT             NOT NULL,
  version_id                INT             NOT NULL,
  last_updated        TIMESTAMP             NOT NULL,
  is_deleted               CHAR(1)          NOT NULL,
  data                     BLOB(2147483647) INLINE LENGTH 10240;

  CREATE UNIQUE INDEX device_resource_prf_in1    ON device_resources (resource_id) INCLUDE (logical_resource_id, version_id, is_deleted);
     * </pre>
     * @param group
     * @param prefix
     */
    public void addResources(List<IDatabaseObject> group, String prefix) {

        // The index which also used by the database to support the primary key constraint
        final List<String> prfIndexCols = Arrays.asList(RESOURCE_ID);
        final List<String> prfIncludeCols = Arrays.asList(LOGICAL_RESOURCE_ID, VERSION_ID, IS_DELETED);
        final String tableName = prefix + _RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addBigIntColumn(          RESOURCE_ID,                     false)
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,                     false)
                .addIntColumn(              VERSION_ID,                     false)
                .addTimestampColumn(      LAST_UPDATED,                     false)
                .addCharColumn(             IS_DELETED,           1,        false)
                .addBlobColumn(                   DATA,  2147483647,  10240, true)
                .addVarcharColumn(RESOURCE_PAYLOAD_KEY,    UUID_LEN,         true)    // new column for V0024
                .addUniqueIndex(tableName + "_PRF_IN1", prfIndexCols, prfIncludeCols)
                .addIndex(IDX + tableName + LOGICAL_RESOURCE_ID, LOGICAL_RESOURCE_ID)
                .addPrimaryKey(tableName + "_PK", RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0024.vid()) {
                        // Migration steps for the V0024 schema version
                        List<ColumnBase> cols = ColumnDefBuilder.builder()
                            .addVarcharColumn(RESOURCE_PAYLOAD_KEY, UUID_LEN, true)
                            .buildColumns();
    
                        statements.add(new AddColumn(schemaName, tableName, cols.get(0)));
                    }
                   return statements;
                })
                .build(model);

        tbl.addTag(FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

        group.add(tbl);
        model.addTable(tbl);

        // Issue 1331. LAST_UPDATED should be indexed now that we're using it
        // in search queries
        CreateIndex idxLastUpdated = CreateIndex.builder()
                .setSchemaName(schemaName)
                .setTableName(tableName)
                .setVersionTrackingName(tableName) // cover up a defect in how we name this index in VERSION_HISTORY
                .setIndexName(IDX + tableName + "_LUPD")
                .setUnique(false)
                .setVersion(FhirSchemaVersion.V0005.vid())
                .addColumn(LAST_UPDATED)
                .build();
        idxLastUpdated.addDependency(tbl); // dependency to the table on which the index applies

        idxLastUpdated.addTag(FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

        group.add(idxLastUpdated);

    }

    /**
     * Add the STR_VALUES table for the given resource name prefix
     * <pre>
  parameter_name_id        INT             NOT NULL,
  str_value            VARCHAR(511 OCTETS),
  str_value_lcase      VARCHAR(511 OCTETS),
  resource_id           BIGINT             NOT NULL,
  composite_id        SMALLINT

CREATE INDEX idx_device_str_values_psr ON device_str_values(parameter_name_id, str_value, resource_id);
CREATE INDEX idx_device_str_values_plr ON device_str_values(parameter_name_id, str_value_lcase, resource_id);
CREATE INDEX idx_device_str_values_rps ON device_str_values(resource_id, parameter_name_id, str_value);
CREATE INDEX idx_device_str_values_rpl ON device_str_values(resource_id, parameter_name_id, str_value_lcase);
ALTER TABLE device_str_values ADD CONSTRAINT fk_device_str_values_pnid FOREIGN KEY (parameter_name_id) REFERENCES parameter_names;
ALTER TABLE device_str_values ADD CONSTRAINT fk_device_str_values_pnid FOREIGN KEY (parameter_name_id) REFERENCES parameter_names;
ALTER TABLE device_str_values ADD CONSTRAINT fk_device_str_values_rid  FOREIGN KEY (resource_id) REFERENCES device_resources;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addStrValues(List<IDatabaseObject> group, String prefix) {

        final int msb = MAX_SEARCH_STRING_BYTES;
        final String tableName = prefix + "_STR_VALUES";
        final String logicalResourcesTable = prefix + "_LOGICAL_RESOURCES";
        
        // Parameters are tied to the logical resource
        Table tbl = Table.builder(schemaName, tableName)
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                // .addBigIntColumn(             ROW_ID,      false) // Removed by issue-1683 - composites refactor
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addVarcharColumn(         STR_VALUE, msb,  true)
                .addVarcharColumn(   STR_VALUE_LCASE, msb,  true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIntColumn(COMPOSITE_ID,                 true)      // V0009
                .addIndex(IDX + tableName + "_PSR", PARAMETER_NAME_ID, STR_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PLR", PARAMETER_NAME_ID, STR_VALUE_LCASE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE)
                .addIndex(IDX + tableName + "_RPL", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE_LCASE)
                .addForeignKeyConstraint(FK + tableName + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_RID", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == 1) {
                        addCompositeMigrationStepsV0009(statements, tableName);
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * For our V0009 schema version.
     * Add the steps we need to migrate a parameters table to the new composites design
     * for issue 1683.
     * @param statements the list of {@link IDatabaseStatement} to add to
     * @param tableName the parameter table name (e.g. <code>{resourceType}_STR_VALUES) </code>
     */
    private void addCompositeMigrationStepsV0009(List<IDatabaseStatement> statements, String tableName) {
        // Simplified composite value support. Eliminate the composites table and instead
        // use a new composite_id column to correlate parameters involved in a composite relationship
        // drop PK constraint; remove ROW_ID column after data migration
        statements.add(new DropPrimaryKey(schemaName, tableName, true));
        statements.add(new DropColumn(schemaName, tableName, true, ROW_ID));

        // Add COMPOSITE_ID SMALLINT used to tie together composite parameter rows
        List<ColumnBase> columns = new ColumnDefBuilder()
                .addIntColumn(COMPOSITE_ID, true)
                .buildColumns();
        for (ColumnBase column : columns) {
            statements.add(new AddColumn(schemaName, tableName, column));
        }
    }

    /**
     * <pre>
  parameter_name_id        INT NOT NULL,
  code_system_id           INT NOT NULL,
  token_value          VARCHAR(255 OCTETS),
  resource_id           BIGINT NOT NULL
  composite_id        SMALLINT
)
;

    /**
     * New schema for issue #1366. Uses a map table to reduce cost of indexing repeated token values
     * @param pdm
     * @return
     */
    public Table addResourceTokenRefs(List<IDatabaseObject> group, String prefix) {

        final String tableName = prefix + "_" + RESOURCE_TOKEN_REFS;

        // logical_resources (1) ---- (*) patient_resource_token_refs (*) ---- (0|1) common_token_values
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0028.vid()) // V0028: ref_version_id removed because refs are now stored in xx_ref_values
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addIntColumn(       PARAMETER_NAME_ID,    false)
                .addBigIntColumn(COMMON_TOKEN_VALUE_ID,     true)
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,    false)
                .addIntColumn(COMPOSITE_ID,                 true)      // V0009
                .addIndex(IDX + tableName + "_TPLR", COMMON_TOKEN_VALUE_ID, PARAMETER_NAME_ID, LOGICAL_RESOURCE_ID) // V0008 change
                .addIndex(IDX + tableName + "_LRPT", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, COMMON_TOKEN_VALUE_ID) // V0008 change
                .addForeignKeyConstraint(FK + tableName + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_TV", schemaName, COMMON_TOKEN_VALUES, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == FhirSchemaVersion.V0006.vid()) {
                        // Migrate the index definitions as part of the V0008 version of the schema
                        // This table was originally introduced as part of the V0006 schema, which
                        // is what we use as the match for the priorVersion
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_TVLR"));
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_LRTV"));

                        // Replace the original TVLR index on (common_token_value_id, parameter_name_id, logical_resource_id)
                        List<OrderedColumnDef> tplr = Arrays.asList(
                            new OrderedColumnDef(COMMON_TOKEN_VALUE_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(PARAMETER_NAME_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(LOGICAL_RESOURCE_ID, OrderedColumnDef.Direction.ASC, null)
                            );
                        statements.add(new CreateIndexStatement(schemaName, IDX + tableName + "_TPLR", tableName, tplr));

                        // Replace the original LRTV index with a new index on (logical_resource_id, parameter_name_id, common_token_value_id)
                        List<OrderedColumnDef> lrpt = Arrays.asList(
                            new OrderedColumnDef(LOGICAL_RESOURCE_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(PARAMETER_NAME_ID, OrderedColumnDef.Direction.ASC, null),
                            new OrderedColumnDef(COMMON_TOKEN_VALUE_ID, OrderedColumnDef.Direction.ASC, null)
                            );
                        statements.add(new CreateIndexStatement(schemaName, IDX + tableName + "_LRPT", tableName, lrpt));
                    }

                    if (priorVersion < FhirSchemaVersion.V0009.vid()) {
                        addCompositeMigrationStepsV0009(statements, tableName);
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }

                    if (priorVersion < FhirSchemaVersion.V0028.vid()) {
                        // For PostgreSQL we can't drop the column because a view depends on it. Just drop the view
                        // because the definition has been updated and it will be added again later
                        final String viewName = prefix + "_" + TOKEN_VALUES_V;
                        statements.add(new DropView(schemaName, viewName));
                        statements.add(new DropColumn(schemaName,  tableName, REF_VERSION_ID));
                    }
                    return statements;
                })
                .build(model);

        tbl.addTag(FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

        group.add(tbl);
        model.addTable(tbl);

        return tbl;
    }

    /**
     * Schema V0027 adds a dedicated table for supporting reference values instead of using
     * token values.
     * @param pdm
     * @return
     */
    public Table addRefValues(List<IDatabaseObject> group, String prefix) {

        final String tableName = prefix + "_REF_VALUES";

        // logical_resources (1) ---- (*) patient_ref_values (*) ---- (0|1) logical_resource_ident
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0028.vid()) // V0028: tweak vacuum and fillfactor
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addIntColumn(           PARAMETER_NAME_ID,    false)
                .addBigIntColumn(      LOGICAL_RESOURCE_ID,    false)
                .addBigIntColumn(  REF_LOGICAL_RESOURCE_ID,     true)
                .addIntColumn(              REF_VERSION_ID,     true) // for when the referenced value is a logical resource with a version
                .addIntColumn(                COMPOSITE_ID,     true)
                .addIndex(IDX + tableName + "_RFPN", REF_LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID)
                .addIndex(IDX + tableName + "_LRPN", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0028.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model);

        tbl.addTag(FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

        group.add(tbl);
        model.addTable(tbl);

        return tbl;
    }

    /**
     * Add the resource-specific profiles table which maps to the normalized URI
     * values stored in COMMON_CANONICAL_VALUES
     * @param group
     * @param prefix
     * @return
     */
    public Table addProfiles(List<IDatabaseObject> group, String prefix) {

        final String tableName = prefix + "_" + PROFILES;

        // logical_resources (1) ---- (*) patient_profiles (*) ---- (0|1) common_canonical_values
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addBigIntColumn(          CANONICAL_ID,    false)
                .addBigIntColumn(   LOGICAL_RESOURCE_ID,    false)
                .addVarcharColumn(             VERSION,  VERSION_BYTES, true)
                .addVarcharColumn(            FRAGMENT, FRAGMENT_BYTES, true)
                .addIndex(IDX + tableName + "_TPLR", CANONICAL_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_LRPT", LOGICAL_RESOURCE_ID, CANONICAL_ID)
                .addForeignKeyConstraint(FK + tableName + "_TV", schemaName, COMMON_CANONICAL_VALUES, CANONICAL_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
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
                .build(model);

        tbl.addTag(FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

        group.add(tbl);
        model.addTable(tbl);

        return tbl;
    }

    /**
     * Resource-specific tags. Tags are treated as tokens, but are separated out
     * into their own table to avoid issues with cardinality estimation (because
     * they are not very selective).
     * @param group
     * @param prefix
     * @return
     */
    public Table addTags(List<IDatabaseObject> group, String prefix) {

        final String tableName = prefix + "_" + TAGS;

        // logical_resources (1) ---- (*) patient_tags (*) ---- (0|1) common_token_values
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addBigIntColumn(COMMON_TOKEN_VALUE_ID,   false)
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,   false)
                .addIndex(IDX + tableName + "_TPLR", COMMON_TOKEN_VALUE_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_LRPT", LOGICAL_RESOURCE_ID, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_TV", schemaName, COMMON_TOKEN_VALUES, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
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
                .build(model);

        tbl.addTag(FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

        group.add(tbl);
        model.addTable(tbl);

        return tbl;
    }

    /**
     * Add the common_token_values mapping table for security search parameters
     * @param group
     * @param prefix
     * @return
     */
    public Table addSecurity(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_" + SECURITY;

        // logical_resources (1) ---- (*) patient_security (*) ---- (0|1) common_token_values
        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addBigIntColumn(COMMON_TOKEN_VALUE_ID,   false)
                .addBigIntColumn(  LOGICAL_RESOURCE_ID,   false)
                .addIndex(IDX + tableName + "_TPLR", COMMON_TOKEN_VALUE_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_LRPT", LOGICAL_RESOURCE_ID, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_TV", schemaName, COMMON_TOKEN_VALUES, COMMON_TOKEN_VALUE_ID)
                .addForeignKeyConstraint(FK + tableName + "_LR", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
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
                .build(model);

        tbl.addTag(FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

        group.add(tbl);
        model.addTable(tbl);

        return tbl;
    }

    /**
     * View created over common_token_values and resource_token_refs to hide the
     * schema change (V0006 issue 1366) as much as possible from the search query
     * generation. Search queries simply need to join against this view instead
     * of the old {resourceType}_token_values table
     * @param pdm
     * @return
     */
    public void addTokenValuesView(List<IDatabaseObject> group, String prefix) {

        final String viewName = prefix + "_" + TOKEN_VALUES_V;

        // Find the two dependencies we need for this view
        IDatabaseObject commonTokenValues = model.findTable(schemaName, COMMON_TOKEN_VALUES);
        IDatabaseObject resourceTokenRefs = model.findTable(schemaName, prefix + "_" + RESOURCE_TOKEN_REFS);

        // We join against the code_systems table because it gives us the code_system_name, which
        // for reference strings is actually the resource type of the reference - this is useful
        // for building chained reference queries, and is well worth the minor cost of an extra join
        StringBuilder select = new StringBuilder();
        select.append("SELECT ref.parameter_name_id, ctv.code_system_id, ctv.token_value, ref.logical_resource_id, ref.common_token_value_id, ref." + COMPOSITE_ID);
        select.append(" FROM ").append(commonTokenValues.getName()).append(" AS ctv, ");
        select.append(resourceTokenRefs.getName()).append(" AS ref ");
        select.append(" WHERE ctv.common_token_value_id = ref.common_token_value_id ");

        View view = View.builder(schemaName, viewName)
                .setVersion(FhirSchemaVersion.V0028.vid())
                .setSelectClause(select.toString())
                .addPrivileges(resourceTablePrivileges)
                .addDependency(commonTokenValues)
                .addDependency(resourceTokenRefs)
                .build();

        group.add(view);
    }

    /**
     * View to encapsulate the join between xx_ref_values and logical_resource_ident
     * tables, which makes it easier for the search query builder to compose search
     * queries using reference parameters.
     * @param group
     * @param prefix
     */
    public void addRefValuesView(List<IDatabaseObject> group, String prefix) {

        final String viewName = prefix + "_" + REF_VALUES_V;

        // Find the two dependencies we need for this view
        IDatabaseObject logicalResourceIdent = model.findTable(schemaName, LOGICAL_RESOURCE_IDENT);
        IDatabaseObject refValues = model.findTable(schemaName, prefix + "_" + REF_VALUES);

        StringBuilder select = new StringBuilder();
        select.append("SELECT ref.parameter_name_id, lri.resource_type_id, lri.logical_id, ref.logical_resource_id, ");
        select.append("       ref.ref_version_id, ref.ref_logical_resource_id, ref.composite_id, ");
        select.append("       lri.logical_id AS ref_value ");
        select.append("  FROM ").append(logicalResourceIdent.getName()).append(" AS lri, ");
        select.append(refValues.getName()).append(" AS ref ");
        select.append(" WHERE lri.logical_resource_id = ref.ref_logical_resource_id ");

        View view = View.builder(schemaName, viewName)
                .setVersion(FhirSchemaVersion.V0027.vid())
                .setSelectClause(select.toString())
                .addPrivileges(resourceTablePrivileges)
                .addDependency(logicalResourceIdent)
                .addDependency(refValues)
                .build();

        group.add(view);
    }

    /**
     * <pre>
CREATE TABLE device_date_values  (
  row_id                BIGINT             NOT NULL,
  parameter_name_id         INT NOT NULL,
  date_start          TIMESTAMP,
  date_end            TIMESTAMP,
  resource_id            BIGINT NOT NULL
)
;

CREATE INDEX idx_device_date_values_pser ON device_date_values(parameter_name_id, date_start, date_end, resource_id);
CREATE INDEX idx_device_date_values_pesr ON device_date_values(parameter_name_id, date_end, date_start, resource_id);
CREATE INDEX idx_device_date_values_rpse   ON device_date_values(resource_id, parameter_name_id, date_start, date_end);
ALTER TABLE device_date_values ADD CONSTRAINT fk_device_date_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names;
ALTER TABLE device_date_values ADD CONSTRAINT fk_device_date_values_r  FOREIGN KEY (resource_id)       REFERENCES device_resources;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addDateValues(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_DATE_VALUES";
        final String logicalResourcesTable = prefix + _LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addTimestampColumn(      DATE_START,      true)
                .addTimestampColumn(        DATE_END,      true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIntColumn(COMPOSITE_ID,                 true)      // V0009
                .addIndex(IDX + tableName + "_PSER", PARAMETER_NAME_ID, DATE_START, DATE_END, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PESR", PARAMETER_NAME_ID, DATE_END, DATE_START, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPSE", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, DATE_START, DATE_END)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_R", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == 1) {
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_PVR"));
                        statements.add(new DropIndex(schemaName, IDX + tableName + "_RPV"));
                        statements.add(new DropColumn(schemaName, tableName, DATE_VALUE_DROPPED_COLUMN));
                    }

                    if (priorVersion < FhirSchemaVersion.V0009.vid()) {
                        addCompositeMigrationStepsV0009(statements, tableName);
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * <pre>
CREATE TABLE device_number_values  (
  row_id               BIGINT NOT NULL,
  parameter_name_id       INT NOT NULL,
  number_value         DOUBLE,
  resource_id          BIGINT NOT NULL
)
;
CREATE INDEX idx_device_number_values_pnnv ON device_number_values(parameter_name_id, number_value, resource_id);
CREATE INDEX idx_device_number_values_rps ON device_number_values(resource_id, parameter_name_id, number_value);
ALTER TABLE device_number_values ADD CONSTRAINT fk_device_number_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names;
ALTER TABLE device_number_values ADD CONSTRAINT fk_device_number_values_r  FOREIGN KEY (resource_id)       REFERENCES device_resources;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addNumberValues(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_NUMBER_VALUES";
        final String logicalResourcesTable = prefix + _LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addDoubleColumn(       NUMBER_VALUE,       true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addDoubleColumn(   NUMBER_VALUE_LOW,       true)
                .addDoubleColumn(  NUMBER_VALUE_HIGH,       true)
                .addIntColumn(COMPOSITE_ID,                 true)      // V0009
                .addIndex(IDX + tableName + "_PNNV", PARAMETER_NAME_ID, NUMBER_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, NUMBER_VALUE)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_RID", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion == 1) {
                        List<ColumnBase> columns = new ColumnDefBuilder()
                                .addDoubleColumn(NUMBER_VALUE_LOW, true)
                                .addDoubleColumn(NUMBER_VALUE_HIGH, true)
                                .buildColumns();
                        for (ColumnBase column : columns) {
                            statements.add(new AddColumn(schemaName, tableName, column));
                        }
                    }

                    if (priorVersion < FhirSchemaVersion.V0009.vid()) {
                        addCompositeMigrationStepsV0009(statements, tableName);
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * <pre>
CREATE TABLE device_latlng_values  (
  row_id              BIGINT NOT NULL,
  parameter_name_id   INT NOT NULL,
  latitude_value      DOUBLE,
  longitude_value     DOUBLE,
  resource_id         BIGINT NOT NULL
)
CREATE INDEX idx_device_latlng_values_pnnlv ON device_latlng_values(parameter_name_id, latitude_value, resource_id);
CREATE INDEX idx_device_latlng_values_pnnhv ON device_latlng_values(parameter_name_id, longitude_value, resource_id);
CREATE INDEX idx_device_latlng_values_rplat ON device_latlng_values(resource_id, parameter_name_id, latitude_value);
CREATE INDEX idx_device_latlng_values_rplng ON device_latlng_values(resource_id, parameter_name_id, longitude_value);
ALTER TABLE device_latlng_values ADD CONSTRAINT fk_device_latlng_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names;
ALTER TABLE device_latlng_values ADD CONSTRAINT fk_device_latlng_values_r  FOREIGN KEY (resource_id)       REFERENCES device_resources;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addLatLngValues(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_LATLNG_VALUES";
        final String logicalResourcesTable = prefix + _LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addDoubleColumn(     LATITUDE_VALUE,       true)
                .addDoubleColumn(    LONGITUDE_VALUE,       true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIntColumn(COMPOSITE_ID,                 true)      // V0009
                .addIndex(IDX + tableName + "_PNNLV", PARAMETER_NAME_ID, LATITUDE_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PNNHV", PARAMETER_NAME_ID, LONGITUDE_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPLAT", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, LATITUDE_VALUE)
                .addIndex(IDX + tableName + "_RPLNG", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, LONGITUDE_VALUE)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_RID", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0009.vid()) {
                        addCompositeMigrationStepsV0009(statements, tableName);
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * <pre>
CREATE TABLE device_quantity_values  (
  row_id                BIGINT NOT NULL,
  parameter_name_id        INT NOT NULL,
  code                 VARCHAR(255 OCTETS) NOT NULL,
  quantity_value        DOUBLE,
  quantity_value_low    DOUBLE,
  quantity_value_high   DOUBLE,
  code_system_id           INT,
  resource_id           BIGINT NOT NULL
)
;

CREATE INDEX idx_device_quantity_values_pnnv   ON device_quantity_values(parameter_name_id, code, quantity_value, resource_id, code_system_id);
CREATE INDEX idx_device_quantity_values_rps    ON device_quantity_values(resource_id, parameter_name_id, code, quantity_value, code_system_id);

CREATE INDEX idx_device_quantity_values_pclhsr  ON device_quantity_values(parameter_name_id, code, quantity_value_low, quantity_value_high, code_system_id, resource_id);
CREATE INDEX idx_device_quantity_values_pchlsr  ON device_quantity_values(parameter_name_id, code, quantity_value_high, quantity_value_low, code_system_id, resource_id);
CREATE INDEX idx_device_quantity_values_rpclhs  ON device_quantity_values(resource_id, parameter_name_id, code, quantity_value_low, quantity_value_high, code_system_id);
CREATE INDEX idx_device_quantity_values_rpchls  ON device_quantity_values(resource_id, parameter_name_id, code, quantity_value_high, quantity_value_low, code_system_id);

ALTER TABLE device_quantity_values ADD CONSTRAINT fk_device_quantity_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names;
ALTER TABLE device_quantity_values ADD CONSTRAINT fk_device_quantity_values_r  FOREIGN KEY (resource_id)       REFERENCES device_resources;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addQuantityValues(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_QUANTITY_VALUES";
        final String logicalResourcesTable = prefix + _LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addVarcharColumn(              CODE, 255, false)
                .addDoubleColumn(     QUANTITY_VALUE,      true)
                .addDoubleColumn( QUANTITY_VALUE_LOW,      true)
                .addDoubleColumn(QUANTITY_VALUE_HIGH,      true)
                .addIntColumn(        CODE_SYSTEM_ID,      true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIntColumn(COMPOSITE_ID,                 true)      // V0009
                .addIndex(IDX + tableName + "_PNNV", PARAMETER_NAME_ID, CODE, QUANTITY_VALUE, LOGICAL_RESOURCE_ID, CODE_SYSTEM_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE, QUANTITY_VALUE, CODE_SYSTEM_ID)
                .addIndex(IDX + tableName + "_PCLHSR", PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_LOW, QUANTITY_VALUE_HIGH, CODE_SYSTEM_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PCHLSR", PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_HIGH, QUANTITY_VALUE_LOW, CODE_SYSTEM_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPCLHS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_LOW, QUANTITY_VALUE_HIGH, CODE_SYSTEM_ID)
                .addIndex(IDX + tableName + "_RPCHLS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_HIGH, QUANTITY_VALUE_LOW, CODE_SYSTEM_ID)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_R", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0009.vid()) {
                        addCompositeMigrationStepsV0009(statements, tableName);
                    }

                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, tableName, 2000, null, 1000));
                    }
                    
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, tableName, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * Special case for LIST resources where we attach a child table to its LIST_LOGICAL_RESOURCES
     * to support usage of the list items in search queries. The FK to LIST_LOGICAL_RESOURCES is
     * its parent. We then point to the resource being referenced via a resourceType/logicalId
     * tuple. This means that the list item record can be created before the referenced resource
     * is created.
     * @param group
     * @param prefix
     */
    public void addListLogicalResourceItems(List<IDatabaseObject> group, String prefix) {
        final int lib = LOGICAL_ID_BYTES;

        Table tbl = Table.builder(schemaName, LIST_LOGICAL_RESOURCE_ITEMS)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addBigIntColumn( LOGICAL_RESOURCE_ID,      false)
                .addIntColumn(       RESOURCE_TYPE_ID,      false)
                .addVarcharColumn(    ITEM_LOGICAL_ID, lib,  true)
                .addForeignKeyConstraint(FK + LIST_LOGICAL_RESOURCE_ITEMS + "_LRID", schemaName, LIST_LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + LIST_LOGICAL_RESOURCE_ITEMS + "_RTID", schemaName, RESOURCE_TYPES, RESOURCE_TYPE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, LIST_LOGICAL_RESOURCE_ITEMS, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, LIST_LOGICAL_RESOURCE_ITEMS, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * Add the extension table used to support references to the current
     * resources lists defined by the spec: https://www.hl7.org/fhir/lifecycle.html#current
     * @param group - the group of tables for this resource (Patient in this case)
     * @param prefix - the resource name - PATIENT
     */
    public void addPatientCurrentRefs(List<IDatabaseObject> group, String prefix) {
        final int lib = LOGICAL_ID_BYTES;

        // The CURRENT_*_LIST columns are the logical_id values of the
        // LIST resources used to host these special lists. We don't
        // model with a foreign key to avoid order of insertion issues

        Table tbl = Table.builder(schemaName, PATIENT_CURRENT_REFS)
                .setVersion(FhirSchemaVersion.V0027.vid()) // V0027: add support for distribution/sharding
                .setDistributionType(DistributionType.DISTRIBUTED) // V0027 support for sharding
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addBigIntColumn(         LOGICAL_RESOURCE_ID,      false)
                .addVarcharColumn(      CURRENT_PROBLEMS_LIST, lib,  true)
                .addVarcharColumn(   CURRENT_MEDICATIONS_LIST, lib,  true)
                .addVarcharColumn(     CURRENT_ALLERGIES_LIST, lib,  true)
                .addVarcharColumn(CURRENT_DRUG_ALLERGIES_LIST, lib,  true)
                .addPrimaryKey(PK + PATIENT_CURRENT_REFS, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + PATIENT_CURRENT_REFS + "_LRID", schemaName, PATIENT_LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .addWiths(withs)
                .addMigration(priorVersion -> {
                    List<IDatabaseStatement> statements = new ArrayList<>();
                    if (priorVersion < FhirSchemaVersion.V0019.vid()) {
                        statements.add(new PostgresVacuumSettingDAO(schemaName, PATIENT_CURRENT_REFS, 2000, null, 1000));
                    }
                    if (priorVersion < FhirSchemaVersion.V0020.vid()) {
                        statements.add(new PostgresFillfactorSettingDAO(schemaName, PATIENT_CURRENT_REFS, FhirSchemaConstants.PG_FILLFACTOR_VALUE));
                    }
                    return statements;
                })
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }
}