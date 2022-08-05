/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.export.fast.checkpoint;

import java.util.logging.Logger;

import javax.batch.api.chunk.CheckpointAlgorithm;
import javax.enterprise.context.Dependent;

import org.linuxforhealth.fhir.bulkdata.jbatch.export.fast.ResourcePayloadReader;

/**
 * Bulk export fast implementation - custom checkpoint algorithm. See class comment
 * for {@link ResourcePayloadReader} for a description of why we checkpoint after
 * every readItem call
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
        // We want to write/checkpoint every time ItemReader#readItem returns.
        return true;
    }
}