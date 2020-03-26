/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Drop an index from a given schema by name 
 */
public class DropIndex implements IDatabaseStatement {
    private final String schemaName;
    private final String indexName;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public DropIndex(String schemaName, String indexName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(indexName);
        this.schemaName = schemaName;
        this.indexName = indexName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, indexName);

        try (Statement s = c.createStatement()) {
            s.executeUpdate("DROP INDEX " + qname);
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * @return the indexNames
     */
    public String getIndexName() {
        return indexName;
    }
}
