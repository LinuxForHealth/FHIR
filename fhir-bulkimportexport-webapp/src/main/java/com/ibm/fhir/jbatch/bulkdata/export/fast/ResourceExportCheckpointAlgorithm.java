/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.fast;

import java.util.logging.Logger;

import javax.batch.api.chunk.CheckpointAlgorithm;
import javax.enterprise.context.Dependent;

/**
 * Bulk export Chunk implementation - custom checkpoint algorithm.
 *
 */
@Dependent
public class ResourceExportCheckpointAlgorithm implements CheckpointAlgorithm {
    private final static Logger logger = Logger.getLogger(ResourceExportCheckpointAlgorithm.class.getName());

    /**
     * Default constructor.
     */
    public ResourceExportCheckpointAlgorithm() {
        // NOP
    }

    @Override
    public int checkpointTimeout() {
        return 0;
    }

    @Override
    public void beginCheckpoint() {
        // Nothing to do here at present
        logger.finer("begin checkpoint");
    }

    @Override
    public void endCheckpoint() {
        // Nothing to do here at present
        logger.finer("end checkpoint");
    }

    @Override
    public boolean isReadyToCheckpoint() {
        // We want to write/checkpoint each item returned by the reader.
        return true;
    }
}