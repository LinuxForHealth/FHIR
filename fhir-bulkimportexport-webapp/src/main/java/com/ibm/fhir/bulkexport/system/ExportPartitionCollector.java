/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionCollector;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.fhir.bulkexport.common.CheckPointUserData;
import com.ibm.fhir.bulkexport.common.TransientUserData;

public class ExportPartitionCollector implements PartitionCollector {
    private static final Logger logger = Logger.getLogger(ExportPartitionCollector.class.getName());
    @Inject
    StepContext stepCtx;

    public ExportPartitionCollector() {
        // The injected properties are not available at class construction time
        // These values are lazy injected BEFORE calling 'collectPartitionData'.
    }

    @Override
    public Serializable collectPartitionData() throws Exception {
        TransientUserData partitionSummaryData  = (TransientUserData)stepCtx.getTransientUserData();
        BatchStatus batchStatus = stepCtx.getBatchStatus();

        // If the job is being stopped or in other status except for "started", then collect nothing.
        if (!batchStatus.equals(BatchStatus.STARTED)) {
            return null;
        }

        CheckPointUserData partitionSummaryForMetrics = CheckPointUserData.fromTransientUserData(partitionSummaryData);
        return partitionSummaryForMetrics;
    }

}
