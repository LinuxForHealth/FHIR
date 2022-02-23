/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.testng.annotations.Test;

import com.ibm.fhir.bulkdata.provider.impl.S3Provider.CountInputStreamReader;
import com.ibm.fhir.exception.FHIRException;

/**
 * Tests the custom CountInputStreamReader for COS/S3
 */
public class S3ProviderStreamReaderTest {

    @Test
    public void testStreamReaderBehavior() throws IOException, FHIRException {
        StringBuilder builder = new StringBuilder();
        for (int idx = 0; idx < 1024; idx++) {
            for (int idxX = 0; idxX < 10243; idxX++) {
                builder.append('A')
                    .append(idxX)
                    .append("_");
            }
            if (idx % 6 == 0) {
                builder.append('\r');
            }
            // System.out.println("Length: " + builder.length());
            builder.append('\n');
        }
        try (ByteArrayInputStream in = new ByteArrayInputStream(builder.toString().getBytes());
                CountInputStreamReader r = new CountInputStreamReader(in)) {
            String line = r.readLine();
            int i = 0;
            while (line != null) {
                // Worth uncommenting when debugging:
                // System.out.println(i + " " + line);
                assertTrue(line.startsWith("A0_"));
                i++;
                line = r.readLine();
            }
            assertEquals(r.getLength(), 62046379);
            assertEquals(i, 1024);
        }
    }
} 