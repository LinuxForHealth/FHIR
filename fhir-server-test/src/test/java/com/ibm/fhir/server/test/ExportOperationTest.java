/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
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
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Reference;

/**
 * These tests exercise the $export operation, a BulkData specification defined operation
 *
 */
public class ExportOperationTest extends FHIRServerTestBase {
    public static final String TEST_GROUP_NAME = "export-operation";
    public static final String PATIENT_VALID_URL = "Patient/$export";
    public static final String GROUP_VALID_URL = "Group/?/$export";
    public static final String BASE_VALID_URL = "/$export";
    public static final String BASE_VALID_STATUS_URL = "/$export-status";
    public static final String FORMAT = "application/fhir+ndjson";
    public static final boolean ON = true;

    public static final boolean DEBUG = false;
    private String exportStatusUrl;
    private String savedPatientId, savedPatientId2;
    private String savedGroupId, savedGroupId2;

    public Response doPost(String path, String mimeType, String outputFormat, Instant since,
        List<String> types, List<String> typeFilters) throws FHIRGeneratorException, IOException {
        WebTarget target = getWebTarget();
        target = target.path(path);
        target = addQueryParameter(target, "_outputFormat", outputFormat);
        // target = addQueryParameter(target, "_since", since);
        target = addQueryParameterList(target, "_type", types);
        target = addQueryParameterList(target, "_typeFilter", typeFilters);
        if (DEBUG) {
            System.out.println("URL -> " + target.getUri());
        }
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
        return target.request(mimeType).get(Response.class);
    }

    private void checkBaseExportStatus() throws InterruptedException {
        Response response;
        do {
            response =
                doGet(exportStatusUrl, FHIRMediaType.APPLICATION_FHIR_JSON);
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            assertTrue(response.getStatus() == 200 || response.getStatus() == 202);
            Thread.sleep(5000);
        } while (response.getStatus() == 202);

        assertEquals(response.getStatus(), 200);
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

    @Test(groups = { TEST_GROUP_NAME })
    public void testCreate2Patients() throws Exception {
        WebTarget target = getWebTarget();
        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the patient's logical id value.
        savedPatientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + savedPatientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Create 2nd Patient.
        entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the 2nd patient's logical id value.
        savedPatientId2 = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + savedPatientId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods={"testCreate2Patients"})
    public void testCreateResource4Patients() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create an Observation for patient1.
        Observation observation = TestUtil.buildPatientObservation(savedPatientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String observationId = getLocationLogicalId(response);
        response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Create a Condition for patient2.
        Condition condition = buildCondition(savedPatientId2, "Condition.json");
        Entity<Condition> cdt = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Condition").request().post(cdt, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String conditionId = getLocationLogicalId(response);
        response = target.path("Condition/" + conditionId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods={"testCreateResource4Patients"})
    public void testGroup() throws Exception {
        WebTarget target = getWebTarget();

        // (1) Build a new Group.
        Group group = TestUtil.readExampleResource("json/spec/group-example-member.json");
        Entity<Group> entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Group").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        savedGroupId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new group and verify it.
        response = target.path("Group/" + savedGroupId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Group responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 4);

        // (2) Build a new Group with patient2 and the above group only as members.
        ArrayList<Member> members = new ArrayList<>();
        group = group.toBuilder().member(members).build();
        Member member = Member.builder().entity(Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Patient/" + savedPatientId2)).build())
                .build();
        members.add(member);
        member = Member.builder().entity(Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Group/" + savedGroupId)).build())
                .build();
        members.add(member);
        group = group.toBuilder().member(members).build();
        entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Group").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the group logical id value.
        savedGroupId2 = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new group and verify it.
        response = target.path("Group/" + savedGroupId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 2);

        // (3) Modify the first group to contain the second group and patient1 only, this creates a circle reference situation.
        members.clear();
        member = Member.builder().entity(Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Patient/" + savedPatientId)).build())
                .build();
        members.add(member);
        member = Member.builder().entity(Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Group/" + savedGroupId2)).build())
                .build();
        members.add(member);
        group = group.toBuilder().id(savedGroupId).member(members).build();

        // Update the patient and verify the response.
        entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Group/" + savedGroupId).request().put(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new group and verify it.
        response = target.path("Group/" + savedGroupId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 2);
    }

    @Test(groups = { TEST_GROUP_NAME }, enabled = ON)
    public void testBaseExport() throws Exception {
        Response response =
            doPost(BASE_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Patient"), null);
        assertEquals(response.getStatus(), 202);

        // check the content-location that's returned.
        String contentLocation = response.getHeaderString("Content-Location");
        if (DEBUG) {
            System.out.println("Content Location: " + contentLocation);
        }

        assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
        exportStatusUrl = contentLocation;
        checkBaseExportStatus();
    }


    @Test(groups = { TEST_GROUP_NAME }, enabled = ON)
    public void testPatientExport() throws Exception {
        Response response =
            doPost(PATIENT_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Observation,Condition"), null);
        assertEquals(response.getStatus(), 202);

        // check the content-location that's returned.
        String contentLocation = response.getHeaderString("Content-Location");
        if (DEBUG) {
            System.out.println("Content Location: " + contentLocation);
        }

        assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
        exportStatusUrl = contentLocation;
        checkBaseExportStatus();
    }

    @Test(groups = { TEST_GROUP_NAME }, enabled = ON, dependsOnMethods={"testGroup"})
    public void testGroupExport() throws Exception {
        Response response =
            doPost(GROUP_VALID_URL.replace("?", savedGroupId2), FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), null, null);
        assertEquals(response.getStatus(), 202);

        // check the content-location that's returned.
        String contentLocation = response.getHeaderString("Content-Location");
        if (DEBUG) {
            System.out.println("Content Location: " + contentLocation);
        }

        assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
        exportStatusUrl = contentLocation;
        checkBaseExportStatus();
    }
}
