/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.schema;

import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.version.SchemaConstants;

/**
 * Update the schema version recorded in the WHOLE_SCHEMA_VERSION table
 * Postgresql-specific to handle ON CONFLICT DO NOTHING
 */
public class UpdateSchemaVersionPostgresql extends UpdateSchemaVersion {
        
    /**
     * Public constructor
     * 
     * @param schemaName
     * @param version
     */
    public UpdateSchemaVersionPostgresql(String schemaName, int version) {
        super(schemaName, version);
    }
    
    @Override
    protected String getInsertSQL(String schemaName) {
        final String WHOLE_SCHEMA_VERSION = DataDefinitionUtil.getQualifiedName(schemaName, SchemaConstants.WHOLE_SCHEMA_VERSION);
        final String INS = "INSERT INTO " + WHOLE_SCHEMA_VERSION + " ("
                + " record_id, version_id) "
                + " VALUES (1, ?) "
                + " ON CONFLICT DO NOTHING";
        return INS;
    }
}