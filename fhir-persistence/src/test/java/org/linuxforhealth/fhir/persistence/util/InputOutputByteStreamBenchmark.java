/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Simple performance comparison between ByteArrayOutputStream/ByteArrayInputStream and
 * the comparable function provided by this class. Useful to run with a profiler.
 * On: MacBook Pro 15-inch, 2016; 2.7 GHz Quad-Core Intel Core i7; 16 GB 2133 MHz LPDDR3
 *     the performance improvement is around 15%.
 */
public class InputOutputByteStreamBenchmark {
    /**
     * Main entry point
     * @param args (not used)
     */
    public static void main(String[] args) {
        final int initialSize = 4096;
        final int iters = 100000;
        final int writeCount = 10000;

        System.out.println("Starting micro-benchmark for " + InputOutputByteStream.class.getSimpleName() + ". This should take less that 1 minute on a modern CPU (2021)");

        try {
            // Just some random text to use as a filler for the buffer
            final byte[] src = new String("{This is the story of...}").getBytes();
            // Get a baseline with the original implementation
            final long origStart = System.nanoTime();
            long bytesWritten = 0;
            for (int i=0; i<iters; i++) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(initialSize);
                for (int x=0; x<writeCount; x++) {
                    bos.write(src);
                }

                byte[] buffer = bos.toByteArray();
                bytesWritten += buffer.length;

                ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                byte[] tmp = new byte[4096];
                while (bis.read(tmp) != -1) {
                    ; // NOP
                }
            }
            final long origEnd = System.nanoTime();
            System.out.println("Total bytes written: " + bytesWritten/1024/1024 + " MiB");

            // Now compare with the new approach
            bytesWritten = 0;
            final long newStart = origEnd;
            for (int i=0; i<iters; i++) {
                InputOutputByteStream iobs = new InputOutputByteStream(initialSize);
                OutputStream os = iobs.outputStream();
                for (int x=0; x<writeCount; x++) {
                    os.write(src);
                }

                bytesWritten += iobs.size();

                InputStream is = iobs.inputStream();
                byte[] tmp = new byte[4096];
                while (is.read(tmp) != -1) {
                    ; // NOP
                }
            }
            final long newEnd = System.nanoTime();
            System.out.println("Total bytes written: " + bytesWritten/1024/1024 + " MiB");

            double origSeconds = (origEnd - origStart) / 1e9;
            double newSeconds = (newEnd - newStart) / 1e9;

            System.out.println(String.format("Original = %5.3fs, New = %5.3fs, Percent improvement = %5.1f%%", origSeconds, newSeconds, 100.0 * (origSeconds - newSeconds) / origSeconds));
        } catch (IOException x) {
            System.out.println("Test failed: " + x.getMessage());
        }
    }
}