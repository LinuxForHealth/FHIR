/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Reference;
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

    public static final boolean DEBUG = false;

    /**
     * used when processing Erase on all Resources
     */
    private static class SubmitResourceAndErase implements Callable<Integer> {
        private EraseOperationTest test;
        private String resourceType;

        SubmitResourceAndErase(EraseOperationTest test, String resourceType) {
            this.test = test;
            this.resourceType = resourceType;
        }

        @Override
        public Integer call() throws Exception {
            String logicalId = test.generateCompleteMockResource(resourceType);
            test.eraseResource(resourceType, logicalId, false, "message");
            test.checkResourceNoLongerExists(resourceType, logicalId);
            if (DEBUG) {
                System.out.println("Done testing erase on " + resourceType + "/" + logicalId + "'");
            }
            return 1;
        }
    }

    /**
     * @return a map of String(resource type), String(logical-id)
     */
    private Set<String> generateResourcesForAllTypes() throws Exception {
        Set<String> results = new HashSet<>();
        for (Class<? extends Resource> flz : ModelSupport.getResourceTypes(false)) {
            results.add(flz.getSimpleName());
        }
        return results;
    }

    /**
     * generates a mock resource for the fhir-operation-erase test.
     * @param resourceType
     * @return the logical id
     * @throws Exception
     */
    public String generateMockResource(String resourceType) throws Exception {
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/" + resourceType + "-1.json"))) {
            Resource r = FHIRParser.parser(Format.JSON).parse(example);
            WebTarget target = getWebTarget();
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            try (Response response = target.path(resourceType).request().post(entity, Response.class)) {
                assertResponse(response, Response.Status.CREATED.getStatusCode());
                String id = getLocationLogicalId(response);
                try (Response responseGet = target.path(resourceType + "/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get()) {
                    assertResponse(responseGet, Response.Status.OK.getStatusCode());
                }
                return id;
            }
        }
    }

    /**
     * generates a mock resource for the fhir-operation-erase test.
     * @param resourceType
     * @return the logical id
     * @throws Exception
     */
    public String generateCompleteMockResource(String resourceType) throws Exception {
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/complete-mock/" + resourceType + "-1.json"))) {
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
    private void eraseResource(String resourceType, String logicalId, boolean error, String msg,
            boolean patient, boolean reason, String reasonMsg) {
        Parameters requestBody = generateParameters(patient, reason, reasonMsg, Optional.empty());
        if (DEBUG) {
            System.out.println("Invoking $erase on '" + resourceType + "/" + logicalId +
                    "' with the following: " + requestBody);
        }

        Entity<Parameters> entity = Entity.entity(requestBody, FHIRMediaType.APPLICATION_FHIR_JSON);

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
     * erases the given resource's version
     *
     * @param resourceType
     * @param logicalId
     * @param version - the version to be deleted
     * @param error do you expect an error?
     * @param msg the error message
     * @param patient should include patientId?
     * @param reason should include the reason?
     * @param reasonMsg the string to provide
     */
    private void eraseResourceByVersion(String resourceType, String logicalId, Integer version, boolean error, String msg, boolean patient, boolean reason, String reasonMsg) {
        Entity<Parameters> entity = Entity.entity(generateParameters(patient, reason, reasonMsg, Optional.of(version)), FHIRMediaType.APPLICATION_FHIR_JSON);

        final String requestPath = "/" + resourceType + "/" + logicalId + "/$erase";
        Response r = getWebTarget()
            .path(requestPath)
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .header("X-FHIR-TENANT-ID", "default")
            .header("X-FHIR-DSID", "default")
            .post(entity, Response.class);
        if (error) {
            assertEquals(r.getStatus(), Response.Status.BAD_REQUEST.getStatusCode(), requestPath);
        } else {
            assertEquals(r.getStatus(), Response.Status.OK.getStatusCode(), requestPath);
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
     * @param version the version that is to be erased
     * @return
     */
    public static Parameters generateParameters(boolean patient, boolean reason, String reasonMsg, Optional<Integer> version) {
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

        if (version != null && version.isPresent()) {
            parameters.add(Parameter.builder().name(string("version")).value(com.ibm.fhir.model.type.Integer.of(version.get())).build());
        }

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        return builder.build();
    }

    public Parameters generateParameters(boolean patient, boolean reason, String reasonMsg, int version) {
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
        r = getWebTarget().path("/" + resourceType)
                .queryParam("_id", logicalId)
                .queryParam("_tag", "6aAf1ugE47")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get();
        Bundle bundle = r.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
        assertNotNull(bundle.getTotal().getValue());
        assertEquals((int) bundle.getTotal().getValue(), 0);
    }

    /**
     * checks that the resource exists
     * @param resourceType
     * @param logicalId
     */
    private void checkResourceExists(String resourceType, String logicalId) {
        WebTarget target = getWebTarget();
        target = target.path("/" + resourceType + "/" + logicalId);
        try (Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class)) {
            assertEquals(r.getStatus(), Status.OK.getStatusCode());
        }
        if ("Patient".equals(resourceType)) {
            try (Response r = getWebTarget().path("/" + resourceType)
                    .queryParam("_id", logicalId)
                    .queryParam("_tag", "6aAf1ugE47")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get()) {
                Bundle bundle = r.readEntity(Bundle.class);
                assertNotNull(bundle);
                assertFalse(bundle.getEntry().isEmpty());
                assertEquals(bundle.getEntry().size(), 1);
            }
        } else {
            // must  be structure definition
            try (Response r = getWebTarget().path("/" + resourceType)
                    .queryParam("_id", logicalId)
                    .queryParam("kind", "resource")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get()) {
                Bundle bundle = r.readEntity(Bundle.class);
                assertNotNull(bundle);
                assertFalse(bundle.getEntry().isEmpty());
                assertEquals(bundle.getEntry().size(), 1);
            }
        }
    }

    /**
     * checks the resource is Deleted, not erased
     * @param resourceType
     * @param logicalId
     */
    private void checkResourceDeletedNotErased(String resourceType, String logicalId) {
        WebTarget target = getWebTarget();
        target = target.path("/" + resourceType + "/" + logicalId);
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(r.getStatus(), Status.GONE.getStatusCode());
    }

    /**
     * checks that the resource history does not exist
     * @param resourceType
     * @param logicalId
     * @param version
     */
    private void checkResourceHistoryDoesNotExist(String resourceType, String logicalId, Integer version) {
        WebTarget target = getWebTarget();
        final String resourcePath = "/" + resourceType + "/" + logicalId + "/_history/" + version;
        target = target.path(resourcePath);
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(r.getStatus(), Status.NOT_FOUND.getStatusCode(), resourcePath);
    }

    /**
     * checks that the resource history exists
     * @param resourceType
     * @param logicalId
     * @param version
     * @param status
     */
    private void checkResourceHistoryExists(String resourceType, String logicalId, Integer version, Status status) {
        WebTarget target = getWebTarget();
        target = target.path("/" + resourceType + "/" + logicalId + "/_history/" + version);
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(r.getStatus(), status.getStatusCode());
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
            assertEquals(response.getStatus().getValue(), "" + Status.OK.getStatusCode());
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

        eraseResource("Patient", id, false, "message");
        checkResourceNoLongerExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceExistsWithLogicalId() throws IOException, FHIRParserException {
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

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("patient")).value(string("Patient/1-2-3-4")).build());
        parameters.add(Parameter.builder().name(string("reason")).value(string("Patient has requested an erase")).build());
        parameters.add(Parameter.builder().name(string("id")).value(string(id)).build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);

        Entity<Parameters> entity = Entity.entity(builder.build(), FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget()
            .path("/Patient/$erase")
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .header("X-FHIR-TENANT-ID", "default")
            .header("X-FHIR-DSID", "default")
            .post(entity, Response.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        checkResourceNoLongerExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceExistsWithLogicalIdUsingVersion1() throws IOException, FHIRParserException {
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

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("patient")).value(string("Patient/1-2-3-4")).build());
        parameters.add(Parameter.builder().name(string("reason")).value(string("Patient has requested an erase")).build());
        parameters.add(Parameter.builder().name(string("id")).value(string(id)).build());
        parameters.add(Parameter.builder().name(string("version")).value(com.ibm.fhir.model.type.Integer.of(1)).build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);

        Entity<Parameters> entity = Entity.entity(builder.build(), FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget()
            .path("/Patient/$erase")
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .header("X-FHIR-TENANT-ID", "default")
            .header("X-FHIR-DSID", "default")
            .post(entity, Response.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        checkResourceNoLongerExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceExistsWithLogicalIdUsingVersion1MultipleVersions() throws IOException, FHIRParserException {
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

            // Version 2 (Update)
            r = response.readEntity(Patient.class);
            entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            response = target.path("Patient/" + id).request()
                    .header("X-FHIR-FORCE-UPDATE", "true")
                    .put(entity, Response.class);
        }

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("patient")).value(string("Patient/1-2-3-4")).build());
        parameters.add(Parameter.builder().name(string("reason")).value(string("Patient has requested an erase")).build());
        parameters.add(Parameter.builder().name(string("id")).value(string(id)).build());
        parameters.add(Parameter.builder().name(string("version")).value(com.ibm.fhir.model.type.Integer.of(1)).build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);

        Entity<Parameters> entity = Entity.entity(builder.build(), FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget()
            .path("/Patient/$erase")
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .header("X-FHIR-TENANT-ID", "default")
            .header("X-FHIR-DSID", "default")
            .post(entity, Response.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        checkResourceExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceExistsWithLogicalIdUsingVersion2MultipleVersionsBad() throws IOException, FHIRParserException {
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

            // Version 2 (Update)
            r = response.readEntity(Patient.class);
            entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            response = target.path("Patient/" + id).request()
                    .header("X-FHIR-FORCE-UPDATE", "true")
                    .put(entity, Response.class);
        }

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("patient")).value(string("Patient/1-2-3-4")).build());
        parameters.add(Parameter.builder().name(string("reason")).value(string("Patient has requested an erase")).build());
        parameters.add(Parameter.builder().name(string("id")).value(string(id)).build());
        parameters.add(Parameter.builder().name(string("version")).value(com.ibm.fhir.model.type.Integer.of(2)).build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);

        Entity<Parameters> entity = Entity.entity(builder.build(), FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget()
            .path("/Patient/$erase")
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .header("X-FHIR-TENANT-ID", "default")
            .header("X-FHIR-DSID", "default")
            .post(entity, Response.class);
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        checkResourceExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceExistsByVersionUsingVersion1() throws IOException, FHIRParserException {
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

        eraseResourceByVersion("Patient", id, 1, false, "message", true, true, "message");
        checkResourceNoLongerExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceExistsByVersionUsingVersion1WithMultipleVersions() throws IOException, FHIRParserException {
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

            // Version 2 (Update)
            r = response.readEntity(Patient.class);
            entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            response = target.path("Patient/" + id).request()
                    .header("X-FHIR-FORCE-UPDATE", "true")
                    .put(entity, Response.class);
        }

        eraseResourceByVersion("Patient", id, 1, false, "message", true, true, "message");
        checkResourceExists("Patient", id);
        checkResourceHistoryDoesNotExist("Patient", id, 1);
        checkResourceHistoryExists("Patient", id, 2, Status.OK);
    }

    /**
     * Create a resource with multiple versions. Erase one of the versions, then
     * request history and check that the erased resource is identified as
     * deleted in the returned history bundle.
     * @throws IOException
     * @throws FHIRParserException
     */
    @Test
    public void testHistoryAfterVersionErase() throws IOException, FHIRParserException {
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

            // Version 2 (Update)
            r = response.readEntity(Patient.class);
            entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            response = target.path("Patient/" + id).request()
                    .header("X-FHIR-FORCE-UPDATE", "true")
                    .put(entity, Response.class);
        }

        eraseResourceByVersion("Patient", id, 1, false, "message", true, true, "message");
        checkResourceExists("Patient", id);
        checkResourceHistoryDoesNotExist("Patient", id, 1);
        checkResourceHistoryExists("Patient", id, 2, Status.OK);

        target = target.path("/Patient/" + id + "/_history");
        Response historyResponse = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(historyResponse.getStatus(), 200);
        Bundle historyBundle = historyResponse.readEntity(Bundle.class);
        List<Entry> entries = historyBundle.getEntry();
        assertEquals(entries.size(), 2);
        // This really should be a 201 but we get a 200 instead due to https://github.com/LinuxForHealth/FHIR/issues/3507#issuecomment-1081157116
        assertEquals(entries.get(0).getResponse().getStatus().getValue(), "200");   // V2
        assertEquals(entries.get(0).getRequest().getMethod().getValue(), "PUT");    // V2
        assertEquals(entries.get(1).getResponse().getStatus().getValue(), "200");   // V1
        assertEquals(entries.get(1).getRequest().getMethod().getValue(), "DELETE"); // V1
    }

    /**
     * Create a resource with multiple versions. Erase one of the versions, then
     * request history and check that the erased resource is identified as
     * deleted in the returned history bundle.
     * @throws IOException
     * @throws FHIRParserException
     */
    @Test
    public void testSearchAfterVersionErase() throws IOException, FHIRParserException {
        final WebTarget target = getWebTarget();
        String id = null;
        String conditionId = null;
        Patient r = null;
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
            r = FHIRParser.parser(Format.JSON).parse(example);
            Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            id = getLocationLogicalId(response);
            response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            // Version 2 (Update)
            r = response.readEntity(Patient.class);
            entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            response = target.path("Patient/" + id).request()
                    .header("X-FHIR-FORCE-UPDATE", "true")
                    .put(entity, Response.class);
        }

        // Create a Condition whose subject points to version 1 of the resource we just created
        try (Reader example = ExamplesUtil.resourceReader(("json/ibm/minimal/Condition-1.json"))) {
            Condition condition = FHIRParser.parser(Format.JSON).parse(example);
            // inject a subject reference into the Condition we just read
            condition = condition.toBuilder().subject(Reference.builder().reference("Patient/" + id + "/_history/1").build()).build();
            Entity<Resource> entity = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Condition").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            conditionId = getLocationLogicalId(response);
            response = target.path("Condition/" + conditionId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }

        // Perform a search showing that we can include Patient version 1 before we erase it
        WebTarget searchTarget = target.path("Condition").queryParam("_id", conditionId).queryParam("_include", "Condition:subject");
        Response searchResponse = searchTarget.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(searchResponse.getStatus(), 200);
        Bundle searchBundle = searchResponse.readEntity(Bundle.class);
        List<Entry> entries = searchBundle.getEntry();
        assertEquals(entries.size(), 2);
        assertEquals(entries.get(0).getResource().getId(), conditionId); // the condition
        assertEquals(entries.get(1).getResource().getId(), id); // the included patient

        // Perform the erase of version 1 of the patient
        eraseResourceByVersion("Patient", id, 1, false, "message", true, true, "message");
        checkResourceExists("Patient", id);
        checkResourceHistoryDoesNotExist("Patient", id, 1);
        checkResourceHistoryExists("Patient", id, 2, Status.OK);

        // Repeat the same search now after the erase operation. The patient
        // should no longer be included
        searchResponse = searchTarget.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(searchResponse.getStatus(), 200);
        searchBundle = searchResponse.readEntity(Bundle.class);
        entries = searchBundle.getEntry();
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getResource().getId(), conditionId); // the condition
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

        eraseResource("Patient", id, false, "message");
        checkResourceNoLongerExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceVersionDelete() throws FHIRParserException, IOException {
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

        eraseResourceByVersion("Patient", id, 1, false, "message", true, true, "message");
        checkResourceNoLongerExists("Patient", id);
    }

    @Test
    public void testEraseWhenResourceExistsLatestDeletedWithVersion() throws IOException, FHIRParserException {
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

            // Version 2 (Deleted)
            response = target.path("Patient/" + id).request().delete();
        }

        eraseResourceByVersion("Patient", id, 1, false, "message", true, true, "message");
        checkResourceDeletedNotErased("Patient", id);
        checkResourceHistoryDoesNotExist("Patient", id, 1);
        checkResourceHistoryExists("Patient", id, 2, Status.GONE);
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

            // Version 2 (delete)
            response = target.path("Patient/" + id).request().delete();

            // Version 3 (Update)
            Collection<Extension> exts = Arrays.asList(
                                            Extension.builder()
                                            .url("dummy")
                                            .value(string("Version: " + 3))
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
            entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
            response = target.path("Patient/" + id).request().put(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
        }

        // Version 4, 5 (Update)
        Patient.Builder builder = r.toBuilder();
        for (int i = 4; i <= 6; i++) {
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

        eraseResource("Patient", id, false, "message", true, true, "message");
        checkResourceNoLongerExists("Patient", id);
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
                .resource(generateParameters(true, true, "Need to Remove the file", null))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file", null))
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
                .resource(generateParameters(true, true, "Need to Remove the file", null))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file", null))
            .build());
        Bundle bundle = Bundle.builder()
                .type(BundleType.BATCH)
                .entry(entries)
                .build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        target = target.path("/");
        try (Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "default")
                     .header("X-FHIR-DSID", "default")
                     .post(entity, Response.class)) {
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
                .resource(generateParameters(true, true, "Need to Remove the file", null))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file", null))
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
                .resource(generateParameters(true, true, "Need to Remove the file", null))
                .build());
        entries.add(Bundle.Entry.builder()
            .request(Request.builder()
                    .method(HTTPVerb.POST)
                    .url(Uri.of("Patient/" + patientId2 + "/$erase"))
                    .build())
            .resource(generateParameters(true, true, "Need to Remove the file", null))
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

        Entity<Parameters> entity = Entity.entity(generateParameters(true, true, null, null),
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
        Entity<Parameters> entity = Entity.entity(generateParameters(true, true, null, null),
            FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/Patient/1-2-3-4-5-BAD/$erase")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .post(entity, Response.class);
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
        Entity<Parameters> entity = Entity.entity(generateParameters(true, true, null, null),
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

    /**
     * Acceptance Criteria 14 - $erase does not support GET method
     * When the $erase operation is run with GET method
     * Then result is bad request
     */
    @Test
    public void testGetMethodNotSupported() {
        Response r = getWebTarget()
                .path("/Patient/1-2-3-4-5-BAD/$erase")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .get(Response.class);
       assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
       OperationOutcome outcome = r.readEntity(OperationOutcome.class);
       assertEquals(outcome.getIssue().get(0).getCode().getValue(), "not-supported");
    }

    public void loadLargeBundle(Patient r, String id) {
        WebTarget target = getWebTarget();

        // 1001 is delete block size (+1)
        Patient.Builder builder = r.toBuilder();

        // Originally this made 1000 Bundle.Entry - I chose to use a 100 for IT.
        for (int j = 1; j <= 10; j++) {
            List<Bundle.Entry> entries = new ArrayList<>();
            Bundle.Builder bundleBuilder = Bundle.builder();
            for (int i = 1; i <= 10; i++) {
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
        System.out.println("testEraseOnAllResources is long running... and just started");
        Set<String> allResourcesById = generateResourcesForAllTypes();
        assertFalse(allResourcesById.isEmpty());
        List<SubmitResourceAndErase> callables = new ArrayList<>();
        ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        try {
            for(String entry: allResourcesById) {
                callables.add(new SubmitResourceAndErase(this, entry));
            }
            exec.invokeAll(callables);
        } catch (Exception e) {
            System.out.println();
            e.printStackTrace();
            fail();
        }
        System.out.println("Finished long running test");
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