/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.version.SchemaConstants;

/**
 * Get the current schema version from the WHOLE_SCHEMA_VERSION table
 */
public class GetSchemaVersion implements IDatabaseSupplier<Integer> {
    
    // The FHIR data schema
    private final String schemaName;
    
    /**
     * Public constructor
     * 
     * @param schemaName
     */
    public GetSchemaVersion(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public Integer run(IDatabaseTranslator translator, Connection c) {
        Integer result;

        // The WHOLE_SCHEMA_VERSION table should only contain a single row, which will
        // always have a record_id = 1.
        final String WHOLE_SCHEMA_VERSION = DataDefinitionUtil.getQualifiedName(schemaName, SchemaConstants.WHOLE_SCHEMA_VERSION);
        final String SEL = "SELECT version_id FROM " + WHOLE_SCHEMA_VERSION + " WHERE record_id = 1";
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SEL);
            if (rs.next()) {
                result = rs.getInt(1);
            } else {
                result = -1;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return result;
    }
}