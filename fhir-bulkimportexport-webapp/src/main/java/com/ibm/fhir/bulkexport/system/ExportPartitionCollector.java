/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.io.Serializable;

import javax.batch.api.partition.PartitionCollector;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkexport.common.CheckPointUserData;
import com.ibm.fhir.bulkexport.common.TransientUserData;

@Dependent
public class ExportPartitionCollector implements PartitionCollector {
    @Inject
    StepContext stepCtx;

    public ExportPartitionCollector() {
        // The injected properties are not available at class construction time
        // These values are lazy injected BEFORE calling 'collectPartitionData'.
    }

    @Override
    public Serializable collectPartitionData() throws Exception {
        TransientUserData transientUserData  = (TransientUserData)stepCtx.getTransientUserData();
        BatchStatus batchStatus = stepCtx.getBatchStatus();

        // If the job is being stopped or in other status except for "started", or if there is more page to process, then collect nothing.
        if (!batchStatus.equals(BatchStatus.STARTED)
            || transientUserData.isMoreToExport()
            || transientUserData.getResourceTypeSummary() == null)
        {
            return null;
        }

        CheckPointUserData partitionSummary = CheckPointUserData.fromTransientUserData(transientUserData);
        return partitionSummary;
    }

}
