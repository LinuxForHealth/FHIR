/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

@Dependent
public class ImportPartitionAnalyzer implements PartitionAnalyzer {

    private static final Logger logger = Logger.getLogger(ImportPartitionAnalyzer.class.getName());

    private static final ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

    @Inject
    JobContext jobContext;

    @Inject
    JobContext jobCtx;

    private List<ImportCheckPointData> partitionSummaries = new ArrayList<>();

    // Used for generating in-fly performance measurement per each resource type.
    private HashMap<String, ImportCheckPointData> importedResourceTypeInFlySummaries = new HashMap<>();

    public ImportPartitionAnalyzer() {
        // No Operation
    }

    @Override
    public void analyzeStatus(BatchStatus batchStatus, String exitStatus) {
        // No Operation and No Try-Catch Logging
    }

    @Override
    public void analyzeCollectorData(Serializable data) {
        long executionId = -1;
        try {
            executionId = jobCtx.getExecutionId();

            if (data == null) {
                return;
            }
            ImportCheckPointData partitionSummaryForMetrics = (ImportCheckPointData) data;

            if (partitionSummaryForMetrics.getNumOfToBeImported() == 0) {
                partitionSummaries.add(partitionSummaryForMetrics);
                jobContext.setTransientUserData(partitionSummaries);
            }

            // Aggregate the processed resource numbers from different partitions for the same resource types.
            ImportCheckPointData importedResourceTypeInFlySummary =
                    importedResourceTypeInFlySummaries.get(partitionSummaryForMetrics.getImportPartitionResourceType());
            if (importedResourceTypeInFlySummary == null) {
                // Instantiate a partition summary for this resourceType and add it to the list
                importedResourceTypeInFlySummary =
                        ImportCheckPointData.Builder.builder()
                            .importPartitionResourceType(partitionSummaryForMetrics.getImportPartitionResourceType())
                            .numOfProcessedResources(adapter.getImportNumberOfFhirResourcesPerRead(null))
                            .inFlyRateBeginMilliSeconds(partitionSummaryForMetrics.getInFlyRateBeginMilliSeconds())
                            .build();

                importedResourceTypeInFlySummaries.put(partitionSummaryForMetrics.getImportPartitionResourceType(), importedResourceTypeInFlySummary);
            } else {
                // Add info to the object thats already in the list
                importedResourceTypeInFlySummary.setNumOfProcessedResources(importedResourceTypeInFlySummary.getNumOfProcessedResources()
                        + adapter.getImportNumberOfFhirResourcesPerRead(null));

                if (importedResourceTypeInFlySummary.getNumOfProcessedResources() % adapter.getImportInflyRateNumberOfFhirResources(null) == 0) {
                    long currentTimeMilliSeconds = System.currentTimeMillis();
                    double jobProcessingSeconds = (currentTimeMilliSeconds - importedResourceTypeInFlySummary.getInFlyRateBeginMilliSeconds()) / 1000.0;
                    jobProcessingSeconds = jobProcessingSeconds < 1 ? 1.0 : jobProcessingSeconds;

                    // log the in-fly rate.
                    logger.info(adapter.getImportInflyRateNumberOfFhirResources(null) + " " + importedResourceTypeInFlySummary.getImportPartitionResourceType()
                            + " resources imported in " + jobProcessingSeconds + " seconds, ImportRate: "
                            + new DecimalFormat("#0.00").format(adapter.getImportInflyRateNumberOfFhirResources(null) / jobProcessingSeconds) + "/Second");

                    importedResourceTypeInFlySummary.setInFlyRateBeginMilliSeconds(currentTimeMilliSeconds);
                }
            }
        } catch (Exception e) {
            // This exception is a warning, not an Error, it doesn't interrupt anything.
            logger.log(Level.WARNING, "Exception hit in the Import Partition Analyzer Job [" + executionId + "]", e);
            throw e;
        }
    }
}