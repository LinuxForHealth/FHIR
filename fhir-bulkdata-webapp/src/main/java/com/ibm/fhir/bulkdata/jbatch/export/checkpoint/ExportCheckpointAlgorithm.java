/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.checkpoint;

import java.util.logging.Logger;

import javax.batch.api.chunk.CheckpointAlgorithm;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 * BulkData Export Custom CheckpointAlgorithm which considers COS size requirements while checkpointing.
 */
@Dependent
public class ExportCheckpointAlgorithm implements CheckpointAlgorithm {

    private final static Logger logger = Logger.getLogger(ExportCheckpointAlgorithm.class.getName());

    @Inject
    StepContext stepCtx;

    public ExportCheckpointAlgorithm() {
        // No Operation
    }

    @Override
    public int checkpointTimeout() {
        return 0;
    }

    @Override
    public void beginCheckpoint() {
        logger.finer("begin checkpoint");
    }

    @Override
    public void endCheckpoint() {
        // Nothing to do here at present
        logger.finer("end checkpoint");
    }

    @Override
    public boolean isReadyToCheckpoint() {
        ExportTransientUserData chunkData = (ExportTransientUserData) stepCtx.getTransientUserData();
        if (chunkData == null) {
            return false;
        }

        int cosFileMaxResources = ConfigurationFactory.getInstance().getCoreCosMaxResources();
        int cosFileMaxSize = ConfigurationFactory.getInstance().getCoreCosMaxSize();
        int cosFileMinSize = ConfigurationFactory.getInstance().getCoreCosMinSize();

        // Stop writing to the current COS object if we hit either the max size or max number
        // of resource limits.

        chunkData.setFinishCurrentUpload(cosFileMaxSize != -1 && chunkData.getCurrentUploadSize() >= cosFileMaxSize
                || cosFileMaxResources != -1 && chunkData.getCurrentUploadResourceNum() >= cosFileMaxResources);

        // Trigger a checkpoint each time we start a new page, if the data written to the reader-to-writer
        // buffer exceeds our min threshold, or we've reached the end for this chunk.
        return (chunkData.getPageNum() > chunkData.getLastPageNum()
                || chunkData.getBufferStream().size() > cosFileMinSize
                || chunkData.isFinishCurrentUpload());
    }
}