/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Drop columns from the view identified by schema and view name
 */
public class DropView implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(DropView.class.getName());
    private final String schemaName;
    private final String viewName;

    /**
     * Public constructor
     * @param schemaName
     * @param viewName
     */
    public DropView(String schemaName, String viewName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(viewName);
        this.schemaName = schemaName;
        this.viewName = viewName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, viewName);
        final String ddl = translator.dropView(qname);

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl.toString());
        } catch (SQLException x) {
            // just log because this means that the view doesn't yet exist
            logger.warning("Drop view statement failed: '" + ddl + "': " + x.getMessage());
        }
    }
}
