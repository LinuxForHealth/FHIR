/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.encoding;

import static org.testng.Assert.assertEquals;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.testng.annotations.Test;

/**
 * Testing our assumptions about the length of Base64 encoded strings (in UTF-8)
 */
public class Base64Test {

    /**
     * Test a variety of different input lengths and check that our calculated length
     * matches the actual length (which includes padding).
     */
    @Test
    public void testLen() {
        Encoder enc = Base64.getEncoder();
        
        for (int l=1; l<=256; l++) {
            byte[] src   = new byte[l];
            for (int i=0; i<src.length; i++) {
                src[i] = (byte)i; // doesn't really matter
            }

            // check that our calculated length equals the actual encoded length
            assertEquals(calcCharLength(src.length), enc.encodeToString(src).length());
        }
    }
    
    protected int calcCharLength(int bytes) {
        // For Base64, each encoded character (symbol) can represent 2^6, but our
        // input is 8 bit symbols (binary data). The LCM is 24 bits, corresponding to 3 bytes
        // of input resulting in 4 symbols of output (4 bytes when mapped to UTF-8). Base64
        // pads the output such that the output length is always a multiple of 4-bytes.
        // See https://en.wikipedia.org/wiki/Base64 for a good description on how the padding
        // actually works.
        return (int)Math.ceil(bytes / 3.0) * 4;
    }
}
