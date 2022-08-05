/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.version.SchemaConstants;

/**
 * Update the schema version recorded in the WHOLE_SCHEMA_VERSION table
 */
public class UpdateSchemaVersion implements IDatabaseStatement {
    
    // The FHIR data schema for which we want to obtain the lease
    private final String schemaName;

    // The schema version
    private final int version;
    
    /**
     * Public constructor
     * 
     * @param schemaName
     * @param version
     */
    public UpdateSchemaVersion(String schemaName, int version) {
        this.schemaName = schemaName;
        this.version = version;
    }

    /**
     * Get the statement string for inserting a record into WHOLE_SCHEMA_VERSION
     * @return the SQL insert statement
     */
    protected String getInsertSQL(String schemaName) {
        final String WHOLE_SCHEMA_VERSION = DataDefinitionUtil.getQualifiedName(schemaName, SchemaConstants.WHOLE_SCHEMA_VERSION);
        final String result = "INSERT INTO " + WHOLE_SCHEMA_VERSION + " ("
                + " record_id, version_id) "
                + " VALUES (1, ?)";
        return result;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // There shouldn't be any concurrency issues, because updates are serialized
        // using the CONTROL table and its lease mechanism (see LeaseManager)
        final String INS = getInsertSQL(this.schemaName);        
        boolean complete = false;
        while (!complete) {
            try (PreparedStatement ps = c.prepareStatement(INS)) {
                ps.setInt(1, version);
                
                if (1 == ps.executeUpdate()) {
                    complete = true; // we inserted the row, so we must own the lock
                }
            } catch (SQLException x) {
                // if the row is a duplicate, we drop through to the SEL statement
                if (!translator.isDuplicate(x)) {
                    throw translator.translate(x);
                }
            }
            
            // Failed because there's a duplicate row...so now we just want to update it
            if (!complete) {
                final String WHOLE_SCHEMA_VERSION = DataDefinitionUtil.getQualifiedName(schemaName, SchemaConstants.WHOLE_SCHEMA_VERSION);
                final String UPD = "UPDATE " + WHOLE_SCHEMA_VERSION
                        + "  SET version_id = ? "
                        + "WHERE record_id = 1 ";

                try (PreparedStatement ps = c.prepareStatement(UPD)) {
                    ps.setInt(1, version);
                    
                    // Note that if we tried to update the row but no rows were affected,
                    // it means that the row was deleted before we could change it. If
                    // that's the case, we just repeat the loop
                    int rows = ps.executeUpdate();
                    if (rows == 1) {
                        complete = true;
                    }
                } catch (SQLException x) {
                    throw translator.translate(x);
                }
            }
        }
    }
}