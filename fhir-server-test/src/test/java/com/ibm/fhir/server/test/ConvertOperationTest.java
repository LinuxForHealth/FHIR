package com.ibm.fhir.server.test;

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.config.FHIRModelConfig;

public class ConvertOperationTest extends FHIRServerTestBase {
    @Test
    public void testConvertOperation1() throws Exception {
        FHIRModelConfig.setCheckReferenceTypes(false);
        try (BufferedReader reader = new BufferedReader(ExamplesUtil.resourceReader("json/ibm/complete-mock/Patient-1.json"))) {
            String input = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            
            // input is JSON
            assertTrue(input.startsWith("{"));
            
            WebTarget endpoint = getWebTarget();
            
            Entity<String> entity = Entity.entity(input, FHIRMediaType.APPLICATION_FHIR_JSON);
            
            Response response = endpoint.path("$convert").request()
                    .header("Content-type", FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("Accept",  FHIRMediaType.APPLICATION_FHIR_XML)
                    .post(entity);
            
            String output = response.readEntity(String.class);
            
            // output is XML
            assertTrue(output.startsWith("<"));
        }
    }
    
    @Test
    public void testConvertOperation2() throws Exception {
        FHIRModelConfig.setCheckReferenceTypes(false);
        try (BufferedReader reader = new BufferedReader(ExamplesUtil.resourceReader("xml/ibm/complete-mock/Patient-1.xml"))) {
            String input = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            
            // input is XML
            assertTrue(input.startsWith("<"));
            
            WebTarget endpoint = getWebTarget();
            
            Entity<String> entity = Entity.entity(input, FHIRMediaType.APPLICATION_FHIR_XML);
            
            Response response = endpoint.path("$convert").request()
                    .header("Content-type", FHIRMediaType.APPLICATION_FHIR_XML)
                    .header("Accept",  FHIRMediaType.APPLICATION_FHIR_JSON)
                    .post(entity);
            
            String output = response.readEntity(String.class);
            
            // output is JSON
            assertTrue(output.startsWith("{"));
        }
    }
}
