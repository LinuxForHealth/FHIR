/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.testng.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Practitioner;

public class XMLParserTest {
    @Test
    public void testXMLParser1() throws Exception {
        // FHIR elements are in default namespace (no prefix), XHTML elements are in default namespace (no prefix)
        StringReader reader = new StringReader("<Practitioner xmlns=\"http://hl7.org/fhir\"><id value=\"0\"/><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">Narrative TBD</div></text></Practitioner>");
        Practitioner practitioner = FHIRParser.parser(Format.XML).parse(reader);
        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.XML).generate(practitioner, writer);
        assertEquals(writer.toString(), "<Practitioner xmlns=\"http://hl7.org/fhir\"><id value=\"0\"/><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">Narrative TBD</div></text></Practitioner>");
    }

    @Test
    public void testXMLParser2() throws Exception {
        // FHIR elements are prefixed, XHTML elements are prefixed, XHTML namespace is declared on root element
        StringReader reader = new StringReader("<f:Practitioner xmlns:f=\"http://hl7.org/fhir\" xmlns:h=\"http://www.w3.org/1999/xhtml\"><f:id value=\"0\"/><f:text><f:status value=\"generated\"/><h:div>Narrative TBD</h:div></f:text></f:Practitioner>");
        Practitioner practitioner = FHIRParser.parser(Format.XML).parse(reader);
        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.XML).generate(practitioner, writer);
        assertEquals(writer.toString(), "<Practitioner xmlns=\"http://hl7.org/fhir\"><id value=\"0\"/><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">Narrative TBD</div></text></Practitioner>");
    }

    @Test
    public void testXMLParser3() throws Exception {
        // FHIR elements are prefixed, XHTML elements are prefixed, XHTML namespace is declared on "div" element
        StringReader reader = new StringReader("<f:Practitioner xmlns:f=\"http://hl7.org/fhir\"><f:id value=\"0\"/><f:text><f:status value=\"generated\"/><h:div xmlns:h=\"http://www.w3.org/1999/xhtml\">Narrative TBD</h:div></f:text></f:Practitioner>");
        Practitioner practitioner = FHIRParser.parser(Format.XML).parse(reader);
        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.XML).generate(practitioner, writer);
        assertEquals(writer.toString(), "<Practitioner xmlns=\"http://hl7.org/fhir\"><id value=\"0\"/><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">Narrative TBD</div></text></Practitioner>");
    }
}
