/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.pool.DatabaseSupport;
import com.ibm.fhir.persistence.cassandra.cql.CreateSchema;
import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;
import com.ibm.fhir.persistence.cassandra.reconcile.PayloadReconciliation;
import com.ibm.fhir.persistence.jdbc.cache.ResourceTypeMaps;
import com.ibm.fhir.persistence.jdbc.dao.impl.ReadResourceTypesDAO;
import com.ibm.fhir.schema.model.ResourceType;

/**
 * Admin operations for the IBM FHIR Server payload offload support
 * in Cassandra.
 * <pre>
 *  1. Bootstrap the offload keyspace and tables in Cassandra 
 *  2. Run the reconciliation process to look for orphaned payload records
 * </pre>
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private int replicationFactor = 0;
    private String fhirConfigDir;
    private String tenantId;
    private String dsIdArg = "default";
    
    // Perform the tenant bootstrap process
    private boolean bootstrap;
    
    // Run the reconciliation process
    private boolean reconcile;
    
    // When set, reconciliation reports inconsistencies but does not delete anything
    private boolean dryRun;
    
    // Properties for the RDBMS connection
    private Properties dbProperties = new Properties();

    // The type of database we are talking to
    private DbType dbType;
    
    // Holds a mapping between resource type ids and names
    private ResourceTypeMaps resourceTypeMaps;

    // simple transaction and database connection pool
    private DatabaseSupport dbSupport;

    /**
     * Create the Cassandra keyspace and tables used to persist FHIR payloads
     * @throws Exception
     */
    private void bootstrapTenant() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, "default"));
        PropertyGroup pg = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD);
        if (pg != null) {
            // Bootstrap for each of the datastore ids configured for the tenant
            for (PropertyEntry pe: pg.getProperties()) {
                final String dsId = pe.getName();
                PropertyGroup datasourceEntry = pg.getPropertyGroup(dsId);
                if (datasourceEntry != null) {
                    final String datasourceType = datasourceEntry.getStringProperty("type");
                    if ("cassandra".equalsIgnoreCase(datasourceType)) {
                        PropertyGroup connectionProperties = datasourceEntry.getPropertyGroup("connectionProperties");
                        if (connectionProperties != null) {
                            final String tenantKeyspace = connectionProperties.getStringProperty("tenantKeyspace", tenantId);
                            bootstrapTenantDatasource(tenantKeyspace, dsId, replicationFactor);
                        } else {
                            throw new IllegalStateException("Missing connectionProperties in payload datasource: " + tenantId + "/" + dsId);
                        }
                    }
                } else {
                    // configuration file is broken
                    throw new IllegalStateException("Datasource property is not a PropertyGroup: " + dsId);
                }
            }
        } else {
            throw new IllegalArgumentException("Tenant not found: " + tenantId);
        }
    }
    
    /**
     * Create the Cassandra keyspace and tables for the given tenant and datastore ids.
     * @param tenantId
     * @param dsId
     * @param replicationFactor
     */
    private void bootstrapTenantDatasource(String tenantKeyspace, String dsId, int replicationFactor) {
        CqlSession session = DatasourceSessions.getSessionForBootstrap(tenantId, dsId);
        CreateSchema createSchema = new CreateSchema(tenantKeyspace);
        createSchema.createKeyspace(session, "SimpleStrategy", replicationFactor);
        createSchema.run(session);
    }

    /**
     * Parse the command-line arguments
     * @param args
     */
    private void parseArgs(String[] args) throws Exception {
        
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--fhir-config-dir":
                if (++i < args.length) {
                    this.fhirConfigDir = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for --fhir-config-dir");
                }
                break;
            case "--replication-factor":
                if (++i < args.length) {
                    this.replicationFactor = Integer.parseInt(args[i]);
                    if (replicationFactor < 1) {
                        throw new IllegalArgumentException("replication-factor must be >= 1, not " + replicationFactor);
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for --replication-factor");
                }
                break;
            case "--tenant-id":
                if (++i < args.length) {
                    this.tenantId = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for --tenant-id");
                }
                break;
            case "--ds-id":
                if (++i < args.length) {
                    this.dsIdArg = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for --ds-id");
                }
                break;
            case "--db-type":
                if (++i < args.length) {
                    this.dbType = DbType.from(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for --db-type");
                }
                break;
            case "--db-properties":
                if (++i < args.length) {
                    readDatabaseProperties(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for --db-properties");
                }
                break;
            case "--bootstrap":
                this.bootstrap = true;
                break;
            case "--reconcile":
                this.reconcile = true;
                break;
            case "--dry-run":
                this.dryRun = true;
                break;
            }
        }

        if (bootstrap) {
            if (this.dryRun) {
                // Just in case someone thinks dry-run applies to bootstrap
                throw new IllegalArgumentException("--dry-run can only be used with --reconcile");
            }
            if (replicationFactor == 0) {
                logger.warning("Using default replication factor of 1 - not suitable for production");
                this.replicationFactor = 1;
            }
        }
    }

    /**
     * Run the reconciliation process which scans the payload store and checks that
     * each record is supported by the correct meta-data in the RDBMS.
     * @throws Exception
     */
    private void runReconciliation() throws Exception {
        fillResourceTypeMaps();
        PayloadReconciliation process = new PayloadReconciliation(this.tenantId, this.dsIdArg, dbSupport, this.resourceTypeMaps, dryRun);
        process.run();
    }

    /**
     * Read the resource types from the database and populate the two lookup
     * maps for easy conversion between resource type id and resource type name
     */
    private void fillResourceTypeMaps() {
        logger.info("Filling resource type maps");
        try (ITransaction tx = dbSupport.getTransaction()) {
            try (Connection c = dbSupport.getConnection()) {
                ReadResourceTypesDAO dao = new ReadResourceTypesDAO();
                List<ResourceType> resourceTypes = dao.run(dbSupport.getTranslator(), c);
                this.resourceTypeMaps = new ResourceTypeMaps();
                this.resourceTypeMaps.init(resourceTypes);
            } catch (SQLException x) {
                tx.setRollbackOnly();
                throw dbSupport.getTranslator().translate(x);
            }
        }
    }

    /**
     * Read the properties file specified by path into the dbProperties
     * @param path
     * @throws Exception
     */
    private void readDatabaseProperties(String path) throws IOException {
        try (InputStream in = new FileInputStream(path)) {
            this.dbProperties.load(in);
        } catch (IOException x) {
            logger.log(Level.SEVERE, "Error loading properties from database properties file '" + path + "'", x);
            throw x;
        }
    }

    /**
     * Perform the action requested on the command line
     * @throws Exception
     */
    private void process() throws Exception {
        if (fhirConfigDir == null || fhirConfigDir.isEmpty()) {
            throw new IllegalArgumentException("File config dir not configured");
        }

        File f = new File(this.fhirConfigDir);
        if (!f.exists() || !f.isDirectory()) {
            throw new IllegalArgumentException("--fhir-config-dir does not point to a directory: '" + this.fhirConfigDir + "'");
        }

        FHIRConfiguration.setConfigHome(fhirConfigDir);
        if (this.bootstrap) {
            bootstrapTenant();
        }
        
        this.dbSupport = new DatabaseSupport(dbProperties, dbType);
        this.dbSupport.init();

        if (this.reconcile) {
            runReconciliation();
        }
    }

    /**
     * Main entry point
     * @param args
     */
    public static void main(String[] args) {

        Main m = new Main();
        
        try {
            m.parseArgs(args);
            try {
                m.process();
            } finally {
                DatasourceSessions.shutdown();
            }
        } catch (Exception x) {
            logger.log(Level.SEVERE, "[FAILED]", x);
            System.exit(1);
        }
    }
}
