/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.common.SchemaInfoObject.Type;

/**
 * DAO to fetch the names of sequences in the given schema
 */
public class PostgresListSequencesForSchema implements IDatabaseSupplier<List<SchemaInfoObject>> {
    
    // The schema of the table
    private final String schemaName;

    /**
     * Public constructor
     * @param schemaName
     */
    public PostgresListSequencesForSchema(String schemaName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
    }

    @Override
    public List<SchemaInfoObject> run(IDatabaseTranslator translator, Connection c) {
        List<SchemaInfoObject> result = new ArrayList<>();
        // Grab the list of sequences for the configured schema from the PostgreSQL schema
        // catalog
        final String sql = ""
                + "SELECT sequence_name FROM information_schema.sequences "
                + " WHERE sequence_schema = ?";
        
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, schemaName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new SchemaInfoObject(Type.SEQUENCE, rs.getString(1)));
            }
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    }
}