/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.tenant;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Executes a CREATE OR REPLACE VIEW statement
 */
public class CreateOrReplaceViewDAO implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(CreateOrReplaceViewDAO.class.getName());
    
    private final String schemaName;
    
    private final String viewName;
    
    private final String selectClause;
    
    private final boolean createOrReplace;

    /**
     * Public constructor
     * @param schemaName
     * @param viewName
     * @param selectClause
     * @param createOrReplace
     */
    public CreateOrReplaceViewDAO(String schemaName, String viewName, String selectClause, boolean createOrReplace) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(viewName);
        this.schemaName = schemaName;
        this.viewName = viewName;
        this.selectClause = selectClause;
        this.createOrReplace = createOrReplace;
    }
    
    /**
     * Public constructor with createOrReplace default to true
     * @param schemaName
     * @param viewName
     * @param selectClause
     * @param createOrReplace
     */
    public CreateOrReplaceViewDAO(String schemaName, String viewName, String selectClause) {
        this(schemaName, viewName, selectClause, true);
    }
    
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qualifiedName = DataDefinitionUtil.getQualifiedName(schemaName, viewName);
        final StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE").append(this.createOrReplace ? " OR REPLACE VIEW " : " VIEW ");
        ddl.append(qualifiedName);
        ddl.append(" AS ");
        ddl.append(selectClause);

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl.toString());
        } catch (SQLException x) {
            logger.log(Level.SEVERE, ddl.toString(), x);
            throw translator.translate(x);
        }
    }
}