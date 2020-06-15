/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test.bulkdata;

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
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
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
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * These tests exercise the $export operation, a BulkData specification defined operation
 */
public class ExportOperationTest extends FHIRServerTestBase {
    public static final String TEST_GROUP_NAME = "export-operation";
    public static final String PATIENT_VALID_URL = "Patient/$export";
    public static final String GROUP_VALID_URL = "Group/?/$export";
    public static final String BASE_VALID_URL = "/$export";
    public static final String BASE_VALID_STATUS_URL = "/$bulkdata-status";
    public static final String FORMAT = "application/fhir+ndjson";

    // Disabled by default
    private static boolean ON = false;

    public static final boolean DEBUG = false;
    private String exportStatusUrl;
    private String savedPatientId, savedPatientId2;
    private String savedGroupId, savedGroupId2;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.bulkdata.export.enabled", "false"));
    }

    public Response doPost(String path, String mimeType, String outputFormat, Instant since, List<String> types, List<String> typeFilters)
        throws FHIRGeneratorException, IOException {
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
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @return
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    private Parameters generateParameters(String outputFormat, Instant since, List<String> types, List<String> typeFilters)
        throws FHIRGeneratorException, IOException {
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

    private void checkExportStatus(boolean isCheckPatient) throws InterruptedException {
        Response response;
        do {
            response = doGet(exportStatusUrl, FHIRMediaType.APPLICATION_FHIR_JSON);
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() || response.getStatus() == Response.Status.ACCEPTED.getStatusCode());
            Thread.sleep(5000);
        } while (response.getStatus() == Response.Status.ACCEPTED.getStatusCode());

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        // Export finished successfully, we should be about to find the "output" part in the message body
        // which includes all the COS objects download urls.
        String body = response.readEntity(String.class);
        if (DEBUG) {
            System.out.println(body);
        }

        if (isCheckPatient) {
            assertTrue(body.contains("Patient_1.ndjson"));
        }

        assertTrue(body.contains("output"));
        // Find and try the first download link
        String downloadUrl = body.substring(body.lastIndexOf("\"output\":"));
        int endIndex = downloadUrl.indexOf(".ndjson") + 7;
        downloadUrl = downloadUrl.substring(downloadUrl.indexOf("https"), endIndex);
        WebTarget client = ClientBuilder.newClient().target(downloadUrl);
        response = client.request().get(Response.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testCreateTwoPatients() throws Exception {
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

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testCreateTwoPatients" })
    public void testCreateResourceForPatients() throws Exception {
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

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testCreateResourceForPatients" })
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
        Member member = Member.builder().entity(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Patient/" + savedPatientId2)).build()).build();
        members.add(member);
        member = Member.builder().entity(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Group/" + savedGroupId)).build()).build();
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

        // (3) Modify the first group to contain the second group and patient1 only, this creates a circle reference
        // situation.
        members.clear();
        member = Member.builder().entity(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Patient/" + savedPatientId)).build()).build();
        members.add(member);
        member = Member.builder().entity(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Group/" + savedGroupId2)).build()).build();
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

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testBaseExport() throws Exception {
        if (ON) {
            Response response =
                    doPost(BASE_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Patient"), null);
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(false);
        } else {
            System.out.println("Base Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testPatientExport() throws Exception {
        if (ON) {
            Response response =
                    doPost(PATIENT_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Observation,Condition,Patient"), null);
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(true);
        } else {
            System.out.println("Patient Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testGroupExport() throws Exception {
        if (ON) {
            Response response =
                    doPost(GROUP_VALID_URL.replace("?", savedGroupId2), FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT, Instant.of("2019-01-01T08:21:26.94-04:00"), null, null);
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkGroupExportStatus();
        } else {
            System.out.println("Group Export Test Disabled, Skipping");
        }
    }

    private void checkGroupExportStatus() throws InterruptedException {
        Response response;
        do {
            response = doGet(exportStatusUrl, FHIRMediaType.APPLICATION_FHIR_JSON);
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() || response.getStatus() == Response.Status.ACCEPTED.getStatusCode());
            Thread.sleep(5000);
        } while (response.getStatus() == Response.Status.ACCEPTED.getStatusCode());

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        // Export finished successfully, we should be about to find the "output" part in the message body
        // which includes all the COS objects download urls.
        String body = response.readEntity(String.class);
        assertTrue(body.contains("output"));
        // Find and try the first download link
        String downloadUrl = body.substring(body.lastIndexOf("\"output\":"));
        int endIndex = downloadUrl.indexOf(".ndjson") + 7;
        downloadUrl = downloadUrl.substring(downloadUrl.indexOf("https"), endIndex);
        if (DEBUG) {
            System.out.println("downloadUrl = " + downloadUrl);
        }
        WebTarget client = ClientBuilder.newClient().target(downloadUrl);
        response = client.request().get(Response.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        // Verify to make sure there are Groups, Condition and Observation in the output
        // (1) Verify that there is one condition exported
        assertTrue(body.contains("Condition_1.ndjson"));
        String conditionStr = body.substring(body.lastIndexOf("Condition_1.ndjson"));
        conditionStr = conditionStr.substring(0, conditionStr.indexOf("}") + 1);
        assertTrue(conditionStr.contains("\"count\": 1"));
        // (2) Verify that there is one observation exported
        assertTrue(body.contains("Observation_1.ndjson"));
        String observationStr = body.substring(body.lastIndexOf("Observation_1.ndjson"));
        observationStr = observationStr.substring(0, observationStr.indexOf("}") + 1);
        assertTrue(observationStr.contains("\"count\": 1"));
        // (3) Verify that there are 2 groups exported
        assertTrue(body.contains("Group_1.ndjson"));
        String groupStr = body.substring(body.lastIndexOf("Group_1.ndjson"));
        groupStr = groupStr.substring(0, groupStr.indexOf("}") + 1);
        assertTrue(groupStr.contains("\"count\": 2"));
        // (4) Verify that there are 2 patients exported
        assertTrue(body.contains("Patient_1.ndjson"));
        String patientStr = body.substring(body.lastIndexOf("Patient_1.ndjson"));
        patientStr = patientStr.substring(0, patientStr.indexOf("}") + 1);
        assertTrue(patientStr.contains("\"count\": 2"));
    }
}