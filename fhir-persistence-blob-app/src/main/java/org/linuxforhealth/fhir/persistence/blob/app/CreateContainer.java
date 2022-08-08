/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.blob.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.persistence.blob.BlobContainerManager;
import org.linuxforhealth.fhir.persistence.blob.BlobManagedContainer;

/**
 * Create the container if it doesn't currently exist
 */
public class CreateContainer {
    private static final Logger logger = Logger.getLogger(CreateContainer.class.getName());
    private final String tenantId;
    private final String dsId;
    private final boolean dryRun;

    /**
     * Public constructor
     * @param tenantId
     * @param dsId
     * @param dryRun
     */
    public CreateContainer(String tenantId, String dsId, boolean dryRun) {
        this.tenantId = tenantId;
        this.dsId = dsId;
        this.dryRun = dryRun;
    }

    /**
     * Create the container for the configured tenant and datasource pair
     */
    public void run() throws FHIRException {
        // Set up the request context for the configured tenant and datastore
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));

        // Check to see if the container already exists, and if not, then 
        // issue the create container command and wait for the response
        BlobManagedContainer bmc = BlobContainerManager.getSessionForTenantDatasource();
        try {
            Boolean exists = bmc.getClient().exists().block();
            if (exists != null && exists.booleanValue()) {
                logger.info("Container already exists: " + bmc.getContainerName());
                return;
            }
            
            if (!this.dryRun) {
                logger.info("Creating container: " + bmc.getContainerName());
                bmc.getClient().create().block();
                logger.info("Container created");
            } else {
                logger.info("[dry-run] Creating container: " + bmc.getContainerName());
            }
        } catch (RuntimeException x) {
            logger.log(Level.SEVERE, "failed to create container: " + bmc.getContainerName(), x);
            throw x;
        }
    }
}