/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;
import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.spec.test.Expectation;
import com.ibm.fhir.validation.FHIRValidator;

public class ExamplesValidationTest {
    @Test
    public void testBBValidationJson() throws Exception {
        try (Reader reader = ExamplesUtil.indexReader(Index.PROFILES_CARRIN_BB_JSON); BufferedReader br = new BufferedReader(reader);) {
            String line = br.readLine();
            while (line != null) {
                String[] tokens = line.split("\\s+");
                Expectation exp = Expectation.valueOf(tokens[0]);
                try (Reader in = ExamplesUtil.resourceReader(tokens[1])) {
                    Resource resource = FHIRParser.parser(Format.JSON).parse(in);
                    List<Issue> issues = FHIRValidator.validator().validate(resource);
                    // Left for debug issues.forEach(System.out::println);

                    if(!exp.equals(Expectation.VALIDATION) && !exp.equals(Expectation.PROCESS)) {
                        assertEquals(countErrors(issues), 0);
                    }
                }
                line = br.readLine();
            }
        }
    }

    @Test
    public void testBBValidationXML() throws Exception {
        try (Reader reader = ExamplesUtil.indexReader(Index.PROFILES_CARRIN_BB_XML); BufferedReader br = new BufferedReader(reader);) {
            String line = br.readLine();
            while (line != null) {
                String[] tokens = line.split("\\s+");
                Expectation exp = Expectation.valueOf(tokens[0]);
                try (Reader in = ExamplesUtil.resourceReader(tokens[1])) {
                    Resource resource = FHIRParser.parser(Format.XML).parse(in);
                    List<Issue> issues = FHIRValidator.validator().validate(resource);
                    // Left for debug issues.forEach(System.out::println);

                    if(!exp.equals(Expectation.VALIDATION) && !exp.equals(Expectation.PROCESS)) {
                        assertEquals(countErrors(issues), 0);
                    }
                }
                line = br.readLine();
            }
        }
    }
}