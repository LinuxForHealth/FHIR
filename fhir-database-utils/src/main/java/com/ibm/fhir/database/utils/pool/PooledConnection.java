/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.pool;

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
import java.util.logging.Logger;

/**
 * Wrapping of a real database connection so that we can intercept the close call
 * and therefore reuse the connection. Note that we also listen for any exceptions
 * generated, and assume (for better or for worse) that something might be broken
 * so call back to the connection provider so that it can decide whether or not
 * to abandon the underlying connection.
 */
public class PooledConnection implements Connection {
    private static final Logger logger = Logger.getLogger(PooledConnection.class.getName());

    // Pointer back to the object which spawned us
    private final PoolConnectionProvider pool;
    
    // The actual connection we're wrapping (decorating)
    private final Connection wrapped;
    
    // Should we assume any exceptions are fatal and mark the connection for no reuse
    private final boolean closeOnAnyError;
    
    // We assume the connection is reusable until we hit a connection error
    private boolean reusable = true;

    // Track the open/close pairing
    private int openCount = 0;
    

    /**
     * Public constructor
     * @param cp
     * @param wrappee
     * @param closeOnAnyError
     */
    public PooledConnection(PoolConnectionProvider cp, Connection wrappee, boolean closeOnAnyError) {
        this.pool = cp;
        this.wrapped = wrappee;
        this.closeOnAnyError = closeOnAnyError;
    }

    /**
     * Get the wrapped connection
     * @return
     */
    public Connection getWrapped() {
        return this.wrapped;
    }

    /**
     * Getter for the broken connection flag
     * @return
     */
    public boolean isReusable() {
        return this.reusable;
    }

    /**
     * Increment the open count and return the new value
     * @return
     */
    public int incOpenCount() {
        return ++this.openCount;
    }

    /**
     * Get the current open count
     * @return
     */
    public int getOpenCount() {
        return this.openCount;
    }

    /**
     * Force the underlying connection to close. We don't care about any
     * exceptions, because there's nothing we can do about them
     */
    public void forceClosed() {
        try {
            this.wrapped.close();
        }
        catch (Exception x) {
            // nop
        }
        finally {
            // obviously we can't reuse this connection now
            this.reusable = false;
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        wrapped.clearWarnings();
    }

    @Override
    public void close() throws SQLException {
        // return the connection, telling the pool if we think the
        // underlying connection can be reused
        this.openCount--;
        pool.returnConnection(this, this.reusable);
    }

    @Override
    public void commit() throws SQLException {
        throw new IllegalStateException("Use transaction commit, not connection commit");
    }
    
    /**
     * Update the reusability status of this connection following an exception
     * @param x
     */
    private void updateReusable(SQLException x) {
        this.reusable = !this.closeOnAnyError && !pool.checkConnectionFailure(x);
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
                    throws SQLException {
        
        try {
            return wrapped.createArrayOf(typeName, elements);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Blob createBlob() throws SQLException {
        try {
            return wrapped.createBlob();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Clob createClob() throws SQLException {
        try {
            return wrapped.createClob();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public NClob createNClob() throws SQLException {
        try {
            return wrapped.createNClob();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        try {
            return wrapped.createSQLXML();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        try {
            return wrapped.createStatement();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
                    throws SQLException {
        try {
            return wrapped.createStatement(resultSetType, resultSetConcurrency);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Statement createStatement(int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
        try {
            return wrapped.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
                    throws SQLException {
        try {
            return wrapped.createStruct(typeName, attributes);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        try {
            return wrapped.getAutoCommit();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        try {
            return wrapped.getCatalog();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        try {
            return wrapped.getClientInfo();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        try {
            return wrapped.getClientInfo(name);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        try {
            return wrapped.getHoldability();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return wrapped.getMetaData();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        try {
            return wrapped.getTransactionIsolation();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        try {
            return wrapped.getTypeMap();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return wrapped.getWarnings();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return wrapped.isClosed();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        try {
            return wrapped.isReadOnly();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        try {
            return wrapped.isValid(timeout);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        try {
            return wrapped.nativeSQL(sql);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        try {
            return wrapped.prepareCall(sql);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                    int resultSetConcurrency) throws SQLException {
        try {
            return wrapped.prepareCall(sql, resultSetType, resultSetConcurrency);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
        try {
            return wrapped.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        try {
            return wrapped.prepareStatement(sql);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
                    throws SQLException {
        try {
            return wrapped.prepareStatement(sql, autoGeneratedKeys);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
                    throws SQLException {
        try {
            return wrapped.prepareStatement(sql, columnIndexes);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
                    throws SQLException {
        try {
            return wrapped.prepareStatement(sql, columnNames);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency) throws SQLException {
        try {
            return wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
        try {
            return wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try {
            wrapped.releaseSavepoint(savepoint);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void rollback() throws SQLException {
        throw new IllegalStateException("Use transaction rollback, not connection rollback");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        try {
            wrapped.rollback(savepoint);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        try {
            wrapped.setAutoCommit(autoCommit);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        try {
            wrapped.setCatalog(catalog);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setClientInfo(Properties properties)
                    throws SQLClientInfoException {
        try {
            wrapped.setClientInfo(properties);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setClientInfo(String name, String value)
                    throws SQLClientInfoException {
        try {
            wrapped.setClientInfo(name, value);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        try {
            wrapped.setHoldability(holdability);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        try {
            wrapped.setReadOnly(readOnly);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        try {
            return wrapped.setSavepoint();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        try {
            return wrapped.setSavepoint(name);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        try {
            wrapped.setTransactionIsolation(level);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        try {
            wrapped.setTypeMap(map);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            if (iface.isAssignableFrom(wrapped.getClass()))
                    return true;
            else
                    return wrapped.isWrapperFor(iface);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            if (iface.isAssignableFrom(wrapped.getClass()))
                    return iface.cast(wrapped);
            else
                    return wrapped.unwrap(iface);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    /**
     * Reduced visibility so that only the connection pool gets to
     * close the underlying connection.
     * @throws SQLException
     */
    protected void closeWrapped() throws SQLException {
        try {
            wrapped.close();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        try {
            logger.warning("Calling setSchema, which exposes a transaction bug in Liberty+Derby: " + schema);
            wrapped.setSchema(schema);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public String getSchema() throws SQLException {
        try {
            return wrapped.getSchema();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        try {
            wrapped.abort(executor);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        try {
            wrapped.setNetworkTimeout(executor, milliseconds);
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        try {
            return wrapped.getNetworkTimeout();
        }
        catch (SQLException x) {
            updateReusable(x);
            throw x;
        }
    }
}
