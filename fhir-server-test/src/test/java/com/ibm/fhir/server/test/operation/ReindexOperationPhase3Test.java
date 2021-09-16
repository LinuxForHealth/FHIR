/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

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
 * Phase 3: Completes the reindex changes after Expression Change
 */
public class ReindexOperationPhase3Test extends FHIRServerTestBase {
    private boolean runIt = false;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        runIt = Boolean.parseBoolean(testProperties.getProperty("test.reindex.enabled", "false"));
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_ChangedExpression_Phase3_Reindex() throws IOException, FHIRParserException, FHIRPathException {
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
            System.out.println("Skipping Phase 3 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {"testReindex_ChangedExpression_Phase3_Reindex"})
    public void testReindex_ChangedExpression_Phase3_Search() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Response response = getWebTarget().path("/Patient")
                    .queryParam("test-code1", "VALUE2")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.readEntity(Bundle.class);
            assertFalse(bundle.getEntry().isEmpty());
        } else {
            System.out.println("Skipping Phase 3 of Reindex Operation Tests");
        }
    }
}