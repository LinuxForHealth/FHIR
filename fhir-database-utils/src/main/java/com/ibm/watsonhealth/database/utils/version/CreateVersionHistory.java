/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.version;

import com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter;
import com.ibm.watsonhealth.database.utils.model.PhysicalDataModel;
import com.ibm.watsonhealth.database.utils.model.Table;

/**
 * We don't add the version_history to the {@link PhysicalDataModel} because
 * it's a schema management table, and the model shouldn't really care about
 * it.
 * @author rarnold
 */
public class CreateVersionHistory {

    /**
     * Create the version history table in the given (admin) schema. This
     * table is used to determine the current version of the schema, and
     * hence which changes should be applied in order to migrate the
     * schema to the latest version.
     * 
     * As this is the version table itself, it is assigned a version id
     * of 0 which is used to indicate that versioning doesn't apply.
     * 
     * The versioning of the admin schema is tracked separately, and each
     * data (fhiruser) schema also has its own version level, so we need
     * a version history table created in the admin (e.g. FHIRADMIN) schema
     * as well as the managed data schemas. This is up to the layer above
     * to organize. This method simply does what it's told and creates a
     * table in the given schema.
     * @param schemaName
     * @param target
     */
    public static void createTableIfNeeded(String schemaName, IDatabaseAdapter target) {
        PhysicalDataModel dataModel = new PhysicalDataModel();
        Table t = Table.builder(schemaName, SchemaConstants.VERSION_HISTORY)
                .setVersion(0)
                .addVarcharColumn(SchemaConstants.OBJECT_TYPE, 16, false)
                .addVarcharColumn(SchemaConstants.OBJECT_NAME, 64, false)
                .addIntColumn(SchemaConstants.VERSION, false)
                .addTimestampColumn(SchemaConstants.APPLIED, false)
                .addPrimaryKey("PK_" + SchemaConstants.VERSION_HISTORY, SchemaConstants.OBJECT_TYPE, SchemaConstants.OBJECT_NAME, SchemaConstants.VERSION)
                .build(dataModel);
        dataModel.addTable(t);
        dataModel.addObject(t);
        
        // apply this data model to the target if necessary - note - this bypasses the
        // version history table...because this is the table we're trying to create!
        if (!t.exists(target)) {
            dataModel.apply(target);            
        }
    }

}
