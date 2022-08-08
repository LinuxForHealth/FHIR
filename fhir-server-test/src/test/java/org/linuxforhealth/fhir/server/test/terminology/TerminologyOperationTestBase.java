/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.terminology;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

import jakarta.json.JsonObject;

public abstract class TerminologyOperationTestBase extends FHIRServerTestBase {

    public static final String FORMAT = "application/json";

    private final String tenantName = "default";
    private final String dataStoreId = "default";

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        setUp(testProperties);
    }

    public Response doPut(String resourceType, String id, String resourcePath) throws Exception {
        JsonObject jsonObject = TestUtil.readJsonObject(resourcePath);
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget().path(resourceType + "/" + id).request().put(entity, Response.class);
        String responseBody = response.readEntity(String.class);
        assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.SUCCESSFUL, responseBody);

        // To support delete AFTER the IT is run.
        String logicalId = getLocationLogicalId(response);
        addToResourceRegistry(resourceType, logicalId);
        return response;
    }

    public Response doGet(String path, String... params) {

        WebTarget target = getWebTarget();
        target = target.path(path);

        // When the path is passed in with the parameters, the ?, &, etc.
        // get escaped and it causes failures, so we are doing some
        // hacking here.
        if (params != null && params.length > 0) {
            assert (params.length % 2 == 0);
            for (int i = 0; i < params.length; i += 2) {
                target = target.queryParam(params[i], params[i + 1]);
            }
        }

        return target.request(FORMAT).header("X-FHIR-TENANT-ID", tenantName).header("X-FHIR-DSID", dataStoreId).get(Response.class);
    }

    public Resource parseResource(String responseBody) throws FHIRParserException {
        Resource resource = FHIRParser.parser(Format.JSON).parse(new ByteArrayInputStream(responseBody.getBytes()));
        return resource;
    }

    public Parameters.Parameter getParameter(Resource resource, String propertyName) {
        assertTrue(resource instanceof Parameters);
        Parameters parameters = (Parameters) resource;
        return parameters.getParameter().stream().filter(p -> p.getName().getValue().equals(propertyName)).reduce((a, b) -> {
            throw new IllegalStateException("More than one parameter found with the same name '" + propertyName + "'");
        }).get();
    }

    public Boolean getBooleanParameterValue(Resource resource, String propertyName) {
        return ((org.linuxforhealth.fhir.model.type.Boolean) getParameter(resource, propertyName).getValue()).getValue();
    }
}
