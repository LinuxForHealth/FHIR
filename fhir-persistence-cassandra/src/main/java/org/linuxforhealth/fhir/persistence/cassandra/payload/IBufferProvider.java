/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.cassandra.payload;

import java.nio.ByteBuffer;

/**
 * Provides a sequential list of buffers which can be iterated over
 * to recompose a larger buffer that has been broken into chunks
 */
public interface IBufferProvider {

    /**
     * Get the next buffer in the sequence, or null when the end has been reached
     * @return
     */
    ByteBuffer nextBuffer();
}
