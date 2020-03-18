/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

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
    private TimeUnit time = TimeUnit.NANOSECONDS;
    long currentExecutionStartTimeInMS;

    @Inject
    JobContext jobContext;

    public ImportJobListener() {

    }


    @Override
    public void afterJob() {
        // jobExecution.getEndTime() for current execution always returns null, so we use system current time as the end time for current execution.
        long currentExecutionEndTimeInMS = time.toMillis(System.nanoTime());

        // Used for generating response for all the import data resources.
        @SuppressWarnings("unchecked")
        List<ImportCheckPointData> partitionSummaries = (List<ImportCheckPointData>)jobContext.getTransientUserData();
        // Used for generating performance measurement per each resource type.
        HashMap<String, ImportCheckPointData> importedResourceTypeSummaries = new HashMap<>();

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long totalJobExecutionMilliSeconds = 0;
        for ( JobExecution jobExecution: jobOperator.getJobExecutions(jobOperator.getJobInstance(jobContext.getExecutionId()))) {
            if (jobExecution.getEndTime() != null) {
                totalJobExecutionMilliSeconds += (jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
            } else {
                totalJobExecutionMilliSeconds += (currentExecutionEndTimeInMS - currentExecutionStartTimeInMS);
            }
        }

        // If the job is stopped before any partition is finished, then nothing to show.
        if (partitionSummaries == null) {
            return;
        }

        for (ImportCheckPointData partitionSummary : partitionSummaries) {
            ImportCheckPointData partitionSummaryInMap = importedResourceTypeSummaries.get(partitionSummary.getImportPartitionResourceType());
            if (partitionSummaryInMap == null) {
                ImportCheckPointData importedResourceTypeSummary = new ImportCheckPointData(partitionSummary.getNumOfProcessedResources(),
                        partitionSummary.getNumOfImportedResources(), partitionSummary.getNumOfImportFailures(), partitionSummary.getImportPartitionResourceType());
                importedResourceTypeSummaries.put(partitionSummary.getImportPartitionResourceType(), importedResourceTypeSummary);
            } else {
                partitionSummaryInMap.setNumOfImportFailures(partitionSummaryInMap.getNumOfImportFailures() + partitionSummary.getNumOfImportFailures());
                partitionSummaryInMap.setNumOfImportedResources(partitionSummaryInMap.getNumOfImportedResources() + partitionSummary.getNumOfImportedResources());
                partitionSummaryInMap.setNumOfProcessedResources(partitionSummaryInMap.getNumOfProcessedResources() + partitionSummary.getNumOfProcessedResources());
            }
        }


        double jobProcessingSeconds = (totalJobExecutionMilliSeconds)/1000.0;
        jobProcessingSeconds = jobProcessingSeconds < 1 ? 1.0 : jobProcessingSeconds;

        // log the simple metrics.
        logger.info(" ---- Fhir resources imported in " + jobProcessingSeconds + "seconds ----");
        logger.info("ResourceType \t Imported \t Failed");
        int totalImportedFhirResources = 0;
        for (ImportCheckPointData importedResourceTypeSummary : importedResourceTypeSummaries.values()) {
            logger.info(importedResourceTypeSummary.getImportPartitionResourceType() + "\t" +
                        importedResourceTypeSummary.getNumOfImportedResources() + "\t" + importedResourceTypeSummary.getNumOfImportFailures());
            totalImportedFhirResources += importedResourceTypeSummary.getNumOfImportedResources();
        }
        logger.info(" ---- Total: " + totalImportedFhirResources
                + " ImportRate: " + totalImportedFhirResources/jobProcessingSeconds + " ----");
    }

    @Override
    public void beforeJob() {
        currentExecutionStartTimeInMS = time.toMillis(System.nanoTime());
    }

}
