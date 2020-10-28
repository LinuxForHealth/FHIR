/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.proxy;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapping of a real database connection so that we can intercept the close call
 * and therefore reuse the connection. Note that we also listen for any exceptions
 * generated, and assume (for better or for worse) that something might be broken
 * so call back to the connection provider so that it can decide whether or not
 * to abandon the underlying connection.
 */
public class FHIRProxyConnection implements Connection {
    private static final Logger logger = Logger.getLogger(FHIRProxyConnection.class.getName());

    // The actual connection we're wrapping (decorating)
    private final Connection wrapped;
    
    // Remember the stack from the last usage
    private Exception lastStack;
    
    /**
     * Public constructor
     * @param wrappee
     */
    public FHIRProxyConnection(Connection wrappee) {
        this.wrapped = wrappee;
    }

    @Override
    public void clearWarnings() throws SQLException {
        wrapped.clearWarnings();
    }

    @Override
    public void close() throws SQLException {
        if (lastStack != null) {
            logger.log(Level.INFO, "LAST USAGE", lastStack);
            lastStack = null;
        }
        wrapped.close();
    }

    @Override
    public void commit() throws SQLException {
        wrapped.commit();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
                    throws SQLException {

        
        return wrapped.createArrayOf(typeName, elements);
    }

    @Override
    public Blob createBlob() throws SQLException {
        return wrapped.createBlob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return wrapped.createClob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return wrapped.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return wrapped.createSQLXML();
    }

    @Override
    public Statement createStatement() throws SQLException {
        lastStack = new Exception();
        return wrapped.createStatement();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
                    throws SQLException {
        lastStack = new Exception();
        return wrapped.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement(int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
        lastStack = new Exception();
        return wrapped.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
                    throws SQLException {
        return wrapped.createStruct(typeName, attributes);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return wrapped.getAutoCommit();
    }

    @Override
    public String getCatalog() throws SQLException {
        return wrapped.getCatalog();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return wrapped.getClientInfo();
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return wrapped.getClientInfo(name);
    }

    @Override
    public int getHoldability() throws SQLException {
        return wrapped.getHoldability();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return wrapped.getMetaData();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return wrapped.getTransactionIsolation();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return wrapped.getTypeMap();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return wrapped.getWarnings();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return wrapped.isClosed();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return wrapped.isReadOnly();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return wrapped.isValid(timeout);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return wrapped.nativeSQL(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                    int resultSetConcurrency) throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
                    throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
                    throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
                    throws SQLException {
        lastStack = new Exception();
        return wrapped.prepareStatement(sql, columnNames);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency) throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
        lastStack = new Exception();
        logger.log(Level.INFO, "LAST USAGE: " + sql, lastStack);
        return wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        wrapped.releaseSavepoint(savepoint);
    }

    @Override
    public void rollback() throws SQLException {
        wrapped.rollback();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        wrapped.rollback(savepoint);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        wrapped.setAutoCommit(autoCommit);
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        wrapped.setCatalog(catalog);
    }

    @Override
    public void setClientInfo(Properties properties)
                    throws SQLClientInfoException {
        wrapped.setClientInfo(properties);
    }

    @Override
    public void setClientInfo(String name, String value)
                    throws SQLClientInfoException {
        wrapped.setClientInfo(name, value);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        wrapped.setHoldability(holdability);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        wrapped.setReadOnly(readOnly);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return wrapped.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return wrapped.setSavepoint(name);
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        wrapped.setTransactionIsolation(level);
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        wrapped.setTypeMap(map);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (iface.isAssignableFrom(wrapped.getClass())) {
            return true;
        } else {
            return wrapped.isWrapperFor(iface);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(wrapped.getClass())) {
            return iface.cast(wrapped);
        } else {
            return wrapped.unwrap(iface);
        }
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        logger.warning("Calling setSchema, which exposes a transaction bug in Liberty+Derby: " + schema);
        wrapped.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return wrapped.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        wrapped.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        wrapped.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return wrapped.getNetworkTimeout();
    }
}