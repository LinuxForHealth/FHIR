/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.payload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Provides an InputStream interface to the ByteBuffer holding the FHIR resource
 * payload
 */
public class CqlPayloadStream extends InputStream {

    // Buffer containing bytes from the current row of the result
    private final ByteBuffer buffer;

    /**
     * Public constructor
     * @param bb the ByteBuffer being wrapped
     */
    public CqlPayloadStream(ByteBuffer bb) {
        this.buffer = bb;
    }

    @Override
    public int read() throws IOException {
        if (buffer == null || !buffer.hasRemaining()) {
            return -1; // no more data
        } else {
            return buffer.get(); // get the next byte, returned as an int
        }
    }

    @Override
    public int read(byte[] dst, int off, int len) throws IOException {

        if (buffer == null || !buffer.hasRemaining()) {
            return -1;
        } else {
            // fetch as much as requested, up to the number of bytes remaining
            len = Math.min(len, buffer.remaining());
            if (len > 0) {
                buffer.get(dst, off, len);
            }
            return len;
        }
    }
}