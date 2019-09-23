/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.test;

import java.io.InputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Binary;

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
