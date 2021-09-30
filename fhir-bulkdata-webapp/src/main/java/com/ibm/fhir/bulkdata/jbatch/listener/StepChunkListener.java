/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.listener.ChunkListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.AmazonS3Exception;

/**
 * Enables Logging for the Given Step
 */
@Dependent
public class StepChunkListener implements ChunkListener {
    private static final Logger LOG = Logger.getLogger(StepChunkListener.class.getName());

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
        LOG.log(Level.SEVERE, "StepChunkListener: job[" + jobCtx.getJobName() + "/" + jobExecutionId + "/" + stepExecutionId + "] --- " + ex.getMessage(), ex);
        LOG.throwing("StepChunkListener", "onError", ex);

        LOG.fine("excause" + ex.getCause());
        if (ex.getCause() instanceof AmazonS3Exception) {
            AmazonS3Exception exceptionCausedByS3Error = (AmazonS3Exception) ex.getCause();
            if ("NoSuchBucket".equals(exceptionCausedByS3Error.getErrorCode())) {
                stepCtx.setExitStatus("NO_SUCH_BUCKET");
            }
        }
    }

    @Override
    public void afterChunk() throws Exception {
     // No Operation
    }
}