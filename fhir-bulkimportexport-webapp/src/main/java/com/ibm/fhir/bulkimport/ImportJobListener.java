/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;

@Dependent
public class ImportJobListener implements JobListener {
    private static final Logger logger = Logger.getLogger(ImportJobListener.class.getName());

    long currentExecutionStartTimeInMS;

    @Inject
    JobContext jobContext;

    @Inject
    @BatchProperty(name = Constants.IMPORT_FHIR_DATASOURCES)
    String dataSourcesInfo;

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

        // Generate import summary and pass it into ExitStatus of the job execution.
        // e.g, [3:0, 4:1] means 3 resources imported and 0 failures for the 1st file, and 4 imported and 1 failure for the 2nd file.
        JsonArray dataSourceArray = BulkDataUtils.getDataSourcesFromJobInput(dataSourcesInfo);

        Map<String, Integer> inputUrlSequenceMap = new HashMap<>();
        int sequnceNum = 0;
        for (JsonValue jsonValue : dataSourceArray) {
            JsonObject dataSourceInfo = jsonValue.asJsonObject();
            String DSTypeInfo = dataSourceInfo.getString(Constants.IMPORT_INPUT_RESOURCE_TYPE);
            String DSDataLocationInfo = dataSourceInfo.getString(Constants.IMPORT_INPUT_RESOURCE_URL);
            inputUrlSequenceMap.put(DSTypeInfo + ":" + DSDataLocationInfo, sequnceNum++);
        }

        String resultInExitStatus[] = new String[sequnceNum];
        for (ImportCheckPointData partitionSummary : partitionSummaries) {
            int index = inputUrlSequenceMap.get(partitionSummary.getImportPartitionResourceType() + ":" + partitionSummary.getImportPartitionWorkitem());
            resultInExitStatus[index] = partitionSummary.getNumOfImportedResources() + ":" + partitionSummary.getNumOfImportFailures();
        }

        jobContext.setExitStatus(Arrays.toString(resultInExitStatus));

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
