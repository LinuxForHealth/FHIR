/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.fast;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * For the fast system export implementation, all processing is handled inside the {@link ResourcePayloadReader}
 * so the writer here is simply a NOP.
 */
@Dependent
public class NopWriter extends AbstractItemWriter {
    private static final Logger logger = Logger.getLogger(NopWriter.class.getName());

    // The context object representing this export job
    @Inject
    JobContext jobContext;

    /**
     * Get a prefix which can be used to identify the job in log messages
     * @return
     */
    private String logPrefix() {
        return jobContext.getJobName() + "[" + jobContext.getExecutionId() + "]";
    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        // We don't need to do anything, other than log a debug message
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(logPrefix() + " write items count: " + items.size());
        }
    }
}