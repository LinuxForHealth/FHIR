/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to create a free tenant slot (to align with a new partition)
 */
public class GetSequenceNextValueDAO implements IDatabaseSupplier<Long> {

    // the name of the schema with the sequence
    private final String schemaName;
    
    // the name of the sequence
    private final String sequenceName;
    
    /**
     * DAO to get the next value from the named sequence
     * @param schemaName
     * @param sequenceName
     */
    public GetSequenceNextValueDAO(String schemaName, String sequenceName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(sequenceName);
        this.schemaName = schemaName;
        this.sequenceName = sequenceName;
    }
    /**
     * Execute the encapsulated query against the database and stream the result data to the
     * configured target
     * @param c
     */
    @Override
    public Long run(IDatabaseTranslator translator, Connection c) {
        // you can't get the current value before calling next value in a given session,
        // so we simply bump the sequence number. The translator is used to support
        // our different database flavors (e.g. Derby, DB2 and PostgreSQL)
        final String SQL = translator.selectSequenceNextValue(schemaName, sequenceName);

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long currentValue = rs.getInt(1);
                if (rs.wasNull()) {
                    return null;
                } else {
                    return currentValue;
                }
            } else {
                // Something broken with the SQL engine if this happens!
                throw new IllegalStateException(SQL + " returned nothing");
            }
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}
