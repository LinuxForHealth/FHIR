/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.load;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.listener.JobListener;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import jakarta.json.JsonArray;

import org.linuxforhealth.fhir.bulkdata.common.BulkDataUtils;
import org.linuxforhealth.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.listener.ExitStatus;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.listener.Reporter;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataContext;

@Dependent
public class ImportJobListener implements JobListener {

    private static final Logger logger = Logger.getLogger(ImportJobListener.class.getName());

    private Reporter reporter = new Reporter();

    @Inject
    JobContext jobCtx;

    public ImportJobListener() {
        // No Operation
    }

    @Override
    public void beforeJob() {
        // No try-catch logging here, since it's a simple method.
        reporter.start();
    }

    @Override
    public void afterJob() {
        if (BatchStatus.FAILED.equals(jobCtx.getBatchStatus())) {
            return;
        }

        long executionId = -1;
        try {
            executionId = jobCtx.getExecutionId();
            JobOperator jobOperator = BatchRuntime.getJobOperator();
            JobExecution jobExecution = jobOperator.getJobExecution(executionId);

            BatchContextAdapter ctxAdapter = new BatchContextAdapter(jobExecution.getJobParameters());

            BulkDataContext ctx = ctxAdapter.getStepContextForPartitionMapperForImport();

            reporter.end();

            reporter.calculateTotal(jobCtx, jobOperator.getJobExecutions(jobOperator.getJobInstance(jobCtx.getExecutionId())));

            // Used for generating response for all the import data resources.
            @SuppressWarnings("unchecked")
            List<ImportCheckPointData> partitionSummaries = (List<ImportCheckPointData>) jobCtx.getTransientUserData();

            // If the job is stopped before any partition is finished, then nothing to show.
            if (partitionSummaries == null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Partition Summaries are null");
                }
                return;
            }

            // Generate import summary and pass it into ExitStatus of the job execution.
            // e.g, [3:0, 4:1] means 3 resources imported and 0 failures for the 1st file, and 4 imported and 1 failure
            // for
            // the 2nd file.
            JsonArray dataSourceArray = BulkDataUtils.getDataSourcesFromJobInput(ctx.getDataSourcesInfo());
            ExitStatus exitStatus = new ExitStatus(dataSourceArray, partitionSummaries);
            jobCtx.setExitStatus(exitStatus.generateResultExitStatus());

            reporter.report(partitionSummaries);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Import PartitionListener.afterJob job[" + executionId + "]", e);
            throw e;
        }
    }
}
