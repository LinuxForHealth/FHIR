/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.fhir.bulkcommon.COSUtils;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;

/**
 * Bulk import Batchlet implementation.
 * 
 * @author Albert Wang
 */
public class BulkImportBatchLet implements Batchlet {
    private final static Logger logger = Logger.getLogger(BulkImportBatchLet.class.getName());
    private AmazonS3 cosClient = null;

    /**
     * The IBM COS API key or S3 access key.
     */
    @Inject
    @BatchProperty(name = "cos.api.key")
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or s3 secret key.
     */
    @Inject
    @BatchProperty(name = "cos.srvinst.id")
    String cosSrvinstId;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.endpointurl")
    String cosEndpintUrl;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.location")
    String cosLocation;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.name")
    String cosBucketName;

    /**
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.objectname")
    String cosBucketObjectName;

    /**
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = "cos.credential.ibm")
    String cosCredentialIbm;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = "fhir.tenant")
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = "fhir.datastoreid")
    String fhirDatastoreId;

    /**
     * Default constructor.
     */
    public BulkImportBatchLet() {

    }

    /**
     * This flag gets set if the batchlet is stopped. This will stop the import.
     */
    private boolean stopRequested = false;

    /**
     * Main entry point.
     */
    @Override
    public String process() throws Exception {
        String exitStatus;

        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);
        if (cosClient == null) {
            logger.warning("process: Failed to get CosClient!");
            return "Failed to get CosClient!";
        } else {
            logger.finer("process: Got CosClient successfully!");
        }

        if (cosBucketName == null) {
            cosBucketName = "fhir-bulkImExport-Connectathon";
        }
        cosBucketName = cosBucketName.toLowerCase();
        if (!cosClient.doesBucketExist(cosBucketName)) {
            logger.warning("process: Bucket '" + cosBucketName + "' not found!");
            COSUtils.listBuckets(cosClient);
            return "Bucket not found!";
        }
        if (fhirTenant == null) {
            fhirTenant = "default";
            logger.info("process: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.info("process: Set DatastoreId to default!");
        }

        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));
        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);

        boolean moreResults = true;
        String nextToken = "";
        int maxKeys = 100, imported = 0;
        while (moreResults && !stopRequested) {
            ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(cosBucketName).withMaxKeys(maxKeys)
                    .withContinuationToken(nextToken);

            ListObjectsV2Result result = cosClient.listObjectsV2(request);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                boolean isToBeProccessed = false;
                if (cosBucketObjectName != null && cosBucketObjectName.trim().length() > 0) {
                    if (objectSummary.getKey().startsWith(cosBucketObjectName.trim())) {
                        isToBeProccessed = true;
                    }
                } else {
                    isToBeProccessed = true;
                }

                if (isToBeProccessed) {
                    logger.info("process: COS Object(" + objectSummary.getKey() + ") - " + objectSummary.getSize()
                            + " bytes.");
                    imported += COSUtils.processCosObject(cosClient, cosBucketName, objectSummary.getKey(),
                            fhirPersistence, persistenceContext);
                }
            }

            if (result.isTruncated()) {
                nextToken = result.getNextContinuationToken();
            } else {
                nextToken = "";
                moreResults = false;
            }
        }

        exitStatus = "Imported - " + imported;
        logger.info("process: " + exitStatus);

        return exitStatus;
    }

    /**
     * Called if the batchlet is stopped by the container.
     */
    @Override
    public void stop() throws Exception {
        logger.info("stop: Stop request accepted!");
        stopRequested = true;
    }
}
