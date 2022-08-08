/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cos.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.persistence.cos.client.COSPayloadClient;
import org.linuxforhealth.fhir.persistence.cos.client.COSPayloadHelper;
import org.linuxforhealth.fhir.persistence.cos.impl.COSClientManager;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Utility app to bootstrap the COS payload datastore for the given tenant. For
 * COS, this just requires ensuring we have the bucket defined.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final String JAR_NAME = "fhir-persistence-cos-*-cli.jar";

    /**
     * Configure the datastore (COS in this case) so that it's ready to support
     * storage of resource payloads.
     * @param tenantId
     * @param dsId
     * @throws Exception
     */
    private static void bootstrapCosForTenant(String tenantId, String dsId) throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));

        // Obtain the PropertyGroup for the payload datasource configuration for
        // the current tenant
        PropertyGroup pg = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + dsId);
        if (pg != null) {
            if ("cos".equals(pg.getStringProperty("type"))) {
                String bucketName = pg.getStringProperty("bucketName");
                if (bucketName == null || bucketName.isEmpty()) {
                    // Compute a deterministic value for the bucket based on tenantId
                    bucketName = COSPayloadHelper.makeTenantBucketName(tenantId, dsId);
                }
                createBucket(bucketName);
            } else {
                // Not a COS datasource
                logger.warning("NOP. Payload datasource configuration type is not cos: '" + pg.getStringProperty("type") + "'");
            }
        } else {
            throw new IllegalArgumentException("Tenant payload datasources configuration not found: " + tenantId);
        }
    }

    /**
     * Make sure we have a bucket for the tenant
     * @param tenantId
     * @param dsId
     */
    private static void createBucket(String bucketName) throws FHIRPersistenceException {
        COSPayloadClient cosClient = COSClientManager.getClientForTenantDatasource();
        cosClient.createBucketIfNeeded(bucketName);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: java -jar " + JAR_NAME + " <fhir-config-dir> <tenant-id>");
        }

        final String fhirConfigDir = args[0];
        final String tenantId = args[1];
        try {
            FHIRConfiguration.setConfigHome(fhirConfigDir);

            try {
                // currently we only support one payload datasource
                bootstrapCosForTenant(tenantId, "default");
            } finally {
                COSClientManager.shutdown();
            }
        } catch (Exception x) {
            logger.log(Level.SEVERE, "bootstrap failed for tenant: " + tenantId, x);
        }
    }
}
