/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.model.transformer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;

/**
 * Tests the Job ID Encoding Transformer.
 */
public class JobIdEncodingTransformerTest {

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

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
    public void testTransformerRoundTrip() throws Exception {
        // Using the legacy implementation for the configuration the encode/decode uses change-password
        final JobIdEncodingTransformer transformer = JobIdEncodingTransformer.getInstance();

        String jobId = transformer.decodeJobId("1");
        assertNotNull(jobId);
        assertEquals(jobId, "1");

        // This results in at least one case where the naive base64 encoding of the encoded jobId would
        // 1. have a leading '/' which is prohibited by the S3 client; and
        // 2. have consecutive '/' which can makes it harder to get
        for (int i = 0; i < 2000; i++) {
            jobId = String.valueOf(i);

            String encodedJobId = transformer.encodeJobId(jobId);
            assertNotNull(encodedJobId);
            assertFalse(encodedJobId.equals(jobId));
            assertFalse(encodedJobId.startsWith("/"));
            assertFalse(encodedJobId.contains("//"));

            encodedJobId = URLDecoder.decode(encodedJobId, StandardCharsets.UTF_8.toString());
            assertNotNull(encodedJobId);

            String decodedJobId = transformer.decodeJobId(encodedJobId);
            assertNotNull(decodedJobId);
            assertEquals(decodedJobId, jobId);
        }
    }
}