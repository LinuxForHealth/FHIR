/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Wraps a {@link PreparedStatement} together with the statement text for easier
 * logging when there are errors
 */
public class PreparedStatementWrapper implements AutoCloseable {
    private final String statementText;
    private final PreparedStatement preparedStatement;

    /**
     * Canonical constructor
     * @param statementText
     * @param ps
     */
    public PreparedStatementWrapper(String statementText, PreparedStatement ps) {
        this.statementText = statementText;
        this.preparedStatement = ps;
    }

    /**
     * @return the statementText
     */
    public String getStatementText() {
        return statementText;
    }

    /**
     * @return the preparedStatement
     */
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    @Override
    public void close() throws SQLException {
        this.preparedStatement.close();
    }

    /**
     * Convenience method to delegate the call to the wrapped statement
     * @return
     * @throws SQLException
     */
    public ResultSet executeQuery() throws SQLException {
        return preparedStatement.executeQuery();
    }
}
