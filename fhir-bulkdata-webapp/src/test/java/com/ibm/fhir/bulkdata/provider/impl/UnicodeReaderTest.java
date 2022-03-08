/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

/**
 * 
 */
public class UnicodeReaderTest {
    @Test
    public void testUnicode() throws IOException {
        List<String> lines = new ArrayList<>();
        int count = 0;
        StringBuilder inBlob = new StringBuilder();
        StringBuilder line = new StringBuilder();
        for (int codePoint = 32; codePoint <= 0x1F64F; codePoint++) {
            line.append(Character.toChars(codePoint));
            if (count++ % 32 == 0) {
                inBlob.append(line.toString())
                    .append("\n");
                lines.add(line.toString());
                line = new StringBuilder();
            }
        }
        String out = inBlob.toString();
        System.out.println(out.getBytes().length);
        
        
        List<String> outLines = new ArrayList<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(out.getBytes());
                FileProvider.CountInputStreamReader cisr = new FileProvider.CountInputStreamReader(bais)){
            String r = cisr.read();
            while(r != null) {
                outLines.add(r);
                r = cisr.readLine();
            }
        }
        
        //System.out.println(outLines);
        String outBlob = outLines.stream().collect(Collectors.joining("\n"));
        System.out.println(outBlob.getBytes().length);
        
        //outLines.stream().forEachOrdered(e -> System.out.println(e));
        
        byte[] ou = outBlob.getBytes();
        byte[] in = inBlob.toString().getBytes();
        for (int i = 0; i < in.length; i++) {
            if (ou.length < in.length && i == ou.length) {
                System.out.println(i + " " + in[i-1]);
            } else if (ou[i] != in[i] && i == outBlob.length()) {
                System.out.println(i);
                System.out.println(Integer.toHexString((int)inBlob.charAt(i)) + " " + Integer.toHexString((int)outBlob.charAt(i)));
            }
        }
        
        

        for (int i = 0; i < inBlob.length(); i++) {
            if (outBlob.length() < inBlob.length() && i == outBlob.length()) {
                System.out.println(i + " " + inBlob.charAt(i));
            } else if (outBlob.charAt(i) != inBlob.charAt(i)) {
                System.out.println(i + " | " + (int)inBlob.charAt(i) + " | " + (int)outBlob.charAt(i));
                System.out.println(Integer.toHexString((int)inBlob.charAt(i)) + " " + Integer.toHexString((int)outBlob.charAt(i)));
            }
        }
        
        
    }
    
    @Test
    public void testInt() {

        //int x = Character.codePointOf("U+1FB00");
        System.out.println("\u1FA00".getBytes().length);
        System.out.println("\u1FA00".getBytes().length);
    }
}
