/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Drop an index from a given schema by name 
 */
public class DropIndex implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(DropIndex.class.getName());
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
        } catch (SQLException x) {
            // useful to know which index is causing the problem
            logger.severe("FAILED: DROP INDEX " + qname);
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
