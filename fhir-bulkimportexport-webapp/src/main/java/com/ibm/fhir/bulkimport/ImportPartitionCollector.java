/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionCollector;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;

@Dependent
public class ImportPartitionCollector implements PartitionCollector {
    private static final Logger logger = Logger.getLogger(ImportPartitionCollector.class.getName());
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

    public ImportPartitionCollector() {
        // The injected properties are not available at class construction time
        // These values are lazy injected BEFORE calling 'collectPartitionData'.
    }

    @Override
    public Serializable collectPartitionData() throws Exception {
        if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
            cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl, cosLocation);

            if (cosClient == null) {
                logger.warning("collectPartitionData: Failed to get CosClient!");
                throw new Exception("Failed to get CosClient!!");
            } else {
                logger.finer("ImportPartitionCollector: Succeed get CosClient!");
            }
        }

        ImportTransientUserData partitionSummaryData  = (ImportTransientUserData)stepCtx.getTransientUserData();
        BatchStatus batchStatus = stepCtx.getBatchStatus();

        // If the job is being stopped or in other status except for "started", then do cleanup for the partition.
        if (!batchStatus.equals(BatchStatus.STARTED)) {
            BulkDataUtils.cleanupTransientUserData(partitionSummaryData, true);
            return null;
        }

        // This function is called at partition chunk check points and also at the end of partition processing.
        // So, check the NumOfToBeImported to make sure at the end of partition processing:
        // (1) upload the remaining OperationComes to COS/S3.
        // (2) finish the multiple-parts uploads.
        // (3) release the resources hold by this partition.
        if (partitionSummaryData.getNumOfToBeImported() == 0) {
            if (Constants.IMPORT_IS_COLLECT_OPERATIONOUTCOMES) {
                // Upload remaining OperationOutcomes.
                if (partitionSummaryData.getBufferStreamForImport().size() > 0) {
                    if (partitionSummaryData.getUploadIdForOperationOutcomes()  == null) {
                        partitionSummaryData.setUploadIdForOperationOutcomes(BulkDataUtils.startPartUpload(cosClient,
                                cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportOperationOutcomes(), true));
                    }

                    partitionSummaryData.getDataPacksForOperationOutcomes().add(BulkDataUtils.multiPartUpload(cosClient,
                            cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportOperationOutcomes(),
                            partitionSummaryData.getUploadIdForOperationOutcomes(), new ByteArrayInputStream(partitionSummaryData.getBufferStreamForImport().toByteArray()),
                            partitionSummaryData.getBufferStreamForImport().size(), partitionSummaryData.getPartNumForOperationOutcomes()));
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("pushImportOperationOutcomes2COS: " + partitionSummaryData.getBufferStreamForImport().size()
                            + " bytes were successfully appended to COS object - " + partitionSummaryData.getUniqueIDForImportOperationOutcomes());
                    }
                    partitionSummaryData.setPartNumForOperationOutcomes(partitionSummaryData.getPartNumForOperationOutcomes() + 1);
                    partitionSummaryData.getBufferStreamForImport().reset();
                }
                // Finish uploading OperationOutcomes.
                if (partitionSummaryData.getUploadIdForOperationOutcomes() != null) {
                    BulkDataUtils.finishMultiPartUpload(cosClient, cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportOperationOutcomes(),
                            partitionSummaryData.getUploadIdForOperationOutcomes(), partitionSummaryData.getDataPacksForOperationOutcomes());
                }

                // Upload remaining failure OperationOutcomes.
                if (partitionSummaryData.getBufferStreamForImportError().size() > 0) {
                    if (partitionSummaryData.getUploadIdForFailureOperationOutcomes()  == null) {
                        partitionSummaryData.setUploadIdForFailureOperationOutcomes(BulkDataUtils.startPartUpload(cosClient,
                                cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes(), true));
                    }

                    partitionSummaryData.getDataPacksForFailureOperationOutcomes().add(BulkDataUtils.multiPartUpload(cosClient,
                            cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes(),
                            partitionSummaryData.getUploadIdForFailureOperationOutcomes(), new ByteArrayInputStream(partitionSummaryData.getBufferStreamForImportError().toByteArray()),
                            partitionSummaryData.getBufferStreamForImportError().size(), partitionSummaryData.getPartNumForFailureOperationOutcomes()));
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("pushImportOperationOutcomes2COS: " + partitionSummaryData.getBufferStreamForImportError().size()
                            + " bytes were successfully appended to COS object - " + partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes());
                    }
                    partitionSummaryData.setPartNumForFailureOperationOutcomes(partitionSummaryData.getPartNumForFailureOperationOutcomes() + 1);
                    partitionSummaryData.getBufferStreamForImportError().reset();
                }
                // Finish uploading failure OperationOutcomes.
                if (partitionSummaryData.getUploadIdForFailureOperationOutcomes() != null) {
                    BulkDataUtils.finishMultiPartUpload(cosClient, cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes(),
                            partitionSummaryData.getUploadIdForFailureOperationOutcomes(), partitionSummaryData.getDataPacksForFailureOperationOutcomes());
                }
            }

            // Clean up.
            BulkDataUtils.cleanupTransientUserData(partitionSummaryData, false);
        }

        ImportCheckPointData partitionSummaryForMetrics = ImportCheckPointData.fromImportTransientUserData(partitionSummaryData);
        partitionSummaryForMetrics.setNumOfToBeImported(partitionSummaryData.getNumOfToBeImported());
        return partitionSummaryForMetrics;
    }

}
