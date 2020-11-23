/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.model.OrderedColumnDef;

/**
 * Create an index
 */
public class CreateIndexStatement implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(CreateIndexStatement.class.getName());
    private final String schemaName;
    private final String indexName;
    private final String tableName;
    private final String tenantColumnName;
    private final List<OrderedColumnDef> columns;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public CreateIndexStatement(String schemaName, String indexName, String tableName, String tenantColumnName, List<OrderedColumnDef> columns) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(indexName);
        DataDefinitionUtil.assertValidName(tableName);
        if (tenantColumnName != null) {
            DataDefinitionUtil.assertValidName(tenantColumnName);
        }
        
        this.schemaName = schemaName;
        this.indexName = indexName;
        this.tableName = tableName;
        this.tenantColumnName = tenantColumnName;
        this.columns = new ArrayList<>(columns);
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        
        List<OrderedColumnDef> cols;
        if (this.tenantColumnName == null) {
            cols = this.columns;
        } else {
            // prepend the column list with the tenant column
            cols = new ArrayList<>(this.columns.size() + 1);
            cols.add(new OrderedColumnDef(tenantColumnName, null, null));
            cols.addAll(this.columns);
        }
        final String ddl = DataDefinitionUtil.createIndex(schemaName, tableName, indexName, cols, translator.isIndexUseSchemaPrefix());

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        } catch (SQLException x) {
            // useful to know which index is causing the problem
            logger.severe("DDL FAILED: " + ddl);
            throw translator.translate(x);
        }
    }
}
