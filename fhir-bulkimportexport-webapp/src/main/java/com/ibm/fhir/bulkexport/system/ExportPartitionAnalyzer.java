/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkexport.common.CheckPointUserData;

@Dependent
public class ExportPartitionAnalyzer implements PartitionAnalyzer {
    @Inject
    JobContext jobContext;

    private List<CheckPointUserData> partitionSummaries = new ArrayList<>();

    public ExportPartitionAnalyzer() {
        // do nothing.
    }

    @Override
    public void analyzeStatus(BatchStatus batchStatus, String exitStatus) {
        // do nothing.
    }

    @Override
    public void analyzeCollectorData(Serializable data) {
        if (data == null) {
            return;
        }

        CheckPointUserData partitionSummary  = (CheckPointUserData) data;
        partitionSummaries.add(partitionSummary);
        jobContext.setTransientUserData(partitionSummaries);
    }
}
