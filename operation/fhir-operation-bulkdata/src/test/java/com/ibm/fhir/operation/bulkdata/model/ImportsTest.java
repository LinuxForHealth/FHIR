/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.operation.bulkdata.model.type.JobParameter;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.persistence.bulkdata.InputDTO;

/**
 * Tests for Input and StorageDetail
 */
public class ImportsTest {
    @Test
    public void testInput() throws IOException {
        InputDTO input = new InputDTO("a", "b");
        assertNotNull(input);
        assertEquals(input.getType(), "a");
        assertEquals(input.getUrl(), "b");
        input.setType("c");
        assertEquals(input.getType(), "c");
        input.setUrl("d");
        assertEquals(input.getUrl(), "d");
    }

    @Test
    public void testStorageDetail() throws IOException {
        StorageDetail detail = new StorageDetail("https", Arrays.asList("a", "b"));
        assertNotNull(detail);
        assertEquals(detail.getType(), "https");
        assertFalse(detail.getContentEncodings().isEmpty());
        assertEquals(detail.getContentEncodings().size(), 2);

        detail.setType("httpx");
        assertEquals(detail.getType(), "httpx");

        detail.addContentEncodings("x");
        assertEquals(detail.getContentEncodings().size(), 3);

        detail = new StorageDetail("https", "a", "b");
        assertNotNull(detail);
        assertEquals(detail.getType(), "https");
        assertFalse(detail.getContentEncodings().isEmpty());
        assertEquals(detail.getContentEncodings().size(), 2);
    }

    @Test
    public void testJobParametersSerialization() throws IOException {
        InputDTO input1 = new InputDTO("a", "b");
        InputDTO input2 = new InputDTO("c", "d");
        List<InputDTO> inputs = Arrays.asList(input1, input2);
        String base64str = JobParameter.Writer.writeToBase64(inputs);
        List<InputDTO> roundtripInputs = JobParameter.Parser.parseInputsFromString(base64str);
        assertNotNull(roundtripInputs);
        assertFalse(roundtripInputs.isEmpty());
        assertEquals(roundtripInputs.size(), 2);
    }
}