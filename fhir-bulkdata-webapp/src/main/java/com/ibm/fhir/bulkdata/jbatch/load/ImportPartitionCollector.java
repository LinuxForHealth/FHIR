/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionCollector;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.bulkdata.provider.ProviderFactory;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

@Dependent
public class ImportPartitionCollector implements PartitionCollector {
    private static final Logger LOG = Logger.getLogger(ImportPartitionCollector.class.getName());

    private Provider provider = null;

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    public ImportPartitionCollector() {
        // The injected properties are not available at class construction time
        // These values are lazy injected BEFORE calling 'collectPartitionData'.
    }

    @Override
    public Serializable collectPartitionData() throws Exception {
        long executionId = -1;
        try {
            executionId = jobCtx.getExecutionId();
            JobExecution jobExecution = BatchRuntime.getJobOperator().getJobExecution(executionId);

            BatchContextAdapter ctxAdapter = new BatchContextAdapter(jobExecution.getJobParameters());

            BulkDataContext ctx = ctxAdapter.getStepContextForImportPartitionCollector();

            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

            StorageType type = adapter.getStorageProviderStorageType(ctx.getOutcome());
            boolean collectImportOperationOutcomes = adapter.shouldStorageProviderCollectOperationOutcomes(ctx.getSource())
                    && !StorageType.HTTPS.equals(type);

            String cosOperationOutcomesBucketName = adapter.getStorageProviderBucketName(ctx.getOutcome());
            if (collectImportOperationOutcomes) {
                provider = ProviderFactory.getSourceWrapper(ctx.getOutcome(),
                        adapter.getStorageProviderType(ctx.getOutcome()));
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
            // (1) upload the remaining OperationOutcomes to COS/S3.
            // (2) finish the multiple-parts uploads.
            // (3) release the resources hold by this partition.
            if (partitionSummaryData.getNumOfToBeImported() == 0) {
                if (collectImportOperationOutcomes) {
                    // Upload remaining OperationOutcomes.
                    if (partitionSummaryData.getBufferStreamForImport().size() > 0) {
                        if (partitionSummaryData.getUploadIdForOperationOutcomes() == null) {
                            partitionSummaryData.setUploadIdForOperationOutcomes(BulkDataUtils.startPartUpload(provider.getClient(),
                                    cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportOperationOutcomes()));
                        }

                        ByteArrayOutputStream baos = partitionSummaryData.getBufferStreamForImportError();

                        PartETag tag = BulkDataUtils.multiPartUpload(provider.getClient(),
                                cosOperationOutcomesBucketName,
                                partitionSummaryData.getUniqueIDForImportOperationOutcomes(),
                                partitionSummaryData.getUploadIdForOperationOutcomes(),
                                new ByteArrayInputStream(baos.toByteArray()), baos.size(),
                                partitionSummaryData.getPartNumForOperationOutcomes());
                        partitionSummaryData.getDataPacksForOperationOutcomes().add(tag);

                        if (LOG.isLoggable(Level.FINE)) {
                            LOG.fine("pushImportOperationOutcomesToCOS: " + partitionSummaryData.getBufferStreamForImport().size()
                                + " bytes were successfully appended to COS object - " + partitionSummaryData.getUniqueIDForImportOperationOutcomes());
                        }
                        partitionSummaryData.setPartNumForOperationOutcomes(partitionSummaryData.getPartNumForOperationOutcomes() + 1);
                        partitionSummaryData.getBufferStreamForImport().reset();
                    }
                    // Finish uploading OperationOutcomes.
                    if (partitionSummaryData.getUploadIdForOperationOutcomes() != null) {
                        BulkDataUtils.finishMultiPartUpload(provider.getClient(), cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportOperationOutcomes(),
                                partitionSummaryData.getUploadIdForOperationOutcomes(), partitionSummaryData.getDataPacksForOperationOutcomes());
                    }

                    // Upload remaining failure OperationOutcomes.
                    if (partitionSummaryData.getBufferStreamForImportError().size() > 0) {
                        if (partitionSummaryData.getUploadIdForFailureOperationOutcomes()  == null) {
                            partitionSummaryData.setUploadIdForFailureOperationOutcomes(BulkDataUtils.startPartUpload(provider.getClient(),
                                    cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes()));
                        }

                        partitionSummaryData.getDataPacksForFailureOperationOutcomes().add(BulkDataUtils.multiPartUpload(provider.getClient(),
                                cosOperationOutcomesBucketName,
                                partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes(),
                                partitionSummaryData.getUploadIdForFailureOperationOutcomes(),
                                new ByteArrayInputStream(partitionSummaryData.getBufferStreamForImportError().toByteArray()),
                                partitionSummaryData.getBufferStreamForImportError().size(),
                                partitionSummaryData.getPartNumForFailureOperationOutcomes()));
                        if (LOG.isLoggable(Level.FINE)) {
                            LOG.fine("pushImportOperationOutcomesToCOS: " + partitionSummaryData.getBufferStreamForImportError().size()
                                + " bytes were successfully appended to COS object - " + partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes());
                        }
                        partitionSummaryData.setPartNumForFailureOperationOutcomes(partitionSummaryData.getPartNumForFailureOperationOutcomes() + 1);
                        partitionSummaryData.getBufferStreamForImportError().reset();
                    }
                    // Finish uploading failure OperationOutcomes.
                    if (partitionSummaryData.getUploadIdForFailureOperationOutcomes() != null) {
                        BulkDataUtils.finishMultiPartUpload(provider.getClient(), cosOperationOutcomesBucketName, partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes(),
                                partitionSummaryData.getUploadIdForFailureOperationOutcomes(), partitionSummaryData.getDataPacksForFailureOperationOutcomes());
                    }
                }

                // Clean up.
                BulkDataUtils.cleanupTransientUserData(partitionSummaryData, false);
            }

            ImportCheckPointData partitionSummaryForMetrics = ImportCheckPointData.fromImportTransientUserData(partitionSummaryData);
            partitionSummaryForMetrics.setNumOfToBeImported(partitionSummaryData.getNumOfToBeImported());

            if (LOG.isLoggable(Level.FINE)) {
                LOG.info("collected partition data: " + partitionSummaryForMetrics);
            }
            return partitionSummaryForMetrics;
        }catch (FHIRException e) {
            LOG.log(Level.SEVERE, "Import PartitionCollector.collectPartitionData during job[" + executionId + "] - " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Import PartitionCollector.collectPartitionData during job[" + executionId + "]", e);
            throw e;
        }
    }
}