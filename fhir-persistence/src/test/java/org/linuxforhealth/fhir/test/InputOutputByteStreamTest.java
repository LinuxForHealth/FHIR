/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.test;


import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;

/**
 * Unit test for {@link InputOutputByteStream}
 */
public class InputOutputByteStreamTest {

    /**
     * Test basic write/read operations
     * @throws IOException
     */
    @Test
    public void testBasicOperation() throws IOException {
        InputOutputByteStream iobs = new InputOutputByteStream(10);
        byte[] source = {0x01, 0x02, 0x03, 0x04};
        OutputStream os = iobs.outputStream();
        os.write(source);

        // read back and check
        InputStream is = iobs.inputStream();
        byte[] readBack = new byte[8];
        int len = is.read(readBack);
        assertEquals(len, source.length);
        assertEquals(compare(source, 0, source.length-1, readBack, 0, len-1), 0);

        // Check that we can repeat the read with another InputStream
        InputStream is2 = iobs.inputStream();
        byte[] readBack2 = new byte[16]; // different size for more testing
        int len2 = is2.read(readBack2);
        assertEquals(len2, source.length);
        assertEquals(compare(source, 0, source.length-1, readBack2, 0, len2-1), 0);
    }

    @Test
    public void testBasicOperationWithMarkAndReset() throws IOException {
        InputOutputByteStream iobs = new InputOutputByteStream(10);
        byte[] source = {0x01, 0x02, 0x03, 0x04};
        OutputStream os = iobs.outputStream();
        os.write(source);

        // read back and check
        InputStream is = iobs.inputStream();
        byte[] readBack = new byte[8];
        int len = is.read(readBack);
        assertEquals(len, source.length);
        assertEquals(compare(source, 0, source.length-1, readBack, 0, len-1), 0);

        // If it's not supported then it'll throw an exception.
        is.reset();

        // Read 3 of 4 bytes, and then iterate.
        byte[] readReset = new byte[3];
        int lenReset = is.read(readReset);
        assertEquals(lenReset, 3);

        // Fixing the mark to the current posn
        is.mark(100);
        lenReset = is.read(readReset);
        assertEquals(lenReset, 1);

        // Reset to marked point
        is.reset();
        lenReset = is.read(readReset);
        assertEquals(lenReset, 1);
    }

    /**
     * Check that the two arrays are equal. Can't use {@link Arrays#compare(byte[], int, int, byte[], int, int)}
     * because that's since Java9 and we still support Java8.
     * @param left
     * @param leftFrom
     * @param leftTo
     * @param right
     * @param rightFrom
     * @param rightTo
     * @return
     */
    private int compare(byte[] left, int leftFrom, int leftTo, byte[] right, int rightFrom, int rightTo) {
        int leftCount = leftTo - leftFrom + 1;
        int rightCount = rightTo - rightFrom + 1;
        int result = leftCount < rightCount ? -1 : rightCount > leftCount ? 1 : 0;

        for (int i=0; i<leftCount && result == 0; i++) {
            result = left[leftFrom + i] - right[rightFrom+i];
        }

        return result;
    }

    @Test
    public void testWriteByByte() throws IOException {
        InputOutputByteStream iobs = new InputOutputByteStream(10);
        byte[] source = {0x01, 0x02, 0x03, 0x04};
        OutputStream os = iobs.outputStream();

        os.write(source[0]);
        os.write(source[1]);
        os.write(source[2]);
        os.write(source[3]);
        assertEquals(iobs.size(), 4);

        // read back and check
        InputStream is = iobs.inputStream();
        byte[] readBack = new byte[8];
        int len = is.read(readBack);
        assertEquals(compare(source, 0, source.length-1, readBack, 0, len-1), 0);

        // read back a subset
        InputStream is2 = iobs.inputStream();
        byte[] readBack2 = new byte[3]; // check we can read some not all
        int len2 = is2.read(readBack2);
        assertEquals(len2, 3);
        assertEquals(compare(source, 0, 2, readBack2, 0, 2), 0);

        // Read the remaining byte
        assertEquals(is2.read(), 0x04);
        assertEquals(is2.read(), -1);

    }

    /**
     * Test the buffer will grow as we write more
     */
    @Test
    public void testGrowth() throws IOException {
        InputOutputByteStream iobs = new InputOutputByteStream(10);
        byte[] source = {0x01, 0x02, 0x03, 0x04};
        OutputStream os = iobs.outputStream();
        os.write(source);
        os.write(source);
        os.write(source);

        // Test that we can read back exactly 4 bytes
        assertEquals(iobs.size(), source.length * 3);
        InputStream is = iobs.inputStream();
        byte[] readBack = new byte[source.length];
        int len = is.read(readBack);
        assertEquals(len, source.length);
        assertEquals(compare(source, 0, source.length-1, readBack, 0, len-1), 0);
    }

    @Test
    public void testReadEachByte() throws IOException {
        InputOutputByteStream iobs = new InputOutputByteStream(10);
        byte[] source = {0x01, 0x02, 0x03, 0x04};
        OutputStream os = iobs.outputStream();
        os.write(source);
        InputStream is = iobs.inputStream();
        assertEquals(is.read(), 0x01);
        assertEquals(is.read(), 0x02);
        assertEquals(is.read(), 0x03);
        assertEquals(is.read(), 0x04);
        assertEquals(is.read(), -1);
    }

    @Test
    public void testReshapeStrategyImpl() {
        InputOutputByteStream.ReshapeStrategy strat = new InputOutputByteStream.ReshapeStrategy();
        assertEquals(strat.newSize(10,  9), 10); // free capacity
        assertEquals(strat.newSize(10, 10), 10); // just fits
        assertEquals(strat.newSize(10, 11), 20); // need more space so double capacity
        assertEquals(strat.newSize(10, 21), 21); // need more than double

        // special case - max array size is slightly less than max int value because of possible compiler overheads
        assertEquals(strat.newSize(Integer.MAX_VALUE/2-2, Integer.MAX_VALUE/2-1), Integer.MAX_VALUE-8);
    }
}