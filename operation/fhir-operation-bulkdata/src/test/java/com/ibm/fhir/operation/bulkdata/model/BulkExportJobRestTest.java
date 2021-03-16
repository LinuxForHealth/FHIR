/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.Test;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * BulkExportJobResponse
 */
public class BulkExportJobRestTest {

    @Test
    public void testBulkExportJobInstanceResponseBuilderAndGenerator() throws IOException, JSONException {
        JobInstanceResponse.Builder builder = JobInstanceResponse.builder();
        builder.appName("appName");
        builder.batchStatus("complete");
        builder.instanceId(1);
        builder.instanceName("name");
        builder.jobName("my-job");
        builder.jobXMLName("here-is-xml-name");
        builder.lastUpdatedTime("1234");
        builder.link("rel1", "href1");
        builder.link("rel2", "href2");

        String str = JobInstanceResponse.Writer.generate(builder.build());
        JSONAssert.assertEquals(str, "{\n" +
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
    public void testBulkExportJobInstanceResponseBuilderAndGeneratorEmpty() throws IOException, JSONException {
        JobInstanceResponse.Builder builder = JobInstanceResponse.builder();

        String str = JobInstanceResponse.Writer.generate(builder.build());
        JSONAssert.assertEquals(str, "{\n" +
                "    \"_links\": [\n" +
                "    ]\n" +
                "}", false);
    }

    @Test
    public void testBulkExportJobInstanceResponseParserAndGeneratorRoundTrip() throws IOException {
        String testStr =
                "{\n" +
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

        try (InputStream in = new ByteArrayInputStream(testStr.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);
            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(testStr, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceResponseParserAndGeneratorRoundTripEmptyLink() throws IOException {
        String testStr =
                "{\n" +
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
                        "        }\n" +
                        "    ]\n" +
                        "}";
        String expectedStr =
                "{\n" +
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
                        "        }\n" +
                        "    ]\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(testStr.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);
            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(expectedStr, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceResponseParserAndGeneratorRoundTripEmptyLinkRel() throws IOException {
        String testStr =
                "{\n" +
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
                        "            \"rel\": \"rel1\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
        String expectedStr =
                "{\n" +
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
                        "        }\n" +
                        "    ]\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(testStr.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);
            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(expectedStr, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceResponseParserAndGeneratorRoundTripEmptyLinkHref() throws IOException {
        String testStr =
                "{\n" +
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
                        "            \"href\": \"href1\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
        String expectedStr =
                "{\n" +
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
                        "        }\n" +
                        "    ]\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(testStr.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);
            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(expectedStr, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceResponseParserAndGeneratorRoundTripWithString() throws IOException {
        String testStr =
                "{\n" +
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
                        "            \"href\": \"href1\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
        String expectedStr =
                "{\n" +
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
                        "        }\n" +
                        "    ]\n" +
                        "}";

        try {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(testStr);
            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(expectedStr, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceResponseParserAndGeneratorRoundTripEmpty() throws IOException {
        String testStr = "{}";
        try (InputStream in = new ByteArrayInputStream(testStr.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);
            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(testStr, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceRequestWithSensitive() throws Exception {
        String jsonString =
                "{\n" +
                        "    \"applicationName\": \"fhir-bulkdata-webapp\",\n" +
                        "    \"moduleName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"jobParameters\": {\n" +
                        "        \"fhir.resourcetype\": \"Patient\",\n" +
                        "        \"fhir.search.fromdate\": \"2019-08-01\",\n" +
                        "        \"fhir.tenant\": \"default\",\n" +
                        "        \"fhir.search.patientgroupid\": \"mytest\",\n" +
                        "        \"cos.bucket.pathprefix\": \"mytest\",\n" +
                        "        \"fhir.datastoreid\": \"default\"\n" +
                        "    }\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceRequest parsedObj = JobInstanceRequest.Parser.parse(in);
            String str = JobInstanceRequest.Writer.generate(parsedObj, true);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceRequestWithSensitiveEmptyJobParameters() throws Exception {
        String jsonString =
                "{\n" +
                        "    \"applicationName\": \"fhir-bulkdata-webapp\",\n" +
                        "    \"moduleName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"jobParameters\": {\n" +
                        "    }\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceRequest parsedObj = JobInstanceRequest.Parser.parse(in);
            String str = JobInstanceRequest.Writer.generate(parsedObj, true);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceRequestWithoutSensitive() throws Exception {
        String jsonString =
                "{\n" +
                        "    \"applicationName\": \"fhir-bulkdata-webapp\",\n" +
                        "    \"moduleName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"jobParameters\": {\n" +
                        "        \"fhir.resourcetype\": \"Patient\",\n" +
                        "        \"fhir.search.fromdate\": \"2019-08-01\",\n" +
                        "        \"fhir.tenant\": \"default\",\n" +
                        "        \"cos.bucket.pathprefix\": \"mytest\",\n" +
                        "        \"fhir.datastoreid\": \"default\",\n" +
                        "        \"fhir.search.patientgroupid\": \"default-group-id\",\n" +
                        "        \"fhir.typeFilters\": \"default-filter-id\"\n" +
                        "    }\n" +
                        "}";

        String expected =
                "{\n" +
                        "    \"applicationName\": \"fhir-bulkdata-webapp\",\n" +
                        "    \"moduleName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"jobParameters\": {\n" +
                        "        \"fhir.resourcetype\": \"Patient\",\n" +
                        "        \"fhir.search.fromdate\": \"2019-08-01\"\n" +
                        "    }\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceRequest parsedObj = JobInstanceRequest.Parser.parse(in);

            String str = JobInstanceRequest.Writer.generate(parsedObj, false);
            JSONAssert.assertEquals(expected, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceRequestEmpty() throws Exception {
        String jsonString = "{}";
        String expected = "{}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceRequest parsedObj = JobInstanceRequest.Parser.parse(in);

            String str = JobInstanceRequest.Writer.generate(parsedObj, false);

            JSONAssert.assertEquals(expected, str, false);
        } catch (Exception e) {

            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceRequestEmptyJobParameters() throws Exception {
        String jsonString = "{\"jobParameters\":{}}";
        String expected = "{\"jobParameters\":{}}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceRequest parsedObj = JobInstanceRequest.Parser.parse(in);
            assertNotNull(parsedObj.getJobParameters());
            String str = JobInstanceRequest.Writer.generate(parsedObj, false);

            JSONAssert.assertEquals(expected, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkExportJobInstanceRequestFHIROperationException() throws Exception {
        String jsonString = "{";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceRequest.Parser.parse(in);
        }
        fail();
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkExportJobExecutionResponseFHIROperationException() throws Exception {
        String jsonString = "{";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobExecutionResponse.Parser.parse(in);
        }
        fail();
    }

    @Test
    public void testBulkImportJobResponse() {
        String jsonString =
                "{\n" +
                        "    \"jobName\": \"BulkImportJob\",\n" +
                        "    \"instanceId\": 2,\n" +
                        "    \"appName\": \"fhir-bulkdata-webapp#fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n"
                        +
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

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);

            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobExecutionResponse() {
        String jsonString =
                "{\n" +
                        "    \"jobName\": \"BulkImportJob\",\n" +
                        "    \"instanceId\": 2,\n" +
                        "    \"appName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
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

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobExecutionResponse parsedObj = JobExecutionResponse.Parser.parse(in);
            String str = JobExecutionResponse.Writer.generate(parsedObj, false);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobExecutionResponseWithHref() {
        String jsonString =
                "{\n" +
                        "    \"jobName\": \"BulkImportJob\",\n" +
                        "    \"instanceId\": 2,\n" +
                        "    \"appName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
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
                        "            \"href\": \"https://localhost:9443/ibm/api/batch/jobinstances/2/joblogs\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        String expectedJsonString =
                "{\n" +
                        "    \"jobName\": \"BulkImportJob\",\n" +
                        "    \"instanceId\": 2,\n" +
                        "    \"appName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
                        "    \"submitter\": \"fhiradmin\",\n" +
                        "    \"batchStatus\": \"STARTING\",\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"instanceState\": \"SUBMITTED\",\n" +
                        "    \"lastUpdatedTime\": \"2019/09/13 07:38:07.159 -0400\",\n" +
                        "    \"_links\": [\n" +
                        "        {\n" +
                        "            \"rel\": \"self\",\n" +
                        "            \"href\": \"https://localhost:9443/ibm/api/batch/jobinstances/2\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobExecutionResponse parsedObj = JobExecutionResponse.Parser.parse(in);
            String str = JobExecutionResponse.Writer.generate(parsedObj, false);
            JSONAssert.assertEquals(expectedJsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobExecutionResponseWithLink() {
        String jsonString =
                "{\n" +
                        "    \"jobName\": \"BulkImportJob\",\n" +
                        "    \"instanceId\": 2,\n" +
                        "    \"appName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
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
                        "            \"rel\": \"job logs\"" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        String expectedJsonString =
                "{\n" +
                        "    \"jobName\": \"BulkImportJob\",\n" +
                        "    \"instanceId\": 2,\n" +
                        "    \"appName\": \"fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n" +
                        "    \"submitter\": \"fhiradmin\",\n" +
                        "    \"batchStatus\": \"STARTING\",\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"instanceState\": \"SUBMITTED\",\n" +
                        "    \"lastUpdatedTime\": \"2019/09/13 07:38:07.159 -0400\",\n" +
                        "    \"_links\": [\n" +
                        "        {\n" +
                        "            \"rel\": \"self\",\n" +
                        "            \"href\": \"https://localhost:9443/ibm/api/batch/jobinstances/2\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobExecutionResponse parsedObj = JobExecutionResponse.Parser.parse(in);
            String str = JobExecutionResponse.Writer.generate(parsedObj, false);
            JSONAssert.assertEquals(expectedJsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobExecutionResponseEmpty() {
        String jsonString = "{}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobExecutionResponse parsedObj = JobExecutionResponse.Parser.parse(in);
            String str = JobExecutionResponse.Writer.generate(parsedObj, false);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobExecutionResponseEmptyAsString()
            throws IOException, FHIROperationException, JSONException {
        JobExecutionResponse parsedObj = JobExecutionResponse.Parser.parse("{}");
        String str = JobExecutionResponse.Writer.generate(parsedObj, false);
        JSONAssert.assertEquals("{}", str, false);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkExportJobExecutionResponseEmptyAsBadString()
            throws IOException, FHIROperationException, JSONException {
        JobExecutionResponse.Parser.parse("{");
        fail();
    }

    @Test
    public void testBulkBulkExportJobInstanceResponse() {
        String jsonString =
                "{\n" +
                        "    \"jobName\": \"BulkImportJob\",\n" +
                        "    \"instanceId\": 2,\n" +
                        "    \"appName\": \"fhir-bulkdata-webapp#fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war\",\n"
                        +
                        "    \"submitter\": \"fhiradmin\",\n" +
                        "    \"batchStatus\": \"STARTING\",\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"instanceState\": \"SUBMITTED\",\n" +
                        "    \"lastUpdatedTime\": \"2019/09/13 07:38:07.159 -0400\"\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);

            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test
    public void testBulkExportJobInstanceResponseEmpty() {
        String jsonString = "{}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceResponse parsedObj = JobInstanceResponse.Parser.parse(in);

            String str = JobInstanceResponse.Writer.generate(parsedObj);
            JSONAssert.assertEquals(jsonString, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkExportJobInstanceResponseFHIROperationExceptionStream() throws Exception {
        String jsonString = "{";
        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobInstanceResponse.Parser.parse(in);
        }
        fail();
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkExportJobInstanceResponseFHIROperationExceptionString() throws Exception {
        String jsonString = "{";
        JobInstanceResponse.Parser.parse(jsonString);
        fail();
    }

    @Test
    public void testBulkExportJobExecutionResponseWithoutSensitive() throws Exception {
        String jsonString =
                "{\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"exitStatus\": \"BadStatus\",\n" +
                        "    \"instanceName\": \"my-inst\",\n" +
                        "    \"jobParameters\": {\n" +
                        "        \"fhir.resourcetype\": \"Patient\",\n" +
                        "        \"fhir.search.fromdate\": \"2019-08-01\",\n" +
                        "        \"fhir.tenant\": \"default\",\n" +
                        "        \"cos.bucket.pathprefix\": \"mytest\",\n" +
                        "        \"fhir.datastoreid\": \"default\",\n" +
                        "        \"fhir.search.patientgroupid\": \"default-group-id\",\n" +
                        "        \"fhir.typeFilters\": \"default-filter-id\"\n" +
                        "    }\n" +
                        "}";

        String expected =
                "{\n" +
                        "    \"jobXMLName\": \"FhirBulkExportChunkJob\",\n" +
                        "    \"exitStatus\": \"BadStatus\",\n" +
                        "    \"instanceName\": \"my-inst\",\n" +
                        "    \"jobParameters\": {\n" +
                        "        \"fhir.resourcetype\": \"Patient\",\n" +
                        "        \"fhir.search.fromdate\": \"2019-08-01\"\n" +
                        "    }\n" +
                        "}";

        try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
            JobExecutionResponse parsedObj = JobExecutionResponse.Parser.parse(in);

            String str = JobExecutionResponse.Writer.generate(parsedObj, false);
            JSONAssert.assertEquals(expected, str, false);
        } catch (Exception e) {
            fail("failed to parse", e);
        }
    }
}