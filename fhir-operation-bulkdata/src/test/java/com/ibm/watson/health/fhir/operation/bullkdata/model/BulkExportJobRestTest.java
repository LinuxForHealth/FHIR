/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.model;

import static org.testng.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.Test;

/**
 * BulkExportJobResponse
 * 
 * @author pbastide 
 *
 */
public class BulkExportJobRestTest {

    @Test
    public void testBulkExportJobInstanceResponseBuilderAndGenerator() throws IOException, JSONException {
        BulkExportJobInstanceResponse.Builder builder = BulkExportJobInstanceResponse.builder();
        builder.appName("appName");
        builder.batchStatus("complete");
        builder.instanceId(1);
        builder.instanceName("name");
        builder.jobName("my-job");
        builder.jobXMLName("here-is-xml-name");
        builder.lastUpdatedTime("1234");
        builder.link("rel1", "href1");
        builder.link("rel2", "href2");
        
        String str = BulkExportJobInstanceResponse.Writer.generate(builder.build());
        JSONAssert.assertEquals(str,"{\n" + 
                "    \"jobName\": \"my-job\",\n" + 
                "    \"instanceId\": 1,\n" + 
                "    \"appName\": \"appName\",\n" + 
                "    \"batchStatus\": \"complete\",\n" + 
                "    \"jobXMLName\": \"here-is-xml-name\",\n" + 
                "    \"instanceName\": \"name\",\n" + 
                "    \"lastUpdatedTime\": \"1234\",\n" + 
                "    \"_links\": [\n" + 
                "        {\n" + 
                "            \"href\": \"href1\",\n" + 
                "            \"rel\": \"rel1\"\n" + 
                "        },\n" + 
                "        {\n" + 
                "            \"href\": \"href2\",\n" + 
                "            \"rel\": \"rel2\"\n" + 
                "        }\n" + 
                "    ]\n" + 
                "}", false);
    }
    
    @Test
    public void testBulkExportJobInstanceResponseParserAndGeneratorRoundTrip() throws IOException {
        String testStr = "{\n" + 
                "    \"jobName\": \"my-job\",\n" + 
                "    \"instanceId\": 1,\n" + 
                "    \"appName\": \"appName\",\n" + 
                "    \"batchStatus\": \"complete\",\n" + 
                "    \"jobXMLName\": \"here-is-xml-name\",\n" + 
                "    \"instanceName\": \"name\",\n" + 
                "    \"lastUpdatedTime\": \"1234\",\n" + 
                "    \"_links\": [\n" + 
                "        {\n" + 
                "            \"href\": \"href1\",\n" + 
                "            \"rel\": \"rel1\"\n" + 
                "        },\n" + 
                "        {\n" + 
                "            \"href\": \"href2\",\n" + 
                "            \"rel\": \"rel2\"\n" + 
                "        }\n" + 
                "    ]\n" + 
                "}";
        
        try(InputStream in = new ByteArrayInputStream(testStr.getBytes())){
            BulkExportJobInstanceResponse parsedObj = BulkExportJobInstanceResponse.Parser.parse(in);
            
            String str = BulkExportJobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(testStr, str, false);
            
            System.out.println(str);
        } catch(Exception e) {
            fail("failed to parse", e);
        }
       
    }
    
    @Test
    public void testBulkExportJobInstanceRequest() throws Exception {
        String jsonString = "{\n" + 
                "    \"applicationName\": \"fhir-bulkimportexport-webapp\",\n" + 
                "    \"moduleName\": \"fhir-bulkimportexport-webapp-4.0.0-SNAPSHOT.war\",\n" + 
                "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" + 
                "    \"jobParameters\": {\n" + 
                "        \"fhir.resourcetype\": \"Patient\",\n" + 
                "        \"cos.bucket.name\": \"fhir-r4-connectathon\",\n" + 
                "        \"cos.location\": \"us\",\n" + 
                "        \"cos.endpointurl\": \"https://fake.cloud\",\n" + 
                "        \"cos.credential.ibm\": \"Y\",\n" + 
                "        \"cos.api.key\": \"key\",\n" + 
                "        \"cos.srvinst.id\": \"crn:v1:bluemix:public:cloud-object-storage:global:a/<>::\",\n" + 
                "        \"fhir.search.fromdate\": \"2019-08-01\"\n" + 
                "    }\n" + 
                "}";
        
        
        try(InputStream in = new ByteArrayInputStream(jsonString.getBytes())){
            BulkExportJobInstanceRequest parsedObj = BulkExportJobInstanceRequest.Parser.parse(in);
            
            String str = BulkExportJobInstanceRequest.Writer.generate(parsedObj);
            
            JSONAssert.assertEquals(jsonString, str, false);
        } catch(Exception e) {
            e.printStackTrace();
            fail("failed to parse", e);
        }
    }
}
