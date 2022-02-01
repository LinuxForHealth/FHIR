/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.Integer.of;
import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

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
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This integration tests the <b>fhir-operation-reindex</b> $reindex operation.
 * These reindex test always run.
 */
public class ReindexOperationTest extends FHIRServerTestBase {
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

    @Test
    public void testReindex_indexIds() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(
            Parameter.builder()
                .name(string("indexIds"))
                .value(string("2,4,6,8,10"))
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
}