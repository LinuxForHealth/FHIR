/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.bulkdata;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.resource.Group;
import org.linuxforhealth.fhir.model.resource.Group.Member;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReaderFactory;

/**
 * These tests exercise the $export operation with azure-blob, a BulkData specification defined operation
 */
public class ExportOperationAzureTest extends FHIRServerTestBase {
    public static final String TEST_GROUP_NAME = "export-operation";
    public static final String PATIENT_VALID_URL = "Patient/$export";
    public static final String GROUP_VALID_URL = "Group/?/$export";
    public static final String BASE_VALID_URL = "/$export";
    public static final String BASE_VALID_STATUS_URL = "/$bulkdata-status";
    public static final String FORMAT_NDJSON = "application/fhir+ndjson";
    public static final String FORMAT_PARQUET = "application/fhir+parquet";
    private final String tenantName = "default";
    private final String dataStoreId = "default";

    // Disabled by default
    private static boolean ON = false;

    public static final boolean DEBUG = false;
    private String exportStatusUrl;
    private String savedPatientId, savedPatientId2;
    private String savedGroupId, savedGroupId2;

    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.bulkdata.export.azure.enabled", "false"));

        if (!ON) {
            throw new SkipException("Not Enabled - Bulk Data Export - Azure");
        }
    }

    public Response doPost(String path, String mimeType, String outputFormat, Instant since, List<String> types, List<String> typeFilters, String provider, String outcome)
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

        //@formatter:off
        return target
                .request(mimeType)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .header("X-FHIR-BULKDATA-PROVIDER", provider)
                .header("X-FHIR-BULKDATA-PROVIDER-OUTCOME", outcome)
                .post(entity, Response.class);
        //@formatter:on
    }

    /**
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @return
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    private Parameters generateParameters(String outputFormat, Instant since, List<String> types, List<String> typeFilters) throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        if (outputFormat != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_outputFormat"))
                    .value(string(outputFormat))
                    .build());
            //@formatter:on
        }

        if (since != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_since"))
                    .value(since)
                    .build());
            //@formatter:on
        }

        if (types != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_type"))
                    .value(string(types.stream().collect(Collectors.joining(","))))
                    .build());
            //@formatter:on
        }

        if (typeFilters != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_typeFilters"))
                    .value(string(types.stream().collect(Collectors.joining(","))))
                    .build());
            //@formatter:on
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

    public Response doGet(String path, String mimeType, String provider, String outcome) {
        WebTarget target = getWebTarget();
        target = target.path(path);
        // @formatter:off
        return target.request(mimeType)
                     .header("X-FHIR-TENANT-ID", tenantName)
                     .header("X-FHIR-DSID", dataStoreId)
                     .header("X-FHIR-BULKDATA-PROVIDER", provider)
                     .header("X-FHIR-BULKDATA-PROVIDER-OUTCOME", outcome)
                     .get(Response.class);
        // @formatter:on
    }

    private void checkExportStatus(boolean isCheckPatient, List<String> types) throws Exception {
        Response response;
        do {
            response = doGet(exportStatusUrl, FHIRMediaType.APPLICATION_FHIR_JSON, "default", "default");
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            assertEquals(Status.Family.familyOf(response.getStatus()), Status.Family.SUCCESSFUL);
            Thread.sleep(1000);
        } while (response.getStatus() == Response.Status.ACCEPTED.getStatusCode());

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        // Export finished successfully, we should be about to find the "output" part in the message body
        // which includes all the objects download urls.
        String body = response.readEntity(String.class);
        if (DEBUG) {
            System.out.println(body);
        }

        if (isCheckPatient) {
            assertTrue(body.contains("Patient_1.ndjson"));
        }

        assertTrue(body.contains("output"));
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testCreateTwoPatients() throws Exception {
        WebTarget target = getWebTarget();
        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        Response response =
                target.path("Patient")
                    .request()
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .header("X-FHIR-DSID", dataStoreId)
                    .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the patient's logical id value.
        savedPatientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        //@formatter:off
        response = target.path("Patient/"+ savedPatientId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Create 2nd Patient.
        entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        response = target.path("Patient")
                            .request()
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the 2nd patient's logical id value.
        savedPatientId2 = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        //@formatter:off
        response = target.path("Patient/"+ savedPatientId2)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testCreateTwoPatients" })
    public void testCreateResourceForPatients() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create an Observation for patient1.
        Observation observation = TestUtil.buildPatientObservation(savedPatientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        Response response =
                target.path("Observation")
                      .request()
                      .header("X-FHIR-TENANT-ID", tenantName)
                      .header("X-FHIR-DSID", dataStoreId)
                      .post(obs, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String observationId = getLocationLogicalId(response);
        //@formatter:off
        response = target.path("Observation/"+ observationId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Create a Condition for patient2.
        Condition condition = buildCondition(savedPatientId2, "Condition.json");
        Entity<Condition> cdt = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        response = target.path("Condition")
                            .request()
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .post(cdt, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String conditionId = getLocationLogicalId(response);
        //@formatter:off
        response = target.path("Condition/"+ conditionId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testCreateResourceForPatients" })
    public void testGroup() throws Exception {
        WebTarget target = getWebTarget();

        // (1) Build a new Group.
        Group group = TestUtil.readExampleResource("json/spec/group-example-member.json");
        Entity<Group> entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        Response response =
                target.path("Group")
                        .request()
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        savedGroupId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new group and verify it.
        //@formatter:off
        response = target.path("Group/" + savedGroupId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());
        Group responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 4);

        // (2) Build a new Group with patient2 and the above group only as members.
        ArrayList<Member> members = new ArrayList<>();
        group = group.toBuilder().member(members).build();
        //@formatter:off
        Member member = Member.builder()
                                .entity(
                                    Reference.builder()
                                        .reference(string("Patient/" + savedPatientId2))
                                        .build())
                                .build();
        //@formatter:on
        members.add(member);
        //@formatter:off
        member = Member.builder()
                            .entity(Reference.builder()
                                        .reference(string("Group/" + savedGroupId))
                                        .build())
                            .build();
        //@formatter:on
        members.add(member);
        group = group.toBuilder().member(members).build();
        entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        response = target.path("Group")
                            .request()
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the group logical id value.
        savedGroupId2 = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new group and verify it.
        //@formatter:off
        response = target.path("Group/" + savedGroupId2)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .get();
        //@formatter:off
        assertResponse(response, Response.Status.OK.getStatusCode());
        responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 2);

        // (3) Modify the first group to contain the second group and patient1 only, this creates a circle reference
        // situation.
        members.clear();
        //@formatter:off
        member = Member.builder()
                    .entity(Reference.builder()
                        .reference(string("Patient/" + savedPatientId))
                        .build())
                    .build();
        //@formatter:on
        members.add(member);

        //@formatter:off
        member = Member.builder()
                    .entity(Reference.builder()
                        .reference(string("Group/" + savedGroupId2))
                        .build())
                    .build();
        //@formatter:on
        members.add(member);
        group = group.toBuilder().id(savedGroupId).member(members).build();

        // Update the patient and verify the response.
        entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        response = target.path("Group/" + savedGroupId).request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .put(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new group and verify it.
        //@formatter:off
        response = target.path("Group/" + savedGroupId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());
        responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 2);
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" }, enabled = true)
    public void testSystemExportToAzure() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Patient");
            Response response = doPost(
                    BASE_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON,
                    FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(false, types);
        } else {
            System.out.println("System Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testPatientExportToAzure() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Observation", "Condition", "Patient");
            Response response = doPost(
                    PATIENT_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(true, types);
        } else {
            System.out.println("Patient Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testGroupExportToAzure() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Patient", "Group", "Condition", "Observation");
            Response response = doPost(
                    GROUP_VALID_URL.replace("?", savedGroupId2),
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkGroupExportStatus(types);
        } else {
            System.out.println("Group Export Test Disabled, Skipping");
        }
    }

    private void checkGroupExportStatus(List<String> types) throws Exception {
        Response response;
        do {
            response = doGet(exportStatusUrl, FHIRMediaType.APPLICATION_FHIR_JSON, "default", "default");
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            assertEquals(Status.Family.familyOf(response.getStatus()), Status.Family.SUCCESSFUL);
            Thread.sleep(5000);
        } while (response.getStatus() == Response.Status.ACCEPTED.getStatusCode());

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        // Export finished successfully, we should be about to find the "output" part in the message body
        // which includes all the objects download urls.
        String body = response.readEntity(String.class);
        JsonObject jsonObject = JSON_READER_FACTORY.createReader(new StringReader(body)).readObject();
        assertTrue(jsonObject.containsKey("output"));
    }
}