/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the Job ID Encoding Transformer.
 */
public class BulkDataCommonUtilTest {
    @BeforeMethod
    public void startMethod(Method method) throws FHIRException {

        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();
        context.setTenantId("default");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    @Test
    public void testJobIdEncodingRoundTrip() throws Exception {
        Set<String> encodedIds = new HashSet<>();
        for (int i = 0; i < Math.pow(10, 5); i++) {
            String jobId = String.valueOf(i);

            String encodedJobId = CommonUtil.encodeJobId(i);
            assertNotNull(encodedJobId);
            assertFalse(encodedJobId.equals(jobId));
            assertFalse(encodedJobId.contains("/"), "can't contain a slash");
            assertTrue(encodedIds.add(encodedJobId), "each encoded id must be unique");

            // Ensure all the chars are URL-safe
            assertEquals(encodedJobId, URLDecoder.decode(encodedJobId, StandardCharsets.UTF_8));

            String decodedJobId = CommonUtil.decodeJobId(encodedJobId);
            assertNotNull(decodedJobId);
            assertEquals(decodedJobId, jobId);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testJobIdDecodingInvalid() throws Exception {
        CommonUtil.decodeJobId("bogus");
    }
}