/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.CheckpointAlgorithm;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;

/**
 * Bulk export Chunk implementation - custom checkpoint algorithm.
 *
 */
@Dependent
public class CheckPointAlgorithm implements CheckpointAlgorithm {
    private final static Logger logger = Logger.getLogger(CheckPointAlgorithm.class.getName());

    @Inject
    StepContext stepCtx;

    /**
     * The file resources number limit when exporting to multiple COS files.
     */
    @Inject
    @BatchProperty(name = Constants.COS_BUCKET_FILE_MAX_RESOURCES)
    String cosBucketFileMaxResources;

    /**
     * The file size limit when exporting to multiple COS files.
     */
    @Inject
    @BatchProperty(name = Constants.COS_BUCKET_FILE_MAX_SIZE)
    String cosBucketFileMaxSize;

    /**
     * Default constructor.
     */
    public CheckPointAlgorithm() {
     // Nothing to do here at present
    }

    @Override
    public int checkpointTimeout() {
        return 0;
    }

    @Override
    public void endCheckpoint() {
        // Nothing to do here at present
    }

    @Override
    public void beginCheckpoint() {
        // Nothing to do here at present
    }

    @Override
    public boolean isReadyToCheckpoint() {
        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();

        if (chunkData != null) {
            int cosFileMaxResources = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_COSFILEMAXRESOURCES, Constants.DEFAULT_COSFILE_MAX_RESOURCESNUMBER);
            int cosFileMaxSize = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_COSFILEMAXSIZE , Constants.DEFAULT_COSFILE_MAX_SIZE);
            if (cosBucketFileMaxSize != null) {
                try {
                    cosFileMaxSize = Integer.parseInt(cosBucketFileMaxSize);
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("isReadyToCheckpoint: Set max COS file size to " + cosFileMaxSize + ".");
                    }
                } catch (Exception e) {
                    logger.warning("isReadyToCheckpoint: Set max COS file size to default("
                            + Constants.DEFAULT_COSFILE_MAX_SIZE + ").");
                }

            }

            if (cosBucketFileMaxResources != null) {
                try {
                    cosFileMaxResources = Integer.parseInt(cosBucketFileMaxResources);
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("isReadyToCheckpoint: " + cosFileMaxResources + " resources per COS file!");
                    }
                } catch (Exception e) {
                    logger.warning("isReadyToCheckpoint: Set number of resources per COS file to default("
                            + Constants.DEFAULT_COSFILE_MAX_RESOURCESNUMBER + ").");
                }
            }

            chunkData.setFinishCurrentUpload(cosFileMaxSize != -1 && chunkData.getCurrentUploadSize() >= cosFileMaxSize
                    || cosFileMaxResources != -1 && chunkData.getCurrentUploadResourceNum() >= cosFileMaxResources);

            return (chunkData.getPageNum() > chunkData.getLastPageNum()
                    || chunkData.getBufferStream().size() > Constants.COS_PART_MINIMALSIZE
                    || chunkData.isFinishCurrentUpload());
            }
            return false;
        }
}
