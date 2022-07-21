/*
 * (C) Copyright IBM Corp. 2019, 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEMS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CURRENT_ALLERGIES_LIST;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CURRENT_DRUG_ALLERGIES_LIST;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CURRENT_MEDICATIONS_LIST;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CURRENT_PROBLEMS_LIST;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.CURRENT_RESOURCE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATA;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_END;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_START;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.DATE_VALUE_DROPPED_COLUMN;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.FK;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.IDX;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.IS_DELETED;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.ITEM_LOGICAL_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LAST_UPDATED;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LATITUDE_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LIST_LOGICAL_RESOURCES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LIST_LOGICAL_RESOURCE_ITEMS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_ID_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LOGICAL_RESOURCE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.LONGITUDE_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MAX_SEARCH_STRING_BYTES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.MT_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.NUMBER_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAMES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PARAMETER_NAME_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PATIENT_CURRENT_REFS;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PATIENT_LOGICAL_RESOURCES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.PK;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.QUANTITY_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.QUANTITY_VALUE_HIGH;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.QUANTITY_VALUE_LOW;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_TYPES;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.RESOURCE_TYPE_ID;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.STR_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.STR_VALUE_LCASE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.TOKEN_VALUE;
import static com.ibm.fhir.schema.control.FhirSchemaConstants.VERSION_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.database.utils.model.Generated;
import com.ibm.fhir.database.utils.model.GroupPrivilege;
import com.ibm.fhir.database.utils.model.IDatabaseObject;
import com.ibm.fhir.database.utils.model.ObjectGroup;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.Tablespace;
import com.ibm.fhir.schema.control.FhirSchemaTags;

/**
 * Utility to create all the tables associated with a particular resource type from IBM FHIR Server version 4.0.1
 *
 * @implNote This is a copy of the FhirResourceTableGroup class from the IBM FHIR Server 4.0.1 release.
 *           Its copied to here in order to provide the DerbyMigrationTest with a way of creating the old schema.
 *           Moving forward, we expect to download and use the executable jar (fhir-persistence-schema-*-cli.jar)
 *           to create older versions of the schema, but version 4.0.1 doesn't support Derby so we can't.
 */
public class OldFhirResourceTableGroup {
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

    private static final String _LOGICAL_RESOURCES = "_LOGICAL_RESOURCES";
    private static final String _RESOURCES = "_RESOURCES";

    private static final String COMP = "COMP";
    private static final String ROW_ID = "ROW_ID";

    /**
     * The maximum number of components we can store in the X_COMPOSITES tables.
     * Per the current design, each component will add 6 columns to the table, so don't go too high.
     * Most of the composite parameters in the specification have 2 components, but a couple have 3.
     */
    private static final int MAX_COMP = 3;
    private static final String _STR = "_STR";
    private static final String _NUMBER = "_NUMBER";
    private static final String _DATE = "_DATE";
    private static final String _TOKEN = "_TOKEN";
    private static final String _QUANTITY = "_QUANTITY";
    private static final String _LATLNG = "_LATLNG";

