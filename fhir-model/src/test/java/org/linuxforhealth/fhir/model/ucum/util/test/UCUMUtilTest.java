/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.ucum.util.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.ucum.util.UCUMUtil;

/**
 * Tests for the UCUMUtil class.
 */
public class UCUMUtilTest {

    private static final String UCUM_DIR = "UCUM/";
    private static final String VALID_CODES_FILE = "ucum_common_codes.txt";
    private static final String INVALID_CODES_FILE = "ucum_invalid_codes.txt";

    @Test
    public void testCompileValidStrings() throws Exception {
        List<String> lines = readLines(VALID_CODES_FILE);
        assertTrue(lines.size() > 0);
        int numValid = 0;
        for (String line : lines) {
            if (UCUMUtil.isValidUcum(line)) {
                numValid++;
            }
        }
        assertEquals(numValid, lines.size());
    }

    @Test
    public void testCompileInvalidStrings() throws Exception {
        List<String> lines = readLines(INVALID_CODES_FILE);
        assertTrue(lines.size() > 0);
        int numInvalid = 0;
        for (String line : lines) {
            if (!UCUMUtil.isValidUcum(line)) {
                numInvalid++;
            }
        }
        assertEquals(numInvalid, lines.size());
    }

    /**
     * Returns the lines from the file of UCUM codes.
     * 
     * @param ucumFileName
     *            the file name
     * @return list of UCUM codes
     */
    private static List<String> readLines(String ucumFileName) throws IOException {
        List<String> list = new ArrayList<>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(UCUM_DIR + ucumFileName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            list = br.lines().collect(Collectors.toList());
        }

        return list;
    }
}
