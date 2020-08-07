/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.InputStream;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;

public class FHIRParserTest {
    @Test
    public void testUnrecognizedElements1() throws Exception {
        String value = "handling=strict";
        if (value.startsWith("handling=")) {

            value = value.substring("handling=".length());
            System.out.println(value);
        }


        try (InputStream in = FHIRParserTest.class.getClassLoader().getResourceAsStream("JSON/observation-unrecognized-elements.json")) {
            FHIRParser.parser(Format.JSON).parse(in);
            fail();
        } catch (FHIRParserException e) {
            assertTrue(e.getMessage().startsWith("Unrecognized element"));
        }
    }

    @Test
    public void testUnrecognizedElements2() throws Exception {
        try (InputStream in = FHIRParserTest.class.getClassLoader().getResourceAsStream("JSON/observation-unrecognized-elements.json")) {
            FHIRParser parser = FHIRParser.parser(Format.JSON);
            parser.setProperty(FHIRParser.PROPERTY_IGNORE_UNRECOGNIZED_ELEMENTS, true);
            assertNotNull(parser.parse(in));
        } catch (FHIRParserException e) {
            fail();
        }
    }
}