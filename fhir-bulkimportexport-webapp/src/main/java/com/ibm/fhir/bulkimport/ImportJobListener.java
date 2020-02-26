/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.util.HashMap;
import java.util.List;

import javax.batch.api.listener.JobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

public class ImportJobListener implements JobListener {
    @Inject
    JobContext jobContext;

    private long jobStartTimeInMS, jobEndTimeInMS;

    public ImportJobListener() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterJob() {
        // Used for generating response for all the import data resources.
        List<ImportCheckPointData> partitionSummaries = (List<ImportCheckPointData>)jobContext.getTransientUserData();
        // Used for generating performance measurement per each resource type.
        HashMap<String, ImportCheckPointData> importedResourceTypeSummaries = new HashMap<>();

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

        jobEndTimeInMS = System.currentTimeMillis();
        double jobProcessingSeconds = (jobEndTimeInMS - jobStartTimeInMS)/1000.0;
        jobProcessingSeconds = jobProcessingSeconds < 1 ? 1.0 : jobProcessingSeconds;

        // Print out the simple metrics to console.
        System.out.println(" ---- Fhir resources imported in " + jobProcessingSeconds + "seconds ----");
        System.out.println("ResourceType \t Imported \t Failed");
        int totalImportedFhirResources = 0;
        for (ImportCheckPointData importedResourceTypeSummary : importedResourceTypeSummaries.values()) {
            System.out.println(importedResourceTypeSummary.getImportPartitionResourceType() + "\t" +
                        importedResourceTypeSummary.getNumOfImportedResources() + "\t" + importedResourceTypeSummary.getNumOfImportFailures());
            totalImportedFhirResources += importedResourceTypeSummary.getNumOfImportedResources();
        }
        System.out.println(" ---- Total: " + totalImportedFhirResources
                + " ImportRate: " + totalImportedFhirResources/jobProcessingSeconds + " ----");
    }

    @Override
    public void beforeJob() {
        jobStartTimeInMS = System.currentTimeMillis();
    }

}
