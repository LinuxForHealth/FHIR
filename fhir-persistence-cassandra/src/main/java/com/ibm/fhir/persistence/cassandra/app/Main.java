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

    private static void bootstrapTenant(String tenantId) throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, "default"));
        PropertyGroup pg = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_DATASOURCES);
        if (pg != null) {
            for (PropertyEntry pe: pg.getProperties()) {
                final String dsId = pe.getName();
                PropertyGroup datasourceEntry = pg.getPropertyGroup(dsId);
                if (datasourceEntry != null) {
                    bootstrapTenantDatasource(tenantId, dsId);
                } else {
                    // configuration file is broken
                    throw new IllegalStateException("Datasource property is not a PropertyGroup: " + dsId);
                }
            }
        } else {
            throw new IllegalArgumentException("Tenant not found: " + tenantId);
        }
    }
    
    private static void bootstrapTenantDatasource(String tenantId, String dsId) {
        CqlSession session = DatasourceSessions.getSessionForBootstrap(tenantId, dsId);
        CreateSchema createSchema = new CreateSchema(tenantId);
        createSchema.createKeyspace(session, "SimpleStrategy", 2);
        createSchema.run(session);
    }
    
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: java -jar fhir-persistence-cassandra-cli.jar <fhir-config-dir> <tenant-id>");
        }
        
        final String fhirConfigDir = args[0];
        final String tenantId = args[1];
        try {
            FHIRConfiguration.setConfigHome(fhirConfigDir);
            
            try {
                bootstrapTenant(tenantId);
            } finally {
                DatasourceSessions.shutdown();
            }
        } catch (Exception x) {
            logger.log(Level.SEVERE, "bootstrap failed for tenant: " + tenantId, x);
        }
    }
}
