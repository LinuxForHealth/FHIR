/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse.Output;

/**
 * Simple Test for the Rough Response defined in the BulkData Export
 */
public class PollingLocationResponseTest {

    @Test
    public void testResponseMetadataJsonEmpty() {
        PollingLocationResponse metadata = new PollingLocationResponse();
        assertEquals(metadata.toJsonString(), "{\n" +
                "\"output\" : []\n" +
                "}");
    }

    @Test
    public void testResponseMetadataJsonFull() {
        PollingLocationResponse metadata = new PollingLocationResponse();
        metadata.setRequest("request");
        metadata.setRequiresAccessToken(Boolean.FALSE);
        Instant now = Instant.now();
        metadata.setTransactionTime(now.toString());
        assertEquals(metadata.toJsonString().replaceFirst(now.getValue().format(Instant.PARSER_FORMATTER), ""), "{\n"
                +
                "\"transactionTime\": \"{\n" +
                "    \"instant\": \"\"\n" +
                "}\",\"request\": \"request\",\"requiresAccessToken\": false,\"output\" : []\n" +
                "}");
    }

    @Test
    public void testResponseMetadataJsonFullWithOutput() {
        Output output = new Output("type1", "url1", "1000");
        assertEquals(output.getType(), "type1");
        output.setType("type2");
        assertEquals(output.getUrl(), "url1");
        output.setUrl("url2");
        assertEquals(output.getCount(), "1000");
        output.setCount("2000");

        List<Output> outputs = new ArrayList<>();
        outputs.add(new Output("type", "url", "1000"));
        outputs.add(output);

        PollingLocationResponse metadata = new PollingLocationResponse();

        metadata.setRequest("request");
        metadata.setRequiresAccessToken(Boolean.FALSE);
        Instant now = Instant.now();
        metadata.setTransactionTime(now.toString());
        assertNotNull(metadata.getTransactionTime());

        metadata.setOutput(outputs);
        assertFalse(metadata.getOutput().isEmpty());
        assertEquals(metadata.toJsonString().replaceFirst(now.getValue().format(Instant.PARSER_FORMATTER), ""), "{\n"
                +
                "\"transactionTime\": \"{\n" +
                "    \"instant\": \"\"\n" +
                "}\",\"request\": \"request\",\"requiresAccessToken\": false,\"output\" : [{ \"type\" : \"type\", \"url\": \"url\", \"count\": 1000},"
                + "{ \"type\" : \"type2\", \"url\": \"url2\", \"count\": 2000}]\n"
                + "}");
    }
}
