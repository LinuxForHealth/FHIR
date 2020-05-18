/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.fhir.bulkexport.common.CheckPointUserData;

public class ExportPartitionAnalyzer implements PartitionAnalyzer {
    private static final Logger logger = Logger.getLogger(ExportPartitionAnalyzer.class.getName());
    @Inject
    JobContext jobContext;

    private List<CheckPointUserData> partitionSummaries = new ArrayList<>();

    public ExportPartitionAnalyzer() {
    }

    @Override
    public void analyzeStatus(BatchStatus batchStatus, String exitStatus) {

    }

    @Override
    public void analyzeCollectorData(Serializable data) {
        if (data == null) {
            return;
        }
        CheckPointUserData partitionSummary  = (CheckPointUserData) data;

        if (partitionSummary != null) {
            partitionSummaries.add(partitionSummary);
            jobContext.setTransientUserData(partitionSummaries);
        }

    }
}
