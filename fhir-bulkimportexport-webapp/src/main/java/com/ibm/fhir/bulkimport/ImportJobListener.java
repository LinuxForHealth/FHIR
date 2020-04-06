/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.batch.api.listener.JobListener;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

public class ImportJobListener implements JobListener {
    private static final Logger logger = Logger.getLogger(ImportJobListener.class.getName());

    long currentExecutionStartTimeInMS;

    @Inject
    JobContext jobContext;

    public ImportJobListener() {

    }


    @Override
    public void afterJob() {
        long currentExecutionEndTimeInMS = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

        // Used for generating response for all the import data resources.
        @SuppressWarnings("unchecked")
        List<ImportCheckPointData> partitionSummaries = (List<ImportCheckPointData>)jobContext.getTransientUserData();
        // Used for generating performance measurement per each resource type.
        HashMap<String, ImportCheckPointData> importedResourceTypeSummaries = new HashMap<>();

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long totalJobExecutionMilliSeconds = 0;
        // The job can be started, stopped and then started again, so we need to add them all to get the whole job execution duration.
        for ( JobExecution jobExecution: jobOperator.getJobExecutions(jobOperator.getJobInstance(jobContext.getExecutionId()))) {
            // For current execution, jobExecution.getEndTime() is either null or with wrong value because the current execution is not
            // finished yet, so always use system time for both job execution start time and end time.
            if (jobExecution.getExecutionId()  == jobContext.getExecutionId()) {
                totalJobExecutionMilliSeconds += (currentExecutionEndTimeInMS - currentExecutionStartTimeInMS);
            } else {
                totalJobExecutionMilliSeconds += (jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
            }
        }

        // If the job is stopped before any partition is finished, then nothing to show.
        if (partitionSummaries == null) {
            return;
        }

        for (ImportCheckPointData partitionSummary : partitionSummaries) {
            ImportCheckPointData partitionSummaryInMap = importedResourceTypeSummaries.get(partitionSummary.getImportPartitionResourceType());
            if (partitionSummaryInMap == null) {
                importedResourceTypeSummaries.put(partitionSummary.getImportPartitionResourceType(), partitionSummary);
            } else {
                partitionSummaryInMap.setNumOfImportFailures(partitionSummaryInMap.getNumOfImportFailures() + partitionSummary.getNumOfImportFailures());
                partitionSummaryInMap.setNumOfImportedResources(partitionSummaryInMap.getNumOfImportedResources() + partitionSummary.getNumOfImportedResources());
                partitionSummaryInMap.setNumOfProcessedResources(partitionSummaryInMap.getNumOfProcessedResources() + partitionSummary.getNumOfProcessedResources());
                partitionSummaryInMap.setTotalReadMilliSeconds(partitionSummaryInMap.getTotalReadMilliSeconds() + partitionSummary.getTotalReadMilliSeconds());
                partitionSummaryInMap.setTotalValidationMilliSeconds(partitionSummaryInMap.getTotalValidationMilliSeconds() + partitionSummary.getTotalValidationMilliSeconds());
                partitionSummaryInMap.setTotalWriteMilliSeconds(partitionSummaryInMap.getTotalWriteMilliSeconds() + partitionSummary.getTotalWriteMilliSeconds());
                partitionSummaryInMap.setImportFileSize(partitionSummaryInMap.getImportFileSize() + partitionSummary.getImportFileSize());
            }
        }

        double jobProcessingSeconds = totalJobExecutionMilliSeconds / 1000.0;
        jobProcessingSeconds = jobProcessingSeconds < 1 ? 1.0 : jobProcessingSeconds;

        // log the simple metrics.
        logger.info(" ---- Fhir resources imported in " + jobProcessingSeconds + "seconds ----");
        logger.info("ResourceType \t| Imported \t| Failed \t| TotalReadMilliSeconds \t| TotalWriteMilliSeconds \t| TotalValidationMilliSeconds"
                    + " \t| TotalSize \t| AverageSize");
        int totalImportedFhirResources = 0;
        for (ImportCheckPointData importedResourceTypeSummary : importedResourceTypeSummaries.values()) {
            logger.info(importedResourceTypeSummary.getImportPartitionResourceType() + "\t|"
                        + importedResourceTypeSummary.getNumOfImportedResources() + "\t|"
                        + importedResourceTypeSummary.getNumOfImportFailures() + "\t|"
                        + importedResourceTypeSummary.getTotalReadMilliSeconds() + "\t|"
                        + importedResourceTypeSummary.getTotalWriteMilliSeconds() + "\t|"
                        + importedResourceTypeSummary.getTotalValidationMilliSeconds() + "\t|"
                        + importedResourceTypeSummary.getImportFileSize() + "\t|"
                        + importedResourceTypeSummary.getImportFileSize()/(importedResourceTypeSummary.getNumOfImportedResources() + importedResourceTypeSummary.getNumOfImportFailures()));
            totalImportedFhirResources += importedResourceTypeSummary.getNumOfImportedResources();
        }
        logger.info(" ---- Total: " + totalImportedFhirResources
                + " ImportRate: " + new DecimalFormat("#0.00").format(totalImportedFhirResources/jobProcessingSeconds) + " ----");
    }

    @Override
    public void beforeJob() {
        currentExecutionStartTimeInMS = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

}
