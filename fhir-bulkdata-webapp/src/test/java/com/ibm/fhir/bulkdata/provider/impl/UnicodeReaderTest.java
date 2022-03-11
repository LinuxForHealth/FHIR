/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import static org.testng.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

/**
 * Tests the FileProvider and S3Provider CountingStream with a variety of single and multibyte characters.
 */
public class UnicodeReaderTest {
    public void test(InputStream in, List<String> outLines) throws IOException {
        try (BufferedInputStream sourceBuffer = new BufferedInputStream(in);
                FileProvider.CountingStream counter = new FileProvider.CountingStream(sourceBuffer)) {
            int r = counter.read();
            while (r != -1) {
                if (counter.eol()) {
                    outLines.add(counter.getLine());
                    counter.resetLine();
                }
                r = counter.read();
            }
        }
    }

    @Test
    public void testUnicode() throws IOException {
        String os = System.getProperty("os.name").toUpperCase();
        if (!os.startsWith("WIN")) {
            return;
        }
        List<String> lines = new ArrayList<>();
        int count = 0;
        StringBuilder inBlob = new StringBuilder();
        StringBuilder line = new StringBuilder();
        for (int codePoint = 32; codePoint <= 0x1F64F; codePoint++) {
            line.append(Character.toChars(codePoint));
            if (count++ % 32 == 0) {
                inBlob.append(line.toString())
                        .append("\n");
                lines.add(Base64.getEncoder().encodeToString(line.toString().getBytes()));
                line = new StringBuilder();
            }
        }
        String out = inBlob.toString();

        List<String> outLines = new ArrayList<>();
        test(new ByteArrayInputStream(out.getBytes()), outLines);
        List<String> outBlob = outLines.stream().map(m -> Base64.getEncoder().encodeToString(m.getBytes()))
                .collect(Collectors.toList());

        count = 0;
        for (String in : lines) {
            if (!outBlob.contains(in)) {
                System.out.println(in);
                System.out.println(outBlob.get(0));
                count++;
            }
        }
        assertEquals(count, 0, "The actual count is not zero, this indicates a bad byte read: " + count);
    }
    
    public void testForS3Stream(InputStream in, List<String> outLines) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (BufferedInputStream sourceBuffer = new BufferedInputStream(in);
                S3Provider.CountingStream counter = new S3Provider.CountingStream(out, sourceBuffer)) {
            String x = counter.readLine();
            while (x != null) {
                outLines.add(counter.getLine());
                x = counter.readLine();
            }
        }
    }

    @Test
    public void testUnicodeS3() throws IOException {
        String os = System.getProperty("os.name").toUpperCase();
        if (!os.startsWith("WIN")) {
            return;
        }
        List<String> lines = new ArrayList<>();
        int count = 0;
        StringBuilder inBlob = new StringBuilder();
        StringBuilder line = new StringBuilder();
        for (int codePoint = 32; codePoint <= 0x1F64F; codePoint++) {
            line.append(Character.toChars(codePoint));
            if (count++ % 32 == 0) {
                inBlob.append(line.toString())
                        .append("\n");
                lines.add(Base64.getEncoder().encodeToString(line.toString().getBytes()));
                line = new StringBuilder();
            }
        }
        String out = inBlob.toString();

        List<String> outLines = new ArrayList<>();
        test(new ByteArrayInputStream(out.getBytes()), outLines);
        List<String> outBlob = outLines.stream().map(m -> Base64.getEncoder().encodeToString(m.getBytes()))
                .collect(Collectors.toList());

        count = 0;
        for (String in : lines) {
            if (!outBlob.contains(in)) {
                System.out.println(in);
                System.out.println(outBlob.get(0));
                count++;
            }
        }
        assertEquals(count, 0, "The actual count is not zero, this indicates a bad byte read: " + count);
    }
}