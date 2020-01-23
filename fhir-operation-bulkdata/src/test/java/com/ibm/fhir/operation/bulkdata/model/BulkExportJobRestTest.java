/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.model;

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
        } catch(Exception e) {
            fail("failed to parse", e);
        }

    }

    @Test
    public void testBulkExportJobInstanceRequestWithSensitive() throws Exception {
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
                "        \"fhir.search.fromdate\": \"2019-08-01\",\n" +
                "        \"fhir.tenant\": \"default\",\n" +
                "        \"cos.bucket.pathprefix\": \"mytest\",\n" +
                "        \"fhir.datastoreid\": \"default\"\n" +
                "    }\n" +
                "}";


        try(InputStream in = new ByteArrayInputStream(jsonString.getBytes())){
            BulkExportJobInstanceRequest parsedObj = BulkExportJobInstanceRequest.Parser.parse(in);
            String str = BulkExportJobInstanceRequest.Writer.generate(parsedObj, true);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch(Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceRequestWithoutSensitive() throws Exception {
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
                "        \"fhir.search.fromdate\": \"2019-08-01\",\n" +
                "        \"fhir.tenant\": \"default\",\n" +
                "        \"cos.bucket.pathprefix\": \"mytest\",\n" +
                "        \"fhir.datastoreid\": \"default\"\n" +
                "    }\n" +
                "}";

        String expected = "{\n" +
                "    \"applicationName\": \"fhir-bulkimportexport-webapp\",\n" +
                "    \"moduleName\": \"fhir-bulkimportexport-webapp-4.0.0-SNAPSHOT.war\",\n" +
                "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                "    \"jobParameters\": {\n" +
                "        \"fhir.resourcetype\": \"Patient\",\n" +
                "        \"cos.location\": \"us\",\n" +
                "        \"cos.credential.ibm\": \"Y\",\n" +
                "        \"fhir.search.fromdate\": \"2019-08-01\"\n" +
                "    }\n" +
                "}";

        try(InputStream in = new ByteArrayInputStream(jsonString.getBytes())){
            BulkExportJobInstanceRequest parsedObj = BulkExportJobInstanceRequest.Parser.parse(in);

            String str = BulkExportJobInstanceRequest.Writer.generate(parsedObj, false);

            JSONAssert.assertEquals(expected, str, false);
        } catch(Exception e) {
            e.printStackTrace();
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkImportJobResponse() {
        String jsonString = "{\n" +
                "    \"jobName\": \"BulkImportJob\",\n" +
                "    \"instanceId\": 2,\n" +
                "    \"appName\": \"fhir-bulkimportexport-webapp#fhir-bulkimportexport-webapp-4.0.0-SNAPSHOT.war\",\n" +
                "    \"submitter\": \"fhiradmin\",\n" +
                "    \"batchStatus\": \"STARTING\",\n" +
                "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                "    \"instanceState\": \"SUBMITTED\",\n" +
                "    \"lastUpdatedTime\": \"2019/09/13 07:38:07.159 -0400\",\n" +
                "    \"_links\": [\n" +
                "        {\n" +
                "            \"rel\": \"self\",\n" +
                "            \"href\": \"https://localhost:9443/ibm/api/batch/jobinstances/2\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"rel\": \"job logs\",\n" +
                "            \"href\": \"https://localhost:9443/ibm/api/batch/jobinstances/2/joblogs\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";


        try(InputStream in = new ByteArrayInputStream(jsonString.getBytes())){
            BulkExportJobInstanceResponse parsedObj = BulkExportJobInstanceResponse.Parser.parse(in);

            String str = BulkExportJobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch(Exception e) {
            fail("failed to parse", e);
        }

    }
}
