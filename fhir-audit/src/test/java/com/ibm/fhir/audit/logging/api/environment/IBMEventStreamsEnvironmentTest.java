/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.logging.api.environment;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.IOException;

import org.testng.annotations.Test;

import com.ibm.fhir.exception.FHIRException;

/**
 * IBMEventStreamsEnvironment
 */
public class IBMEventStreamsEnvironmentTest {

    @Test
    public void testEnvironment() throws FHIRException, IOException {
        String[] kafkaBrokerSasls = new String[1];
        kafkaBrokerSasls[0] = "test";

        IBMEventStreamsEnvironment.EventStreamsCredentials esc =
                IBMEventStreamsEnvironment.EventStreamsCredentials.builder().build();

        esc =
                IBMEventStreamsEnvironment.EventStreamsCredentials.builder().apiKey("apiKey")
                        .kafkaBrokersSasl(kafkaBrokerSasls).password("password").user("user").build();

        String jsonString = IBMEventStreamsEnvironment.EventStreamsCredentials.Writer.generate(esc);
        esc = IBMEventStreamsEnvironment.EventStreamsCredentials.Parser.parse(jsonString);

        assertEquals(esc.getApiKey(), "apiKey");
        assertEquals(esc.getPassword(), "password");
        assertEquals(esc.getUser(), "user");
        assertEquals(esc.getKafkaBrokersSasl().length, 1);
        assertEquals(esc.getKafkaBrokersSasl()[0], "test");

        esc = IBMEventStreamsEnvironment.getEventStreamsCredentials();
        assertNull(esc);

        esc = IBMEventStreamsEnvironment.parseEventStreamsCredentials("{}{\n" +
                "    \"api_key\": \"apiKey\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"user\": \"user\",\n" +
                "    \"kafka_brokers_sasl\": [\n" +
                "        \"test\"\n" +
                "    ]\n" +
                "}");
        assertNotNull(esc);

        esc = IBMEventStreamsEnvironment.parseEventStreamsCredentials("{\n" +
                "    \"api_key\": \"apiKey\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"user\": \"user\",\n" +
                "    \"kafka_brokers_sasl\": [\n" +
                "        \"test\"\n" +
                "    ]\n" +
                "}");
        assertNotNull(esc);

        // Invalid
        esc = IBMEventStreamsEnvironment.parseEventStreamsCredentials("" +
                "    \"api_key\": \"apiKey\",\n" +
                "        \"test\"\n" +
                "    ]\n" +
                "}");
        assertNull(esc);
    }

    @Test
    public void testStringArrayToCSVHasBrokers() {
        String[] arr = new String[] {"broker-1:9093","broker-2:9093"};
        String output = IBMEventStreamsEnvironment.stringArrayToCSV(arr);
        assertEquals("broker-1:9093,broker-2:9093",output);
    }

    @Test
    public void testStringArrayToCSVHasNoBrokers() {
        String[] arr = new String[] {};
        String output = IBMEventStreamsEnvironment.stringArrayToCSV(arr);
        assertEquals("",output);
    }
}