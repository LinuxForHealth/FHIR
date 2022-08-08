/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.version;

import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;
import org.linuxforhealth.fhir.database.utils.model.PhysicalDataModel;
import org.linuxforhealth.fhir.database.utils.model.Privilege;
import org.linuxforhealth.fhir.database.utils.model.Table;

/**
 * Creates the WHOLE_SCHEMA_VERSION table. Although this is an administration
 * table, for least privileges it is stored in the data schema, not the
 * admin schema. This makes it easier to keep data private so that tenants
 * aren't able to see the schema version used by other tenants, but do
 * get to see their own tenant version. This is because the table will
 * contain only one row.
 * <p>
 */
public class CreateWholeSchemaVersion {
    private static final Logger logger = Logger.getLogger(CreateControl.class.getName());
    public static final String SCHEMA_GROUP_TAG = "SCHEMA_GROUP";
    public static final String ADMIN_GROUP = "FHIR_ADMIN";

    /**
     * Builds the definition of the WHOLE_SCHEMA_VERSION table in the schema
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
        Table t = Table.builder(schemaName, SchemaConstants.WHOLE_SCHEMA_VERSION)
                .setVersion(0)
                .addIntColumn(SchemaConstants.RECORD_ID, false)
                .addIntColumn(SchemaConstants.VERSION_ID, false)
                .addPrimaryKey("PK_WHOLE_SCHEMA_VERSION", SchemaConstants.RECORD_ID)
                .addCheckConstraint("CK_ONE_ROW", "RECORD_ID = 1")
                .build(dataModel);
        dataModel.addTable(t);
        dataModel.addObject(t);

        // The FHIRUSER (sometimes FHIRSERVER) database user needs SELECT on this table
        t.addPrivilege(SchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.SELECT);

        if (addTags) {
            t.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
        }

        return t;
    }

    /**
     * Create the WHOLE_SCHEMA_VERSION table. The table is created in the
     * target data schema, not the admin schema.
     * <br>
     *
     * @param schemaName
     * @param target
     */
    public static void createTableIfNeeded(String schemaName, ISchemaAdapter target) {
        PhysicalDataModel dataModel = new PhysicalDataModel();
        SchemaApplyContext context = SchemaApplyContext.getDefault();

        Table t = buildTableDef(dataModel, schemaName, false);

        // apply this data model to the target if necessary - note - this table
        // is not managed in the VERSION_HISTORY table
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

    /**
     * Drop the WHOLE_SCHEMA_VERSION table if it exists in the given schema
     * @param schemaName
     * @param target
     */
    public static void dropTable(String schemaName, ISchemaAdapter target) {
        PhysicalDataModel dataModel = new PhysicalDataModel();

        Table t = buildTableDef(dataModel, schemaName, false);

        // apply this data model to the target if necessary - note - this table
        // is not managed in the VERSION_HISTORY table so we need to perform this
        // manually.
        if (t.exists(target)) {
            dataModel.drop(target);
        }
    }

    /**
     * Grant the user privileges so that the row from this table can
     * be read by the $healthcheck custom operation.
     * @param target
     * @param groupName
     * @param toUser
     */
    public static void grantPrivilegesTo(ISchemaAdapter target, String schemaName, String groupName, String toUser) {
        PhysicalDataModel dataModel = new PhysicalDataModel();
        Table t = buildTableDef(dataModel, schemaName, false);
        t.grant(target, groupName, toUser);
    }
}