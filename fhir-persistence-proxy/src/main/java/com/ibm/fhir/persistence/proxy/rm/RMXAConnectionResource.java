/*
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.proxy.rm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.proxy.FHIRProxyXADataSource;
import com.ibm.fhir.persistence.proxy.FHIRProxyXADataSource.DataSourceCacheEntry;

/**
 * This class is used during XA recovery operations to represent an XAConnection and its associated XAResource, but in
 * reality it serves as a proxy for ALL of the XAConnections and their associated XAResources related to XADataSources
 * cached by the proxy datasource.
 * 
 * This class is only used during the XA Resource recovery operations triggered by the Liberty Recovery Manager.
 * 
 * @author padams
 */
public class RMXAConnectionResource implements XAConnection, XAResource {
    private static final Logger log = Logger.getLogger(RMXAConnectionResource.class.getName());

    private Map<String, DataSourceCacheEntry> proxiedXADataSources;
    private List<XAConnection> proxiedXAConnections;
    private List<XAResource> proxiedXAResources;
    private Map<XidKey, List<XAResource>> proxiedXids;

    // This threadlocal is used to bypass artificial failures that might be requested during testing.
    private static ThreadLocal<Boolean> bypassFailures = new ThreadLocal<Boolean>() {
        @Override
        public Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    /**
     * This ctor is invoked by the FHIRProxyXADataSource class when the Liberty Recovery Manager has triggered XA
     * recovery operations.
     * 
     * @param dataSource
     *            the parent FHIRProxyXADataSource instance
     * @throws SQLException
     */
    public RMXAConnectionResource(FHIRProxyXADataSource parentDS) throws SQLException {
        log.entering(this.getClass().getName(), "RMXAConnectionResource ctor");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            buildProxiedXADataSources();
            // setProxiedXADataSources(parentDS.getCachedDataSources());
            buildProxiedXAConnections();
        } finally {
            log.exiting(this.getClass().getName(), "RMXAConnectionResource ctor");
        }
    }

    public static boolean shouldBypassFailures() {
        return bypassFailures.get();
    }

    public static void setBypassFailures(Boolean flag) {
        bypassFailures.set(flag);
    }

    //
    // Getter/setter methods
    //

    public Map<String, DataSourceCacheEntry> getProxiedXADataSources() {
        return proxiedXADataSources;
    }

    public void setProxiedXADataSources(Map<String, DataSourceCacheEntry> proxiedXADataSources) {
        this.proxiedXADataSources = proxiedXADataSources;
    }

    public List<XAConnection> getProxiedXAConnections() {
        return proxiedXAConnections;
    }

    public void setProxiedXAConnections(List<XAConnection> proxiedXAConnections) {
        this.proxiedXAConnections = proxiedXAConnections;
    }

    public List<XAResource> getProxiedXAResources() {
        return proxiedXAResources;
    }

    public void setProxiedXAResources(List<XAResource> proxiedXAResources) {
        this.proxiedXAResources = proxiedXAResources;
    }

    public Map<XidKey, List<XAResource>> getProxiedXids() {
        return proxiedXids;
    }

    public void setProxiedXids(Map<XidKey, List<XAResource>> proxiedXids) {
        this.proxiedXids = proxiedXids;
    }

    //
    // XAConnection methods
    //

