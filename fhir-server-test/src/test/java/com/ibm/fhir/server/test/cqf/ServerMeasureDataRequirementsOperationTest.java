/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test.cqf;

import static org.junit.Assert.assertNotNull;

import java.io.StringReader;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.test.TestUtil;

import jakarta.json.JsonObject;

public class ServerMeasureDataRequirementsOperationTest extends BaseMeasureOperationTest {
    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        setUp(testProperties);

        JsonObject jsonObject = TestUtil.readJsonObject("testdata/Patient_SallyFields.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = getWebTarget().path("/" + TEST_PATIENT_ID).request().put(entity);
        assertResponse(response, Response.Status.Family.SUCCESSFUL);
        
        jsonObject = TestUtil.readJsonObject("testdata/Bundle-EXM74-10.2.000.json");
        entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = getWebTarget().request().post( entity );
        assertResponse( response, Response.Status.Family.SUCCESSFUL );
    }
    
    @Test
    public void testMeasureDataRequirementsInstance() throws Exception {
        Response response =
                getWebTarget().path("/Measure/{id}/$data-requirements")
                    .resolveTemplate("id", TEST_MEASURE_ID)
                    .queryParam("periodStart", TEST_PERIOD_START)
                    .queryParam("periodEnd", TEST_PERIOD_END)
                    .request().get();
        assertResponse(response, 200);

        String responseBody = response.readEntity(String.class);
        System.out.println(responseBody);
        Library module = (Library) FHIRParser.parser(Format.JSON).parse(new StringReader(responseBody));
        assertNotNull(module);
    }
}
