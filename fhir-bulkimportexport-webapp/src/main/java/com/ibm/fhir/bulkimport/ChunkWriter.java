/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * Bulk import Chunk implementation - the Writer.
 *
 */
@Dependent
public class ChunkWriter extends AbstractItemWriter {
    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());
    AmazonS3 cosClient = null;

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
    String cosEndpointUrl;

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
    @BatchProperty(name = Constants.PARTITION_RESOURCE_TYPE)
    String importPartitionResourceType;


    /**
     * If validate FHIR resources.
     */
    @Inject
    @BatchProperty(name = Constants.IMPORT_FHIR_IS_VALIDATION_ON)
    String fhirValidation;


    public ChunkWriter() {
        super();
    }

    // This is for the warning triggered by IMPORT_IS_COLLECT_OPERATIONOUTCOMES which controls if upload OperationOutcomes to COS/S3.
    @Override
    public void writeItems(List<java.lang.Object> arg0) throws Exception {
        boolean isValidationOn = false;
        Set<String> failValidationIds = new HashSet<>();

        if (fhirValidation != null) {
            isValidationOn = fhirValidation.equalsIgnoreCase("Y");
        }
        if (fhirTenant == null) {
            fhirTenant = "default";
            logger.info("writeItems: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.info("writeItems: Set DatastoreId to default!");
        }

        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());

        int processedNum = 0, succeededNum =0, failedNum = 0;
        ImportTransientUserData chunkData = (ImportTransientUserData) stepCtx.getTransientUserData();

        // Validate the resources first if required.
        if (isValidationOn) {
            long validationStartTimeInMilliSeconds = System.currentTimeMillis();
            for (Object objResJsonList : arg0) {
                @SuppressWarnings("unchecked")
                List<Resource> fhirResourceList = (List<Resource>) objResJsonList;

                for (Resource fhirResource : fhirResourceList) {
                    try {
                        BulkDataUtils.validateInput(fhirResource);
                    } catch (FHIRValidationException|FHIROperationException e) {
                        logger.warning("Failed to validate '" + fhirResource.getId() + "' due to error: " + e.getMessage());
                        failedNum++;
                        failValidationIds.add(fhirResource.getId());
                        if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
                            OperationOutcome operationOutCome = FHIRUtil.buildOperationOutcome(e, false);
                            FHIRGenerator.generator(Format.JSON).generate(operationOutCome, chunkData.getBufferStreamForImportError());
                            chunkData.getBufferStreamForImportError().write(Constants.NDJSON_LINESEPERATOR);
                        }
                    }
                }
            }
            chunkData.setTotalValidationMilliSeconds(chunkData.getTotalValidationMilliSeconds()
                    + (System.currentTimeMillis() - validationStartTimeInMilliSeconds));
        }

        // Begin writing the resources into DB.
        long writeStartTimeInMilliSeconds = System.currentTimeMillis();
        // Acquire a DB connection which will be used in the batch.
        // This doesn't really start the transaction, because the transaction has already been started by the JavaBatch
        // framework at this time point.
        txn.enroll();
        for (Object objResJsonList : arg0) {
            @SuppressWarnings("unchecked")
            List<Resource> fhirResourceList = (List<Resource>) objResJsonList;

            for (Resource fhirResource : fhirResourceList) {
                try {
                    String id = fhirResource.getId();
                    processedNum++;
                    // Skip the resources which failed the validation
                    if (failValidationIds.contains(id)) {
                        continue;
                    }
                    OperationOutcome operationOutcome =
                            fhirPersistence.update(persistenceContext, id, fhirResource).getOutcome();
                    succeededNum++;
                    if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES && operationOutcome != null) {
                        FHIRGenerator.generator(Format.JSON).generate(operationOutcome, chunkData.getBufferStreamForImport());
                        chunkData.getBufferStreamForImport().write(Constants.NDJSON_LINESEPERATOR);
                    }
                } catch (FHIROperationException e) {
                    logger.warning("Failed to import '" + fhirResource.getId() + "' due to error: " + e.getMessage());
                    failedNum++;
                    if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
                        OperationOutcome operationOutCome = FHIRUtil.buildOperationOutcome(e, false);
                        FHIRGenerator.generator(Format.JSON).generate(operationOutCome, chunkData.getBufferStreamForImportError());
                        chunkData.getBufferStreamForImportError().write(Constants.NDJSON_LINESEPERATOR);
                    }
                }
            }
        }
        // Release the DB connection.
        // This doesn't really commit the transaction, because the transaction was started and will be committed
        // by the JavaBatch framework.
        txn.unenroll();

        chunkData.setTotalWriteMilliSeconds(chunkData.getTotalWriteMilliSeconds() + (System.currentTimeMillis() - writeStartTimeInMilliSeconds));
        chunkData.setNumOfProcessedResources(chunkData.getNumOfProcessedResources() + processedNum + chunkData.getNumOfParseFailures());
        chunkData.setNumOfImportedResources(chunkData.getNumOfImportedResources() + succeededNum);
        chunkData.setNumOfImportFailures(chunkData.getNumOfImportFailures() + failedNum + chunkData.getNumOfParseFailures());
        // Reset NumOfParseFailures for next batch.
        chunkData.setNumOfParseFailures(0);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("writeItems: processed " + processedNum + " " + importPartitionResourceType + " from " +  chunkData.getImportPartitionWorkitem());
        }

        if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
            pushImportOperationOutcomes2COS(chunkData);
        }
    }


    private void pushImportOperationOutcomes2COS(ImportTransientUserData chunkData) throws Exception{
        // Create the COS/S3 client if it's not created yet.
        if (cosClient == null) {
            cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl, cosLocation);

            if (cosClient == null) {
                logger.warning("pushImportOperationOutcomes2COS: Failed to get CosClient!");
                throw new Exception("Failed to get CosClient!!");
            } else {
                logger.finer("pushImportOperationOutcomes2COS: Got CosClient successfully!");
            }
        }

        // Upload OperationOutcomes in buffer if it reaches the minimal size for multiple-parts upload.
        if (chunkData.getBufferStreamForImport().size() > Constants.COS_PART_MINIMALSIZE) {
            if (chunkData.getUploadIdForOperationOutcomes()  == null) {
                chunkData.setUploadIdForOperationOutcomes(BulkDataUtils.startPartUpload(cosClient,
                        cosOperationOutcomesBucketName, chunkData.getUniqueIDForImportOperationOutcomes(), true));
            }

            chunkData.getDataPacksForOperationOutcomes().add(BulkDataUtils.multiPartUpload(cosClient,
                    cosOperationOutcomesBucketName, chunkData.getUniqueIDForImportOperationOutcomes(),
                    chunkData.getUploadIdForOperationOutcomes(), new ByteArrayInputStream(chunkData.getBufferStreamForImport().toByteArray()),
                    chunkData.getBufferStreamForImport().size(), chunkData.getPartNumForOperationOutcomes()));
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("pushImportOperationOutcomesToCOS: " + chunkData.getBufferStreamForImport().size()
                    + " bytes were successfully appended to COS object - " + chunkData.getUniqueIDForImportOperationOutcomes());
            }
            chunkData.setPartNumForOperationOutcomes(chunkData.getPartNumForOperationOutcomes() + 1);
            chunkData.getBufferStreamForImport().reset();
        }

        // Upload OperationOutcomes in failure buffer if it reaches the minimal size for multiple-parts upload.
        if (chunkData.getBufferStreamForImportError().size() > Constants.COS_PART_MINIMALSIZE) {
            if (chunkData.getUploadIdForFailureOperationOutcomes()  == null) {
                chunkData.setUploadIdForFailureOperationOutcomes(BulkDataUtils.startPartUpload(cosClient,
                        cosOperationOutcomesBucketName, chunkData.getUniqueIDForImportFailureOperationOutcomes(), true));
            }

            chunkData.getDataPacksForFailureOperationOutcomes().add(BulkDataUtils.multiPartUpload(cosClient,
                    cosOperationOutcomesBucketName, chunkData.getUniqueIDForImportFailureOperationOutcomes(),
                    chunkData.getUploadIdForFailureOperationOutcomes(), new ByteArrayInputStream(chunkData.getBufferStreamForImportError().toByteArray()),
                    chunkData.getBufferStreamForImportError().size(), chunkData.getPartNumForFailureOperationOutcomes()));
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("pushImportOperationOutcomes2COS: " + chunkData.getBufferStreamForImportError().size()
                    + " bytes were successfully appended to COS object - " + chunkData.getUniqueIDForImportFailureOperationOutcomes());
            }
            chunkData.setPartNumForFailureOperationOutcomes(chunkData.getPartNumForFailureOperationOutcomes() + 1);
            chunkData.getBufferStreamForImportError().reset();
        }
    }
}
