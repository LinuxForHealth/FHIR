/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.operation;

import static org.linuxforhealth.fhir.model.type.Integer.of;
import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

/**
 * This integration tests the <b>fhir-operation-reindex</b> $reindex operation.
 * If there is a lot of data in the system, then this can take some time, over a 120s timeout
 * so this is moved to a separate test that runs in the Integration Test workload
 */
public class ReindexOperationLongRunTest extends FHIRServerTestBase {
    private boolean runIt = false;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        runIt = Boolean.parseBoolean(testProperties.getProperty("test.reindex.enabled", "false"));
    }

    @Test(groups = {"reindex"})
    public void createResources() throws Exception {
        if (runIt) {
            System.out.println("Create Patient Details for Reindex");
            return;
        }
        Bundle patientBundle = TestUtil.readLocalResource("everything-operation/Antonia30_Acosta403.json");
        Entity<Bundle> entity = Entity.entity(patientBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget().request().post(entity, Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertFalse(responseBundle.getEntry().isEmpty());
        for (Entry entry : responseBundle.getEntry()) {
            org.linuxforhealth.fhir.model.resource.Bundle.Entry.Response transactionResponse = entry.getResponse();
            assertEquals(transactionResponse.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
            String[] locationElements = transactionResponse.getLocation().getValue().split("/");
            assertTrue(locationElements.length > 2, "Incorrect location URI format: " + transactionResponse.getLocation());
        }
    }

    @Test(groups = {"reindex" }, dependsOnMethods = {"createResources"})
    public void testReindexCountOverlimit() {
        if (runIt) {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder()
                .name(string("resourceCount"))
                .value(of(1001))
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
        } else {
            System.out.println("Skipping over long run $reindex IT test");
        }
    }
}