/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.proxy;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;

/**
 * This class serves as a proxy for creating XA connections to databases according to a properties-based configuration.
 */
public class FHIRProxyXADataSource implements XADataSource {
    private static final Logger log = Logger.getLogger(FHIRProxyXADataSource.class.getName());

    public FHIRProxyXADataSource() {
        log.entering(this.getClass().getName(), "FHIRProxyXADataSource");
        log.exiting(this.getClass().getName(), "FHIRProxyXADataSource");
    }

    /**
     * This function will return an XADataSource instance that is configured according to the 
     * connection properties associated with the FHIRRequestContext info found on thread-local.
     */
    private XADataSource getDelegate() {
        log.entering(this.getClass().getName(), "getDelegate");
        try {
            FHIRRequestContext context = FHIRRequestContext.get();
            log.finer("FHIRRequestContext contains tenant-id '" + context.getTenantId() 
                + "', datastore-id '" + context.getDataStoreId() + "'.");

            // Stub for now...
            // EmbeddedXADataSource derbyDS = new EmbeddedXADataSource();
            
            
            
            return null;
        } finally {
            log.exiting(this.getClass().getName(), "getDelegate");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.CommonDataSource#getLogWriter()
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getDelegate().getLogWriter();
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
     */
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        getDelegate().setLogWriter(out);
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.CommonDataSource#setLoginTimeout(int)
     */
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        getDelegate().setLoginTimeout(seconds);
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.CommonDataSource#getLoginTimeout()
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return getDelegate().getLoginTimeout();
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.CommonDataSource#getParentLogger()
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getDelegate().getParentLogger();
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.XADataSource#getXAConnection()
     */
    @Override
    public XAConnection getXAConnection() throws SQLException {
        return getDelegate().getXAConnection();
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.XADataSource#getXAConnection(java.lang.String, java.lang.String)
     */
    @Override
    public XAConnection getXAConnection(String user, String password) throws SQLException {
        return getDelegate().getXAConnection(user, password);
    }
}
