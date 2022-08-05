/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.model.OrderedColumnDef;

/**
 * Create an index
 */
public class CreateIndexStatement implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(CreateIndexStatement.class.getName());
    private final String schemaName;
    private final String indexName;
    private final String tableName;
    private final List<OrderedColumnDef> columns;
    private final boolean unique;

    /**
     * Public constructor. Non-unique index
     * @param schemaName
     * @param indexName
     * @param tableName
     * @param tenantColumnName
     * @param columns
     */
    public CreateIndexStatement(String schemaName, String indexName, String tableName, List<OrderedColumnDef> columns) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(indexName);
        DataDefinitionUtil.assertValidName(tableName);

        this.schemaName = schemaName;
        this.indexName = indexName;
        this.tableName = tableName;
        this.columns = new ArrayList<>(columns);
        this.unique = false;
    }

    /**
     * Public constructor
     * @param schemaName
     * @param indexName
     * @param tableName
     * @param tenantColumnName
     * @param columns
     * @param unique
     */
    public CreateIndexStatement(String schemaName, String indexName, String tableName, List<OrderedColumnDef> columns, boolean unique) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(indexName);
        DataDefinitionUtil.assertValidName(tableName);

        this.schemaName = schemaName;
        this.indexName = indexName;
        this.tableName = tableName;
        this.columns = new ArrayList<>(columns);
        this.unique = unique;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        final String ddl;
        if (this.unique) {
            ddl = DataDefinitionUtil.createUniqueIndex(schemaName, tableName, indexName, this.columns, translator.isIndexUseSchemaPrefix());
        } else {
            ddl = DataDefinitionUtil.createIndex(schemaName, tableName, indexName, this.columns, translator.isIndexUseSchemaPrefix());
        }

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        } catch (SQLException x) {
            // useful to know which index is causing the problem
            logger.severe("DDL FAILED: " + ddl);
            throw translator.translate(x);
        }
    }
}
