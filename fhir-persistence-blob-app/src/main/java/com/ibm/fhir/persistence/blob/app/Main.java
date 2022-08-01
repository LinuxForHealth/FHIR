/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.blob.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.pool.DatabaseSupport;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.blob.BlobContainerManager;
import com.ibm.fhir.persistence.blob.BlobName;
import com.ibm.fhir.persistence.jdbc.cache.ResourceTypeMaps;
import com.ibm.fhir.persistence.jdbc.dao.impl.ReadResourceTypesDAO;
import com.ibm.fhir.schema.model.ResourceType;

/**
 * Standalone application to provide support services (like reconciliation) for
 * payload offload using Azure Blob
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // properties for connecting to the database
    private final Properties databaseProperties = new Properties();

    private String fhirConfigDir;
    private String tenantId = "default";
    private String dsId = "default";
    private DbType dbType;
    private boolean reconcile;
    private boolean createContainer;
    private boolean dryRun = true;
    private String continuationToken = null;

    // A list of resources for which we want to read the blob contents
    private final List<String> resourceList = new ArrayList<>();

    // Provides access to database connections and transaction handling
    private DatabaseSupport dbSupport;

    // Support for resource type lookup when we have a database config
    private ResourceTypeMaps resourceTypeMaps;

    // Stop scanning after this number of seconds. Use continuationToken to continue
    private int maxScanSeconds = 120;

    /**
     * Parse the command line arguments
     * @param args
     */
    protected void parseArgs(String[] args) {
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
            case "--continuation-token":
                if (++i < args.length) {
                    this.continuationToken = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for --continuation-token");
                }
                break;
            case "--db-properties":
                if (++i < args.length) {
                    readDatabaseProperties(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for --db-properties");
                }
                break;
            case "--db-type":
                if (++i < args.length) {
                    this.dbType = DbType.from(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for --db-type");
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
                    this.dsId = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for --ds-id");
                }
                break;
            case "--reconcile":
                this.reconcile = true;
                break;
            case "--create-container":
                this.createContainer = true;
                break;
            case "--no-dry-run":
                this.dryRun = false;
                break;
            case "--dry-run":
                this.dryRun = true;
                break;
            case "--max-scan-seconds":
                if (++i < args.length) {
                    this.maxScanSeconds = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for --max-scan-seconds");
                }
                break;
            case "--read":
                if (++i < args.length) {
                    this.resourceList.add(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for --read");
                }
                break;
            }
        }
    }

    /**
     * Read database properties from the given filename
     * @param filename
     */
    protected void readDatabaseProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            databaseProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Process what was requested on the CLI
     */
    protected void process() throws Exception {
        if (fhirConfigDir == null || fhirConfigDir.isEmpty()) {
            throw new IllegalArgumentException("File config dir not configured");
        }

        File f = new File(this.fhirConfigDir);
        if (!f.exists() || !f.isDirectory()) {
            throw new IllegalArgumentException("--fhir-config-dir does not point to a directory: '" + this.fhirConfigDir + "'");
        }

        if (!Files.isDirectory(f.toPath().resolve("config"))) {
            throw new IllegalArgumentException("--fhir-config-dir must be the parent of a directory named 'config'");
        }

        FHIRConfiguration.setConfigHome(fhirConfigDir);

        boolean didSomething = false;
        if (createContainer) {
            createContainer();
            didSomething = true;
        }

        if (this.databaseProperties.size() > 0 && this.dbType != null) {
            this.dbSupport = new DatabaseSupport(this.databaseProperties, dbType);
            this.dbSupport.init();
            fillResourceTypeMaps();
        }

        // Run reconciliation OR read resources, not both
        if (this.reconcile) {
            runReconciliation();
            didSomething = true;
        } else if (this.resourceList.size() > 0) {
            doReads();
            didSomething = true;
        }

        if (!didSomething) {
            throw new IllegalArgumentException("Must specify at least one action");
        }
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
     * Create the container
     */
    private void createContainer() throws FHIRException {
        CreateContainer action = new CreateContainer(this.tenantId, this.dsId, this.dryRun);
        action.run();
    }

    /**
     * Run the reconciliation process
     */
    protected void runReconciliation() throws Exception {
        if (this.databaseProperties == null) {
            // bad args on the CLI
            throw new IllegalArgumentException("Missing database configuration which is required to run reconciliation");
        }

        if (this.resourceTypeMaps == null || !this.resourceTypeMaps.isInitialized()) {
            throw new IllegalStateException("ResourceTypeMaps not initialized");
        }

        PayloadReconciliation process = new PayloadReconciliation(this.tenantId, this.dsId, dbSupport, this.resourceTypeMaps, dryRun, this.maxScanSeconds);
        String newContinuationToken = process.run(this.continuationToken);

        if (newContinuationToken == null) {
            logger.info("Scan complete");
        }
    }

    /**
     * Read each of the resources in the resourceList
     * @throws Exception
     */
    protected void doReads() throws Exception {
        for (String nm: this.resourceList) {
            // resourceTypeMaps may be null or empty if no database config
            // has been provided, in which case BlobName can't do any
            // conversion
            BlobName blobName = BlobName.create(resourceTypeMaps, nm);
            if (blobName.getResourceTypeId() < 0) {
                throw new IllegalArgumentException("Must use resourceTypeId for path, or provide a database configuration to allow lookup");
            }
            ReadBlobValue action = new ReadBlobValue(this.tenantId, this.dsId, this.resourceTypeMaps, blobName);
            action.run();
        }
    }

    /**
     * Shut down any thread pools so we can make a quick exit
     */
    protected void terminate() {
        BlobContainerManager.shutdown();
    }

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args) {
        Main m = new Main();
        try {
            m.parseArgs(args);
            m.process();
            m.terminate();
        } catch (Exception x) {
            logger.log(Level.SEVERE, "failed", x);
            System.exit(-1);
        }
    }
}