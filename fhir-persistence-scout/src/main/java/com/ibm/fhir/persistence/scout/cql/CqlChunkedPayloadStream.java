/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout.cql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

/**
 * Reads the payload from CQL. Cassandra imposes both hard and practical
 * upper limits on the length of a column value. Because FHIR payloads
 * can be an arbitrary size, we separate payloads into chunks spread
 * across multiple rows. This class reads those rows in sequence and
 * presents the results as an ordinary stream of bytes
 */
public class CqlChunkedPayloadStream extends InputStream {
    
    // Buffer containing bytes from the current row of the result
    private ByteBuffer buffer;

    // The ResultSet we are adapting as a stream
    private final ResultSet resultSet;

    // Track each ordinal returned so we can detect gaps
    private int currentOrdinal = -1;
 
    /**
     * Public constructor
     * @param rs
     */
    public CqlChunkedPayloadStream(ResultSet rs) {
        this.resultSet = rs;
    }

    @Override
    public int read() throws IOException {
        refreshBuffer();
        
        if (buffer == null || !buffer.hasRemaining()) {
            return -1; // no more data
        } else {
            return buffer.get(); // get the next byte, returned as an int
        }
    }
    
    @Override
    public int read(byte[] dst, int off, int len) throws IOException {
        refreshBuffer();

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

    /**
     * If we don't currently have a buffer, or we do but it is empty,
     * refresh it with data from the next row
     */
    private void refreshBuffer() throws IOException {
        if (buffer == null || !buffer.hasRemaining()) {
            Row row = resultSet.one();
            if (row != null) {
                if (row.isNull(2)) {
                    throw new IllegalStateException("buffer value must not be null");
                }

                // check for gaps...which would break what is supposed to be a continuous stream of data
                int rowOrdinal = row.getInt(1);
                int gap = rowOrdinal - currentOrdinal;
                if (gap != 1) {
                    throw new IOException("Gap in chunk ordinal. ResultSet not ordered by ordinal, or a row is missing");
                }
                
                // column 2 of the result set should be a blob which we can consume as
                // a byte buffer
                this.buffer = row.getByteBuffer(2);
            } else {
                // no more data
                this.buffer = null;
            }
        }
    }
}
