/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import net.jcip.annotations.NotThreadSafe;

/**
 * A byte buffer which supports both InputStream and OutputStream interfaces
 * without needless copying of (potentially large) byte arrays. Not intended for
 * multi-threaded access (no synchronization).
 */
@NotThreadSafe
public class InputOutputByteStream {

    // The buffer used to hold the bytes
    private byte[] buffer;

    // the position in the buffer where we will write the next byte
    private int offset = 0;

    // @implNote See ByteArrayOutputStream for notes on array size limit
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private static final int HALF_MAX_SIZE = MAX_ARRAY_SIZE / 2;

    // the strategy used to reshape the internal buffer when we need more capacity
    private final ReshapeStrategy reshapeStrat;

    /**
     * Strategy for how we resize the buffer.
     */
    public static class ReshapeStrategy {
        public int newSize(int currentSize, int desiredSize) {
            if (currentSize < 1) {
                throw new IllegalArgumentException("currentSize must be > 0");
            }

            if (desiredSize > MAX_ARRAY_SIZE) {
                throw new OutOfMemoryError("buffer capacity cannot exceed " + MAX_ARRAY_SIZE);
            }

            int newSize;
            if (desiredSize > currentSize) {
                // double the capacity each time, but check first to avoid overflow issues
                newSize = currentSize > HALF_MAX_SIZE ? MAX_ARRAY_SIZE : currentSize << 1;

                // if doubling wasn't enough, we need to (at a minimum) match the desired size precisely
                if (newSize < desiredSize) {
                    newSize = desiredSize;
                }
            } else {
                newSize = currentSize; // no change needed
            }

            return newSize;
        }
    }

    /**
     * Inner class used to expose the buffer as an OutputStream
     */
    private class ByteOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            int idx = offset++;
            extend(idx);
            buffer[idx] = (byte)b;
        }

        @Override
        public void write(byte b[], int off, int len) {
            int newLength = offset + len;
            extend(newLength);
            System.arraycopy(b, off, buffer, offset, len);
            offset += len;
        }

    }

    /**
     * Inner class used to expose the buffer as an InputStream when
     * the accessMode has been flipped to READ.
     */
    private class ByteInputStream extends InputStream {
        // each read stream gets its own read offset in the buffer
        private int posn = 0;

        // maintains the position of the last call to mark
        private int mark = 0;

        @Override
        public int read() throws IOException {
            return posn < offset ? (buffer[posn++] & 0xff) : -1;
        }

        @Override
        public int read(byte b[], int off, int len) throws IOException {
            if (posn >= offset) {
                return -1;
            }

            // clamp the desired length to the number we have available
            int avail = offset - posn;
            if (len > avail) {
                len = avail;
            }

            if (len <= 0) {
                return 0;
            }

            // copy the buffer contents into b[] and advance the posn marker
            System.arraycopy(buffer, posn, b, off, len);
            posn += len;

            // return how many bytes we ended up reading from buffer
            return len;
        }

        @Override
        public long skip(long n) {
            if (n < 0) {
                return 0;
            }

            // don't try to skip more than we have left
            long remaining = offset - posn;
            if (n > remaining) {
                n = remaining;
            }

            posn += n;
            return n;
        }

        @Override
        public void reset() throws IOException {
            posn = mark;
        }

        @Override
        public void mark(int readlimit) {
            mark = posn;
        }

        @Override
        public boolean markSupported() {
            return true;
        }
    }

    /**
     * Create a buffer with the given capacity and default {@link ReshapeStrategy}
     */
    public InputOutputByteStream(int initialCapacity) {
        this.buffer = new byte[initialCapacity];
        this.reshapeStrat = new ReshapeStrategy();
    }

    public InputOutputByteStream(byte[] adoptBuffer, int offset) {
        // Adopt a buffer which may already contain data
        this.buffer = adoptBuffer;
        this.offset = offset;

        // Just in case we get a null buffer, provide our own
        if (this.buffer == null) {
            this.buffer = new byte[4096];
        }
        this.reshapeStrat = new ReshapeStrategy();
    }

    /**
     * Create a buffer with a given capacity and override the {@link ReshapeStrategy}
     * @param initialCapacity
     * @param strat
     */
    public InputOutputByteStream(int initialCapacity, ReshapeStrategy strat) {
        this.buffer = new byte[initialCapacity];
        this.reshapeStrat = strat;
    }

    /**
     * How many bytes have been written to the byte buffer
     * @return
     */
    public int size() {
        return this.offset;
    }

    /**
     * Extend the internal buffer so that it can accommodate at least
     * the requiredCapacity
     * @param requiredCapacity
     */
    private void extend(int requiredCapacity) {
        if (requiredCapacity > MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("buffer capacity cannot exceed " + MAX_ARRAY_SIZE);
        }

        if (requiredCapacity > buffer.length) {
            int newSize = reshapeStrat.newSize(buffer.length, requiredCapacity);
            buffer = Arrays.copyOf(buffer, newSize);
        }
    }

    /**
     * Return a new output stream backed by the byte buffer managed by this.
     * @return
     * @throws IllegalStateException if called after the mode has been
     *         flipped to read.
     */
    public OutputStream outputStream() {
        return new ByteOutputStream();
    }

    /**
     * Provide a new InputStream with its own dedicated position starting at 0 offset
     * @return
     */
    public InputStream inputStream() {
        // ByteArrayInputStream uses synchronized, so we cut our own
        return new ByteInputStream();
    }

    /**
     * Provides a wrapped version of the internal buffer.
     * @return a read-only ByteBuffer.
     */
    public ByteBuffer wrap() {
        return ByteBuffer.wrap(this.buffer, 0, size()).asReadOnlyBuffer();
    }

    /**
     * Reset the offset to make the buffer appear empty. Does not change the current
     * length (capacity). Note that any streams currently being used to read the data
     * will end.
     */
    public void reset() {
        this.offset = 0;
    }
}