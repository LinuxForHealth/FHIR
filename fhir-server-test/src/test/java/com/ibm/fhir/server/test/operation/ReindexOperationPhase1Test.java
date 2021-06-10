/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This integration tests the <b>fhir-operation-reindex</b> $reindex operation.
 * Phase 1: Populate and Confirm it's not found
 * Followed by Server Side Restart
 */
public class ReindexOperationPhase1Test extends FHIRServerTestBase {

    private boolean runIt = false;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        runIt = Boolean.parseBoolean(testProperties.getProperty("test.reindex.enabled", "false"));
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_ChangedExpression_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Patient r = null;
            // Create ID with 2 Special Extensions
            try (Reader example = ExamplesUtil.resourceReader(("json/ibm/fhir-operation-erase/Patient-1.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);
                Extension ext1 = Extension.builder()
                        .url("NAME1")
                        .value(string("VALUE1"))
                    .build();
                Extension ext2 = Extension.builder()
                        .url("NAME2")
                        .value(string("VALUE2"))
                    .build();
                Collection<Extension> exts = Arrays.asList(ext1, ext2);
                r = r.toBuilder()
                        .id("REIN-DEX-TEST-1")
                        .extension(
                            Extension.builder()
                                .url("http://ibm.com/fhir/test")
                                .extension(exts)
                                .build())
                        .build();
                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Patient/REIN-DEX-TEST-1")
                        .request()
                        .put(entity, Response.class);
                assertResponse(response, Response.Status.OK.getStatusCode());
            }

            // Search and Confirm the search using the test-code1 does not return anything.
            Response response = getWebTarget().path("/Patient")
                    .queryParam("test-code1", "VALUE1")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }
}