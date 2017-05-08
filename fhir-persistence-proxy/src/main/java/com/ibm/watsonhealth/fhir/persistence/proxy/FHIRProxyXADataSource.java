/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.proxy;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.apache.commons.beanutils.PropertyUtils;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.config.PropertyGroup.PropertyEntry;

/**
 * This class serves as a proxy for creating XA connections to databases according to a properties-based configuration.
 * 
 * @author padams
 */
public class FHIRProxyXADataSource implements XADataSource {
    private static final Logger log = Logger.getLogger(FHIRProxyXADataSource.class.getName());

    // This Map is a cache of XADataSource instances, keyed first by tenant-id, then by datastore-id.
    private static Map<String, Map<String, XADataSource>> datasourceCache = new HashMap<>();

    // This Map provides a mapping of database type to XADataSource classname for our supported database types.
    private static Map<String, String> datasourceTypeMapping = null;
    static {
        datasourceTypeMapping = new HashMap<>();
        datasourceTypeMapping.put("db2", "com.ibm.db2.jcc.DB2XADataSource");
        datasourceTypeMapping.put("derby", "org.apache.derby.jdbc.EmbeddedXADataSource");
    }

    public FHIRProxyXADataSource() {
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
        try {
            return getDelegate().getParentLogger();
        } catch (Throwable t) {
            throw new SQLFeatureNotSupportedException("Unexpected error encountered.", t);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.XADataSource#getXAConnection()
     */
    @Override
    public XAConnection getXAConnection() throws SQLException {
        log.entering(this.getClass().getName(), "getXAConnection");
        try {
            return getDelegate().getXAConnection();
        } finally {
            log.exiting(this.getClass().getName(), "getXAConnection");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.XADataSource#getXAConnection(java.lang.String, java.lang.String)
     */
    @Override
    public XAConnection getXAConnection(String user, String password) throws SQLException {
        log.entering(this.getClass().getName(), "getXAConnection(String,String)");
        try {
            return getDelegate().getXAConnection(user, password);
        } finally {
            log.exiting(this.getClass().getName(), "getXAConnection");
        }
    }

    /**
     * This function will return an XADataSource instance that is configured according to the connection properties
     * associated with the FHIRRequestContext info found on thread-local. Note: this function is declared as public for
     * testing purposes. Normally, this function is only called internally.
     * 
     * @throws SQLException
     */
    public XADataSource getDelegate() throws SQLException {
        log.entering(this.getClass().getName(), "getDelegate");
        try {

            // Grab the context info (tenant-id, datastore-id) from thread-local.
            FHIRRequestContext context = FHIRRequestContext.get();

            // Next, retrieve from cache or create the tenant's datasource map.
            Map<String, XADataSource> tenantMap = datasourceCache.get(context.getTenantId());
            if (tenantMap == null) {
                synchronized (datasourceCache) {
                    tenantMap = datasourceCache.get(context.getTenantId());
                    if (tenantMap == null) {
                        if (log.isLoggable(Level.FINER)) {
                            log.finer("Tenant datasource cache was not found, creating new cache for tenant-id '" + context.getTenantId() + "'.");
                        }
                        tenantMap = new HashMap<String, XADataSource>();
                        datasourceCache.put(context.getTenantId(), tenantMap);
                    }
                }
            }

            // Next, retrieve the data source based on the datastore-id value,
            // or create a new one if necessary.
            XADataSource datasource = tenantMap.get(context.getDataStoreId());
            if (datasource == null) {
                synchronized (tenantMap) {
                    datasource = tenantMap.get(context.getDataStoreId());
                    if (datasource == null) {
                        if (log.isLoggable(Level.FINER)) {
                            log.finer("Datasource '" + context.getDataStoreId() + "' was not found, creating a new datasource and adding to the cache.");
                        }
                        datasource = createDataSource(context.getDataStoreId());
                        tenantMap.put(context.getDataStoreId(), datasource);
                    }
                }
            }

            return datasource;
        } catch (Throwable t) {
            throw new SQLException("Unexpected error while retrieving XADataSource delegate.", t);
        } finally {
            log.exiting(this.getClass().getName(), "getDelegate");
        }
    }

    /**
     * Creates an appropriate datasource according to the properties associated with the specified datastore-id
     */
    private XADataSource createDataSource(String dsId) throws Exception {
        log.entering(this.getClass().getName(), "createDataSource");
        try {
            XADataSource datasource = null;

            // Retrieve the property group containing all of the datasource definitions for the current tenant.
            PropertyGroup datasourcesPG = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_DATASOURCES);
            if (datasourcesPG == null) {
                throw new IllegalStateException("Could not locate property group '" + FHIRConfiguration.PROPERTY_DATASOURCES);
            }

            // Next, retrieve the property group pertaining to the datastore-id.
            PropertyGroup dsPG = datasourcesPG.getPropertyGroup(dsId);
            if (dsPG == null) {
                throw new IllegalStateException("Could not locate properties for datastore-id '" + dsId + "'.");
            }

            // Get the datasource type (Derby, DB2, etc.).
            String type = dsPG.getStringProperty("type", null);
            if (type == null) {
                throw new IllegalStateException("Could not locate 'type' property within datasource property group '" + dsId + "'.");
            }

            // Get the connection properties
            PropertyGroup connectionProps = dsPG.getPropertyGroup("connectionProperties");
            if (connectionProps == null) {
                throw new IllegalStateException("Could not locate 'connectionProperties' property within datasource property group '" + dsId + "'.");
            }

            // Fold the type to lowercase to "normalize" it.
            String typeLC = type.toLowerCase();

            String datasourceClassname = datasourceTypeMapping.get(typeLC);
            if (datasourceClassname == null) {
                throw new IllegalArgumentException("Datasource type '" + type + "' not supported.");
            }
            
            if (log.isLoggable(Level.FINER)) {
                log.finer("Mapped database type '" + type + "' to XADataSource implementation class '" + datasourceClassname + "'.");
            }

            // Next, create an instance of the appropriate datasource class.
            Class<?> datasourceClass = null;
            try {
                datasourceClass = Class.forName(datasourceClassname);
            } catch (Throwable t) {
                throw new IllegalStateException("Error attempting to load XADataSource class '" + datasourceClassname + "'.", t);
            }

            // Instantiate the appropriate XADataSource class
            Object dsObj = datasourceClass.newInstance();
            if (!(dsObj instanceof XADataSource)) {
                throw new IllegalStateException("Class '" + datasourceClass.getName() + "' is not a subclass of XADataSource.");
            }

            datasource = (XADataSource) dsObj;

            if (log.isLoggable(Level.FINER)) {
                String msg = "Created new instance of datasource for tenant-id '" + FHIRRequestContext.get().getTenantId()
                        + "' and datastore-id '" + dsId + "': " + dsObj.getClass().getSimpleName();
                log.info(msg);
            }

            // Finally, set the properties found in the "connectionProperties" property group
            // on the XADataSource instance.
            // Note: this requires the "connectionProperties" property group to contain ONLY
            // properties that map to valid field names within the vendor-specific XADataSource impl class.
            setConnectionProperties(datasource, connectionProps);

            return datasource;
        } finally {
            log.exiting(this.getClass().getName(), "createDataSource");
        }
    }

    /**
     * This function will use reflection to set each of the properties found in "connectionProps" on the specified
     * datasource instance using the appropriate setter methods.
     * 
     * @param datasource
     *            the datasource instance to set the properties on
     * @param connectionProps
     *            a PropertyGroup containing the properties to be set on the datasource
     */
    private void setConnectionProperties(XADataSource datasource, PropertyGroup connectionProps) throws Exception {
        // Configure the datasource according to the connection properties.
        // We'll do this by visiting each of the individual properties found in the
        // "connectionProperties" property group, and set each one on the datasource.
        if (log.isLoggable(Level.FINER)) {
            log.finer("Setting connection properties on '" + datasource.getClass().getName() + "' instance.");
        }
        for (PropertyEntry property : connectionProps.getProperties()) {
            String propertyName = property.getName();
            Object propertyValue = property.getValue();
            if (log.isLoggable(Level.FINER)) {
                String value = (propertyName.toLowerCase().contains("password") ? "********" : propertyValue.toString());
                log.finer("Found property '" + propertyName + "' = '" + value + "'.");
            }
            try {
                PropertyUtils.setSimpleProperty(datasource, propertyName, propertyValue);
            } catch (Throwable t) {
                String msg = "Error setting property '" + propertyName + "' on instance of class '" + datasource.getClass().getName() + ".";
                log.log(Level.SEVERE, msg, t);
                throw new IllegalStateException(msg, t);
            }
        }
    }
}
