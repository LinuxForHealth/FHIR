/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.payload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Reads the payload from CQL. Cassandra imposes both hard and practical
 * upper limits on the length of a column value. Because FHIR payloads
 * can be an arbitrary size, we separate payloads into chunks spread
 * across multiple rows. This class reads those rows in sequence and
 * presents the results as an ordinary stream of bytes
 */
public class CqlChunkedPayloadStream extends InputStream {
    private static final Logger logger = Logger.getLogger(CqlChunkedPayloadStream.class.getName());
    
    // Buffer containing bytes from the current row of the result
    private ByteBuffer buffer;

    // The provider we use to get the sequence of buffers to read from
    private final IBufferProvider bufferProvider;
 
    /**
     * Public constructor
     * @param rs
     */
    public CqlChunkedPayloadStream(IBufferProvider bp) {
        this.bufferProvider = bp;
    }

    @Override
    public int read() throws IOException {
        refreshBuffer();
        
        if (buffer == null || !buffer.hasRemaining()) {
            return -1; // no more data
        } else {
            // get the next byte from the buffer which we know will not underflow
            return Byte.toUnsignedInt(buffer.get());
        }
    }
    
    @Override
    public int read(byte[] dst, int off, int len) throws IOException {
        // remember that read is allowed to return less than len bytes
        // although never 0 bytes, unless len == 0
        if (len == 0) {
            return 0;
        }
        
        // Fetch a buffer from the ResultSet if there's no data currently
        refreshBuffer();

        if (buffer == null || !buffer.hasRemaining()) {
            // Because we just refreshed, if hasRemaining is false, it's EOF
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

    /**
     * If we don't currently have a buffer, or we do but it is empty,
     * refresh it with data from the next row
     */
    private void refreshBuffer() throws IOException {
        if (buffer == null || !buffer.hasRemaining()) {
            // get the next buffer in the sequence, or null at the end
            buffer = bufferProvider.nextBuffer();
        }
    }
}