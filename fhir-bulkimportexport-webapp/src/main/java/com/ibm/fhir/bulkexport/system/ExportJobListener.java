/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.listener.JobListener;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.bulkexport.common.CheckPointUserData;

@Dependent
public class ExportJobListener implements JobListener {
    private static final Logger logger = Logger.getLogger(ExportJobListener.class.getName());

    long currentExecutionStartTimeInMS;

    @Inject
    JobContext jobContext;

    @Inject
    @BatchProperty(name = Constants.IMPORT_FHIR_DATASOURCES)
    String dataSourcesInfo;

    public ExportJobListener() {
        // do nothing
    }


    @Override
    public void afterJob() {
        long currentExecutionEndTimeInMS = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

        // Used for generating response for all the import data resources.
        @SuppressWarnings("unchecked")
        List<CheckPointUserData> partitionSummaries = (List<CheckPointUserData>)jobContext.getTransientUserData();

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

        double jobProcessingSeconds = totalJobExecutionMilliSeconds / 1000.0;
        jobProcessingSeconds = jobProcessingSeconds < 1 ? 1.0 : jobProcessingSeconds;

        // log the simple metrics.
        logger.info(" ---- Fhir resources exported in " + jobProcessingSeconds + "seconds ----");
        logger.info("ResourceType \t| Exported");
        int totalExportedFhirResources = 0;
        List<String> resourceTypeSummaries = new ArrayList<>();
        for (CheckPointUserData partitionSummary : partitionSummaries) {
            logger.info(partitionSummary.getResourceTypeSummary() + "\t|"
                  + partitionSummary.getTotalResourcesNum());
            resourceTypeSummaries.add(partitionSummary.getResourceTypeSummary());
            totalExportedFhirResources += partitionSummary.getTotalResourcesNum();
        }

        logger.info(" ---- Total: " + totalExportedFhirResources
                + " ExportRate: " + new DecimalFormat("#0.00").format(totalExportedFhirResources/jobProcessingSeconds) + " ----");

        if (resourceTypeSummaries.size() > 0) {
            // e.g, Patient[1000,1000,200]:Observation[1000,250]
            jobContext.setExitStatus(String.join(":", resourceTypeSummaries));
        }
    }

    @Override
    public void beforeJob() {
        currentExecutionStartTimeInMS = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

}
