/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.String.string;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * Tests the $erase operation from the module fhir-operation-erase
 *
 * @implNote If Audit is enabled on the server, then there should be audit records generated.
 * However, this code DOES not test the audit records generated.
 */
public class EraseOperationTest extends FHIRServerTestBase {

    private static final String LONG_REASON =
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    /**
     * @return a map of String(resource type), String(logical-id)
     */
    private Map<String,String> generateResourcesForAllTypes() throws Exception {
        Map<String,String> results = new LinkedHashMap<>();
        for (Class<? extends Resource> flz : ModelSupport.getResourceTypes(false)) {
            String resourceType = flz.getSimpleName();
            results.put(resourceType, generateMockResource(resourceType));
        }
        return results;
    }

    /**
     * generates a mock resource for the fhir-operation-erase test.
     * @param resourceType
     * @return the logical id
     * @throws Exception
     */
    private String generateMockResource(String resourceType) throws Exception {
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/" + resourceType + "-1.json"))) {
            Resource r = FHIRParser.parser(Format.JSON).parse(example);
            WebTarget target = getWebTarget();
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path(resourceType).request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            String id = getLocationLogicalId(response);
            response = target.path(resourceType + "/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
            return id;
        }
    }

    /**
     * erases the given resource
     * @param resourceType
     * @param logicalId
     * @param error do you expect an error?
     * @param msg the error message
     */
    private void eraseResource(String resourceType, String logicalId, boolean error, String msg) {
        eraseResource(resourceType, logicalId, error, msg, true, true, null);
    }

    /**
     *
     * erases the given resource
     * @param resourceType
     * @param logicalId
     * @param error do you expect an error?
     * @param msg the error message
     * @param patient should include patientId?
     */
    private void eraseResource(String resourceType, String logicalId, boolean error, String msg, boolean patient) {
        eraseResource(resourceType, logicalId, error, msg, patient, true, null);
    }

    /**
     * erases the given resource
     * @param resourceType
     * @param logicalId
     * @param error do you expect an error?
     * @param msg the error message
     * @param patient should include patientId?
     * @param reason should include the reason?
     */
    private void eraseResource(String resourceType, String logicalId, boolean error, String msg, boolean patient, boolean reason) {
        eraseResource(resourceType, logicalId, error, msg, patient, reason, null);
    }

