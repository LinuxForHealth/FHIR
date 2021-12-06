/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.persistence.cassandra.cql.CreateSchema;
import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;

/**
 * Bootstrap all the Cassandra databases for the given tenant
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static void bootstrapTenant(String tenantId, int replicationFactor) throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, "default"));
        PropertyGroup pg = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD);
        if (pg != null) {
            for (PropertyEntry pe: pg.getProperties()) {
                final String dsId = pe.getName();
                PropertyGroup datasourceEntry = pg.getPropertyGroup(dsId);
                if (datasourceEntry != null) {
                    bootstrapTenantDatasource(tenantId, dsId, replicationFactor);
                } else {
                    // configuration file is broken
                    throw new IllegalStateException("Datasource property is not a PropertyGroup: " + dsId);
                }
            }
        } else {
            throw new IllegalArgumentException("Tenant not found: " + tenantId);
        }
    }
    
    private static void bootstrapTenantDatasource(String tenantId, String dsId, int replicationFactor) {
        CqlSession session = DatasourceSessions.getSessionForBootstrap(tenantId, dsId);
        CreateSchema createSchema = new CreateSchema(tenantId);
        createSchema.createKeyspace(session, "SimpleStrategy", replicationFactor);
        createSchema.run(session);
    }
    
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: java -jar fhir-persistence-cassandra-VERSION-cli.jar <fhir-config-dir> <tenant-id> [ replication-factor ]");
        }
        
        int replicationFactor = 1;
        final String fhirConfigDir = args[0];
        final String tenantId = args[1];
        
        if (args.length == 3) {
            replicationFactor = Integer.parseInt(args[2]);
            if (replicationFactor < 1) {
                throw new IllegalArgumentException("replication-factor must be >= 1, not " + replicationFactor);
            }
        }
        try {
            FHIRConfiguration.setConfigHome(fhirConfigDir);
            
            try {
                bootstrapTenant(tenantId, replicationFactor);
            } finally {
                DatasourceSessions.shutdown();
            }
        } catch (Exception x) {
            logger.log(Level.SEVERE, "bootstrap failed for tenant: " + tenantId, x);
        }
    }
}
