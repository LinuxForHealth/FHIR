/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;

/**
 * Bulk import Chunk implementation - the Writer.
 *
 */
public class ChunkWriter extends AbstractItemWriter {
    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());

    @Inject
    StepContext stepCtx;

    /**
     * The IBM COS API key or S3 access key.
     */
    @Inject
    @BatchProperty(name = Constants.COS_API_KEY)
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or S3 secret key.
     */
    @Inject
    @BatchProperty(name = Constants.COS_SRVINST_ID)
    String cosSrvinstId;

    /**
     * The IBM COS or S3 End point URL.
     */
    @Inject
    @BatchProperty(name = Constants.COS_ENDPOINT_URL)
    String cosEndpintUrl;

    /**
     * The IBM COS or S3 location.
     */
    @Inject
    @BatchProperty(name = Constants.COS_LOCATION)
    String cosLocation;

    /**
     * The IBM COS or S3 bucket name for import OperationOutcomes.
     */
    @Inject
    @BatchProperty(name = Constants.COS_OPERATIONOUTCOMES_BUCKET_NAME)
    String cosOperationOutcomesBucketName;

    /**
     * If use IBM credential or S3 secret keys.
     */
    @Inject
    @BatchProperty(name = Constants.COS_IS_IBM_CREDENTIAL)
    String cosCredentialIbm;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_TENANT)
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_DATASTORE_ID)
    String fhirDatastoreId;

    /**
     * Fhir resource type to process.
     */
    @Inject
    @BatchProperty(name = Constants.IMPORT_PARTITTION_RESOURCE_TYPE)
    String importPartitionResourceType;


    public ChunkWriter() {
        super();
    }

    @Override
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

        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());

        int processedNum = 0, succeededNum =0, failedNum = 0;
        ImportTransientUserData chunkData = (ImportTransientUserData) stepCtx.getTransientUserData();
        for (Object objResJasonList : arg0) {
            List<Resource> fhirResourceList = (List<Resource>) objResJasonList;

            for (Resource fhirResource : fhirResourceList) {
                try {
                    txn.begin();
                    OperationOutcome operationOutcome = fhirPersistence.update(persistenceContext, fhirResource.getId(), fhirResource).getOutcome();
                    txn.commit();
                    processedNum++;
                    succeededNum++;
                    if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
                        if (operationOutcome != null) {
                            FHIRGenerator.generator(Format.JSON).generate(operationOutcome, chunkData.getBufferStream4Import());
                            chunkData.getBufferStream4Import().write(Constants.NDJSON_LINESEPERATOR);
                        }
                    }
                } catch (FHIRPersistenceException e) {
                    logger.warning("Failed to import due to error: " + e.getMessage());
                    failedNum++;
                    if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
                        FHIRGenerator.generator(Format.JSON).generate(FHIRUtil.buildOperationOutcome(e, false), chunkData.getBufferStream4ImportError());
                        chunkData.getBufferStream4ImportError().write(Constants.NDJSON_LINESEPERATOR);
                    }
                }

            }
        }
        chunkData.setNumOfProcessedResources(chunkData.getNumOfProcessedResources() + processedNum);
        chunkData.setNumOfImportedResources(chunkData.getNumOfImportedResources() + succeededNum);
        chunkData.setNumOfImportFailures(chunkData.getNumOfImportFailures() + failedNum);
        logger.info("writeItems: processed " + processedNum + " " + importPartitionResourceType + " from " +  chunkData.getImportPartitionWorkitem());

        if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
            pushImportOperationOutcomes2COS(chunkData);
        }
    }


    private void pushImportOperationOutcomes2COS(ImportTransientUserData chunkData) throws Exception{
        AmazonS3 cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);

        if (cosClient == null) {
            logger.warning("pushImportOperationOutcomes2COS: Failed to get CosClient!");
            throw new Exception("pushImportOperationOutcomes2COS: Failed to get CosClient!!");
        } else {
            logger.finer("pushImportOperationOutcomes2COS: Got CosClient successfully!");
        }

        // Upload OperationOutcomes in buffer if it reaches the minimal size for multiple-parts upload.
        if (chunkData.getBufferStream4Import().size() > Constants.COS_PART_MINIMALSIZE) {
            if (chunkData.getUploadId4OperationOutcomes()  == null) {
                chunkData.setUploadId4OperationOutcomes(BulkDataUtils.startPartUpload(cosClient,
                        cosOperationOutcomesBucketName, chunkData.getUniqueID4ImportOperationOutcomes()));
            }

            chunkData.getDataPacks4OperationOutcomes().add(BulkDataUtils.multiPartUpload(cosClient,
                    cosOperationOutcomesBucketName, chunkData.getUniqueID4ImportOperationOutcomes(),
                    chunkData.getUploadId4OperationOutcomes(), new ByteArrayInputStream(chunkData.getBufferStream4Import().toByteArray()),
                    chunkData.getBufferStream4Import().size(), chunkData.getPartNum4OperationOutcomes()));
            logger.info("pushImportOperationOutcomes2COS: " + chunkData.getBufferStream4Import().size()
                    + " bytes were successfully appended to COS object - " + chunkData.getUniqueID4ImportOperationOutcomes());
            chunkData.setPartNum4OperationOutcomes(chunkData.getPartNum4OperationOutcomes() + 1);
            chunkData.getBufferStream4Import().reset();
        }

        // Upload OperationOutcomes in failure buffer if it reaches the minimal size for multiple-parts upload.
        if (chunkData.getBufferStream4ImportError().size() > Constants.COS_PART_MINIMALSIZE) {
            if (chunkData.getUploadId4FailureOperationOutcomes()  == null) {
                chunkData.setUploadId4FailureOperationOutcomes(BulkDataUtils.startPartUpload(cosClient,
                        cosOperationOutcomesBucketName, chunkData.getUniqueID4ImportFailureOperationOutcomes()));
            }

            chunkData.getDataPacks4FailureOperationOutcomes().add(BulkDataUtils.multiPartUpload(cosClient,
                    cosOperationOutcomesBucketName, chunkData.getUniqueID4ImportFailureOperationOutcomes(),
                    chunkData.getUploadId4FailureOperationOutcomes(), new ByteArrayInputStream(chunkData.getBufferStream4ImportError().toByteArray()),
                    chunkData.getBufferStream4ImportError().size(), chunkData.getPartNum4FailureOperationOutcomes()));
            logger.info("pushImportOperationOutcomes2COS: " + chunkData.getBufferStream4ImportError().size()
                    + " bytes were successfully appended to COS object - " + chunkData.getUniqueID4ImportFailureOperationOutcomes());
            chunkData.setPartNum4FailureOperationOutcomes(chunkData.getPartNum4FailureOperationOutcomes() + 1);
            chunkData.getBufferStream4ImportError().reset();
        }
    }
}
