/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

public class ImportPartitionAnalyzer implements PartitionAnalyzer {
    @Inject
    JobContext jobContext;

    private List<ImportCheckPointData> partitionSummaries = new ArrayList<>();

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
        partitionSummaries.add((ImportCheckPointData) data);
        jobContext.setTransientUserData(partitionSummaries);
    }

}
