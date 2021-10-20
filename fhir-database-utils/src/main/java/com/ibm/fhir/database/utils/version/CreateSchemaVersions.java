/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.version;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Table;

/**
 * Creates the SCHEMA_VERSIONS table. Although this is an administration
 * table, for least privileges it is stored in the data schema, not the
 * admin schema. This makes it easier to keep data private so that tenants
 * aren't able to see the schema version used by other tenants, but do
 * get to see their own tenant version. This is because the table will
 * contain only one row. For Db2 multitenant, all tenants
 * in a schema share the same version, so we don't need to add mt_id,
 * making things a little simpler.
 * <p>
 */
public class CreateSchemaVersions {
    private static final Logger logger = Logger.getLogger(CreateControl.class.getName());
    public static final String SCHEMA_GROUP_TAG = "SCHEMA_GROUP";
    public static final String ADMIN_GROUP = "FHIR_ADMIN";

    /**
     * Builds the definition of the SCHEMA_VERSIONS table in the schema
     * identified by schemaName
     * @param dataModel
     * @param schemaName
     * @param addTags
     * @return the {@link Table} definition
     */
    public static Table buildTableDef(PhysicalDataModel dataModel, String schemaName, boolean addTags) {
        // RECORD_ID is used to make sure we only put one row in this table
        // It is the primary key, and we add a check constraint RECORD_ID = 1
        // so the two restrictions combined enforce a single row
        Table t = Table.builder(schemaName, SchemaConstants.SCHEMA_VERSIONS)
                .setVersion(0)
                .addIntColumn(SchemaConstants.RECORD_ID, false)
                .addIntColumn(SchemaConstants.VERSION_ID, false)
                .addPrimaryKey("PK_SCHEMA_VERSIONS", SchemaConstants.RECORD_ID)
                .addCheckConstraint("CK_ONE_ROW", "RECORD_ID = 1")
                .build(dataModel);
        dataModel.addTable(t);
        dataModel.addObject(t);

        if (addTags) {
            t.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        }

        return t;
    }

    /**
     * Create the CONTROL table in the admin schema.
     * <br>
     * Version id of 0 is used to imply that this table
     * is not versioned (it is created before the VERSION_HISTORY
     * table and therefore is not managed by it)
     *
     * @param adminSchemaName
     * @param target
     */
    public static void createTableIfNeeded(String adminSchemaName, IDatabaseAdapter target) {
        PhysicalDataModel dataModel = new PhysicalDataModel();

        Table t = buildTableDef(dataModel, adminSchemaName, false);

        // apply this data model to the target if necessary - note - this table
        // is not managed in the VERSION_HISTORY table
        if (!t.exists(target)) {
            // we need to protect against concurrency here. Because we can't lock
            // until this table exists, there's a chance two instances of the schema
            // update tool could try to build the table. The solution is to make it
            // idempotent...if the table exists already, that's success
            try {
                dataModel.apply(target);
            } catch (Exception x) {
                if (t.exists(target)) {
                    logger.info("Table '" + t.getQualifiedName() + "' already exists; skipping create");
                } else {
                    throw x;
                }
            }
        }
    }
}