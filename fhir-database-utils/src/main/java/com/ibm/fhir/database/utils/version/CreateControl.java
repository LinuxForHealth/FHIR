/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.version;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Table;

/**
 * Creates the admin CONTROL table.
 * <p>
 * The CONTROL table is added before all other tables and is used to ensure
 * we have only a single instance of the schema tool running at a time.
 */
public class CreateControl {
    private static final Logger logger = Logger.getLogger(CreateControl.class.getName());
    public static final String SCHEMA_GROUP_TAG = "SCHEMA_GROUP";
    public static final String ADMIN_GROUP = "FHIR_ADMIN";

    /**
     * Builds the definition of the admin CONTROL table
     * @param dataModel
     * @param adminSchemaName
     * @param addTags
     * @return
     */
    public static Table buildTableDef(PhysicalDataModel dataModel, String adminSchemaName, boolean addTags) {
        Table t = Table.builder(adminSchemaName, SchemaConstants.CONTROL)
                .setVersion(0)
                .addVarcharColumn(        SchemaConstants.SCHEMA_NAME, 128, false)
                .addVarcharColumn(   SchemaConstants.LEASE_OWNER_HOST,  64, false)
                .addVarcharColumn(   SchemaConstants.LEASE_OWNER_UUID,  36, false)
                .addTimestampColumn(      SchemaConstants.LEASE_UNTIL,      false)
                .addPrimaryKey("PK_CONTROL", SchemaConstants.SCHEMA_NAME)
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
        SchemaApplyContext context = SchemaApplyContext.getDefault();
        PhysicalDataModel dataModel = new PhysicalDataModel();

        Table t = buildTableDef(dataModel, adminSchemaName, false);

        // apply this data model to the target if necessary - note - this bypasses the
        // version history table because this table is expected to be the very first
        // table that gets built
        if (!t.exists(target)) {
            // we need to protect against concurrency here. Because we can't lock
            // until this table exists, there's a chance two instances of the schema
            // update tool could try to build the table. The solution is to make it
            // idempotent...if the table exists already, that's success
            try {
                dataModel.apply(target, context);
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