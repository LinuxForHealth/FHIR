/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.ibm.fhir.model.type.Instant;

/**
 * These tests exercise the $export operation, a BulkData specification defined operation.
 * 
 * @author pbastide
 *
 */
public class ExportOperationTest extends FHIRServerTestBase {

    public static final String TEST_GROUP_NAME = "export-operation";

    public static final String PATIENT_VALID_URL = "Patient/$export";
    public static final String GROUP_VALID_URL = "Group/$export";
    public static final String BASE_VALID_URL = "/$export";

    public static final String BASE_VALID_STATUS_URL = "/$export-status";

    public static final String FORMAT = "application/fhir+ndjson";

    public static final boolean ON = false;
    
    @Test(groups = { TEST_GROUP_NAME }, enabled = ON)
    public void testBaseExport() throws FHIRGeneratorException, IOException {
        Response response =
            doPost(BASE_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Patient"), null);
        assertEquals(response.getStatus(), 202);

        // Debug the content-location that's returned. 
        String contentLocation = response.getHeaderString("Content-Location");
        System.out.println("Content Location: " + contentLocation);
        
        assertEquals(contentLocation, "/fhir-server/api/v4/$export-status?job=1");

    }

    public Response doPost(String path, String mimeType, String outputFormat, Instant since,
        List<String> types, List<String> typeFilters) throws FHIRGeneratorException, IOException {

        WebTarget target = getWebTarget();

        System.out.println(path);

        target = target.path(path);

        target = addQueryParameter(target, "_outputFormat", outputFormat);
        // target = addQueryParameter(target, "_since", since);
        target = addQueryParameterList(target, "_type", types);
        target = addQueryParameterList(target, "_typeFilter", typeFilters);

        System.out.println("URL -> " + target.getUri());

        Parameters parameters = generateParameters(outputFormat, since, types, null);
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        return target.request(mimeType).post(entity, Response.class);

    }

    /*
     * 
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @return
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    private Parameters generateParameters(String outputFormat, Instant since, List<String> types,
        List<String> typeFilters) throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        if (outputFormat != null) {
            parameters.add(Parameter.builder().name(string("_outputFormat")).value(string(outputFormat)).build());
        }

        if (since != null) {
            parameters.add(Parameter.builder().name(string("_since")).value(since).build());
        }

        if (types != null) {
            parameters.add(Parameter.builder().name(string("_type")).value(string(types.stream().collect(Collectors.joining(",")))).build());
        }

        if (typeFilters != null) {
            parameters.add(Parameter.builder().name(string("_typeFilters")).value(string(types.stream().collect(Collectors.joining(",")))).build());
        }

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

    @Test(groups = { TEST_GROUP_NAME }, enabled = ON, dependsOnMethods = {})
    public void testBaseExportStatus() {
        String jobInstance = "1";

        Response response =
                doGet(BASE_VALID_STATUS_URL, FHIRMediaType.APPLICATION_FHIR_JSON, jobInstance);
        assertEquals(response.getStatus(), 200);

        // String contentLocation = response.getHeaderString("Content-Location");
        // assertEquals(contentLocation,
        // "https://s3.us-south.cloud-object-storage.appdomain.cloud/fhir-r4-connectathon/job1/Patient_1.ndjson");

    }

    public Response doGet(String path, String mimeType, String jobInstance) {

        WebTarget target = getWebTarget();

        System.out.println(path);

        target = target.path(path);

        target = addQueryParameter(target, "job", jobInstance);

        System.out.println("URL -> " + target.getUri());

        return target.request(mimeType).get(Response.class);

    }
}
