/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;


/**
 * Hides the logic behind obtaining a JDBC {@link Connection} from the DAO code.
 * 
 * This strategy is used for configurations using the FHIR proxy datasource, 
 * which supports dynamic configurations of datasources without requiring
 * the application server to restart.
 * 
 * @implNote Refactored from {@link FHIRDbDAOImpl}. Improves separation of
 *           concerns by removing connection management code from the DAO
 *           and injecting it as a strategy instead. This not only simplifies
 *           things, but also makes it easier to implement new strategies,
 *           such as using a JEE datasource directly instead of the FHIR
 *           proxy datasource used here.
 */
public class FHIRDbProxyDatasourceConnectionStrategy extends FHIRDbConnectionStrategyBase {
    private static final Logger log = Logger.getLogger(FHIRDbProxyDatasourceConnectionStrategy.class.getName());
    private static final String CLASSNAME = FHIRDbDAOImpl.class.getName();

    // number of nanoseconds in a millisecond
    private static final long NANOMS = 1000000;
    
    // The (proxy) datasource
    private final DataSource datasource;
    
    // JNDI address of the (proxy) datasource
    private final String datasourceJndiName;


    /**
     * Public constructor. The proxy datasource must be present (registered in JNDI)
     * at server startup.
     * @throws FHIRPersistenceDBConnectException if the proxy datasource is not configured
     */
    public FHIRDbProxyDatasourceConnectionStrategy(UserTransaction userTx, TransactionSynchronizationRegistry trxSyncRegistry, Action newConnectionAction) throws FHIRException {
        super(userTx, trxSyncRegistry, newConnectionAction);
        final String METHODNAME = "FHIRDbProxyDatasourceConnectionProvider()";
        
        
        // Find the JNDI name of the datasource we want to use
        try {
            this.datasourceJndiName =
                    FHIRConfiguration.getInstance().loadConfiguration().getStringProperty(
                        FHIRConfiguration.PROPERTY_JDBC_DATASOURCE_JNDINAME, FHIRDbDAO.FHIRDB_JNDI_NAME_DEFAULT);
            
            if (log.isLoggable(Level.FINE)) {
                log.fine("Using datasource JNDI name: " + datasourceJndiName);
            }
        } catch (Throwable e) {
            FHIRException fx = new FHIRPersistenceDBConnectException("Failure acquiring datasource");
            log.log(Level.SEVERE, fx.addProbeId("Failure to find proxy datasource in FHIR server configuration"), e);
            throw fx;
        }
        
        // JNDI lookup. May fail if the server configuration is incorrect
        try {
            InitialContext ctxt = new InitialContext();
            datasource = (DataSource) ctxt.lookup(datasourceJndiName);
        } catch (Throwable e) {
            FHIRException fx = new FHIRPersistenceDBConnectException("Failure acquiring datasource");
            log.log(Level.SEVERE, fx.addProbeId("Failure acquiring datasource: " + datasourceJndiName), e);
            throw fx;
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, METHODNAME);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionProvider#getConnection()
     */
    @Override
    public Connection getConnection() throws FHIRPersistenceDBConnectException {
        Connection connection = null;
        final String METHODNAME = "getConnection";

        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, METHODNAME);
        }

        try {
            // Resources can be routed to different databases using the dsId currently
            // set on the context.
            String tenantId = FHIRRequestContext.get().getTenantId();
            String dsId = FHIRRequestContext.get().getDataStoreId();
            
            long start = System.nanoTime();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Getting connection for tenantId/dsId: [" + tenantId + "/" + dsId + "]...");
            }

            // Use the username/password interface to pass the tenantId and datasource id
            // paramters into the proxy datasource so that it can find the correct connection
            connection = datasource.getConnection(tenantId, dsId);

            if (log.isLoggable(Level.FINE)) {
                long deltams = (System.nanoTime() - start) / NANOMS;
                log.fine("Got the connection for [" + tenantId + "/" + dsId + "]. Took " + deltams + " ms");
            }

            // make sure this connection is configured the first time we see it in a transaction
            configure(connection, tenantId, dsId);
        } catch (Throwable e) {
            // Don't emit secrets in case they are returned to a client
            FHIRPersistenceDBConnectException fx =
                    new FHIRPersistenceDBConnectException("Failure acquiring connection for datasource");
            throw FHIRDbHelper.severe(log, fx, "Failure acquiring connection for datasource: " + datasourceJndiName, e);
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, METHODNAME);
            }
        }
        
        return connection;
    }



}
