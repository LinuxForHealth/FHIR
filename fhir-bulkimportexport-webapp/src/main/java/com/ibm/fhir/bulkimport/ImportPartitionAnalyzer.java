/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkcommon.Constants;

@Dependent
public class ImportPartitionAnalyzer implements PartitionAnalyzer {
    private static final Logger logger = Logger.getLogger(ImportPartitionAnalyzer.class.getName());
    @Inject
    JobContext jobContext;

    private List<ImportCheckPointData> partitionSummaries = new ArrayList<>();
    // Used for generating in-fly performance measurement per each resource type.
    private HashMap<String, ImportCheckPointData> importedResourceTypeInFlySummaries = new HashMap<>();

    public ImportPartitionAnalyzer() {
    }

    @Override
    public void analyzeStatus(BatchStatus batchStatus, String exitStatus) {

    }

    @Override
    public void analyzeCollectorData(Serializable data) {
        if (data == null) {
            return;
        }
        ImportCheckPointData partitionSummaryForMetrics  = (ImportCheckPointData) data;

        if (partitionSummaryForMetrics.getNumOfToBeImported() == 0) {
            partitionSummaries.add(partitionSummaryForMetrics);
            jobContext.setTransientUserData(partitionSummaries);
        }


        // Aggregate the processed resource numbers from different partitions for the same resource types.
        ImportCheckPointData importedResourceTypeInFlySummary = importedResourceTypeInFlySummaries.get(partitionSummaryForMetrics.getImportPartitionResourceType());
        if (importedResourceTypeInFlySummary == null) {
            importedResourceTypeInFlySummaries.put(partitionSummaryForMetrics.getImportPartitionResourceType(),
                    new ImportCheckPointData(partitionSummaryForMetrics.getImportPartitionResourceType(), Constants.IMPORT_NUMOFFHIRRESOURCES_PERREAD, partitionSummaryForMetrics.getInFlyRateBeginMilliSeconds()));
        } else {
            importedResourceTypeInFlySummary.setNumOfProcessedResources(importedResourceTypeInFlySummary.getNumOfProcessedResources() + Constants.IMPORT_NUMOFFHIRRESOURCES_PERREAD);

            if (importedResourceTypeInFlySummary.getNumOfProcessedResources() % Constants.IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES == 0) {
                long currentTimeMilliSeconds = System.currentTimeMillis();
                double jobProcessingSeconds = (currentTimeMilliSeconds - importedResourceTypeInFlySummary.getInFlyRateBeginMilliSeconds()) / 1000.0;
                jobProcessingSeconds = jobProcessingSeconds < 1 ? 1.0 : jobProcessingSeconds;

                // log the in-fly rate.
                logger.info(Constants.IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES + " " + importedResourceTypeInFlySummary.getImportPartitionResourceType()
                    + " resources imported in " + jobProcessingSeconds + " seconds, ImportRate: "
                    + new DecimalFormat("#0.00").format(Constants.IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES/jobProcessingSeconds) + "/Second");

                importedResourceTypeInFlySummary.setInFlyRateBeginMilliSeconds(currentTimeMilliSeconds);
            }
        }

    }
}
