/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.proxy;

import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.apache.commons.beanutils.PropertyUtils;

import com.ibm.watson.health.fhir.config.FHIRConfigHelper;
import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.config.PropertyGroup;
import com.ibm.watson.health.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.watson.health.fhir.persistence.proxy.rm.RMXAConnectionResource;

/**
 * This class serves as a proxy for creating XA connections to databases according to a properties-based configuration.
 * 
 * @author padams
 */
public class FHIRProxyXADataSource implements XADataSource {
    private static final Logger log = Logger.getLogger(FHIRProxyXADataSource.class.getName());
    private static final String CLASSNAME = FHIRProxyXADataSource.class.getName();

    // This Map is a cache of XADataSource instances, keyed first by tenant-id, then by datastore-id.
    private static final Map<String, Map<String, DataSourceCacheEntry>> datasourceCache = new ConcurrentHashMap<>();

    // This Map provides a mapping of database type to XADataSource classname for our supported database types.
    private static Map<String, String> datasourceTypeMapping = null;
    static {
        datasourceTypeMapping = new HashMap<>();
        datasourceTypeMapping.put("db2", "com.ibm.db2.jcc.DB2XADataSource");
        datasourceTypeMapping.put("derby", "org.apache.derby.jdbc.EmbeddedXADataSource");
        datasourceTypeMapping.put("derby_network_server", "org.apache.derby.jdbc.ClientXADataSource");
        datasourceTypeMapping.put("db2test", "com.ibm.watson.health.fhir.persistence.proxy.rm.test.TestDB2XADataSource");
        datasourceTypeMapping.put("derbytest", "com.ibm.watson.health.fhir.persistence.proxy.rm.test.TestEmbeddedXADataSource");
    }

    /**
     * Internal envelope for caching data source entries
     * @author rarnold
     */
    public static class DataSourceCacheEntry {
        private final XADataSource datasource;
        private final String tenantKey;
        
        public DataSourceCacheEntry(XADataSource ds, String tenantKey) {
            this.datasource = ds;
            this.tenantKey = tenantKey;
        }

        /**
         * Getter for the tenantKey property. Can be null if datasource is not multi-tenant
         * @return
         */
        public String getTenantKey() {
            return this.tenantKey;
        }

        /**
         * Getter for the {@link XADataSource}
         * @return
         */
        public XADataSource getDataSource() {
            return this.datasource;
        }
    }

    public FHIRProxyXADataSource() {
    }
    
