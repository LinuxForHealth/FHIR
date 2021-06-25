/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.Integer.of;
import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This integration tests the <b>fhir-operation-reindex</b> $reindex operation.
 *
 * <b>Phase 2</b>:
 * Pre-condition: The Phase 1 loaded Resources which were not previously found.
 * Completes the reindexes with the newly found SearchParameters (extension-search-parameters.json).
 * These SearchParameters include the original FHIR Path Expressions important for the `ChangedExpression`.
 *
 * This test is followed by a Server Side Restart and then Phase3.
 */
public class ReindexOperationPhase2Test extends FHIRServerTestBase {
    private boolean runIt = false;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        runIt = Boolean.parseBoolean(testProperties.getProperty("test.reindex.enabled", "false"));
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_ChangedExpression_Phase2_Reindex() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder()
                .name(string("resourceLogicalId"))
                .value(string("Patient/REIN-DEX-TEST-1"))
                .build());

            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

            Response response = getWebTarget()
                    .path("/$reindex")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .post(entity, Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
            OperationOutcome oo = response.readEntity(OperationOutcome.class);
            assertEquals(oo.getIssue().get(0).getDiagnostics().getValue(), "Processed Patient/REIN-DEX-TEST-1");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {"testReindex_ChangedExpression_Phase2_Reindex"})
    public void testReindex_ChangedExpression_Phase2_Search() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Response response = getWebTarget().path("/Patient")
                    .queryParam("test-code1", "VALUE1")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.readEntity(Bundle.class);
            assertFalse(bundle.getEntry().isEmpty());
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {"testReindex_ChangedExpression_Phase2_Search"})
    public void testReindex_ChangedExpression_Phase2_Reindex_Type() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            List<Parameter> parameters = new ArrayList<>();
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

            Response response = getWebTarget()
                    .path("/Patient/$reindex")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .post(entity, Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
            OperationOutcome oo = response.readEntity(OperationOutcome.class);
            assertEquals(oo.getIssue().get(0).getDiagnostics().getValue(), "Processed Patient/REIN-DEX-TEST-1");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {"testReindex_ChangedExpression_Phase2_Reindex_Type"})
    public void testReindex_ChangedExpression_Phase2_Search_Type() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Response response = getWebTarget().path("/Patient")
                    .queryParam("test-code1", "VALUE1")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.readEntity(Bundle.class);
            assertFalse(bundle.getEntry().isEmpty());
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = { "reindex" }, dependsOnMethods = {"testReindex_ChangedExpression_Phase2_Search_Type"})
    public void testReindexWithInstanceExists_Phase2() {
        if (runIt) {
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
                    .path("/Patient/REIN-DEX-TEST-1/$reindex")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .post(entity, Response.class);

            assertEquals(r.getStatus(), Status.OK.getStatusCode());
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    /**
     * reindex a specific Basic/id resource
     */
    private void reindex(String id) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder()
            .name(string("resourceLogicalId"))
            .value(string("Basic/" + id))
            .build());

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Entity<Parameters> entity = Entity.entity(ps, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget()
                .path("/$reindex")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome oo = response.readEntity(OperationOutcome.class);
        assertEquals(oo.getIssue().get(0).getDiagnostics().getValue(), "Processed Basic/" + id);
    }

    /**
     * checks the basic resource is found
     * @param code
     * @param id
     * @param value
     */
    private void verifyFoundBySearch(String code, String id, String value) {
        // Search and Confirm the search using the 'code' does returns a resource.
        Response response = getWebTarget().path("/Basic")
                .queryParam(code, value)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);
        assertEquals(response.getStatus(), Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertFalse(bundle.getEntry().isEmpty());
        assertEquals(bundle.getEntry().get(0).getId(), id);
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_String_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-STRING");
            verifyFoundBySearch("reindex-string", "REINDEX-STRING", "testString");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Token_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-TOKEN");
            verifyFoundBySearch("reindex-token", "REINDEX-TOKEN", "http://example.org/codesystem|code");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_URI_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-URI");
            verifyFoundBySearch("reindex-uri", "REINDEX-URI", "http://hl7.org/fhir/DSTU2");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Reference_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-REFERENCE");
            verifyFoundBySearch("reindex-reference", "REINDEX-REFERENCE", "Basic/123");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Quantity_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-QUANTITY");
            verifyFoundBySearch("reindex-quantity", "REINDEX-QUANTITY", "25|http://unitsofmeasure.org|s");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Number_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-NUMBER");
            verifyFoundBySearch("reindex-decimal", "REINDEX-NUMBER", "99.99");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Date_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-DATE");
            verifyFoundBySearch("reindex-date", "REINDEX-DATE", "2018-10-29");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Composite_Phase2() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            reindex("REINDEX-COMPOSITE");
            verifyFoundBySearch("reindex-composite", "REINDEX-COMPOSITE", "code$code");
        } else {
            System.out.println("Skipping Phase 2 of Reindex Operation Tests");
        }
    }
}