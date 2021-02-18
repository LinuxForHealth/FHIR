/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.jbatch.bulkdata.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.listener.ChunkListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;


/**
 * Enables Logging for the Given Step
 */
@Dependent
public class StepChunkListener implements ChunkListener {
    private final static Logger logger = Logger.getLogger(StepChunkListener.class.getName());

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    @Override
    public void beforeChunk() throws Exception {
        // No Operation
    }

    @Override
    public void onError(Exception ex) throws Exception {
        long stepExecutionId = stepCtx.getStepExecutionId();
        long jobExecutionId = jobCtx.getInstanceId();
        logger.log(Level.SEVERE, "StepChunkListener: job[" + jobCtx.getJobName() + "/" + jobExecutionId + "/" + stepExecutionId + "] --- " + ex.getMessage(), ex);
        // Enable PrintStackTrace when debugging in dev - ex.printStackTrace();
    }

    @Override
    public void afterChunk() throws Exception {
     // No Operation
    }
}