/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.version.SchemaConstants;

/**
 * Update the schema version recorded in the SCHEMA_VERSIONS table
 */
public class UpdateSchemaVersion implements IDatabaseStatement {
    
    // The FHIR data schema for which we want to obtain the lease
    private final String schemaName;

    // The FHIR schema version
    private final FhirSchemaVersion version;
    
    /**
     * Public constructor
     * @param schemaName
     */
    public UpdateSchemaVersion(String schemaName, FhirSchemaVersion version) {
        this.schemaName = schemaName;
        this.version = version;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // There shouldn't be any concurrency issues, because updates are serialized
        // using the CONTROL table and its lease mechanism (see LeaseManager)
        final String SCHEMA_VERSIONS = DataDefinitionUtil.getQualifiedName(schemaName, SchemaConstants.SCHEMA_VERSIONS);
        final String INS = "INSERT INTO " + SCHEMA_VERSIONS + " ("
                + " record_id, version_id) "
                + " VALUES (1, ?)";
        
        boolean locked = false;
        while (!locked) {
            try (PreparedStatement ps = c.prepareStatement(INS)) {
                ps.setInt(1, version.vid());
                
                ps.executeUpdate();
                locked = true; // we inserted the row, so we must own the lock
            } catch (SQLException x) {
                // if the row is a duplicate, we drop through to the SEL statement
                if (!translator.isDuplicate(x)) {
                    throw translator.translate(x);
                }
            }
            
            // Failed because there's a duplicate row...so now we just want to update it
            if (!locked) {
                final String UPD = "UPDATE " + SCHEMA_VERSIONS
                        + "  SET version_id = ? "
                        + "WHERE record_id = 1 ";

                try (PreparedStatement ps = c.prepareStatement(UPD)) {
                    ps.setInt(1, version.vid());
                    
                    // Note that if we tried to update to update the row but no rows were affected,
                    // it means that we were unable to obtain the lease because it is held by another
                    // instance, hence result == FALSE
                    int rows = ps.executeUpdate();
                    if (rows == 1) {
                        locked = true; // we're done
                    }
                } catch (SQLException x) {
                    throw translator.translate(x);
                }
            }
        }
    }
}