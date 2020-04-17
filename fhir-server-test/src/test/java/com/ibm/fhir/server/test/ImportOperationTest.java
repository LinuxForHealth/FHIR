/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;

/**
 * These tests exercise the $import operation a bulkdata proposal
 */
public class ImportOperationTest extends FHIRServerTestBase {
    // Test Specific
    public static final String TEST_GROUP_NAME = "import-operation";
    public static final boolean ON = true;
    public static final boolean DEBUG = false;

    // URLs to call against the instance
    public static final String BASE_VALID_URL = "/$import";
    public static final String BASE_VALID_STATUS_URL = "/$bulkdata-status";
    public static final String FORMAT = "application/fhir+ndjson";

    // Cross Test Values
    private String statusUrl;

    public Response doPost(String path, String inputFormat, String inputSource, String resourceType, String url)
            throws FHIRGeneratorException, IOException {
        WebTarget target = getWebTarget();
        target = target.path(path);
        if (DEBUG) {
            System.out.println("URL -> " + target.getUri());
        }
        Parameters parameters = generateParameters(inputFormat, inputSource, resourceType, url);
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        return target.request(FHIRMediaType.APPLICATION_FHIR_JSON).post(entity, Response.class);
    }

    private Parameters generateParameters(String inputFormat, String inputSource, String resourceType, String url)
            throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        // Required: inputFormat
        parameters.add(Parameter.builder().name(string("inputFormat")).value(string(inputFormat)).build());

        // Required: inputSource - where it came from. 
        parameters.add(Parameter.builder().name(string("inputSource")).value(Uri.uri(inputSource)).build());

        // Required: Input Values
        Parameter part1 = Parameter.builder().name(string("type")).value(string(resourceType)).build();
        Parameter part2 = Parameter.builder().name(string("url")).value(Url.of(url)).build();

        // Required: Input
        Parameter inputParameter = Parameter.builder().name(string("input")).part(part1, part2).build();
        parameters.add(inputParameter);

        inputParameter = Parameter.builder().name(string("input")).part(part1, part2).build();
        parameters.add(inputParameter);

        // Optional: Storage Detail
        Parameter storageDetailParameter =
                Parameter.builder().name(string("storageDetail")).value(string("ibm-cos")).build();
        parameters.add(storageDetailParameter);

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(ps, writer);
            System.out.println(writer.toString());
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
        return target.request(mimeType).get(Response.class);
    }

    public Response polling(String statusUrl) throws InterruptedException {
        int status = -1;
        int totalTime = 0;
        Response response = null;
        while (Response.Status.ACCEPTED.getStatusCode() == status) {
            response = doGet(statusUrl, FHIRMediaType.APPLICATION_FHIR_JSON);
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            status   = response.getStatus();

            assertTrue(
                    status == Response.Status.OK.getStatusCode() || status == Response.Status.ACCEPTED.getStatusCode());

            Thread.sleep(5000);
            totalTime += 5000;
            if (totalTime > 10 * 60 * 1000) {
                fail("Too Long a Wait");
            }
        }
        assertEquals(status, Response.Status.OK.getStatusCode());
        return response;
    }

    public void checkStatus() throws InterruptedException {
        Response response = polling(statusUrl);

        // Export finished successfully, we should be about to find the "output" part in the message body
        // which includes all the COS objects download urls.
        String body = response.readEntity(String.class);
        assertTrue(body.contains("output"));
        // Find and try the first download link
        String downloadUrl = body.substring(body.lastIndexOf("\"output\" :"));
        int endIndex = downloadUrl.indexOf(".ndjson") + 7;
        downloadUrl = downloadUrl.substring(downloadUrl.indexOf("https"), endIndex);
        WebTarget client = ClientBuilder.newClient().target(downloadUrl);
        response = client.request().get(Response.class);
        assertEquals(response.getStatus(), 200);
    }

    @Test(groups = { TEST_GROUP_NAME }, enabled = ON)
    public void testImport() throws Exception {
        String path = BASE_VALID_URL;
        String inputFormat = FORMAT;
        String inputSource = "https://localhost:9443/source-fhir-server";
        String resourceType = "Patient";
        String url = "https://s3.us-east.cloud-object-storage.appdomain.cloud/fhir-integration-test/test-import.ndjson";
        Response response = doPost(path, inputFormat, inputSource, resourceType, url);

        String body = response.readEntity(String.class);
        System.out.println(body);
        assertEquals(response.getStatus(), 202);

        // check the content-location that's returned.
        String contentLocation = response.getHeaderString("Content-Location");
        if (DEBUG) {
            System.out.println("Content Location: " + contentLocation);
        }

        assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
        statusUrl = contentLocation;
        checkStatus();
    }

    /*
     * generates the test ndjson
     */
    public static void main(String... args) throws Exception {
        // Create a logical id?
        Patient patientBase = TestUtil.readLocalResource("Patient_JohnDoe.json");
        HumanName.Builder hnBuilder = patientBase.getName().get(0).toBuilder();

        hnBuilder.given(string("import-1"));
        Patient.Builder builder = patientBase.toBuilder().name(Arrays.asList(hnBuilder.build()));
        Patient patient1 = builder.build();

        hnBuilder = patientBase.getName().get(0).toBuilder();
        hnBuilder.given(string("import-2"));
        builder = patientBase.toBuilder().name(Arrays.asList(hnBuilder.build()));
        Patient patient2 = builder.build();

        hnBuilder = patientBase.getName().get(0).toBuilder();
        hnBuilder.given(string("import-3"));
        builder = patientBase.toBuilder().name(Arrays.asList(hnBuilder.build()));
        Patient patient3 = builder.build();

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, false).generate(patient1, writer);
            writer.write("\n");
            FHIRGenerator.generator(Format.JSON, false).generate(patient2, writer);
            writer.write("\n");
            FHIRGenerator.generator(Format.JSON, false).generate(patient3, writer);
            System.out.println(writer.toString());
        }
    }
}