/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.fast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.listener.JobListener;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.jbatch.export.fast.data.PartitionSummary;

@Dependent
public class ExportJobListener implements JobListener {
    private static final Logger logger = Logger.getLogger(ExportJobListener.class.getName());

    @Inject
    JobContext jobContext;

    public ExportJobListener() {
        // No Operation
    }

    @Override
    public void afterJob() {

        // This list of partition summaries is compiled by the ExportPartitionAnalyzer and
        // injected into the jobContext as transient user data
        @SuppressWarnings("unchecked")
        List<PartitionSummary> partitionSummaries = (List<PartitionSummary>)jobContext.getTransientUserData();

        // If the job is stopped before any partition is finished, then nothing to show.
        if (partitionSummaries == null) {
            return;
        }

        int totalResourceCount = 0;
        List<String> resourceTypeSummaries = new ArrayList<>();
        for (PartitionSummary partitionSummary : partitionSummaries) {
            // If we always send back 140+ resources we get a bit cramped on the ExitStatus.
            // @see https://github.com/LinuxForHealth/FHIR/issues/2018
            int partitionSum = partitionSummary.getResourceCounts().stream().reduce(0, Integer::sum);
            if (partitionSum > 0) {
                totalResourceCount += partitionSum;
                resourceTypeSummaries.add(partitionSummary.getResourceTypeSummary());
            }

            logger.info(String.format("%s %32s %10d", logPrefix(), partitionSummary.getResourceType(), partitionSum));
        }

        logger.info(String.format("%s %32s %10s", logPrefix(), "--------------------------------", "----------"));
        logger.info(String.format("%s %32s %10d", logPrefix(), "TOTAL", totalResourceCount));

        if (resourceTypeSummaries.size() > 0) {
            // e.g, Patient[1000,1000,200]:Observation[1000,250]
            String status = String.join(":", resourceTypeSummaries);
            logger.fine(logPrefix() + " Setting jobContext exit status: '" + status + "'");
            jobContext.setExitStatus(status);
        }
    }

    @Override
    public void beforeJob() {
        // NOP
    }

    /**
     * Prefix for log messages to identify the job
     * @return
     */
    private String logPrefix() {
        return jobContext.getJobName() + "[" + jobContext.getExecutionId() + "]";
    }
}