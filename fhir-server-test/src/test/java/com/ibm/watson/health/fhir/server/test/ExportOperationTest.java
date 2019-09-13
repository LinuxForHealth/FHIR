/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.server.test;

import static com.ibm.watson.health.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.core.FHIRMediaType;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Parameters.Parameter;
import com.ibm.watson.health.fhir.model.type.Id;
import com.ibm.watson.health.fhir.model.type.Instant;

/**
 * @author pbastide
 *
 */
public class ExportOperationTest extends FHIRServerTestBase {

    public static final String TEST_GROUP_NAME = "export-operation";

    public static final String PATIENT_VALID_URL = "Patient/$export";
    public static final String BASE_VALID_URL = "/$export";
    public static final String GROUP_VALID_URL = "Group/$export";

    public static final String FORMAT = "application/fhir+ndjson";

    @Test(groups = { TEST_GROUP_NAME }, enabled = false)
    public void testBaseExport() {
        Response response =
                doPost(BASE_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Patient"), null);
        assertEquals(response.getStatus(), 202);
        
        String contentLocation = response.getHeaderString("Content-Location");
        assertEquals(contentLocation, "https://s3.us-south.cloud-object-storage.appdomain.cloud/fhir-r4-connectathon/job1_Patient_0.ndjson");
        
    }

    public Response doPost(String path, String mimeType, String outputFormat, Instant since,
        List<String> types, List<String> typeFilters) {

        WebTarget target = getWebTarget();

        System.out.println(path);

        target = target.path(path);

        target = addQueryParameter(target, "_outputFormat", outputFormat);
        //target = addQueryParameter(target, "_since", since);
        target = addQueryParameterList(target, "_type", types);
        target = addQueryParameterList(target, "_typeFilter", typeFilters);

        System.out.println("URL -> " + target.getUri());

        Parameters parameters = generateParameters(outputFormat, since, types, null);
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        return target.request(mimeType).post(entity, Response.class);

    }

    /*
     * @param outputFormat
     * @param since
     * @param types
     * @return
     */
    private Parameters generateParameters(String outputFormat, Instant since, List<String> types, List<String> typeFilters) {
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
        builder.id(Id.of(UUID.randomUUID().toString()));
        builder.parameter(parameters);
        return builder.build();
    }

    /**
     * add query parameter list
     * 
     * @param target
     * @param header
     * @param val
     * @return
     */
    public WebTarget addQueryParameterList(WebTarget target, String header, List<String> vals) {
        if (header != null && vals != null && !vals.isEmpty()) {
            target = target.queryParam(header, vals.stream().collect(Collectors.joining(",")));

        }
        return target;
    }

    /**
     * adds the query parameter
     * 
     * @param builder
     * @param header
     * @param vals
     * @return
     */
    public WebTarget addQueryParameter(WebTarget target, String header, String val) {
        if (header != null && val != null) {
            target = target.queryParam(header, val);
        }
        return target;
    }

}
