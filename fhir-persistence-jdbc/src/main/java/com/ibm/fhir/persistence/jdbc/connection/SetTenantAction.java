/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigProvider;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.api.BadTenantFrozenException;
import com.ibm.fhir.database.utils.api.BadTenantKeyException;
import com.ibm.fhir.database.utils.api.BadTenantNameException;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Set the tenant variable in the session (part of Db2 multi-tenancy
 * support). This needs to be executed at the beginning of an
 * interaction with a database connection, before any other
 * DML/SQL.
 */
public class SetTenantAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(SetTenantAction.class.getName());
    
    // Used to indicate the default behavior of a datastore as multitenant.
    public static final List<String> DATASTORE_REQUIRES_ROW_PERMISSIONS = Arrays.asList("db2");
    
    // From where we get our configuration information
    private final FHIRConfigProvider configProvider;

    /**
     * Public constructor. No next action, so this will be the last action applied
     * @param configProvider adapter for access to fhir-server-config properties
     */
    public SetTenantAction(FHIRConfigProvider configProvider) {
        super();
        this.configProvider = configProvider;
    }
    
    /**
     * Public constructor
     * @param configProvider adapter for access to fhir-server-config properties
     * @param next the next action in the chain
     */
    public SetTenantAction(FHIRConfigProvider configProvider, Action next) {
        super(next);
        this.configProvider = configProvider;
    }

    @Override
    public void performOn(FHIRDbFlavor flavor, Connection c) throws FHIRPersistenceDBConnectException {
        
        configureTenantAccess(c);
        
        // perform next action in the chain
        super.performOn(flavor, c);
    }

    /**
     * Find the tenantKey for the datasource/tenant from the FHIR server configuration
     * and use it to set up the connection for access by this tenant by setting 
     * the SV_TENANT_ID session variable (via the FHIR_ADMIN.SET_TENANT stored procedure).
     * Note that the database user we connect as does not have direct access to WRITE
     * to the SV_TENANT_ID...it can only be set through the SET_TENANT call, for which
     * we have been explicitly granted execute privilege.
     *
     * @param connection the connection to configure
     * @throws FHIRPersistenceException if the configuration fails
     */
    public void configureTenantAccess(Connection connection) throws FHIRPersistenceDBConnectException {
        boolean multiTenantFeature = false;
        String tenantKey = null;
        
        // Get the datastore and tenant from the request context
        String tenantName = FHIRRequestContext.get().getTenantId();
        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        // Retrieve the property group pertaining to the specified datastore.
        // Find and set the tenantKey for the request, otherwise subsequent pulls from the pool
        // miss the tenantKey.
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = configProvider.getPropertyGroup(dsPropertyName);
        if (dsPG != null) {
            
            try {
                tenantKey = dsPG.getStringProperty("tenantKey", null);
                if (log.isLoggable(Level.FINE)) {
                    log.finer("tenantKey is null? = [" + Objects.isNull(tenantKey) + "]");
                }
    
                // Specific to Db2 right now, we want to switch behavior if multitenant row level permission is required.
                String type = dsPG.getStringProperty("type", null);
                if (type != null) {
                    // Based on the default for the database type, the code.
                    multiTenantFeature =
                            dsPG.getBooleanProperty("multitenant", DATASTORE_REQUIRES_ROW_PERMISSIONS.contains(type));
                }
            } catch (Exception x) {
                log.log(Level.SEVERE, "Datastore configuration issue for '" + datastoreId + "'", x);
                throw new FHIRPersistenceDBConnectException("Datastore configuration issue. Details in server logs");
            }
        } else {
            log.fine("there are no datasource properties found for : [" + dsPropertyName + "]");
        }

        // only try to configure the connection if we are running in a multi-tenant configuration.
        if (multiTenantFeature) {
            if (Objects.isNull(tenantKey)) {
                // Should have been set.
                throw FHIRDbHelper.buildFHIRPersistenceDBConnectException("MISSING TENANT KEY [" + tenantName + "]",
                        IssueType.EXCEPTION);
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("Setting tenant access on connection for: [" + tenantName + "]");
            }

            // At this point, tenantName and tenantKey should be non-null.
            Db2SetTenantVariable cmd = new Db2SetTenantVariable("FHIR_ADMIN", tenantName, tenantKey);
            JdbcTarget target = new JdbcTarget(connection);
            Db2Adapter adapter = new Db2Adapter(target);
            try {
                adapter.runStatement(cmd);
            } catch (BadTenantKeyException x) {
                throw FHIRDbHelper.buildFHIRPersistenceDBConnectException("MISSING OR INVALID TENANT KEY [" + tenantName + "]",
                        IssueType.EXCEPTION);
            } catch (BadTenantNameException x) {
                throw FHIRDbHelper.buildFHIRPersistenceDBConnectException("MISSING OR INVALID TENANT NAME [" + tenantName + "]",
                        IssueType.EXCEPTION);
            } catch (BadTenantFrozenException x) {
                throw FHIRDbHelper.buildFHIRPersistenceDBConnectException("TENANT FROZEN [" + tenantName + "]", IssueType.EXCEPTION);
            }
        }
    }

}
