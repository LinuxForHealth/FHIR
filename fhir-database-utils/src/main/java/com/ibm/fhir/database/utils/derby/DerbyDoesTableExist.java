/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * For this implementation we simply run a select against the table
 * and see if it kicks back with an error (which isn't related to
 * connection failure).
 */
public class DerbyDoesTableExist implements IDatabaseSupplier<Boolean> {
    
    // The schema of the table
    private final String schemaName;

    // The name of the table
    private final String tableName;
    
    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public DerbyDoesTableExist(String schemaName, String tableName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
        this.tableName = DataDefinitionUtil.assertValidName(tableName);
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result;
        final String tbl = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String sql = "SELECT 1 FROM " + tbl + " WHERE 1=0";
        
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeQuery();
            result = true;
        }
        catch (SQLException x) {
            if (translator.isConnectionError(x)) {
                throw translator.translate(x);
            }
            else {
                result = false;
            }
        }
        
        return result;
    }
}
