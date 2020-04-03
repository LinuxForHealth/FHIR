/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import java.io.InputStream;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.validation.FHIRValidator;

public class BBValidationTest {
    @Test
    public void testBBValidation() throws Exception {
        try (InputStream in = BBValidationTest.class.getClassLoader().getResourceAsStream("JSON/EOB1.json")) {
            ExplanationOfBenefit explanationOfBenefit = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(explanationOfBenefit);
            issues.forEach(System.out::println);
            Assert.assertEquals(issues.size(), 8);
        }
    }
}
