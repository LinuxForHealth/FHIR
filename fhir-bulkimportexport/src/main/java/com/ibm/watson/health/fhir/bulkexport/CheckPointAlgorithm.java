/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

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

    @Inject
    JobContext jobContext;
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
        TransientUserData chunkData = (TransientUserData)jobContext.getTransientUserData();
        
        if (chunkData != null) {
            return chunkData.getCurrentPartSize() >= Constants.COS_PART_MINIMALSIZE || chunkData.pageNum == -1;
        } else {
            return true;
        }
        
    }

}
