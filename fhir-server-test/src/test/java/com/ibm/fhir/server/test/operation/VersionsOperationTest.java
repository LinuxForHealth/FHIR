/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This class tests the $version operation.
 */
public class VersionsOperationTest extends FHIRServerTestBase {
    private static final String PARAM_VERSION = "version";
    private static final String PARAM_DEFAULT = "default";

    @Test
    public void testVersions() {
        WebTarget target = getWebTarget();
        target = target.path("/$versions");

        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get(Response.class);

        assertEquals(r.getStatus(), Status.OK.getStatusCode());
        Parameters parameters = r.readEntity(Parameters.class);

        List<String> versions = getParameterValues(parameters, PARAM_VERSION);
        assertEquals(versions.size(), 1);
        assertTrue(versions.contains("4.0"));

        List<String> defaults = getParameterValues(parameters, PARAM_DEFAULT);
        assertEquals(defaults.size(), 1);
        assertTrue(defaults.contains("4.0"));
    }

    private List<String> getParameterValues(Parameters parameters, String name) {
        List<String> valueStrings = new ArrayList<>();
        for (Parameter parameter : parameters.getParameter()) {
            if (name.equals(parameter.getName().getValue())) {
                valueStrings.add(parameter.getValue().as(Code.class).getValue());
            }
        }
        return valueStrings;
    }
}