    /**
     * erases the given resource
     * @param resourceType
     * @param logicalId
     * @param error do you expect an error?
     * @param msg the error message
     * @param patient should include patientId?
     * @param reason should include the reason?
     * @param reasonMsg the string to provide
     */
    private void eraseResource(String resourceType, String logicalId, boolean error, String msg, boolean patient, boolean reason, String reasonMsg) {
        Entity<Parameters> entity = Entity.entity(generateParameters(patient, reason, reasonMsg), FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
            .path("/" + resourceType + "/" + logicalId + "/$erase")
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .header("X-FHIR-TENANT-ID", "default")
            .header("X-FHIR-DSID", "default")
            .post(entity, Response.class);
        if (error) {
            assertEquals(r.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        } else {
            assertEquals(r.getStatus(), Response.Status.OK.getStatusCode());
        }
    }

    /**
     * runs with query parameters
     * @param resourceType
     * @param logicalId
     */
    private void eraseResourceWithQueryParameters(String resourceType, String logicalId) {
        Entity<String> entity = Entity.entity("{\"resourceType\": \"Parameters\"}",
            FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        target = target.path("/" + resourceType + "/" + logicalId + "/$erase")
                .queryParam("patient", "1-2-3-4")
                .queryParam("reason", "patient erasure");
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .post(entity, Response.class);
        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
        r = getWebTarget().path("/" + resourceType + "/" + logicalId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    /**
     * generate parameters object
     * @param patient indicating that the patient parameter should be included
     * @param reason should include the reason?
     * @param reasonMsg the string to provide
     * @return
     */
    public Parameters generateParameters(boolean patient, boolean reason, String reasonMsg) {
        List<Parameter> parameters = new ArrayList<>();
        if (patient) {
            parameters.add(Parameter.builder().name(string("patient")).value(string("Patient/1-2-3-4")).build());
        }

        if (reason) {
            if (reasonMsg == null) {
                parameters.add(Parameter.builder().name(string("reason")).value(string("Patient has requested an erase")).build());
            } else {
                parameters.add(Parameter.builder().name(string("reason")).value(string(reasonMsg)).build());
            }
        }
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        return builder.build();
    }

    /**
     * checks that the resource no longer exists
     * @param resourceType
     * @param logicalId
     */
    private void checkResourceNoLongerExists(String resourceType, String logicalId) {
        WebTarget target = getWebTarget();
        target = target.path("/" + resourceType + "/" + logicalId);
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(r.getStatus(), Status.NOT_FOUND.getStatusCode());
    }

    /**
     * checks that the resource exists
     * @param resourceType
     * @param logicalId
     */
    private void checkResourceExists(String resourceType, String logicalId) {
        WebTarget target = getWebTarget();
        target = target.path("/" + resourceType + "/" + logicalId);
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    /**
     * verifies a bundle
     * @param r
     */
    private void verifyBundle(Response r) {
        Bundle bundle = r.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertFalse(bundle.getEntry().isEmpty());
        for(Bundle.Entry entry : bundle.getEntry()){
            Bundle.Entry.Response response = entry.getResponse();
            assertEquals("200", response.getStatus().getValue());
        }
    }

    /**
     * Acceptance Criteria 1/2: Resource Exists, not Deleted and No Audit
     *
     * GIVEN Patient Resource (ID1)
     *    AND Resource latest is not deleted
     *     AND Audit is disabled
     * WHEN the ID1 is $erase
     * THEN Patient Resource is no longer searchable
     *    AND READ does not return GONE
     *    AND READ does not return the resource
     *    AND READ does returns not found
     *    AND READ does not return a Resource
     *    AND No Audit Record created
     * @throws IOException
     * @throws FHIRParserException
     */
    @Test
    public void testEraseWhenResourceExists() throws IOException, FHIRParserException {
        WebTarget target = getWebTarget();
        String id = null;
        Patient r = null;
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
            r = FHIRParser.parser(Format.JSON).parse(example);
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            id = getLocationLogicalId(response);
            response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }

        // TODO Run  Delete
    }

    /**
     * Acceptance Criteria 3/4: Resource Exists, latest is Deleted
     * GIVEN Patient Resource (ID2)
     *    AND Resource latest is deleted
     * WHEN the ID1 is $erase
     * THEN Patient Resource is no longer searchable
     *    AND READ does not return GONE
     *    AND READ does not return the resource
     *    AND READ does returns not found
     *    AND READ does not return a Resource
     * @throws FHIRParserException
     * @throws IOException
     */
    @Test
    public void testEraseWhenResourceExistsLatestDeleted() throws FHIRParserException, IOException {
        WebTarget target = getWebTarget();
        String id = null;
        Patient r = null;
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
            r = FHIRParser.parser(Format.JSON).parse(example);
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            id = getLocationLogicalId(response);
            response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }

        //  DELETE
    }

    /**
     * Acceptance Criteria 5/6: Resource Exists, latest not Deleted
     *
     * GIVEN Patient Resource v1
     *   AND Resource v2 is created
     *   AND Resource v3 is deleted
     *   AND Resource latest is not deleted
     * WHEN the $erase is called on the resource
     * THEN Patient Resource is no longer searchable
     *    AND READ does not return GONE
     *   AND READ does not return the resource
     *   AND READ does returns not found
     *   AND READ does not return a Resource
     *   AND no History is accessible
     * @throws FHIRParserException
     * @throws IOException
     */
    @Test
    public void testEraseWhenResourceExistsWithMultipleVersionsWithOneDeleted() throws FHIRParserException, IOException {
        WebTarget target = getWebTarget();
        String id = null;
        Patient r = null;
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
            r = FHIRParser.parser(Format.JSON).parse(example);
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            id = getLocationLogicalId(response);
            response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }

        Patient.Builder builder = r.toBuilder();
        for (int i = 1; i <= 2; i++) {
            r = builder.build();
            Collection<Extension> exts = Arrays.asList(
                                            Extension.builder()
                                            .url("dummy")
                                            .value(string("Version: " + i))
                                        .build());
            r = r.toBuilder()
                    .meta(r.getMeta().toBuilder()
                        .id(id)
                        .build())
                    .id(id)
                    .extension(Extension.builder()
                        .url("http://ibm.com/fhir/test")
                        .extension(exts)
                        .build())
                    .build();
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient/" + id).request().put(entity, Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());

            id = getLocationLogicalId(response);
            response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }

        //  TODO  delete

    }

    /**
     * Acceptance Criteria 7b - Run From a Transaction Bundle - One Bad, One Good, Fails
     * Given a Resource is created
     *  AND another Resource that does not exist
     *  AND a Transaction Bundle is created to Post
     *  AND one BAD, one good Bundle.Entry
     * When the $erase operation is run
     * Then result is the resource is no resources are deleted
     *  AND the Response Bundle indicates failure
     * @throws Exception
     */
    @Test
    public void testAsTransactionBundle() throws Exception {
        String patientId1 = generateMockResource("Patient");
        String patientId2 =  "1-2-3-4-5-BAD";
        List<Bundle.Entry> entries = new ArrayList<>();
        entries.add(Bundle.Entry.builder()
                .request(Request.builder()
                        .method(HTTPVerb.POST)
                        .url(Uri.of("Patient/" + patientId1 + "/$erase"))
                        .build())
                .resource(generateParameters(true, true, "Need to Remove the file"))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file"))
            .build());
        Bundle bundle = Bundle.builder()
                .type(BundleType.TRANSACTION)
                .entry(entries)
                .build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        Response r = target.path("/")
                     .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .post(entity, Response.class);
        assertResponse(r, Status.BAD_REQUEST.getStatusCode());

        OperationOutcome responseOutcome = r.readEntity(OperationOutcome.class);
        assertNotNull(responseOutcome);
    }

    /**
     * Acceptance Criteria 7a - Run From a Batch Bundle - One Bad, One Good, Fails
     * Given a Resource is created
     *  AND another Resource that does not exist
     *  AND a Batch Bundle is created to Post
     *  AND one BAD, one good Bundle.Entry
     * When the $erase operation is run
     * Then result is the good resource is deleted
     *  AND the Response Bundle indicates failure with the bad resource
     * @throws Exception
     */
    @Test
    public void testAsBatchBundle() throws Exception {
        String patientId1 = generateMockResource("Patient");
        String patientId2 =  "1-2-3-4-5-BaD";
        List<Bundle.Entry> entries = new ArrayList<>();
        entries.add(Bundle.Entry.builder()
                .request(Request.builder()
                        .method(HTTPVerb.POST)
                        .url(Uri.of("Patient/" + patientId1 + "/$erase"))
                        .build())
                .resource(generateParameters(true, true, "Need to Remove the file"))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file"))
            .build());
        Bundle bundle = Bundle.builder()
                .type(BundleType.BATCH)
                .entry(entries)
                .build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        target = target.path("/");
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .post(entity, Response.class);
        assertResponse(r, Status.OK.getStatusCode());

        Bundle responseBundle = r.readEntity(Bundle.class);
        int countFail = 0;
        int countSuccess = 0;
        int countUnknown = 0;
        for(Bundle.Entry entry : responseBundle.getEntry()) {
            Bundle.Entry.Response response =  entry.getResponse();
            if ("404".equals(response.getStatus().getValue())) {
                // this should be hit when there is a
                countFail++;
            } else if ("200".equals(response.getStatus().getValue())) {
                countSuccess++;
            } else {
                countUnknown++;
            }
        }
        assertEquals(countFail, 1);
        assertEquals(countSuccess, 1);
        assertEquals(countUnknown, 0);
    }

    /**
     * Acceptance Criteria 8b - Run From a Transaction Bundle - All Good
     * Given a Resource is created
     *  AND another Resource that exists
     *  AND a Transaction Bundle is created to Post
     *  AND all good Bundle.Entry
     * When the $erase operation is run
     * Then result is the resource is all Resources are deleted
     *  AND the Response Bundle indicates success
     * @throws Exception
     */
    @Test
    public void testAsTransactionBundleGood() throws Exception {
        String patientId1 = generateMockResource("Patient");
        String patientId2 =  generateMockResource("Patient");
        List<Bundle.Entry> entries = new ArrayList<>();
        entries.add(Bundle.Entry.builder()
                .request(Request.builder()
                        .method(HTTPVerb.POST)
                        .url(Uri.of("Patient/" + patientId1 + "/$erase"))
                        .build())
                .resource(generateParameters(true, true, "Need to Remove the file"))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file"))
            .build());
        Bundle bundle = Bundle.builder()
                .type(BundleType.TRANSACTION)
                .entry(entries)
                .build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        target = target.path("/");
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .post(entity, Response.class);
        assertResponse(r, Status.OK.getStatusCode());
        verifyBundle(r);
    }

    /**
     * Acceptance Criteria 8a - Run From a Batch Bundle - All Good
     * Given a Resource is created
     *  AND another Resource that exists
     *  AND a batch Bundle is created to Post
     *  AND all good Bundle.Entry
     * When the $erase operation is run
     * Then result is the resource is all Resources are deleted
     *  AND the Response Bundle indicates success
     * @throws Exception
     */
    @Test
    public void testAsBatchBundleGood() throws Exception {
        String patientId1 = generateMockResource("Patient");
        String patientId2 =  generateMockResource("Patient");
        List<Bundle.Entry> entries = new ArrayList<>();
        entries.add(Bundle.Entry.builder()
                .request(Request.builder()
                        .method(HTTPVerb.POST)
                        .url(Uri.of("Patient/" + patientId1 + "/$erase"))
                        .build())
                .resource(generateParameters(true, true, "Need to Remove the file"))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file"))
            .build());
        Bundle bundle = Bundle.builder()
                .type(BundleType.BATCH)
                .entry(entries)
                .build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        target = target.path("/");
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .post(entity, Response.class);
        assertResponse(r, Status.OK.getStatusCode());
        verifyBundle(r);
    }

    /**
     * Acceptance Criteria 10 - $erase is not authorized for the given tenant's users
     * Given the Resource ID exists
     *   And Tenant is configured to disallow FHIRUsers
     * When the $erase operation is run
     * Then result is forbidden
     * @throws IOException
     * @throws FHIRParserException
     */
    @Test
    public void testForbiddenResourceExists() throws IOException, FHIRParserException {
        WebTarget target = getWebTarget();
        String id = null;
        Patient r = null;
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
            r = FHIRParser.parser(Format.JSON).parse(example);
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient")
                    .request()
                    .header("X-FHIR-TENANT-ID", "tenant1")
                    .header("X-FHIR-DSID", "profile")
                    .post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            id = getLocationLogicalId(response);

            response = getWebTarget()
                    .path("Patient/" + id)
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "tenant1")
                    .header("X-FHIR-DSID", "profile")
                    .get();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }

        Entity<Parameters> entity = Entity.entity(generateParameters(true, true, null),
            FHIRMediaType.APPLICATION_FHIR_JSON);

        target = target.path("/Patient/" + id + "/$erase");
        Response response = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "tenant1")
                     .header("X-FHIR-DSID", "profile")
                     .post(entity, Response.class);
        assertEquals(response.getStatus(), Status.FORBIDDEN.getStatusCode());


        response = getWebTarget()
                .path("Patient/" + id)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    /**
     * Acceptance Criteria 11 - $erase is not authorized for the given tenant's users
     * Given the Resource ID does not exist
     *   And Tenant is configured to disallow FHIRUsers
     * When the $erase operation is run
     * Then result is forbidden
     */
    @Test
    public void testForbiddenResourceDoesNotExists() {
        Response r = getWebTarget()
                .path("/Patient/1-2-3-4-5-BAD/$erase")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .get(Response.class);
       assertEquals(r.getStatus(), Status.FORBIDDEN.getStatusCode());
       OperationOutcome outcome = r.readEntity(OperationOutcome.class);
       assertEquals(outcome.getIssue().get(0).getCode().getValue(), "forbidden");
    }

    /**
     * Acceptance Criteria 12 - $erase fails as a bad request when resource doesn't exist
     * Given the Resource ID does not Exist (UUID Generate + '-bad'
     * When the $erase operation is run
     * Then result is bad request
     */
    @Test
    public void testEraseWhenResourceDoesNotExist() {
        String resourceType = "Patient";
        String logicalId = "1-2-3-4-5-BAD";
        Entity<Parameters> entity = Entity.entity(generateParameters(true, true, null),
            FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        target = target.path("/" + resourceType + "/" + logicalId + "/$erase");
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .post(entity, Response.class);
        assertEquals(r.getStatus(), Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Acceptance Criteria 13 - $erase a deleted resource
     * Given the Resource ID exist (UUID Generate)
     *    AND the last version of the Resource is deleted
     * When the $erase operation is run
     * Then result is each version of the Resource is deleted
     *    AND the resource is not found
     * @throws Exception
     */
    @Test
    public void testEraseWhenLastResourceVersionDeleted() throws Exception {
        WebTarget target = getWebTarget();
        String id = null;
        Patient r = null;
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
            r = FHIRParser.parser(Format.JSON).parse(example);
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            id = getLocationLogicalId(response);
            response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
            r = response.readEntity(Patient.class);
        }

        loadLargeBundle(r, id);

        // Delete the latest version of the resource
        Response response = target
                .path("Patient/" + id)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .delete(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        eraseResource("Patient", id, false, "Deleting per Patient Request");
        checkResourceNoLongerExists("Patient", id);

    }

    public void loadLargeBundle(Patient r, String id) {
        WebTarget target = getWebTarget();

        // 1001 is delete block size (+1)
        Patient.Builder builder = r.toBuilder();

        for (int j = 1; j <= 10; j++) {
            List<Bundle.Entry> entries = new ArrayList<>();
            Bundle.Builder bundleBuilder = Bundle.builder();
            for (int i = 1; i <= 100; i++) {
                r = builder.build();
                Collection<Extension> exts = Arrays.asList(
                                                Extension.builder()
                                                .url("dummy")
                                                .value(string("Version: " + (j * i)))
                                            .build());
                Patient tmp = r.toBuilder()
                        .meta(r.getMeta().toBuilder()
                            .id(id)
                            .build())
                        .id(id)
                        .extension(Extension.builder()
                            .url("http://ibm.com/fhir/test")
                            .extension(exts)
                            .build())
                        .build();

                Bundle.Entry.Request request = Bundle.Entry.Request.builder()
                        .url(Uri.uri("Patient/" + id))
                        .method(HTTPVerb.PUT)
                        .build();
                Bundle.Entry entry = Bundle.Entry.builder()
                        .request(request)
                        .resource(tmp)
                        .build();
                entries.add(entry);
            }
            Bundle bundle = bundleBuilder
                    .type(BundleType.BATCH)
                    .entry(entries)
                    .build();

            Entity<Resource> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("/").request().post(entity, Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    /**
     * Acceptance Criteria 14 - All Resources work with $erase
     * Given for each Resource type a version of the Resource is created as empty
     * When for each the $erase operation is run
     * Then result is each version of the Resource is deleted
     *    AND the resource is not found
     * @throws Exception
     */
    @Test
    public void testEraseOnAllResources() throws Exception {
        Map<String,String> allResourcesById = generateResourcesForAllTypes();
        assertFalse(allResourcesById.isEmpty());
        for(Entry<String,String> entry : allResourcesById.entrySet()) {
            eraseResource(entry.getKey(), entry.getValue(), false, "message");
            checkResourceNoLongerExists(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Acceptance Criteria 15 - No Patient Parameter Outside of Compartment
     * Given a Resource is created
     *  and the Resource is outside the Patient Compartment
     * When for each the $erase operation is run
     * Then result is the resource is deleted
     *    AND the resource is not found upon a search
     * @throws Exception
     */
    @Test
    public void testEraseWithoutPatientIdOutsideOfPatientCompartment() throws Exception {
        String resourceType = "StructureDefinition";
        String logicalId = generateMockResource(resourceType);
        eraseResource(resourceType, logicalId, false, null, false);
        checkResourceNoLongerExists(resourceType, logicalId);
    }

    /**
     * Acceptance Criteria 16 - No Patient Parameter Inside of Compartment
     * Given a Resource is created
     *  and the Resource is inside the Patient Compartment
     *  and the parameters object does not include patient
     * When for each the $erase operation is run
     * Then result is the resource is NOT deleted
     *    AND the resource is found upon a search
     * @throws Exception
     */
    @Test
    public void testEraseWithoutPatientIdInsideOfPatientCompartment() throws Exception {
        String resourceType = "Patient";
        String logicalId = generateMockResource(resourceType);
        eraseResource(resourceType, logicalId, true, null, false);
        checkResourceExists(resourceType, logicalId);
    }

    /**
     * Acceptance Criteria 16 - No Reason
     * Given a Resource is created
     * When for each the $erase operation is run
     * Then result is the resource is NOT deleted
     *    AND the resource is found upon a search
     *    AND there is a bad request response/status code
     * @throws Exception
     */
    @Test
    public void testEraseWithoutReason() throws Exception {
        String resourceType = "StructureDefinition";
        String logicalId = generateMockResource(resourceType);
        eraseResource(resourceType, logicalId, true, "No Reason Given", false, false);
        checkResourceExists(resourceType, logicalId);
    }

    /**
     * Acceptance Criteria 17 - Too Long a Reason
     * Given a Resource is created
     * When for each the $erase operation is run
     *  AND the reason is 1001 characters
     * Then result is the resource is NOT deleted
     *    AND the resource is found upon a search
     *    AND there is a bad request response/status code
     * @throws Exception
     */
    @Test
    public void testEraseWithTooLongAReason() throws Exception {
        String resourceType = "StructureDefinition";
        String logicalId = generateMockResource(resourceType);
        eraseResource(resourceType, logicalId, true, "No Reason Given", false, true, LONG_REASON);
        checkResourceExists(resourceType, logicalId);
    }

    /**
     * Acceptance Criteria 18 - Empty Parameters Object
     * Given a Resource is created
     * When the $erase operation is run
     *  AND the empty parameters object is run
     *  AND the query parameters are added to the URL
     * Then the result is rejected
     *  AND result is the resource is not deleted
     * @throws Exception
     */
    @Test
    public void testEmptyParametersObject() throws Exception {
        String resourceType = "StructureDefinition";
        String logicalId = generateMockResource(resourceType);
        eraseResourceWithQueryParameters(resourceType, logicalId);
        checkResourceExists(resourceType, logicalId);
    }

    /**
     * Acceptance Criteria 9 - Lots of Versions of the Same Resource which hit a timeout
     * Given the Resource ID exists
     *   And there are at least 1000 versions of the Same Resource
     * When the $erase operation is run
     *  AND result is a partial erase
     *  And the $erase operation is looped to completion
     * Then
     *   The resource does not exist
     * @throws IOException
     * @throws FHIRParserException
     */
    @Test(timeOut = 1200000, enabled = false) // 20 Minutes
    public void testTooManyVersionsForOneErase() throws IOException, FHIRParserException {
        // Timeout of 120s / 200ms per version = 600 resources in the time frame
        // Timeout of 120s / 5ms per version = 24000 resources in the time frame
        // Adding 24K versions of the single resource

        // Trial Runs:
        // 1 - Created 3500 in 600 seconds = 171ms per resource
        // 2 - Created 3501 in 601 seconds = 172ms per resource

        WebTarget target = getWebTarget();
        String id = null;
        Patient r = null;
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
            r = FHIRParser.parser(Format.JSON).parse(example);
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            id = getLocationLogicalId(response);
            response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }

        loadExtraLargeBundle(r, id);

        // Delete the latest version of the resource
        Response response = target
                .path("Patient/" + id)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .delete(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        eraseResource("Patient", id, false, "Deleting per Patient Request");
        checkResourceNoLongerExists("Patient", id);
    }


    public void loadExtraLargeBundle(Patient r, String id) {
        WebTarget target = getWebTarget();

        Patient.Builder builder = r.toBuilder();

        for (int j = 2; j <= 350000; j++) {
            List<Bundle.Entry> entries = new ArrayList<>();
            Bundle.Builder bundleBuilder = Bundle.builder();
            for (int i = 1; i <= 100; i++, j++) {
                r = builder.build();
                Collection<Extension> exts = Arrays.asList(
                                                Extension.builder()
                                                .url("dummy")
                                                .value(string("Version: " + j))
                                            .build());
                Patient tmp = r.toBuilder()
                        .meta(r.getMeta().toBuilder()
                            .id(id)
                            .build())
                        .id(id)
                        .extension(Extension.builder()
                            .url("http://ibm.com/fhir/test")
                            .extension(exts)
                            .build())
                        .build();

                Bundle.Entry.Request request = Bundle.Entry.Request.builder()
                        .url(Uri.uri("Patient/" + id))
                        .method(HTTPVerb.PUT)
                        .build();
                Bundle.Entry entry = Bundle.Entry.builder()
                        .request(request)
                        .resource(tmp)
                        .build();
                entries.add(entry);
            }
            Bundle bundle = bundleBuilder
                    .type(BundleType.BATCH)
                    .entry(entries)
                    .build();

            Entity<Resource> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("/").request().post(entity, Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    /**
     * used to generate a VERY large test data set.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        EraseOperationTest test = new EraseOperationTest();
        test.setUp();
        test.testTooManyVersionsForOneErase();
    }
}