/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Check to see if we have any data in the given table.
 */
public class TableHasData implements IDatabaseSupplier<Boolean> {

    // The FHIR data schema
    private final String schemaName;
    
    // The FHIR table name
    private final String tableName;
    
    // The Adapter
    private final IDatabaseAdapter adapter;

    
   
    /**
     * Instantiates a new table has data.
     *
     * @param schemaName the schema name
     * @param tableName the table name
     * @param adapter the adapter
     */
    public TableHasData(String schemaName, String tableName, IDatabaseAdapter adapter) {
        this.schemaName =DataDefinitionUtil.assertValidName(schemaName);
        this.tableName = DataDefinitionUtil.assertValidName(tableName);
        this.adapter = adapter;
    }

    /**
     * Execute the statement using the connection and return the value.
     * @param translator the translator
     * @param c the c
     * @return the boolean. Returns true if data is available in the given table 
     */
    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = false;
        if (adapter.doesTableExist(schemaName, tableName)) {
            final String qualifiedTableName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
            final String SQL = "SELECT 1 "
                    + "  FROM " + qualifiedTableName + " "
                    + translator.limit("1");
            try (Statement s = c.createStatement()) {
                ResultSet rs = s.executeQuery(SQL);
                if (rs.next()) {
                    result = true;
                }
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        }
        return result;
    }
}