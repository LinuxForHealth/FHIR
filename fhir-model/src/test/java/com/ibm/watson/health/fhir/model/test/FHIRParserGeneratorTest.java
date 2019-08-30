/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.test;

import java.io.InputStream;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.generator.FHIRGenerator;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.ActivityDefinition;

public class FHIRParserGeneratorTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = FHIRParserGeneratorTest.class.getClassLoader().getResourceAsStream("JSON/activitydefinition-administer-zika-virus-exposure-assessment.json")) {
            ActivityDefinition activityDefinition = FHIRParser.parser(Format.JSON).parse(in);
            FHIRGenerator.generator(Format.JSON, true).generate(activityDefinition, System.out);
        }
    }
}
