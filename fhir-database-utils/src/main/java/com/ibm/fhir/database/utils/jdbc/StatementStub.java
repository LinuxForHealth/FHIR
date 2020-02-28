/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Simple stub implementation of a JDBC statement, useful for some simple unit-tests
 * of things beyond the capability of Derby
 */
public class StatementStub implements Statement {
    private final ConnectionStub connection;

    public StatementStub(ConnectionStub connection) {
        this.connection = connection;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public void addBatch(String arg0) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean execute(String arg0) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String arg0, int arg1) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String arg0, int[] arg1) throws SQLException {

        return false;
    }

    @Override
    public boolean execute(String arg0, String[] arg1) throws SQLException {
        return false;
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return null;
    }

    @Override
    public ResultSet executeQuery(String arg0) throws SQLException {
        return new ResultSetStub(connection);
    }

    @Override
    public int executeUpdate(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String arg0, int arg1) throws SQLException {

        return 0;
    }

    @Override
    public int executeUpdate(String arg0, int[] arg1) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String arg0, String[] arg1) throws SQLException {
        return 0;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public boolean getMoreResults(int arg0) throws SQLException {
        return false;
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void setCursorName(String arg0) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean arg0) throws SQLException {

    }

    @Override
    public void setFetchDirection(int arg0) throws SQLException {

    }

    @Override
    public void setFetchSize(int arg0) throws SQLException {

    }

    @Override
    public void setMaxFieldSize(int arg0) throws SQLException {

    }

    @Override
    public void setMaxRows(int arg0) throws SQLException {

    }

    @Override
    public void setPoolable(boolean arg0) throws SQLException {

    }

    @Override
    public void setQueryTimeout(int arg0) throws SQLException {

    }
}