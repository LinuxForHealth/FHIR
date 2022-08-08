/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.load;

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

import org.linuxforhealth.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;

@Dependent
public class ImportPartitionAnalyzer implements PartitionAnalyzer {

    private static final Logger logger = Logger.getLogger(ImportPartitionAnalyzer.class.getName());

    private static final ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
    private static final DecimalFormat FORMAT = new DecimalFormat("#0.000");

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
                // Note we're caching here in case there are more than 1 executions of the job.
                // (for instance a recoverable failure of the job during execution).
                importedResourceTypeInFlySummary =
                        ImportCheckPointData.Builder.builder()
                            .importPartitionResourceType(partitionSummaryForMetrics.getImportPartitionResourceType())
                            .importPartitionWorkitem(partitionSummaryForMetrics.getImportPartitionWorkitem())
                            .numOfProcessedResources(partitionSummaryForMetrics.getNumOfProcessedResources())
                            .inFlyRateBeginMilliSeconds(partitionSummaryForMetrics.getInFlyRateBeginMilliSeconds())
                            .build();

                importedResourceTypeInFlySummaries.put(partitionSummaryForMetrics.getImportPartitionResourceType(), importedResourceTypeInFlySummary);
            } else {
                // Add current summary to the local cache.
                importedResourceTypeInFlySummary.addToNumOfProcessedResources(partitionSummaryForMetrics.getNumOfToBeImported());
                if ((importedResourceTypeInFlySummary.getNumOfProcessedResources() - importedResourceTypeInFlySummary.getLastChecked()) > adapter.getImportInflyRateNumberOfFhirResources(null)) {
                    long currentTimeMilliSeconds = System.currentTimeMillis();
                    long time = currentTimeMilliSeconds - importedResourceTypeInFlySummary.getInFlyRateBeginMilliSeconds();
                    double rate = (importedResourceTypeInFlySummary.getNumOfProcessedResources() - importedResourceTypeInFlySummary.getLastChecked()) / (1.0 * time);

                    // log the in-fly rate.
                    logger.info("Import in-fly rate: [" + jobCtx.getInstanceId() + "/" + jobCtx.getExecutionId() + "/"
                            + importedResourceTypeInFlySummary.getImportPartitionWorkitem() + "/" + importedResourceTypeInFlySummary.getImportPartitionResourceType()
                            + "] reportingThreshold=[" + adapter.getImportInflyRateNumberOfFhirResources(null)
                            + "] resources=[" + (importedResourceTypeInFlySummary.getNumOfProcessedResources() - importedResourceTypeInFlySummary.getLastChecked())
                            + "] imported in " + time + " milliseconds, ImportRate: [" + FORMAT.format(rate) + "] Resources/milliseconds");

                    importedResourceTypeInFlySummary.setInFlyRateBeginMilliSeconds(currentTimeMilliSeconds);
                    importedResourceTypeInFlySummary.setLastChecked(importedResourceTypeInFlySummary.getNumOfProcessedResources());
                }
            }
        } catch (Exception e) {
            // This exception is a warning, not an Error, it doesn't interrupt anything.
            logger.log(Level.WARNING, "Exception hit in the Import Partition Analyzer Job [" + executionId + "]", e);
            throw e;
        }
    }
}