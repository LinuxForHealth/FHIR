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
import com.ibm.fhir.model.resource.ConceptMap;

public class ConceptMapTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = ConceptMapTest.class.getClassLoader().getResourceAsStream("XML/conceptmap-example.xml")) {
            ConceptMap conceptMap = FHIRParser.parser(Format.XML).parse(in);
            FHIRGenerator.generator(Format.XML, true).generate(conceptMap, System.out);
        }
    }
}