    /**
     * This method returns a list of all the cached XADataSource instances held by this proxy datasource.
     */
    public List<XADataSource> getCachedDataSources() {
        log.entering(this.getClass().getName(), "getCachedDataSources");
        try {
            List<XADataSource> result = new ArrayList<>();
            log.fine("Building list of cached DataSources...");
            for (Map.Entry<String, Map<String, DataSourceCacheEntry>> tenantEntry : datasourceCache.entrySet()) {
                log.fine("Tenant id: " + tenantEntry.getKey());
                for (Map.Entry<String, DataSourceCacheEntry> dsEntry : tenantEntry.getValue().entrySet()) {
                    
                    log.fine("   XADataSource for dsId: " + dsEntry.getKey());
                    result.add(dsEntry.getValue().getDataSource());
                }
            }
            
            log.fine("Returning XADataSource list of size: " + result.size());

            return result;
        } finally {
            log.exiting(this.getClass().getName(), "getCachedDataSources");
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
        log.entering(this.getClass().getName(), "getXAConnection()");
        try {
            XAConnection connection = null;
            
            // XA recovery will be triggered by a call to this method, while
            // threadlocal contains "default/default" for the tenant-id and datastore-id.
            // If we find something else on thread-local then we'll treat it as an error.
            String tenantId = FHIRRequestContext.get().getTenantId();
            String dsId = FHIRRequestContext.get().getDataStoreId();
            if (FHIRConfiguration.DEFAULT_TENANT_ID.equals(tenantId) && FHIRConfiguration.DEFAULT_DATASTORE_ID.equals(dsId)) {
                log.info("Initiating XA recovery process...");
                // cacheAllConfiguredDataSources();
                connection = new RMXAConnectionResource(this);
            } else {
                throw new SQLException("The getXAConnection() method should be called only during XA recovery.");
            }
            return connection;
        } finally {
            log.exiting(this.getClass().getName(), "getXAConnection()");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.XADataSource#getXAConnection(java.lang.String, java.lang.String)
     */
    @Override
    public XAConnection getXAConnection(String tenantId, String dsId) throws SQLException {
        log.entering(this.getClass().getName(), "getXAConnection(String,String)", new Object[]{tenantId, dsId});
        try {
            // Make sure that the values stored on thread-local match the parameters passed in.
            if (!FHIRRequestContext.get().getTenantId().equals(tenantId)) {
                throw new SQLException("tenantId parameter value (" + tenantId + ") does not match thread-local value (" 
                        + FHIRRequestContext.get().getTenantId() + ").");
            }
            if (!FHIRRequestContext.get().getDataStoreId().equals(dsId)) {
                throw new SQLException("dsId parameter value (" + dsId + ") does not match thread-local value (" 
                        + FHIRRequestContext.get().getDataStoreId() + ").");
            }
            
            XAConnection connection = getDelegate().getXAConnection();
            
            return connection;
        } finally {
            log.exiting(this.getClass().getName(), "getXAConnection(String,String)");
        }
    }
    
    /**
     * This function will return an XADataSource instance that is configured according to the connection properties
     * associated with the FHIRRequestContext info found on thread-local. 
     * Note: this function is declared as public for testing purposes. Normally, this function is only called internally.
     * 
     * @throws SQLException
     */
    public XADataSource getDelegate() throws SQLException {
        log.entering(this.getClass().getName(), "getDelegate");
        try {
            String tenantId = FHIRRequestContext.get().getTenantId();
            String dsId = FHIRRequestContext.get().getDataStoreId();
            
            // Retrieve from cache or create the tenant's datasource map.
            Map<String, DataSourceCacheEntry> tenantMap = datasourceCache.get(tenantId);
            if (tenantMap == null) {
                synchronized (datasourceCache) {
                    tenantMap = datasourceCache.get(tenantId);
                    if (tenantMap == null) {
                        if (log.isLoggable(Level.FINER)) {
                            log.finer("Tenant datasource cache was not found, creating new cache for tenant-id " + tenantId + ".");
                        }
                        tenantMap = new ConcurrentHashMap<String, DataSourceCacheEntry>();
                        datasourceCache.put(tenantId, tenantMap);
                    }
                }
            }

            // Next, retrieve the data source based on the datastore-id value,
            // or create a new one if necessary.
            DataSourceCacheEntry dsCacheEntry = tenantMap.get(dsId);
            if (dsCacheEntry == null) {
                synchronized (tenantMap) {
                    dsCacheEntry = tenantMap.get(dsId);
                    if (dsCacheEntry == null) {
                        if (log.isLoggable(Level.FINER)) {
                            log.finer("Datasource " + dsId + " was not found in cache, creating a new datasource and adding to the cache.");
                        }
                        dsCacheEntry = createDataSourceCacheEntry(dsId);
                        tenantMap.put(dsId, dsCacheEntry);
                    }
                }
            }

            String tenantKey = dsCacheEntry.getTenantKey();
            if (tenantKey != null) {
                // write the tenantKey for the chosen datasource into the request context
                FHIRRequestContext.get().setTenantKey(tenantKey);
            }
            
            return dsCacheEntry.getDataSource();
        } catch (Throwable t) {
            throw new SQLException("Unexpected error while retrieving XADataSource delegate.", t);
        } finally {
            log.exiting(this.getClass().getName(), "getDelegate");
        }
    }
    
    /**
     * Returns the XADataSource implementation classname associated with the specified type,
     * or null if the type is not recognized.
     * @param type the datasource type (e.g. "db2", "derby", etc.)
     */
    public static String getDataSourceImplClassnameForType(String type) {
        String implClassname = datasourceTypeMapping.get(type.toLowerCase());
        return implClassname;
    }

    /**
     * Creates an appropriate datasource and its cache entry according to the 
     * properties associated with the specified datastore-id
     */
    public static DataSourceCacheEntry createDataSourceCacheEntry(String dsId) throws Exception {
        log.entering(CLASSNAME, "createDataSource", new Object[]{"dsId", dsId});
        try {
            // Retrieve the property group pertaining to the specified datastore.
            String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + dsId;
            PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
            if (dsPG == null) {
                throw new IllegalStateException("Could not locate configuration property group: " + dsPropertyName);
            }

            // Get the datasource type (Derby, DB2, etc.).
            String type = dsPG.getStringProperty("type", null);
            if (type == null) {
                throw new IllegalStateException("Could not locate 'type' property within datasource property group: " + dsPropertyName);
            }

            // The tenant key required when using a multi-tenant schema. Null for single-tenant schemas
            String tenantKey = dsPG.getStringProperty("tenantKey", null);

            // Get the connection properties
            PropertyGroup connectionProps = dsPG.getPropertyGroup("connectionProperties");
            if (connectionProps == null) {
                throw new IllegalStateException("Could not locate 'connectionProperties' property group within datasource property group: " + dsPropertyName);
            }

            // Map the type to an XADataSource impl classname.
            String datasourceClassname = getDataSourceImplClassnameForType(type);
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

            XADataSource datasource = (XADataSource) dsObj;

            if (log.isLoggable(Level.FINER)) {
                String msg = "Created new instance of datasource for tenant-id '" + FHIRRequestContext.get().getTenantId() + "' and datastore-id '" + dsId
                        + "': " + dsObj.getClass().getSimpleName();
                log.info(msg);
            }

            // Finally, set the properties found in the "connectionProperties" property group
            // on the XADataSource instance.
            // Note: this requires the "connectionProperties" property group to contain ONLY
            // properties that map to valid field names within the vendor-specific XADataSource impl class.
            setConnectionProperties(datasource, connectionProps);

            return new DataSourceCacheEntry(datasource, tenantKey);
        } finally {
            log.exiting(CLASSNAME, "createDataSource");
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
    private static void setConnectionProperties(XADataSource datasource, PropertyGroup connectionProps) throws Exception {
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
                
                if ("securityMechanism".equals(propertyName)) {
                    PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(datasource, propertyName);
                    if (pd != null) {
                        // OK, let's try this way
                        short securityMechanismValue = (short)(int)propertyValue;
                        log.info("Setting security mechanism: " + securityMechanismValue);
                        Method setSecurityMechanism = PropertyUtils.getWriteMethod(pd);
                        setSecurityMechanism.invoke(datasource, securityMechanismValue);
                    }
                    else {
                        throw new IllegalStateException(propertyName + " not supported on datasource class: " + datasource.getClass().getName());
                    }
                }
                else {
                    // Standard property
                    PropertyUtils.setSimpleProperty(datasource, propertyName, propertyValue);
                }
            } catch (Throwable t) {
                String msg = "Error setting property '" + propertyName + "' on instance of class '" + datasource.getClass().getName() + ".";
                log.log(Level.SEVERE, msg, t);
                throw new IllegalStateException(msg, t);
            }
        }
    }
}
