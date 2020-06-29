/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Hides the logic behind obtaining a JDBC {@link Connection} from the DAO code.
 * 
 * Uses datasource and tenant ids configured in the fhir-server-config to
 * map directly to a managed datasource. All managed datasources must be
 * available when the server starts. This differs from the proxy
 * datasource strategy {@link FHIRDbPropsConnectionStrategy} which supports
 * dynamic (programmatic) definition of managed datasources.
 * 
 * @implNote Refactored from {@link FHIRDbDAOImpl}. Improves separation of
 *           concerns by removing connection management code from the DAO
 *           and injecting it as a strategy instead. This not only simplifies
 *           things, but also makes it easier to implement new strategies,
 *           such as using a JEE datasource directly instead of the FHIR
 *           proxy datasource used here.
 */
public class FHIRDbTenantDatasourceConnectionStrategy extends FHIRDbConnectionStrategyBase {
    private static final Logger log = Logger.getLogger(FHIRDbDAOImpl.class.getName());
    private static final String CLASSNAME = FHIRDbDAOImpl.class.getName();

    // number of nanoseconds in a millisecond
    private static final long NANOMS = 1000000;
    
    // JNDI address of the (proxy) datasource
    private final String datasourceBaseName = "jdbc/fhir_";

    // Cache of datasources we've found
    private final Map<String, DataSource> datasourceMap = new ConcurrentHashMap<>();
    
    // the flavor of the database we are configured to represent
    private final FHIRDbFlavor flavor;

    /**
     * Public constructor. The proxy datasource must be present (registered in JNDI)
     * at server startup.
     * @throws FHIRPersistenceDBConnectException if the proxy datasource is not configured
     */
    public FHIRDbTenantDatasourceConnectionStrategy(TransactionSynchronizationRegistry trxSyncRegistry, Action newConnectionAction) throws FHIRException {
        super(trxSyncRegistry, newConnectionAction);

        // Find the base JNDI name of the datasource we want to use
        try {
//            this.datasourceBaseName =
//                    FHIRConfiguration.getInstance().loadConfiguration().getStringProperty(
//                        FHIRConfiguration.PROPERTY_JDBC_DATASOURCE_JNDINAME, FHIRDbDAO.FHIRDB_JNDI_NAME_DEFAULT);
            
            if (log.isLoggable(Level.FINE)) {
                log.fine("Using datasource JNDI name: " + datasourceBaseName);
            }
        } catch (Throwable e) {
            FHIRException fx = new FHIRPersistenceDBConnectException("Failure acquiring datasource");
            log.log(Level.SEVERE, fx.addProbeId("Failure to find proxy datasource in FHIR server configuration"), e);
            throw fx;
        }
        
        this.flavor = createFlavor();
    }

    @Override
    public Connection getConnection() throws FHIRPersistenceDBConnectException {
        Connection connection = null;
        final String METHODNAME = "getConnection";

        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, METHODNAME);
        }
        
        // the dsId/tenantId specific datasource we need to locate
        DataSource datasource;
        
        // Resources can be routed to different databases using the dsId currently
        // set on the context.
        String tenantId = FHIRRequestContext.get().getTenantId();
        String dsId = FHIRRequestContext.get().getDataStoreId();
        
        // this is the important bit...how we name our actual datasources
        final String datasourceName = datasourceBaseName + tenantId + "_" + dsId;
        
        // Note: we don't need any synchronization around ConcurrentHashMap, but that
        // doesn't change the fact that we may look up the datasource and put it into
        // the map more than once. That's fine. There aren't any integrity issues, just
        // the chance of doing more work than necessary a single time, but we avoid the
        // need for any synchronization. We cache locally, because we've found that JNDI
        // lookups can become a bottleneck with high concurrency
        datasource = datasourceMap.get(datasourceName);
        if (datasource == null) {
            // cache miss
            try {
                InitialContext ctxt = new InitialContext();
                
                datasource = (DataSource) ctxt.lookup(datasourceName);
                datasourceMap.put(datasourceName, datasource);
            } catch (Throwable e) {
                // don't emit secrets in exceptions
                FHIRPersistenceDBConnectException fx = new FHIRPersistenceDBConnectException("Failure acquiring datasource");
                throw FHIRDbHelper.severe(log, fx, "Failure acquiring connection for datasource: " + datasourceName, e);
            } finally {
                if (log.isLoggable(Level.FINEST)) {
                    log.exiting(CLASSNAME, METHODNAME);
                }
            }
        }
        
        long start = System.nanoTime();
        if (log.isLoggable(Level.FINE)) {
            log.fine("Getting connection for tenantId/dsId: [" + tenantId + "/" + dsId + "]...");
        }

        // Now use the dsId/tenantId specific JEE datasource to get a connection
        try {
            connection = getConnection(datasource, tenantId, dsId);

            if (log.isLoggable(Level.FINE)) {
                long deltams = (System.nanoTime() - start) / NANOMS;
                log.fine("Got the connection for [" + tenantId + "/" + dsId + "]. Took " + deltams + " ms");
            }
        } catch (Throwable e) {
            // Don't emit secrets in case they are returned to a client
            FHIRPersistenceDBConnectException fx =
                    new FHIRPersistenceDBConnectException("Failure acquiring connection for datasource");
            throw FHIRDbHelper.severe(log, fx, "Failure acquiring connection for datasource: " + datasourceName, e);
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, METHODNAME);
            }
        }
        
        return connection;
    }
    
    @Override
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException {
        return this.flavor;
    }

    /**
     * Identify the flavor of the database using information from the
     * datasource configuration.
     * TODO duplicate code in FHIRDbConnectionStrategyBase. Refactor
     * @return
     * @throws FHIRPersistenceException
     */
    private FHIRDbFlavor createFlavor() throws FHIRPersistenceDataAccessException {
        FHIRDbFlavor result;
        
        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        // Retrieve the property group pertaining to the specified datastore.
        // Find and set the tenantKey for the request, otherwise subsequent pulls from the pool
        // miss the tenantKey.
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG != null) {
            
            try {
                boolean multitenant = false;
                String typeValue = dsPG.getStringProperty("type");
                
                DbType type = DbType.from(typeValue);
                if (type == DbType.DB2) {
                    // We make this absolute for now. May change in the future if we
                    // support a single-tenant schema in DB2.
                    multitenant = true;
                }
                
                result = new FHIRDbFlavorImpl(type, multitenant);
            }
            catch (Exception x) {
                log.log(Level.SEVERE, "No type property found for datastore '" + datastoreId + "'", x);
                throw new FHIRPersistenceDataAccessException("Datastore configuration issue. Details in server logs");
            }
        } else {
            log.log(Level.SEVERE, "Missing datastore configuration for '" + datastoreId + "'");
            throw new FHIRPersistenceDataAccessException("Datastore configuration issue. Details in server logs");
        }
        
        return result;
    }
}
