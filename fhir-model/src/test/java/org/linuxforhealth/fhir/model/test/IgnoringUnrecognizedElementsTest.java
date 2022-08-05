/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.StringReader;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Patient;

public class IgnoringUnrecognizedElementsTest {
    @Test
    public void testIgnoringUnrecognizedElements1() {
        try {
            String xmlString = "<Patient xmlns=\"http://hl7.org/fhir\"><hamburger/></Patient>";
            FHIRParser parser = FHIRParser.parser(Format.XML);
            parser.parse(new StringReader(xmlString));
        } catch (FHIRParserException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals(e.getCause().getMessage(), "Unrecognized element: 'hamburger'");
        }
    }

    @Test
    public void testIgnoringUnrecognizedElements2() {
        try {
            String jsonString = "{\"resourceType\":\"Patient\",\"hamburger\":true}";
            FHIRParser parser = FHIRParser.parser(Format.JSON);
            parser.parse(new StringReader(jsonString));
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals(e.getCause().getMessage(), "Unrecognized element: 'hamburger'");
        }
    }

    @Test
    public void testIgnoringUnrecognizedElements3() throws Exception {
        String xmlString = "<Patient xmlns=\"http://hl7.org/fhir\"><hamburger/></Patient>";
        FHIRParser parser = FHIRParser.parser(Format.XML);
        parser.setIgnoringUnrecognizedElements(true);
        Patient patient = parser.parse(new StringReader(xmlString));
        assertNotNull(patient);
    }

    @Test
    public void testIgnoringUnrecognizedElements4() throws Exception {
        String jsonString = "{\"resourceType\":\"Patient\",\"hamburger\":true}";
        FHIRParser parser = FHIRParser.parser(Format.JSON);
        parser.setIgnoringUnrecognizedElements(true);
        Patient patient = parser.parse(new StringReader(jsonString));
        assertNotNull(patient);
    }
}
