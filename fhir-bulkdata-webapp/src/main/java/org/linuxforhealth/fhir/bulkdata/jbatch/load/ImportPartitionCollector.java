/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.load;

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

import org.linuxforhealth.fhir.bulkdata.common.BulkDataUtils;
import org.linuxforhealth.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import org.linuxforhealth.fhir.bulkdata.provider.Provider;
import org.linuxforhealth.fhir.bulkdata.provider.ProviderFactory;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataContext;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageType;

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

            ImportTransientUserData partitionSummaryData = (ImportTransientUserData) stepCtx.getTransientUserData();
            BatchStatus batchStatus = stepCtx.getBatchStatus();

            // If the job is being stopped or in other status except for "started", then do
            // cleanup for the partition.
            if (!batchStatus.equals(BatchStatus.STARTED)) {
                BulkDataUtils.cleanupTransientUserData(partitionSummaryData, true);
                return null;
            }

            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            StorageType type = adapter.getStorageProviderStorageType(ctx.getOutcome());
            boolean collectImportOperationOutcomes = adapter.shouldStorageProviderCollectOperationOutcomes(ctx.getSource())
                    && !StorageType.HTTPS.equals(type);

            if (collectImportOperationOutcomes) {
                provider = ProviderFactory.getSourceWrapper(ctx.getOutcome(),
                        adapter.getStorageProviderType(ctx.getOutcome()));
            }

            // This function is called at partition chunk check points and also at the end of partition processing.
            // So, check the NumOfToBeImported to make sure at the end of partition processing:
            // (1) upload the remaining OperationOutcomes to the StorageProviders (!HTTPS).
            // (2) finish the uploads.
            // (3) release the resources held by this partition.
            if (partitionSummaryData.getNumOfToBeImported() == 0 && collectImportOperationOutcomes) {
                String folder = ctx.getCosBucketPathPrefix();

                // Success Messages
                ByteArrayOutputStream baosSuccess = partitionSummaryData.getBufferStreamForImport();
                String fileNameSuccess = partitionSummaryData.getUniqueIDForImportOperationOutcomes();
                provider.pushEndOfJobOperationOutcomes(baosSuccess, folder, fileNameSuccess);

                // Failure Messages
                ByteArrayOutputStream baosFailure = partitionSummaryData.getBufferStreamForImportError();
                String fileNameFailure = partitionSummaryData.getUniqueIDForImportFailureOperationOutcomes();
                provider.pushEndOfJobOperationOutcomes(baosFailure, folder, fileNameFailure);

                // Clean up
                BulkDataUtils.cleanupTransientUserData(partitionSummaryData, false);
            }

            ImportCheckPointData partitionSummaryForMetrics = ImportCheckPointData.fromImportTransientUserData(partitionSummaryData);
            partitionSummaryForMetrics.setNumOfToBeImported(partitionSummaryData.getNumOfToBeImported());

            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("collected partition data: " + partitionSummaryForMetrics);
            }
            return partitionSummaryForMetrics;
        }catch (FHIRException e) {
            LOG.throwing(ImportPartitionCollector.class.getName(), "collectPartitionData", e);
            LOG.log(Level.SEVERE, "Import PartitionCollector.collectPartitionData during job[" + executionId + "] - "
                    + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Import PartitionCollector.collectPartitionData during job[" + executionId + "]", e);
            throw e;
        }
    }
}