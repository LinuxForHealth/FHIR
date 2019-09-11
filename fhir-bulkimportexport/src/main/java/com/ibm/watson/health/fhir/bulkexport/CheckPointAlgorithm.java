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
    private final static Logger logger = Logger.getLogger(ChunkProcessor.class.getName());
    @Inject
    JobContext jobContext;
    
    /**
     * The cos.pagesperobject.
     */
    @Inject
    @BatchProperty(name = "cos.pagesperobject")
    String pagesPerCosObject;
    
    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }
    
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
        int numofPagePerCosObject;
        try {
            numofPagePerCosObject = Integer.parseInt(pagesPerCosObject);           
        } catch (Exception e) {
            numofPagePerCosObject = Constants.DEFAULT_NUMOFPAGES_EACH_COS_OBJECT;
        }
        
        log("isReadyToCheckpoint", numofPagePerCosObject + " pages per COS object!");
        
        TransientUserData chunkData = (TransientUserData)jobContext.getTransientUserData();
        
        if (chunkData != null) {
            if (chunkData.isSingleCosObject()) {
                return chunkData.getCurrentPartSize() >= Constants.COS_PART_MINIMALSIZE || chunkData.getPageNum() > chunkData.getLastPageNum();
            } else {
                return ((chunkData.getPageNum() - 1) % numofPagePerCosObject == 0 || chunkData.getPageNum() > chunkData.getLastPageNum());
            }
        } 
        return true;
    }

}
