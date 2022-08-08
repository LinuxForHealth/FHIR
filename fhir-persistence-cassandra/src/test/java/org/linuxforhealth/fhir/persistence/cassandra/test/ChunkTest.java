/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.cassandra.test;

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.persistence.cassandra.payload.CqlChunkedPayloadStream;
import org.linuxforhealth.fhir.persistence.cassandra.payload.IBufferProvider;
import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;

/**
 * Unit test for the payload chunk write/read cycle
 */
public class ChunkTest {

    String bigString;

    /**
     * Build a random string because we don't want GZIP to compress it
     */
    @BeforeClass
    public void prepare() {
        StringBuilder big = new StringBuilder();
        SecureRandom rnd = new SecureRandom();
        for (int i=0; i<1024*1024; i++) {
            big.append(rnd.nextLong());
        }
        
        bigString = big.toString();
    }

    /**
     * Test processing a big object written and read as a single chunk
     */
    @Test
    public void testBigSingleChunk() throws IOException {
        
        // Render the string to a byte-buffer
        InputOutputByteStream iobs = new InputOutputByteStream(4096);
        try (GZIPOutputStream os = new GZIPOutputStream(iobs.outputStream())) {
            os.write(bigString.getBytes(StandardCharsets.UTF_8));
            os.finish();
        }
        
        // Decode the whole thing once just so we know what we're doing
        try (GZIPInputStream is = new GZIPInputStream(iobs.inputStream())) {
            // Keep reading the stream of bytes to recompose the big string
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int nr;
            byte b[] = new byte[4096];
            while ((nr = is.read(b)) >= 0) {
                if (nr > 0) {
                    bos.write(b, 0, nr);
                }
            }
            
            bos.flush();
            String readBack = new String(bos.toByteArray(), StandardCharsets.UTF_8);
            
            // Doing it this way because we don't want the huge strings to be printed
            // if something went wrong with assertEquals(readBack, bigString)
            assertTrue(readBack.equals(bigString));
        }
    }

    /**
     * Test processing a big object written and read as multiple chunks
     */
    @Test
    public void testBigMultipleChunks() throws IOException {
        
        // Render the string to a byte-buffer
        InputOutputByteStream iobs = new InputOutputByteStream(4096);
        try (GZIPOutputStream os = new GZIPOutputStream(iobs.outputStream())) {
            os.write(bigString.getBytes(StandardCharsets.UTF_8));
            os.finish();
        }
        
        // Break the stream into multiple chunks
        List<ByteBuffer> chunks = new ArrayList<>();
        try (InputStream is = iobs.inputStream()) {
            int nr;
            byte b[] = new byte[4096];
            while ((nr = is.read(b)) >= 0) {
                if (nr > 0) {
                    ByteBuffer bb = ByteBuffer.wrap(b, 0, nr);
                    chunks.add(bb);
                    
                    // The ByteBuffer adopts the byte array, so we need a new one each time
                    b = new byte[4096];
                }
            }
        }

        // Wrap our list of chunks in a simple IBufferProvider implementation
        // which we can use with our payload stream
        IBufferProvider bufferProvider = new IBufferProvider() {
            int idx = 0;
            @Override
            public ByteBuffer nextBuffer() {
                return idx < chunks.size() ? chunks.get(idx++) : null;
            }
        };
        
        // Decompress the chunked stream
        try (GZIPInputStream is = new GZIPInputStream(new CqlChunkedPayloadStream(bufferProvider))) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int nr;
            byte b[] = new byte[4096];
            while ((nr = is.read(b)) >= 0) {
                if (nr > 0) {
                    bos.write(b, 0, nr);
                }
            }
            
            bos.flush();
            String readBack = new String(bos.toByteArray(), StandardCharsets.UTF_8);
            
            // Doing it this way because we don't want the huge strings to be printed
            // if something went wrong with assertEquals(readBack, bigString)
            assertTrue(readBack.equals(bigString));
        }
    }
}