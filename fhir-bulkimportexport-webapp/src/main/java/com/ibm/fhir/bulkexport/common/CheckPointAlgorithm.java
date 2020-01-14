/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.common;

import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.CheckpointAlgorithm;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.fhir.bulkcommon.Constants;

/**
 * Bulk export Chunk implementation - custom checkpoint algorithm.
 *
 */

public class CheckPointAlgorithm implements CheckpointAlgorithm {
    private final static Logger logger = Logger.getLogger(CheckPointAlgorithm.class.getName());
    @Inject
    JobContext jobContext;

    /**
     * The cos.pagesperobject.
     */
    @Inject
    @BatchProperty(name = "cos.pagesperobject")
    String pagesPerCosObject;

    /**
     * The file size limit when exporting to multiple COS files.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.maxfilesize")
    String cosBucketMaxFileSize;

    /**
     * Default constructor.
     */
    public CheckPointAlgorithm() {
     // Nothing to do here at present
    }

    /**
     * @see CheckpointAlgorithm#checkpointTimeout()
     */
    @Override
    public int checkpointTimeout() {
        return 0;
    }

    /**
     * @see CheckpointAlgorithm#endCheckpoint()
     */
    @Override
    public void endCheckpoint() {
        // Nothing to do here at present
    }

    /**
     * @see CheckpointAlgorithm#beginCheckpoint()
     */
    @Override
    public void beginCheckpoint() {
        // Nothing to do here at present
    }

    /**
     * @see CheckpointAlgorithm#isReadyToCheckpoint()
     */
    @Override
    public boolean isReadyToCheckpoint() {
        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();

        if (chunkData != null) {
            if (chunkData.isSingleCosObject()) {
                return chunkData.getBufferStream().size() > Constants.COS_PART_MINIMALSIZE
                        || chunkData.getPageNum() > chunkData.getLastPageNum();
            } else {
                int numofPagePerCosObject, cosMaxFileSize;
                if (cosBucketMaxFileSize != null) {
                    try {
                        cosMaxFileSize = Integer.parseInt(cosBucketMaxFileSize);
                        logger.fine("isReadyToCheckpoint: Set max COS file size to " + cosMaxFileSize + ".");
                    } catch (Exception e) {
                        cosMaxFileSize = Constants.DEFAULT_MAXCOSFILE_SIZE;
                        logger.warning("isReadyToCheckpoint: Set max COS file size to default("
                                + Constants.DEFAULT_MAXCOSFILE_SIZE + ").");
                    }
                    return (chunkData.getBufferStream().size() >= cosMaxFileSize
                            || chunkData.getPageNum() > chunkData.getLastPageNum());
                } else {
                    if (pagesPerCosObject != null) {
                        try {
                            numofPagePerCosObject = Integer.parseInt(pagesPerCosObject);
                            logger.fine("isReadyToCheckpoint: " + numofPagePerCosObject + " pages per COS object!");
                        } catch (Exception e) {
                            numofPagePerCosObject = Constants.DEFAULT_NUMOFPAGES_EACH_COS_OBJECT;
                            logger.warning("isReadyToCheckpoint: Set number of pages per COS object to default("
                                    + Constants.DEFAULT_NUMOFPAGES_EACH_COS_OBJECT + ").");
                        }
                        return ((chunkData.getPageNum() - 1) % numofPagePerCosObject == 0
                                || chunkData.getPageNum() > chunkData.getLastPageNum());
                    }
                }
            }
        }
        return true;
    }

}
