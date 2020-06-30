/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.proxy;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Wraps a Derby embedded datasource so we can debug connection handling issues
 * by returning wrapped datasources (and wrapped connections)
 */
public class FHIRDebugXADataSource implements XADataSource {
    private static final Logger log = Logger.getLogger(FHIRDebugXADataSource.class.getName());

    // Fixed configuration for debug
    private static final String DERBY_EMBEDDED_XA_DATASOURCE = "org.apache.derby.jdbc.EmbeddedXADataSource";
    
    // The configured delegate
    private XADataSource delegate;

    public FHIRDebugXADataSource() {
        // No Operation
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getDelegate().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        getDelegate().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        getDelegate().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return getDelegate().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        try {
            return getDelegate().getParentLogger();
        } catch (Throwable t) {
            throw new SQLFeatureNotSupportedException("Unexpected error encountered.", t);
        }
    }

    @Override
    public XAConnection getXAConnection() throws SQLException {
        // wrap the XAConnection for easier tracing
        return new FHIRProxyXAConnection(getDelegate().getXAConnection());
    }

    @Override
    public XAConnection getXAConnection(String tenantId, String dsId) throws SQLException {
        return new FHIRProxyXAConnection(getDelegate().getXAConnection(tenantId, dsId));
    }

    public XADataSource getDelegate() throws SQLException {
        if (delegate == null) {
            delegate = makeDelegate();
        }
        return delegate;
    }
    
    /**
     * This function will return an XADataSource instance that is configured according to the connection properties
     * associated with the FHIRRequestContext info found on thread-local.
     * Note: this function is declared as public for testing purposes. Normally, this function is only called internally.
     *
     * @throws SQLException
     */
    public XADataSource makeDelegate() throws SQLException {
        try {
            // Next, create an instance of the appropriate datasource class.
            Class<?> datasourceClass = null;
            try {
                datasourceClass = Class.forName(DERBY_EMBEDDED_XA_DATASOURCE);
            } catch (Throwable t) {
                throw new IllegalStateException("Error attempting to load XADataSource class '" + DERBY_EMBEDDED_XA_DATASOURCE + "'.", t);
            }
    
            // Instantiate the XADataSource class
            Object dsObj = datasourceClass.newInstance();
            if (!(dsObj instanceof XADataSource)) {
                throw new IllegalStateException("Class '" + datasourceClass.getName() + "' is not a subclass of XADataSource.");
            }
    
            XADataSource datasource = (XADataSource) dsObj;
    
            // Finally, set the properties found in the "connectionProperties" property group on the XADataSource instance.
            // Note: this requires the "connectionProperties" property group to contain ONLY
            // properties that map to valid field names within the vendor-specific XADataSource impl class.
            setProperty(datasource, "databaseName", "derby/fhirDB");
            setProperty(datasource, "createDatabase", "create");
            return datasource;
        } catch (Throwable t) {
            throw new SQLException("Unexpected error while retrieving XADataSource delegate.", t);
        } finally {
            log.exiting(this.getClass().getName(), "getDelegate");
        }
    }

    /**
     * Set the named property on the datasource
     * @param datasource
     * @param propertyName
     * @param propertyValue
     */
    private static void setProperty(XADataSource datasource, String propertyName, String propertyValue) {
        if (log.isLoggable(Level.FINER)) {
            String value = (propertyName.toLowerCase().contains("password") ? "********" : propertyValue.toString());
            log.finer("Found property '" + propertyName + "' = '" + value + "'.");
        }
        
        try {
            // Standard property
            PropertyUtils.setSimpleProperty(datasource, propertyName, propertyValue);
        } catch (Throwable t) {
            String msg = "Error setting property '" + propertyName + "' on instance of class '" + datasource.getClass().getName() + ".";
            log.log(Level.SEVERE, msg, t);
            throw new IllegalStateException(msg, t);
        }
    }
}
