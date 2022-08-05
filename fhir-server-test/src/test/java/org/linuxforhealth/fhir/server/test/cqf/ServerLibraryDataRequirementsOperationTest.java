/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.server.test.cqf;

import static org.testng.Assert.assertNotNull;

import java.io.StringReader;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Library;

public class ServerLibraryDataRequirementsOperationTest extends BaseMeasureOperationTest {
    public static final String TEST_LIBRARY_ID = "8-auq9X1o0TxTYLhRGlt8ISNC1w7ELS5sKyhmUTj4SE";
    public static final String TEST_LIBRARY_URL = "http://ibm.com/health/Library/EXM74";


    @Test
    public void testLibraryDataRequirementsInstance() throws Exception {
        Response response =
                getWebTarget().path("/Library/{id}/$data-requirements")
                    .resolveTemplate("id", TEST_LIBRARY_ID)
                    .request().get();
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        Library module = (Library) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertNotNull(module);
    }

    @Test
    public void testLibraryDataRequirementsSystem() throws Exception {
        Response response =
                getWebTarget().path("/$data-requirements")
                    .queryParam("target", TEST_LIBRARY_ID)
                    .request().get();
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        Library module = (Library) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertNotNull(module);
    }
}
