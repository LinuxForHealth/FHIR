/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.jbatch.load.listener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;

import com.ibm.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;

/**
 * Generates the Report for the Import Job
 */
public class Reporter {
    private static final Logger logger = Logger.getLogger(Reporter.class.getName());

    private long currentExecutionStartTimeInMS;
    private long currentExecutionEndTimeInMS;
    private long totalJobExecutionMilliSeconds = 0;

    public Reporter() {
        // No Operation
    }

    public void start() {
        currentExecutionStartTimeInMS = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    public void end() {
        currentExecutionEndTimeInMS = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    public void calculateTotal(JobContext jobContext, List<JobExecution> jobExecutions) {
        // The job can be started, stopped and then started again, so we need to add them all to get the whole job execution duration.
        for ( JobExecution jobExecution: jobExecutions) {
            // For current execution, jobExecution.getEndTime() is either null or with wrong value because the current execution is not
            // finished yet, so always use system time for both job execution start time and end time.
            if (jobExecution.getExecutionId()  == jobContext.getExecutionId()) {
                totalJobExecutionMilliSeconds += (currentExecutionEndTimeInMS - currentExecutionStartTimeInMS);
            } else {
                totalJobExecutionMilliSeconds += (jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
            }
        }
    }

    public void report(List<ImportCheckPointData> partitionSummaries) {

        // Used for generating performance measurement per each resource type.
        Map<String, ImportCheckPointData> importedResourceTypeSummaries = new HashMap<>();

        for (ImportCheckPointData partitionSummary : partitionSummaries) {
            ImportCheckPointData partitionSummaryInMap = importedResourceTypeSummaries.get(partitionSummary.getImportPartitionResourceType());
            if (partitionSummaryInMap == null) {
                importedResourceTypeSummaries.put(partitionSummary.getImportPartitionResourceType(), partitionSummary);
            } else {
                partitionSummaryInMap.setNumOfImportFailures(partitionSummaryInMap.getNumOfImportFailures() + partitionSummary.getNumOfImportFailures());
                partitionSummaryInMap.setNumOfImportedResources(partitionSummaryInMap.getNumOfImportedResources() + partitionSummary.getNumOfImportedResources());
                partitionSummaryInMap.setNumOfProcessedResources(partitionSummaryInMap.getNumOfProcessedResources() + partitionSummary.getNumOfProcessedResources());
                partitionSummaryInMap.setNumOfSkippedResources(partitionSummaryInMap.getNumOfSkippedResources() + partitionSummary.getNumOfSkippedResources());
                partitionSummaryInMap.setTotalReadMilliSeconds(partitionSummaryInMap.getTotalReadMilliSeconds() + partitionSummary.getTotalReadMilliSeconds());
                partitionSummaryInMap.setTotalValidationMilliSeconds(partitionSummaryInMap.getTotalValidationMilliSeconds() + partitionSummary.getTotalValidationMilliSeconds());
                partitionSummaryInMap.setTotalWriteMilliSeconds(partitionSummaryInMap.getTotalWriteMilliSeconds() + partitionSummary.getTotalWriteMilliSeconds());
                partitionSummaryInMap.setImportFileSize(partitionSummaryInMap.getImportFileSize() + partitionSummary.getImportFileSize());
            }
        }

        double jobProcessingSeconds = totalJobExecutionMilliSeconds / 1000.0;
        jobProcessingSeconds = jobProcessingSeconds < 1 ? 1.0 : jobProcessingSeconds;

        // log the simple metrics.
        logger.info("Operation Type: $import");
        logger.info(String.format("%-32s%-32s%-32s%-32s%-32s%-32s%-32s%-32s%-32s%-32s",
            "Resource Type","failures (R)", "success (R)", "processed (R)", "totalRead (ms)", "totalSkip (R)", "totalValidation (ms)", "totalWrite (ms)", "fileSize (bytes)", "Resource Size (bytes/resource)"));
        long totalImportedFhirResources = 0;
        for (ImportCheckPointData importedResourceTypeSummary : importedResourceTypeSummaries.values()) {
            String resourceType = importedResourceTypeSummary.getImportPartitionResourceType();
            long failures = importedResourceTypeSummary.getNumOfImportFailures();
            long success = importedResourceTypeSummary.getNumOfImportedResources();
            long totalRead = importedResourceTypeSummary.getTotalReadMilliSeconds();
            long totalWrite = importedResourceTypeSummary.getTotalWriteMilliSeconds();
            long totalSkip = importedResourceTypeSummary.getNumOfSkippedResources();
            long totalValidation = importedResourceTypeSummary.getTotalValidationMilliSeconds();
            long processed = importedResourceTypeSummary.getNumOfImportedResources();
            long fileSize = importedResourceTypeSummary.getImportFileSize();

            totalImportedFhirResources += success;

            double resourceSize = -1.0;
            if (success + failures > 0) {
                resourceSize = fileSize / (success + failures);
            } else {
                logger.fine("No Content was imported");
            }
            logger.info(String.format("%-32s%-32s%-32s%-32s%-32s%-32s%-32s%-32s%-32s%-32s",
               resourceType,failures, success, processed, totalRead, totalSkip, totalValidation, totalWrite, fileSize, resourceSize));
        }
        logger.info(" ---- Total: " + totalImportedFhirResources
                + " ImportRate: " + new DecimalFormat("#0.00").format(totalImportedFhirResources/jobProcessingSeconds) + " ----");
    }
}