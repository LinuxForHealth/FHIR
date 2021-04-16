/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.query.expression.StringStatementRenderer;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;

/**
 *
 */
public class QueryUtil {
    private static final Logger logger = Logger.getLogger(QueryUtil.class.getName());

    /**
     * Prepares the given Select statement and sets any bind parameters
     * @param select
     * @return
     * @throws SQLException
     */
    public static PreparedStatement prepareSelect(Connection connection, Select select, IDatabaseTranslator translator) throws SQLException {

        // Render the statement to a database-specific string
        final StringStatementRenderer statementRenderer = new StringStatementRenderer(translator, true);
        final String query = select.render(statementRenderer);
        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        select.gatherBindMarkers(bindMarkers);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("bind marker count: " + bindMarkers.size());
            logger.fine("     query string: " + query);
        }

        // The caller is responsible for closing the statement
        PreparedStatement ps = connection.prepareStatement(query);

        // Set values on the statement for each of the bind markers
        BindVisitor bindVisitor = new BindVisitor(ps, translator);
        for (BindMarkerNode bindMarker: bindMarkers) {
            bindMarker.visit(bindVisitor);
        }

        // This statement is ready to execute
        return ps;
    }
}