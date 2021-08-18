/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.terminology;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Properties;

import jakarta.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * These tests exercise the $lookup operation on a CodeSystem
 *
 * <pre>
 * curl -k -v -X POST -u "fhiruser:change-password" -H 'Content-Type: application/fhir+json' -d {@literal @}src/test/resources/testdata/CodeSystem.json 'https://localhost:9443/fhir-server/api/v4'
 *
 * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/CodeSystem/$lookup?http://hl7.org/fhir/CodeSystem/example&amp;code=chol'
 *
 * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/CodeSystem/$lookup?http://hl7.org/fhir/CodeSystem/example&amp;code=INVALID'
 *
 * curl -k -u "fhiruser:change-password" -v 'https://localhost:9443/fhir-server/api/v4/CodeSystem/$lookup?system=INVALID&amp;code=chol'
 *
 * </pre>
 */
public class LookupOperationTest extends FHIRServerTestBase {
    private static final String EXAMPLE_SYSTEM = "http://hl7.org/fhir/CodeSystem/example";
    private static final String EXAMPLE_CODE = "chol";

    // Test Specific
    public static final String TEST_GROUP_NAME = "terminology";
    public static final String FORMAT = "application/json";
    public static final boolean DEBUG = false;

    // URLs to call against the instance
    public static final String BASE_VALID_URL = "CodeSystem/$lookup";
    private final String tenantName = "default";
    private final String dataStoreId = "default";

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        setUp(testProperties);

        JsonObject jsonObject = TestUtil.readJsonObject("testdata/CodeSystem.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject,  FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget().path("/CodeSystem/1749c179cd5-bfbb6872-3f47-4f7d-97f6-fc4231e5cba5")
                .request().put(entity,Response.class);
        assertEquals( response.getStatusInfo().getFamily(), Response.Status.Family.SUCCESSFUL );
    }

    public Response doGet(String path, String mimeType, String system, String code) {
        WebTarget target = getWebTarget();
        target = target.path(path);
        return target.queryParam("system", system).queryParam("code", code).request(mimeType)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get(Response.class);
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testLookupBySystemAndCodeValid() throws Exception {
        Response response = doGet(BASE_VALID_URL, FORMAT, EXAMPLE_SYSTEM, EXAMPLE_CODE);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testLookupBySystemAndCodeInvalidSystem() throws Exception {
        Response response = doGet(BASE_VALID_URL, FORMAT, "INVALID", EXAMPLE_CODE);
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        String entity = response.readEntity(String.class);
        assertTrue( entity.contains("not available"), entity );
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testLookupBySystemAndCodeInvalidCode() throws Exception {
        Response response = doGet(BASE_VALID_URL, FORMAT, EXAMPLE_SYSTEM, "INVALID");
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        String entity = response.readEntity(String.class);
        assertTrue( entity.contains("not-found"), entity );
    }
}
