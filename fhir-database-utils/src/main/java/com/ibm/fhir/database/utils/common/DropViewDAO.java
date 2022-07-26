/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Executes a DROP VIEW statement
 */
public class DropViewDAO implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(DropViewDAO.class.getName());
    
    private final String schemaName;
    
    private final String viewName;
    
    private final boolean propagateError;
    
    /**
     * Public constructor
     * @param schemaName
     * @param viewName
     * @param propagateError if false, errors will be suppressed, if true, they will be propagated
     */
    public DropViewDAO(String schemaName, String viewName, boolean propagateError) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(viewName);
        this.schemaName = schemaName;
        this.viewName = viewName;
        this.propagateError = propagateError;
    }
    
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qualifiedName = DataDefinitionUtil.getQualifiedName(schemaName, viewName);
        final StringBuilder ddl = new StringBuilder();
        ddl.append("DROP VIEW ");
        ddl.append(qualifiedName);

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl.toString());
        } catch (SQLException x) {
            if (propagateError) {
                logger.log(Level.SEVERE, ddl.toString(), x);
                throw translator.translate(x);
            }
        }
    }
}