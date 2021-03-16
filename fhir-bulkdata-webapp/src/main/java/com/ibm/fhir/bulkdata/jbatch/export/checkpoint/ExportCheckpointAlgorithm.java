/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.checkpoint;

import java.util.logging.Level;
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
        if (logger.isLoggable(Level.FINE)) {
            ExportTransientUserData chunkData = (ExportTransientUserData) stepCtx.getTransientUserData();
            if (chunkData != null) {
                logger.fine("begin checkpoint [" +
                        "page " + chunkData.getPageNum() + " of " + chunkData.getLastPageNum() + ", " +
                        "bufferSize=" + chunkData.getBufferStream().size() + ", " +
                        "uploadPart=" + chunkData.getPartNum() + ", " +
                        "currentUploadSize=" + chunkData.getCurrentUploadSize() + ", " +
                        "currentUploadResourceNum=" + chunkData.getCurrentUploadResourceNum() +
                        "]");
            }
        }
    }

    @Override
    public void endCheckpoint() {
        if (logger.isLoggable(Level.FINE)) {
            ExportTransientUserData chunkData = (ExportTransientUserData) stepCtx.getTransientUserData();
            if (chunkData == null) {
                logger.warning("end checkpoint [no chunkData]");
            } else {
                logger.fine("end checkpoint [" +
                        "page " + chunkData.getPageNum() + " of " + chunkData.getLastPageNum() + ", " +
                        "bufferSize=" + chunkData.getBufferStream().size() + ", " +
                        "uploadPart=" + chunkData.getPartNum() + ", " +
                        "currentUploadSize=" + chunkData.getCurrentUploadSize() + ", " +
                        "currentUploadResourceNum=" + chunkData.getCurrentUploadResourceNum() +
                        "]");
            }
        }
    }

    @Override
    public boolean isReadyToCheckpoint() {
        ExportTransientUserData chunkData = (ExportTransientUserData) stepCtx.getTransientUserData();
        if (chunkData == null) {
            return false;
        }

        long cosFileMaxResources = ConfigurationFactory.getInstance().getCoreCosObjectResourceCountThreshold();
        long cosFileThresholdSize = ConfigurationFactory.getInstance().getCoreCosObjectSizeThreshold();
        long cosMultiPartMinSize = ConfigurationFactory.getInstance().getCoreCosPartUploadTriggerSize();

        // Stop writing to the current COS object
        boolean overFileSizeThreshold = cosFileThresholdSize != 0 && chunkData.getBufferStream().size() >= cosFileThresholdSize;
        boolean overMaxResourceCountThreshold = cosFileMaxResources != 0 && chunkData.getCurrentUploadResourceNum() >= cosFileMaxResources;
        boolean end = chunkData.getPageNum() >= chunkData.getLastPageNum();
        chunkData.setFinishCurrentUpload(overFileSizeThreshold || overMaxResourceCountThreshold || end);

        // Indicates a multipart read.
        boolean multiPartChunkMinimum = chunkData.getBufferStream().size() > cosMultiPartMinSize;

        // At this point, there are two conditions that trigger a checkpoint:
        // 1 - Multipart Chunk
        // 2 - Or the end of a write
        return multiPartChunkMinimum || chunkData.isFinishCurrentUpload();
    }
}