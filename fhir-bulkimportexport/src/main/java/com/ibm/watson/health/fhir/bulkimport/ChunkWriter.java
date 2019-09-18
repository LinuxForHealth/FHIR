/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkimport;

import java.util.List;
import java.util.logging.Logger;
import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.waston.health.fhir.bulkcommon.COSUtils;
import com.ibm.waston.health.fhir.bulkcommon.Constants;
import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watson.health.fhir.persistence.helper.FHIRPersistenceHelper;

/**
 * Bulk import Chunk implementation - the Writer.
 * 
 * @author Albert Wang
 */
public class ChunkWriter extends AbstractItemWriter {
    private final static Logger logger = Logger.getLogger(ChunkWriter.class.getName());
    private AmazonS3 cosClient = null;
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
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = "cos.credential.ibm")
    String cosCredentialIbm;

    /**
     * @see AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
    }

    /**
     * @throws Exception
     * @see AbstractItemWriter#writeItems(List<java.lang.Object>)
     */
    @SuppressWarnings("unchecked")
    public void writeItems(List<java.lang.Object> arg0) throws Exception {
        if (fhirTenant == null) {
            fhirTenant = "default";
            logger.info("writeItems: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.info("writeItems: Set DatastoreId to default!");
        }
        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);
        if (cosClient == null) {
            logger.warning("writeItems: Failed to get CosClient!");
            return;
        } else {
            logger.finer("writeItems: Got CosClient successfully!");
        }

        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);

        int imported = 0;
        for (Object objResJasonList : arg0) {
            List<String> resCosObjectNameList = (List<String>) objResJasonList;

            for (String resCosObjectName : resCosObjectNameList) {
                imported += COSUtils.processCosObject(cosClient, cosBucketName, resCosObjectName, fhirPersistence,
                        persistenceContext);
                logger.info("writeItems: SubTotal Import - " + imported);
            }
        }
    }

}
