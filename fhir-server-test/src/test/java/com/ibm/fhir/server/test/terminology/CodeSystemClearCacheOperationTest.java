/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.terminology;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Parameters;

/**
 * These tests exercise the $clear-cache operation on a CodeSystem.
 */
public class CodeSystemClearCacheOperationTest extends TerminologyOperationTestBase {

    private static final String SNOMED_CT = "http://snomed.info/sct";
    // Test Specific
    public static final String TEST_GROUP_NAME = "terminology";
    public static final boolean DEBUG = false;

    // URLs to call against the instance
    public static final String BASE_VALID_URL = "/CodeSystem";

    public static final String CODE_SYSTEM_ID = "test";
    public static final String CODE_SYSTEM_URL = "http://ibm.com/fhir/CodeSystem/test";
    public static final String CODE_SYSTEM_VERSION = "1.0.0";

    @Override
    @BeforeClass
    public void setup() throws Exception {
        try (Response response = doPut("CodeSystem", CODE_SYSTEM_ID, "testdata/CodeSystem-test.json")) {
            assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.SUCCESSFUL);
        }
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testClearCacheParameterVariations() throws Exception {
        Response response;

        response = doGet(BASE_VALID_URL + "/$clear-cache");
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        response = doGet(BASE_VALID_URL + "/$clear-cache", "url", CODE_SYSTEM_URL);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        response = doGet(BASE_VALID_URL + "/$clear-cache", "url", CODE_SYSTEM_URL, "codeSystemVersion", CODE_SYSTEM_VERSION);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        clearCache();
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testClearCacheValidateCode() throws Exception {
        Response response;
        Parameters parameters;

        // Make sure the resource is there
        response = doPut("CodeSystem", CODE_SYSTEM_ID, "testdata/CodeSystem-test.json");
        assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.SUCCESSFUL);

        // Once to make sure it is gone from the cache
        clearCache();

        // Twice to see what happens when it is empty
        clearCache();

        // Subsumes to reload the cache
        parameters = validateCode(SNOMED_CT, "K");
        assertEquals(getBooleanParameterValue(parameters, "result"), Boolean.TRUE);

        // Updating the CodeSystem resource makes the cache stale
        response = doPut("CodeSystem", CODE_SYSTEM_ID, "testdata/CodeSystem-test-updated.json");
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        parameters = validateCode(SNOMED_CT, "K");
        assertEquals(getBooleanParameterValue(parameters, "result"), Boolean.FALSE);

        // After cache is cleared, subsumes outcome should be updated
        clearCache();

        parameters = validateCode(SNOMED_CT, "K");
        assertEquals(getBooleanParameterValue(parameters, "result"), Boolean.FALSE);
    }

    private Parameters validateCode(String system, String code) throws Exception {
        Response response = doGet(BASE_VALID_URL + "/" + CODE_SYSTEM_ID + "/$validate-code", "system", system, "code", code);
        String responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        return FHIRParser.parser(Format.JSON).parse(new ByteArrayInputStream(responseBody.getBytes()));
    }

    private void clearCache() {
        Response response = doGet(BASE_VALID_URL + "/" + CODE_SYSTEM_ID + "/$clear-cache");
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }
}