    /*
     * (non-Javadoc)
     * @see javax.sql.XAConnection#getXAResource()
     */
    @Override
    public XAResource getXAResource() throws SQLException {
        log.entering(this.getClass().getName(), "getXAResource");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            buildProxiedXAResources();
            return this;
        } finally {
            log.exiting(this.getClass().getName(), "getXAResource");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        //
        // Note: there's really no good answer for how to implement this method in
        // the context of the RMXAConnectionResource class. This class's job is to
        // act as a proxy for all the XAConnections associated with the proxy datasource's
        // cached XADataSource instances. Most of the methods in this class will simply
        // delegate calls to each of the proxied XAConnections.
        // However, this method must return a Connection associated with our proxy XAConnection,
        // and it wouldn't make sense to try to delete each of those method calls to EACH of the
        // proxied XAConnections. Our assumption is that the Liberty Recovery Manager will use this
        // Connection to gather metadata about the datasource (database) and will not actually try to
        // execute any SQL statements, etc. So, we'll grab the first proxied XAConnection and then
        // call it's getConnection() method, then wrap that with our TestConnection wrapper class
        // and hope for the best :)
        //
        log.entering(this.getClass().getName(), "getConnection");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            Connection conn = null;
            if (getProxiedXAConnections() != null && getProxiedXAConnections().size() > 0) {
                conn = getProxiedXAConnections().get(0).getConnection();
            }
            return conn;
        } finally {
            log.exiting(this.getClass().getName(), "getConnection");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#close()
     */
    @Override
    public void close() throws SQLException {
        log.entering(this.getClass().getName(), "close");
        try {
            if (getProxiedXAConnections() != null) {
                // Call close on each proxied XAConnection.
                for (XAConnection connection : getProxiedXAConnections()) {
                    connection.close();
                }

                // Clear out all our state data now.
                setProxiedXids(null);
                setProxiedXAResources(null);
                setProxiedXAConnections(null);
            }
        } finally {
            log.exiting(this.getClass().getName(), "close");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        log.entering(this.getClass().getName(), "addConnectionEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.addConnectionEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "addConnectionEventListener");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        log.entering(this.getClass().getName(), "removeConnectionEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.removeConnectionEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "removeConnectionEventListener");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#addStatementEventListener(javax.sql.StatementEventListener)
     */
    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        log.entering(this.getClass().getName(), "addStatementEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.addStatementEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "addStatementEventListener");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#removeStatementEventListener(javax.sql.StatementEventListener)
     */
    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        log.entering(this.getClass().getName(), "removeStatementEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.removeStatementEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "removeStatementEventListener");
        }
    }

    //
    // XAResource methods
    //

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#recover(int)
     */
    @Override
    public Xid[] recover(int flag) throws XAException {
        log.entering(this.getClass().getName(), "recover(int)", new Object[] {
                "flag", flag
        });
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            log.info("Retrieving transaction id information from cached datsources.");
            
            // We'll need to visit each proxied XAResource and call its recover() method,
            // then store each of the returned Xid's in a map keyed by Xid and holding the
            // list of XAResources associated with that Xid.
            buildProxiedXids(flag);

            // Return an array containing all the Xid keys from the map.
            Xid[] result = buildXidArrayFromMap();

            log.info("Finished retrieving transaction id information from cached datsources. Size=" 
                    + (result != null ? result.length : "<null>") );

            return result;
        } finally {
            log.exiting(this.getClass().getName(), "recover(int)");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid, boolean)
     */
    @Override
    public void commit(Xid xid, boolean onePhase) throws XAException {
        log.entering(this.getClass().getName(), "commit", new Object[] {
                "onePhase", Boolean.toString(onePhase), "Xid", displayXid(xid)
        });
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("commit: Unknown Xid");
            }

            String xidString = displayXid(xid);
            log.info("Initiating recovery 'commit' processing for Xid:\n" + xidString);
            
            // Make sure our artificial failures are not triggered during recovery processing :)
            setBypassFailures(Boolean.TRUE);

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.commit(xid, onePhase);
            }
            
            log.info("Finished recovery 'commit' processing for Xid:\n" + xidString);
        } finally {
            setBypassFailures(Boolean.FALSE);
            log.exiting(this.getClass().getName(), "commit");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
     */
    @Override
    public void end(Xid xid, int flag) throws XAException {
        log.entering(this.getClass().getName(), "end");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("end: Unknown Xid");
            }

            String xidString = displayXid(xid);
            log.info("Initiating recovery 'end' processing for Xid:\n" + xidString);

            // Make sure our artificial failures are not triggered during recovery processing :)
            setBypassFailures(Boolean.TRUE);

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.end(xid, flag);
            }
            
            log.info("Finished recovery 'end' processing for Xid:\n" + xidString);
        } finally {
            setBypassFailures(Boolean.FALSE);
            log.exiting(this.getClass().getName(), "end");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
     */
    @Override
    public void forget(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), "forget");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("forget: Unknown Xid");
            }

            String xidString = displayXid(xid);
            log.info("Initiating recovery 'forget' processing for Xid:\n" + xidString);
            
            // Make sure our artificial failures are not triggered during recovery processing :)
            setBypassFailures(Boolean.TRUE);

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.forget(xid);
            }
            
            log.info("Finished recovery 'forget' processing for Xid:\n" + xidString);
        } finally {
            setBypassFailures(Boolean.FALSE);
            log.exiting(this.getClass().getName(), "forget");
        }

    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#getTransactionTimeout()
     */
    @Override
    public int getTransactionTimeout() throws XAException {
        log.entering(this.getClass().getName(), "getTransactionTimeout");
        int timeoutSecs = 0;
        try {
            // We'll return the min(timeout) across all our proxied XAResource instances.
            if (getProxiedXAResources() != null) {
                timeoutSecs = Integer.MAX_VALUE;
                for (XAResource res : getProxiedXAResources()) {
                    int resourceTimeout = res.getTransactionTimeout();
                    if (resourceTimeout < timeoutSecs) {
                        timeoutSecs = resourceTimeout;
                    }
                }
            }
            return timeoutSecs;
        } finally {
            log.exiting(this.getClass().getName(), "getTransactionTimeout", new Object[] {
                    "timeout", timeoutSecs
            });
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
     */
    @Override
    public boolean isSameRM(XAResource otherResource) throws XAException {
        log.entering(this.getClass().getName(), "isSameRM");
        boolean isSame = false;
        try {
            // Drive the method calls to each of the proxied XAResource instances,
            // and return the logically ANDed value received from those method calls.
            if (getProxiedXAResources() != null) {
                isSame = true;
                for (XAResource resource : getProxiedXAResources()) {
                    isSame = isSame && resource.isSameRM(otherResource);
                }
            }
            return isSame;
        } finally {
            log.exiting(this.getClass().getName(), "isSameRM", new Object[] {
                    "isSame", Boolean.toString(isSame)
            });
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
     */
    @Override
    public int prepare(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), "prepare");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        int vote = XAResource.XA_OK;
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("prepare: Unknown Xid");
            }

            String xidString = displayXid(xid);
            log.info("Initiating recovery 'prepare' processing for Xid:\n" + xidString);
            
            // Make sure our artificial failures are not triggered during recovery processing :)
            setBypassFailures(Boolean.TRUE);

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                int resourceVote = resource.prepare(xid);
                if (resourceVote > vote) {
                    vote = resourceVote;
                }
            }
            
            log.info("Finished recovery 'prepare' processing for Xid:\n" + xidString);
            return vote;
        } finally {
            setBypassFailures(Boolean.FALSE);
            log.exiting(this.getClass().getName(), "prepare", new Object[] {
                    "vote", vote
            });
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
     */
    @Override
    public void rollback(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), "rollback");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("rollback: Unknown Xid");
            }

            String xidString = displayXid(xid);
            log.info("Initiating recovery 'rollback' processing for Xid:\n" + xidString);
            
            // Make sure our artificial failures are not triggered during recovery processing :)
            setBypassFailures(Boolean.TRUE);

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.rollback(xid);
            }
            
            log.info("Finished recovery 'rollback' processing for Xid:\n" + xidString);
        } finally {
            setBypassFailures(Boolean.FALSE);
            log.exiting(this.getClass().getName(), "rollback");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
     */
    @Override
    public boolean setTransactionTimeout(int timeoutSecs) throws XAException {
        log.entering(this.getClass().getName(), "setTransactionTimeout");
        boolean result = false;
        try {
            // Drive the method calls to each of the proxied XAResource instances.
            if (getProxiedXAResources() != null) {
                result = true;
                for (XAResource res : getProxiedXAResources()) {
                    result = result && res.setTransactionTimeout(timeoutSecs);
                }
            }
            return result;
        } finally {
            log.exiting(this.getClass().getName(), "setTransactionTimeout");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
     */
    @Override
    public void start(Xid xid, int flag) throws XAException {
        log.entering(this.getClass().getName(), "start");
        // log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("start: Unknown Xid");
            }

            String xidString = displayXid(xid);
            log.info("Initiating recovery 'start' processing for Xid:\n" + xidString);
            
            // Make sure our artificial failures are not triggered during recovery processing :)
            setBypassFailures(Boolean.TRUE);

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.start(xid, flag);
            }
            
            log.info("Finished recovery 'start' processing for Xid:\n" + xidString);
        } finally {
            setBypassFailures(Boolean.FALSE);
            log.exiting(this.getClass().getName(), "start");
        }
    }

    //
    // Private methods
    //

    /**
     * Performs a "toString"-like operation on the specified xid, suitable for including in
     * informational and trace messages.
     * @param xid the transaction id value to be displayed.
     */
    private String displayXid(Xid xid) {
        StringBuilder sb = new StringBuilder();
        sb.append("Xid[");
        sb.append("formatId=").append(xid.getFormatId());
        sb.append(",globalTxnId=").append(bytesToHex(xid.getGlobalTransactionId()));
        sb.append(",branchQualifier=").append(bytesToHex(xid.getBranchQualifier()));
        sb.append("]");
        return sb.toString();
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Converts the specified byte array into a string by converting each
     * byte into a 2-digit hex string.
     * @param bytes the byte array to be converted
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Retrieves the list of XAResources associated with the specified transaction id (xid)
     * 
     * @param xid
     *            the transaction identifier
     * @return a list of XAResources associated with the transaction, or null if mapping is found
     */
    private List<XAResource> getXAResourcesForXid(Xid xid) throws XAException {
        List<XAResource> result = null;
        if (getProxiedXids() == null || getProxiedXids().isEmpty()) {
            buildProxiedXids(XAResource.TMSTARTRSCAN | XAResource.TMENDRSCAN);
        }
        if (getProxiedXids() != null) {
            result = getProxiedXids().get(new XidKey(xid));
        }
        return result;
    }

    /**
     * This method will look at the current configuration (e.g. set of tenants and their datasources)
     * and instantiate each unique datasource so that we can proceed with transaction recovery operations.
     * In this context, uniqueness is determined by the serverName, databaseName and currentSchema properties
     * associated with the datasource
     */
    private void buildProxiedXADataSources() {
        log.entering(this.getClass().getName(), "buildProxiedXADataSources");
        
        // Save off the current request context.
        FHIRRequestContext context = FHIRRequestContext.get();
        
        Map<String, FHIRProxyXADataSource.DataSourceCacheEntry> dsMap = new HashMap<>();
        
        try {
            List<String> tenantIds = FHIRConfiguration.getInstance().getConfiguredTenants();
            for (String tenantId : tenantIds) {
                log.fine("Looking for datasources belonging to tenant id: " + tenantId);
                
                // Set the tenantId on thread local and then retrieve the datasources property group for it.
                try {
                    FHIRRequestContext.set(new FHIRRequestContext(tenantId));
                } catch (FHIRException e) {
                    log.log(Level.WARNING, "Error initializing the FHIRRequestContext for tenantId '" + tenantId + "'", e);
                    continue;
                }
                
                // Retrieve and process each datasource entry found under "fhirServer/persistence/datasources".
                PropertyGroup datasourcesPG = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_DATASOURCES);
                if (datasourcesPG != null) {
                    try {
                        for (PropertyGroup.PropertyEntry propEntry : datasourcesPG.getProperties()) {
                            String datasourceId = propEntry.getName();
                            log.fine("Found datasource entry: " + datasourceId);
                            
                            // Skip if the datasource name starts with a _
                            if (datasourceId.startsWith("_")) {
                                log.fine("Skipping because name starts with '_'...");
                                continue;
                            }
                            
                            PropertyGroup dsPG = datasourcesPG.getPropertyGroup(datasourceId);
                            
                            // Retrieve the "type" property.
                            String type = dsPG.getStringProperty("type");
                            String tenantKey = dsPG.getStringProperty("tenantKey", null);
                            
                            // Skip this entry if the type is not recognized.
                            String datasourceClassname = FHIRProxyXADataSource.getDataSourceImplClassnameForType(type);
                            if (datasourceClassname == null) {
                                log.fine("Skipping because type '" + type + "' is not recognized...");
                                continue;
                            }
                            
                            // Retrieve the "connectionProps" property group so we can retrieve the
                            // serverName and currentSchema properties.
                            PropertyGroup connProps = dsPG.getPropertyGroup("connectionProperties");
                            if (connProps == null) {
                                log.fine("Skipping because no 'connectionProperties' property was found...");
                                continue;
                            }
                            
                            // Next, retrieve some connection properties to complete our key value.
                            String serverName = null;
                            String databaseName = null;
                            String currentSchema = null;
                            try {
                                serverName = connProps.getStringProperty("serverName", "null");
                                databaseName = connProps.getStringProperty("databaseName", "null");
                                currentSchema = connProps.getStringProperty("currentSchema", "null");
                            } catch (Throwable t) {
                                log.fine("Skipping because the key connection properties couldn't be retrieved.");
                                continue;
                            }
                            
                            // Compute the key and check to see if this datasource is already in our map.
                            String datasourceKey = getDatasourceKey(type, serverName, databaseName, currentSchema);
                            log.fine("Datasource key: " + datasourceKey);
                            DataSourceCacheEntry dsCacheEntry = dsMap.get(datasourceKey);
                            
                            // If not found in the map, let's create this datasource and add to the map.
                            if (dsCacheEntry == null) {
                                try {
                                    dsCacheEntry = FHIRProxyXADataSource.createDataSourceCacheEntry(datasourceId);
                                    dsMap.put(datasourceKey, dsCacheEntry);
                                } catch (Throwable t) {
                                    // Ignore any exceptions here.
                                }
                            }
                        }
                    } catch (Throwable t) {
                        // Ignore any exceptions here.
                    }
                }
            }
            
            log.fine("Setting map of proxied datasources... size=" + dsMap.size());
            setProxiedXADataSources(dsMap);
        } finally {
            // Restore the old context.
            FHIRRequestContext.set(context);

            log.exiting(this.getClass().getName(), "buildProxiedXADataSources");
        }
    }

    /**
     * Produces a key string composed of the input parameters.
     */
    private String getDatasourceKey(String type, String serverName, String databaseName, String currentSchema) {
        StringBuilder sb = new StringBuilder();
        sb.append("dskey::type=").append(type).append("::");
        sb.append("serverName=").append(serverName).append("::");
        sb.append("databaseName=").append(databaseName).append("::");
        sb.append("currentSchema=").append(currentSchema);
        return sb.toString();
    }

    /**
     * This method will walk the list of proxied XADataSources and obtain a connection for each one.
     */
    private void buildProxiedXAConnections() throws SQLException {
        log.entering(this.getClass().getName(), "buildProxiedXAConnections");
        try {
            List<XAConnection> connections = new ArrayList<>();
            if (getProxiedXADataSources() != null) {
                for (Map.Entry<String, DataSourceCacheEntry> entry : getProxiedXADataSources().entrySet()) {
                    DataSourceCacheEntry ds = entry.getValue();
                    log.fine("Building XAConnection for XADataSource: " + ds.toString());
                    XAConnection connection = ds.getDataSource().getXAConnection();
                    connections.add(connection);
                }
            }
            setProxiedXAConnections(connections);
        } finally {
            log.exiting(this.getClass().getName(), "buildProxiedXAConnections");
        }
    }

    /**
     * This method will walk the list of proxied XAConnections and obtain an XAResource for each one.
     */
    private void buildProxiedXAResources() throws SQLException {
        log.entering(this.getClass().getName(), "buildProxiedXAResources");
        try {
            List<XAResource> resources = new ArrayList<>();
            if (getProxiedXAConnections() != null) {
                for (XAConnection connection : getProxiedXAConnections()) {
                    XAResource resource = connection.getXAResource();
                    resources.add(resource);
                }
            }
            setProxiedXAResources(resources);
        } finally {
            log.exiting(this.getClass().getName(), "buildProxiedXAResources");
        }
    }

    /**
     * This method builds our map of Xid -> List<XAResource> for the collection of proxied XAResource instances.
     */
    private void buildProxiedXids(int flag) throws XAException {
        log.entering(this.getClass().getName(), "buildProxiedXids(int)", new Object[] {
                "flag", flag
        });
        try {
            log.info("Collecting the list of in-doubt transactions...");
            Map<XidKey, List<XAResource>> xidMap = new HashMap<>();
            if (getProxiedXAResources() != null) {
                // Visit each XAResource, and add it to the correct map entry(ies).
                for (XAResource resource : getProxiedXAResources()) {

                    // Ask the XAResource which transactions it is involved with.
                    Xid[] xids = resource.recover(flag);
                    if (xids != null) {
                        // Make sure the XAResource instance is associated with each of
                        // its transactions (Xids).
                        for (int i = 0; i < xids.length; i++) {
                            // First, make sure we have a map entry for this xid.
                            XidKey key = new XidKey(xids[i]);
                            List<XAResource> resourceList = xidMap.get(key);
                            if (resourceList == null) {
                                resourceList = new ArrayList<>();
                                xidMap.put(key, resourceList);
                            }

                            // Next, add this XAResource to the map entry's list.
                            if (!resourceList.contains(resource)) {
                                resourceList.add(resource);
                            }
                        }
                    }
                }
            }
            setProxiedXids(xidMap);
            log.info("Retrieved information about " + xidMap.size() + " in-doubt transaction(s).");
            if (log.isLoggable(Level.FINER)) {
                log.finer("Built the following Xid/XAResource mapping:");
                int i = 1;
                for (Map.Entry<XidKey, List<XAResource>> entry : xidMap.entrySet()) {
                    Xid xid = entry.getKey().getXid();
                    log.finer("Xid[" + i++ + "]: " + displayXid(xid));
                    log.finer("\t# of XAResources: " + entry.getValue().size());
                }
            }
        } finally {
            log.exiting(this.getClass().getName(), "buildProxiedXids(int)");
        }
    }

    private Xid[] buildXidArrayFromMap() {
        log.entering(this.getClass().getName(), "buildXidArrayFromMap");
        try {
            Xid[] result = null;
            if (getProxiedXids() != null) {
                Set<XidKey> keyset = getProxiedXids().keySet();
                int numXids = keyset.size();
                result = new Xid[numXids];
                int i = 0;
                for (XidKey key : keyset) {
                    result[i] = key.getXid();
                    i++;
                }
            }

            log.fine("Returning Xid[] of size: " + result.length);

            return result;
        } finally {
            log.exiting(this.getClass().getName(), "buildXidArrayFromMap");
        }
    }

    /**
     * This inner class is used to represent an xid-based key suitable for use within a HashMap.
     *
     */
    public static class XidKey {
        private Xid xid;

        public XidKey(Xid xid) {
            this.xid = xid;
        }

        public Xid getXid() {
            return this.xid;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(xid.getBranchQualifier());
            result = prime * result + xid.getFormatId();
            result = prime * result + Arrays.hashCode(xid.getGlobalTransactionId());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            XidKey other = (XidKey) obj;
            if (!Arrays.equals(xid.getBranchQualifier(), other.xid.getBranchQualifier()))
                return false;
            if (xid.getFormatId() != other.xid.getFormatId())
                return false;
            if (!Arrays.equals(xid.getGlobalTransactionId(), other.xid.getGlobalTransactionId()))
                return false;
            return true;
        }
    }
}
