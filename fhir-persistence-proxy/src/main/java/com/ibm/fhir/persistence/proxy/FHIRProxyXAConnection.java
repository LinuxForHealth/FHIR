/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.proxy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;


/**
 * Proxy/wrapper for XAConnection objects obtained via the FHIRProxyXADataSource.
 * Provides additional tracing for connection/transaction/pooling issues
 */
public class FHIRProxyXAConnection implements XAConnection {
    private static final Logger logger = Logger.getLogger(FHIRProxyXAConnection.class.getName());
    private final XAConnection delegate;
    
    public FHIRProxyXAConnection(XAConnection delegate) {
        this.delegate = delegate;
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        // dump the stack showing who's calling
        Exception x = new Exception("stack");
        logger.log(Level.INFO, "PROXY XA CONNECTION", x);
        
        if (logger.isLoggable(Level.FINE)) {
            // Wrap the connection so that we can really see what's going on
            return new FHIRProxyConnection(delegate.getConnection());
        } else {
            return delegate.getConnection();
        }
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#close()
     */
    @Override
    public void close() throws SQLException {
        this.delegate.close();
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        this.delegate.addConnectionEventListener(listener);
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        this.delegate.removeConnectionEventListener(listener);
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#addStatementEventListener(javax.sql.StatementEventListener)
     */
    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        this.delegate.addStatementEventListener(listener);
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#removeStatementEventListener(javax.sql.StatementEventListener)
     */
    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        this.delegate.removeStatementEventListener(listener);
    }

    /* (non-Javadoc)
     * @see javax.sql.XAConnection#getXAResource()
     */
    @Override
    public XAResource getXAResource() throws SQLException {
        return this.delegate.getXAResource();
    }
}
