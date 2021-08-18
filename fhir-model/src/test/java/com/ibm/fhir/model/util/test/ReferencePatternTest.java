/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test;

import static com.ibm.fhir.model.util.FHIRUtil.REFERENCE_PATTERN;
import static org.testng.Assert.assertEquals;

import java.util.regex.Matcher;

import org.testng.annotations.Test;

public class ReferencePatternTest {
    private static final int BASE_URL_GROUP = 1;
    private static final int RESOURCE_TYPE_GROUP = 4;
    private static final int LOGICAL_ID_GROUP = 5;
    private static final int VERSION_ID_GROUP = 7;

    @Test
    public void testReferencePattern() throws Exception {
        Matcher matcher = REFERENCE_PATTERN.matcher("https://localhost:9443/fhir-server/api/v4/Patient/12345/_history/1");
        assertEquals(matcher.groupCount(), 7);
        if (matcher.matches()) {
            assertEquals(matcher.group(BASE_URL_GROUP), "https://localhost:9443/fhir-server/api/v4/");
            assertEquals(matcher.group(RESOURCE_TYPE_GROUP), "Patient");
            assertEquals(matcher.group(LOGICAL_ID_GROUP), "12345");
            assertEquals(matcher.group(VERSION_ID_GROUP), "1");
        }
    }
}
