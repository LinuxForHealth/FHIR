/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.fast;

import java.io.Serializable;

import javax.batch.api.partition.PartitionCollector;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Final step which is executed after the individual partitions have completed
 * or the job has been terminated.
 */
@Dependent
public class ExportPartitionCollector implements PartitionCollector {
    @Inject
    StepContext stepCtx;

    @Override
    public Serializable collectPartitionData() throws Exception {
        return null;
    }
}
