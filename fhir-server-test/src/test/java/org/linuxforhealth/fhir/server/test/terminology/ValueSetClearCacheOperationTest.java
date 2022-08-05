/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.terminology;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * These tests exercise the $clear-cache operation on a ValueSet
 */
public class ValueSetClearCacheOperationTest extends TerminologyOperationTestBase {

    // Test Specific
    public static final String TEST_GROUP_NAME = "terminology";
    public static final boolean DEBUG = false;

    // URLs to call against the instance
    public static final String BASE_VALUE_SET_URL = "/ValueSet";
    public static final String BASE_CODE_SYSTEM_URL = "/CodeSystem";

    /**
     * Check that various parameter combinations work for clearing the cache.
     * 
     * <pre>
     * curl -k -v -X PUT -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/ValueSet-extensional.json 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5'
     *
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/$clear-cache?url=http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1114.7'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/$clear-cache?url=http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1114.7|20200331'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/$clear-cache?url=http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1114.7&valueSetVersion=20200331' 
     *
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5/$clear-cache'
     *
     * </pre>
     */
    @Test(groups = { TEST_GROUP_NAME })
    public void testClearCacheParameterVariations() throws Exception {
        Response response;

        String id = "179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5";
        String url = "http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1114.7";
        String version = "20200331";

        // Initialize the resource
        doPut(BASE_VALUE_SET_URL, id, "testdata/ValueSet-extensional.json");

        // URL only
        response = doGet(BASE_VALUE_SET_URL + "/$clear-cache", "url", url);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        // Canonical URL with version
        response = doGet(BASE_VALUE_SET_URL + "/$clear-cache", "url", url + "|" + version);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        // Version as a separate param
        response = doGet(BASE_VALUE_SET_URL + "/$clear-cache", "url", url, "valueSetVersion", version);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        // Directly on the target resource
        response = doGet(BASE_VALUE_SET_URL + "/" + id + "/$clear-cache");
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    /**
     * Check that the result of validate-code for an extensional valueset is updated after the resource is changed and
     * the cache is cleared.
     * 
     * <pre>
     * curl -k -v -X PUT -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/ValueSet-extensional.json 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5/$validate-code?code=9999&system=http://snomed.info/sct' 
     * 
     * curl -k -v -X PUT -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/ValueSet-extensional-updated.json 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5/$validate-code?code=9999&system=http://snomed.info/sct'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5/$clear-cache'
     *      
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5/$validate-code?code=9999&system=http://snomed.info/sct'
     * 
     * </pre>
     */
    @Test(groups = { TEST_GROUP_NAME })
    public void testExtensionalValidateCodeChangedAfterUpdate() throws Exception {
        Response response;
        String responseBody;
        Resource resource;

        String id = "179814145dd-affc9873-c326-43c3-bf0f-7d7d695644b5";

        // Create the resource initial state
        doPut(BASE_VALUE_SET_URL, id, "testdata/ValueSet-extensional.json");

        // Base Content expansion
        response = doGet(BASE_VALUE_SET_URL + "/" + id + "/$validate-code", "code", "9999", "system", "http://snomed.info/sct");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        resource = parseResource(responseBody);
        assertEquals((boolean) getBooleanParameterValue(resource, "result"), false, responseBody);

        // Update the resource
        doPut(BASE_VALUE_SET_URL, id, "testdata/ValueSet-extensional-updated.json");

        // Updated Content expansion
        response = doGet(BASE_VALUE_SET_URL + "/" + id + "/$validate-code", "code", "9999", "system", "http://snomed.info/sct");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        resource = parseResource(responseBody);
        assertEquals((boolean) getBooleanParameterValue(resource, "result"), false, responseBody);

        clearValueSetCache(id);

        // Should have the correct result now
        response = doGet(BASE_VALUE_SET_URL + "/" + id + "/$validate-code", "code", "9999", "system", "http://snomed.info/sct");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        resource = parseResource(responseBody);
        assertEquals((boolean) getBooleanParameterValue(resource, "result"), true, responseBody);
    }

    /**
     * Check that the expansion of an intensional value set is updated after the value set resource changes. No caching
     * is currently interrupting this operation from getting updated data.
     * 
     * <pre>
     * curl -k -v -X PUT -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/ValueSet-intensional.json 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional/$expand' 
     * 
     * curl -k -v -X PUT -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/ValueSet-intensional-updated.json 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional'
     * </pre>
     */
    @Test(groups = { TEST_GROUP_NAME })
    public void testIntensionalExpansionChangedAfterValueSetUpdate() throws Exception {
        Response response;
        String responseBody;

        String codeSystemId = "test";
        String valueSetId = "test-intensional";

        doPut(BASE_CODE_SYSTEM_URL, codeSystemId, "testdata/CodeSystem-test.json");
        doPut(BASE_VALUE_SET_URL, valueSetId, "testdata/ValueSet-intensional.json");

        // Base Content expansion
        response = doGet(BASE_VALUE_SET_URL + "/" + valueSetId + "/$expand");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        assertFalse(responseBody.contains("Concept A"), responseBody);

        // Update the resource
        doPut(BASE_VALUE_SET_URL, valueSetId, "testdata/ValueSet-intensional-updated.json");

        // Updated Content expansion
        response = doGet(BASE_VALUE_SET_URL + "/" + valueSetId + "/$expand");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        assertTrue(responseBody.contains("Concept A"), responseBody);
    }

    /**
     * Check that the expansion of an intensional value set is updated after the related code system resource changes.
     * 
     * <pre>
     * curl -k -v -X PUT -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/ValueSet-intensional.json 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional/$expand' 
     * 
     * curl -k -v -X PUT -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/CodeSystem-test-updated.json 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional/$expand' 
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional/$clear-cache'
     * 
     * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/ValueSet/test-intensional/$expand'
     * </pre>
     */
    @Test(groups = { TEST_GROUP_NAME })
    public void testIntensionalExpansionChangedAfterCodeSystemUpdate() throws Exception {
        Response response;
        String responseBody;

        String codeSystemId = "test";
        String valueSetId = "test-intensional";

        doPut("/CodeSystem", "test", "testdata/CodeSystem-test.json");
        doPut(BASE_VALUE_SET_URL, valueSetId, "testdata/ValueSet-intensional.json");

        clearCodeSystemCache(codeSystemId);
        clearValueSetCache(valueSetId);

        // Base Content expansion
        response = doGet(BASE_VALUE_SET_URL + "/" + valueSetId + "/$expand");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        assertTrue(responseBody.contains("Concept K"), responseBody);

        // Update the CodeSystem
        doPut("/CodeSystem", "test", "testdata/CodeSystem-test-updated.json");

        // Check the expansion is the same after the codesystem change
        response = doGet(BASE_VALUE_SET_URL + "/" + valueSetId + "/$expand");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        assertTrue(responseBody.contains("Concept K"), responseBody);

        // Clear the ValueSet cache
        clearCodeSystemCache(codeSystemId);
        clearValueSetCache(valueSetId);

        // Check the expansion after the cache is cleared
        response = doGet(BASE_VALUE_SET_URL + "/" + valueSetId + "/$expand");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
        assertFalse(responseBody.contains("Concept K"), responseBody);
        assertTrue(responseBody.contains("Concept ZZ"), responseBody);
    }

    private void clearValueSetCache(String id) {
        clearCache(BASE_VALUE_SET_URL, id);
    }

    private void clearCodeSystemCache(String id) {
        clearCache(BASE_CODE_SYSTEM_URL, id);
    }

    private void clearCache(String baseUrl, String id) {
        Response response;
        String responseBody;
        response = doGet(baseUrl + "/" + id + "/$clear-cache");
        responseBody = response.readEntity(String.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(), responseBody);
    }
}
