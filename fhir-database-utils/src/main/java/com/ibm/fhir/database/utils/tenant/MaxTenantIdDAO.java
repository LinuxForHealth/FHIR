/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to create a free tenant slot (to align with a new partition)
 */
public class MaxTenantIdDAO implements IDatabaseSupplier<Integer> {
    
    private final String schemaName;
    
    /**
     * Get partition information for all tables in the tableSchema, using
     * the catalogSchema as the schema containing the DATAPARTITIONS system table
     * @param schemaName
     */
    public MaxTenantIdDAO(String schemaName) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
    }
    
    
    /**
     * Execute the encapsulated query against the database and stream the result data to the
     * configured target
     * @param c
     */
    @Override
    public Integer run(IDatabaseTranslator translator, Connection c) {
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANTS");
        final String SQL = ""
                + "   SELECT MAX(mt_id) FROM " + tableName;

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int maxTenantId = rs.getInt(1);
                if (rs.wasNull()) {
                    return null;
                }
                else {
                    return maxTenantId;
                }
            }
            else {
                // Something broken with the SQL engine if this happens!
                throw new IllegalStateException("SELECT MAX... FROM " + tableName + " returned nothing");
            }
        }
        catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}
