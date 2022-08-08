/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Connection Stub
 */
public class ConnectionStub implements Connection {

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // Stub Only
        return false;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new StatementStub(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new PreparedStatementStub(this);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        // Stub Only
        
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        // Stub Only
        return false;
    }

    @Override
    public void commit() throws SQLException {
        // Stub Only
        
    }

    @Override
    public void rollback() throws SQLException {
        // Stub Only
        
    }

    @Override
    public void close() throws SQLException {
        // Stub Only
        
    }

    @Override
    public boolean isClosed() throws SQLException {
        // Stub Only
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        // Stub Only
        
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        // Stub Only
        return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        // Stub Only
        
    }

    @Override
    public String getCatalog() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        // Stub Only
        
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        // Stub Only
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        // Stub Only
        
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        // Stub Only
        
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        // Stub Only
        
    }

    @Override
    public int getHoldability() throws SQLException {
        // Stub Only
        return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        // Stub Only
        
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        // Stub Only
        
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return new PreparedStatementStub(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        // Stub Only
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        // Stub Only
        
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        // Stub Only
        
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        // Stub Only
        
    }

    @Override
    public String getSchema() throws SQLException {
        // Stub Only
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        // Stub Only
        
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        // Stub Only
        
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        // Stub Only
        return 0;
    }

}
