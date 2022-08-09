/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.operation;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.examples.ExamplesUtil;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

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
            try (Reader example = ExamplesUtil.resourceReader(("json/fhir-operation-erase/Patient-1.json"))) {
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
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the test-code1 fails with BAD_REQUEST as the code does not yet exist
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

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_String_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicString.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);
                r = r.toBuilder()
                        .id("REINDEX-STRING")
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-STRING")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-string", "testString")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Token_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicToken.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);

                // "http://example.org/ContactPoint-noSystem"
                // This is a negative example which needs to be removed.
                // As it generates {"text":"cpt-2: A system is required if a value is provided."}
                java.util.List<Extension> exts = r.getExtension();
                java.util.List<Extension> newExts = new ArrayList<>();

                for (Extension ext : exts) {
                    if (!"http://example.org/ContactPoint-noSystem".equals(ext.getUrl())) {
                        newExts.add(ext);
                    }
                }

                r = r.toBuilder()
                        .id("REINDEX-TOKEN")
                        .extension(newExts)
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-TOKEN")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-token", "http://example.org/codesystem|code")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_URI_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicURI.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);
                r = r.toBuilder()
                        .id("REINDEX-URI")
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-URI")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-uri", "Basic/123")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Reference_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicReference.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);
                r = r.toBuilder()
                        .id("REINDEX-REFERENCE")
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-REFERENCE")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-reference", "Basic/123")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Quantity_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicQuantity.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);
                r = r.toBuilder()
                        .id("REINDEX-QUANTITY")
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-QUANTITY")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-quantity", "25|http://unitsofmeasure.org|s")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Number_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicNumber.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);
                r = r.toBuilder()
                        .id("REINDEX-NUMBER")
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-NUMBER")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-decimal", "99.99")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Date_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicDate.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);

                r = r.toBuilder()
                        .id("REINDEX-DATE")
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-DATE")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-date", "2018-10-29")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", "default")
                    .header("X-FHIR-DSID", "default")
                    .get(Response.class);
            assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());
        } else {
            System.out.println("Skipping Phase 1 of Reindex Operation Tests");
        }
    }

    @Test(groups = {"reindex"}, dependsOnMethods = {})
    public void testReindex_Composite_Phase1() throws IOException, FHIRParserException, FHIRPathException {
        if (runIt) {
            Basic r = null;
            try (Reader example = ExamplesUtil.resourceReader(("json/basic/BasicComposite.json"))) {
                r = FHIRParser.parser(Format.JSON).parse(example);
                r = r.toBuilder()
                        .id("REINDEX-COMPOSITE")
                    .build();

                Entity<Resource> entity = Entity.entity(r, FHIRMediaType.APPLICATION_FHIR_JSON);
                Response response = getWebTarget()
                        .path("Basic/REINDEX-COMPOSITE")
                        .request()
                        .put(entity, Response.class);
                assertEquals(Response.Status.Family.familyOf(response.getStatus()), Response.Status.Family.SUCCESSFUL);
            }

            // Search and Confirm the search using the 'code' fails with BAD_REQUEST as the code does not yet exist
            Response response = getWebTarget().path("/Basic")
                    .queryParam("reindex-composite", "code$code")
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