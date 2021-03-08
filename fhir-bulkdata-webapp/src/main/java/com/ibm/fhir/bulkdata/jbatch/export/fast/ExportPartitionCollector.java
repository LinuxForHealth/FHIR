/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.fast;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionCollector;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.jbatch.export.fast.data.PartitionSummary;
import com.ibm.fhir.bulkdata.jbatch.export.fast.data.TransientUserData;

/**
 * Final step which is executed after the individual partitions have completed
 * or the job has been terminated.
 */
@Dependent
public class ExportPartitionCollector implements PartitionCollector {
    private static final Logger logger = Logger.getLogger(ExportPartitionCollector.class.getName());

    @Inject
    StepContext stepCtx;

    @Override
    public Serializable collectPartitionData() throws Exception {
        TransientUserData tud = (TransientUserData)stepCtx.getTransientUserData();

        BatchStatus batchStatus = stepCtx.getBatchStatus();
        switch (batchStatus) {
        case STARTED:
            if (tud.isCompleted()) {
                // Partition is done, so we can hand the summary to the analyzer
                // so that it can build up an overall result
                logger.fine("collectPartitionData - partition complete; returning CheckpointUserData for analyzer");
                return PartitionSummary.from(tud);
            } else {
                return null;
            }
        default:
            logger.fine("collectPartitionData batchStatus = " + batchStatus.name());
            return null;
        }
    }
}
