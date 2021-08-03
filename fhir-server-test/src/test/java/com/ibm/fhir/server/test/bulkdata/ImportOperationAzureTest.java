/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.bulkdata;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.server.test.FHIRServerTestBase;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

/**
 * Azure Blob $import operation Integration Tests
 */
public class ImportOperationAzureTest extends FHIRServerTestBase {
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    // Test Specific
    public static final String TEST_GROUP_NAME = "import-operation";
    private static boolean ON = false;
    public static final boolean DEBUG = false;

    // URLs to call against the instance
    public static final String BASE_VALID_URL = "/$import";
    public static final String BASE_VALID_STATUS_URL = "/$bulkdata-status";
    public static final String FORMAT = "application/fhir+ndjson";
    private final String tenantName = "default";
    private final String dataStoreId = "default";

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.bulkdata.import.azure.enabled", "false"));
        if (!ON) {
            System.out.println("Import Test Disabled, Skipping");
        }
    }

    private Parameters generateParameters(String inputFormat, String inputSource, String resourceType, String url, String provider) throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        // Required: inputFormat
        parameters.add(Parameter.builder().name(string("inputFormat")).value(string(inputFormat)).build());

        // Required: inputSource - where it came from.
        parameters.add(Parameter.builder().name(string("inputSource")).value(Uri.uri(inputSource)).build());

        // Required: Input Values
        Parameter part1 = Parameter.builder().name(string("type")).value(string(resourceType)).build();
        Parameter part2 = Parameter.builder().name(string("url")).value(Url.of(url)).build();

        // Required: Input (relative to bucket)
        Parameter inputParameter = Parameter.builder().name(string("input")).part(part1, part2).build();
        parameters.add(inputParameter);

        // Optional: Storage Detail
        if (provider.equals("default")) {
            Parameter storageDetailParameter = Parameter.builder().name(string("storageDetail")).value(string("azure-blob")).build();
            parameters.add(storageDetailParameter);
        }

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(ps, writer);
            if (DEBUG) {
                System.out.println(writer.toString());
            }
        }
        return ps;
    }

    /**
     * add query parameter list
     */
    public WebTarget addQueryParameterList(WebTarget target, String header, List<String> vals) {
        if (header != null && vals != null && !vals.isEmpty()) {
            target = target.queryParam(header, vals.stream().collect(Collectors.joining(",")));
        }
        return target;
    }

    /**
     * adds the query parameter
     */
    public WebTarget addQueryParameter(WebTarget target, String header, String val) {
        if (header != null && val != null) {
            target = target.queryParam(header, val);
        }
        return target;
    }

    public Response doGet(String path, String mimeType) {
        WebTarget target = getWebTarget();
        target = target.path(path);
        return target.request(mimeType).header("X-FHIR-TENANT-ID", tenantName).header("X-FHIR-DSID", dataStoreId).get(Response.class);
    }

    public Response doPost(String path, String inputFormat, String inputSource, String resourceType, String url) throws FHIRGeneratorException, IOException {
        return doPost(path, inputFormat, inputSource, resourceType, url, "default");
    }

    public Response doPost(String path, String inputFormat, String inputSource, String resourceType, String url, String provider)
        throws FHIRGeneratorException, IOException {
        WebTarget target = getWebTarget();
        target = target.path(path);
        if (DEBUG) {
            System.out.println("URL -> " + target.getUri());
        }
        Parameters parameters = generateParameters(inputFormat, inputSource, resourceType, url, provider);
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        return target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .header("X-FHIR-BULKDATA-PROVIDER", provider)
                .header("X-FHIR-BULKDATA-PROVIDER-OUTCOME", provider)
                .post(entity, Response.class);
    }

    public Response polling(String statusUrl) throws InterruptedException {
        int status = 202;
        int totalTime = 0;
        Response response = null;
        System.out.println("Started Checking");
        while (Response.Status.ACCEPTED.getStatusCode() == status) {
            response = doGet(statusUrl, FHIRMediaType.APPLICATION_FHIR_JSON);
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            status = response.getStatus();

            assertTrue(status == Response.Status.OK.getStatusCode() || status == Response.Status.ACCEPTED.getStatusCode());

            Thread.sleep(5000);
            totalTime += 5000;
            if (totalTime > 10 * 60 * 1000) {
                fail("Too Long a Wait");
            }
        }
        assertEquals(status, Response.Status.OK.getStatusCode());
        System.out.println("Finished Checking");
        return response;
    }

    public void checkValidResponse(Response response) {
        String body = response.readEntity(String.class);
        try(JsonReader reader = JSON_READER_FACTORY.createReader(new ByteArrayInputStream(body.getBytes()))){
            JsonObject obj = reader.readObject();
            assertTrue(obj.containsKey("transactionTime"));
            assertTrue(obj.containsKey("request"));
            assertTrue(obj.containsKey("requiresAccessToken"));
            assertTrue(obj.containsKey("output"));
            assertTrue(obj.containsKey("error"));
        } catch(Exception e) {
            fail("Issue checking and validating", e);
        }
    }

    public void checkOnFourResources() {
        checkOnResource("72b0d93c-93d0-43d9-94a8-d5154ce07152");
        checkOnResource("72b0d93c-93d0-43d9-94a8-d5154ce07153");
        checkOnResource("72b0d93c-93d0-43d9-94a8-d5154ce07154");
        checkOnResource("1772b6bb75a-fd1b2296-6666-4ac1-8b06-f3651eebcc0a");
    }

    /**
     * verify that the resource exists and there is at least one
     * @param id
     */
    public void checkOnResource(String id) {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("_id", id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testImportFromAzure() throws Exception {
        if (ON) {
            String path = BASE_VALID_URL;
            String inputFormat = FORMAT;
            String inputSource = "https://localhost:9443/source-fhir-server";
            String resourceType = "Patient";
            String url = "test-import.ndjson";

            Response response = doPost(path, inputFormat, inputSource, resourceType, url, "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));

            // Check eventual value
            response = polling(contentLocation);
            assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
            checkValidResponse(response);
        }
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testImportFromAzure_FileDoesNotExist() throws Exception {
        if (ON) {
            String path = BASE_VALID_URL;
            String inputFormat = FORMAT;
            String inputSource = "https://localhost:9443/source-fhir-server";
            String resourceType = "Patient";
            String url = "test-import-" + Math.random() + ".ndjson";

            Response response = doPost(path, inputFormat, inputSource, resourceType, url, "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));

            // Check eventual value
            response = pollingFailure(contentLocation);
            assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    public Response pollingFailure(String statusUrl) throws InterruptedException {
        int status = 202;
        int totalTime = 0;
        Response response = null;
        while (Response.Status.ACCEPTED.getStatusCode() == status) {
            response = doGet(statusUrl, FHIRMediaType.APPLICATION_FHIR_JSON);
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            status = response.getStatus();

            assertTrue(status == Response.Status.OK.getStatusCode() || status == Response.Status.ACCEPTED.getStatusCode() || status == Response.Status.BAD_REQUEST.getStatusCode());

            Thread.sleep(5000);
            totalTime += 5000;
            if (totalTime > 10 * 60 * 1000) {
                fail("Too Long a Wait");
            }
        }
        return response;
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testImportFromAzureNegative() throws Exception {
        if (ON) {
            String path = BASE_VALID_URL;
            String inputFormat = FORMAT;
            String inputSource = "https://localhost:9443/source-fhir-server";
            String resourceType = "Patient";
            String url = "test-import-neg.ndjson";

            Response response = doPost(path, inputFormat, inputSource, resourceType, url, "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));

            // Check eventual value
            response = polling(contentLocation);
            assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
            checkValidResponse(response);
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testImportFromAzure" })
    public void testImportCheckQuery() throws Exception {
        if (ON) {
            WebTarget target = getWebTarget();
            Response response = target.path("Patient/1772b6bb75a-fd1b2296-6666-4ac1-8b06-f3651eebcc0a").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            response =
                    target.path("Patient").queryParam("gender", "male").queryParam("_id", "1772b6bb75a-fd1b2296-6666-4ac1-8b06-f3651eebcc0a").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.readEntity(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
        }
    }
}