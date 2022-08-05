/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import java.io.InputStream;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Binary;

public class BinaryTest {
    public static void main(String[] args) throws Exception {
        // JSON
        try (InputStream in = BinaryTest.class.getClassLoader().getResourceAsStream("JSON/binary-example.json")) {
            Binary binary = FHIRParser.parser(Format.JSON).parse(in);
            FHIRGenerator.generator(Format.JSON, true).generate(binary, System.out);
        }
        
        // XML
        try (InputStream in = BinaryTest.class.getClassLoader().getResourceAsStream("XML/binary-example.xml")) {
            Binary binary = FHIRParser.parser(Format.XML).parse(in);
            FHIRGenerator.generator(Format.XML, true).generate(binary, System.out);
        }
    }
}
