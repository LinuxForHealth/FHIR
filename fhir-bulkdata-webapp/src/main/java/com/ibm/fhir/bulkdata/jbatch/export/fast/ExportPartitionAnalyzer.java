/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.fast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.jbatch.export.fast.data.PartitionSummary;

@Dependent
public class ExportPartitionAnalyzer implements PartitionAnalyzer {
    private static final Logger logger = Logger.getLogger(ExportPartitionAnalyzer.class.getName());

    @Inject
    JobContext jobContext;

    // accumulate the individual PartitionSummary objects
    private List<PartitionSummary> partitionSummaries = new ArrayList<>();

    public ExportPartitionAnalyzer() {
        // do nothing.
    }

    @Override
    public void analyzeStatus(BatchStatus batchStatus, String exitStatus) {
        if (BatchStatus.FAILED.equals(batchStatus) && "NO_SUCH_BUCKET".equals(exitStatus)) {
            jobContext.setExitStatus("NO_SUCH_BUCKET");
        }
    }

    @Override
    public void analyzeCollectorData(Serializable data) {
        // data is the value returned by the ExportPartitionCollector
        if (data == null) {
            return;
        }

        PartitionSummary ps = (PartitionSummary) data;
        logger.fine("adding partition summary to final result for '" + ps.getResourceType() + "'");

        partitionSummaries.add(ps);

        // Publish the current summary as transient user data so that it can be picked
        // up by the ExportJobListener which will generate a final exitStatus message
        jobContext.setTransientUserData(partitionSummaries);
    }
}