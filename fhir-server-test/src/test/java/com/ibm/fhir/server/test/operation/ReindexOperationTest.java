/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.Integer.of;
import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Builder;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This integration tests the <b>fhir-operation-reindex</b> $reindex operation.
 * These reindex test always run.
 */
public class ReindexOperationTest extends FHIRServerTestBase {
    private static final String COUNT_PARAM = "_count";
    private static final String NOT_MODIFIED_AFTER_PARAM = "notModifiedAfter";
    private static final int MAX_RETRIEVE_COUNT = 10;

    private boolean runIt = true;

    @BeforeClass(enabled = false)
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        runIt = Boolean.parseBoolean(testProperties.getProperty("test.reindex.enabled", "false"));
    }

    @Test(groups = { "reindex" })
    public void testReindex() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" })
    public void testReindexWith_GET() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        WebTarget target = getWebTarget();
        target = target.path("/$reindex")
                .queryParam("resourceCount", "5");

        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get();

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" })
    public void testReindexWithType() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/Patient/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" })
    public void testReindexWithInvalidType() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/DomainResource/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.NOT_FOUND.getStatusCode());
    }

    @Test(groups = { "reindex" })
    public void testReindexWithType_BadResourceLogicalIdParameter() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient"))
            .build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/Patient/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" })
    public void testReindexWithInstanceDoesntExist() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/Patient/NOT-EXISTS/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexFromTimestampInstant() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());
        parameters.add(Parameter.builder()
            .name(string("tstamp"))
            .value(string(Instant.now().toString()))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexFromTimestampDayTimeFormat() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        String tstamp =
                zdt.getYear()
                + "-"
                + String.format("%02d", zdt.getMonthValue())
                + "-"
                + String.format("%02d", zdt.getDayOfMonth());

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());
        parameters.add(Parameter.builder()
            .name(string("tstamp"))
            .value(string(tstamp))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexFromTimestampMalformed() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceCount"))
            .value(of(5))
            .build());
        parameters.add(Parameter.builder()
            .name(string("tstamp"))
            .value(string("BAD_STRING"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexUsingUnsupportedGet() {
        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithLogicalIdDoesntExist() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        /*
         * Note, this still passes, and ends up reindexing the default number of resources.
         */
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient/" + UUID.randomUUID()+"Doesntexist"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithSpecificTypeLogicalId() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient/1-2-3-4"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithSpecificTypeMissingLogicalId() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient/"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithSpecificTypeThatDoesntExist() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("BadType"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {})
    public void testReindexWithPatientResourceType() {
        if (!runIt) {
            System.out.println("Skipping over $reindex IT test");
            return;
        }
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Patient"))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
    }

    /**
     * Wrapper for strings.
     * @param str the string
     * @return the string
     */
    protected static com.ibm.fhir.model.type.String str(String str) {
        return com.ibm.fhir.model.type.String.of(str);
    }

    /**
     * Wrapper for integers.
     * @param val the integer
     * @return the integer
     */
    protected static com.ibm.fhir.model.type.Integer intValue(int val) {
        return com.ibm.fhir.model.type.Integer.of(val);
    }

    @Test
    public void testReindex_indexIds() {
        // Allocation of logical_resource_id values (from fhir_sequence) is more complicated
        // now, so we can't depend on valid values being 2,4,6,8,10 etc. Need to perform a
        // $retrieve-index operation first
        final String reindexTimestamp = Instant.now().toString(); // ISO UTC
        Builder builder = Parameters.builder();
        builder.parameter(Parameter.builder().name(str(COUNT_PARAM)).value(intValue(MAX_RETRIEVE_COUNT)).build());
        builder.parameter(Parameter.builder().name(str(NOT_MODIFIED_AFTER_PARAM)).value(str(reindexTimestamp)).build());
        builder.id(UUID.randomUUID().toString());
        Parameters parameters = builder.build();
        

        Entity<Parameters> requestEntity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response r = getWebTarget()
                .path("/$retrieve-index")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(requestEntity, Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        
        // We can turn around and use the Parameters response directly in our reindex call
        Parameters indexIdParams = r.readEntity(Parameters.class);
        assertTrue(indexIdParams.getParameter().size() == 1);
        assertEquals(indexIdParams.getParameter().get(0).getName().getValue(), "indexIds");
        Entity<Parameters> entity = Entity.entity(indexIdParams, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response r2 = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);

        assertEquals(r2.getStatus(), Status.OK.getStatusCode());
    }
}