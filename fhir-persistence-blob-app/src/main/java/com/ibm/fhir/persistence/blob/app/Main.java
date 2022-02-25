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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.blob.BlobContainerManager;

/**
 * Standalone application to provide support services (like reconciliation) for
 * payload offload using Azure Blob
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // properties for connecting to the database
    private final Properties databaseProperties = new Properties();

    private String fhirConfigDir;
    private String tenantId;
    private String dsId;
    private DbType dbType;
    private boolean reconcile;
    private boolean createContainer;
    private boolean dryRun = true;
    private String continuationToken = null;

    // A list of resources for which we want to read the blob contents
    private final List<String> resourceList = new ArrayList<>();

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
            case "--confirm":
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

        FHIRConfiguration.setConfigHome(fhirConfigDir);

        boolean didSomething = false;
        if (createContainer) {
            createContainer();
            didSomething = true;
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
        PayloadReconciliation process = new PayloadReconciliation(this.tenantId, this.dsId, databaseProperties, dbType, dryRun, this.maxScanSeconds);
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
        for (String blobName: this.resourceList) {
            ReadBlobValue action = new ReadBlobValue(this.tenantId, this.dsId, blobName);
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