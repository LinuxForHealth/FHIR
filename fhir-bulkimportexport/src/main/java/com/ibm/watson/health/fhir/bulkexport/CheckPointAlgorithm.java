/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.CheckpointAlgorithm;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.waston.health.fhir.bulkcommon.Constants;

/**
 * Bulk export Chunk implementation - custom checkpoint algorithm.
 * 
 * @author Albert Wang
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
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.maxfilesize")
    String cosBucketMaxFileSize;

    /**
     * Default constructor.
     */
    public CheckPointAlgorithm() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see CheckpointAlgorithm#checkpointTimeout()
     */
    public int checkpointTimeout() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see CheckpointAlgorithm#endCheckpoint()
     */
    public void endCheckpoint() {
        // TODO Auto-generated method stub
    }

    /**
     * @see CheckpointAlgorithm#beginCheckpoint()
     */
    public void beginCheckpoint() {
        // TODO Auto-generated method stub
    }

    /**
     * @see CheckpointAlgorithm#isReadyToCheckpoint()
     */
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
                        logger.info("isReadyToCheckpoint: Set max COS file size to " + cosMaxFileSize + ".");
                    } catch (Exception e) {
                        cosMaxFileSize = Constants.DEFAULT_MAXCOSFILE_SIZE;
                        logger.warning("isReadyToCheckpoint: Set max COS file size to default("
                                + Constants.DEFAULT_MAXCOSFILE_SIZE + ").");
                    }
                    return (chunkData.getBufferStream().size() >= cosMaxFileSize
                            || chunkData.getPageNum() > chunkData.getLastPageNum());
                } else {
                    try {
                        numofPagePerCosObject = Integer.parseInt(pagesPerCosObject);
                        logger.info("isReadyToCheckpoint: " + numofPagePerCosObject + " pages per COS object!");
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
        return true;
    }

}