    /**
     * Public constructor
     */
    public OldFhirResourceTableGroup(PhysicalDataModel model, String schemaName,
            Set<IDatabaseObject> procedureDependencies, Tablespace fhirTablespace, Collection<GroupPrivilege> privileges) {
        this.model = model;
        this.schemaName = schemaName;
        this.procedureDependencies = procedureDependencies;
        this.fhirTablespace = fhirTablespace;
        this.resourceTablePrivileges = privileges;
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
        addTokenValues(group, tablePrefix);
        addDateValues(group, tablePrefix);
        addNumberValues(group, tablePrefix);
        addLatLngValues(group, tablePrefix);
        addQuantityValues(group, tablePrefix);
        addComposites(group, tablePrefix);

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
        Table tbl = Table.builder(schemaName, tableName)
                .setTenantColumnName(MT_ID)
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addBigIntColumn(LOGICAL_RESOURCE_ID, false)
                .addVarcharColumn(LOGICAL_ID, LOGICAL_ID_BYTES, false)
                .addBigIntColumn(CURRENT_RESOURCE_ID, true)
                .addPrimaryKey(tableName + "_PK", LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint("FK_" + tableName + "_LRID", schemaName, LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                // Add indexes to avoid dead lock issue of derby, and improve Db2 performance
                // Derby requires all columns used in where clause to be indexed, otherwise whole table lock will be
                // used instead of row lock, which can cause dead lock issue frequently during concurrent accesses.
                .addIndex(IDX + tableName + CURRENT_RESOURCE_ID, CURRENT_RESOURCE_ID)
                .addIndex(IDX + tableName + LOGICAL_ID, LOGICAL_ID)
                .build(model);

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
                .setTenantColumnName(MT_ID)
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .addBigIntColumn(        RESOURCE_ID,              false)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,              false)
                .addIntColumn(            VERSION_ID,              false)
                .addTimestampColumn(    LAST_UPDATED,              false)
                .addCharColumn(           IS_DELETED,           1, false)
                .addBlobColumn(                 DATA,  2147483647,  10240,   true)
                .addUniqueIndex(tableName + "_PRF_IN1", prfIndexCols, prfIncludeCols)
                .addIndex(IDX + tableName + LOGICAL_RESOURCE_ID, LOGICAL_RESOURCE_ID)
                .addPrimaryKey(tableName + "_PK", RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .build(model);

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * Add the STR_VALUES table for the given resource name prefix
     * <pre>
  row_id                BIGINT             NOT NULL,
  parameter_name_id        INT             NOT NULL,
  str_value            VARCHAR(511 OCTETS),
  str_value_lcase      VARCHAR(511 OCTETS),
  resource_id           BIGINT             NOT NULL

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
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(             ROW_ID,      false)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addVarcharColumn(         STR_VALUE, msb,  true)
                .addVarcharColumn(   STR_VALUE_LCASE, msb,  true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PSR", PARAMETER_NAME_ID, STR_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PLR", PARAMETER_NAME_ID, STR_VALUE_LCASE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE)
                .addIndex(IDX + tableName + "_RPL", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE_LCASE)
                .addPrimaryKey(PK + tableName, ROW_ID)
                .setIdentityColumn(ROW_ID, Generated.BY_DEFAULT)
                .addForeignKeyConstraint(FK + tableName + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_RID", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * <pre>
  row_id                BIGINT NOT NULL,
  parameter_name_id        INT NOT NULL,
  code_system_id           INT NOT NULL,
  token_value          VARCHAR(255 OCTETS),
  resource_id           BIGINT NOT NULL
)
;

CREATE INDEX idx_device_token_values_pncscv ON device_token_values(parameter_name_id, code_system_id, token_value, resource_id);
CREATE INDEX idx_device_token_values_rps ON device_token_values(resource_id, parameter_name_id, code_system_id, token_value);
ALTER TABLE device_token_values ADD CONSTRAINT fk_device_token_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names;
ALTER TABLE device_token_values ADD CONSTRAINT fk_device_token_values_cs FOREIGN KEY (code_system_id)    REFERENCES code_systems;
ALTER TABLE device_token_values ADD CONSTRAINT fk_device_token_values_r  FOREIGN KEY (resource_id)       REFERENCES device_resources;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addTokenValues(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_TOKEN_VALUES";
        final String logicalResourcesTable = prefix + _LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(             ROW_ID,      false)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addIntColumn(        CODE_SYSTEM_ID,      false)
                .addVarcharColumn(       TOKEN_VALUE, 511,  true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PNCSCV", PARAMETER_NAME_ID, CODE_SYSTEM_ID, TOKEN_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE_SYSTEM_ID, TOKEN_VALUE)
                .addPrimaryKey(PK + tableName, ROW_ID)
                .setIdentityColumn(ROW_ID, Generated.BY_DEFAULT)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_CS", schemaName, CODE_SYSTEMS, CODE_SYSTEM_ID)
                .addForeignKeyConstraint(FK + tableName + "_R", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * <pre>
CREATE TABLE device_date_values  (
  row_id                BIGINT             NOT NULL,
  parameter_name_id         INT NOT NULL,
  date_value          TIMESTAMP,
  date_start          TIMESTAMP,
  date_end            TIMESTAMP,
  resource_id            BIGINT NOT NULL
)
;

CREATE INDEX idx_device_date_values_pvr ON device_date_values(parameter_name_id, date_value, resource_id);
CREATE INDEX idx_device_date_values_rpv  ON device_date_values(resource_id, parameter_name_id, date_value);
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
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(             ROW_ID,      false)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addTimestampColumn(      DATE_VALUE_DROPPED_COLUMN,      true)
                .addTimestampColumn(      DATE_START,      true)
                .addTimestampColumn(        DATE_END,      true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PVR", PARAMETER_NAME_ID, DATE_VALUE_DROPPED_COLUMN, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPV", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, DATE_VALUE_DROPPED_COLUMN)
                .addIndex(IDX + tableName + "_PSER", PARAMETER_NAME_ID, DATE_START, DATE_END, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PESR", PARAMETER_NAME_ID, DATE_END, DATE_START, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPSE", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, DATE_START, DATE_END)
                .addPrimaryKey(PK + tableName, ROW_ID)
                .setIdentityColumn(ROW_ID, Generated.BY_DEFAULT)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_R", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * <pre>
-- ----------------------------------------------------------------------------
--
-- ----------------------------------------------------------------------------
CREATE TABLE device_number_values  (
  row_id               BIGINT NOT NULL,
  parameter_name_id       INT NOT NULL,
  number_value         DOUBLE,
  resource_id          BIGINT NOT NULL
)
;
CREATE INDEX idx_device_number_values_pnnv ON device_number_values(parameter_name_id, number_value, resource_id);
CREATE INDEX idx_device_number_values_rps ON device_number_values(resource_id, parameter_name_id, number_value);
ALTER TABLE device_number_values ADD CONSTRAINT fk_device_number_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names ON DELETE CASCADE;
ALTER TABLE device_number_values ADD CONSTRAINT fk_device_number_values_r  FOREIGN KEY (resource_id)       REFERENCES device_resources ON DELETE CASCADE;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addNumberValues(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_NUMBER_VALUES";
        final String logicalResourcesTable = prefix + _LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(             ROW_ID,      false)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addDoubleColumn(       NUMBER_VALUE,       true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PNNV", PARAMETER_NAME_ID, NUMBER_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, NUMBER_VALUE)
                .addPrimaryKey(PK + tableName, ROW_ID)
                .setIdentityColumn(ROW_ID, Generated.BY_DEFAULT)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_RID", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
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
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(             ROW_ID,      false)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addDoubleColumn(     LATITUDE_VALUE,       true)
                .addDoubleColumn(    LONGITUDE_VALUE,       true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PNNLV", PARAMETER_NAME_ID, LATITUDE_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PNNHV", PARAMETER_NAME_ID, LONGITUDE_VALUE, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPLAT", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, LATITUDE_VALUE)
                .addIndex(IDX + tableName + "_RPLNG", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, LONGITUDE_VALUE)
                .addPrimaryKey(PK + tableName, ROW_ID)
                .setIdentityColumn(ROW_ID, Generated.BY_DEFAULT)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_RID", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
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

ALTER TABLE device_quantity_values ADD CONSTRAINT fk_device_quantity_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names ON DELETE CASCADE;
ALTER TABLE device_quantity_values ADD CONSTRAINT fk_device_quantity_values_r  FOREIGN KEY (resource_id)       REFERENCES device_resources ON DELETE CASCADE;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addQuantityValues(List<IDatabaseObject> group, String prefix) {
        final String tableName = prefix + "_QUANTITY_VALUES";
        final String logicalResourcesTable = prefix + _LOGICAL_RESOURCES;

        Table tbl = Table.builder(schemaName, tableName)
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(             ROW_ID,      false)
                .addIntColumn(     PARAMETER_NAME_ID,      false)
                .addVarcharColumn(              CODE, 255, false)
                .addDoubleColumn(     QUANTITY_VALUE,      true)
                .addDoubleColumn( QUANTITY_VALUE_LOW,      true)
                .addDoubleColumn(QUANTITY_VALUE_HIGH,      true)
                .addIntColumn(        CODE_SYSTEM_ID,      true)
                .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
                .addIndex(IDX + tableName + "_PNNV", PARAMETER_NAME_ID, CODE, QUANTITY_VALUE, LOGICAL_RESOURCE_ID, CODE_SYSTEM_ID)
                .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE, QUANTITY_VALUE, CODE_SYSTEM_ID)
                .addIndex(IDX + tableName + "_PCLHSR", PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_LOW, QUANTITY_VALUE_HIGH, CODE_SYSTEM_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PCHLSR", PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_HIGH, QUANTITY_VALUE_LOW, CODE_SYSTEM_ID, LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPCLHS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_LOW, QUANTITY_VALUE_HIGH, CODE_SYSTEM_ID)
                .addIndex(IDX + tableName + "_RPCHLS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, CODE, QUANTITY_VALUE_HIGH, QUANTITY_VALUE_LOW, CODE_SYSTEM_ID)
                .addPrimaryKey(PK + tableName, ROW_ID)
                .setIdentityColumn(ROW_ID, Generated.BY_DEFAULT)
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_R", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

    /**
     * Add the COMPOSITES table for the given resource name prefix
     * <pre>
  parameter_name_id       INT  NOT NULL,
  comp1_str            BIGINT,
  comp1_number         BIGINT,
  comp1_date           BIGINT,
  comp1_token          BIGINT,
  comp1_quantity       BIGINT,
  comp1_latlng         BIGINT,
  ...
  logical_resource_id  BIGINT  NOT NULL

CREATE INDEX idx_device_composites_pttr ON device_composites(parameter_name_id, comp1_token, comp2_token, resource_id);
CREATE INDEX idx_device_composites_ptqr ON device_composites(parameter_name_id, comp1_token, comp2_quantity, resource_id);
CREATE INDEX idx_device_composites_rptt ON device_composites(resource_id, parameter_name_id, comp1_token, comp2_token);
CREATE INDEX idx_device_composites_rptq ON device_composites(resource_id, parameter_name_id, comp1_token, comp2_quantity);
ALTER TABLE device_composites ADD CONSTRAINT fk_device_composites_pnid FOREIGN KEY (comp1_str)      REFERENCES device_str_values;
ALTER TABLE device_composites ADD CONSTRAINT fk_device_composites_rid  FOREIGN KEY (comp1_number)   REFERENCES device_number_values;
ALTER TABLE device_composites ADD CONSTRAINT fk_device_composites_rid  FOREIGN KEY (comp1_date)     REFERENCES device_date_values;
ALTER TABLE device_composites ADD CONSTRAINT fk_device_composites_rid  FOREIGN KEY (comp1_token)    REFERENCES device_token_values;
ALTER TABLE device_composites ADD CONSTRAINT fk_device_composites_rid  FOREIGN KEY (comp1_quantity) REFERENCES device_quantity_values;
ALTER TABLE device_composites ADD CONSTRAINT fk_device_composites_rid  FOREIGN KEY (comp1_latlng)   REFERENCES device_latlng_values;
...
ALTER TABLE device_quantity_values ADD CONSTRAINT fk_device_quantity_values_pn FOREIGN KEY (parameter_name_id) REFERENCES parameter_names ON DELETE CASCADE;
ALTER TABLE device_quantity_values ADD CONSTRAINT fk_device_quantity_values_r  FOREIGN KEY (resource_id)       REFERENCES device_logical_resources ON DELETE CASCADE;
     * </pre>
     * @param group
     * @param prefix
     */
    public void addComposites(List<IDatabaseObject> group, String prefix) {

        final String tableName = prefix + "_COMPOSITES";
        final String logicalResourcesTable = prefix + "_LOGICAL_RESOURCES";

        // Parameters are tied to the logical resource
        Table.Builder tbl = Table.builder(schemaName, tableName)
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addIntColumn(     PARAMETER_NAME_ID, false)
                .addBigIntColumn(LOGICAL_RESOURCE_ID, false);

        for (int i = 1; i <= MAX_COMP; i++) {
             String comp = COMP + i;
             tbl.addBigIntColumn(     comp + _STR, true)
                .addBigIntColumn(  comp + _NUMBER, true)
                .addBigIntColumn(    comp + _DATE, true)
                .addBigIntColumn(   comp + _TOKEN, true)
                .addBigIntColumn(comp + _QUANTITY, true)
                .addBigIntColumn(  comp + _LATLNG, true)
                .addForeignKeyConstraint(FK + tableName + _STR, schemaName, prefix + "_STR_VALUES", comp + _STR)
                .addForeignKeyConstraint(FK + tableName + _NUMBER, schemaName, prefix + "_NUMBER_VALUES", comp + _NUMBER)
                .addForeignKeyConstraint(FK + tableName + _DATE, schemaName, prefix + "_DATE_VALUES", comp + _DATE)
                .addForeignKeyConstraint(FK + tableName + _TOKEN, schemaName, prefix + "_TOKEN_VALUES", comp + _TOKEN)
                .addForeignKeyConstraint(FK + tableName + _QUANTITY, schemaName, prefix + "_QUANTITY_VALUES", comp + _QUANTITY)
                .addForeignKeyConstraint(FK + tableName + _LATLNG, schemaName, prefix + "_LATLNG_VALUES", comp + _LATLNG);
        }

        // add indexes for just the two common cases; token$token and token$quantity
        tbl.addIndex(IDX + tableName + "_PTTR", PARAMETER_NAME_ID, "COMP1_TOKEN", "COMP2_TOKEN", LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_PTQR", PARAMETER_NAME_ID, "COMP1_TOKEN", "COMP2_QUANTITY", LOGICAL_RESOURCE_ID)
                .addIndex(IDX + tableName + "_RPTT", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, "COMP1_TOKEN", "COMP2_TOKEN")
                .addIndex(IDX + tableName + "_RPTQ", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, "COMP1_TOKEN", "COMP2_QUANTITY")
                .addForeignKeyConstraint(FK + tableName + "_PN", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
                .addForeignKeyConstraint(FK + tableName + "_R", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                ;
        Table composites = tbl.build(model);
        group.add(composites);
        model.addTable(composites);
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
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn( LOGICAL_RESOURCE_ID,      false)
                .addIntColumn(       RESOURCE_TYPE_ID,      false)
                .addVarcharColumn(    ITEM_LOGICAL_ID, lib,  true)
                .addForeignKeyConstraint(FK + LIST_LOGICAL_RESOURCE_ITEMS + "_LRID", schemaName, LIST_LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + LIST_LOGICAL_RESOURCE_ITEMS + "_RTID", schemaName, RESOURCE_TYPES, RESOURCE_TYPE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
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
                .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
                .setTenantColumnName(MT_ID)
                .addBigIntColumn(         LOGICAL_RESOURCE_ID,      false)
                .addVarcharColumn(      CURRENT_PROBLEMS_LIST, lib,  true)
                .addVarcharColumn(   CURRENT_MEDICATIONS_LIST, lib,  true)
                .addVarcharColumn(     CURRENT_ALLERGIES_LIST, lib,  true)
                .addVarcharColumn(CURRENT_DRUG_ALLERGIES_LIST, lib,  true)
                .addPrimaryKey(PK + PATIENT_CURRENT_REFS, LOGICAL_RESOURCE_ID)
                .addForeignKeyConstraint(FK + PATIENT_CURRENT_REFS + "_LRID", schemaName, PATIENT_LOGICAL_RESOURCES, LOGICAL_RESOURCE_ID)
                .setTablespace(fhirTablespace)
                .addPrivileges(resourceTablePrivileges)
                .build(model)
                ;

        group.add(tbl);
        model.addTable(tbl);
    }

}