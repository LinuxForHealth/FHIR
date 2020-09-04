/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countWarnings;

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
    public void testBBValidation1() throws Exception {
        try (InputStream in = BBValidationTest.class.getClassLoader().getResourceAsStream("JSON/eob-no-identifier-asserted.json")) {
            ExplanationOfBenefit explanationOfBenefit = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(explanationOfBenefit);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 1);
            Assert.assertEquals(countWarnings(issues), 11);
        }
    }

    @Test
    public void testBBValidation2() throws Exception {
        try (InputStream in = BBValidationTest.class.getClassLoader().getResourceAsStream("JSON/eob-no-identifier-not-asserted.json")) {
            ExplanationOfBenefit explanationOfBenefit = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(explanationOfBenefit);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
            Assert.assertEquals(countWarnings(issues), 1);
        }
    }

    @Test
    public void testBBValidation3() throws Exception {
        try (InputStream in = BBValidationTest.class.getClassLoader().getResourceAsStream("XML/eob-no-identifier-asserted.xml")) {
            ExplanationOfBenefit explanationOfBenefit = FHIRParser.parser(Format.XML).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(explanationOfBenefit);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 1);
            Assert.assertEquals(countWarnings(issues), 11);
        }
    }

    @Test
    public void testBBValidation4() throws Exception {
        try (InputStream in = BBValidationTest.class.getClassLoader().getResourceAsStream("XML/eob-no-identifier-not-asserted.xml")) {
            ExplanationOfBenefit explanationOfBenefit = FHIRParser.parser(Format.XML).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(explanationOfBenefit);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
            Assert.assertEquals(countWarnings(issues), 1);
        }
    }
}
